package kpk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class BruteForce2 {
    private static int length;
    private static int capacity;
    static void findBestBasket(String path){
        Item[]items=readFromFile(path);
        System.out.println(findBestCombination(items));
    }
    private static Item[] readFromFile(String path){
        List<Item[]> datasets = new ArrayList<>();
        try{
            BufferedReader bf=new BufferedReader(new FileReader(path));
            String line=bf.readLine();
            length = Integer.parseInt(line.substring(9,11));
            capacity = Integer.parseInt(line.substring(22,24));
            for (int i=0;i<15;i++) {
                Item[] items = new Item[length];
                for (int j = 0; j < items.length; j++) {
                    items[j] = new Item();
                }
                line = bf.readLine();
                line = bf.readLine();
                StringTokenizer stringTokenizer = new StringTokenizer(line.substring(9,line.length()-1), ", ");
                int itemPositionCounter = 1;
                for (Item item : items) {
                    item.weight = Integer.parseInt(stringTokenizer.nextToken());
                }
                line = bf.readLine();
                stringTokenizer = new StringTokenizer(line.substring(8,line.length()-1), ", ");
                for (Item item : items) {
                    item.value = Integer.parseInt(stringTokenizer.nextToken());
                    item.position = itemPositionCounter;
                    itemPositionCounter++;
                }
                line = bf.readLine();
                datasets.add(items);
            }
            bf.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        Random random = new Random();
        int pickedDataset = random.nextInt(14);
        System.out.println("Picked dataset is "+(pickedDataset+1));
        return datasets.get(pickedDataset);
    }
    static String findBestCombination(Item[]items){
        int maxValueSum=0;
        int finalWeight=0;
        Item[] finalCombinationBasket= new Item[length];
        int numberOfCombinations = (int)Math.pow(2,items.length);
        //System.out.println(numberOfCombinations);
        for (int i = 1; i < numberOfCombinations; i++) {
            String combinationString = Integer.toBinaryString(i);
            StringBuilder formattedCombinationString= new StringBuilder();
            for (int j = combinationString.length()-1; j >=0 ; j--) {
                formattedCombinationString.append(combinationString.charAt(j));
            }
            formattedCombinationString.append("0".repeat(Math.max(0, length - combinationString.length())));
            char[]combination= formattedCombinationString.toString().toCharArray();
            int valueSum=0;
            int weightSum=0;
            Item[] combinationBasket= new Item[length];
            for (int j = 0; j < combination.length; j++) {
                if(combination[j]=='1'){
                    valueSum+=items[j].value;
                    weightSum+=items[j].weight;
                    combinationBasket[j]=items[j];
                }
            }
            if(weightSum<=capacity&&valueSum>maxValueSum){
                maxValueSum=valueSum;
                finalWeight=weightSum;
                finalCombinationBasket=combinationBasket;
            }
        }
        for (Item item : finalCombinationBasket){
            if(!(item == null)){
                System.out.println(item);
            }
        }
        System.out.println("Total value: "+maxValueSum);
        return "Total weight: "+finalWeight;
    }
}