import java.io.BufferedReader;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Custom exception classes for number-related exceptions
class NumberException extends Exception {
    NumberException(String errMsg) {
        super(errMsg);
    }
}

class NegativeNumberException extends NumberException {
    NegativeNumberException(String errMsg) {
        super(errMsg);
    }
}

class ZeroNumberException extends NumberException {
    ZeroNumberException(String errMsg) {
        super(errMsg);
    }
}

// RetailStore class that implements the Runnable interface
public class RetailStore implements Runnable {
    // Instance variables
    private Scanner scan;
    private Console con;
    private ArrayList<User> users;
    private ArrayList<Product> products;

    // Private constructor to initialize instance variables
    private RetailStore() {
        this.scan = new Scanner(System.in);
        this.con = System.console();
        this.users = new ArrayList<User>();
        this.products = new ArrayList<Product>();
        this.fetchUsers();
        this.fetchProducts();
    }

    // Factory method to create a RetailStore instance
    public static RetailStore makeInstance() {
        return new RetailStore();
    }

    // Method to start the RetailStore application
    public void begin() {
        this.displayHomeMenu();
    }

    // Method to display the home header
    private void displayHomeHeader() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("+--------------------------------------------+");
        System.out.println("|                                            |");
        System.out.println("| =======> Retail Store Application <======= |");
        System.out.println("|                                            |");
        System.out.println("+--------------------------------------------+");
        System.out.println();
    }

    // Method to display the home menu text
    private void displayHomeMenuTxt() {
        this.displayHomeHeader();
        System.out.println("1. Sign In");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.println("Enter your choice: ");
    }

    // Method to handle the home menu options
    private void displayHomeMenu() {
        int choice = -1;

        while(choice != 3) {
            this.displayHomeMenuTxt();
            try {
                if(choice == -2) {
                    this.scan.nextLine();
                }
                choice = this.scan.nextInt();
            } catch(InputMismatchException e) {
                System.err.println("\nPlease enter number input\n");
                System.out.println("Press enter to continue");

                choice = -2;
                this.con.readLine();
                continue;
            }

            switch(choice) {
                case 1:
                    this.displaySignInMenu();
                    break;
                case 2:
                    this.handleRegistration();
                    break;
                case 3:
                    System.out.println("\nGoodbye...");
                    break;
                default:
                    System.out.println("\nINVALID INPUT\n");
                    System.out.println("Press enter to continue");
                    this.con.readLine();
            }
        }
    }

    // Method to display the sign-in menu and handle user input
    private void displaySignInMenu() {
        int choice = -1;
        while (choice != 1 && choice != 2) {
            System.out.println("\nChoose an option:- ");
            System.out.println("1. Enter sign in details");
            System.out.println("2. Forgot password");
            System.out.println("Enter your choice: ");

            try {
                if(choice == -2) {
                    this.scan.nextLine();
                }

                choice = this.scan.nextInt();
            } catch(InputMismatchException e) {
                System.err.println("\nPlease enter number input\n");
                System.out.println("Press enter to continue");

                choice = -2;
                this.con.readLine();
                continue;
            }

            switch (choice) {
                case 1:
                    User user = this.handleSignIn();
                    if (user != null) {
                        this.handleSignedInUser(user);
                    }
                    break;
                case 2:
                    this.handleForgotPassword();
                    break;
                default:
                    System.out.println("\nINVALID INPUT\n");
                    System.out.println("Press enter to continue");
                    this.con.readLine();
            }
        }
    }

    // Method to handle user sign-in and return the signed-in User object or null
    private User handleSignIn() {
        String username = this.getUsernameInput();
        String password = this.getPasswordInput("Enter password: ");

        // Check if the entered username and password match any registered user
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password.hashCode() + "")) {
                    return user;
                } else {
                    System.out.println("\nInvalid password\n");
                    System.out.println("Press enter to continue");
                    this.con.readLine();
                    return null;
                }
            }
        }
        System.out.println("\nUser is not registered\n");
        System.out.println("Press enter to continue");
        this.con.readLine();
        return null;
    }

    // Method to handle the signed-in user and display the appropriate view
    private void handleSignedInUser(User user) {
        System.out.println("\nSuccessfully signed in\n");
        System.out.println("Press enter to continue");
        this.con.readLine();

        if(this.isAdmin(user)) {
            this.displayAdminView((Admin) user);
        } else {
            this.fetchPurchasedProducts((Customer) user);
            this.displayCustomerView((Customer) user);
        }
    }

    // Method to display the admin view and handle admin options
    private void displayAdminView(Admin adminUser) {
        int choice = -1;
        while(choice != 4) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("\n=================================================================================> Admin Panel <=================================================================================\n");
            
            this.displayAvailableProductCatalogue();
            
            System.out.println("Choose an option");
            System.out.println("1. Add product");
            System.out.println("2. Get total number of products available at the store");
            System.out.println("3. Calculate maximum profit that can be made from the sale of the products");
            System.out.println("4. Logout");
            System.out.println("Enter your choice: ");

            try {
                if(choice == -2) {
                    this.scan.nextLine();
                }
                choice = this.scan.nextInt();
            } catch(InputMismatchException e) {
                System.err.println("\nPlease enter number input\n");
                System.out.println("Press enter to continue");

                choice = -2;
                this.con.readLine();
                continue;
            }

            switch(choice) {
                case 1:
                    if(adminUser.addProduct(getProductInput()) && this.saveProducts()) {
                        System.out.println("\nProduct added successfully to the catalogue\n");
                    } else {
                        System.out.println("\nFailed to add product to the catalogue\n");
                    }
                    break;

                case 2:
                    System.out.println("\nTotal number of products available at the store: " + adminUser.getTotalAvailableProducts() + "\n");
                    break;
                    
                case 3:
                    System.out.println("\nMaximum profit that can be made from the sale of the products: " + adminUser.getMaximumProfit() + "\n");
                    break;

                case 4:
                    System.out.println("\nYou are logged out\n");
                    break;

                default:
                    System.out.println("\nINVALID INPUT\n");
            }
            
            System.out.println("Press enter to continue");
            this.con.readLine();
        }
    }
//

private void displayCustomerView(Customer customer) {
    int choice = -1;
    while(choice != 4) {
        // Clear the console
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Display the Customer Panel menu
        System.out.println("\n===============================================================================> Customer Panel <================================================================================\n");

        // Create a new RetailStore and run it in a separate thread
        RetailStore retailStore = new RetailStore();
        Thread thread = new Thread(retailStore);
        thread.start();

        // Wait for the thread to finish
        while(thread.isAlive()) {}

        // Display menu options and get user input
        System.out.println("Choose an option");
        System.out.println("1. Purchase product");
        System.out.println("2. Cancel purchased product");
        System.out.println("3. Display Profile");
        System.out.println("4. Logout");
        System.out.println("Enter your choice: ");

        // Handle user input and catch any input mismatch exceptions
        try {
            if(choice == -2) {
                this.scan.nextLine();
            }
            choice = this.scan.nextInt();
        } catch(InputMismatchException e) {
            System.err.println("\nPlease enter number input\n");
            System.out.println("Press enter to continue");

            choice = -2;
            this.con.readLine();
            continue;
        }

        // Perform the selected menu option
        switch(choice) {
            case 1:
                // Purchase a product
                if(customer.purchaseProduct(this.getProductIdInput(), this.getPurchaseQtyInput()) && this.savePurchasedProducts(customer) && this.saveProducts()) {
                    System.out.println("\nSuccessfully purchased the product\n");
                } else {
                    System.out.println("\nFailed to purchase the product\n");
                }
                break;

            case 2:
                // Cancel a purchased product
                int status = customer.cancelPurchasedProduct(this.getProductIdInput());

                if(status == -1) {
                    System.out.println("\nProduct not available\n");
                } else if(status == -2) {
                    System.out.println("\nFailed to cancel the purchased product\n");
                } else {
                    this.savePurchasedProducts(customer);
                    System.out.println("\nProduct returned successfully\n");
                }

                if(status > 0) {
                    System.out.println("Total Fine of " + status + " rupees due to late return of product\n");
                }
                break;

            case 3:
                // Display customer profile
                customer.displayProfile();
                break;

            case 4:
                // Logout
                System.out.println("\nYou are logged out\n");
                break;

            default:
                System.out.println("\nINVALID INPUT\n");
        }

        // Wait for the user to press enter before continuing
        System.out.println("Press enter to continue");
        this.con.readLine();
    }
}

// Get product ID input from the user
private int getProductIdInput() {
    int productId = -1;
    while(productId < 0) {
        System.out.println("Enter product id: ");
        productId = this.handleIdInput(productId);
        if(productId == -2) {
            continue;
        }
    }
    return productId;
}

// Get purchase quantity input from the user
private int getPurchaseQtyInput() {
    int qty = -1;
    while(qty < 0) {
        System.out.println("Enter qty: ");
        qty = this.handleQtyInput(qty);
        if(qty == -2) {
            continue;
        }
    }
    return qty;
}

    //


// Handle the input for product ID, quantity, and price
// Catch any exceptions related to input mismatch, negative numbers or zero input

private int handleIdInput(int id) {
    try {
        if(id == -2) {
            this.scan.nextLine();
        }

        // Read the input for the product ID
        id = this.scan.nextInt();

        // Check if the input is negative or zero, and throw the appropriate exception
        if(id < 0) {
            throw new NegativeNumberException("Id cannot be negative");
        } else if(id == 0) {
            throw new ZeroNumberException("Id cannot be zero");
        }
        return id;
    } catch(InputMismatchException e) {
        // Handle input mismatch exception
        System.err.println("\nPlease enter number input\n");
        System.out.println("Press enter to continue");

        id = -2;
        this.con.readLine();
        return id;
    } catch(NegativeNumberException e) {
        // Handle negative number exception
        System.err.println("\n" + e.getMessage() + "\n");
        System.out.println("Press enter to continue");

        id = -2;
        this.con.readLine();
        return id;
    } catch(ZeroNumberException e) {
        // Handle zero number exception
        System.err.println("\n" + e.getMessage() + "\n");
        System.out.println("Press enter to continue");

        id = -2;
        this.con.readLine();
        return id;
    }
}

private int handleQtyInput(int qty) {
    try {
        if(qty == -2) {
            this.scan.nextLine();
        }

        // Read the input for the purchase quantity
        qty = this.scan.nextInt();

        // Check if the input is negative, and throw the appropriate exception
        if(qty < 0) {
            throw new NegativeNumberException("Quantity cannot be negative");
        } 

        return qty;
    } catch(InputMismatchException e) {
        // Handle input mismatch exception
        System.err.println("\nPlease enter number input\n");
        System.out.println("Press enter to continue");

        qty = -2;
        this.con.readLine();
        return qty;
    } catch(NegativeNumberException e) {
        // Handle negative number exception
        System.err.println("\n" + e.getMessage() + "\n");
        System.out.println("Press enter to continue");

        qty = -2;
        this.con.readLine();
        return qty;
    }
}

private double handlePriceInput(double price) {
    try {
        if(price == -2) {
            this.scan.nextLine();
        }

        // Read the input for the product price
        price = this.scan.nextDouble();

        // Check if the input is negative, and throw the appropriate exception
        if(price < 0) {
            throw new NegativeNumberException("Price cannot be negative");
        } 

        return price;
    } catch(InputMismatchException e) {
        // Handle input mismatch exception
        System.err.println("\nPlease enter number input\n");
        System.out.println("Press enter to continue");

        price = -2;
        this.con.readLine();
        return price;
    } catch(NegativeNumberException e) {
        // Handle negative number exception
        System.err.println("\n" + e.getMessage() + "\n");
        System.out.println("Press enter to continue");

        price = -2;
        this.con.readLine();
        return price;
    }
}




    //
// Get product input from the user and create a new Product object
private Product getProductInput() {
    int id;
    String name;
    int availableQty;
    double price;
    double discountedPrice;
    Date returnPeriod = null;

    // Get product ID input
    id = -1;
    while(id < 0) {
        System.out.println("Enter product id: ");

        id = this.handleIdInput(id);
        if(id == -2) {
            continue;
        }
    }

    // Get product name input
    System.out.println("Enter product name: ");
    this.scan.nextLine();
    name = this.scan.nextLine();
    name = name.trim();

    // Get available quantity input
    availableQty = -1;
    while(availableQty < 0) {
        System.out.println("Enter available quantity: ");

        availableQty = this.handleQtyInput(availableQty);
        if(availableQty == -2) {
            continue;
        }
    }

    // Get price input
    price = -1;
    while(price < 0) {
        System.out.println("Enter price: ");

        price = this.handlePriceInput(price);
        if(price == -2) {
            continue;
        }
    }

    // Get discounted price input
    discountedPrice = -1;
    while(discountedPrice < 0) {
        System.out.println("Enter discounted price: ");
        discountedPrice = this.handlePriceInput(discountedPrice);
        if(discountedPrice == -2) {
            continue;
        }
    }

    // Get return period input and validate the date format
    boolean isReturnPeriodValid = false;
    String date = null;
    while(!isReturnPeriodValid) {
        System.out.println("Enter return period (dd-mm-yyyy): ");
        date = this.scan.next();
        date = date.trim();

        isReturnPeriodValid = this.validateReturnPeriodDate(date);

        if(!isReturnPeriodValid) {
            System.err.println("\nPlease enter valid date in return period in dd-mm-yyyy format\n");
            date = null;
        }
    }

    // Parse the date string into a Date object
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");

    try {
        returnPeriod = dateFormat.parse(date);
    } catch(ParseException e) {
        System.err.println("Failed to convert date string to date type");
        System.err.println(e);
    }

    // Create and return a new Product object with the given input
    return new Product(id, name, availableQty, price, discountedPrice, returnPeriod);
}

// Handle forgotten password scenario by resetting the user's password
private void handleForgotPassword() {
    String fullName = this.getFullNameInput();
    if (this.resetPassword(fullName)) {
        System.out.println("\nSuccessfully changed the password\n");
    } else {
        System.out.println("\nFull name doesn't exists\n");
    }
    System.out.println("Press enter to continue");
    this.con.readLine();
}

// Reset the password for the user with the given full name
private boolean resetPassword(String fullName) {
    for (User user : this.users) {
        if (user.getFullName().equals(fullName)) {
            user.setPassword(this.getPasswordInput("Enter new password: "));
            return true;
        }
    }
    return false;
}

// Handle user registration by creating a new User object and adding it to the system
private void handleRegistration() {
    User newUser = this.getRegistrationInput();
    String userRoleTxt = this.isAdmin(newUser) ? "an Admin" : this.isMember(newUser) ? "a Member Customer" : "a Non-Member Customer";

    if(this.users.add(newUser) && this.saveUsers()) {
        this.fetchUsers();
        System.out.println("\nRegistered successfully as " + userRoleTxt + "\n");
    } else {
        System.out.println("\nFailed to register\n");
    }
    System.out.println("Press enter to continue");
    this.con.readLine();
}


    //
// Get registration input from the user and create a new User object
private User getRegistrationInput() {
    // Get user information (full name, username, and password)
    String fullName = this.getFullNameInput();
    String username = this.getUsernameInput();
    String password = this.getPasswordInput("Enter password: ");
    boolean isAdmin = false;
    int choice = -1;

    // Get user role (Admin or Customer)
    while(choice != 1 && choice != 2) {
        System.out.println("\nRegister as:");
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.println("Choose an option: ");

        // Handle user input for role selection
        try {
            if(choice == -2) {
                this.scan.nextLine();
            }

            choice = this.scan.nextInt();
        } catch(InputMismatchException e) {
            System.err.println("\nPlease enter number input\n");
            System.out.println("Press enter to continue");

            choice = -2;
            this.con.readLine();
            continue;
        }

        if(choice == 1 || choice == 2) {
            isAdmin = choice == 1;
        } else {
            System.out.println("\nINVALID INPUT\n");
        }
    }

    User newUser = null;

    // Create a new User object based on the selected role
    if(!isAdmin) {
        boolean isMember = false;
        choice = -1;

        // Get customer membership status (Member or Non-Member)
        while(choice != 1 && choice != 2) {
            System.out.println("\nRegister customer as member:");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("Choose an option: ");

            // Handle user input for membership selection
            try {
                if(choice == -2) {
                    this.scan.nextLine();
                }

                choice = this.scan.nextInt();
            } catch(InputMismatchException e) {
                System.err.println("\nPlease enter number input\n");
                System.out.println("Press enter to continue");

                choice = -2;
                this.con.readLine();
                continue;
            }

            if(choice == 1 || choice == 2) {
                isMember = choice == 1;
            } else {
                System.out.println("\nINVALID INPUT\n");
            }
        }

        // Create a new Customer object based on the membership status
        newUser = isMember ? new Member() : new NonMember();

    } else {
        newUser = new Admin();
    }

    // Set the user's information and provide access to the retail store products
    newUser.setFullName(fullName);
    newUser.setUsername(username);
    newUser.setPassword(password.hashCode() + "");
    newUser.provideAccessToRetailStoreProducts(products);

    return newUser;
}

// Check if a user is an Admin
private boolean isAdmin(User user) {
    return user instanceof Admin;
}

// Check if a user is a Member
private boolean isMember(User user) {
    return user instanceof Member;
}

// Save user information to a file
private boolean saveUsers() {
    try {
        PrintWriter printWriter = new PrintWriter(new FileWriter("users.txt"));
        for(User user: this.users) {
            String userRole = this.isAdmin(user) ? "admin" : this.isMember(user) ? "member" : "non-member";
            printWriter.printf("%s,%s,%s,%s\n", user.getFullName(), user.getUsername(), user.getPassword(), userRole);
        }
        printWriter.close();
        return true;
    } catch(IOException e) {
        System.err.println(e.getMessage());
        return false;
    }
}

// Fetch user information from a file and create User objects
private void fetchUsers() {
    try {
        this.users.clear();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("users.txt"));
        String line = null;
        String[] strArr = null;
        while(true) {
            line = bufferedReader.readLine();
            if(line == null) {
                break;
            }
            strArr = line.split(",");

            User user = null;

            // Create User objects based on the user role
            if(strArr[3].equals("admin")) {
                user = new Admin(strArr[0], strArr[1], strArr[2]);
            } else if(strArr[3].equals("member")) {
                user = new Member(strArr[0], strArr[1], strArr[2]);
            } else if(strArr[3].equals("non-member")) {
                user = new NonMember(strArr[0], strArr[1], strArr[2]);
            }

            // Provide access to retail store products and add the user to the list
            user.provideAccessToRetailStoreProducts(this.products);
            this.users.add(user);
        }

        bufferedReader.close();
    } catch(FileNotFoundException e) {
    
    } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }   


    //


// Save product information to a file
private boolean saveProducts() {
    try {
        PrintWriter printWriter = new PrintWriter(new FileWriter("products.txt"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyy");
        // Write product information to the file
        for(Product product: this.products) {
            String returnPeriodDateStr = dateFormat.format(product.getReturnPeriod());
            printWriter.printf("%d,%s,%s,%d,%f,%f,%s\n", product.getId(), product.getName(), product.getAvailabilityStatus(), product.getQty(), product.getPrice(), product.getDiscountedPrice(), returnPeriodDateStr);
        }
        printWriter.close();
        return true;
    } catch(IOException e) {
        System.err.println(e.getMessage());
        return false;
    }
}

// Fetch product information from a file and create Product objects
private void fetchProducts() {
    try {
        this.products.clear();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("products.txt"));
        String line = null;
        String[] strArr = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");

        // Read product information from the file
        while(true) {
            line = bufferedReader.readLine();
            if(line == null) {
                break;
            }
            strArr = line.split(",");

            // Parse the return period date string into a Date object
            Date returnPeriod = null;
            try {
                returnPeriod = dateFormat.parse(strArr[6]);
            } catch(ParseException e) {
                System.err.println("Failed to convert date string to date type");
                System.err.println(e);
            }

            // Create a new Product object and add it to the list
            this.products.add(new Product(Integer.parseInt(strArr[0]), strArr[1], Integer.parseInt(strArr[3]), Double.parseDouble(strArr[4]), Double.parseDouble(strArr[5]), returnPeriod));
        }

        bufferedReader.close();
    } catch(FileNotFoundException e) {

    } catch(IOException e) {
        System.err.println(e.getMessage());
    }
}


    //
// Save purchased products for a customer to a file
private boolean savePurchasedProducts(Customer customer) {
    try {
        String filePath = getPurchasedProductFilePath(customer.getFullName());

        PrintWriter printWriter = new PrintWriter(new FileWriter(filePath));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyy");
        // Write purchased product information to the file
        for(PurchaseProduct product: customer.getPurchaseProducts()) {
            String returnPeriodDateStr = dateFormat.format(product.getReturnPeriod());
            String issueDateStr = dateFormat.format(product.getIssueDate());
            String returnDateStr = product.getReturnDate() == null ? "Not return" : dateFormat.format(product.getReturnDate());

            printWriter.printf("%s,%d,%s,%s,%d,%f,%f,%s,%s,%s\n", customer.getFullName(), product.getId(), product.getName(), product.getAvailabilityStatus(), product.getQty(), product.getPrice(), product.getDiscountedPrice(), returnPeriodDateStr, issueDateStr, returnDateStr);
        }
        printWriter.close();
        return true;
    } catch(IOException e) {
        System.err.println(e.getMessage());
        return false;
    }
}

// Get the file path for the purchased products file of a customer
private String getPurchasedProductFilePath(String name) {
    String purchasedProductsDir = "./purchasedProducts/";
    String filePath = purchasedProductsDir + name + "PurchasedProducts.txt";
    return filePath;
}

// Fetch purchased products for a customer from a file and create PurchaseProduct objects
private void fetchPurchasedProducts(Customer customer) {
    try {
        ArrayList<PurchaseProduct> purchasedProducts = customer.getPurchaseProducts();

        purchasedProducts.clear();

        String filePath = getPurchasedProductFilePath(customer.getFullName());

        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line = null;
        String[] strArr = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");

        // Read purchased product information from the file
        while(true) {
            line = bufferedReader.readLine();
            if(line == null) {
                break;
            }
            strArr = line.split(",");

            // Check if the purchased product belongs to the customer
            if(!strArr[0].equals(customer.getFullName())) {
                continue;
            }

            // Parse the date strings into Date objects
            Date returnPeriod = null;
            Date issueDate = null;
            Date returnDate = null;

            try {
                returnPeriod = dateFormat.parse(strArr[7]);
                issueDate = dateFormat.parse(strArr[8]);
                returnDate = strArr[9].equals("Not return") ? null : dateFormat.parse(strArr[9]);
            } catch(ParseException e) {
                System.err.println("Failed to convert date string to date type");
                System.err.println(e);
            }

            // Create a new PurchaseProduct object and add it to the list
            PurchaseProduct purchasedProduct = new PurchaseProduct(Integer.parseInt(strArr[1]), strArr[2], Integer.parseInt(strArr[4]), Double.parseDouble(strArr[5]), Double.parseDouble(strArr[6]), returnPeriod, issueDate);
            purchasedProduct.setReturnDate(returnDate);
            purchasedProducts.add(purchasedProduct);
        }

        bufferedReader.close();
    } catch(FileNotFoundException e) {

    } catch(IOException e) {
        System.err.println(e.getMessage());
    }
}

// Display the available product catalog
private void displayAvailableProductCatalogue() {
    String whiteSpace = "";

    System.out.println("\n==============================================================================> Product Catalogue <==============================================================================\n");
    // Print the header row for the product catalog
    System.out.printf("Product Id%-5s\tName%-20sAvailability Status%-10s\tQty%-5s\tPrice%-10s\tDiscounted Price%-5s\tReturn Period", whiteSpace, whiteSpace, whiteSpace, whiteSpace, whiteSpace, whiteSpace);
    System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

    // Check if there are any available products
    if(this.products.size() < 1) {
        System.out.println("Product not available");
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");

    // Print the product catalog
    for(Product product: this.products) {
        if(product.getQty() < 1) {
            continue;
        }
        String returnPeriodDateStr = dateFormat.format(product.getReturnPeriod());
        System.out.printf("%-5s\t\t%-20s\t%-10s\t\t\t%-5s\t\t%.2f\t\t%.2f\t\t\t%-10s\n", product.getId(), product.getName(), product.getAvailabilityStatus(), product.getQty(), product.getPrice(), product.getDiscountedPrice(), returnPeriodDateStr);
    }

    System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
}
 // Get the user's username input
private String getUsernameInput() {
    System.out.println("Enter username: ");
    String username = this.scan.next();
    return username.trim();
}

// Get the user's full name input
private String getFullNameInput() {
    System.out.println("Enter full name: ");
    this.scan.nextLine();
    String fullName = this.scan.nextLine();
    return fullName.trim();
}

// Get the user's password input with a custom message
private String getPasswordInput(String message) {
    if (this.con == null) {
        System.out.println("\nConsole is not available\n");
        return null;
    }
    String password = "";
    boolean isPasswordInputValid = true;
    // Validate password input
    while(isPasswordInputValid) {
        System.out.print(message);
        char[] pass;
        pass = this.con.readPassword();
        for (int i = 0; i < pass.length; i++) {
            System.out.print("*");
        }
        System.out.println();
        password = new String(pass);
        if(this.validatePassword(password)) {
            isPasswordInputValid = false;
        } else {
            System.out.println("Password should be 6 to 18 characters long and it should consist a lowercase letter, a digit, a special character and no white space");
        }
    }
    return password;
}

// Validate password using a regular expression
private boolean validatePassword(String password) {
    String passwordRegExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{6,18}$";
    Pattern pattern = Pattern.compile(passwordRegExp);
    Matcher matcher = pattern.matcher(password);
    return matcher.matches();
}

// Validate return period date using a regular expression
private boolean validateReturnPeriodDate(String returnPeriod) {
    String returnPeriodRegExp = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
    Pattern pattern = Pattern.compile(returnPeriodRegExp);
    Matcher matcher = pattern.matcher(returnPeriod);
    return matcher.matches();
}

// Run method to display the available product catalog
@Override
public void run() {
    this.displayAvailableProductCatalogue();
}
}