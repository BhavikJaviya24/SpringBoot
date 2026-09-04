# 06-Named-Query

A continuation of the `SpringDataJPA` series, demonstrating JPA's **`@NamedQuery`/`@NamedQueries`** — defining JPQL queries directly on the `@Entity` class itself (rather than on the repository method, as in project 05's `@Query`), including a modifying update query and a `GROUP BY` aggregate.

## Overview

Same `Student`/`Gender` domain and layered architecture as the earlier `CrudRepository`/`@Query` projects, but here the JPQL lives on `Student` via `@NamedQuery`/`@NamedQueries`, and the repository methods just need to be *named to match* — no `@Query` string on the repository interface at all. The set of queries covers a filtered `SELECT`, a bulk `UPDATE` (with `@Modifying` + `@Transactional`), a two-column projection, and a `GROUP BY ... count(...)` aggregate.

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
06-Named-Query/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java                  # entry point (just boots Spring)
    │   │   ├── entity/
    │   │   │   ├── Gender.java                     # MALE/FEMALE/OTHER enum
    │   │   │   └── Student.java                     # @Entity + @NamedQuery/@NamedQueries
    │   │   ├── repository/
    │   │   │   └── StudentRepository.java             # method names match the named queries
    │   │   ├── service/
    │   │   │   ├── StudentService.java                 # service interface
    │   │   │   └── StudentServiceImpl.java               # delegates to the repository
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

### `Student` — JPQL defined on the entity

```java
@NamedQuery(name = "Student.findAllPerAndGender",
    query = "select s from Student s where per>=:per and gender=:gender")
@NamedQueries(value = {
    @NamedQuery(name = "Student.updateName",
        query = "update Student s set s.name=:name where s.rno=:rno"),
    @NamedQuery(name = "Student.findByGenderAndName",
        query = "select s.rno, s.name, s.gender from Student s where s.name=:name and s.gender=:gender"),
    @NamedQuery(name = "Student.findGenderCount",
        query = "select s.gender, count(s) from Student s group by s.gender")
})
```
- A single query uses the bare `@NamedQuery`; multiple queries on the same entity are grouped under `@NamedQueries`.
- Each query's `name` follows the `EntityName.methodStyleName` convention (`Student.findAllPerAndGender`, etc.) — this naming convention is what lets Spring Data JPA automatically wire a matching repository method to the named query, with **no `@Query` annotation needed on the repository at all**.

### `StudentRepository` — methods that just need the right name

```java
public interface StudentRepository extends CrudRepository<Student, Integer> {
    List<Student> findAllPerAndGender(@Param("per") Double per, @Param("gender") Gender gender);

    @Modifying
    @Transactional
    void updateName(@Param("name") String name, @Param("rno") Integer rno);

    List<Object[]> findByGenderAndName(@Param("name") String name, @Param("gender") Gender gender);

    List<Object[]> findGenderCount();
}
```
- Spring Data matches each method to its named query by `EntityName.methodName` — e.g. `findAllPerAndGender` resolves to `Student.findAllPerAndGender` automatically.
- `updateName` is a **modifying** query (an `UPDATE`, not a `SELECT`), so it needs both `@Modifying` (to tell Spring Data this isn't a read) and `@Transactional` (an update must run inside a transaction) — the same rules apply as for a hand-written `@Modifying @Query`.
- `findByGenderAndName` and `findGenderCount` both select specific columns/aggregates rather than full entities, so they return `List<Object[]>` — same scalar-projection pattern seen in project 05.

### `StudentServiceImpl` — thin pass-through
Each service method (`fetchAllPerAndGender`, `modifyName`, `fetchByGenderAndName`, `fetchGenderCount`) just delegates to the matching repository method.

### `Runner` — exercising the `GROUP BY` aggregate
```java
List<Object[]> counts = studentService.fetchGenderCount();
for (Object[] object : counts) {
    System.out.println("Gender :: " + object[0]);
    System.out.println("Count :: " + object[1]);
}
```
By default, only the gender-count aggregate runs. The other three demos (filter by percentage/gender, update a name, filter by name/gender) sit above it in a commented-out block, ready to uncomment individually.

## Database setup

Same as the earlier JPA projects — a PostgreSQL database `sbai02`, with Hibernate managing the `student` table via `ddl-auto=update`:
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

By default this prints a **count of students grouped by gender**. Uncomment the other blocks in `Runner.java` to try the percentage/gender filter, the name update, or the name/gender filter instead.

## Key takeaways

- `@NamedQuery`/`@NamedQueries` put JPQL on the entity class rather than the repository, and Spring Data auto-wires a repository method to a matching named query purely by naming convention (`EntityName.methodName`) — no `@Query` string required on the method.
- A query that modifies data (`UPDATE`/`DELETE`) needs `@Modifying` + `@Transactional` on the repository method, whether it comes from a named query or an inline `@Query`.
- `GROUP BY` with an aggregate function (`count(...)`) is just more JPQL — and like any multi-column select, it returns `List<Object[]>` rather than a list of entities.
- Choosing between named queries (project 06) and repository-level `@Query` (project 05) is largely a style/organization preference — named queries centralize JPQL on the entity, while `@Query` keeps it next to the method that uses it.
