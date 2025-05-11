# StudentRentals Scraper

A Java-based web scraper that reveals hidden apartment listings from [studentrentalslacrosse.com](https://www.studentrentalslacrosse.com). Uses raw ID iteration to uncover listings that don't show up on the site's search filters.

## Features
- Extracts address, bed/bath info, price, and link from individual listings
- Bypasses filter UI by directly querying listing IDs
- Skips 404s and missing pages
- Outputs clean CSV for personal use

## Output
Generates 'hidden_listings.csv' in your project folder with this format:

Company,Bed/Bath,Price,Link
"Prairie Properties","1br/1ba","$500","https://..."

## Technologies
- Java
- Jsoup (HTML parsing)
- Regex
- FileWriter (CSV export)

## Notes
This project was developed with the help of AI and community tools.
- Core coding assistance and debugging by [DeepSeek](https://chat.deepseek.com/)
- Project structure, documentation, and README support guided with GPT

## Contact
Created by Andrew Peirce - student at UW-La Crosse
