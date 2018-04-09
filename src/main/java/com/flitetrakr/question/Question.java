package com.flitetrakr.question;

import com.flitetrakr.model.ConnectionGraph;
import com.flitetrakr.model.Trip;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface for questions. All questions need to be able to process a
 * given question from a {@link ConnectionGraph} passed as argument.
 */
public interface Question {
    /**
     * This method is to be implemented by every concrete question,
     * given a {@link ConnectionGraph} passed as argument.
     * @param graph a {@link ConnectionGraph}.
     * @return a list of trips answering the question, or an empty list if not trip matches the question
     */
    @NotNull
    List<Trip> processQuestion(final ConnectionGraph graph);

    /**
     * Get a string for printing results specific to the question on the console.
     * Every question has different rules for how to print results.
     * An empty argument means no connections were found satisfying the question.
     */
    @NotNull
    String toString(@NotNull final List<Trip> trips);
}