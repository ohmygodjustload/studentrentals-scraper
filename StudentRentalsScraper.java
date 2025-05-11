/**
 * StudentRentalsScraper.java
 * 
 * Description: A web scraper project I created to find cheap listings that weren't directly
 * available on the studentrenatlslacrosse.com web site.
 * 
 * @author Andrew Peirce
 * 
 * Date Last Modified: 05/11/2025
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class StudentRentalsScraper implements ApartmentScraper {
	
	// URL pattern for scraping listings (formatted with query + listing ID)
    private static final String BASE_URL = "https://www.studentrentalslacrosse.com/all-rental-listings-la-crosse/%s/%d/";
    
    // Regex pattern to extract property title (company and bed/bath info)
    private static final Pattern TITLE_PATTERN = Pattern.compile("^(.*?)\\s*[\\–\\-]\\s*(\\d+br\\/\\d+(\\.\\d+)?ba).*");
    
    // Regex pattern to extract prices (e.g. $650, $1,200)
    private static final Pattern PRICE_PATTERN = Pattern.compile("\\$(\\d{4,}|\\d{1,3}(?:,\\d{3})*)");
    
    // Max number of listing IDs to check
    private static final int MAX_ID = 900;
    
    // Delay between requests to avoid hammering the server
    private static final int DELAY_MS = 800 + (int)(Math.random() * 400); // Random delay between 800-1200 ms
    
    // Method to scrape the given URL
    @Override
    public List<Apartment> scrape() {
        List<Apartment> results = new ArrayList<>();
        
        // Loop through possible listing IDs
        for (int id = 1; id <= MAX_ID; id++) {
            try {
                String url = String.format(BASE_URL, "any-words", id);
                System.out.printf("Checking ID %d... ", id);
                
                // Fetch HTML document using Jsoup
                Document doc = Jsoup.connect(url)
                					.userAgent("Mozilla/5.0")
                					.timeout(10000)
                					.ignoreHttpErrors(true)
                					.get();

                // Skip 404 (not found) responses
                if (doc.connection().response().statusCode() == 404) {
                    System.out.println("[404]");
                    continue;
                }

                // Clean and parse the page title for listing info
                String rawTitle = cleanTitle(doc.title());
                String price = extractPrice(doc.body().text());
                String[] titleParts = parseTitle(rawTitle);
                
                // If both title and price are valid, add to result list
                if (titleParts != null && !price.isEmpty()) {
                    results.add(new Apartment(
                    			titleParts[0], 	// Company
                    			titleParts[1],	// Bed/Bath
                    			price,			// Price
                    			url				// Link to Listing
                    			));
                    System.out.printf("[VALID] %s - %s - %s%n", titleParts[0], titleParts[1], price);
                } else {
                    System.out.println(price.isEmpty() ? "[NO PRICE]" : "[INVALID TITLE]");
                }

                Thread.sleep(DELAY_MS); // Pause before next request
                
            } catch (Exception e) {
                System.out.printf("[ERROR] ID %d: %s%n", id, e.getMessage());
            }
        }

        saveToCSV(results);
        return results;
    }
    
    /**
     * Fixes bad characters and extra text from listing titles
     * @param input The raw title with all characters
     * @return The cleaned title
     */
    private String cleanTitle(String input) {
        return input.replace("â€“", "-")
                   .replaceAll("\\|.*", "")
                   .trim();
    }
    
    /**
     * Extracts the company name and bed/bath info from the listing title
     * @param rawTitle The raw title which is the renter name and bed/bath info in one string
     * @return The company name and bed/bath info as two strings in an array
     */
    private String[] parseTitle(String rawTitle) {
        Matcher matcher = TITLE_PATTERN.matcher(rawTitle);
        if (matcher.find()) {
            return new String[] {
            	matcher.group(1).trim(), // Company or Renter name
                matcher.group(2).trim()  // Bed/Bath format
            };
        }
        return null;
    }

    /**
     * Searches the page text for a price using regex
     * @param text The page text
     * @return The price data
     */
    private String extractPrice(String text) {
        Matcher matcher = PRICE_PATTERN.matcher(text);
        return matcher.find() ? "$" + matcher.group(1) : "";
    }

    /**
     * Saves final scraped listings to a CSV file
     * @param listings The list of valid apartment listings
     */
    private void saveToCSV(List<Apartment> listings) {
        try (FileWriter writer = new FileWriter("hidden_listings.csv")) {
            writer.append("Company,Bed/Bath,Price,Link\n");
            for (Apartment apt : listings) {
                writer.append(String.format("\"%s\",\"%s\",\"%s\",\"%s\"%n", 
                	escape(apt.getCompany()),
                	escape(apt.getBedBath()),
                	escape(apt.getPrice()),
                	escape(apt.getLink())));
            }
        } catch (IOException e) {
            System.out.println("Error saving CSV: " + e.getMessage());
        }
    }

    /**
     * Escapes quotes for CSV compatibility
     * @param input
     * @return input without quotes
     */
    private String escape(String input) {
        return input.replace("\"", "\"\"");
    }

    @Override
    public String getSourceName() {
        return "StudentRentalsLaCrosse";
    }
}