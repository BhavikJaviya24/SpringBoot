# demoApp

A small Spring Boot playground project exploring the edge cases of `@Autowired` dependency injection — specifically what happens when you try to inject into **static** and **static final** fields, and the workarounds needed to make it work.

## Overview

Spring's dependency injection is designed for **instance** fields. This project demonstrates, through working code and inline commentary, why `@Autowired` silently fails on static fields, and shows two patterns to get a dependency into a static field anyway:

1. **Constructor injection as a bridge** (`A` → `B`) — Spring injects into the constructor (an instance-level method), and the constructor body manually assigns the value to the static field.
2. **Static initializer block for `static final` fields** (`C` → `D`) — since DI can't touch `static final` fields at all, the field is populated manually inside a `static { }` block instead.

## Tech Stack

| Component | Version |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.0 |
| Build tool | Maven (with Maven Wrapper) |

## Project Structure

```
demoApp/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/bhavik/
    │   │   ├── DemoAppApplication.java   # entry point
    │   │   └── beans/
    │   │       ├── A.java                # constructor-injection-into-static-field demo
    │   │       ├── B.java                # plain bean, dependency of A
    │   │       ├── C.java                # static-final-field-via-static-block demo
    │   │       └── D.java                # plain bean, dependency of C
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/bhavik/
            └── DemoAppApplicationTests.java
```

## What each bean demonstrates

### `A` and `B` — injecting into a `static` field

```java
public static B b;

@Autowired
public A(B b) {
    this.b = b; // static field set manually inside the constructor
}
```

- A plain `@Autowired private static B b;` field is **silently ignored** by Spring — static fields belong to the class, not to any bean instance, so the container has no instance to inject into.
- The workaround: let Spring inject `B` through **constructor injection** (an instance-level entry point), then assign it to the static field yourself inside the constructor body. The static field is guaranteed to be set the moment the bean is created.
- The file also keeps commented-out attempts (field injection on a static field, and setter injection) to show the alternatives and why constructor injection is the more predictable choice.

> **Note:** In the current code, `A`'s `@Component` annotation is commented out, so `A` is not actually picked up by Spring's component scan. As written, `A`'s constructor won't run automatically — this line is worth un-commenting if you want to see the injection actually fire.

### `C` and `D` — injecting into a `static final` field

```java
public final static D d;

static {
    d = new D(); // must be assigned at declaration or in a static block
}
```

- Dependency injection (field, constructor, or setter) **cannot** populate a `static final` field under any circumstances — Java requires a `final` field to be assigned exactly once, either at declaration or inside a `static` initializer block.
- Since `D` is also annotated `@Component`, this setup ends up creating **two `D` instances**: one managed by the Spring container (because of `@Component` on `D`), and a second one created manually via `new D()` inside `C`'s static block. This is called out directly in the code comments as a side effect worth being aware of.

## Running the project

```bash
# from the demoApp directory
./mvnw spring-boot:run
```

Watch the console output — the order and count of `"... Bean created"` print statements is the whole point of the demo, showing exactly when each bean is instantiated and whether static injection succeeded.

Run the test suite (just a context-load smoke test):

```bash
./mvnw test
```

## Key takeaways

- `@Autowired` never works on `static` fields — Spring injects into bean instances, not classes.
- To get a dependency onto a static field, inject it into a constructor or setter and assign it manually.
- `static final` fields can't be touched by DI at all; initialize them at declaration or in a `static { }` block.
- Watch out for duplicate bean creation when a class is both `@Component`-managed and also manually `new`'d elsewhere (as with `D` here).
