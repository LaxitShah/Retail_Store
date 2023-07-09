public class Main {
    public static void main(String[] args) {
        // Create an instance of the RetailStore singleton class
        RetailStore retailStore = RetailStore.makeInstance();

        // Call the begin method of the RetailStore instance to start the application
        retailStore.begin();
    }
}
