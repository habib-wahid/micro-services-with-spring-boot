package org.example.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventEnum {
    PRODUCT_PURCHASE("Product purchased"),
    CREATE_ORDER("Create order"),
    CONFIRM_ORDER("Confirm order"),
    CANCEL_ORDER("Cancel order"),
    NOTIFY_CUSTOMER("Notify customer");

    private final String value;

    public String getValue() {
        return value;
    }
}
