# 03-Procedure-call-plsql

A Spring JDBC project demonstrating how to call **PostgreSQL PL/pgSQL functions** from Spring using `SimpleJdbcCall`, instead of writing raw SQL for the logic.

## Overview

Where the earlier projects in this series (`01-jdbc-app`, `02-Spring-jdbc-select-operations`) run SQL directly through `JdbcTemplate`, this project delegates two pieces of logic to the database itself — a `get_grade` function and an `avg_percentage` function — and calls them from Java via Spring's `SimpleJdbcCall`.

## Tech Stack

| Component | Version / Detail |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.0 |
| Spring JDBC | `spring-boot-starter-jdbc` (`SimpleJdbcCall`) |
| Database | PostgreSQL (`postgresql` JDBC driver) |
| Build tool | Maven (with Maven Wrapper) |

## Project Structure

```
03-Procedure-call-plsql/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java              # entry point, invokes both functions
    │   │   ├── config/
    │   │   │   └── MyConfig.java              # SimpleJdbcCall bean definitions
    │   │   ├── model/
    │   │   │   └── Student.java               # POJO: rno, name, per, city
    │   │   └── repository/
    │   │       └── StudentRepository.java     # wraps the two function calls
    │   └── resources/
    │       └── application.properties         # PostgreSQL datasource config
    └── test/
        └── java/com/bhavik/ApplicationTests.java
```

## What it demonstrates

### `MyConfig` — one `SimpleJdbcCall` bean per database function

```java
@Bean
public SimpleJdbcCall getGradeCall(DataSource dataSource){
    return new SimpleJdbcCall(dataSource).withFunctionName("get_grade");
}

@Bean
public SimpleJdbcCall getPercentageCall(DataSource dataSource){
    return new SimpleJdbcCall(dataSource).withFunctionName("avg_percentage");
}
```

The key lesson called out in the code comments: a `SimpleJdbcCall` instance is bound to one function name at construction (`withFunctionName(...)`), so calling it against a *different* function later still fails. The fix is to declare a **separate bean per function** — hence two `@Bean` methods here, one for `get_grade` and one for `avg_percentage`.

### `StudentRepository` — invoking the functions

```java
@Autowired
private SimpleJdbcCall getGradeCall;

@Autowired
private SimpleJdbcCall getPercentageCall;

public Object findGrade(int rno) {
    return getGradeCall.execute(rno).get("returnvalue");
}

public Object findAverage(String city) {
    return getPercentageCall.execute(city).get("returnvalue");
}
```

- `.execute(...)` runs the underlying PL/pgSQL function with the given positional argument(s) and returns a `Map` of output parameters.
- On PostgreSQL, a function's return value is keyed as `"returnvalue"` in that map (noted in the code as differing from MySQL, where the key is `"return"`).
- A comment also flags that if a same-named function exists in another database/schema, `withCatalogName("sbai02")` would be needed to disambiguate.

### `Application` — sample calls
```java
Object grade = studentRepository.findGrade(104);
Object avg = studentRepository.findAverage("Chennai");
```
Prints the grade for student `104` and the average percentage for students in `"Chennai"`.

## Database setup

Requires a PostgreSQL database `sbai02` with:
- A `student` table (`rno`, `name`, `per`, `city`) — same shape as the earlier projects.
- Two PL/pgSQL functions that this project calls but does **not** create:
  - `get_grade(rno INT) RETURNS <type>` — presumably derives a letter/numeric grade from a student's percentage.
  - `avg_percentage(city TEXT) RETURNS <type>` — presumably returns the average percentage of students in a given city.

You'll need to create these functions yourself in `sbai02` before running the app, e.g. as a starting point:
```sql
CREATE OR REPLACE FUNCTION get_grade(rno INT)
RETURNS TEXT AS $$
DECLARE
    pct NUMERIC;
BEGIN
    SELECT per INTO pct FROM student WHERE rno = rno;
    RETURN CASE
        WHEN pct >= 75 THEN 'A'
        WHEN pct >= 60 THEN 'B'
        ELSE 'C'
    END;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION avg_percentage(city_in TEXT)
RETURNS NUMERIC AS $$
DECLARE
    avg_val NUMERIC;
BEGIN
    SELECT AVG(per) INTO avg_val FROM student WHERE city = city_in;
    RETURN avg_val;
END;
$$ LANGUAGE plpgsql;
```
(Adjust to match your actual function signatures/logic if different.)

Datasource config (`application.properties`):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sbai02
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the project

```bash
./mvnw spring-boot:run
```

Prints the grade for roll number `104` and the average percentage for `"Chennai"` students to the console.

## Key takeaways

- `SimpleJdbcCall` lets you call stored functions/procedures without hand-writing `CallableStatement` boilerplate.
- Each `SimpleJdbcCall` bean is tied to a single function name — define one bean per function you need to call.
- The output parameter key for a PostgreSQL function's return value is `"returnvalue"`, not `"return"` (that's MySQL's convention).
