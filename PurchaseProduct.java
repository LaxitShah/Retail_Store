// Importing the Date class from the java.util package
import java.util.Date;

// A public class named PurchaseProduct which extends the Product class
public class PurchaseProduct extends Product {
// Private member variables of type Date for issue date and return date
private Date issueDate;
private Date returnDate;
// create a constructor with parameters
public PurchaseProduct(int id, String name, int qty, double price, double discountedPrice, Date returnPeriod, Date issueDate) {
    // call the constructor of the superclass using the "super" keyword
    super(id, name, qty, price, discountedPrice, returnPeriod);
    // set the instance variable issueDate to the passed argument
    this.issueDate = issueDate;
    // set the instance variable returnDate to null
    this.returnDate = null;
}

// create a default constructor
public PurchaseProduct() {
    super();
}

// create getter methods for the instance variables
public Date getIssueDate() {
    return this.issueDate;
}

public Date getReturnDate() {
    return this.returnDate;
}

// calculate the total purchase amount
public double getTotalPurchaseAmount() {
    return (this.getQty() * this.getDiscountedPrice());
}

// create setter methods for the instance variables
public void setIssueDate(Date issueDate) {
    this.issueDate = issueDate;
}

public void setReturnDate(Date returnDate) {
    this.returnDate = returnDate;
}
}