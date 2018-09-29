# Chapter 2. Creating and Destroying Objects

- [ITEM 1: CONSIDER STATIC FACTORY METHODS INSTEAD OF CONSTRUCTORS](#item-1-consider-static-factory-methods-instead-of-constructors)
- [ITEM 2: CONSIDER A BUILDER WHEN FACED WITH MANY CONSTRUCTOR PARAMETERS](#item-2-consider-a-builder-when-faced-with-many-constructor-parameters)
- [ITEM 3: ENFORCE THE SINGLETON PROPERTY WITH A PRIVATE CONSTRUCTOR OR AN ENUM TYPE](#item-3-enforce-the-singleton-property-with-a-private-constructor-or-an-enum-type)
- [ITEM 4: ENFORCE NONINSTANTIABILITY WITH A PRIVATE CONSTRUCTOR](#item-4-enforce-noninstantiability-with-a-private-constructor)
- [ITEM 5: PREFER DEPENDENCY INJECTION TO HARDWIRING RESOURCES](#item-5-prefer-dependency-injection-to-hardwiring-resources)
- [ITEM 6: AVOID CREATING UNNECESSARY OBJECTS](#item-6-avoid-creating-unnecessary-objects)
- [ITEM 7: ELIMINATE OBSOLETE OBJECT REFERENCES](#item-7-eliminate-obsolete-object-references)
- [ITEM 8: AVOID FINALIZERS AND CLEANERS](#item-8-avoid-finalizers-and-cleaners)
- [ITEM 9: PREFER TRY-WITH-RESOURCES TO TRY-FINALLY](#item-9-prefer-try-with-resources-to-try-finally)

## ITEM 1: CONSIDER STATIC FACTORY METHODS INSTEAD OF CONSTRUCTORS

```java
public static Boolean valueOf(boolean b) {

    return b ? Boolean.TRUE : Boolean.FALSE;

}
```

### Item 1: Advantages

1. Unlike constructors, they have names. `BigInteger.probablePrime`
2. Unlike constructors, they are not required to create a new object each time they’re invoked.
    > This technique is similar to the **Flyweight pattern**. It can greatly improve performance if equivalent objects are requested often, especially if they are expensive to create.
3. Unlike constructors, they can return an object of any subtype of their return type.
4. The class of the returned object can vary from call to call as a function of the input parameters.
    > The *EnumSet* class has no public constructors, only static factories. In the OpenJDK implementation, they return an instance of one of two subclasses, depending on the size of the underlying enum type: if it has sixty-four or fewer elements, as most enum types do, the static factories return a *RegularEnumSet* instance, which is backed by a single long; if the enum type has sixty-five or more elements, the factories return a *JumboEnumSet* instance, backed by a long array.
5. The class of the returned object need not exist when the class containing the method is written.
    > There are three essential components in a service provider framework: a **service interface**, which represents an implementation; a **provider registration API**, which providers use to register implementations; and a **service access API**, which clients use to obtain instances of the service. An optional fourth component of a service provider framework is a **service provider interface**, which describes a factory object that produces instances of the service interface. In the case of service provider framework JDBC, Connection plays the part of the service interface, *DriverManager.registerDriver* is the provider registration API, *DriverManager.getConnection* is the service access API, and *Driver* is the service provider interface.

### Item 1: Disadvantages

1. The main limitation of providing only static factory methods is that classes without public or protected constructors cannot be subclassed.
    > For example, it is impossible to subclass any of the convenience implementation classes in the Collections Framework.
2. A second shortcoming of static factory methods is that they are hard for programmers to find. Here are some common names for static factory methods. This list is far from exhaustive:
    - *from* : A **type-conversion** method that takes a single parameter and returns a corresponding instance of this type, for example:
        > `Date d = Date.from(instant);`
    - *of* : An **aggregation method** that takes multiple parameters and returns an instance of this type that incorporates them, for example:
        > `Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);`
    - *valueOf* - A more verbose alternative to *from* and *of*, for example:
        > `BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);`
    - *instance* or *getInstance* : Returns an instance that is described by its parameters (if any) but cannot be said to have the same value, for example:
        > `StackWalker luke = StackWalker.getInstance(options);`
    - *create* or *newInstance* : Like *instance* or *getInstance*, except that the method guarantees that each call returns a new instance, for example:
        > Object newArray = Array.newInstance(classObject, arrayLen);
    - *getType* : Like *getInstance*, but used if the factory method is in a different class. Type is the type of object returned by the factory method, for example:
        > `FileStore fs = Files.getFileStore(path);`
    - *newType* : Like *newInstance*, but used if the factory method is in a different class. Type is the type of object returned by the factory method, for example:
        > `BufferedReader br = Files.newBufferedReader(path);`
    - *type* : A concise alternative to *getType* and *newType*, for example:
        > `List<Complaint> litany = Collections.list(legacyLitany);`

### Item 1: Summary

**Static factory methods** and **public constructors** both have their uses, and it pays to understand their relative merits. Often static factories are preferable, so avoid the reflex to provide public constructors without first considering static factories.

## ITEM 2: CONSIDER A BUILDER WHEN FACED WITH MANY CONSTRUCTOR PARAMETERS

Example:

```java
// Builder Pattern

public class NutritionFacts {

    private final int servingSize;

    private final int servings;

    private final int calories;

    private final int fat;

    private final int sodium;

    private final int carbohydrate;

    public static class Builder {

        // Required parameters

        private final int servingSize;

        private final int servings;

        // Optional parameters - initialized to default values

        private int calories      = 0;

        private int fat           = 0;

        private int sodium        = 0;

        private int carbohydrate  = 0;

        public Builder(int servingSize, int servings) {

            this.servingSize = servingSize;

            this.servings    = servings;

        }

        public Builder calories(int val)

            { calories = val;      return this; }

        public Builder fat(int val)

            { fat = val;           return this; }

        public Builder sodium(int val)

            { sodium = val;        return this; }

        public Builder carbohydrate(int val)

            { carbohydrate = val;  return this; }

        public NutritionFacts build() {

            return new NutritionFacts(this);

        }

    }

    private NutritionFacts(Builder builder) {

        servingSize  = builder.servingSize;

        servings     = builder.servings;

        calories     = builder.calories;

        fat          = builder.fat;

        sodium       = builder.sodium;

        carbohydrate = builder.carbohydrate;

    }

}

// Client side code
NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8).calories(100).sodium(35).carbohydrate(27).build();
```

### Item 2: Advantages

- Combines the **safety** of the **telescoping constructor pattern** with the **readability** of **the JavaBeans pattern**.  
- Instead of making the desired object directly, the client calls a constructor (or static factory) with all of the required parameters and gets a builder object.
- This client code is easy to write and, more importantly, easy to read. The Builder pattern simulates named optional parameters as found in Python and Scala.

- The Builder pattern is well suited to class hierarchies.
    > Use a parallel hierarchy of builders, each nested in the corresponding class. Abstract classes have abstract builders; concrete classes have concrete builders.

### Item 2: Disadvantages

- In order to create an object, you must first create its builder.
    > While the cost of creating this builder is unlikely to be noticeable in practice, it could be a problem in performance-critical situations.
- The Builder pattern is more verbose than the telescoping constructor pattern, so it should be used only if there are enough parameters to make it worthwhile, say four or more.
    > But keep in mind that you may want to add more parameters in the future. But if you start out with constructors or static factories and switch to a builder when the class evolves to the point where the number of parameters gets out of hand, the obsolete constructors or static factories will stick out like a sore thumb. Therefore, it’s often better to start with a builder in the first place.

### Item 3: Summary

**the Builder pattern** is a good choice when designing classes whose *constructors* or *static factories* would have more than a handful of parameters, especially if many of the parameters are optional or of identical type. 

Client code is much easier to read and write with builders than with telescoping constructors, and builders are much safer than JavaBeans.

## ITEM 3: ENFORCE THE SINGLETON PROPERTY WITH A PRIVATE CONSTRUCTOR OR AN ENUM TYPE

There are two common ways to implement singletons. Both are based on keeping the constructor private and exporting a public static member to provide access to the sole instance.

- Approach 1: The public member is a **final field**

```java
// Singleton with public final field
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() { }
    public static Elvis getInstance() { return INSTANCE; }

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

    // This code would normally appear outside the class!
    public static void main(String[] args) {
        Elvis elvis = Elvis.getInstance();
        elvis.leaveTheBuilding();
    }
}
```

- Approach 2: The public member is a **static factory method**

```java
// Singleton with static factory
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();

    private Elvis() { }

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

    // This code would normally appear outside the class!
    public static void main(String[] args) {
        Elvis elvis = Elvis.INSTANCE;
        elvis.leaveTheBuilding();
    }
}
```

> To make a singleton class that uses either of these approaches serializable, it is not sufficient merely to add implements *Serializable* to its declaration. To maintain the singleton guarantee, declare all instance fields *transient* and provide a *readResolve* method. Otherwise, each time a serialized instance is deserialized, a new instance will be created.

```java
// readResolve method to preserve singleton property
private Object readResolve() {

     // Return the one true Elvis and let the garbage collector

     // take care of the Elvis impersonator.
    return INSTANCE;
}
```

- Approach 3: A single-element enum

```java
// Enum singleton - the preferred approach
public enum Elvis {
    INSTANCE;

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

    // This code would normally appear outside the class!
    public static void main(String[] args) {
        Elvis elvis = Elvis.INSTANCE;
        elvis.leaveTheBuilding();
    }
}
```

### Item 3: Advantages

- **The public field** approach is that the API makes it clear that the class is a singleton:** the public static field is final**, so it will always contain **the same object reference**
- **The static factory approach** is that it gives you the flexibility to change your mind about whether the class is a singleton without changing its API. The factory method returns the sole instance, but it could be modified to return, say, a separate instance for each thread that invokes it.
- **Enum** approach is similar to the public field approach, but it is more concise, provides the serialization machinery for free, and provides an ironclad guarantee against multiple instantiation, even in the face of sophisticated serialization or reflection attacks. 
    > This approach may feel a bit unnatural, but **a single-element enum type is often the best way to implement a singleton**. Note that you can’t use this approach if your singleton must extend a superclass other than Enum (though you can declare an enum to implement interfaces).



## ITEM 4: ENFORCE NONINSTANTIABILITY WITH A PRIVATE CONSTRUCTOR

```java
// Noninstantiable utility class

public class UtilityClass {

    // Suppress default constructor for noninstantiability

    private UtilityClass() {

        throw new AssertionError();

    }
    ... // Remainder omitted
}
```


## ITEM 5: PREFER DEPENDENCY INJECTION TO HARDWIRING RESOURCES

Many classes depend on one or **more underlying resources**. 
    > For example, a spell checker depends on a dictionary. It is not uncommon to see such classes implemented as static utility classes

- Static utility classes and singletons are inappropriate for classes whose **behavior is parameterized by an underlying resource**.
- A simple pattern that satisfies this requirement is to pass the resource into the **constructor** when **creating a new instance**.

```java
// Dependency injection provides flexibility and testability
public class SpellChecker {

    private final Lexicon dictionary;

    public SpellChecker(Lexicon dictionary) {

        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public boolean isValid(String word) { ... }

    public List<String> suggestions(String typo) { ... }
}
```

- It preserves **immutability** , so multiple clients can share dependent objects (assuming the clients desire the same underlying resources). 
- Dependency injection is equally **applicable** to **constructors, static factories, and builders**.

### Summary

- Do not use a singleton or static utility class to implement a class that **depends on one or more underlying resources** whose behavior affects that of the class, and do not have the class create these resources directly. 
- Instead, pass the resources, or factories to create them, into the constructor (or static factory or builder). 
- This practice, known as **dependency injection**, will greatly enhance the **flexibility, reusability, and testability** of a class.

## ITEM 6: AVOID CREATING UNNECESSARY OBJECTS

It is often appropriate to reuse a single object instead of creating a new functionally equivalent object each time it is needed. Reuse can be both faster and more stylish. An object can always be reused if it is immutable.

What **not to do**, consider this statement:
```java
String s = new String("bikini");  // DON'T DO THIS!
```

If this usage occurs in a loop or in a frequently invoked method, millions of String instances can be created needlessly.

**Cache it for reuse**

Some object creations are much more expensive than others. If you’re going to need such an “expensive object” repeatedly, it may be advisable to cache it for reuse. 

```java
// Performance can be greatly improved!
static boolean isRomanNumeral(String s) {

    return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"

            + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

}
```

reusing example

```java
// Reusing expensive object for improved performance

public class RomanNumerals {

    private static final Pattern ROMAN = Pattern.compile(

            "^(?=.)M*(C[MD]|D?C{0,3})"

            + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }
}
```

**Prefer primitives to boxed primitives, and watch out for unintentional autoboxing.**

```java
// Hideously slow! Can you spot the object creation?
private static long sum() {

    Long sum = 0L;

    for (long i = 0; i <= Integer.MAX_VALUE; i++)

        sum += i;

    return sum;
}
```

## ITEM 7: ELIMINATE OBSOLETE OBJECT REFERENCES

- An obsolete reference is simply a **reference that will never be dereferenced again**. 


Can you spot the "memory leak"?

```java

public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    /**
     * Ensure space for at least one more element, roughly
     * doubling the capacity each time the array needs to grow.
     */

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

- Stack maintains **obsolete references** to these objects. 
- In this case, any references outside of the “active portion” of the element array are obsolete. 
- The active portion consists of the elements whose index is less than size.

To fix this: **Eliminate obsolete reference**

```java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();

    Object result = elements[--size];
    elements[size] = null; // Eliminate obsolete reference

    return result;
}
```

## ITEM 8: AVOID FINALIZERS AND CLEANERS

- Finalizers are unpredictable, often dangerous, and generally unnecessary.
- Their use can cause erratic behavior, poor performance, and portability problems.
- The Java 9 replacement for finalizers is cleaners.
- One shortcoming of finalizers and cleaners is that there is no guarantee they’ll be executed promptly.
    > Never do anything time-critical in a finalizer or cleaner
- As a consequence, you should never depend on a finalizer or cleaner to ***update persistent state***.

What to do?
- Just have your class implement AutoCloseable
- Require its clients to invoke the close method on each instance when it is no longer needed, typically using try-with-resources to ensure termination even in the face of exceptions 

What, if anything, are cleaners and finalizers good for?
- One is to act as a **safety net** in case the owner of a resource neglects to call its close method.
- A second legitimate use of cleaners concerns objects with **native peers**.

Autocloseable class using safety net:
```java
import java.lang.ref.Cleaner;

// An autocloseable class using a cleaner as a safety net (Page 32)
public class Room implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();

    // Resource that requires cleaning. Must not refer to Room!
    private static class State implements Runnable {
        int numJunkPiles; // Number of junk piles in this room

        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        // Invoked by close method or cleaner
        @Override public void run() {
            System.out.println("Cleaning room");
            numJunkPiles = 0;
        }
    }

    // The state of this room, shared with our cleanable
    private final State state;

    // Our cleanable. Cleans the room when it’s eligible for gc
    private final Cleaner.Cleanable cleanable;

    public Room(int numJunkPiles) {
        state = new State(numJunkPiles);
        cleanable = cleaner.register(this, state);
    }

    @Override public void close() {
        cleanable.clean();
    }
}
```

Clients:
```java
// Ill-behaved client of resource with cleaner safety-net
public class Teenager {
    public static void main(String[] args) {
        new Room(99);
        System.out.println("Peace out");

        // Uncomment next line and retest behavior, but note that you MUST NOT depend on this behavior!
//      System.gc();
    }
}



// Well-behaved client of resource with cleaner safety-net (Page 33)
public class Adult {
    public static void main(String[] args) {
        try (Room myRoom = new Room(7)) {
            System.out.println("Goodbye");
        }
    }
}
```

## ITEM 9: PREFER TRY-WITH-RESOURCES TO TRY-FINALLY

- The Java libraries include many resources that must be closed manually by invoking a *close* method. 
    > Examples include *InputStream*, *OutputStream*, and *java.sql.Connection*. 
- Closing resources is often overlooked by clients, with predictably dire performance consequences.

Example `try-finally`:
```java
import java.io.*;

public class Copy {
    private static final int BUFFER_SIZE = 8 * 1024;

    // try-finally is ugly when used with more than one resource!
    static void copy(String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0)
                    out.write(buf, 0, n);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String src = args[0];
        String dst = args[1];
        copy(src, dst);
    }
}
```

- All of these problems were solved in one fell swoop when Java 7 introduced the ***try-with-resources*** statement. 
- To be usable with this construct, 
    - a resource must implement the *AutoCloseable* interface, which consists of a single void-returning *close* method. 
- Many classes and interfaces in the Java libraries and in third-party libraries now implement or extend *AutoCloseable*. 
- If you write a class that represents a resource that must be closed, your class should implement *AutoCloseable* too.

Example `try-with-resources`:
```java
import java.io.*;

public class Copy {
    private static final int BUFFER_SIZE = 8 * 1024;

    // try-with-resources on multiple resources - short and sweet
    static void copy(String src, String dst) throws IOException {
        try (InputStream   in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        }
    }

    public static void main(String[] args) throws IOException {
        String src = args[0];
        String dst = args[1];
        copy(src, dst);
    }
}
```

You can put ***catch*** clauses on *try-with-resources* statements, just as you can on regular *try-finally* statements.

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TopLineWithDefault {
    // try-with-resources with a catch clause  (Page 36)
    static String firstLineOfFile(String path, String defaultVal) {
        try (BufferedReader br = new BufferedReader(
                new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            return defaultVal;
        }
    }

    public static void main(String[] args) throws IOException {
        String path = args[0];
        System.out.println(firstLineOfFile(path, "Toppy McTopFace"));
    }
}
```
