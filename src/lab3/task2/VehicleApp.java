package lab3.task2;

import java.util.*;

/*
 * Lab 3 - Task 2: Vehicle Hierarchy with Polymorphism
 * ---------------------------------------------------
 * Goal: Practice INHERITANCE and POLYMORPHISM by creating a Vehicle base class and
 *       multiple subclasses (Car, Motorcycle, Truck). Show method overriding and calling
 *       subclass-specific behavior via a Vehicle[] array.
 *
 * What you will learn:
 * - How subclasses extend a base class using 'extends'.
 * - How to override methods (displayInfo, calculateMaintenanceCost).
 * - How polymorphism lets you call overridden methods through a base-class reference.
 * - Why @Override is useful (compile-time check that a superclass method is actually overridden).
 */

// ------------------------- Base class -------------------------
// Abstract because we don't want to instantiate a generic Vehicle directly.
abstract class Vehicle {
    // 'protected' so subclasses can access these fields directly (as per task guidance)
    protected String brand;
    protected String model;
    protected int year;

    public Vehicle(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    // Concrete method: common way to display info; subclasses may extend/override
    public void displayInfo() {
        System.out.println(year + " " + brand + " " + model);
    }

    // Abstract method: forces each subclass to provide its own maintenance cost formula
    public abstract double calculateMaintenanceCost();
}

// ------------------------- Subclasses -------------------------
class Car extends Vehicle {
    private int numberOfDoors; // subclass-specific field

    public Car(String brand, String model, int year, int doors) {
        super(brand, model, year);
        this.numberOfDoors = doors;
    }

    @Override
    public void displayInfo() {
        // Use super.displayInfo() to reuse base info, then add Car-specific data
        super.displayInfo();
        System.out.println("Doors: " + numberOfDoors);
    }

    @Override
    public double calculateMaintenanceCost() {
        // Simple example formula: older cars cost more. Adjust as needed.
        int age = Math.max(0, currentYear() - year);
        return age * 150.0 + numberOfDoors * 20.0; // doors slightly affect cost
    }

    private int currentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}

class Motorcycle extends Vehicle {
    private int engineCC; // displacement in cubic centimeters

    public Motorcycle(String brand, String model, int year, int engineCC) {
        super(brand, model, year);
        this.engineCC = engineCC;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Engine CC: " + engineCC);
    }

    @Override
    public double calculateMaintenanceCost() {
        int age = Math.max(0, Calendar.getInstance().get(Calendar.YEAR) - year);
        // Bikes generally cheaper to maintain; engine size influences the cost
        return age * 80.0 + engineCC * 0.05;
    }
}

class Truck extends Vehicle {
    private int cargoCapacityKg; // payload capacity

    public Truck(String brand, String model, int year, int cargoCapacityKg) {
        super(brand, model, year);
        this.cargoCapacityKg = cargoCapacityKg;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Cargo capacity: " + cargoCapacityKg + " kg");
    }

    @Override
    public double calculateMaintenanceCost() {
        int age = Math.max(0, Calendar.getInstance().get(Calendar.YEAR) - year);
        // Trucks: age and capacity both push maintenance higher
        return age * 220.0 + cargoCapacityKg * 0.02;
    }
}

// ------------------------- Demo application -------------------------
public class VehicleApp {
    public static void main(String[] args) {
        System.out.println("=== Lab 3 - Task 2: Vehicle Hierarchy with Polymorphism ===");

        // Create different vehicles and store them in a single Vehicle[] (polymorphism!)
        Vehicle[] fleet = new Vehicle[] {
            new Car("Toyota", "Camry", 2020, 4),
            new Motorcycle("Honda", "CBR", 2021, 600),
            new Truck("Ford", "F-150", 2019, 1500)
        };

        // Iterate through the array and call the same methods; each subclass's override runs.
        for (Vehicle v : fleet) {
            v.displayInfo();
            System.out.printf("Maintenance cost: %.2f%n", v.calculateMaintenanceCost());
            System.out.println("---");
        }

        // Extra: show that you can still do subclass-specific operations via casting if necessary
        // (But prefer designing common interfaces in the base class where possible)
        for (Vehicle v : fleet) {
            if (v instanceof Truck) {
                System.out.println("This vehicle is a Truck (found via instanceof).\n");
            }
        }
    }
}
