package nai02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Perceptron {

    public List<String> readedTrainingFile = new ArrayList<>();
    public List<Double> listOfWeights = new ArrayList<>();
    public List<String> readedTestFile = new ArrayList<>();
    public List<Double> listOfWeight = new ArrayList<>();
    double alpha = 8.0;
    int limit = 1;

    public Perceptron(){

    }



    public void readTrainingFile(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String textLine = bufferedReader.readLine();

        do {
//		    System.out.println(textLine);
            textLine = textLine.replace(",", ".");
            textLine = textLine.replace("\n", "");
            textLine = textLine.replace(" ", "");

            readedTrainingFile.add(textLine);

            textLine = bufferedReader.readLine();


        } while(textLine != null);

        bufferedReader.close();
        readedTrainingFile.spliterator();
        System.out.println(readedTrainingFile);
    }

    public void readTestFile(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String textLine = bufferedReader.readLine();

        do {
//		    System.out.println(textLine);
            textLine = textLine.replace("\n", "");
            textLine = textLine.replace(" ", "");
            textLine = textLine.replace(",", ".");
            readedTestFile.add(textLine);


            textLine = bufferedReader.readLine();


        } while(textLine != null);

        for(int i = 0; i<readedTestFile.size(); i++) {
            listOfWeights.add(i, 1.0);
//				System.out.println(listOfWeights.get(i));
        }
        bufferedReader.close();
        System.out.println(readedTestFile);
    }

    public void fillListOfWeight() {
        for(int i = 0; i<readedTrainingFile.size(); i++) {
            listOfWeight.add(i, Math.random());
//			System.out.println(listOfWeight.get(i));
        }
    }

    public void learn() {

        int count = 0;
        int success = 0;


        for(String s : readedTrainingFile) {

            float net = 0;
            String[] line = s.split("\\s+");
            double firstValue = Double.parseDouble(line[0]);
            double secondValue = Double.parseDouble(line[1]);
            double thirdValue = Double.parseDouble(line[2]);
            double fourthValue = Double.parseDouble(line[3]);
            String fifthValue = line[4];
//			System.out.println(firstValue + "  "+secondValue + "  "+thirdValue + "  "+fourthValue +"  "+ fifthValue);
            for(int i = 0; i<readedTrainingFile.size(); i++) {
                listOfWeight.add(i, Math.random());
//				System.out.println(listOfWeight.get(i));
            }

            double[] atrybut = new double[4];
            atrybut[0] = firstValue;
            atrybut[1] = secondValue;
            atrybut[2] = thirdValue;
            atrybut[3] = fourthValue;

            for(int i=0; i<4;i++) {
                net += atrybut[i]*listOfWeight.get(i);
            }
//			System.out.println("net value: " + net);
//			System.out.println(readedTrainingFile.size());



            while(success < readedTrainingFile.size()) {
                success = 0;
//				System.out.println(success);
                if(net > limit && !fifthValue.equals("Iris-versicolor")) {
                    for(int i=0; i<4;i++) {
//						System.out.println("blad1");
                        listOfWeight.set(i, listOfWeight.get(i) + (-1) * atrybut[i]);
//						System.out.println("net1 value: " + net);
                    }

                }else if(net < limit && fifthValue.equals("Iris-versicolor")) {
                    for(int i=0;i<4;i++) {
//						System.out.println("blad2");
                        listOfWeight.set(i, listOfWeight.get(i) + 1 * atrybut[i]);
//						System.out.println("net2 value: " + net);
                    }
                }else {
                    success = success+1;
//					System.out.println("tu blad");
                }
                count=count+1;
//				System.out.println("iteracja " + count + " success " + success);
                break;
            }
        }

    }

    public void test() {

        double succ = 0;
        double eff = 0 ;

        for(String s : readedTestFile) {

            float net = 0;
            String[] line = s.split("\\s+");
            double firstValue = Double.parseDouble(line[0]);
            double secondValue = Double.parseDouble(line[1]);
            double thirdValue = Double.parseDouble(line[2]);
            double fourthValue = Double.parseDouble(line[3]);
            String fifthValue = line[4];

            double[] atrybut = new double[4];
            atrybut[0] = firstValue;
            atrybut[1] = secondValue;
            atrybut[2] = thirdValue;
            atrybut[3] = fourthValue;

            for(int i=0; i<4;i++) {
                net += atrybut[i]*listOfWeights.get(i);
            }
//			System.out.println("net :" + net);
            if(net < alpha && fifthValue.equals("Iris-versicolor")) {
//				System.out.println("setosa");
                succ++;
            }else if(net >= alpha && !fifthValue.equals("Iris-versicolor")){
//				System.out.println("netVal dla nie setosy :-" + net);
                succ++;
            }


        }
        eff = (succ/readedTestFile.size())*100;
        System.out.println("Prawidłowo zaklasyfikowane: " + succ + ", skuteczność procentowa: " + eff + "%");



    }



    public void newTest() {
        // TODO Auto-generated method stub

        Scanner scan = new Scanner(System.in);
//		String answer = scan.nextLine();

        System.out.println("Prosze podac pierwszy atrybut");
        double a1 = scan.nextDouble();
        System.out.println("Prosze podac drugi atrybut");
        double a2 = scan.nextDouble();
        System.out.println("Prosze podac trzeci atrybut");
        double a3 = scan.nextDouble();
        System.out.println("Prosze podac czwarty atrybut");
        double a4 = scan.nextDouble();

        double[] atrybut = new double[4];
        float net = 0;

        atrybut[0] = a1;
        atrybut[1] = a2;
        atrybut[2] = a3;
        atrybut[3] = a4;

        for(int i = 0; i<atrybut.length; i++) {
            net += atrybut[i] * listOfWeights.get(i);
        }

        if(net < alpha) {
            System.out.println("To jest iris-versicolor");
        }else {
            System.out.println("To nie jest iris-versicolor");
        }


    }




}
