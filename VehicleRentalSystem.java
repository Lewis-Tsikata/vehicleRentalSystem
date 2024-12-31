

import java.util.ArrayList;
import java.util.List;

class VehicleRentalSystem {

    public static void main(String[] args) {
        RentalAgency agency = new RentalAgency();

        // Add vehicles to the rental agency
        Car car1 = new Car("GW-23", "Toyota Corolla", 50.0, true);
        Motorcycle motorcycle1 = new Motorcycle("GA-42", "Royal FZ", 30.0, false);
        Truck truck1 = new Truck("VR-64", "Ford F-150", 70.0, 1000);

        agency.addVehicle(car1);
        agency.addVehicle(motorcycle1);
        agency.addVehicle(truck1);

        // Create a customer
        Customer customer = new Customer("22123273", "Tsikata Lewis");

        // Rent a vehicle
        agency.rentVehicle("GW-23", customer, 5);
        agency.rentVehicle("GA-42", customer, 3);

        // Return vehicles
        agency.returnVehicle("GW-23");
        agency.returnVehicle("GA-42");

        // Print rental transactions
        System.out.println("Rental Transactions:");
        agency.getTransactions().forEach(System.out::println);
    }
}

// Abstract Class
abstract class Vehicle {
    private final String vehicleId;
    private String model;
    private double baseRentalRate;
    private boolean isAvailable;

    public Vehicle(String vehicleId, String model, double baseRentalRate) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseRentalRate = baseRentalRate;
        this.isAvailable = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (model == null || model.isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
        this.model = model;
    }

    public double getBaseRentalRate() {
        return baseRentalRate;
    }

    public void setBaseRentalRate(double baseRentalRate) {
        if (baseRentalRate <= 0) {
            throw new IllegalArgumentException("Base rental rate must be positive");
        }
        this.baseRentalRate = baseRentalRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public abstract double calculateRentalCost(int days);

    public abstract boolean isAvailableForRental();
}

// Concrete Classes
class Car extends Vehicle {
    private final boolean hasAirConditioning;

    public Car(String vehicleId, String model, double baseRentalRate, boolean hasAirConditioning) {
        super(vehicleId, model, baseRentalRate);
        this.hasAirConditioning = hasAirConditioning;
    }

    @Override
    public double calculateRentalCost(int days) {
        double rate = getBaseRentalRate() * days;
        return hasAirConditioning ? rate + 50 : rate;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }
}

class Motorcycle extends Vehicle {
    private final boolean hasSidecar;

    public Motorcycle(String vehicleId, String model, double baseRentalRate, boolean hasSidecar) {
        super(vehicleId, model, baseRentalRate);
        this.hasSidecar = hasSidecar;
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days * (hasSidecar ? 1.1 : 1);
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }
}

class Truck extends Vehicle {
    private final double loadCapacity;

    public Truck(String vehicleId, String model, double baseRentalRate, double loadCapacity) {
        super(vehicleId, model, baseRentalRate);
        this.loadCapacity = loadCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days + (loadCapacity * 10);
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }
}

// Customer Class
class Customer {
    private final String customerId;
    private String name;
    private List<String> rentalHistory;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        this.rentalHistory = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public List<String> getRentalHistory() {
        return rentalHistory;
    }

    public void addRental(String vehicleId) {
        rentalHistory.add(vehicleId);
    }
}

// Rental Agency Class
class RentalAgency {
    private List<Vehicle> fleet;
    private List<RentalTransaction> transactions;

    public RentalAgency() {
        this.fleet = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    public void rentVehicle(String vehicleId, Customer customer, int days) {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getVehicleId().equals(vehicleId) && vehicle.isAvailableForRental()) {
                double cost = vehicle.calculateRentalCost(days);
                vehicle.setAvailable(false);
                transactions.add(new RentalTransaction(vehicleId, customer.getCustomerId(), days, cost));
                customer.addRental(vehicleId);
                System.out.println("Vehicle rented: " + vehicle.getModel() + " | Rental cost: " + cost);
                return;
            }
        }
        throw new IllegalArgumentException("Vehicle not available or invalid ID");
    }

    public void returnVehicle(String vehicleId) {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getVehicleId().equals(vehicleId) && !vehicle.isAvailableForRental()) {
                vehicle.setAvailable(true);
                System.out.println("Vehicle returned: " + vehicle.getModel());
                return;
            }
        }
        throw new IllegalArgumentException("Vehicle ID is invalid or vehicle is not rented");
    }

    public List<RentalTransaction> getTransactions() {
        return transactions;
    }
}

// Rental Transaction Class
class RentalTransaction {
    private final String vehicleId;
    private final String customerId;
    private final int daysRented;
    private final double totalCost;

    public RentalTransaction(String vehicleId, String customerId, int daysRented, double totalCost) {
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.daysRented = daysRented;
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "RentalTransaction{" +
                "vehicleId='" + vehicleId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", daysRented=" + daysRented +
                ", totalCost=" + totalCost +
                '}';
    }
}
