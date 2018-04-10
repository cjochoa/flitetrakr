package com.flitetrakr.model;

import org.junit.Assert;
import org.junit.Test;

public class TripTest {
    @Test
    public void removeLastSegment() throws Exception {
        final Trip trip = new Trip();
        trip.addSegment(new FlightSegment(Airport.AMS, Airport.BOS, 575));
        Assert.assertEquals(0, trip.getStopsNumber());
        trip.addSegment(new FlightSegment(Airport.BOS, Airport.DXB, 675));
        Assert.assertEquals(1, trip.getStopsNumber());
        Assert.assertTrue(trip.getSource().isPresent());
        Assert.assertEquals(Airport.AMS, trip.getSource().get());
        Assert.assertTrue(trip.getDestination().isPresent());
        Assert.assertEquals(Airport.DXB, trip.getDestination().get());
        trip.removeLastSegment();
        Assert.assertEquals(0, trip.getStopsNumber());
        Assert.assertEquals(Airport.AMS, trip.getSource().get());
        Assert.assertEquals(Airport.BOS, trip.getDestination().get());
        trip.removeLastSegment();
    }

    @Test
    public void containsLoops() throws Exception {
        final Trip trip = new Trip();
        trip.addSegment(new FlightSegment(Airport.AMS, Airport.BOS, 575));
        Assert.assertEquals(0, trip.getStopsNumber());
        trip.addSegment(new FlightSegment(Airport.BOS, Airport.DXB, 675));
        Assert.assertEquals(1, trip.getStopsNumber());
        Assert.assertFalse(trip.containsLoops());
        trip.addSegment(new FlightSegment(Airport.DXB, Airport.BOS, 775));
        Assert.assertTrue(trip.containsLoops());
    }

    @Test
    public void containsAirport() throws Exception {
        final Trip trip = new Trip();
        trip.addSegment(new FlightSegment(Airport.AMS, Airport.BOS, 575));
        Assert.assertTrue(trip.containsAirport(Airport.BOS));
        Assert.assertFalse(trip.containsAirport(Airport.DXB));
    }

    @Test
    public void getPrice() throws Exception {
        final Trip trip = new Trip();
        trip.addSegment(new FlightSegment(Airport.AMS, Airport.BOS, 575));
        Assert.assertEquals(0, trip.getStopsNumber());
        trip.addSegment(new FlightSegment(Airport.BOS, Airport.DXB, 675));
        Assert.assertEquals(1250, trip.getPrice());
    }

}