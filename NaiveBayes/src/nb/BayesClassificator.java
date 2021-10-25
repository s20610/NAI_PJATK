package nb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class BayesClassificator {
    private final List<List<String>>trainData;
    //dokladnosc eksperymentu
    double dokladnosc = 0;
    //zaklasyfikowane jako klasa x
    private double zaklasyfikowanychSetosa = 0;
    private double zaklasyfikowanychVirginica = 0;
    private double zaklasyfikowanychVersicolor = 0;
    //zaklasyfikowane poprawnie jako klasa x
    private double zaklasyfikowanychPoprawnieSetosa = 0;
    private double zaklasyfikowanychPoprawnieVirginica = 0;
    private double zaklasyfikowanychPoprawnieVersicolor = 0;
    //precyzja
    private double precyzjaSetosa = 0;
    private double precyzjaVersicolor = 0;
    private double precyzjaVirginica = 0;
    //pelnosc
    private double pelnoscSetosa = 0;
    private double pelnoscVersicolor = 0;
    private double pelnoscVirginica = 0;
    //fmiara
    private float FmiaraSetosa = 0;
    private float FmiaraVersicolor = 0;
    private float FmiaraVirginica = 0;
    //macierz omylek zaklasyfikowanaXnaprawdeY
    private double zaklasSetosatrVirginica = 0;
    private double zaklasSetosatrVersicolor = 0;
    private double zaklasVirginicatrSetosa = 0;
    private double zaklasVirginicatrVersicolor = 0;
    private double zaklasVersicolortrSetosa = 0;
    private double zaklasVersicolortrVirginica = 0;

    public BayesClassificator(String trainSet){
        trainData=loadSetFromFile(trainSet);
    }
    private static List<List<String>> loadSetFromFile(String filePath){
        List<List<String>> set=new ArrayList<>();
        try{
            BufferedReader bf=new BufferedReader(new FileReader(filePath));
            String line;
            while((line=bf.readLine())!=null){
                StringTokenizer tokenizer=new StringTokenizer(line," ");
                String token;
                List<String>attrs=new ArrayList<>();
                while(tokenizer.hasMoreTokens()){
                    token=tokenizer.nextToken();
                    attrs.add(token);
                }
                set.add(attrs);
            }
            bf.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return set;
    }
    void classifyFromFile(String filePath){
        List<List<String>>testData=loadSetFromFile(filePath);
        for(List<String>object:testData) {
            classify(object, false);
            //System.out.println();
        }
        dokladnosc = (zaklasyfikowanychPoprawnieSetosa+zaklasyfikowanychPoprawnieVirginica+zaklasyfikowanychPoprawnieVersicolor)*100/30;
        precyzjaSetosa = (zaklasyfikowanychPoprawnieSetosa/zaklasyfikowanychSetosa)*100;
        precyzjaVersicolor = (zaklasyfikowanychPoprawnieVersicolor/zaklasyfikowanychVersicolor)*100;
        precyzjaVirginica = (zaklasyfikowanychPoprawnieVirginica/zaklasyfikowanychVirginica)*100;
        pelnoscSetosa = (zaklasyfikowanychPoprawnieSetosa/zaklasyfikowanychSetosa)*100;
        pelnoscVersicolor = (zaklasyfikowanychPoprawnieVersicolor/zaklasyfikowanychVersicolor)*100;
        pelnoscVirginica = (zaklasyfikowanychPoprawnieVirginica/zaklasyfikowanychVirginica)*100;
        // Fmiara: 2*(RecallX*PrecisionX)/SUM(PrecisionX+RecallX)
        FmiaraSetosa = (float) (2*(pelnoscSetosa*precyzjaSetosa)/(precyzjaSetosa+pelnoscSetosa));
        FmiaraVersicolor = (float) (2*(pelnoscVersicolor*precyzjaVersicolor)/(precyzjaVersicolor+pelnoscVersicolor));
        FmiaraVirginica = (float) (2*(pelnoscVirginica*precyzjaVirginica)/(precyzjaVirginica+pelnoscVirginica));

        System.out.println("Dokładność eksperymentu: "+dokladnosc+"%");
        //dokładność eksperymentu, precyzję, pełność, F-miarę wyrażone w procentach
        System.out.println("Wynik eksperymentu");
        resultTable();
        System.out.println();
        System.out.println("Macierz omyłek");
        //macierz omylek
        matrixTable();
    }
    public void classify(List<String>attrs,boolean inputVector){
        //Szuka atrybutow decyzyjnych w danych treningowych
        List<DecisionalAttribute>decisionalAttributes=findDecisionalAttributes();
        //Dla kazdego atrybutu liczy prawdopodobienstwo
        //P(X|wiersz) = P(L1|X) · P(L2|X) · P(L3|X) · P(L4|X) · P(X)
        //P(Y|wiersz) = P(L1|Y) · P(L2|Y) · P(L3|Y) · P(L4|Y) · P(Y)
        //P(Z|wiersz) = P(L1|Z) · P(L2|Z) · P(L3|Z) · P(L4|Y) · P(Z)
        for(DecisionalAttribute decisionalAttribute:decisionalAttributes){
            double possibility = countPossibility(attrs,decisionalAttribute);
            decisionalAttribute.setPossibility(possibility);
        }
        //Wersja dla wpisywanego wektora
        if(inputVector){
            System.out.print("Attributes: ");
            for(String attr:attrs){
                System.out.print(attr+" ");
            }
            System.out.println("classified to: "+ findDecisionalAttributeWithHighestPossibility(decisionalAttributes));
        }

        switch (findDecisionalAttributeWithHighestPossibility(decisionalAttributes)) {
            case "Iris-setosa":
                zaklasyfikowanychSetosa++;
                if(attrs.get(attrs.size() - 1).equals("Iris-versicolor")){
                    zaklasSetosatrVersicolor++;
                }else if(attrs.get(attrs.size() - 1).equals("Iris-virginica")){
                    zaklasSetosatrVirginica++;
                }
                break;
            case "Iris-versicolor":
                zaklasyfikowanychVersicolor++;
                if(attrs.get(attrs.size() - 1).equals("Iris-setosa")){
                    zaklasVersicolortrSetosa++;
                }else if(attrs.get(attrs.size() - 1).equals("Iris-virginica")){
                    zaklasVersicolortrVirginica++;
                }
                break;
            case "Iris-virginica":
                zaklasyfikowanychVirginica++;
                if(attrs.get(attrs.size() - 1).equals("Iris-setosa")){
                    zaklasVirginicatrSetosa++;
                }else if(attrs.get(attrs.size() - 1).equals("Iris-versicolor")){
                    zaklasVirginicatrVersicolor++;
                }
                break;
        }

        if(findDecisionalAttributeWithHighestPossibility(decisionalAttributes).equals(attrs.get(attrs.size() - 1)) && findDecisionalAttributeWithHighestPossibility(decisionalAttributes).equals("Iris-setosa")){
            zaklasyfikowanychPoprawnieSetosa++;
        }else if (findDecisionalAttributeWithHighestPossibility(decisionalAttributes).equals(attrs.get(attrs.size() - 1)) && findDecisionalAttributeWithHighestPossibility(decisionalAttributes).equals("Iris-versicolor")){
            zaklasyfikowanychPoprawnieVersicolor++;
        }else if (findDecisionalAttributeWithHighestPossibility(decisionalAttributes).equals(attrs.get(attrs.size() - 1)) && findDecisionalAttributeWithHighestPossibility(decisionalAttributes).equals("Iris-virginica")){
            zaklasyfikowanychPoprawnieVirginica++;
        }
    }
    private List<DecisionalAttribute>findDecisionalAttributes(){
        List<DecisionalAttribute>appearedPossibilites=new ArrayList<>();
        for(List<String>object:trainData){
            boolean appeared=false;
            String value=object.get(trainData.get(0).size()-1);
            for(DecisionalAttribute appearedValue:appearedPossibilites){
                if (value.equals(appearedValue.getValue())) {
                    appeared = true;
                    break;
                }
            }
            if(!appeared){
                appearedPossibilites.add(new DecisionalAttribute(value));
            }
        }
        return appearedPossibilites;
    }
    private double countPossibility(List<String>attrs,DecisionalAttribute decisionalAttribute){
        //P(SETOSA) np 40/120
        double possibility=countAppearanceOfValue(decisionalAttribute.getValue(),trainData.get(0).size()-1,decisionalAttribute)/trainData.size();
        for (int i = 0; i < attrs.size()-1; i++) {
            //P(L1|SETOSA)=Ile razy wystapila taka wartosc atrybutu/ilosc przypadkow setosa,P(L2|SETOSA) itd...
            possibility*=(((countAppearanceOfValue(attrs.get(i),i,decisionalAttribute))+1)/(trainData.size()));
        }
        //P(SETOSA|WierszTestowy)
        return possibility;
    }
    private double countAppearanceOfValue(String value,int attributeNumber,DecisionalAttribute decisionalAttribute){
        //Liczy ilosc wystapien wartosci
        int counter=0;
        for(List<String>object:trainData){
            if(object.get(attributeNumber).equals(value)&&object.get(object.size()-1).equals(decisionalAttribute.getValue()))
                counter++;
        }
        return counter;
    }

    private String findDecisionalAttributeWithHighestPossibility(List<DecisionalAttribute>decisionalAttributes){
        //Znajduje wynikowy atrybut np Setosa
        DecisionalAttribute retDecisionalAttribute=decisionalAttributes.get(0);
        for(DecisionalAttribute decisionalAttribute:decisionalAttributes){
            if(!decisionalAttribute.getValue().equals(retDecisionalAttribute.getValue())){
                if(decisionalAttribute.getPossibility()>retDecisionalAttribute.getPossibility())
                    //Sprawdza, czy atrybut ktory jest wybrany jest wiekszy/mniejszy od innego w petli, ten ktory ma najwieksze zostanie wybrany
                    retDecisionalAttribute=decisionalAttribute;
            }
        }
        return retDecisionalAttribute.getValue();
    }

    public void resultTable() {
        //precyzję, pełność, F-miarę wyrażone w procentach
        String[][] table = new String[][] { { "Klasa", "Precyzja", "Pełność", "F-miara" },
                { "Iris-Setosa", precyzjaSetosa+"%", pelnoscSetosa+"%", FmiaraSetosa+"%" },
                { "Iris-Versicolor", precyzjaVersicolor+"%", pelnoscVersicolor+"%", FmiaraVersicolor+"%" },
                { "Iris-Virginica", precyzjaVirginica+"%", pelnoscVirginica+"%", FmiaraVirginica+"%" }};
        /*
         * Calculate appropriate Length of each column by looking at width of data in
         * each column.
         *
         * Map columnLengths is <column_number, column_length>
         */
        Map<Integer, Integer> columnLengths = new HashMap<>();
        Arrays.stream(table).forEach(a -> Stream.iterate(0, (i -> i < a.length), (i -> ++i)).forEach(i -> {
            columnLengths.putIfAbsent(i, 0);
            if (columnLengths.get(i) < a[i].length()) {
                columnLengths.put(i, a[i].length());
            }
        }));
        /*
         * Prepare format String
         */
        final StringBuilder formatString = new StringBuilder();
        String flag = "-";
        columnLengths.forEach((key, value) -> formatString.append("| %").append(flag).append(value).append("s "));
        formatString.append("|\n");
        /*
         * Print table
         */
        Stream.iterate(0, (i -> i < table.length), (i -> ++i))
                .forEach(a -> System.out.printf(formatString.toString(), table[a]));

    }
    public void matrixTable() {
        //macierz omylek
        String[][] table = new String[][] { { " ", "True Iris-Setosa", "True Iris-Versicolor", "True Iris-Virginica" },
                { "Predicted as Iris-Setosa", zaklasyfikowanychPoprawnieSetosa+"", zaklasSetosatrVersicolor+"",zaklasSetosatrVirginica+""  },
                { "Predicted as Iris-Versicolor", zaklasVersicolortrSetosa+"", zaklasyfikowanychPoprawnieVersicolor+"", zaklasVersicolortrVirginica+"" },
                { "Predicted as Iris-Virginica", zaklasVirginicatrSetosa+"", zaklasVirginicatrVersicolor+"", zaklasyfikowanychPoprawnieVirginica+"" }};
        /*
         * Calculate appropriate Length of each column by looking at width of data in
         * each column.
         *
         * Map columnLengths is <column_number, column_length>
         */
        Map<Integer, Integer> columnLengths = new HashMap<>();
        Arrays.stream(table).forEach(a -> Stream.iterate(0, (i -> i < a.length), (i -> ++i)).forEach(i -> {
            columnLengths.putIfAbsent(i, 0);
            if (columnLengths.get(i) < a[i].length()) {
                columnLengths.put(i, a[i].length());
            }
        }));
        /*
         * Prepare format String
         */
        final StringBuilder formatString = new StringBuilder();
        String flag = "-";
        columnLengths.forEach((key, value) -> formatString.append("| %").append(flag).append(value).append("s "));
        formatString.append("|\n");
        /*
         * Print table
         */
        Stream.iterate(0, (i -> i < table.length), (i -> ++i))
                .forEach(a -> System.out.printf(formatString.toString(), table[a]));

    }
}