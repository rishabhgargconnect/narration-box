package edu.tamu.narrationbox.enums;

public enum ImgType {
    CATEGORY("category"),
    EMOTION("emotion");

    public String getValue() {
        return value;
    }

    private String value;

    ImgType(String value) {
        this.value = value;
    }
}
