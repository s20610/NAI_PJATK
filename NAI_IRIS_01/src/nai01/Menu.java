package nai01;

import java.util.Scanner;

public class Menu {

    public Menu(){
        Scanner sc = new Scanner(System.in);
        //System.out.println("Podaj ścieżkę pliku testowego: ");
        //String testFilePath = sc.nextLine();
        //System.out.println("Podaj ścieżkę pliku treningowego: ");
        //String trainingFilePath = sc.nextLine();
        String testFilePath = "iris_test.txt";
        String trainingFilePath = "iris_training.txt";
        KNN knn = new KNN();
        knn.readFile(testFilePath, "test");
        knn.readFile(trainingFilePath, "training");
        System.out.println("Wpisz liczbę k");
        knn.comparing();
        System.out.println("Czy chcesz wprowadzić nowy obiekt testowy? y/n");
        String answer = sc.nextLine();
        if(answer.equals("y")) {
            knn.newInstance();
            System.out.println("Czy chcesz wprowadzić następny obiekt testowy? y/n");
            String answer1 = sc.nextLine();
            while(answer1.equals("y")){
                knn.newInstance();
                System.out.println("Czy chcesz wprowadzić następny obiekt testowy? y/n");
                answer1 = sc.nextLine();
            }
        }
        System.out.println("Do widzenia :)");
    }
}
