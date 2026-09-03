# 02-Enum-Demo

A plain-Java (no Spring Boot) project exploring how **enums with fields and constructors** work — using a `Gender` enum with a code/value pair as the running example, ahead of using it as a JPA-mapped entity field later in the series.

## Overview

This is a lightweight, standalone Java project (not a Spring Boot app — there's no `spring-boot-starter-parent` and no `@SpringBootApplication`) sitting between `01-Lombok-Api` and `03-CrudRepository-Jpa-app`. It's purely about Java language mechanics: how to give an enum constant its own fields, a private constructor, and accessor methods, since `Student.gender` will be persisted as this kind of enum in the next project.

## Tech Stack

| Component | Version / Detail |
|---|---|
| Java | 17 (`maven.compiler.source`/`target`) |
| Build tool | Maven (plain `pom.xml`, no Spring dependencies) |

## Project Structure

```
02-Enum-Demo/
├── pom.xml
└── src/
    └── main/
        └── java/com/bhavik/
            ├── Main.java              # entry point, iterates over Gender values
            └── entity/
                ├── Gender.java         # enum with value/code fields
                └── Student.java        # plain POJO using Gender
```

## What it demonstrates

### `Gender` — an enum with data, not just constants
```java
public enum Gender {
    MALE("M", 100),
    FEMALE("F", 200);

    private String value;
    private int code;

    private Gender(String value, int code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() { return value; }
    public int getCode() { return code; }
}
```
A simpler, bare-constants version (`MALE, FEMALE, OTHERS;`) is kept commented out above it for comparison. The code also includes a comment sketching what the compiler effectively does behind the scenes with any enum:
```java
// public final class Gender {
//     final static Gender MALE = new Gender("MALE");
//     final static Gender FEMALE = new Gender("FEMALE");
// }
// final to stop inheritance.
```
i.e., each enum constant is really a `public static final` instance of an implicitly-`final` class, constructed exactly once.

### `Student` — a plain model referencing the enum
A conventional POJO (`rno`, `name`, `per`, `gender`) with manual getters/setters and `toString()` — no Lombok here, and no JPA annotations yet (that comes in project 03).

### `Main` — inspecting enum behavior at runtime
```java
for (Gender g : Gender.values()) {
    System.out.println(g);           // MALE / FEMALE
    System.out.println(g.ordinal()); // 0 / 1
    System.out.println(g.getValue());// "M" / "F"
    System.out.println(g.getCode()); // 100 / 200
}
```
Loops over all enum constants via `.values()` and prints the constant name, its ordinal position, and its two custom fields. Building a `Student` with a `Gender` is present but commented out, left as a follow-up exercise.

## Running the project

```bash
mvn compile exec:java -Dexec.mainClass=com.bhavik.Main
```
(or run `Main.java` directly from your IDE — there's no Spring Boot runner here, just a plain `main` method)

Expected output: for each of `MALE` and `FEMALE`, the constant name, its ordinal (`0`, `1`), its `value` (`"M"`, `"F"`), and its `code` (`100`, `200`).

## Key takeaways

- Enum constants can carry their own fields and be constructed with arguments, via a (typically private) constructor — useful for attaching a short code, display label, or numeric weight to each constant.
- `.values()` and `.ordinal()` are built into every enum for free; ordinal reflects declaration order, not any custom `code` field.
- Under the hood, an enum compiles to a `final` class with one `public static final` instance per constant — which is why enums can't be subclassed and why `==` comparison is safe for enum constants.
