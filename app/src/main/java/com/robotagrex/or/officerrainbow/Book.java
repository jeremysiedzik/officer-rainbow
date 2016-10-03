package com.robotagrex.or.officerrainbow;

class Book {
    private final int name;
    private final int author;
    private final int imageResource;
    private boolean isFavorite = false;
    private final String imageUrl;

    Book(int name, int author, int imageResource, String imageUrl) {
        this.name = name;
        this.author = author;
        this.imageResource = imageResource;
        this.imageUrl = imageUrl;
    }

    public int getName() {
        return name;
    }

    int getAuthor() {
        return author;
    }

    int getImageResource() {
        return imageResource;
    }

    boolean getIsFavorite() {
        return isFavorite;
    }
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    void toggleFavorite() {
        isFavorite = !isFavorite;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}