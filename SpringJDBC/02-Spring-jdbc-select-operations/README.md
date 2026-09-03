# 02-Spring-jdbc-select-operations

A follow-up to `01-jdbc-app`, focused specifically on **SELECT** operations with Spring's `JdbcTemplate` — fetching a single row, all rows, and filtered rows — on top of the same CRUD foundation.

## Overview

This project builds on the same `Student` domain as `01-jdbc-app` but shifts the emphasis to read operations: `queryForMap` for a single record and `queryForList` for multi-row results, including a WHERE-clause filter. The `main` method walks through all three read operations against sample data, with the earlier insert/update/delete calls kept as commented-out reference code.

## Tech Stack

| Component | Version / Detail |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.0 |
| Spring JDBC | `spring-boot-starter-jdbc` |
| Database | PostgreSQL (`postgresql` JDBC driver) |
| Build tool | Maven (with Maven Wrapper) |

## Project Structure

```
02-Spring-jdbc-select-operations/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java              # entry point, runs SELECT demos
    │   │   ├── model/
    │   │   │   └── Student.java               # POJO: rno, name, city, per
    │   │   └── repository/
    │   │       └── StudentRepository.java     # JdbcTemplate CRUD + SELECT methods
    │   └── resources/
    │       └── application.properties         # PostgreSQL datasource config
    └── test/
        └── java/com/bhavik/ApplicationTests.java
```

## What it demonstrates

### `StudentRepository` — read operations

```java
public Map<String, Object> findById(int rno) {
    String sql = "select * from student where rno = ?";
    return jdbcTemplate.queryForMap(sql, rno);
}

public List<Map<String, Object>> findAll() {
    String sql = "select * from student";
    return jdbcTemplate.queryForList(sql);
}

public List<Map<String, Object>> findByCity(String city) {
    String sql = "select * from student where city = ?";
    return jdbcTemplate.queryForList(sql, city);
}
```

- **`findById`** uses `queryForMap`, which is convenient for a query expected to return exactly one row — it maps each column name to its value in a `Map<String, Object>`.
- **`findAll`** uses `queryForList`, returning a `List<Map<String, Object>>` — one map per row — without needing a dedicated row mapper class.
- **`findByCity`** shows the same pattern with a parameterized `WHERE` clause.

The write operations (`save`, `delete`, `update`) from `01-jdbc-app` are still present on the repository for reference/reuse.

### `Application` — exercising all three reads
```java
Map<String, Object> student = studentRepository.findById(103);
List<Map<String, Object>> studentList = studentRepository.findAll();
List<Map<String, Object>> cityStudents = studentRepository.findByCity("Chennai");
```
Each result set is printed to the console with separator banners between them for readability. The insert/delete/update calls from the previous project are kept, commented out, as optional follow-up exercises.

## Database setup

Same as `01-jdbc-app` — a PostgreSQL database `sbai02` with a `student` table:
```sql
CREATE TABLE student (
    rno  INT PRIMARY KEY,
    name VARCHAR(50),
    per  NUMERIC(5,2),
    city VARCHAR(50)
);
```
Credentials are read from `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sbai02
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the project

```bash
./mvnw spring-boot:run
```

By default this prints:
1. The single student with `rno = 103` (via `queryForMap`).
2. Every row in the `student` table (via `queryForList`).
3. Every student whose city is `"Chennai"` (via a parameterized `queryForList`).

## Key takeaways

- `queryForMap` is the go-to for a single-row result; it throws if zero or more than one row comes back.
- `queryForList` is the go-to for multi-row results when you don't need (or don't yet want) a strongly-typed row mapper — each row becomes a `Map<String, Object>`.
- Parameterized filters (`WHERE city = ?`) work the same way for reads as for writes — pass the parameter as a trailing argument to the `JdbcTemplate` method.
