# Effective Java, 3rd edtion Notes

[Source Code of book](https://github.com/jbloch/effective-java-3e-source-code)

Chapters

- [Chapter 2. Creating and Destroying Objects](chapter2.md)

## Patterns

Book
> Gamma, Erich, Richard Helm, Ralph Johnson, and John Vlissides. 1995. Design Patterns: Elements of Reusable Object-Oriented Software. Reading, MA: Addison-Wesley. ISBN: 0201633612.

### Flyweight pattern

- Consider static factory methods instead of constructors, It can greatly improve performance if equivalent objects are requested often, especially if they are expensive to create.

### The Bridge pattern

- For example, the service access API can return a richer service interface to clients than the one furnished by providers. This is the Bridge pattern.

### Telescoping constructor pattern

- In which you provide a constructor with only the required parameters, another with a single optional parameter, a third with two optional parameters, and so on, culminating in a constructor with all the optional parameters.
- Telescoping constructor pattern - does not scale well!

### JavaBeans pattern

- In which you call a parameterless constructor to create the object and then call setter methods to set each required parameter and each optional parameter of interest.
- JavaBeans Pattern - allows inconsistency, mandates mutability
- a JavaBean may be in an inconsistent state partway through its construction.
- the JavaBeans pattern precludes the possibility of making a class immutable

### Builder pattern

- Combines the **safety** of the **telescoping constructor pattern** with the **readability** of **the JavaBeans pattern**.  
- Instead of making the desired object directly, the client calls a constructor (or static factory) with all of the required parameters and gets a builder object.
- The Builder pattern simulates named optional parameters as found in Python and Scala.

### Singleton

- A singleton is simply a class that is instantiated exactly once. 
- Singletons typically represent either a stateless object such as a *function* or a *system component* that is intrinsically unique. 
- Making a class a singleton can make it difficult to test its clients because it’s impossible to substitute a mock implementation for a singleton unless it implements an interface that serves as its type.

## Adapters

- Also known as views. 
- An adapter is an object that delegates to a backing object, providing an alternative interface. Because an adapter has **no state** beyond that of its backing object, there’s no need to create more than one instance of a given adapter to a given object.
