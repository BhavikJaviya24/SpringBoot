# 08-ID-Generation-SEQUENCE

A continuation of the `SpringDataJPA` series, demonstrating JPA's **`SEQUENCE` primary key generation strategy** — using a database sequence plus Hibernate's `allocationSize` optimization to generate IDs efficiently for bulk inserts.

## Overview

Project 07 used `IDENTITY`, which forces each insert to happen immediately (no batching) because the database only reveals the generated ID after the row is written. This project switches to `SEQUENCE`, which lets Hibernate ask the database for a *block* of future IDs at once and hand them out to entities locally — enabling genuine batch inserts. `Runner` demonstrates this by saving 5 students in a single `saveAll(...)` call.

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
08-ID-Generation-SEQUENCE/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java                  # entry point (just boots Spring)
    │   │   ├── entity/
    │   │   │   ├── Gender.java                     # MALE/FEMALE/OTHER enum
    │   │   │   └── Student.java                     # @Entity, SEQUENCE-generated @Id
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

## What it demonstrates

### `Student` — sequence-backed ID generation

```java
@Id
@SequenceGenerator(name = "rno_gen",        // app-side generator name
    sequenceName = "student_seq2",           // db-side sequence name
    allocationSize = 5)                      // hibernate will generate 5 values, at db side 'increment by' must be 5 or more
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rno_gen")
@Column(name = "rno")
private Integer rno;
```
- `@SequenceGenerator` names an application-side generator (`rno_gen`) and points it at a database sequence (`student_seq2`).
- `allocationSize = 5` tells Hibernate: "each time you hit the database sequence, treat the value you get back as covering a block of 5 IDs" — so Hibernate only needs to query the sequence once per 5 saves, not once per save. For this to line up correctly, the sequence's own `INCREMENT BY` on the database side needs to be `5` (or higher) to match.
- A commented-out single-record version above it (`allocationSize = 1`, pointing at a different sequence `student_seq`) is kept as a reference for the simpler, one-ID-per-call case, before switching to the batching version actually in use.

### The block-allocation comment, explained

```java
/*
 batch    |  INCREMENT BY = 5  | allocationSize = 5
-----------------------------------------------------
 1st batch |        101         |         97
           |                    |         98
           |                    |         99
           |                    |         100
           |                    |         101
-----------------------------------------------------
 2nd batch |        106         |         102
           |                    |         103
           |                    |         104
           |                    |         105
           |                    |         106
-----------------------------------------------------
 */
```

This table captures a genuinely counter-intuitive (and commonly misunderstood) part of how Hibernate's default sequence optimizer works:

- The **"INCREMENT BY = 5"** column is the raw value the database sequence itself returns each time Hibernate calls `nextval` on it — first call returns `101`, second call returns `106`, and so on, jumping by 5 each time (matching the sequence's `INCREMENT BY 5`).
- The **"allocationSize = 5"** column is the actual set of `rno` values Hibernate assigns to entities for that batch — and it is **not** simply "101 to 105" as you might expect from a single `nextval() = 101`. Instead, Hibernate's default "hilo"-style optimizer treats the number returned by the sequence as the **top** of a 5-wide block and works *backwards*: for a raw value of `101` with a block size of 5, the usable IDs are `97, 98, 99, 100, 101` (i.e. `101 - 5 + 1` through `101`).
- The same pattern repeats for the second batch: the sequence call returns `106`, and Hibernate hands out `102, 103, 104, 105, 106` from that.
- **Why this matters:** if you only look at the database sequence's current value, you'll see it jump `101 → 106 → 111 → ...`, which looks like large gaps — but those "gaps" are actually being filled locally by Hibernate handing out the preceding 4 IDs from its in-memory block, without hitting the database again. Confusing this behavior (expecting sequence value `101` to mean the *first* ID in that batch, rather than the *last*) is a classic Hibernate `SEQUENCE` + `allocationSize` gotcha.
- The practical upshot: as long as the database's `INCREMENT BY` matches (or exceeds) `allocationSize`, every ID that gets handed out is still unique and gap-free within normal operation — the numbering just isn't in the order a database call alone would suggest.

### `Runner` — exercising batch insert
```java
List<Student> savedStudents = studentService.saveAllStudents(List.of(s1, s2, s3, s4, s5));
StudentUtil.printStudents(savedStudents);
```
Builds 5 `Student` objects (no `rno` set on any of them) and saves them all in one `saveAll(...)` call. Because `allocationSize` matches the batch size here, this should trigger exactly one round-trip to the sequence, with Hibernate handing out all 5 IDs from the resulting block. A single-student save block is kept above it, commented out, as a simpler alternative demo.

## Database setup

Create Sequence at Database side 
```bash
create sequence student_seq2 start with 101 increment by 5;
```
## Running the project

```bash
./mvnw spring-boot:run
```

By default, this inserts 5 students in one batch (via `saveAllStudents`) and prints each one back with its generated `rno`. Turn on `spring.jpa.show-sql=true` (already enabled) to watch the console for how many `INSERT` statements and sequence calls actually run for this batch.

## Key takeaways

- `GenerationType.SEQUENCE` lets Hibernate pre-fetch a block of IDs from a database sequence, enabling real JDBC batching of inserts — unlike `IDENTITY`, which forces one insert per ID.
- `allocationSize` controls how large that block is, and should match the sequence's own `INCREMENT BY` on the database side.
- Hibernate's default optimizer treats the value returned by the sequence as the **top** of the allocated block, not the bottom — so don't be surprised if the IDs assigned to your entities are *lower* than the raw sequence value you'd see if you queried it directly.
