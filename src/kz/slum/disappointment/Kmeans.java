package kz.slum.disappointment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Kmeans {

        private static final int COUNT_OF_CLUSTERS = 5;
        private static final int MAX_ITERATION = 30;
        private static final String SOURCE_FILE = "/home/sdfsds.txt";

        private int countOfAtributes;
        private Map<Integer, List<Double>> dataTable = new HashMap<>();
        private Map<Integer, List<Double>> currentCentres = new HashMap<>();
        private Map<Integer, List<Integer>> clusters = new HashMap<>();


        public static void main(String[] args) throws IOException {
                new Kmeans().start();
        }


        private void start() throws IOException {

                initData();
                int dataSize = dataTable.size();
                countOfAtributes = dataTable.get(0).size();
                Random random = new Random();
                for (int i = 0; i < COUNT_OF_CLUSTERS; i++){
                        List<Double> doubles = dataTable.get(random.nextInt(dataSize));
                        currentCentres.put(i, doubles);
                }

                System.out.println(currentCentres);

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
                        System.out.println(outer + " ) iteration");
                        System.out.println("old centre: " + currentCentres);
                        System.out.println("new centre: " + newCentres);
                        if(equals){
                                break;
                        } else {
                                currentCentres = newCentres;
                        }
                }

                System.out.println("cluster: " + clusters);



        }

        private void initData() throws IOException {
                BufferedReader br = new BufferedReader(new FileReader(SOURCE_FILE));

                String line = null;
                int i = 0;

                while ((line = br.readLine()) == null) {
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

                int i = 1;
                for (; i < list.size(); i++) {
                       if (min > list.get(i)){
                               min = list.get(i);
                       }
                }
                return i;
        }

        private Map<Integer, List<Double>> average(Map<Integer, List<Integer>> clusters){

                Map<Integer, List<Double>> newCentres = new HashMap<>();

                for (Integer clusterKey : clusters.keySet()){
                        List<Integer> cluster = clusters.get(clusterKey);
                        List<Double> sums = new ArrayList<>(countOfAtributes);
                        for (int i = 0; i < countOfAtributes; i++) {
                                sums.add(0.0);
                        }
                        for (Integer objectId : cluster){
                                List<Double> object = dataTable.get(objectId);
                                sum(sums, object);
                        }

                        for (int i = 0; i < countOfAtributes; i++) {
                                sums.add(i, sums.get(i)/cluster.size());
                        }

                        newCentres.put(clusterKey, sums);
                }

                return newCentres;

        }

        private void sum(List<Double> first, List<Double> second){

                for (int i = 0; i < first.size(); i++) {
                        first.add(i, first.get(i) + second.get(i));
                }
        }
}

