package org.example.data;

public class Product {
    private int id;
    private String url;
    private String image;
    private String name;
    private String price;

    public Product(){};

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return id + "\n" +
                "{ \"url\":\"" + url + "\", " + "\n"
                + " \"image\":\"" + image + "\", " + "\n"
                + "\"name\":\"" + name + "\", " + "\n"
                + "\"price\": \"" + price + "\" }" + "\n";
    }
}
