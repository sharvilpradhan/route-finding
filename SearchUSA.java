package com.search;




import java.util.LinkedHashMap;
import java.util.List;

public class SearchUSA {

    public static void main(String[] args) {

        // astar, greedy, dynamic
        String algoName = args[0];
        // source city
        String sourceCityName = args[1];
        // destination city
        String destinationCityName = args[2];

        // Read city data
        LinkedHashMap<String, City> cityData = ReadDataFromFile.readCityFile();


        // get source, destination
        City sourceCity = ReadDataFromFile.fetchCity(sourceCityName, cityData);;
        City destinationCity = ReadDataFromFile.fetchCity(destinationCityName, cityData);

        // Calculate Heuristic cost
        if (destinationCity != null && sourceCity != null) {
            LinkedHashMap<String, City> cityWithHeuristics = ReadDataFromFile.calculateHeuristics(destinationCity, cityData);

            // Saving graph info
            LinkedHashMap<String, List<AdjacentNode>> roadInfoHashMap = ReadDataFromFile.createGraphInfoMap(cityWithHeuristics);

            // Displaying output
            System.out.println("Source : " + sourceCity.name);
            System.out.println("Destination : " + destinationCity.name);
            System.out.println("Search algorithm : " + algoName);
            System.out.println("List of nodes in solution path: ");
            System.out.print("( ");
            List<City> path = AStarSearch.aStarSearch(sourceCity, destinationCity, roadInfoHashMap, cityWithHeuristics, algoName);
            System.out.println(" )");
            System.out.println("Total Nodes in the solution path : " + path.size());

            double cost = 0;
            for(City eachCity : path) {
                cost += eachCity.pathCost;
            }
            System.out.println("Total distance : " + cost + " miles");

        } else {
            System.out.println("Error : Source or Destination city not found !!");
        }

    }
}
