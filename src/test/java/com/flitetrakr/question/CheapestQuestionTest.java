package com.flitetrakr.question;

import com.flitetrakr.model.Airport;
import com.flitetrakr.model.ConnectionGraph;
import com.flitetrakr.model.FlightSegment;
import com.flitetrakr.model.Trip;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class CheapestQuestionTest {

    private ConnectionGraph graph;

    @Before
    public void setUp() throws Exception {
        graph = new ConnectionGraph();
        graph.addEdge(new FlightSegment(Airport.AMS, Airport.LHR, 300));
        graph.addEdge(new FlightSegment(Airport.AMS, Airport.FRA, 400));
        graph.addEdge(new FlightSegment(Airport.FRA, Airport.NUE, 500));
        graph.addEdge(new FlightSegment(Airport.NUE, Airport.LHR, 600));
        graph.addEdge(new FlightSegment(Airport.LHR, Airport.NUE, 500));
    }


    @Test
    public void processWrongQuestionTest() throws Exception {
        final String line = "";
        final Optional<Question> question = QuestionFactory.getQuestion(line);
        Assert.assertFalse(question.isPresent());
    }

    @Test
    public void cheapestFlightTest() throws Exception {
        final String line = "#7: What is the cheapest connection from AMS to NUE?";
        final Optional<Question> question = QuestionFactory.getQuestion(line);
        Assert.assertTrue(question.isPresent());
        Assert.assertTrue(question.get() instanceof CheapestQuestion);
        final List<Trip> answer = question.get().processQuestion(graph);
        Assert.assertEquals(1, answer.size());
        Assert.assertEquals("#7: AMS-LHR-NUE-800", question.get().toString(answer));

    }

    @Test
    public void loopTest() throws Exception {
        final String line = "#25: What is the cheapest connection from NUE to NUE?";
        final Optional<Question> question = QuestionFactory.getQuestion(line);
        Assert.assertTrue(question.isPresent());
        Assert.assertTrue(question.get() instanceof CheapestQuestion);
        final List<Trip> answer = question.get().processQuestion(graph);
        Assert.assertEquals(1, answer.size());
        Assert.assertEquals("#25: NUE-LHR-NUE-1100", question.get().toString(answer));
    }

    @Test
    public void noConnectionFlightTest() throws Exception {
        final String line = "#1: What is the cheapest connection from NUE to AMS?";
        final Optional<Question> question = QuestionFactory.getQuestion(line);
        Assert.assertTrue(question.isPresent());
        Assert.assertTrue(question.get() instanceof CheapestQuestion);
        final List<Trip> answer = question.get().processQuestion(graph);
        Assert.assertTrue( answer.isEmpty());

    }
}