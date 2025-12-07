import java.util.Scanner;

// Lab 2 - Task 6: Simple Inventory Management System
class Item {
    private String name;
    private int itemID;
    private int quantity;
    private double price;
    private static int totalItems = 0; // counts how many Item objects have been created

    public Item(String name, int itemID, int quantity, double price) {
        setName(name);
        setItemID(itemID);
        setQuantity(quantity);
        setPrice(price);
        totalItems++;
    }

    public String getName() { return name; }
    public int getItemID() { return itemID; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setItemID(int itemID) {
        if (itemID <= 0) throw new IllegalArgumentException("Item ID must be positive");
        this.itemID = itemID;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public double getTotalValue() { return quantity * price; }
    public boolean isLowStock() { return quantity < 5; }

    public static int getTotalItems() { return totalItems; }

    @Override
    public String toString() {
        return String.format("Item{id=%d, name='%s', qty=%d, price=%.2f, value=%.2f}",
                itemID, name, quantity, price, getTotalValue());
    }
}

class Inventory {
    private Item[] items;
    private int itemCount;
    private static final int INITIAL_CAPACITY = 10;

    public Inventory() {
        items = new Item[INITIAL_CAPACITY];
        itemCount = 0;
    }

    public int getItemCount() { return itemCount; }

    public boolean addItem(Item item) {
        if (findItemByID(item.getItemID()) != null) {
            System.out.println("Item with this ID already exists. Use restock to add quantity.");
            return false;
        }
        ensureCapacity();
        items[itemCount++] = item;
        return true;
    }

    public boolean removeItemByID(int id) {
        int idx = indexOfId(id);
        if (idx == -1) return false;
        // shift left
        for (int i = idx + 1; i < itemCount; i++) {
            items[i - 1] = items[i];
        }
        items[--itemCount] = null;
        return true;
    }

    public Item findItemByID(int id) {
        int idx = indexOfId(id);
        return idx >= 0 ? items[idx] : null;
    }

    public Item[] findItemsByName(String name) {
        int matches = 0;
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getName().equalsIgnoreCase(name)) matches++;
        }
        Item[] res = new Item[matches];
        int j = 0;
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getName().equalsIgnoreCase(name)) res[j++] = items[i];
        }
        return res;
    }

    public boolean restock(int id, int amount) {
        if (amount <= 0) return false;
        Item it = findItemByID(id);
        if (it == null) return false;
        it.setQuantity(it.getQuantity() + amount);
        return true;
    }

    public boolean sell(int id, int amount) {
        if (amount <= 0) return false;
        Item it = findItemByID(id);
        if (it == null) return false;
        if (it.getQuantity() < amount) return false;
        it.setQuantity(it.getQuantity() - amount);
        return true;
    }

    public Item[] getLowStockItems() {
        int count = 0;
        for (int i = 0; i < itemCount; i++) if (items[i].isLowStock()) count++;
        Item[] res = new Item[count];
        int j = 0;
        for (int i = 0; i < itemCount; i++) if (items[i].isLowStock()) res[j++] = items[i];
        return res;
    }

    public double getTotalInventoryValue() {
        double sum = 0;
        for (int i = 0; i < itemCount; i++) sum += items[i].getTotalValue();
        return sum;
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inventory Report\n");
        sb.append("Count: ").append(itemCount).append(", Total value: ")
          .append(String.format("%.2f", getTotalInventoryValue())).append("\n");
        for (int i = 0; i < itemCount; i++) {
            sb.append(" - ").append(items[i]).append(items[i].isLowStock() ? " [LOW STOCK]" : "").append("\n");
        }
        return sb.toString();
    }

    private int indexOfId(int id) {
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getItemID() == id) return i;
        }
        return -1;
    }

    private void ensureCapacity() {
        if (itemCount >= items.length) resizeArray();
    }

    private void resizeArray() {
        Item[] newItems = new Item[items.length * 2];
        System.arraycopy(items, 0, newItems, 0, itemCount);
        items = newItems;
    }

    public Item[] toArray() {
        Item[] copy = new Item[itemCount];
        System.arraycopy(items, 0, copy, 0, itemCount);
        return copy;
    }
}

public class Task6InventoryApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Inventory inv = new Inventory();
        System.out.println("=== Task 6: Inventory Management System ===");

        int choice;
        do {
            printMenu();
            System.out.print("Enter choice: ");
            choice = readInt(sc);
            switch (choice) {
                case 1:
                    addItemFlow(sc, inv);
                    break;
                case 2:
                    restockFlow(sc, inv);
                    break;
                case 3:
                    sellFlow(sc, inv);
                    break;
                case 4:
                    searchByIdFlow(sc, inv);
                    break;
                case 5:
                    searchByNameFlow(sc, inv);
                    break;
                case 6:
                    lowStockFlow(inv);
                    break;
                case 7:
                    System.out.printf("Total inventory value: %.2f%n", inv.getTotalInventoryValue());
                    break;
                case 8:
                    System.out.println(inv.generateReport());
                    break;
                case 9:
                    removeItemFlow(sc, inv);
                    break;
                case 10:
                    System.out.println("Current items in inventory: " + inv.getItemCount());
                    System.out.println("Total Item objects ever created (static): " + Item.getTotalItems());
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1: Add new item");
        System.out.println("2: Restock item quantity");
        System.out.println("3: Sell item (reduce quantity)");
        System.out.println("4: Search item by ID");
        System.out.println("5: Search items by name");
        System.out.println("6: Display low stock warnings (< 5)");
        System.out.println("7: Calculate total inventory value");
        System.out.println("8: Generate inventory report");
        System.out.println("9: Remove item by ID");
        System.out.println("10: Show counts (current & static totalItems)");
        System.out.println("0: Exit");
    }

    private static void addItemFlow(Scanner sc, Inventory inv) {
        System.out.print("Item name: ");
        String name = readLine(sc);
        System.out.print("Item ID (positive integer): ");
        int id = readPositiveInt(sc);
        System.out.print("Initial quantity (>= 0): ");
        int qty = readNonNegativeInt(sc);
        System.out.print("Price (>= 0): ");
        double price = readNonNegativeDouble(sc);
        try {
            boolean ok = inv.addItem(new Item(name, id, qty, price));
            System.out.println(ok ? "Item added." : "Add failed (maybe duplicate ID).");
        } catch (IllegalArgumentException e) {
            System.out.println("Add failed: " + e.getMessage());
        }
    }

    private static void restockFlow(Scanner sc, Inventory inv) {
        System.out.print("Item ID: ");
        int id = readPositiveInt(sc);
        System.out.print("Amount to add (> 0): ");
        int amt = readPositiveInt(sc);
        boolean ok = inv.restock(id, amt);
        System.out.println(ok ? "Restocked successfully." : "Restock failed (check ID and amount).");
    }

    private static void sellFlow(Scanner sc, Inventory inv) {
        System.out.print("Item ID: ");
        int id = readPositiveInt(sc);
        System.out.print("Amount to sell (> 0): ");
        int amt = readPositiveInt(sc);
        boolean ok = inv.sell(id, amt);
        System.out.println(ok ? "Sale recorded." : "Sale failed (check ID, amount, and stock).");
    }

    private static void searchByIdFlow(Scanner sc, Inventory inv) {
        System.out.print("Item ID: ");
        int id = readPositiveInt(sc);
        Item it = inv.findItemByID(id);
        System.out.println(it != null ? it : "No item with that ID.");
    }

    private static void searchByNameFlow(Scanner sc, Inventory inv) {
        System.out.print("Item name: ");
        String name = readLine(sc);
        Item[] arr = inv.findItemsByName(name);
        if (arr.length == 0) {
            System.out.println("No items with that name.");
        } else {
            for (Item it : arr) System.out.println(it);
        }
    }

    private static void lowStockFlow(Inventory inv) {
        Item[] lows = inv.getLowStockItems();
        if (lows.length == 0) {
            System.out.println("No low stock items.");
        } else {
            System.out.println("Low stock items:");
            for (Item it : lows) System.out.println(" - " + it);
        }
    }

    private static void removeItemFlow(Scanner sc, Inventory inv) {
        System.out.print("Item ID to remove: ");
        int id = readPositiveInt(sc);
        boolean ok = inv.removeItemByID(id);
        System.out.println(ok ? "Removed." : "Remove failed (ID not found).");
    }

    // Input helpers
    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            sc.next();
        }
        return sc.nextInt();
    }

    private static int readPositiveInt(Scanner sc) {
        int v;
        do {
            v = readInt(sc);
            if (v <= 0) System.out.print("Please enter a positive integer: ");
        } while (v <= 0);
        return v;
    }

    private static int readNonNegativeInt(Scanner sc) {
        int v;
        do {
            v = readInt(sc);
            if (v < 0) System.out.print("Please enter a non-negative integer: ");
        } while (v < 0);
        return v;
    }

    private static double readNonNegativeDouble(Scanner sc) {
        while (true) {
            if (sc.hasNextDouble()) {
                double v = sc.nextDouble();
                if (v >= 0) return v;
            } else {
                sc.next();
            }
            System.out.print("Please enter a non-negative number: ");
        }
    }

    private static String readLine(Scanner sc) {
        String line = sc.nextLine();
        if (line.isEmpty()) line = sc.nextLine();
        return line.trim();
    }
}
