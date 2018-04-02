package com.flitetrakr.adidas.challenge.question;

import com.flitetrakr.adidas.challenge.model.Airport;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements a Factory pattern to create a specific Question from a source line.
 * If variations of such questions need to be handled, only this class needs to be modified
 */
public final class QuestionFactory {
    /**
     * Pattern of the questions for {@link ConnectionPriceQuestion}.
     */
    @NonNls
    private final static Pattern connectionPricePattern = Pattern.compile("#(\\d+):\\sWhat is the price of the connection (.*)\\?");

    /**
     * Pattern of the questions for {@link CheapestQuestion}.
     */
    @NonNls
    private final static Pattern cheapestPricePattern = Pattern.compile("#(\\d+):\\sWhat is the cheapest connection from (.*)\\?");

    /**
     * Pattern of the questions for {@link AllConnectionsQuestion}
     */
    @NonNls
    private final static Pattern allConnectionsPattern = Pattern.compile("#(\\d+):\\sFind all connections from (.*) below (\\d+) Euros!");

    /**
     * Pattern of the questions for {@link DifferentConnectionsQuestion}.
     */
    @NonNls
    private final static Pattern differentConnectionsPattern = Pattern.compile("#(\\d+):\\sHow many different connections with (maximum|minimum|exactly) (\\d) stop[s]? exist between (.*)\\?");



    /**
     * Gets a list of airports specified in the question and stores them
     * as a list for further processing
     * @param str a string with 2 or more airport codes
     * @param delimiter a delimiter separating the airports.
     */
    @NotNull
    private static List<Airport> extractAirports(@NotNull final String str, @NotNull  final String delimiter) {
        final List<Airport> result = new ArrayList<>();
        for (final String airport : str.split(delimiter)) {
            try {
                result.add(Airport.valueOf(airport));
            } catch (final IllegalArgumentException ex) {
                System.out.println("Wrong airport " + airport + ": " + ex.getMessage());
            }
        }
        return result;
    }

    @Nullable
    public static Question getQuestion(@NotNull final String line) {
        Matcher m = connectionPricePattern.matcher(line);
        if (m.find()) {
            // note that m.group(1) is a number, since this is ensured by the RE
            final int questionNumber = Integer.valueOf(m.group(1));

            return new ConnectionPriceQuestion(questionNumber, extractAirports(m.group(2), "-"));
        }

        m = cheapestPricePattern.matcher(line);
        if (m.find()) {
            // note that m.group(1) is a number, since this is ensured by the RE
            final int questionNumber = Integer.valueOf(m.group(1));
            final List<Airport> airports = extractAirports(m.group(2), " to ");

            if (airports.size() != 2) {
                System.out.println("Wrong number of airports in " + line);
                return null;
            }
            return new CheapestQuestion(questionNumber, airports.get(0), airports.get(1));
        }

        m = allConnectionsPattern.matcher(line);
        if (m.find()) {
            // note that m.group(1) is a number, since this is ensured by the RE
            final int questionNumber = Integer.valueOf(m.group(1));
            final List<Airport> airports = extractAirports(m.group(2), " to ");

            if (airports.size() != 2) {
                System.out.println("Wrong number of airports in " + line);
                return null;
            }
            return new AllConnectionsQuestion(questionNumber, airports.get(0), airports.get(1), Integer.valueOf(m.group(3)));
        }

        m = differentConnectionsPattern.matcher(line);

        if (m.find()) {
            // note that m.group(1) is a number, since this is ensured by the RE
            final int questionNumber = Integer.valueOf(m.group(1));
            try {
                final DifferentConnectionsQuestion.StopsComparison comparisonOperator = DifferentConnectionsQuestion.StopsComparison.fromString(m.group(2));


                final int stops = Integer.valueOf(m.group(3));
                List<Airport> airports = extractAirports(m.group(4), " and ");

                if (airports.size() != 2) {
                    System.out.println("Wrong number of airports in " + line);
                    return null;
                }
                return new DifferentConnectionsQuestion(questionNumber, airports.get(0), airports.get(1), comparisonOperator, stops);
            } catch (final IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return null;
    }

}