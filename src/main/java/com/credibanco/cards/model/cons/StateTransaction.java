package com.credibanco.cards.model.cons;

public enum StateTransaction {

    ACTIVE("ACTIVE"),
    ANNULLED("ANNULLED");

    private final String state;

    StateTransaction(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

}
