package kz.slum.disappointment;

import java.util.*;

public class DefineCentroids {
    public DefineCentroids(Map<Integer, List<Double>> dataTable) {
        this.dataTable = dataTable;
    }

    private Map<Integer, List<Double>> dataTable = new HashMap<>();
    private Set<Integer> centroids = new TreeSet<>();
    private List<Double> maxCentroidDistances = new ArrayList<>();


    public Set<Integer> start() {

        List<Double> firstCentroidsId = dataTable.get(0);
        ObjectMinDistance maxDistantIndex = getMaxDistantIndex(firstCentroidsId);
        centroids.add(0);
        centroids.add(maxDistantIndex.id);
        maxCentroidDistances.add(maxDistantIndex.minDistance);

        boolean res = true;
        for (int i = 0; i < dataTable.size() && res; i++) {
            res = getAllDistancesBetweenObjectsAndCentroids();
        }
        
        return centroids;
        
    } 
    
    
    private boolean getAllDistancesBetweenObjectsAndCentroids() {

        List<ObjectMinDistance> minDistances = new ArrayList<>();
        
        for(Integer itmKey : dataTable.keySet()) {
            
           if(centroids.contains(itmKey)) {
               continue;
           }

           Double innerMin = Double.MAX_VALUE;
           for(Integer centroidId: centroids) {
               Double centroidItem = Kmeans.calculateDistance(dataTable.get(centroidId), dataTable.get(itmKey));
               if(centroidItem < innerMin)
                   innerMin = centroidItem;
           }
                      
            minDistances.add(new ObjectMinDistance(itmKey, innerMin));
            
        }


        ObjectMinDistance objectMinDistance = minDistances.stream().max(Comparator.comparing(itm -> itm.minDistance)).orElseThrow(NullPointerException::new);

        if( average()/2 < objectMinDistance.minDistance){
            maxCentroidDistances.add(objectMinDistance.minDistance);
            centroids.add(objectMinDistance.id);
        } else {
            System.out.println("centroids: " + centroids);
            return false;
        }
        return true;
    }

    private Double average (){

        return maxCentroidDistances.stream().mapToDouble(itm -> itm).average().orElseThrow(NullPointerException::new);
    }

    
    private ObjectMinDistance getMaxDistantIndex(List<Double>  firtCentroid){

        Double maxDistance = 0.0;
        int maxIndex = -1;
        for (Integer itmKey : dataTable.keySet()) {
            Double distance = Kmeans.calculateDistance(firtCentroid, dataTable.get(itmKey));
            
            if(maxDistance < distance){
                maxDistance = distance;
                maxIndex = itmKey;
            }

        }
        return new ObjectMinDistance(maxIndex, maxDistance);
        
    }

}


class ObjectMinDistance {
    public ObjectMinDistance(Integer id, Double minDistance) {
        this.id = id;
        this.minDistance = minDistance;
    }

    Integer id;
    Double minDistance;

}
