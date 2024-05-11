package com.product.aggregate;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AggregatedProduct {
    private String productTitle;
    private String commonStorage;
    private String commonRam;
    private double minPrice;
    private String minPriceStore;
    private String minPriceUrl;
    private int maxStoreBoosterScore;

    public AggregatedProduct(String productTitle) {
        this.productTitle = productTitle;
        this.minPrice = Double.MAX_VALUE;
        this.maxStoreBoosterScore = Integer.MIN_VALUE;
    }

    // Getters and setters (omitted for brevity)

    // Method to aggregate information from scraped metadata
    public void aggregate(ScrapedMetadata scrapedMetadata) {
        String title = scrapedMetadata.getProductTitle();
        ProductDescription productDescription = scrapedMetadata.getProductDescription();
        if (title != null && productDescription != null) {
            String storage = productDescription.getStorage();
            String ram = productDescription.getRam();
            String price = productDescription.getPrice();
            if (storage != null && ram != null && price != null) {
                if (commonStorage == null) {
                    commonStorage = storage;
                    commonRam = ram;
                } else {
                    if (!commonStorage.equals(storage)) {
                        commonStorage = "Mixed";
                    }
                    if (!commonRam.equals(ram)) {
                        commonRam = "Mixed";
                    }
                }

                double priceValue = 0;
                try {
                    priceValue = Double.parseDouble(price.substring(4));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (priceValue < minPrice) {
                    minPrice = priceValue;
                    minPriceStore = scrapedMetadata.getStore();
                    minPriceUrl = scrapedMetadata.getUrl();
                }

                if (scrapedMetadata.getStoreBoosterScore() > maxStoreBoosterScore) {
                    maxStoreBoosterScore = scrapedMetadata.getStoreBoosterScore();
                }
            } else {
                System.out.println("Incomplete product description found in ScrapedMetadata: " + scrapedMetadata);
            }
        } else {
            System.out.println("Null or incomplete product description found in ScrapedMetadata: " + scrapedMetadata);
        }
    }

    // Method to display aggregated product information
    public void displayAggregatedProductInfo() {
        System.out.println("Product Title: " + productTitle);
        System.out.println("Common Storage: " + commonStorage);
        System.out.println("Common RAM: " + commonRam);
        System.out.println("Minimum Price: INR " + minPrice);
        System.out.println("Store with Minimum Price: " + minPriceStore);
        System.out.println("URL: " + minPriceUrl);
        System.out.println("Max Store Booster Score: " + maxStoreBoosterScore);
    }

    // Main method for testing
    public static void main(String[] args) {
        Gson gson = new Gson();
        ScrapedMetadata[] scrapedMetadata = null;

        try (BufferedReader br = new BufferedReader(new FileReader("/Users/sujaynag/projects/apache-beam-practice/src/main/resources/products.json"))) {
            scrapedMetadata = gson.fromJson(br, ScrapedMetadata[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (scrapedMetadata != null) {
            AggregatedProduct aggregatedProduct = new AggregatedProduct("iPhone 13");

            Map<String, Integer> descriptionCounts = new HashMap<>();
            Map<String, Integer> storeCounts = new HashMap<>();

            for (ScrapedMetadata data : scrapedMetadata) {
                aggregatedProduct.aggregate(data);

                String descriptionKey = data.getProductDescription().getStorage() + "|" + data.getProductDescription().getRam();
                descriptionCounts.put(descriptionKey, descriptionCounts.getOrDefault(descriptionKey, 0) + 1);

                storeCounts.put(data.getStore(), storeCounts.getOrDefault(data.getStore(), 0) + 1);
            }

            // Find most repeated product description
            int maxDescriptionCount = 0;
            String mostRepeatedDescription = null;
            for (Map.Entry<String, Integer> entry : descriptionCounts.entrySet()) {
                if (entry.getValue() > maxDescriptionCount) {
                    maxDescriptionCount = entry.getValue();
                    mostRepeatedDescription = entry.getKey();
                }
            }

            // Find most repeated store
            int maxStoreCount = 0;
            String mostRepeatedStore = null;
            for (Map.Entry<String, Integer> entry : storeCounts.entrySet()) {
                if (entry.getValue() > maxStoreCount) {
                    maxStoreCount = entry.getValue();
                    mostRepeatedStore = entry.getKey();
                }
            }

            // Update aggregated product with most repeated description and store
            if (mostRepeatedDescription != null) {
                String[] descriptionParts = mostRepeatedDescription.split("\\|");
                aggregatedProduct.setCommonStorage(descriptionParts[0]);
                aggregatedProduct.setCommonRam(descriptionParts[1]);
            }
            if (mostRepeatedStore != null) {
                aggregatedProduct.setMinPriceStore(mostRepeatedStore);
            }

            aggregatedProduct.displayAggregatedProductInfo();
        }
    }

    private void setMinPriceStore(String mostRepeatedStore) {
        this.minPriceStore = minPriceStore;
    }

    private void setCommonRam(String descriptionPart) {
        this.commonRam = commonRam;
    }

    private void setCommonStorage(String descriptionPart) {
        this.commonStorage = commonStorage;
    }
}
