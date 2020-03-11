package com.search;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ReadDataFromFile {

    public static LinkedHashMap<String, City> readCityFile() {
        System.out.println(System.getProperty("user.dir"));
        String fileName = System.getProperty("user.dir") + "/DataFiles/cityLocation.txt";
        System.out.println("City location file name : " + fileName);
        LinkedHashMap<String, City> citiesInfoMap = new LinkedHashMap<>();
        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                //process the line
                String cityInfo = line.substring(line.indexOf("(")+1,line.indexOf(")"));
                String[] cityInfoArray = cityInfo.split(",");

                String cityName = cityInfoArray[0].trim();
                double latitude = Double.parseDouble(cityInfoArray[1].trim());
                double longitude = Double.parseDouble(cityInfoArray[2].trim());
                City cityObj = new City(cityName, latitude, longitude);
                citiesInfoMap.put(cityName, cityObj);
            }
            System.out.println("City Location Parsing Done !!!");
            return citiesInfoMap;
        } catch (Exception e) {
            e.printStackTrace();
            return citiesInfoMap;
        }
    }

    public static LinkedHashMap<String, City> calculateHeuristics(City destinationCity, LinkedHashMap<String, City> cities) {
        for(City sourceCity: cities.values()) {
            sourceCity.heuristicCost = heuristicFunction(sourceCity, destinationCity);
        }
        return cities;
    }

    public static City fetchCity(String cityName, LinkedHashMap<String, City> cities) {
        return cities.get(cityName);
    }

    public static double heuristicFunction(City source, City destination) {
        double heuristicCost = Math.sqrt( Math.pow( (69.5 * (source.lat - destination.lat)), 2.0) +
                Math.pow( (69.5 * Math.cos((source.lat + destination.lat)/360 * 3.14159) * (source.lon - destination.lon)), 2.0)
        );
        return heuristicCost;
    }

    public static LinkedHashMap<String, List<AdjacentNode>> createGraphInfoMap(LinkedHashMap<String, City> cities) {
        String fileName = System.getProperty("user.dir") + "/DataFiles/roadData.txt";
        System.out.println("Road data file name : " + fileName);
        LinkedHashMap<String, List<AdjacentNode>> allGraphInfo = new LinkedHashMap<>();
        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                String[] roads = line.split("\\.");
                for (String road : roads) {
                    road = road.trim();
                    String roadInfo = road.substring(road.indexOf("(")+1,road.indexOf(")"));
                    String[] roadInfoArray = roadInfo.split(",");
                    String startCityName = roadInfoArray[0].trim();
                    String endCityName = roadInfoArray[1].trim();
                    double distance = Double.parseDouble(roadInfoArray[2].trim());

                    // For a link A - B

                    // For A -> B
                    // Here B acts as adjacent node
                    // for start node as adjacent node
                    AdjacentNode adjNodeObj = new AdjacentNode();
                    adjNodeObj.cityName = endCityName;
                    adjNodeObj.distanceCost = distance;
                    City endCity = cities.get(endCityName);
                    adjNodeObj.heuristicCost = endCity.heuristicCost;
                    adjNodeObj.totalCost = adjNodeObj.distanceCost + adjNodeObj.heuristicCost;
                    if (allGraphInfo.containsKey(startCityName)) {
                        allGraphInfo.get(startCityName).add(adjNodeObj);
                    } else {
                        List<AdjacentNode> eachNode = new ArrayList<>();
                        eachNode.add(adjNodeObj);
                        allGraphInfo.put(startCityName, eachNode);
                    }

                    // For  B -> A
                    // Here A acts as adjacent node
                    //for end node as adjacent node
                    AdjacentNode adjNodeObj2 = new AdjacentNode();
                    adjNodeObj2.cityName = startCityName;
                    adjNodeObj2.distanceCost = distance;
                    City startCity = cities.get(startCityName);
                    adjNodeObj2.heuristicCost = startCity.heuristicCost;
                    adjNodeObj2.totalCost = adjNodeObj2.distanceCost + adjNodeObj2.heuristicCost;
                    if (allGraphInfo.containsKey(endCityName)) {
                        allGraphInfo.get(endCityName).add(adjNodeObj2);
                    } else {
                        List<AdjacentNode> eachNode = new ArrayList<>();
                        eachNode.add(adjNodeObj2);
                        allGraphInfo.put(endCityName, eachNode);
                    }

                }
            }
            System.out.println("Creating road graph Parsing Done !!!");
            return allGraphInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return allGraphInfo;
        }
    }
}
