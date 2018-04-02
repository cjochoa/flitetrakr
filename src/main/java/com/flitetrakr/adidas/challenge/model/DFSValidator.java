package com.flitetrakr.adidas.challenge.model;

/**
 * This interface is intended to implement lambda expressions that can
 * validate a trip.
 */
public interface DFSValidator {
    /**
     * Checks if the current search should continue
     * @param trip a trip
     * @return true if the given search should continue
     */
    boolean continueSearch(Trip trip);
}