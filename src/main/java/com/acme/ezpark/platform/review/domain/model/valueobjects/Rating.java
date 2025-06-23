package com.acme.ezpark.platform.review.domain.model.valueobjects;


public enum Rating {
    ONE_STAR(1, "⭐"),
    TWO_STARS(2, "⭐⭐"), 
    THREE_STARS(3, "⭐⭐⭐"),
    FOUR_STARS(4, "⭐⭐⭐⭐"),
    FIVE_STARS(5, "⭐⭐⭐⭐⭐");
    
    private final int value;
    private final String display;
    
    Rating(int value, String display) {
        this.value = value;
        this.display = display;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getDisplay() {
        return display;
    }
    
    public static Rating fromValue(int value) {
        for (Rating rating : Rating.values()) {
            if (rating.value == value) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid rating value: " + value + ". Must be between 1 and 5.");
    }
}
