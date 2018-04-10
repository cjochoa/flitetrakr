package com.flitetrakr.question;

import com.flitetrakr.model.Airport;
import com.flitetrakr.model.ConnectionGraph;
import com.flitetrakr.model.Trip;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class can handle questions of the form Find all connections from XXX to YYY below ZZZ Euros
 * with any number of stops.
 */
/* package */ final class AllConnectionsQuestion extends AbstractQuestion {
    /**
     * Source airport
     */
    private final Airport source;
    /**
     * Destination airport
     */
    private final Airport destination;
    /**
     * AtMost price for this connection
     */
    private final int price;

    /**
     * Constructor
     * @param questionNumber question number in input list
     * @param from source airport
     * @param to target airport
     * @param topPrice maximum price for connection
     */
    AllConnectionsQuestion(final int questionNumber,
                           @NotNull final Airport from,
                           @NotNull final Airport to,
                           final int topPrice) {
        this.questionNumber = questionNumber;
        this.source = from;
        this.destination = to;
        this.price = topPrice;
    }

    @NotNull
    public List<Trip> processQuestion(@NotNull final ConnectionGraph graph) {
        //System.out.println(String.format("%d find all connections between %s below %d Euros", questionNumber, Arrays.toString(this.airportList.toArray()) , this.price));
        List<Trip> tripList = new ArrayList<>();
        for (final Trip trip : graph.getAllTrips(source, destination, true, (Trip trip) -> trip.getPrice() < this.price)) {
            if (trip.getDestination().isPresent() && trip.getDestination().get().equals(destination)) {
                tripList.add(trip);
            }
        }
        return tripList;
        //return String.format("#%d: %s", questionNumber, tripList.stream()
        //        .collect(Collectors.joining(", ")));
    }

    @NotNull
    public String toString(@NotNull final List<Trip> trips) {
        if (trips.isEmpty()) {
            return String.format("#%d: No connections found!", questionNumber);
        }
        return String.format("#%d: %s", questionNumber, trips.stream().map(Trip::toString)
                        .collect(Collectors.joining(", ")));
    }
}
