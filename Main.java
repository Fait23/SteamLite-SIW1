import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String GAMES_FILE = "C:\\Users\\Hello User\\IdeaProjects\\SIW\\src\\games.txt";
    private static final String LOG_FILE = "sales_log.txt";

    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        AdminPanel admin = new AdminPanel(engine);
        Developer dev = new Developer(1, "StudioX");
        Scanner scanner = new Scanner(System.in);

        loadGamesFromFile(GAMES_FILE, admin);

        boolean mainRunning = true;

        while (mainRunning) {
            System.out.println("\n==================================================");
            System.out.println("              WELCOME TO STEAM LITE               ");
            System.out.println("==================================================");
            System.out.println("1 - Log in as ADMIN (Moderation & Management)");
            System.out.println("2 - Log in as USER (" + dev.getUsername() + ")");
            System.out.println("0 - Power Off System");
            System.out.print("Select role: ");

            int role = getUserInt(scanner);

            if (role == 1) {
                runAdminMenu(scanner, admin, engine);
            } else if (role == 2) {
                runUserMenu(scanner, engine, dev);
            } else if (role == 0) {
                System.out.println("System shutting down...");
                mainRunning = false;
            } else {
                System.out.println("Invalid option! Please select 1, 2, or 0.");
            }
        }
        scanner.close();
    }

    private static void loadGamesFromFile(String filename, AdminPanel admin) {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                String type = parts[0].trim();
                int id = Integer.parseInt(parts[1].trim());
                String title = parts[2].trim();
                double price = Double.parseDouble(parts[3].trim());
                double rating = Double.parseDouble(parts[4].trim());

                if (type.equalsIgnoreCase("PC")) {
                    int ram = Integer.parseInt(parts[5].trim());
                    double storage = Double.parseDouble(parts[6].trim());
                    admin.submitGameForReview(new PCGame(id, title, price, rating, ram, storage));
                } else if (type.equalsIgnoreCase("MOBILE")) {
                    boolean iap = Boolean.parseBoolean(parts[5].trim());
                    double size = Double.parseDouble(parts[6].trim());
                    admin.submitGameForReview(new MobileGame(id, title, price, rating, iap, size));
                } else if (type.equalsIgnoreCase("CONSOLE")) {
                    String console = parts[5].trim();
                    boolean disc = Boolean.parseBoolean(parts[6].trim());
                    admin.submitGameForReview(new ConsoleGame(id, title, price, rating, console, disc));
                }
            }
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    private static void saveLogToFile(String logMessage) {
        File file = new File(LOG_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException ignored) {
        }
    }

    private static void runAdminMenu(Scanner scanner, AdminPanel admin, GameEngine engine) {
        boolean adminRunning = true;
        while (adminRunning) {
            System.out.println("\n--- ADMIN DASHBOARD ---");
            System.out.println("1 - Process next game in moderation queue (FIFO)");
            System.out.println("2 - Approve ALL games in queue (Auto-assign Action)");
            System.out.println("3 - Edit game price (O(1) Search)");
            System.out.println("4 - Delete game from store (Sync CRUD)");
            System.out.println("5 - View Store Statistics");
            System.out.println("0 - Logout to Main Menu");
            System.out.print("Select action: ");

            int choice = getUserInt(scanner);
            switch (choice) {
                case 1:
                    admin.processNextGameRequest("Mixed");
                    break;
                case 2:
                    admin.processAllRequests("Action");
                    break;
                case 3:
                    System.out.print("Enter Game ID to update: ");
                    int idToUpdate = getUserInt(scanner);

                    System.out.print("Enter new price: ");
                    double newPrice = getUserDouble(scanner);
                    while (newPrice < 0) {
                        System.out.print("Price cannot be negative! Enter valid new price: ");
                        newPrice = getUserDouble(scanner);
                    }

                    if (engine.updateGamePrice(idToUpdate, newPrice)) {
                        System.out.println("Price updated successfully.");
                    } else {
                        System.out.println("Update failed. Game not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter Game ID to delete: ");
                    int idToDelete = getUserInt(scanner);
                    if (engine.removeGame(idToDelete)) {
                        System.out.println("Game completely removed from all databases.");
                    } else {
                        System.out.println("Delete failed. Game not found.");
                    }
                    break;
                case 5:
                    engine.printStoreStatistics();
                    break;
                case 0:
                    adminRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void runUserMenu(Scanner scanner, GameEngine engine, Developer dev) {
        System.out.println("\n>>> WELCOME! HERE IS OUR CURRENT STORE CATALOG <<<");
        printList(engine.getGamesSortedByPrice());

        boolean userRunning = true;
        while (userRunning) {
            System.out.println("\n--- STOREFRONT (Logged in as: " + dev.getUsername() + ") ---");
            System.out.println("1 - View Catalog (Sort: Price ASC)");
            System.out.println("2 - View Catalog (Sort: Rating DESC)");
            System.out.println("3 - View Catalog (Multi-Sort: Price & Rating)");
            System.out.println("4 - Filter: Free to Play Only");
            System.out.println("5 - Filter: By Genre (Action)");
            System.out.println("6 - BUY A GAME");
            System.out.println("7 - My Library & Play Games");
            System.out.println("0 - Logout to Main Menu");
            System.out.print("Select action: ");

            int choice = getUserInt(scanner);
            switch (choice) {
                case 1:
                    printList(engine.getGamesSortedByPrice());
                    break;
                case 2:
                    printList(engine.getGamesSortedByRatingDesc());
                    break;
                case 3:
                    printList(engine.getGamesSortedByPriceAndRating());
                    break;
                case 4:
                    printList(engine.filterFreeGames());
                    break;
                case 5:
                    printList(engine.getGamesByCategory("Action"));
                    break;
                case 6:
                    System.out.print("Enter Game ID to buy: ");
                    int buyId = getUserInt(scanner);
                    Game gameToBuy = engine.getGameById(buyId);

                    if (gameToBuy != null) {
                        PaymentMethod method = selectPaymentMethod(scanner);
                        if (method == null) {
                            System.out.println("Purchase canceled.");
                            break;
                        }

                        if (dev.buyGame(gameToBuy)) {
                            System.out.println("Success! Payment processed via " + method + ".");
                            engine.recordSale(gameToBuy.getPrice());


                            String log = "Purchase: " + gameToBuy.getTitle() +
                                    " (ID:" + gameToBuy.getId() + ") by " +
                                    dev.getUsername() +
                                    " | Price: $" + gameToBuy.getPrice() +
                                    " | Method: " + method;
                            saveLogToFile(log);
                        } else {
                            System.out.println("Error: You already own this game!");
                        }
                    } else {
                        System.out.println("Error: Game not found in the store catalog.");
                    }
                    break;
                case 7:
                    System.out.println("\n=== MY LIBRARY ===");
                    List<Game> owned = dev.getOwnedGames();
                    if (owned.isEmpty()) {
                        System.out.println("Library is empty.");
                        break;
                    }

                    for (Game g : owned) {
                        System.out.println(g.getId() + " - " + g.getTitle() + " [" + g.getPlatform() + "]");
                    }

                    System.out.print("Enter Game ID to play (or 0 to go back): ");
                    int playId = getUserInt(scanner);
                    if (playId == 0) break;

                    boolean foundToPlay = false;
                    for (Game g : owned) {
                        if (g.getId() == playId) {
                            g.play();

                            if (g instanceof MobileGame) {
                                int adChoice = 0;
                                while (adChoice != 1 && adChoice != 2) {
                                    System.out.print("Want to watch a short ad to earn a reward? (1 - Yes, 2 - No): ");
                                    adChoice = getUserInt(scanner);
                                    if (adChoice == 1) {
                                        ((MobileGame) g).watchAdForRewards();
                                    } else if (adChoice != 2) {
                                        System.out.println("Invalid choice. Please enter 1 for Yes or 2 for No.");
                                    }
                                }
                            }
                            foundToPlay = true;
                            break;
                        }
                    }
                    if (!foundToPlay) {
                        System.out.println("Game not found in your library.");
                    }
                    break;
                case 0:
                    userRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static PaymentMethod selectPaymentMethod(Scanner scanner) {
        while (true) {
            System.out.println("\nSelect Payment Method:");
            System.out.println("1 - CREDIT CARD | 2 - PAYPAL | 3 - CRYPTO | 0 - Cancel");
            System.out.print("Select option: ");
            int payChoice = getUserInt(scanner);

            switch (payChoice) {
                case 1:
                    return PaymentMethod.CREDIT_CARD;
                case 2:
                    return PaymentMethod.PAYPAL;
                case 3:
                    return PaymentMethod.CRYPTO;
                case 0:
                    return null;
                default:
                    System.out.println("Invalid payment method! Please choose 1, 2, 3, or 0 to cancel.");
            }
        }
    }

    private static void printList(List<Game> games) {
        System.out.println();
        if (games.isEmpty()) {
            System.out.println("No games found. (Admin might need to approve games first)");
        } else {
            for (Game g : games) System.out.println(g);
        }
    }

    private static int getUserInt(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a valid number: ");
            }
        }
    }

    private static double getUserDouble(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a valid decimal number: ");
            }
        }
    }
}
