// OnlineAuctionSystem.java
// Combined implementation of CO1, CO2, CO3

import java.util.Scanner;

public class OnlineAuctionSystem {

    // ---------- CO1: Data Types & Data Storage ----------
    static final int MAX_ITEMS = 5;
    static final int MAX_BIDS  = 10;

    static int[]      itemIds       = new int[MAX_ITEMS];        // 1-D Array (int)
    static String[]   itemNames     = new String[MAX_ITEMS];     // 1-D Array (String)
    static double[]   basePrices    = new double[MAX_ITEMS];     // 1-D Array (double)
    static boolean[]  isActive      = new boolean[MAX_ITEMS];    // 1-D Array (boolean)
    static double[][] bids          = new double[MAX_ITEMS][MAX_BIDS]; // 2-D Array
    static int[]      bidCount      = new int[MAX_ITEMS];        // bids per item
    static int        itemCount     = 0;

    static Scanner sc = new Scanner(System.in);

    // ---------- CO3: Method to Register Item ----------
    static void registerItem() {
        if (itemCount >= MAX_ITEMS) {
            System.out.println(">> Item limit reached!");
            return;
        }

        System.out.print("Enter Item ID (int)        : ");
        itemIds[itemCount] = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter Item Name (String)   : ");
        itemNames[itemCount] = sc.nextLine();

        System.out.print("Enter Base Price (double)  : ");
        basePrices[itemCount] = sc.nextDouble();

        System.out.print("Is item active? (true/false): ");
        isActive[itemCount] = sc.nextBoolean();

        bidCount[itemCount] = 0;
        itemCount++;

        System.out.println(">> Item registered successfully!");
    }

    // ---------- CO3: Method to Display Items (uses CO2 for-loop) ----------
    static void displayItems() {
        if (itemCount == 0) {
            System.out.println(">> No items available.");
            return;
        }

        System.out.println("\n----- Available Auction Items -----");
        System.out.printf("%-5s %-6s %-20s %-12s %-10s%n",
                "No.", "ID", "Name", "Base Price", "Active");
        System.out.println("----------------------------------------------------------");

        for (int i = 0; i < itemCount; i++) {
            System.out.printf("%-5d %-6d %-20s %-12.2f %-10s%n",
                    (i + 1), itemIds[i], itemNames[i], basePrices[i], isActive[i]);
        }
    }

    // ---------- CO3: Method to Get Highest Bid ----------
    static double getHighestBid(int idx) {
        double highest = basePrices[idx];
        for (int i = 0; i < bidCount[idx]; i++) {
            if (bids[idx][i] > highest) {
                highest = bids[idx][i];
            }
        }
        return highest;
    }

    // ---------- CO3: Recursive Method to Sum All Bids ----------
    static double sumBids(int itemIdx, int bidIdx) {
        if (bidIdx >= bidCount[itemIdx]) {           // base case
            return 0;
        }
        return bids[itemIdx][bidIdx] + sumBids(itemIdx, bidIdx + 1); // recursive call
    }

    // ---------- CO2 + CO3: Place Bid with Validation ----------
    static void placeBid() {
        displayItems();
        if (itemCount == 0) return;

        System.out.print("\nSelect item number: ");
        int idx = sc.nextInt() - 1;

        // CO2: Selection (if-else) for validation
        if (idx < 0 || idx >= itemCount) {
            System.out.println(">> Invalid item number!");
            return;
        }

        if (!isActive[idx]) {
            System.out.println(">> This item is not active for bidding.");
            return;
        }

        if (bidCount[idx] >= MAX_BIDS) {
            System.out.println(">> Bid limit reached for this item.");
            return;
        }

        System.out.print("Enter your bid amount: ");
        double amount = sc.nextDouble();

        double highest = getHighestBid(idx);

        // CO2: Selection with nested conditions
        if (amount <= basePrices[idx]) {
            System.out.println(">> Bid must be at least the base price: " + basePrices[idx]);
        } else if (amount <= highest && bidCount[idx] > 0) {
            System.out.println(">> Bid too low. Current highest is: " + highest);
        } else {
            bids[idx][bidCount[idx]] = amount;
            bidCount[idx]++;
            System.out.println(">> Bid placed successfully! You are now the highest bidder.");
        }
    }

    // ---------- CO3: Show Detailed Results (uses recursion) ----------
    static void showResults() {
        if (itemCount == 0) {
            System.out.println(">> No items to display.");
            return;
        }

        System.out.println("\n=========== AUCTION RESULTS ===========");
        for (int i = 0; i < itemCount; i++) {
            System.out.println("Item ID       : " + itemIds[i]);
            System.out.println("Item Name     : " + itemNames[i]);
            System.out.println("Base Price    : " + basePrices[i]);
            System.out.println("Highest Bid   : " + getHighestBid(i));
            System.out.println("Total Bids    : " + bidCount[i]);
            System.out.println("Sum of Bids   : " + sumBids(i, 0)); // recursion
            System.out.println("---------------------------------------");
        }
    }

    // ---------- CO3: Search Item by ID ----------
    static int searchItemById(int id) {
        for (int i = 0; i < itemCount; i++) {
            if (itemIds[i] == id) return i;
        }
        return -1;
    }

    static void searchMenu() {
        System.out.print("Enter Item ID to search: ");
        int id = sc.nextInt();
        int idx = searchItemById(id);

        if (idx == -1) {
            System.out.println(">> Item not found.");
        } else {
            System.out.println(">> Item Found: " + itemNames[idx] +
                               " | Base Price: " + basePrices[idx] +
                               " | Highest Bid: " + getHighestBid(idx));
        }
    }

    // ---------- CO2: Main Menu with do-while loop ----------
    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("   WELCOME TO ONLINE AUCTION & BIDDING    ");
        System.out.println("==========================================");

        int choice;

        do {
            System.out.println("\n============= MAIN MENU =============");
            System.out.println("1. Register New Item");
            System.out.println("2. View All Items");
            System.out.println("3. Place a Bid");
            System.out.println("4. Search Item by ID");
            System.out.println("5. Show Auction Results");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1: registerItem();   break;   // CO1 + CO3
                case 2: displayItems();   break;   // CO2 + CO3
                case 3: placeBid();       break;   // CO2 + CO3
                case 4: searchMenu();     break;   // CO3
                case 5: showResults();    break;   // CO3 (recursion)
                case 6: System.out.println("\n>> Thank you for using the Auction System!");
                        break;
                default: System.out.println(">> Invalid choice! Try again.");
            }

        } while (choice != 6);   // CO2: iteration

        sc.close();
    }
}