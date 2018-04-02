package com.flitetrakr.adidas.challenge.model;

import org.jetbrains.annotations.NotNull;

/**
 * This enum list all airports known in the domain.
 * It also provides friendly names for the cities where the airport are located.
 */
public enum Airport {
    NUE ("Herzogenaurach"),
    BOS ("Canton, MA"),
    AMS ("Amsterdam"),
    FRA ("Frankfurt"),
    DXB ("Dubai"),
    HKG ("Hong Kong"),
    PDX ("Portland, OR"),
    LHR ("London-Heathrow");

    /**
     * City name for this airport
     */
    final String city;

    Airport(@NotNull  final String city) {
        this.city = city;
    }

    @NotNull
    public String getCity() {
        return city;
    }
}
