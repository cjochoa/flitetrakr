package com.flitetrakr.adidas.challenge.model;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a graph of flight connections.
 * Every node in this graph is an airport, and every edge is a flight segment connecting airports.
 */
public final class ConnectionGraph {

    /**
     * Nodes for this graph. We use a set since nodes cannot be duplicated.
     */
    private Set<Airport> nodes = new HashSet<>();
    /**
     * Edges for this graph. We allow multigraphs, so multiple segments between the same pair of
     * airports (with different prices) are allowed.
     */
    private List<FlightSegment> edges = new ArrayList<>();

    /**
     * Constructor.
     */
    public ConnectionGraph() {
    }

    /**
     * Gets all the nodes (airports) in this graph.
     *
     * @return a list of airports.
     */
    @NotNull
    public Set<Airport> getNodes() {
        return nodes;
    }

    /**
     * Gets all the edges (flight segments) in this graph.
     *
     * @return a list of flight segments.
     */
    @NotNull
    public List<FlightSegment> getEdges() {
        return edges;
    }

    /**
     * Adds a node (airport) to this graph.
     *
     * @param airport an Airport
     */
    public void addNode(@NotNull  final Airport airport) {
        this.nodes.add(airport);
    }

    /**
     * Adds a node (airport) to this graph.
     * if airports in the segment are not part of this graph, they are added.
     *
     * @param segment a valid segment
     */
    public void addEdge(@NotNull  final FlightSegment segment) {
        // add nodes just in case
        this.nodes.add(segment.getSource());
        this.nodes.add(segment.getDestination());
        // now add segment
        this.edges.add(segment);
    }

    /**
     * Searches a list of all {@link Trip} between {@code src} and {@code dst}.
     * It does this by performing a Depth First Search on the {@code src}.
     * The main difficulty is with the fact that, for some questions,
     * loops are allowed, so we need to make sure that we don't fall in a
     *  infinite loop. To avoid this, we add a predicate {@code stopSearch}
     *  that we can use to stop searching.
     * @param src A source {@link Airport}
     * @param dst A destination {@link Airport}
     * @param loopsAllowed True if loops are allowed in this search. Note that a proper condition must be used in {@code stopSearch} to avoid infinite loops
     * @param stopSearch a predicate that determines if we should keep searching or not.
     * @return a list of trips.
     */
    @NotNull
    public List<Trip> getAllTrips(@NotNull  final Airport src,
                                  @NotNull  final Airport dst,
                                  final boolean loopsAllowed,
                                  @NotNull final DFSValidator stopSearch) {
        final List<Trip> result = new ArrayList<>();
        Trip currentTrip = new Trip();
        for (final FlightSegment edge : getEdges()) {
            if (edge.getSource().equals(src)) {
                currentTrip.addSegment(edge);
                result.addAll(getAllTripsTemp(currentTrip, edge.getDestination(), dst, loopsAllowed, stopSearch));
                currentTrip.removeLastSegment();
            }
        }
        return result;
    }

    @NotNull
    private List<Trip> getAllTripsTemp(@NotNull final Trip currentTrip,
                                       @NotNull final Airport src,
                                       @NotNull final Airport dst,
                                       final boolean loopsAllowed,
                                       @NotNull final DFSValidator stopSearch) {
        final List<Trip> result = new ArrayList<>();

        if (src.equals(dst) && stopSearch.continueSearch(currentTrip)) {
            result.add(currentTrip.clone()); // clone trip, otherwise changes to trip will affect the final result
        }

        for (final FlightSegment edge : getEdges()) {
            if (edge.getSource().equals(src) &&
                    (loopsAllowed || !currentTrip.containsAirport(edge.getDestination()))) { // do not allow other loops
                currentTrip.addSegment(edge);
                if (stopSearch.continueSearch(currentTrip)) { // if current trip is no longer valid, disregard
                    // keep iterating
                    result.addAll(getAllTripsTemp(currentTrip, edge.getDestination(), dst, loopsAllowed, stopSearch));
                }
                currentTrip.removeLastSegment();
            }
        }

        return result;
    }

}
