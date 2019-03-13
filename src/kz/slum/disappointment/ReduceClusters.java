package kz.slum.disappointment;

import java.io.IOException;
import java.util.*;

public class ReduceClusters {

    private Map<Integer, List<Double>> dataTable = new HashMap<>();

    private int reduceClustersTo = 2;

    public static void main(String[] args) throws IOException {
        new ReduceClusters().start();
    }


    private void start() throws IOException {


        Kmeans kmeans = new Kmeans();
        kmeans.initData();
        this.dataTable = kmeans.getDataTable();
        //List<Double> resultCentroids = new ArrayList<>();

        DefineCentroids defineCentroids = new DefineCentroids(kmeans.getDataTable());
        Set<Integer> firstCentroids = defineCentroids.start();


        System.out.println("\nПо алгоритму максимального растояния опеределили центры: " + firstCentroids);

        System.out.print("\nЗапускаем алгоритм ближайшего соседа для центров: " + firstCentroids);
        kmeans.start(firstCentroids);

        Map<Integer, List<Double>> prototype = kmeans.getCurrentCentres();


        while (prototype.size() > reduceClustersTo) {
            List<Double> resultCentroids = new ArrayList<>();
            for (Integer centerId : prototype.keySet()) {

                Map<Integer, List<Double>> centroids = new HashMap();
                centroids.putAll(prototype);
                centroids.remove(centerId);
                System.out.print("\nЗапускаем алгоритм ближайшего соседа для " + centroids.size() + " ти центров: " );
                kmeans.start(centroids);

                Map<Integer, List<Integer>> clusters = kmeans.getClusters();

                Double F = functionalInClusters(clusters);

                resultCentroids.add(F);

            }

            Integer minId = Kmeans.min(resultCentroids);
            prototype.remove(minId);

            System.out.println("----------> сократился кластер: " + minId);
        }
        kmeans.start(prototype);

    }



    private Double functionalInClusters(Map<Integer, List<Integer>> clusters) {

        double functionalCluster = 0.0;

        for (List<Integer> cluster : clusters.values()) {
            for ( Integer firstObjectId : cluster) {
                for (Integer secondObjectId: cluster) {
                    functionalCluster += Kmeans.calculateDistance(dataTable.get(firstObjectId), dataTable.get(secondObjectId));
                }
            }
        }
        return functionalCluster;
    }





}
