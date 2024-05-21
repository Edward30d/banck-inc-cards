package com.credibanco.cards.model.cons;

public enum StateCardEnum {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    BLOCKED("BLOCKED"),
    DEFEATED("DEFEATED");

    private final String state;

    StateCardEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}