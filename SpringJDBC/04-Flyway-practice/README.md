# 04-Flyway-practice

A Spring Boot project demonstrating database schema versioning with **Flyway** against PostgreSQL — no application logic beyond letting Flyway run its migrations on startup.

## Overview

The final project in this `SpringJDBC` series shifts focus from writing queries to managing schema evolution. Three sequential SQL migration scripts create a `student` table and then incrementally add columns to it, with Flyway tracking and applying them automatically each time the app starts.

## Tech Stack

| Component | Version / Detail |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.0 |
| Spring JDBC | `spring-boot-starter-jdbc` |
| Migrations | Flyway (`spring-boot-starter-flyway`, `flyway-database-postgresql`) |
| Database | PostgreSQL (`postgresql` JDBC driver) |
| Build tool | Maven (with Maven Wrapper) |

## Project Structure

```
04-Flyway-practice/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   └── Application.java                              # entry point (no custom logic)
    │   └── resources/
    │       ├── application.properties                         # datasource + Flyway config
    │       └── db/migration/
    │           ├── V1__Create_student_table.sql                # creates & seeds `student`
    │           ├── V2__Add_percentage_column.sql                # adds `percentage` column
    │           └── V3__Add_email_and_phone_number_columns.sql   # adds `email`, `phno` columns
    └── test/
        └── java/com/bhavik/ApplicationTests.java
```

## What it demonstrates

### Flyway configuration
```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=0
```
- Migrations live under `src/main/resources/db/migration` and are picked up from the classpath automatically.
- `baseline-on-migrate=true` with `baseline-version=0` lets Flyway adopt an already-existing (or empty) database as version `0` and apply all subsequent migrations from there, rather than failing on a non-empty, non-Flyway-tracked schema.

### Migration scripts — versioned, one change per file

| File | Effect |
|---|---|
| `V1__Create_student_table.sql` | Creates `student(id, name, city)` and seeds 4 rows (`101`–`104`, Pune/Mumbai/Nashik) |
| `V2__Add_percentage_column.sql` | Adds a `percentage NUMERIC(5,2)` column |
| `V3__Add_email_and_phone_number_columns.sql` | Adds `email VARCHAR(100)` and `phno VARCHAR(15)` columns |

Flyway's naming convention (`V<version>__<description>.sql`) is what lets it detect, order, and track which migrations have already run — each one executes exactly once, in order, and is recorded in Flyway's schema history table.

### `Application`
```java
public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
}
```
Deliberately minimal — there's no repository or CRUD code here. The point of the project is what happens *before* `main` even finishes: Flyway runs the pending migrations against the configured datasource as part of application startup.

## Database setup

Unlike the earlier projects, you do **not** need to manually create the `student` table — Flyway does it for you via `V1`. You only need an empty (or already-baselined) PostgreSQL database named `sbai02` reachable with:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sbai02
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the project

```bash
./mvnw spring-boot:run
```

On startup, Flyway will:
1. Create its schema history table (if not already present).
2. Apply `V1`, `V2`, `V3` in order (if not already applied).
3. Leave the `student` table with columns: `id, name, city, percentage, email, phno`, seeded with the 4 rows from `V1`.

Re-running the app is safe — Flyway skips migrations it has already recorded as applied.

## Key takeaways

- Flyway migrations are plain, ordered SQL files named `V<version>__<description>.sql` — no annotations or Java DSL required.
- `baseline-on-migrate` + `baseline-version` control how Flyway treats a database that already has data/schema before Flyway starts managing it.
- Splitting schema changes into small, sequential files (create table → add column → add more columns) is the standard way to evolve a schema over time while keeping a full, replayable history.
