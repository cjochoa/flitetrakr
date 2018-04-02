package com.flitetrakr.adidas.challenge.model;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a trip, which consists of a list of flight segments
 * where for every pair of elements in the list, the destination of a segment
 * is the source of the next.
 * Stops can be duplicated.
 */
public final class Trip {
    /**
     * List of segments for this trip.
     */
    private final List<FlightSegment> segments;

    /**
     * Public constructor.
     */
    public Trip() {
        segments = new ArrayList<>();
    }

    /**
     * Gets the initial airport where this trip starts.
     * @return initial airport
     */
    @Nullable
    public Airport getSource() {
        if (segments.isEmpty()) {
            return null;
        }
        return segments.get(0).getSource();
    }

    /**
     * Gets the final airport where this trip ends.
     * @return final airport
     */
    @Nullable
    public  Airport getDestination() {
        if (segments.isEmpty()) {
            return null;
        }
        return segments.get(segments.size()-1).getDestination();
    }

    /**
     * Adds a segment to the trip. It checks that the destination of the current trip
     * is the same as the source of the segment to be added.
     * @param segment a flight segment
     * @return True if the segment can be added, false otherwise
     */
    public boolean addSegment(@NotNull final FlightSegment segment) {
        if (!segments.isEmpty() && getDestination() != segment.getSource()) {
            return false;
        }
        // else add this segment
        return this.segments.add(segment);
    }

    /**
     * Removes the last segment in the trip and returns it.
     * @return the last segment, null if the trip has no segment.
     */
    @Nullable
    public  FlightSegment removeLastSegment() {
        if (segments.isEmpty()) {
            return null;
        }
        return segments.remove(segments.size()-1);
    }

    /**
     * @return a list of all segments in this trip, in order.
     */
    @NotNull
    /* package */  List<FlightSegment> getSegments() {
        return segments;
    }

    /**
     * Checks if the current trip contains any loops, i.e., an airport is twice
     * @return true if the current trip contains any loops
     */
    public boolean containsLoops() {
        final Set<Airport> distinctAirports = segments.stream().map(FlightSegment::getAirports).flatMap(Collection::stream).collect(Collectors.toSet());
        // if there are no loops, then #segments == #airports - 1
        return segments.isEmpty() || segments.size() != distinctAirports.size() - 1;
    }

    /**
     * Checks if the given airport exists in the trip (as source, destination, or stop)
     * @param airport an Airport
     * @return true if the given airport exists in the trip, false otherwise
     */
    /* package */  boolean containsAirport(@NotNull  final Airport airport) {
        return segments.stream().filter(segment -> segment.getSource().equals(airport) || segment.getDestination().equals(airport)).count() > 0;
    }

    /**
     * Gets the price for the whole trip
     * @return price for this trip, 0 if the trip has no segments
     */
    public int getPrice() {
        if (segments.isEmpty()) {
            return 0;
        }
        return segments.stream().mapToInt(FlightSegment::getPrice).sum();
    }

    /**
     * Gets the number of stops for this trip.
     * @return the number of stops for this trip.
     */
    public int getStopsNumber() {
        return segments.isEmpty() ? 0 : segments.size()-1;
    }

    /**
     * @return a string representation of a trip
     */
    @Override
    public String toString() {
        if (segments.isEmpty()) {
            return "";
        }
        return String.format("%s-%s-%d", segments.get(0).getSource(), segments.stream().map(FlightSegment::getDestination).map(Airport::toString)
                .collect(Collectors.joining("-")), getPrice());
    }

    /**
     * This method performs a deep clone, cloning every flight segment in the trip
     * @return a cloned trip.
     */
    public Trip clone(){
        final Trip clone = new Trip();
        for (final FlightSegment segment : segments) {
            clone.addSegment(segment.clone());
        }
        return clone;
    }

}
