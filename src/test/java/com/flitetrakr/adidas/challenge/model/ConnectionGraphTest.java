package com.flitetrakr.adidas.challenge.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ConnectionGraphTest {
    @Test
    public void getAllTrips() throws Exception {
        final ConnectionGraph graph = new ConnectionGraph();
        graph.addEdge(new FlightSegment(Airport.AMS, Airport.BOS, 500));
        graph.addEdge(new FlightSegment(Airport.AMS, Airport.PDX, 600));
        graph.addEdge(new FlightSegment(Airport.BOS, Airport.PDX, 700));
        graph.addEdge(new FlightSegment(Airport.PDX, Airport.HKG, 800));
        Assert.assertEquals(4, graph.getNodes().size());
        final List<Trip> trips =graph.getAllTrips(Airport.AMS, Airport.HKG, true, (Trip trip) -> true);
        Assert.assertEquals(2, trips.size());
    }

}