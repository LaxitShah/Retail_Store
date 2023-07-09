public class Member extends Customer {

    // Constructor to create a new member with given full name, username, and password
    public Member(String fullName, String username, String password) {
        super(fullName, username, password);
    }
    
    // Default constructor to create a new member with no initial values
    public Member() {
        super();
    }

    // Method to return the additional discount percentage for members
    @Override
    public double getAdditionalDiscount() {
        return 0.1; // 10% additional discount for members
    }

    // Method to return whether the customer is a member (always true for members)
    @Override
    protected boolean getIsMember() {
        return true;
    }
}
