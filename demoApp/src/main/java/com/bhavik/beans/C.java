package com.bhavik.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class C {

    //@Autowired
   public final static D d; // this is static final variable

    public C() {
        System.out.println("C Bean Created");
    }

    /*
    @Autowired
    public C(D d) {
        System.out.println("C Bean created using Constructor Injection");
        this.d = d; // static field set here
    }
    */

    /*
    @Autowired
    public void setD(D d) {
        System.out.println("D Bean Injected using Setter Injection");
        this.d = d; // static field set here
    }
     */

    // final means the field can be assigned exactly once, and that assignment must happen either:
    //  1. at the point of declaration (public static final D d = ...;), or
    //  2. inside a static initializer block (static { ... })

    // IMPORTANT :: Dependency Injection of any type(field/constructor/setter) doesn't work for static final references.

    // to initialize static finale:
    static{
        System.out.println("D bean created from static block manually ");
        d = new D(); // two D beans are created due to 1. @Component at class D, 2. d = new D(); in static block.
    }
}
