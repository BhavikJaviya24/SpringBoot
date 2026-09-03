# 04-CrudRepository-Custom-Methods

A direct continuation of `03-CrudRepository-Jpa-app`, focused on **Spring Data JPA's derived query methods** — writing repository method signatures whose names alone generate the SQL, no `@Query` or method body required.

## Overview

Same `Student`/`Gender` domain and layered architecture as project 03, but the `StudentRepository` interface now declares a set of custom finder methods that Spring Data parses from the method name itself (query derivation), covering equality, ranges, before/after comparisons, string prefix matching, and top-N sorted results.

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
04-CrudRepository-Custom-Methods/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java                  # entry point (just boots Spring)
    │   │   ├── entity/
    │   │   │   ├── Gender.java                     # MALE/FEMALE/OTHER enum (same as project 03)
    │   │   │   └── Student.java                     # @Entity, Lombok-powered (same as project 03)
    │   │   ├── repository/
    │   │   │   └── StudentRepository.java             # CrudRepository + derived query methods
    │   │   ├── service/
    │   │   │   ├── StudentService.java                 # service interface
    │   │   │   └── StudentServiceImpl.java               # delegates to the derived queries
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

### `StudentRepository` — derived (method-name) queries

```java
public interface StudentRepository extends CrudRepository<Student, Integer> {
    List<Student> findByGender(Gender gender);
    List<Student> getByBirthDate(LocalDate birthDate);
    List<Student> readByPerGreaterThanEqual(Double per);
    List<Student> findByPerBetween(Double startPer, Double endPer);     // inclusive
    List<Student> findByBirthDateAfter(LocalDate date);
    List<Student> findByBirthDateBefore(LocalDate date);
    List<Student> findByPerBefore(Double per);
    List<Student> findByPerAfter(Double per);
    List<Student> findByNameStartingWithIgnoreCase(String name);
    List<Student> findTop3ByOrderByPerDesc();
}
```

Key points called out directly in the code comments:
- The method prefix can be `find`, `get`, or `read` interchangeably — Spring Data recognizes all three (`readByPerGreaterThanEqual` mixed in alongside `findBy...` methods on purpose, to show they're equivalent).
- `Between` is inclusive of both bounds.
- `findTop3ByOrderByPerDesc()` combines two derivation features: `Top3` limits the result to 3 rows, and `OrderByPerDesc` sorts by `per` descending — together giving you the top 3 highest-percentage students without writing any SQL or JPQL.

Spring parses each method name at startup, matches it against the entity's field names (`gender`, `birthDate`, `per`, `name`), and generates the corresponding JPQL/SQL automatically.

### `StudentService` / `StudentServiceImpl`
Exposes one service method per repository query (`fetchByGender`, `fetchByBirthDate`, `fetchByDistinction` — hardcoded to `per >= 74.00` — `fetchByPerBetween`, `fetchByPerAfter/Before`, `fetchByDateAfter/Before`, `fetchByNameStartingWith`, `fetchTop3ByPer`), each simply delegating to the matching repository method.

### `Runner` — demo calls, one left active
`Runner.java` contains a large commented-out block exercising every derived query (by gender, by birth date, distinction students, percentage range, date/percentage before-and-after, name prefix search) — useful as a menu of examples to uncomment individually. By default, only the "toppers" call runs:
```java
List<Student> toppers = studentService.fetchTop3ByPer();
StudentUtil.printStudents(toppers);
```

## Database setup

Same as project 03 — a PostgreSQL database `sbai02`, with Hibernate managing the `student` table automatically via `ddl-auto=update`:
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

By default this prints the **top 3 students by percentage** (descending). Uncomment any of the other blocks in `Runner.java` to try the remaining derived queries (by gender, birth date, percentage range, name prefix, etc.) against your seeded data.

## Key takeaways

- Spring Data JPA can generate full query implementations purely from a method's name (`find`/`get`/`read` + `By` + field name + optional keyword like `Between`, `After`, `Before`, `GreaterThanEqual`, `StartingWith`, `IgnoreCase`) — no `@Query` annotation needed for common cases.
- `Top<N>` combined with `OrderBy<Field><Asc|Desc>` gives you limited, sorted results in one derived method.
- This approach trades a bit of "magic" (you have to know the keyword vocabulary) for zero boilerplate — for anything the naming convention can't express, you'd fall back to `@Query` with JPQL/native SQL.
