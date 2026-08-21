@Autowired
    private B b;
Field Injection works on private data members.

@Autowired
     public static B b;
Autowired annotation is not supported on static fields.
But how does the code work then???
Spring doesn't support @Autowired on static fields.
Spring's dependency injection works on instance fields, and static fields belong to the class rather than any bean instance, so the container has no instance context to inject into.
If you annotate a static field with @Autowired, Spring will silently ignore it (the field just stays null), though in some versions you may get a warning logged.
