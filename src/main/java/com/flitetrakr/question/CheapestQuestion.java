package com.flitetrakr.question;

import com.flitetrakr.model.Airport;
import com.flitetrakr.model.ConnectionGraph;
import com.flitetrakr.model.Trip;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class can handle questions of the form What is the cheapest connection XXX to YYY?
 */
/* package */ final class CheapestQuestion extends AbstractQuestion {
    /**
     * Source airport
     */
    private final Airport source;
    /**
     * Destination airport
     */
    private final Airport destination;

    /**
     * Constructor
     * @param questionNumber question number in input list
     * @param from source airport
     * @param to target airport
     */
    CheapestQuestion(final int questionNumber, @NotNull final Airport from, @NotNull final Airport to) {
        this.questionNumber = questionNumber;
        this.source = from;
        this.destination = to;
    }

    @NotNull
    public List<Trip> processQuestion(@NotNull final ConnectionGraph graph) {
        // System.out.println(String.format("%d cheapest connection between %s ", questionNumber, Arrays.toString(this.airportList.toArray())));
        int cheapestPrice = Integer.MAX_VALUE;
        List<Trip> result = new ArrayList<>();
        Trip cheapestTrip = null;
        for (final Trip trip : graph.getAllTrips(source, destination, source.equals(destination), (Trip trip) -> (source.equals(destination) && destination.equals(trip.getDestination()) ) || !trip.containsLoops())) {
            // System.out.println(String.format("Trip: %s", trip));
            if (trip.getPrice() < cheapestPrice) {
                cheapestTrip = trip;
                cheapestPrice = trip.getPrice();
            }
        }

        if (cheapestTrip != null) {
            result.add(cheapestTrip);
            //return String.format("#%d: %s", questionNumber, cheapestTrip);
        }
        return result;
    }

    @NotNull
    public String toString(@NotNull final List<Trip> trips) {
        if (trips.isEmpty()) {
            return String.format("#%d: No such connection found!", questionNumber);
        } else {
            // only one trip is returned here
            return String.format("#%d: %s", questionNumber, trips.get(0));
        }
    }
}
