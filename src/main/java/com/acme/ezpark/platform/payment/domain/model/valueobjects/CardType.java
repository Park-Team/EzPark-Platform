package com.acme.ezpark.platform.payment.domain.model.valueobjects;

public enum CardType {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    AMERICAN_EXPRESS("American Express"),
    DISCOVER("Discover"),
    DINERS_CLUB("Diners Club"),
    JCB("JCB");

    private final String displayName;

    CardType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CardType fromCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            throw new IllegalArgumentException("Invalid card number");
        }
        
        String firstFour = cardNumber.substring(0, 4);
        int firstDigit = Integer.parseInt(firstFour.substring(0, 1));
        
        return switch (firstDigit) {
            case 4 -> VISA;
            case 5 -> MASTERCARD;
            case 3 -> cardNumber.startsWith("34") || cardNumber.startsWith("37") ? 
                     AMERICAN_EXPRESS : DINERS_CLUB;
            case 6 -> DISCOVER;
            default -> throw new IllegalArgumentException("Unsupported card type");
        };
    }
}
