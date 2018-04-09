package com.flitetrakr.question;

import com.flitetrakr.model.Airport;
import com.flitetrakr.model.ConnectionGraph;
import com.flitetrakr.model.FlightSegment;
import com.flitetrakr.model.Trip;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AllConnectionsQuestionTest {

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
    public void allConnectionsTest() throws Exception {
        final String line = "#78: Find all connections from FRA to LHR below 2500 Euros!";
        final Question question = QuestionFactory.getQuestion(line);
        Assert.assertNotNull(question);
        Assert.assertTrue(question instanceof AllConnectionsQuestion);
        final List<Trip> answer = question.processQuestion(graph);
        Assert.assertEquals(2, answer.size());
        Assert.assertEquals("#78: FRA-NUE-LHR-1100, FRA-NUE-LHR-NUE-LHR-2200", question.toString(answer));

    }

    @Test
    public void noConnectionsTest() throws Exception {
        final String line = "#78: Find all connections from FRA to LHR below 300 Euros!";
        final Question question = QuestionFactory.getQuestion(line);
        Assert.assertNotNull(question);
        Assert.assertTrue(question instanceof AllConnectionsQuestion);
        final List<Trip> answer = question.processQuestion(graph);
        Assert.assertEquals(0, answer.size());
        Assert.assertEquals("#78: No connections found!", question.toString(answer));

    }
}