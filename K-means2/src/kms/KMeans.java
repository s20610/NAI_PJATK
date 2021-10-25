package kms;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class KMeans {
    static final Double PRECISION = 0.0;
    static LinkedList<HashMap<String, Double>> initialCentroids(DataSet data, int K){
        LinkedList<HashMap<String,Double>> centroids = new LinkedList<>();
        for (int i = 0; i < K; i++) {
            centroids.add(data.records.get(i).getRecord());
        }
        for(int i=1; i<K; i++){
            centroids.add(data.calculateWeighedCentroid());
        }
        return centroids;
    }

    static void kmeans(DataSet data, int K){
        // select K initial centroids
        LinkedList<HashMap<String,Double>> centroids = initialCentroids(data, K);

        // initialize Sum of Squared Errors
        Double SSE = Double.MAX_VALUE;
        int iterationsCounter = 0;
        while (true) {

            // assign observations to centroids
            var records = data.getRecords();

            // for each record
            for(var record : records){
                Double minDist = Double.MAX_VALUE;
                // find the centroid at a minimum distance from it and add the record to its cluster
                for(int i=0; i<centroids.size(); i++){
                    Double dist = DataSet.euclideanDistance(centroids.get(i), record.getRecord());
                    if(dist<minDist){
                        minDist = dist;
                        record.setClusterNo(i);
                    }
                }

            }

            // recompute centroids according to new cluster assignments
            centroids = data.recomputeCentroids(K);

            // exit condition, SSE changed less than PRECISION parameter
            Double newSSE = data.calculateTotalSSE(centroids);
            iterationsCounter++;
            System.out.println("Suma kwadratów odległości: "+newSSE+" Iteracja: "+iterationsCounter);
            if(SSE-newSSE <= PRECISION){
                break;
            }
            SSE = newSSE;
        }
    }

    public static void main(String[] args) {
        try {
            // read data
            DataSet data = new DataSet("D:\\Projekty\\K-means2\\src\\training.txt");
            // remove prior classification attr if it exists (input any irrelevant attributes)
            data.removeAttr("Name");
            Scanner sc = new Scanner(System.in);
            System.out.println("Proszę o podanie k");
            int k = sc.nextInt();
            // cluster
            kmeans(data, k);

            // output into a csv
            data.createCsvOutput("D:\\Projekty\\K-means2\\src\\trainingClustered.txt");

        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
