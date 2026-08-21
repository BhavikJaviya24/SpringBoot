package com.bhavik.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class A {

    /*
    @Autowired
    private B b;
    // Field Injection works on private data members.
    */

    /*
    @Autowired
     public static B b;
    // Autowired annotation is not supported on static fields.
    // But how does the code work then???
    // Spring doesn't support @Autowired on static fields.
    // Spring's dependency injection works on instance fields, and static fields belong to the class rather than any
    // bean instance, so the container has no instance context to inject into.
    // If you annotate a static field with @Autowired, Spring will silently ignore it (the field just stays null),
    // though in some versions you may get a warning logged.
    */
    public A() {
        System.out.println("A Bean created");
    }
//-----------------------------------------------------------------------------------------------------------------------------------------------


    // Spring can't inject into static fields directly, but both constructor and setter injection can be used as a bridge
    // — Spring injects into the instance-level method (constructor/setter), and you manually assign that value to the
    // static field inside it.

    public static B b;

    @Autowired
    public A(B b) {
        System.out.println("A Bean created using Constructor Injection");
        this.b = b; // static field set here
    }
    // Runs the moment the bean is created.
    // Static field is guaranteed to be set as soon as A's constructor finishes.
    // Preferred choice — more predictable, treats the dependency as mandatory.



    /*
    @Autowired
    public void setB(B b) {
        System.out.println("B Bean Injected using Setter Injection");
        this.b = b; // static field set here
    }
    // Object is created first (via no-arg or default constructor), then Spring calls the setter afterward to inject the dependency.
    // Small window between object creation and setter call where A.b(this.b) could still be null if something reads it too early(before setter gets called).
    // Dependency is technically optional, setter can be called again later and overwrite the static field.
     */


}
