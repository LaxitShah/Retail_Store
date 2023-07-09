import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class Customer extends User {
    protected ArrayList<PurchaseProduct> purchasedProducts;

    // Constructor for initializing the purchased products list
    public Customer(String fullName, String username, String password) {
        super(fullName, username, password);
        this.purchasedProducts = new ArrayList<PurchaseProduct>();
    }
    
    // Default constructor for initializing the purchased products list
    public Customer() {
        super();
        this.purchasedProducts = new ArrayList<PurchaseProduct>();
    }

    // Getter method for the purchased products list
    public ArrayList<PurchaseProduct> getPurchaseProducts() {
        return this.purchasedProducts;
    }

    // Setter method for the purchased products list
    public void setPurchaseProducts(ArrayList<PurchaseProduct> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    // Abstract method to get additional discount for the customer
    public abstract double getAdditionalDiscount();

    // Abstract method to check if the customer is a member
    protected abstract boolean getIsMember();

    // Private method to get a product by its id
    private Product getProductById(int productId) {
        for(Product product: this.products) {
            if(product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    // Private method to get a purchased product by its id
    private PurchaseProduct getPurchasedProductById(int purchasedProductId) {
        for(PurchaseProduct product: this.purchasedProducts) {
            if(product.getId() == purchasedProductId) {
                return product;
            }
        }
        return null;
    }

    // Private method to calculate the discount on a product
    private double calculateDiscount(double price) {
        return (price * this.getAdditionalDiscount());
    }

    // Method to purchase a product
    public boolean purchaseProduct(int productId, int purchaseQty) {
        Product product = this.getProductById(productId);

        if(product == null) {
            System.out.println("\nProduct not found");
            return false;
        }

        if(purchaseQty > product.getQty()) {
            System.out.println("\nPurchase quantity is more than the available quantity");
            return false;
        }

        product.setQty(product.getQty() - purchaseQty);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Date issueDate = null;

        try {
            // issueDate = new Date();
            issueDate = dateFormat.parse("01-04-2023");
        } catch(ParseException e) {
            System.err.println("Failed to convert date string to date type");
            System.err.println(e);
            return false;
        }

        double discountedPrice = product.getDiscountedPrice() - this.calculateDiscount(product.getDiscountedPrice());

        return this.purchasedProducts.add(new PurchaseProduct(product.getId(), product.getName(), purchaseQty, product.getPrice(), discountedPrice, product.getReturnPeriod(), issueDate));
    }

    // Method to cancel a purchased product
    public int cancelPurchasedProduct(int productId) {
        PurchaseProduct product = this.getPurchasedProductById(productId);

        if(product == null) {
            return -1;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        // Date returnDate = new Date();
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse("19-04-2023");
        } catch(ParseException e) {
            System.err.println(e);
            return -2;
        }

        product.setReturnDate(returnDate);

        int fine = (new Admin()).calculateFine(product);
        
        return fine;
    }

    // Method to display the customer's profile

    @Override
    public void displayProfile() {
        // Display customer profile
        System.out.println("\n==============================================================================> Customer Profile <==============================================================================\n");
        
        // Call superclass method to display customer details
        super.displayProfile();

        // Display customer membership status
        String memberStr = "Member: " + (this.getIsMember() ? "Yes" : "No" ); 
        System.out.println(memberStr);
        
        // Call the private method to display the list of purchased products
        this.displayPurchaseProduct();
    }

    private void displayPurchaseProduct() {
        // Create a whitespace variable to format the table
        String whiteSpace = "";

        // Display purchased products table header
        System.out.println("\n==============================================================================> Purchased Products <==============================================================================\n");
        System.out.printf("Product Id%-5s\tName%-20sQty%-5sPrice%-11sDiscounted Price%-8sTotal Amount\tReturn Period\tIssue Date\tReturn Date", whiteSpace, whiteSpace, whiteSpace, whiteSpace, whiteSpace, whiteSpace, whiteSpace, whiteSpace);
        System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        
        // Check if the customer has any purchased products
        if(this.purchasedProducts.size() < 1) {
            System.out.println("Product not purchased");
        }
        
        // Create a date format object to format date strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        
        // Display the details of each purchased product in a table row
        for(PurchaseProduct product: this.purchasedProducts) {
            String returnPeriodDateStr = dateFormat.format(product.getReturnPeriod());
            String issueDateStr = dateFormat.format(product.getIssueDate());
            String returnDateStr = product.getReturnDate() == null ? "Not return" : dateFormat.format(product.getReturnDate());
            System.out.printf("%-15s\t%-20s\t%-5s\t%.2f\t\t%.2f\t%-10s\t%.2f\t\t%s\t%s\t%s\n", product.getId(), product.getName(), product.getQty(), product.getPrice(), product.getDiscountedPrice(), whiteSpace, product.getTotalPurchaseAmount(), returnPeriodDateStr, issueDateStr, returnDateStr);
        }
        
        System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}
