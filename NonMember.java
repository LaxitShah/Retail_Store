public class NonMember extends Customer {

    /**
     * Constructs a new NonMember object with the given full name, username, and password.
     * @param fullName the full name of the customer
     * @param username the username of the customer
     * @param password the password of the customer
     */
    public NonMember(String fullName, String username, String password) {
        super(fullName, username, password);
    }
    
    /**
     * Constructs a new default NonMember object.
     */
    public NonMember() {
        super();
    }

    /**
     * Returns the additional discount that non-member customers receive, which is 0%.
     * @return the additional discount for non-member customers (0)
     */
    @Override
    public double getAdditionalDiscount() {
        return 0;
    }

    /**
     * Returns whether the customer is a member or not.
     * @return false, as non-member customers are not members
     */
    @Override
    protected boolean getIsMember() {
        return false;
    }
}
