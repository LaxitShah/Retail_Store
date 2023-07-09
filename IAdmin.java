interface IAdmin {
    // Method to add a new product to the inventory
    boolean addProduct(Product newProduct);
    
    // Method to calculate the fine for a returned product based on the return date
    int calculateFine(PurchaseProduct product);
    
    // Method to get the total number of available products in the inventory
    int getTotalAvailableProducts();
    
    // Method to get the maximum possible profit from selling all products
    double getMaximumProfit();    
}
