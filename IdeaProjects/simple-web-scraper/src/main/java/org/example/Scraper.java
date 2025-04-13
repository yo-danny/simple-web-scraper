package org.example;
import org.example.data.Product;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.IOException;
import java.util.ArrayList;

public class Scraper {

    private String url;

    public Scraper (String url) {
        this.url = url;
    }

    public void initScraper () {
        Document doc;
        ArrayList<Product> products = new ArrayList<>();
        int id = 0;

        while (url != null) {

            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                        .header("Accept-Language", "*")
                        .get();
                //userAgent and header are used to not being blocked automatically by sites
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // retrieving the list of product HTML elements
            Elements productElements = doc.select("li.product");

            // iterating over the list of HTML products
            for (Element productElement : productElements) {
                Product product = new Product();
                id++;

                // extracting the data of interest from the product HTML element
                // and storing it in product
                product.setId(id);
                product.setUrl(productElement.selectFirst("a").attr("href"));
                product.setImage(productElement.selectFirst("img").attr("src"));
                product.setName(productElement.selectFirst("h2").text());
                product.setPrice(productElement.selectFirst("span").text());

                products.add(product);
            }

            Element nextButton = doc.selectFirst("a.next");
            if (nextButton != null) {
                String nextPageUrl = nextButton.attr("href");
                if (!nextPageUrl.startsWith("http")) {
                    nextPageUrl = url + nextPageUrl.replaceFirst("^/", "");
                }
                url = nextPageUrl;
            } else {
                url = null;
            }
        }

            products.toString();
            System.out.println(products);
    }
}
