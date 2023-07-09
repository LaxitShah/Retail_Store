import java.util.ArrayList;

// Abstract User class
public abstract class User {
    private String fullName;
    private String username;
    private String password;

    // List of products available to the user
    protected ArrayList<Product> products;

    // Constructor with parameters
    public User(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    // Default constructor
    public User() {}

    // Getter methods
    public String getFullName() {
        return this.fullName;
    }

    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }

    // Setter methods
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    // Provide access to retail store products
    public void provideAccessToRetailStoreProducts(ArrayList<Product> products) {
        this.products = products;
    }

    // Display user profile information
    public void displayProfile() {
        System.out.println("Full name: " + this.fullName);
        System.out.println("username: " + this.username);
    }
}
