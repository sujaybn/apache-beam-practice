package com.product.aggregate;

class ScrapedMetadata {
    private String store;
    private String productTitle;
    private ProductDescription productDescription;
    private String url;
    private int storeBoosterScore;

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public ProductDescription getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(ProductDescription productDescription) {
        this.productDescription = productDescription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStoreBoosterScore() {
        return storeBoosterScore;
    }

    public void setStoreBoosterScore(int storeBoosterScore) {
        this.storeBoosterScore = storeBoosterScore;
    }
}
