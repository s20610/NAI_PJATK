package nai02;

import java.io.IOException;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Perceptron testFile = new Perceptron();
        String testFilePath = "D:\\Projekty\\NAI_IRIS_02\\iris_test.txt";
        String trainingFilePath = "D:\\Projekty\\NAI_IRIS_02\\iris_training.txt";

        try {
            testFile.readTestFile(testFilePath);
            testFile.readTrainingFile(trainingFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        testFile.fillListOfWeight();
        testFile.learn();
        testFile.test();
//		System.out.println(testFile.listOfWeight.get(0) + " " + testFile.listOfWeight.get(1) + " " + testFile.listOfWeight.get(2) + " " + testFile.listOfWeight.get(3)+ " ");
        System.out.println("Czy chcesz podać nowy obiekt do sprawdzenia klasyfikacji?");
        Scanner scanik = new Scanner(System.in);
        String answer = scanik.nextLine();

        if(answer.equals("tak")) {
            testFile.newTest();
            System.out.println("Czy chcesz podać nastepny obiekt do sprawdzenia?");
            String answer1 = scanik.nextLine();

            while(answer1.equals("tak")){
                testFile.newTest();
                System.out.println("Czy chcesz podać nastepny obiekt do sprawdzenia?");
                answer1 = scanik.nextLine();
            }
        }
        System.out.println("Dziekuje za skorzystanie z programu");
    }
}
