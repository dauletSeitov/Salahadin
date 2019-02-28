package kz.slum.disappointment;

import org.omg.PortableInterceptor.INACTIVE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Kmeans {

        private int COUNT_OF_CLUSTERS = -1;
        private static final int MAX_ITERATION = 30;
        private static final String SOURCE_FILE = "/home/skynet/IdeaProjects/Salahadin/src/kz/slum/disappointment/data.txt";

        private int countOfAtributes;
        private Map<Integer, List<Double>> dataTable = new HashMap<>();
        private Map<Integer, List<Double>> currentCentres = new HashMap<>();
        private Map<Integer, List<Integer>> clusters = new HashMap<>();


        public static void main(String[] args) throws IOException {
                Kmeans kmeans = new Kmeans();
                kmeans.initData();

                DefineCentroids defineCentroids = new DefineCentroids(kmeans.dataTable);
                Set<Integer> centroids = defineCentroids.start();

                kmeans.start(centroids);
        }


        private void start(Set<Integer> centroids) throws IOException {

                countOfAtributes = dataTable.get(0).size();

                COUNT_OF_CLUSTERS = centroids.size();

                int index = 0;
                for (Integer centroidId : centroids){
                        List<Double> doubles = dataTable.get(centroidId);
                        currentCentres.put(index++, doubles);
                }

                for (int outer = 0; outer < MAX_ITERATION; outer++) {
                        for (int i = 0; i < COUNT_OF_CLUSTERS; i++) {
                                clusters.put(i, new ArrayList<>());
                        }

                        for(Integer dataTableId : dataTable.keySet()){ //0 - 60

                                List<Double> dataRow = dataTable.get(dataTableId);
                                List<Double> distances = new ArrayList<>();
                                for(Integer centerKey : currentCentres.keySet()){ //0 - 5

                                        List<Double> center = currentCentres.get(centerKey);
                                        distances.add(calculateDistance(center, dataRow));
                                }

                                Integer id = min(distances);

                                List<Integer> integers = clusters.get(id);
                                integers.add(dataTableId);

                        }

                        Map<Integer, List<Double>> newCentres = average(clusters);

                        boolean equals = equals(newCentres, currentCentres);
                        System.out.println("\n" + outer + ") iteration");
                        System.out.println("old centre: ");
                        print(currentCentres);
                        System.out.println("new centre: ");
                        print(newCentres);
                        System.out.println("-----------------------------------------------------------------------------------------");
                        if(equals){
                                break;
                        } else {
                                currentCentres = newCentres;
                        }
                }

                System.out.println("\ncluster: ");
                printf(clusters);

        }

        private void initData() throws IOException {
                BufferedReader br = new BufferedReader(new FileReader(SOURCE_FILE));

                String line = null;
                int i = 0;

                while ((line = br.readLine()) != null) {
                        String[] atributes = line.split(" ");
                        List<Double> row = Arrays.stream(atributes).map(Double::new).collect(Collectors.toList());
                        dataTable.put(i, row);
                        i++;
                }
                br.close();
        }

        private boolean equals(Map<Integer, List<Double>> newCentres, Map<Integer, List<Double>> oldCenters) {
                for (int i = 0; i < COUNT_OF_CLUSTERS; i++) {
                        boolean equals = equalsList(newCentres.get(i), oldCenters.get(i));
                        if(!equals){
                                return false;
                        }
                }
                return true;
        }

        private boolean equalsList(List<Double> first, List<Double> second) {
                for (int i = 0; i < first.size(); i++) {
                        if(!first.get(i).equals(second.get(i))){
                            return false;
                        }
                }
                return true;
        }

        private Double calculateDistance(List<Double> firstDot, List<Double> secondDot){

                Double sum = 0.0;
                for (int i = 0; i < firstDot.size(); i++) {
                        sum += Math.pow(firstDot.get(i)-secondDot.get(i), 2);
                }
                return Math.sqrt(sum);
        }

        private Integer min (List<Double> list){

                Double min = list.get(0);
                int minIndex = 0;

                for (int i = 1; i < list.size(); i++) {
                       if (min > list.get(i)){
                               min = list.get(i);
                               minIndex = i;
                       }
                }
                return minIndex;
        }

        private Map<Integer, List<Double>> average(Map<Integer, List<Integer>> clusters){

                Map<Integer, List<Double>> newCentres = new HashMap<>();

                for (Integer clusterKey : clusters.keySet()){
                        List<Integer> cluster = clusters.get(clusterKey);
                        List<Double> sums = new ArrayList<>(Collections.nCopies(countOfAtributes, 0.0));

                        for (Integer objectId : cluster){
                                List<Double> object = dataTable.get(objectId);
                                sum(sums, object);
                        }

                        for (int i = 0; i < countOfAtributes; i++) {
                                sums.set(i, sums.get(i)/cluster.size());
                        }

                        newCentres.put(clusterKey, sums);
                }

                return newCentres;

        }

        private void sum(List<Double> first, List<Double> second){

                for (int i = 0; i < first.size(); i++) {
                        first.set(i, first.get(i) + second.get(i));
                }
        }

        private void print(Map<Integer, List<Double>> currentCentres){

                for (int i = 0; i < COUNT_OF_CLUSTERS; i++) {
                        System.out.println(currentCentres.get(i));
                }

        }

        private void printf(Map<Integer, List<Integer>> clusters){

                for (int i = 0; i < COUNT_OF_CLUSTERS; i++) {
                        System.out.println(clusters.get(i));
                }

        }
}

