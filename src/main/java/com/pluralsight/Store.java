
package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starter code for the Online Store workshop.
 * Students will complete the TODO sections to make the program work.
 */
public class Store {
    /* ------------------------------------------------------------------
       Shared data
       ------------------------------------------------------------------ */

    private static final String listHeaderLine = String.format("%-30s|%-40s|%s", "SKU", "Product Name", "Price");

    /* ------------------------------------------------------------------
           text colors
          ------------------------------------------------------------------ */
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[93m";
    private static final String BLUE = "\u001B[34m";
    private static final String BLUE2 = "\u001B[94m";
    private static final String BOLD = "\u001B[1m";
    private static final String WHITE2 = "\u001B[97m";
    private static final String CYAN = "\u001B[36m";


    /* ------------------------------------------------------------------
       Main menu
       ------------------------------------------------------------------ */

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        // Load inventory from the data file (pipe-delimited: id|name|price)
        loadInventory("products.csv", inventory);

        // Main menu loop
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                scanner.nextLine();                 // discard bad input
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();                     // clear newline

            switch (choice) {
                case 1 -> displayProducts(inventory, cart, scanner);
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();

    }

    /**
     * Reads product data from a file and populates the inventory list.
     * File format (pipe-delimited):
     * id|name|price
     * <p>
     * Example line:
     * A17|Wireless Mouse|19.99
     */
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        // TODO: read each line, split on "|",
        //       create a Product object, and add it to the inventory list

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String sku = parts[0];
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);

                inventory.add(new Product(sku,name,price));
            }
            reader.close();

        } catch (Exception e) {
            System.out.println(RED + "Error, Unable to read file. " + fileName + e + RESET);
        }

    }

    /**
     * Displays all products and lets the user add one to the cart.
     * Typing X returns to the main menu.
     */
    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {
        // TODO: show each product (id, name, price),
        //       prompt for an id, find that product, add to cart

        System.out.println("All Available Products: ");
        System.out.println(listHeaderLine);
        try {
            for (Product product : inventory) {
                System.out.println(product);
            }
        } catch (Exception e) {
            System.out.println(RED + "Error displaying list. " + e + RESET);
        }

        System.out.println(WHITE2 + "\nTo add a product to cart enter 'Y' and follow the prompt.' \n" +
                "To go back to the home screen enter 'X'" + RESET);
        String choice = scanner.nextLine().trim().toLowerCase();
        switch (choice) {
            case "y":
                System.out.print(CYAN + "Please enter the Product ID/SKU: " + RESET);
                String id = scanner.nextLine().trim();
                findProductById(id, inventory);
                break;

            case "x":
                System.out.println(GREEN + "Exit to home screen. " + RESET);
                break;

            default:
                System.out.println(YELLOW + "Invalid command. Return to home screen. " + RESET);
                break;
        }


    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        // TODO:
        //   • list each product in the cart
        //   • compute the total cost
        //   • ask the user whether to check out (C) or return (X)
        //   • if C, call checkOut(cart, totalAmount, scanner)
    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        // TODO: implement steps listed above
    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // TODO: loop over the list and compare ids
        return null;
    }
}


