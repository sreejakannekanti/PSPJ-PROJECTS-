import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

// ==========================================
// MODULE 4: Custom Exception
// ==========================================
class InvalidBidException extends Exception {
    public InvalidBidException(String message) {
        super(message);
    }
}

// ==========================================
// MODULE 4: Interface Definition
// ==========================================
interface Auditable {
    String getAuditSummary();
}

// ==========================================
// MODULE 4 & 5: Object-Oriented Modelling, Encapsulation & Comparable
// ==========================================
class AuctionItem implements Comparable<AuctionItem>, Auditable {
    private String itemId;
    private String title;
    private String category;
    private double currentBid;
    private String highestBidder;
    private boolean active;

    // Default Constructor
    public AuctionItem() {
        this.itemId = "UNKNOWN";
        this.title = "No Title";
        this.category = "GENERAL";
        this.currentBid = 0.0;
        this.highestBidder = "None";
        this.active = true;
    }

    // Parameterized Constructor (Module 4)
    public AuctionItem(String itemId, String title, String category, double startingPrice) {
        this.itemId = itemId.trim().toUpperCase();
        this.title = title.trim();
        this.category = category.trim().toUpperCase();
        this.currentBid = startingPrice;
        this.highestBidder = "None";
        this.active = true;
    }

    // Getters and Setters (Encapsulation - Module 4)
    public String getItemId() { return itemId; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public double getCurrentBid() { return currentBid; }
    public String getHighestBidder() { return highestBidder; }
    public boolean isActive() { return active; }
    public void closeAuction() { this.active = false; }

    // Method to place a bid with validation rules (Module 4)
    public void placeBid(String bidderName, double bidAmount) throws InvalidBidException {
        if (!active) {
            throw new InvalidBidException("Auction for item [" + itemId + "] is closed.");
        }
        if (bidAmount <= currentBid) {
            throw new InvalidBidException("Bid amount ($" + bidAmount + ") must exceed current bid ($" + currentBid + ").");
        }
        this.currentBid = bidAmount;
        this.highestBidder = bidderName.trim();
    }

    // MODULE 5: Comparable implementation for sorting items by current bid (Descending)
    @Override
    public int compareTo(AuctionItem other) {
        return Double.compare(other.currentBid, this.currentBid);
    }

    // MODULE 4: Interface method implementation
    @Override
    public String getAuditSummary() {
        return "Item: " + title + " | Category: " + category + " | Winning Bid: $" + currentBid + " (" + highestBidder + ")";
    }

    // MODULE 4: toString() Override
    @Override
    public String toString() {
        String status = active ? "ACTIVE" : "CLOSED";
        return String.format("ID: %-5s | Title: %-18s | Cat: %-10s | Current Bid: $%-8.2f | Bidder: %-12s | Status: %s",
                itemId, title, category, currentBid, highestBidder, status);
    }

    // Helper for File CSV Persistence (Module 5)
    public String toCsvString() {
        return itemId + "," + title + "," + category + "," + currentBid + "," + highestBidder + "," + active;
    }
}

// ==========================================
// MODULE 3: Recursion and Matrix Processing Utilities
// ==========================================
class AuctionAnalyticsUtils {
    // Recursive method calculating transaction loyalty bonus or fee tier (Module 3)
    public static long calculateTierBonus(int n) {
        if (n <= 1) return 10;
        return n + calculateTierBonus(n - 1);
    }

    // 2D Array Matrix processing for category statistics (Module 3)
    public static void processCategoryMatrix(int[][] matrix) {
        System.out.println("\n--- 2D Array Category Activity Matrix Analysis ---");
        for (int i = 0; i < matrix.length; i++) {
            int rowSum = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                rowSum += matrix[i][j];
            }
            System.out.println("Tier " + (i + 1) + " Total Bids Processed: " + rowSum);
        }
    }
}

// ==========================================
// MAIN CONTROLLER: OnlineAuctionSystem
// ==========================================
public class OnlineAuctionSystem {
    private static final String DATA_FILE = "auction_items.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // MODULE 6: Collections Framework & Generics
        List<AuctionItem> itemList = new ArrayList<>();
        Map<String, AuctionItem> itemMap = new HashMap<>();
        Set<String> uniqueCategories = new HashSet<>();

        // Load persistent records from file on startup (Module 5)
        loadDataFromFile(itemList, itemMap, uniqueCategories);

        int choice;
        // MODULE 2: do-while loop and switch-case menu navigation
        do {
            System.out.println("\n==================================================");
            System.out.println("      ONLINE AUCTION & BIDDING MANAGEMENT SYSTEM  ");
            System.out.println("==================================================");
            System.out.println("1. Add New Auction Item (Object Creation & Encapsulation - Mod 4)");
            System.out.println("2. View All Auction Catalog (Collections & Iteration - Mod 6)");
            System.out.println("3. Place a Bid on Item (Exception Handling - Mod 4)");
            System.out.println("4. Search Item using Binary Search / Optional (Mod 5 & 6)");
            System.out.println("5. Stream Analytics & Category Grouping (Stream API - Mod 6)");
            System.out.println("6. Run Recursive & Matrix Utilities (Mod 3)");
            System.out.println("7. Save & Exit (java.nio.file File I/O - Mod 5)");
            System.out.print("Enter your choice (1-7): ");

            // MODULE 1 & 2: Console validation using Scanner
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a valid number (1-7): ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume trailing newline

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter Item ID (e.g., A101): ");
                        String id = scanner.nextLine().trim().toUpperCase();
                        
                        if (itemMap.containsKey(id)) {
                            System.out.println(">> Error: Item ID already exists.");
                            break;
                        }

                        System.out.print("Enter Item Title: ");
                        String title = scanner.nextLine().trim();

                        System.out.print("Enter Category (Electronics/Art/Collectibles): ");
                        String cat = scanner.nextLine().trim().toUpperCase();

                        System.out.print("Enter Starting Price ($): ");
                        double price = scanner.nextDouble();
                        scanner.nextLine();

                        // Instantiate Object (Module 4)
                        AuctionItem newItem = new AuctionItem(id, title, cat, price);
                        itemList.add(newItem);
                        itemMap.put(id, newItem);
                        uniqueCategories.add(cat);

                        System.out.println(">> Success! Item added to catalog.");
                    } catch (Exception e) {
                        System.out.println(">> Input error: " + e.getMessage());
                    }
                    break;

                case 2:
                    // MODULE 6: Enhanced for-each loop and generics traversal
                    System.out.println("\n--- Complete Auction Item Catalog ---");
                    if (itemList.isEmpty()) {
                        System.out.println("Catalog is currently empty.");
                    } else {
                        for (AuctionItem item : itemList) {
                            System.out.println(item);
                        }
                    }
                    break;

                case 3:
                    // MODULE 4: Exception Handling (try-catch with custom exception)
                    System.out.print("Enter Item ID to bid on: ");
                    String targetId = scanner.nextLine().trim().toUpperCase();
                    AuctionItem selectedItem = itemMap.get(targetId);

                    if (selectedItem == null) {
                        System.out.println(">> Error: Item not found in system.");
                        break;
                    }

                    try {
                        System.out.print("Enter Bidder Name: ");
                        String bidder = scanner.nextLine().trim();

                        System.out.print("Enter Bid Amount ($): ");
                        double bidVal = scanner.nextDouble();
                        scanner.nextLine();

                        selectedItem.placeBid(bidder, bidVal);
                        System.out.println(">> Success! Bid registered successfully.");
                    } catch (InvalidBidException e) {
                        System.out.println(">> Bidding Rejected: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println(">> Invalid numeric format entered.");
                        scanner.nextLine();
                    }
                    break;

                case 4:
                    // MODULE 5 & 6: Binary search and Optional handling
                    System.out.print("Enter Item ID to search: ");
                    String searchId = scanner.nextLine().trim().toUpperCase();
                    
                    // Sorting array IDs for binary search demonstration (Module 5)
                    List<String> sortedIds = itemList.stream().map(AuctionItem::getItemId).sorted().collect(Collectors.toList());
                    String[] idArray = sortedIds.toArray(new String[0]);
                    Arrays.sort(idArray);

                    int index = Arrays.binarySearch(idArray, searchId);
                    Optional<AuctionItem> foundItem = Optional.ofNullable(itemMap.get(searchId));

                    if (index >= 0 && foundItem.isPresent()) {
                        System.out.println(">> Item Found via Binary Search & Map Lookup:");
                        System.out.println("   " + foundItem.get());
                    } else {
                        System.out.println(">> Item ID '" + searchId + "' not found.");
                    }
                    break;

                case 5:
                    // MODULE 6: Stream API pipelines, Filtering, Sorting, and Collectors groupingBy
                    System.out.println("\n--- Stream Analytics: Active High-Value Items (Bid >= $200) ---");
                    List<AuctionItem> highValueItems = itemList.stream()
                            .filter(i -> i.isActive() && i.getCurrentBid() >= 200.0)
                            .sorted() // Uses Comparable interface
                            .collect(Collectors.toList());

                    if (highValueItems.isEmpty()) {
                        System.out.println("No active items found above $200.");
                    } else {
                        highValueItems.forEach(i -> System.out.println("   " + i.getAuditSummary()));
                    }

                    System.out.println("\n--- Category Grouping Summary (Collectors.groupingBy) ---");
                    Map<String, Long> categoryCount = itemList.stream()
                            .collect(Collectors.groupingBy(AuctionItem::getCategory, Collectors.counting()));
                    categoryCount.forEach((cat, count) -> System.out.println("Category: " + cat + " -> " + count + " item(s)"));
                    break;

                case 6:
                    // MODULE 3: Recursion and 2D Array Matrix
                    System.out.print("Enter tier level for loyalty bonus calculation (Recursion): ");
                    int tier = scanner.nextInt();
                    scanner.nextLine();
                    if(tier > 0) {
                        long bonus = AuctionAnalyticsUtils.calculateTierBonus(tier);
                        System.out.println(">> Calculated Loyalty Bonus: $" + bonus);
                    }

                    // 2D Array matrix demonstration
                    int[][] activityMatrix = {
                        {12, 15, 8},
                        {20, 25, 30}
                    };
                    AuctionAnalyticsUtils.processCategoryMatrix(activityMatrix);
                    break;

                case 7:
                    // MODULE 5: File I/O persistence using java.nio.file with try-with-resources
                    saveDataToFile(itemList);
                    System.out.println(">> All auction data persisted successfully. Exiting system. Goodbye!");
                    break;

                default:
                    System.out.println(">> Invalid option. Please choose between 1 and 7.");
            }
        } while (choice != 7);

        scanner.close();
    }

    // ==========================================
    // MODULE 5: File Handling Implementation (java.nio.file)
    // ==========================================
    private static void saveDataToFile(List<AuctionItem> items) {
        Path path = Paths.get(DATA_FILE);
        List<String> lines = items.stream().map(AuctionItem::toCsvString).collect(Collectors.toList());
        try {
            // Using modern Java nio.file Files API
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println(">> File Write Error: " + e.getMessage());
        }
    }

    private static void loadDataFromFile(List<AuctionItem> list, Map<String, AuctionItem> map, Set<String> categories) {
        Path path = Paths.get(DATA_FILE);
        if (Files.exists(path)) {
            try (var reader = Files.newBufferedReader(path)) { // Try-with-resources (Module 5)
                List<String> lines = Files.readAllLines(path);
                for (String line : lines) {
                    String[] tokens = line.split(",");
                    if (tokens.length == 6) {
                        AuctionItem item = new AuctionItem(tokens[0], tokens[1], tokens[2], Double.parseDouble(tokens[3]));
                        // If there was a higher bid stored, apply it
                        if (!tokens[4].equals("None")) {
                            try {
                                item.placeBid(tokens[4], Double.parseDouble(tokens[3]));
                            } catch (Exception ignored) {}
                        }
                        if (!Boolean.parseBoolean(tokens[5])) {
                            item.closeAuction();
                        }
                        list.add(item);
                        map.put(tokens[0], item);
                        categories.add(tokens[2]);
                    }
                }
                System.out.println(">> Loaded " + list.size() + " auction records from storage file.");
            } catch (Exception e) {
                System.out.println(">> Error reading storage file: " + e.getMessage());
            }
        }
    }
}