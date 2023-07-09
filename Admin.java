public class Admin extends User implements IAdmin {
    // Admin constructor that takes in full name, username, and password
    public Admin(String fullName, String username, String password) {
        // Call the superclass constructor with the given parameters
        super(fullName, username, password);
    }

    // Empty constructor for Admin
    public Admin() {
        // Call the superclass empty constructor
        super();
    }

    // Override method from the IAdmin interface to add a new product
    @Override
    public boolean addProduct(Product newProduct) {
        // Add the new product to the list of products and return true if successful
        return this.products.add(newProduct);
    }

    // Override method from the IAdmin interface to calculate fine for a product
    @Override
    public int calculateFine(PurchaseProduct product) {
        // Get the time for the allowed return period for the product
        long returnPeriodTime = product.getReturnPeriod().getTime();
        // Set the return time to 0
        long returnTime = 0;
        // Try to get the return date for the product, and catch a NullPointerException if it is null
        try {
            returnTime = product.getReturnDate().getTime();
        } catch(NullPointerException e) {
            System.err.println("Return date is null");
            System.err.println(e);
        }

        // If the return time is before or equal to the return period time, return 0 fine
        if(returnTime <= returnPeriodTime) {
            return 0;
        }
        // Otherwise, return a fine of 500
        return 500;
    }

    // Override method from the IAdmin interface to get the total number of available products
    @Override
    public int getTotalAvailableProducts() {
        // Initialize a counter for available products
        int productCounter = 0;

        // Loop through all products and increment the counter if the product quantity is greater than 0
        for(Product product: this.products) {
            if(product.getQty() > 0) {
                productCounter++;
            }
        }

        // Return the total count of available products
        return productCounter;
    }

    // Override method from the IAdmin interface to get the maximum profit that can be made from selling all products
    @Override
    public double getMaximumProfit() {
        // Initialize a sum variable for the total profit
        double sum = 0;

        // Loop through all products and add the product's profit (quantity times discounted price) to the sum
        for(Product product: this.products) {
            sum += (product.getQty() * product.getDiscountedPrice());
        }

        // Return the total maximum profit
        return sum;
    }
}
