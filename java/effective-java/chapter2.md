# Chapter 2. Creating and Destroying Objects

- [ITEM 1: CONSIDER STATIC FACTORY METHODS INSTEAD OF CONSTRUCTORS](#ITEM-1-CONSIDER-STATIC-FACTORY-METHODS-INSTEAD-OF-CONSTRUCTORS)
- [ITEM 2: CONSIDER A BUILDER WHEN FACED WITH MANY CONSTRUCTOR PARAMETERS](#ITEM-2-CONSIDER-A-BUILDER-WHEN-FACED-WITH-MANY-CONSTRUCTOR-PARAMETERS)

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
- asda

### Item 2: Disadvantages

- In order to create an object, you must first create its builder.
    > While the cost of creating this builder is unlikely to be noticeable in practice, it could be a problem in performance-critical situations.
- The Builder pattern is more verbose than the telescoping constructor pattern, so it should be used only if there are enough parameters to make it worthwhile, say four or more.
    > But keep in mind that you may want to add more parameters in the future. But if you start out with constructors or static factories and switch to a builder when the class evolves to the point where the number of parameters gets out of hand, the obsolete constructors or static factories will stick out like a sore thumb. Therefore, it’s often better to start with a builder in the first place.

### Item 3: Summary

**the Builder pattern** is a good choice when designing classes whose *constructors* or *static factories* would have more than a handful of parameters, especially if many of the parameters are optional or of identical type. 

Client code is much easier to read and write with builders than with telescoping constructors, and builders are much safer than JavaBeans.

