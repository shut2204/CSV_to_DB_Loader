package org.jester2204.model;

public enum ProductType {
    WEIGHABLE(1), UNITS(0);

    private final int value;

    ProductType(int value) {
        this.value = value;
    }

    public String getValue() {
        return Integer.toString(value);
    }
}
