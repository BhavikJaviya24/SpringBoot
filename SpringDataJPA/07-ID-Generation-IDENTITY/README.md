# 07-ID-Generation-IDENTITY

A continuation of the `SpringDataJPA` series, demonstrating JPA's **`IDENTITY` primary key generation strategy** — letting the database auto-generate `rno` on insert instead of the application supplying it.

## Overview

Earlier projects in this series (03–06) required you to set a `Student`'s `rno` yourself before saving. Here, `rno` is annotated `@GeneratedValue(strategy = GenerationType.IDENTITY)`, so the database assigns the primary key automatically on every insert — the app only needs to build a `Student` *without* an `rno` and save it.

## What is `GenerationType.IDENTITY`?

`IDENTITY` is one of JPA's four ID-generation strategies (`AUTO`, `IDENTITY`, `SEQUENCE`, `TABLE`). It delegates primary-key generation entirely to the database's native auto-increment column feature — `SERIAL`/`BIGSERIAL`/`GENERATED ... AS IDENTITY` in PostgreSQL, `AUTO_INCREMENT` in MySQL, `IDENTITY` columns in SQL Server, and so on.

How it behaves in practice:
- **The database owns the counter.** Every time a row is inserted, the database itself picks the next value for that column — Hibernate never has to compute or reserve an ID up front.
- **The `INSERT` must happen immediately to get the ID back.** Because the ID isn't known until the database generates it, Hibernate can't batch up multiple inserts and send them together — it has to execute each `INSERT` right away and read the generated key back from the database before it can return a fully-populated entity to your code. This is the main downside compared to `SEQUENCE`: it disables JDBC batch-inserting of new entities, which can matter for bulk-insert performance.
- **Entities are never "pre-assigned" an ID.** Unlike `SEQUENCE` (which can fetch a block of future IDs from a database sequence before insert), with `IDENTITY` the entity's `@Id` field stays `null` until the row is actually persisted.
- **It's the simplest option to set up** — most databases already default a primary-key column to an auto-increment type, so `IDENTITY` typically requires no extra schema object (unlike `SEQUENCE`, which needs an explicit `CREATE SEQUENCE`).

In short: `IDENTITY` trades a bit of insert-performance flexibility for simplicity — it "just works" with the database's built-in auto-increment column, which is exactly what this project demonstrates.

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
07-ID-Generation-IDENTITY/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java                  # entry point (just boots Spring)
    │   │   ├── entity/
    │   │   │   ├── Gender.java                     # MALE/FEMALE/OTHER enum
    │   │   │   └── Student.java                     # @Entity, IDENTITY-generated @Id
    │   │   ├── repository/
    │   │   │   └── StudentRepository.java             # plain CrudRepository, no custom methods
    │   │   ├── service/
    │   │   │   ├── StudentService.java                 # save + fetchAll
    │   │   │   └── StudentServiceImpl.java               # delegates to the repository
    │   │   ├── runners/
    │   │   │   └── Runner.java                            # ApplicationRunner: save + list demo
    │   │   └── util/
    │   │       └── StudentUtil.java                         # console print helper
    │   └── resources/
    │       └── application.properties                      # datasource + Hibernate config
    └── test/
        └── java/com/bhavik/ApplicationTests.java
```

## What it demonstrates

### `Student` — database-generated primary key
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "rno")
private Long rno;
```
- `rno` changes from `Integer` (in earlier projects) to `Long` here, and is no longer set by application code — Hibernate leaves it `null` on a new `Student` and reads the database-assigned value back immediately after the `INSERT`.
- Note the entity also drops `@AllArgsConstructor` (present in earlier projects) — since `rno` is now generated rather than supplied, building a `Student` with every field pre-filled, including its ID, doesn't fit the workflow anymore; only the no-arg constructor + setters are used.

> **Worth double-checking:** `StudentRepository extends CrudRepository<Student, Integer>`, but `Student.rno` is now typed `Long`, not `Integer`. This is a type mismatch between the entity's actual `@Id` type and the repository's declared ID generic — it's likely left over from copying the repository interface from an earlier project. It may still compile depending on how it's used, but for correctness the repository should be `CrudRepository<Student, Long>`.

### `Runner` — saving without an ID
```java
Student student = new Student();
student.setName("CCC");
student.setPer(56.33);
student.setGender(Gender.FEMALE);
student.setBirthDate(LocalDate.now());

Student savedStudent = studentService.save(student);
StudentUtil.printStudents(List.of(savedStudent));
```
This save-and-print block is present but commented out by default; the active code just runs `fetchAll()` and prints every existing student. Uncomment the block above to see a new student get inserted with an `rno` value the database assigns automatically, then printed back with that generated ID populated.

## Database setup

Same as the earlier JPA projects — a PostgreSQL database `sbai02`, with Hibernate managing the `student` table via `ddl-auto=update`. Because `rno` is now `IDENTITY`-generated, Hibernate will create/alter the column as a PostgreSQL identity/auto-increment column rather than a plain integer primary key.
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

By default this prints every existing student. Uncomment the save block in `Runner.java` to insert a new student and see its database-generated `rno` in the output.

## Key takeaways

- `GenerationType.IDENTITY` delegates ID generation to the database's native auto-increment column — simplest to set up, but forces Hibernate to execute each insert immediately (no JDBC batching of inserts) since the ID isn't known until after the row is written.
- With `IDENTITY`, a new entity's `@Id` field must be left `null` (or unset) before saving — the framework, not your code, fills it in.
- Keep the repository's declared ID type in sync with the entity's actual `@Id` field type; a mismatch here (as flagged above) is a common copy-paste bug when adapting a repository from an earlier project.
