package lab3.task3;

/*
 * Lab 3 - Task 3: Animal Classification System
 * --------------------------------------------
 * Goal: Practice advanced inheritance concepts using an abstract base class (Animal)
 *       and multiple interfaces (Flyable, Swimmable). Show multiple interface
 *       implementation (Duck implements both) and interface-based polymorphism.
 *
 * What you will learn:
 * - How abstract classes define shared state/behavior and abstract methods.
 * - How interfaces declare capabilities that can be mixed into different classes.
 * - How a class can implement multiple interfaces in Java.
 * - How to use polymorphism via base classes and interfaces.
 */

// ------------------------- Abstract base class -------------------------
abstract class Animal {
    // 'protected' so subclasses can access these fields directly
    protected String name;
    protected String species;
    protected int age;

    public Animal(String name, String species, int age) {
        this.name = name;
        this.species = species;
        this.age = age;
    }

    // Concrete method shared by all animals
    public void displayInfo() {
        System.out.println(name + " is a " + age + " year old " + species);
    }

    // Abstract method: must be implemented by concrete subclasses
    public abstract void makeSound();
}

// ------------------------- Interfaces (capabilities) -------------------------
interface Flyable {
    void fly();

    // Default method provides a sensible default for all implementers
    default int getMaxAltitude() { return 1000; }
}

interface Swimmable {
    void swim();
}

// ------------------------- Concrete animal classes -------------------------
class Bird extends Animal implements Flyable {
    public Bird(String name, int age) {
        super(name, "Bird", age);
    }

    @Override
    public void makeSound() {
        System.out.println("Chirp!");
    }

    @Override
    public void fly() {
        System.out.println(name + " is soaring in the sky up to " + getMaxAltitude() + " meters!");
    }
}

class Fish extends Animal implements Swimmable {
    public Fish(String name, int age) {
        super(name, "Fish", age);
    }

    @Override
    public void makeSound() {
        System.out.println("(Fish are generally quiet...) ");
    }

    @Override
    public void swim() {
        System.out.println(name + " is swimming swiftly under water!");
    }
}

class Duck extends Animal implements Flyable, Swimmable {
    public Duck(String name, int age) {
        super(name, "Duck", age);
    }

    @Override
    public void makeSound() {
        System.out.println("Quack!");
    }

    @Override
    public void fly() {
        System.out.println(name + " is flying over the pond!");
    }

    @Override
    public void swim() {
        System.out.println(name + " is paddling on the water!");
    }
}

class Dog extends Animal {
    public Dog(String name, int age) {
        super(name, "Dog", age);
    }

    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
}

// ------------------------- Demo application -------------------------
public class AnimalApp {
    public static void main(String[] args) {
        System.out.println("=== Lab 3 - Task 3: Animal Classification System ===");

        // Base-class polymorphism: treat different animals as Animal
        Animal[] animals = new Animal[] {
            new Bird("Tweety", 2),
            new Fish("Nemo", 1),
            new Duck("Donald", 4),
            new Dog("Rex", 5)
        };

        for (Animal a : animals) {
            a.displayInfo();
            a.makeSound();
            System.out.println("---");
        }

        // Interface polymorphism: use interface references to call capability-specific methods
        Flyable[] flyers = new Flyable[] {
            new Bird("Eagle", 3),
            new Duck("Daisy", 2)
        };
        for (Flyable f : flyers) {
            f.fly();
        }

        Swimmable[] swimmers = new Swimmable[] {
            new Fish("Goldie", 1),
            new Duck("Huey", 1)
        };
        for (Swimmable s : swimmers) {
            s.swim();
        }
    }
}
