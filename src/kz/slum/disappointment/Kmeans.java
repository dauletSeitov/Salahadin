package kz.slum.disappointment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Kmeans {

        private int COUNT_OF_CLUSTERS = -1;
        private static final int MAX_ITERATION = 30;
        private static final String SOURCE_FILE = "/home/user/IdeaProjects/Salahadin/src/kz/slum/disappointment/data.txt";

        private int countOfAtributes;
        private Map<Integer, List<Double>> dataTable = new HashMap<>();
        private Map<Integer, List<Double>> currentCentres = new HashMap<>();
        private Map<Integer, List<Integer>> clusters = new HashMap<>();


        public Map<Integer, List<Double>> getDataTable() {
                return dataTable;
        }

        public Map<Integer, List<Integer>> getClusters() {
                return clusters;
        }

        public Map<Integer, List<Double>> getCurrentCentres() {
                return currentCentres;
        }

        public void start(Map<Integer, List<Double>> centroids){

                countOfAtributes = dataTable.get(0).size();

                COUNT_OF_CLUSTERS = centroids.size();

                currentCentres = centroids;

                clusters = new HashMap<>();

                start();

        }

        public void start(Set<Integer> centroids) {

                countOfAtributes = dataTable.get(0).size();

                COUNT_OF_CLUSTERS = centroids.size();

                currentCentres = new HashMap<>();

                int index = 0;
                for (Integer centroidId : centroids){
                        List<Double> doubles = dataTable.get(centroidId);
                        currentCentres.put(index++, doubles);
                }

                clusters = new HashMap<>();

                start();
        }


        public void start() {



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
                        print(currentCentres, COUNT_OF_CLUSTERS);
                        System.out.println("new centre: ");
                        print(newCentres, COUNT_OF_CLUSTERS);
                        System.out.println("-----------------------------------------------------------------------------------------");
                        if(equals){
                                break;
                        } else {
                                currentCentres = newCentres;
                        }
                }

                System.out.println("\ncluster: ");
                printf(clusters, COUNT_OF_CLUSTERS);

        }

        public void initData() throws IOException {
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

                LinkedList<Integer> newCentresKeySet = new LinkedList<>(newCentres.keySet());
                LinkedList<Integer> oldCentersKeySet = new LinkedList<>(oldCenters.keySet());

                newCentresKeySet.sort(Integer::compareTo);
                oldCentersKeySet.sort(Integer::compareTo);


                while(!newCentresKeySet.isEmpty()) {

                        boolean equals = equalsList(newCentres.get(newCentresKeySet.pollFirst()), oldCenters.get(oldCentersKeySet.peekFirst()));
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

        public static double calculateDistance(List<Double> firstDot, List<Double> secondDot){

                double sum = 0.0;
                for (int i = 0; i < firstDot.size(); i++) {
                        sum += Math.pow(firstDot.get(i)-secondDot.get(i), 2);
                }
                return Math.sqrt(sum);
        }

        public static Integer min (List<Double> list){

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

        public static void print(Map<Integer, List<Double>> currentCentres, int size){

                for (int i = 0; i < size; i++) {
                        System.out.println(currentCentres.get(i));
                }

        }

        public static void printf(Map<Integer, List<Integer>> clusters, int size){

                for (int i = 0; i < size; i++) {
                        System.out.println(clusters.get(i));
                }

        }
}

