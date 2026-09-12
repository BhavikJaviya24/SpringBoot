# 09-ID-Generation-TABLE

The final entry in the `SpringDataJPA` ID-generation trio (`IDENTITY` → `SEQUENCE` → this one), exploring the remaining JPA `@GeneratedValue` strategies — `TABLE`, `AUTO`, and `UUID` — with three alternative approaches kept side by side in `Student.java` for comparison, and `UUID` left as the one actually active.

## Overview

Despite the folder name, this project's `Student.java` actually walks through **three** different ID-generation strategies as commented-in/commented-out blocks: `TABLE` (commented out), `AUTO` (commented out, with Hibernate's generated DDL captured as a comment), and `UUID` (active). Only the `UUID` version is live, so by default this project generates `rno` as a random UUID rather than a numeric ID — the other two are left in place purely for reference and comparison.

## `GenerationType` strategies explained

### `TABLE`
```java
@Id
@TableGenerator(name = "rno_gen",
        table = "id_generator",
        pkColumnName = "gen_name",
        valueColumnName = "gen_value",
        pkColumnValue = "student_id",
        allocationSize = 1
)
@GeneratedValue(strategy = GenerationType.TABLE, generator = "rno_gen")
```
`TABLE` generates IDs using an ordinary database **table** as a counter, instead of a native sequence or auto-increment column. Hibernate creates (or expects) a generic table — here named `id_generator`, with a "name" column (`gen_name`) and a "value" column (`gen_value`) — and each entity type gets its own row in it, identified by `pkColumnValue` (here `"student_id"`). To get the next ID, Hibernate reads the current value for that row, increments it, and writes it back (using row-level locking to stay safe under concurrent inserts).
- **Why it exists:** it's the most portable strategy — it works identically on every database, even ones with no native sequence or auto-increment support, since it's just plain rows in a plain table.
- **Why it's rarely the first choice:** every ID generation requires a read-then-update on that shared table, which is slower and more contention-prone than a native sequence (`SEQUENCE`) or auto-increment column (`IDENTITY`). It's typically used only for portability across databases that don't all support sequences.

### `AUTO`
```java
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
@Column(name="rno")
private Integer rno;
/*
 for AUTO, it uses SEQUENCE for PostgreSQL

Hibernate:
    create table student (...)

Hibernate:
    create sequence student_seq start with 1 increment by 50
 */
```
`AUTO` tells Hibernate "you decide" — it picks whichever generation strategy best fits the target database dialect, based on what that database supports natively. The comment captures exactly what Hibernate chose for PostgreSQL in this case: it fell back to `SEQUENCE`, auto-creating a sequence named `student_seq` starting at `1` and incrementing by `50` (Hibernate's own default block size when you don't specify one yourself). On a database without sequence support, `AUTO` might instead resolve to `TABLE` or `IDENTITY`.
- **Why it exists:** convenience — you don't have to know or care which strategy a given database prefers.
- **Trade-off:** less predictable/explicit than choosing a strategy yourself, and the exact behavior (including that default increment-by-50 block size) can be a surprise if you're not expecting it — as this comment itself is pointing out.

### `UUID` — the active strategy
```java
@Id
@GeneratedValue(strategy = GenerationType.UUID)
@Column(name="rno")
private UUID rno;
/*
Hibernate:
    create table student (
        rno uuid not null,
        ...
        primary key (rno)
    )

notice rno column had datatype uuid.
uuid rno = e63160ab-03e5-4833-a93d-989243dd33aa -> RNOs are generated like this (16-Byte binary data)
*/
```
`UUID` generates a random **universally unique identifier** (a 128-bit / 16-byte value, conventionally displayed as a 36-character hex string like `e63160ab-03e5-4833-a93d-989243dd33aa`) entirely in the application, with no database round-trip needed to obtain it. Hibernate maps the `@Id` field's Java type to a native `uuid` column in PostgreSQL (as the comment shows), rather than an integer.
- **Why it's attractive:** IDs can be generated before the entity is even saved (unlike `IDENTITY`), there's no shared counter or sequence to coordinate across databases or services, and UUIDs are effectively globally unique — handy in distributed systems, offline-first apps, or anywhere merging data from multiple sources could cause numeric-ID collisions.
- **Trade-offs:** UUIDs are larger (16 bytes vs. 4 for an `int`), don't sort in a humanly meaningful/insertion order (which can hurt index locality/performance on very large tables compared to sequential integer keys), and are less convenient to read, type, or debug by hand than a plain number.

## Why three strategies are stacked in one file

The two unused strategies (`TABLE`, `AUTO`) are kept as commented-out code directly above the active `UUID` block, functioning as inline documentation/comparison — each labeled `// GenerationType.TABLE` or explained with a captured snippet of the DDL Hibernate would generate for it. This makes the file a quick reference for "what would this look like with a different strategy" without needing to check out an earlier commit or a different project.
## Tech Stack

| Component | Version / Detail |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.1 |
| Spring Data JPA | `spring-boot-starter-data-jpa` (Hibernate under the hood) |
| Lombok | `@NoArgsConstructor`, `@Data` on the entity |
| Database | PostgreSQL (`postgresql` JDBC driver) |
| Build tool | Maven (with Maven Wrapper) |

## Project Structure

```
09-ID-Generation-TABLE/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java                  # entry point (just boots Spring)
    │   │   ├── entity/
    │   │   │   ├── Gender.java                     # MALE/FEMALE/OTHER enum
    │   │   │   └── Student.java                     # @Entity — TABLE/AUTO/UUID compared, UUID active
    │   │   ├── repository/
    │   │   │   └── StudentRepository.java             # plain CrudRepository
    │   │   ├── service/
    │   │   │   ├── StudentService.java                 # save, saveAllStudents, fetchAll
    │   │   │   └── StudentServiceImpl.java               # delegates to the repository
    │   │   ├── runners/
    │   │   │   └── Runner.java                            # ApplicationRunner: bulk-save demo
    │   │   └── util/
    │   │       └── StudentUtil.java                         # console print helper
    │   └── resources/
    │       └── application.properties                      # datasource + Hibernate config
    └── test/
        └── java/com/bhavik/ApplicationTests.java
```

## `Runner` — saving with UUID keys

```java
List<Student> savedStudents = studentService.saveAllStudents(List.of(s1, s2, s3, s4, s5));
StudentUtil.printStudents(savedStudents);
```
Builds and saves 5 students in one batch (a single-student version is kept above it, commented out). Since each `rno` is a locally-generated UUID rather than a database-assigned counter value, all 5 IDs are available immediately, with no dependency on sequence or identity behavior at all.

## Database setup

A PostgreSQL database `sbai02`, with Hibernate managing the `student` table via `ddl-auto=update` — with the active `UUID` strategy, the `rno` column will be created with PostgreSQL's native `uuid` type rather than an integer:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sbai02
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=update
```

## Running the project

```bash
./mvnw spring-boot:run
```

By default this inserts 5 students in one batch and prints each back with its generated UUID `rno`.

## Key takeaways

- `TABLE` generates IDs from a plain database table acting as a shared counter — the most portable option, but the slowest due to per-ID row locking.
- `AUTO` lets Hibernate choose a strategy based on the database dialect — convenient, but less predictable (e.g. it silently created a `SEQUENCE` with a default block size of 50 for PostgreSQL here).
- `UUID` generates a random, globally-unique 128-bit identifier entirely on the application side — no database round-trip needed before save, at the cost of larger, non-sequential keys.
- Keep the repository's declared ID generic type in sync with whichever `@Id` type is actually active in the entity — copying a repository interface across strategy changes (as flagged above) is an easy way to introduce a type mismatch.
