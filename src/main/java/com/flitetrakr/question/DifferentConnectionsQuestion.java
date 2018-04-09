package com.flitetrakr.question;

import com.flitetrakr.model.Airport;
import com.flitetrakr.model.ConnectionGraph;
import com.flitetrakr.model.Trip;
import com.flitetrakr.model.DFSValidator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class can handle questions of the form How many different connections with X stops exist between YYY and ZZZ?
 * with any number of stops.
 */
/* package */ final class DifferentConnectionsQuestion extends AbstractQuestion {

    /**
     * This interface is intended to implement lambda expressions that can
     * validate a trip.
     */
    private interface TripValidator {
        /**
         * Checks if the given trip is valid for the current problem statement
         * @param trip a trip
         * @return true if the given trip is valid
         */
        boolean validTrip(Trip trip);
    }

    /**
     * Compares actual stops vs desired stops.
     */
    /* package */  enum StopsComparison {
        AtMost("maximum"),
        AtLeast("minimum"),
        Exactly("exactly");

        final String value;

        StopsComparison(@NotNull final String value) {
            this.value = value;
        }

        /**
         * Converts a string into an instance of this enum. Note that multiple
         * synonyms could be allowed, by having "value" as a List<String> with allowed value
         * @param str a string
         * @return an instance of {@link StopsComparison}
         */
        static StopsComparison fromString(@NotNull final String str) {
            for (final StopsComparison comp : StopsComparison.values()) {
                if (comp.value.equalsIgnoreCase(str)) {
                    return comp;
                }
            }
            throw new IllegalArgumentException(String.format("%s is not supported to compare number of stops", str));
        }
    }

    /**
     * StopsComparison of this instance
     */
    private StopsComparison operator;
    /**
     * Source airport
     */
    private final Airport source;
    /**
     * Destination airport
     */
    private final Airport destination;
    /**
     * Number of stops allowed in the question.
     */
    private final int stops;

    /**
     * Constructor
     * @param questionNumber question number in input list
     * @param from source airport
     * @param to target airport
     * @param operator operator that is combined with stops
     * @param stops number of stops to be considered in operator
     */
    DifferentConnectionsQuestion(final int questionNumber,
                                 @NotNull final Airport from,
                                 @NotNull final Airport to,
                                 @NotNull final DifferentConnectionsQuestion.StopsComparison operator,
                                 final int stops) {
        this.questionNumber = questionNumber;
        this.source = from;
        this.destination = to;
        this.operator = operator;
        this.stops = stops;
    }

    @NotNull
    public List<Trip> processQuestion(@NotNull final ConnectionGraph graph) {
        // System.out.println(String.format("#%d find connections between %s with %s %d stops", questionNumber, Arrays.toString(this.airportList.toArray()), this.operator, this.stops));
        final DFSValidator dfsValidator;
        final TripValidator tripValidator;
        switch (this.operator) {
            // in all cases, we take 2 from the number of airports, since the initial and end
            // airports are not part of the stops
            case Exactly:
                tripValidator = (Trip trip) -> trip.getStopsNumber() == this.stops;
                // note that we need to keep searching until we get to the actual number of stops desired
                // For example, if we want exactly 3 stops, trips with 0, 1 and 2 stops are potentially
                // valid, and we need to continue searching in case they finally result in trips with 3 stops.
                dfsValidator = (Trip trip) -> trip.getStopsNumber() <= this.stops;
                break;
            case AtMost:
                tripValidator = (Trip trip) -> trip.getStopsNumber() <= this.stops;
                dfsValidator = (Trip trip) -> trip.getStopsNumber() <= this.stops;
                break;
            case AtLeast:
                tripValidator = (Trip trip) -> trip.getStopsNumber() >= this.stops;
                // This could result in infinite loops, however that is allowed by the
                // exercise statement. In a real situation we should add some other condition (e.g. max price)
                // note that we always keep searching, and we cannot stop the search.
                // dfsValidator = (Trip trip) -> true;
                System.out.println(String.format("#%d: %s", questionNumber, "Looking for a minimum number of stops might result in a infinite loop. The current problem statement is wrong and needs to add an additional condition "));
                return new ArrayList<>();
                // break;
            default:
                // we should never get here, unless a new operator is added.
                throw new IllegalArgumentException("Unexpected operation: " + this.operator);
        }
        final List<Trip> result = new ArrayList<>();
        for (final Trip trip : graph.getAllTrips(source, destination, true, dfsValidator)) {
            if (tripValidator.validTrip(trip)) {
                result.add(trip);
            }
        }
        return result;
    }

    @NotNull
    public String toString(@NotNull final List<Trip> trips) {
        return String.format("#%d: %d", questionNumber, trips.size());
    }
}

