# 05-Query-Annotation

A direct continuation of the `SpringDataJPA` series, demonstrating Spring Data's **`@Query` annotation** — writing custom JPQL and native SQL queries by hand (positional params, named params, and scalar/column projections) for cases the derived-method-name approach from project 04 can't express.

## Overview

Same `Student`/`Gender` domain and layered architecture as projects 03 and 04, but the repository now moves past method-name query derivation and shows how to write explicit queries with `@Query` — both JPQL (entity-based, database-agnostic) and native SQL (raw, database-specific) — including range filters with named parameters and a query that returns only a subset of columns instead of full `Student` objects.

## Tech Stack

| Component | Version / Detail |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.1 |
| Spring Data JPA | `spring-boot-starter-data-jpa` (Hibernate under the hood) |
| Lombok | `@NoArgsConstructor`, `@AllArgsConstructor`, `@Data` on the entity |
| Database | PostgreSQL (`postgresql` JDBC driver) |
| Build tool | Maven (with Maven Wrapper) |

## Project Structure

```
05-Query-Annotation/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java                  # entry point (just boots Spring)
    │   │   ├── entity/
    │   │   │   ├── Gender.java                     # MALE/FEMALE/OTHER enum
    │   │   │   └── Student.java                     # @Entity, Lombok-powered
    │   │   ├── repository/
    │   │   │   └── StudentRepository.java             # CrudRepository + custom @Query methods
    │   │   ├── service/
    │   │   │   ├── StudentService.java                 # service interface
    │   │   │   └── StudentServiceImpl.java               # delegates to the @Query methods
    │   │   ├── runners/
    │   │   │   └── Runner.java                            # ApplicationRunner: demo calls
    │   │   └── util/
    │   │       └── StudentUtil.java                         # console print helper
    │   └── resources/
    │       └── application.properties                      # datasource + Hibernate config
    └── test/
        └── java/com/bhavik/ApplicationTests.java
```

## What it demonstrates

### `StudentRepository` — hand-written queries via `@Query`

**JPQL vs. native SQL** — two ways to fetch every student:
```java
@Query("select s from Student s")
List<Student> findAllStudent();       // JPQL — entity-based, database-agnostic

@Query(value = "select * from student", nativeQuery = true)
List<Student> giveAllStudent();       // native SQL — raw, database-specific
```
The code comments spell out the trade-off directly: reach for native SQL only when JPQL can't express what you need, since a native query is tied to the specific database dialect and would need to be rewritten by hand if the project ever switched databases — a real cost on large projects with many tables.

**Positional vs. named parameters** — a percentage-range filter:
```java
//@Query("select s from Student s where s.per>=?1 and s.per<=?2")
@Query("select s from Student s where s.per>=:start and s.per<=:end")
List<Student> findPercentageRange(@Param("start") Double start, @Param("end") Double end);
```
The commented-out line shows the positional-parameter form (`?1`, `?2`, matched by argument order); the active version uses named parameters (`:start`, `:end`) tied to the arguments via `@Param(...)` — generally the more readable and less error-prone option, and the same syntax works for native SQL queries too.

**Scalar projection** — selecting specific columns instead of whole entities:
```java
@Query("select s.gender, s.name, s.per from Student s where s.per>:one and s.gender=:two")
List<Object[]> findGenderNamePercentage(@Param("one") Double per, @Param("two") Gender gender);
```
Since the query selects individual columns (`gender`, `name`, `per`) rather than the full `Student` entity, the return type changes from `List<Student>` to `List<Object[]>` — each array holding one row's selected column values in the order they appear in the `select` clause.

### `StudentServiceImpl` — wiring
Each service method delegates straight to the matching repository query: `fetchAllStudent()` → `giveAllStudent()` (the native-SQL variant), `fetchPercentageRange(...)` → `findPercentageRange(...)`, `fetchGenderNamePer(...)` → `findGenderNamePercentage(...)`.

> **Note:** `fetchAllStudent()` currently calls `giveAllStudent()` (native SQL), not the JPQL `findAllStudent()` method also declared on the repository — so `findAllStudent()` is present but unused. Worth knowing if you're expecting the JPQL path to run.

### `Runner` — exercising the scalar projection
```java
List<Object[]> students = studentService.fetchGenderNamePer(60.0, Gender.MALE);
for (Object[] student : students) {
    System.out.println("Name : " + student[1]);
    System.out.println("Percentage : " + student[2]);
    System.out.println("Gender : " + student[0]);
}
```
By default only this call is active; the full-list and percentage-range calls are present above it, commented out, as alternative demos to try.

## Database setup

Same as projects 03/04 — a PostgreSQL database `sbai02`, with Hibernate managing the `student` table via `ddl-auto=update`:
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

By default this prints the name, percentage, and gender of every **male student with `per > 60.0`**, via the scalar-projection query. Uncomment the other blocks in `Runner.java` to try the full-list or percentage-range queries instead.

## Key takeaways

- `@Query` lets you write JPQL (portable, entity-oriented) or native SQL (`nativeQuery = true`, dialect-specific) directly on a repository method, for cases derived method names can't express cleanly.
- Prefer JPQL over native SQL unless you specifically need database-specific syntax — native queries create a maintenance cost if you ever change databases.
- Named parameters (`:name` + `@Param("name")`) are generally clearer than positional ones (`?1`, `?2`), especially as a query grows more parameters.
- Selecting specific columns (scalar projection) requires `List<Object[]>` instead of `List<Entity>`, since the result rows no longer map to a full entity.
