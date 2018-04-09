package com.flitetrakr.model;


import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a flight segment, from a source airport to a destination airport
 * with a certain price.
 */
public final class FlightSegment {
    /**
     * Source airport.
     */
    private final Airport source;
    /**
     * Destination airport.
     */
    private final Airport destination;
    /**
     * Price for this segment. Euros is assumed.
     */
    private final int price;

    /**
     * Constructor.
     * @param source the source {@link Airport} for this flight segment.
     * @param destination the destination {@link Airport} for this flight segment.
     * @param price the price for this flight segment.
     */
    public FlightSegment(@NotNull final Airport source, @NotNull final Airport destination, final int price) {
        this.source = source;
        this.destination = destination;
        this.price = price;
    }

    /**
     * Gets the destination airport of this segment
     * @return an Airport
     */
    @NotNull
    public Airport getDestination() {
        return destination;
    }

    /**
     * Gets the source airport of this segment
     * @return an Airport
     */
    @NotNull
    public Airport getSource() {
        return source;
    }

    /**
     * Gets a set with the airports on this segment.
     * @return a set of aiports
     */
    @NotNull
    /* package */ Set<Airport> getAirports() {
        return new HashSet<>(Arrays.asList(this.source, this.destination));
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s-%s", source, destination);
    }

    public FlightSegment clone() {
        return new FlightSegment(this.source, this.destination, this.price);
    }

}

