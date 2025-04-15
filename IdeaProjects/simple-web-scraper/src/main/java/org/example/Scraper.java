package org.example;
import org.example.data.Product;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Scraper {

    private String startUrl;
    private static final int THREAD_POOL_SIZE = 5;
    private int id = 0;

    public Scraper(String url) {
        this.startUrl = url;
    }

    public List<Product> ScrapeConcurrently() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Product> products = new CopyOnWriteArrayList<>();
        List<Future<String>> futures = new ArrayList<>();
        ConcurrentSkipListSet<String> visitedPages = new ConcurrentSkipListSet<>();

        visitedPages.add(startUrl);
        futures.add(executor.submit(() -> initScraper(startUrl, products, visitedPages)));

        while (!futures.isEmpty()) {
            List<Future<String>> newFutures = new ArrayList<>();
            for (Future<String> future : futures) {
                try {
                    String nextUrl = future.get();
                    if (nextUrl != null && visitedPages.add(nextUrl)) {
                        newFutures.add(executor.submit(() -> initScraper(nextUrl, products, visitedPages)));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("error executing task: " + e.getMessage());
                }
            }
            futures = newFutures;
        }
        executor.shutdown();
        try {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Executor shutdown interrupted:" + e.getMessage());
        }
        return products;
    }

    public String initScraper(String url, List<Product> products, ConcurrentSkipListSet<String> visitedPages) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                    .header("Accept-Language", "*")
                    .get();
            //userAgent and header are used to not being blocked automatically by sites

            // retrieving the list of product HTML elements
            Elements productElements = doc.select("li.product");

            // iterating over the list of HTML products
            for (Element productElement : productElements) {
                Product product = new Product();
                id++;

                Element linkElement = productElement.selectFirst(".woocommerce-LoopProduct-link");
                Element imgElement = productElement.selectFirst(".product-image");
                Element nameElement = productElement.selectFirst(".product-name");
                Element priceElement = productElement.selectFirst(".price");

                // extracting the data of interest from the product HTML element
                // and storing it in product
                product.setId(id);
                product.setUrl(linkElement != null ? linkElement.absUrl("href") : "N/A");
                product.setImage(imgElement != null ? imgElement.absUrl("src") : "N/A");
                product.setName(nameElement != null ? nameElement.text() : "N/A");
                product.setPrice(priceElement != null ? priceElement.text() : "N/A");

                products.add(product);
            }

            Element nextButton = doc.selectFirst("a.next");
            return (nextButton != null) ? nextButton.absUrl("href") : null;

        } catch (IOException e) {
            System.err.println("error fetching page: " + e.getMessage());
        }
        return null;
    }
}
