# 03-CrudRepository-Jpa-app

An interactive, menu-driven Spring Boot console app demonstrating **Spring Data JPA's `CrudRepository`** — save, update, delete, and fetch operations on a `Student` entity, wired through a layered Repository → Service → Runner architecture, backed by PostgreSQL.

## Overview

This is the first "real" JPA project in the series (following the Lombok and enum warm-ups). Instead of hand-writing SQL like the earlier `SpringJDBC` projects, `Student` is mapped as a JPA `@Entity`, and all CRUD operations go through Spring Data's `CrudRepository` — no query methods need to be implemented manually. An `ApplicationRunner` presents a numbered console menu so you can exercise every operation interactively at startup.

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
03-CrudRepository-Jpa-app/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java                  # entry point (just boots Spring)
    │   │   ├── entity/
    │   │   │   ├── Gender.java                     # simple MALE/FEMALE/OTHER enum
    │   │   │   └── Student.java                     # @Entity, Lombok-powered
    │   │   ├── repository/
    │   │   │   └── StudentRepository.java            # extends CrudRepository<Student, Integer>
    │   │   ├── service/
    │   │   │   ├── StudentService.java                # service interface
    │   │   │   └── StudentServiceImpl.java             # delegates to the repository
    │   │   ├── runner/
    │   │   │   └── Runner.java                         # ApplicationRunner: console menu
    │   │   └── util/
    │   │       └── StudentUtil.java                     # console I/O helpers
    │   └── resources/
    │       └── application.properties                  # datasource + Hibernate config
    └── test/
        └── java/com/bhavik/ApplicationTests.java
```

## What it demonstrates

### `Student` — a JPA entity
```java
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "student")
public class Student {
    @Id
    @Column(name = "rno")
    private Integer rno;

    @Column(name = "name")
    private String name;

    @Column(name = "per")
    private Double per;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;
}
```
- `@Id` marks `rno` as the primary key.
- `@Enumerated(EnumType.STRING)` stores the `Gender` enum as its name (`"MALE"`/`"FEMALE"`/`"OTHER"`) in the database column, rather than its ordinal integer — the safer choice, since it survives reordering the enum constants.
- Lombok's `@Data` supplies getters/setters/`toString()`/`equals()` so the entity stays boilerplate-free.

### `StudentRepository` — zero-code CRUD
```java
@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
}
```
Just by extending `CrudRepository<Student, Integer>`, Spring Data generates implementations for `save`, `saveAll`, `findById`, `findAll`, `findAllById`, `existsById`, `delete`, `deleteById`, `deleteAll`, `deleteAllById`, etc. — no method bodies written.

### `StudentService` / `StudentServiceImpl` — a thin service layer
Wraps the repository behind an interface, adding small pieces of logic on top (e.g. `deleteStudentById` first checks `existsById` before deleting, `deleteAllStudent(List<Student>)` checks the list isn't empty).

### `Runner` — an interactive console menu
Implements `ApplicationRunner` so it runs automatically after the Spring context starts. Presents a 10-option menu covering save/update, bulk save, single/bulk/by-id delete, and single/bulk fetch, reading input via `Scanner` and delegating to `StudentService`. `StudentUtil` centralizes the console prompts (`readStudentDetails`, `readIdList`) and pretty-printing (`printStudents`).

## Database setup

Requires a PostgreSQL database `sbai02`. Unlike the raw-JDBC projects, you don't need to hand-write the `CREATE TABLE` — `spring.jpa.hibernate.ddl-auto=update` lets Hibernate create/update the `student` table to match the `@Entity` mapping automatically on startup.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sbai02
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=update
```
`spring.jpa.show-sql=true` (plus `format_sql=true`) prints every generated SQL statement to the console, formatted — useful for seeing exactly what Hibernate does for each menu operation.

## Running the project

```bash
./mvnw spring-boot:run
```

This drops you straight into the interactive `STUDENT MENU` in the console — enter a number to save, delete, or fetch students, and `0` to exit.

## Key takeaways

- `CrudRepository<T, ID>` gives you full CRUD for free — no SQL, no boilerplate implementation, just an interface declaration.
- `@Enumerated(EnumType.STRING)` is the recommended way to persist enums; it's more robust than the ordinal default.
- `ddl-auto=update` is convenient for development (Hibernate keeps the schema in sync with your entities) but isn't something you'd typically rely on in production, where controlled migrations (see the earlier `04-Flyway-practice` project) are preferred.
