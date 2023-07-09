import java.util.Date;

// The Product class represents a product with its properties and methods
public class Product {
    // Instance variables
    private int id;
    private String name;
    private String availabilityStatus;
    private int qty;
    private double price;
    private double discountedPrice;
    private Date returnPeriod;

    // Constructor that takes all properties as arguments
    public Product(int id, String name, int qty, double price, double discountedPrice, Date returnPeriod) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.returnPeriod = returnPeriod;
        this.setAvailabilityStatus();
    }

    // Default constructor
    public Product() {}

    // Getter methods for each property
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAvailabilityStatus() {
        return this.availabilityStatus;
    }

    public int getQty() {
        return this.qty;
    }

    public double getPrice() {
        return this.price;
    }

    public double getDiscountedPrice() {
        return this.discountedPrice;
    }

    public Date getReturnPeriod() {
        return this.returnPeriod;
    }

    // Setter methods for each property
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Sets the availability status based on the quantity
    public void setAvailabilityStatus() {
        this.availabilityStatus = this.getQty() > 0 ? "In-store" : "Out-of-Stock";
    }

    public void setQty(int qty) {
        this.qty = qty;
        this.setAvailabilityStatus();
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public void setReturnPeriod(Date returnPeriod) {
        this.returnPeriod = returnPeriod;
    }
}
