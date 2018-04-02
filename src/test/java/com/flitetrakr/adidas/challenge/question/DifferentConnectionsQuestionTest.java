package com.flitetrakr.adidas.challenge.question;

import com.flitetrakr.adidas.challenge.model.Airport;
import com.flitetrakr.adidas.challenge.model.ConnectionGraph;
import com.flitetrakr.adidas.challenge.model.FlightSegment;
import com.flitetrakr.adidas.challenge.model.Trip;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

public class DifferentConnectionsQuestionTest {
    private ConnectionGraph graph;

    @Before
    public void setUp() throws Exception {
        graph = new ConnectionGraph();
        graph.addEdge(new FlightSegment(Airport.AMS, Airport.LHR, 300));
        graph.addEdge(new FlightSegment(Airport.AMS, Airport.FRA, 400));
        graph.addEdge(new FlightSegment(Airport.FRA, Airport.NUE, 500));
        graph.addEdge(new FlightSegment(Airport.NUE, Airport.LHR, 600));
        graph.addEdge(new FlightSegment(Airport.LHR, Airport.NUE, 500));
        graph.addEdge(new FlightSegment(Airport.LHR, Airport.FRA, 200));
    }


    @Test
    public void maximumConnectionsTest() throws Exception {
        final String line = "#1: How many different connections with maximum 3 stops exist between AMS and FRA?";
        final Question question = QuestionFactory.getQuestion(line);
        Assert.assertNotNull(question);
        Assert.assertTrue(question instanceof DifferentConnectionsQuestion);
        final List<Trip> answer = question.processQuestion(graph);
        Assert.assertEquals(4, answer.size());
        Assert.assertEquals("[AMS-LHR-NUE-LHR-FRA-1600, AMS-LHR-FRA-500, AMS-FRA-400, AMS-FRA-NUE-LHR-FRA-1700]", answer.toString());
        Assert.assertEquals("#1: 4", question.toString(answer));

    }

    @Test
    public void noConnectionsTest() throws Exception {
        final String line = "#1: How many different connections with maximum 1 stop exist between NUE and AMS?";
        final Question question = QuestionFactory.getQuestion(line);
        Assert.assertNotNull(question);
        Assert.assertTrue(question instanceof DifferentConnectionsQuestion);
        final List<Trip> answer = question.processQuestion(graph);
        Assert.assertEquals(0, answer.size());
        Assert.assertEquals("#1: 0", question.toString(answer));

    }

    @Test
    public void exactlyConnectionsTest() throws Exception {
        final String line = "#1: How many different connections with exactly 1 stop exist between AMS and FRA?";
        final Question question = QuestionFactory.getQuestion(line);
        Assert.assertNotNull(question);
        Assert.assertTrue(question instanceof DifferentConnectionsQuestion);
        final List<Trip> answer = question.processQuestion(graph);
        Assert.assertEquals(1, answer.size());
        Assert.assertEquals("[AMS-LHR-FRA-500]", answer.toString());
        Assert.assertEquals("#1: 1", question.toString(answer));

    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();


    @Test
    public void minimumConnectionsTest() {
        final String line = "#1: How many different connections with minimum 2 stops exist between AMS and LHR?";
        final Question question = QuestionFactory.getQuestion(line);
        Assert.assertNotNull(question);
        Assert.assertTrue(question instanceof DifferentConnectionsQuestion);
        final List<Trip> answer = question.processQuestion(graph);
        Assert.assertTrue(answer.isEmpty());
    }
}