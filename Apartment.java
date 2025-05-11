/**
 * Apartment.java
 * 
 * Description: An apartment class for holding the basic data such as the renter's name, 
 * bed/bath count, price per month, and link to the listing
 * 
 * @author Andrew Peirce
 * 
 * Date Last Modified: 05/11/2025
 * 
 */
public class Apartment {
    private String company;
    private String bedBath;
    private String price;
    private String link;

    /**
     * Constructor
     * @param company
     * @param bedBath
     * @param price
     * @param link
     */
    public Apartment(String company, String bedBath, String price, String link) {
        this.company = company;
        this.bedBath = bedBath;
        this.price = price;
        this.link = link;
    }

    // Getters
    public String getCompany() { return company; }
    public String getBedBath() { return bedBath; }
    public String getPrice() { return price; }
    public String getLink() { return link; }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s", company, bedBath, price, link);
    }
}