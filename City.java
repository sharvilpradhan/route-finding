package com.search;

public class City {

    public String name;
    public double heuristicCost = 0.0;
    public double lat = 0.0;
    public double lon = 0.0;

    public double pathCost = 0.0;
    public City(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }
}
