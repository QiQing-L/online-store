
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
           text colors
        ------------------------------------------------------------------ */
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String RED2 = "\u001B[91m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String YELLOW2 = "\u001B[93m";
    private static final String BLUE = "\u001B[34m";
    private static final String BLUE2 = "\u001B[94m";
    private static final String BOLD = "\u001B[1m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String WHITE2 = "\u001B[97m";
    private static final String CYAN = "\u001B[36m";
    private static final String CYAN2 = "\u001B[96m";

    /* ------------------------------------------------------------------
       Shared data
       ------------------------------------------------------------------ */
    private static final String listHeaderLine = String.format(BOLD + CYAN + "%-25s|%-40s|%s", "SKU", "Product Name", "Price" + RESET);



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
            System.out.println(YELLOW2 +  BOLD + "\nWelcome to the Online Store!\n" + RESET);
            System.out.println(WHITE2 + "1. Show Products" + RESET);
            System.out.println(WHITE2 + "2. Show Cart" + RESET);
            System.out.println(RED2 + "3. Exit" + RESET);
            System.out.print(CYAN2 + "Your choice: " + RESET );

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
    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {
        // TODO: show each product (id, name, price),
        //       prompt for an id, find that product, add to cart

        System.out.println(BOLD + "\nAll Available Products: \n" + RESET);
        System.out.println(listHeaderLine);
        try {
            for (Product product : inventory) {
                System.out.println(product);
            }
        } catch (Exception e) {
            System.out.println(RED + "Error displaying list. " + e + RESET);
        }

        while (true) {
            System.out.println(WHITE2 + "\nEnter a Product ID/SKU to add it to your cart,' \n" +
                    "Or type 'X' to return to the main menu." + RESET);
            String id = scanner.nextLine().trim().toLowerCase();

            if (id.equalsIgnoreCase("x")) {
                System.out.println(YELLOW + "Returning to home screen..." + RESET);
                break;
            }

            Product foundProduct = findProductById(id, inventory);

            if (foundProduct == null) {
                System.out.println(RED + "No product found with that ID. Please try again." + RESET);
            } else {
                cart.add(foundProduct);
                System.out.println(GREEN + "Product: "+ CYAN + foundProduct.getProductName() + GREEN + ", successfully added to your cart."  + RESET);
                break;
            }

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
        System.out.println(BOLD + "\nYour Shopping Cart:\n" + RESET);
        System.out.println(listHeaderLine);

        //This checks if cart is empty. If empty print message and return to home screen.
        if (cart.isEmpty()) {
            System.out.println(MAGENTA + "Your cart is empty. Please add a product before checking out.\n"
                    + YELLOW + "Returning to home screen..." + RESET);
            return;
        }

        double total = 0;
        for (Product product : cart) {
            System.out.println(product);
            total += product.getPrice();
        }

        //Print total
        System.out.printf(CYAN + "\nTotal: $%.2f\n" + RESET, total);

        // To check out or return
        System.out.println(WHITE2 + "\nEnter 'C' to check out or 'X' to return to the main menu." + RESET);
        String choice = scanner.nextLine().trim().toLowerCase();

        switch (choice) {
            case "c":
                checkOut(cart, total, scanner);
                break;
            case "x":
                System.out.println(GREEN + "Returning to home screen..." + RESET);
                break;
            default:
                System.out.println(YELLOW + "Invalid choice. Returning to home screen." + RESET);
                break;
        }

    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart, double totalAmount, Scanner scanner) {

        System.out.println(BOLD + BLUE + "\n--- CHECKOUT ---\n" + RESET);
        System.out.printf(CYAN + "Total amount owed: $%.2f\n" + RESET, totalAmount);

        // 1. Confirm purchase
        System.out.println(GREEN + "Would you like to proceed with your purchase? (Y/N)" + RESET);
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println(YELLOW + "Checkout canceled. Returning to cart." + RESET);
            return;
        }

        // 2️. Accept payment
        double payment;
        while (true) {
            System.out.print(CYAN + "Enter payment amount (cash): $" + RESET);
            String input = scanner.nextLine().trim();

            try {
                payment = Double.parseDouble(input);
                if (payment < totalAmount) {
                    double missingAmount = totalAmount - payment;
                    System.out.printf(RED + "Insufficient amount. You still owe $%.2f\n" + RESET, missingAmount);

                    // Ask if user want to retry or cancel check out
                    System.out.println(WHITE2 + "Would you like to try again? (Y to retry / N to cancel checkout)" + RESET);
                    String retry = scanner.nextLine().trim().toLowerCase();

                    if (!retry.equals("y")) {
                        System.out.println(YELLOW + "Checkout canceled. Returning to home screen." + RESET);
                        return;
                    }

                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println(RED + "Invalid input. Please enter a valid number." + e + RESET );
            }
        }

        // 3️. Calculate change and display receipt

        //4. Clear the cart
        cart.clear();
    }



    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
    // TODO: loop over the list and compare ids

        for (Product product : inventory) {
            if (id.equalsIgnoreCase(product.getSku())) {
                return product;
            }
        }
        return null;
    }

    public static void printAndSaveReceipt(ArrayList<Product> cart, double totalAmount, double payment){
        double change = payment - totalAmount;

        System.out.println(BOLD + BLUE2 + "\n---------Sales Receipt---------" + RESET);
        System.out.println(listHeaderLine);
        for (Product product : cart) {
            System.out.println(product);
        }
        System.out.printf(CYAN + "\nTotal: $%.2f\nPaid: $%.2f\nChange: $%.2f\n" + RESET,
                totalAmount, payment, change);

        System.out.println(GREEN + "\nThank you for your purchase!" + RESET);

    }
}
