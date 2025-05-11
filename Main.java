/**
 * Main.java
 * 
 * Description: A driver to run the Apartment Scraper.
 * 
 * @author Andrew Peirce
 * 
 * Date Last Modified: 05/11/2025
 */
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApartmentScraper scraper = new StudentRentalsScraper(); // Instantiate scraper for StudentRentalsLaCrosse
        List<Apartment> apartments = scraper.scrape();			// Fetch and parse apartment listings

        System.out.println("\nFinal Results:");
        for (Apartment apt : apartments) {
            System.out.println(apt);
        }
        System.out.println("\nDone. Listings saved to hidden_listings.csv");
    }
}