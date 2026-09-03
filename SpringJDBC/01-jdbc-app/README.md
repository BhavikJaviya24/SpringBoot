# 01-jdbc-app

A minimal Spring Boot + Spring JDBC project demonstrating raw JDBC CRUD operations against PostgreSQL using `JdbcTemplate`, without JPA/Hibernate.

## Overview

This is the first project in a `SpringJDBC` learning series. It sets up the basic building blocks — a `Student` model, a `@Repository` backed by `JdbcTemplate`, and a `main` method that exercises insert, update, and delete operations directly with SQL strings.

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
01-jdbc-app/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java              # entry point, runs sample CRUD calls
    │   │   ├── model/
    │   │   │   └── Student.java               # POJO: rno, name, city, per
    │   │   └── repository/
    │   │       └── StudentRepository.java     # JdbcTemplate-backed CRUD
    │   └── resources/
    │       └── application.properties         # PostgreSQL datasource config
    └── test/
        └── java/com/bhavik/ApplicationTests.java
```

## What it demonstrates

### `Student` — model
A plain POJO with `rno` (roll number), `name`, `city`, and `per` (percentage), plus getters/setters and a `toString()`.

### `StudentRepository` — JDBC CRUD via `JdbcTemplate`
```java
@Autowired
private JdbcTemplate jdbcTemplate;
```
- `save(Student)` — parameterized `INSERT`
- `delete(int rno)` — parameterized `DELETE`, returns rows affected
- `update(Student)` — parameterized `UPDATE`, returns rows affected

All queries use `?` placeholders passed as varargs to `jdbcTemplate.update(...)`, avoiding string concatenation / SQL injection.

### `Application` — wiring without field injection into `main`
```java
/*
@Autowired
static StudentRepository studentRepository;
static members can't be autowired.
*/
```
Since `main` is static and Spring can't autowire into static fields, the repository bean is instead pulled manually out of the `ApplicationContext`:
```java
StudentRepository studentRepository = (StudentRepository) context.getBean("studentRepository");
```
The insert and delete calls are present but commented out; only the **update** call is active by default, updating student `101`'s name to `"BBB"`.

## Database setup

The app expects a PostgreSQL database named `sbai02` with a `student` table matching the four `Student` fields (`rno`, `name`, `per`, `city`), reachable with the credentials in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sbai02
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Create the table manually before running, e.g.:
```sql
CREATE TABLE student (
    rno  INT PRIMARY KEY,
    name VARCHAR(50),
    per  NUMERIC(5,2),
    city VARCHAR(50)
);
```

## Running the project

```bash
./mvnw spring-boot:run
```

With the default code, this updates roll number `101` to name `"BBB"` and prints the number of rows affected. Uncomment the insert/delete blocks in `Application.java` to try those operations instead.

## Key takeaways

- `JdbcTemplate` is the low-level, boilerplate-light way to run SQL from Spring without a full ORM.
- Parameterized queries (`?` + varargs) are the standard way to avoid SQL injection with `JdbcTemplate`.
- You can't `@Autowired` a static field for use in `main` — instead, fetch the bean explicitly via `ApplicationContext.getBean(...)`.
