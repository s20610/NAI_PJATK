package nb;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) {
        BayesClassificator bayesClassificator=new BayesClassificator("D:\\Projekty\\NaiveBayes\\src\\iris_training.txt");
        bayesClassificator.classifyFromFile("D:\\Projekty\\NaiveBayes\\src\\iris_test.txt");
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);
        while(loop){
            System.out.println("Czy chcesz wprowadzić nowe dane? Tak/Nie");
            String answer = scanner.nextLine();
            if(answer.equals("Tak")){
                List<String> attrs = new ArrayList<>();
                System.out.println("Proszę wprowadzić dane oddzielone spacją");
                String data = scanner.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(data," ");
                while (tokenizer.hasMoreTokens()){
                    attrs.add(tokenizer.nextToken());
                }
                bayesClassificator.classify(attrs,true);
            }else{
                loop = false;
            }
        }
    }
}