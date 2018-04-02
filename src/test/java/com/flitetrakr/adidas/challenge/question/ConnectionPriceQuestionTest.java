package com.flitetrakr.adidas.challenge.question;

import com.flitetrakr.adidas.challenge.model.Airport;
import com.flitetrakr.adidas.challenge.model.ConnectionGraph;
import com.flitetrakr.adidas.challenge.model.FlightSegment;
import com.flitetrakr.adidas.challenge.model.Trip;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ConnectionPriceQuestionTest {

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
    public void getPriceTest() throws Exception {
        final String line = "#1: What is the price of the connection AMS-LHR-NUE-LHR?";
        final Question question = QuestionFactory.getQuestion(line);
        Assert.assertNotNull(question);
        Assert.assertTrue(question instanceof ConnectionPriceQuestion);
        final List<Trip> answer = question.processQuestion(graph);
        Assert.assertEquals(1, answer.size());
        Assert.assertEquals("#1: 1400", question.toString(answer));
    }

    @Test
    public void getInvalidPriceTest() throws Exception {
        final String line = "#12: What is the price of the connection NUE-AMS-BOS?";
        final Question question = QuestionFactory.getQuestion(line);
        Assert.assertNotNull(question);
        Assert.assertTrue(question instanceof ConnectionPriceQuestion);
        final List<Trip> answer = question.processQuestion(graph);
        Assert.assertEquals(0, answer.size());
        Assert.assertEquals("#12: No such connection found!", question.toString(answer));
    }
}