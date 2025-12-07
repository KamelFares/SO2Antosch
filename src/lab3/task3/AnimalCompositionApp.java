package lab3.task3;

import java.util.*;

interface SoundBehavior {
    void makeSound();
}

interface FlyBehavior {
    void fly(String name);
}

interface SwimBehavior {
    void swim(String name);
}

// Concrete sound behaviors
class ChirpSound implements SoundBehavior {
    public void makeSound() { System.out.println("chirp"); }
}
class QuackSound implements SoundBehavior {
    public void makeSound() { System.out.println("quack"); }
}
class WoofSound implements SoundBehavior {
    public void makeSound() { System.out.println("wau"); }
}
class SilentSound implements SoundBehavior {
    public void makeSound() { System.out.println("(fish are quite ) "); }
}

// Concrete fly behaviors
class CanFly implements FlyBehavior {
    public void fly(String name) { System.out.println(name + " is flying!"); }
}
class CannotFly implements FlyBehavior {
    public void fly(String name) { /* do nothing */ }
}

// Concrete swim behaviors
class CanSwim implements SwimBehavior {
    public void swim(String name) { System.out.println(name + " is swimming!"); }
}
class CannotSwim implements SwimBehavior {
    public void swim(String name) { /* do nothing */ }
}

// Animal class using composition instead of inheritance
class Animal2 {
    private final String name;
    private final String species;
    private final int age;
    private final SoundBehavior soundBehavior;
    private final FlyBehavior flyBehavior;
    private final SwimBehavior swimBehavior;

    public Animal2(String name, String species, int age,
                   SoundBehavior soundBehavior,
                   FlyBehavior flyBehavior,
                   SwimBehavior swimBehavior) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.soundBehavior = soundBehavior;
        this.flyBehavior = flyBehavior;
        this.swimBehavior = swimBehavior;
    }

    public void displayInfo() {
        System.out.println(name + " is a " + age + " year old " + species);
    }
    public void makeSound() { soundBehavior.makeSound(); }
    public void fly() { flyBehavior.fly(name); }
    public void swim() { swimBehavior.swim(name); }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public int getAge() { return age; }
}

public class AnimalCompositionApp {
    public static void main(String[] args) {
        System.out.println("=== Lab 3 - Task 3: Animal System with Composition ===");

        Animal2 bird = new Animal2("Tweety", "Bird", 2, new ChirpSound(), new CanFly(), new CannotSwim());
        Animal2 fish = new Animal2("Nemo", "Fish", 1, new SilentSound(), new CannotFly(), new CanSwim());
        Animal2 duck = new Animal2("Donald", "Duck", 4, new QuackSound(), new CanFly(), new CanSwim());
        Animal2 dog = new Animal2("Rex", "Dog", 5, new WoofSound(), new CannotFly(), new CannotSwim());

        List<Animal2> animals = Arrays.asList(bird, fish, duck, dog);

        // for (Animal2 a : animals) {
        //     a.displayInfo();
        //     a.makeSound();
        //     System.out.println("---");
        // }


for ( Animal2 a : animals){
    a.displayInfo();
    a.makeSound();
    
}   

        System.out.println("Flyers:");
        for (Animal2 a : animals) {
            // We simply call fly(); animals with CannotFly behavior will effectively do nothing
            a.fly();
        }

        System.out.println("Swimmers:");
        for (Animal2 a : animals) {
            // Similarly, all animals call swim(); those with CannotSwim just don't output anything
            a.swim();
        }
    }
}
