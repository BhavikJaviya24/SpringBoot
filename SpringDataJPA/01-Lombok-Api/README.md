# 01-Lombok-Api

A small Spring Boot project demonstrating **Project Lombok** — using `@Data`, `@NoArgsConstructor`, and `@AllArgsConstructor` to eliminate getter/setter/constructor/`equals()`/`toString()` boilerplate on a POJO.

## Overview

This is the first project in the `SpringDataJPA` learning series, and it's not about JPA at all yet — it's a warm-up on Lombok. A single `Student` model is annotated with Lombok annotations instead of hand-written accessors, and `Application.java` exercises object creation, field access, `toString()`, and `equals()` to show what Lombok generates under the hood.

## Tech Stack

| Component | Version / Detail |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.1 |
| Lombok | `org.projectlombok:lombok` (optional, annotation-processor wired into `maven-compiler-plugin`) |
| Build tool | Maven (with Maven Wrapper) |

## Project Structure

```
01-Lombok-Api/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── Application.java     # entry point, exercises the Lombok-generated methods
    │   │   └── model/
    │   │       └── Student.java      # Lombok-annotated POJO
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/bhavik/ApplicationTests.java
```

## What it demonstrates

### `Student` — Lombok in action
```java
@NoArgsConstructor
@AllArgsConstructor
//@Setter
//@Getter
//@ToString
@Data
public class Student {
    private Integer rno;
    private String name;
    private String city;
}
```
- `@NoArgsConstructor` / `@AllArgsConstructor` generate a no-arg and a fully-parameterized constructor.
- `@Data` is a shortcut bundling `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`, and `@RequiredArgsConstructor` — which is why the individual `@Setter`/`@Getter`/`@ToString` annotations are commented out as redundant once `@Data` is present.
- `pom.xml` wires the Lombok annotation processor explicitly into both the `default-compile` and `default-testCompile` executions of `maven-compiler-plugin`, which is what makes the generated code available at compile time.

### `Application` — proving the generated code works
```java
Student student2 = new Student(101, "AAA", "Pune");   // @AllArgsConstructor
Student student3 = new Student();                      // @NoArgsConstructor
student3.setRno(102); student3.setName("BBB"); ...      // @Setter (via @Data)
System.out.println("Student 2: " + student2);           // @ToString (via @Data)

Student student4 = new Student(101, "AAA", "Pune");
student2.equals(student4);                               // @EqualsAndHashCode (via @Data)
```
The `main` method builds students three different ways (all-args constructor, no-arg + setters), prints them via the generated `toString()`, and compares two structurally-identical-but-distinct `Student` instances with `.equals()` to confirm Lombok's generated equality check compares field values rather than object references.

## Running the project

```bash
./mvnw spring-boot:run
```

Console output walks through each student's field values, its `toString()` representation, and the result of the `equals()` comparison (expected: `true`, since `student2` and `student4` have identical field values).

## Key takeaways

- `@Data` is a convenience meta-annotation bundling getters, setters, `toString()`, `equals()`/`hashCode()`, and a required-args constructor — no need to stack the individual annotations alongside it.
- Lombok needs its annotation processor explicitly registered in the Maven compiler plugin (as done here) to actually generate code at build time.
- Lombok's generated `equals()` is value-based: two separately-constructed objects with the same field values are equal, even though they're different object references.
