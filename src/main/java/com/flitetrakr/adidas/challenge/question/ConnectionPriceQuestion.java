package com.flitetrakr.adidas.challenge.question;

import com.flitetrakr.adidas.challenge.model.Airport;
import com.flitetrakr.adidas.challenge.model.ConnectionGraph;
import com.flitetrakr.adidas.challenge.model.FlightSegment;
import com.flitetrakr.adidas.challenge.model.Trip;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class can handle questions of the form What is the price of the connection XXX-YYY-ZZZ
 * with any number of stops.
 */
/* package */ final class ConnectionPriceQuestion extends AbstractQuestion {

    /**
     * A list of airports representing a trip.
     */
    private List<Airport> airportList = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param questionNumber a question number
     * @param airports       a list of airport representing a trip
     */
    ConnectionPriceQuestion(final int questionNumber, @NotNull final List<Airport> airports) {
        this.questionNumber = questionNumber;
        this.airportList = airports;
    }

    @NotNull
    public List<Trip> processQuestion(@NotNull final ConnectionGraph graph) {
        //System.out.println(String.format("%d price of connection between %s ", questionNumber, Arrays.toString(this.airportList.toArray())));
        // create Trip and get price
        final Trip trip = new Trip();
        List<Trip> result = new ArrayList<>();
        for (int i = 0; i < airportList.size() - 1; i++) {
            Airport src = airportList.get(i);
            Airport dst = airportList.get(i + 1);
            boolean isSegmentValid = false;
            for (final FlightSegment edge : graph.getEdges()) {
                if (edge.getSource().equals(src) && edge.getDestination().equals(dst)) {
                    trip.addSegment(edge);
                    isSegmentValid = true;
                    break;
                }
            }
            if (!isSegmentValid) {
                // segment is not valid, so there are no connections found.
                return result;
            }
        }
        result.add(trip);
        return result;
        //
    }

    @NotNull
    public String toString(@NotNull final List<Trip> trips) {
        if (trips.isEmpty()) {
            return String.format("#%d: No such connection found!", questionNumber);
        }            // only one trip is returned here
        return String.format("#%d: %s", questionNumber, trips.get(0).getPrice());
    }

}
