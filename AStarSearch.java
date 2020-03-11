package com.search;



import java.util.*;

public class AStarSearch {
    public static List<City> aStarSearch(City sourceCity, City destinationCity, LinkedHashMap<String, List<AdjacentNode>> roadInfoHashMap, LinkedHashMap<String, City> cityWithHeuristics, String algoName) {
        LinkedList<City> pathToDestination = new LinkedList<>();


        City currentlySelectedNode = sourceCity;
        currentlySelectedNode.pathCost = currentlySelectedNode.heuristicCost + 0; // in case of source node distanceCost is 0
        pathToDestination.add(currentlySelectedNode);

        System.out.print(currentlySelectedNode.name);
        while (!currentlySelectedNode.equals(destinationCity)) {


            // Expand the current node and select the min total cost node.
            List<AdjacentNode> listOfAdjacentNodes = roadInfoHashMap.get(currentlySelectedNode.name);
            if (listOfAdjacentNodes.size() == 0) {

                if (pathToDestination.size() > 0){
                    pathToDestination.remove(currentlySelectedNode);
                    currentlySelectedNode = pathToDestination.getLast();
                } else{
                    System.out.println("No Optimal path found !!");
                    break;
                }
            } else {

                if (listOfAdjacentNodes != null) {
                    AdjacentNode minCostNode;
                    if (algoName.equals("astar")) {
                        minCostNode = Collections.min(listOfAdjacentNodes, (s1, s2) -> {
                            return Double.compare(s1.totalCost, s2.totalCost);
                        });
                    } else if (algoName.equals("greedy")) {
                        minCostNode = Collections.min(listOfAdjacentNodes, (s1, s2) -> {
                            return Double.compare(s1.heuristicCost, s2.heuristicCost);
                        });
                    } else if (algoName.equals("dynamic")) {
                        minCostNode = Collections.min(listOfAdjacentNodes, (s1, s2) -> {
                            return Double.compare(s1.distanceCost, s2.distanceCost);
                        });
                    } else {
                        minCostNode = null;
                        System.out.println("Error : Algo Name does not match; it should one of following : astar, greedy, dynamic");
                        return pathToDestination;
                    }

                    if (minCostNode != null) {
                        //add city to path
                        cityWithHeuristics.get(minCostNode.cityName).pathCost = minCostNode.totalCost;
                        pathToDestination.add(cityWithHeuristics.get(minCostNode.cityName));

                        //remove that node from adjacent nodes in roadInfoHashMap
                        roadInfoHashMap = removeFunc(currentlySelectedNode.name, minCostNode.cityName, roadInfoHashMap);
                        roadInfoHashMap = removeFunc(minCostNode.cityName, currentlySelectedNode.name, roadInfoHashMap);

                        currentlySelectedNode = cityWithHeuristics.get(minCostNode.cityName);
                        System.out.print( ", "+currentlySelectedNode.name);
                    } else {
                        System.out.println("Error : No Min cost Node found");
                    }

                } else {
                    System.out.println("Error : No adjacent nodes found !!");
                }
            }

        }
        return  pathToDestination;
    }


    public static LinkedHashMap<String, List<AdjacentNode>> removeFunc(String searchInHashMap, String deleteFromList, LinkedHashMap<String, List<AdjacentNode>> roadInfoHashMap) {
        List<AdjacentNode> allLists = roadInfoHashMap.get(searchInHashMap);
        AdjacentNode nodeToRemove = null;
        for(AdjacentNode an : allLists) {
            if (an.cityName.equals(deleteFromList)){
                nodeToRemove = an;
                break;
            }
        }

        if (nodeToRemove != null) {
            roadInfoHashMap.get(searchInHashMap).remove(nodeToRemove);
        } else {
            System.out.println("Error : in removing node - "+deleteFromList);
        }
        return roadInfoHashMap;
    }

    public boolean checkIfGraphNodesExist(LinkedHashMap<String, List<AdjacentNode>> allNodes) {
        int zeroLengthAdjacentNodes = 0;
        for(List<AdjacentNode> eachnode : allNodes.values()) {
            if(eachnode.size() == 0)
                zeroLengthAdjacentNodes += 1;
        }
        if(zeroLengthAdjacentNodes == allNodes.size()) {
            return false;
        }
        return true;
    }
}
