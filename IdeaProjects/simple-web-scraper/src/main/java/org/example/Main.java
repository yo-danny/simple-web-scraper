package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scraper scraperTest = new Scraper("https://www.scrapingcourse.com/ecommerce/");

        scraperTest.initScraper();
    }
}