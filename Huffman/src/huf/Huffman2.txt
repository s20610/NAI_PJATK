package huf;

import java.util.*;
class Node
{
    Character ch;
    Integer freq;
    Node left = null, right = null;

    Node(Character ch, Integer freq)
    {
        this.ch = ch;
        this.freq = freq;
    }

    public Node(Character ch, Integer freq, Node left, Node right)
    {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}

class Huffman2
{
    public static void encode(Node root, String str,
                              Map<Character, String> huffmanCode)
    {
        if (root == null) {
            return;
        }

        if (isLeaf(root)) {
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");
        }

        encode(root.left, str + '0', huffmanCode);
        encode(root.right, str + '1', huffmanCode);
    }

    public static boolean isLeaf(Node root) {
        return root.left == null && root.right == null;
    }

    public static void buildHuffmanTree(String text)
    {
        if (text == null || text.length() == 0) {
            return;
        }

        Map<Character, Integer> freq = new HashMap<>();
        for (char c: text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        int freqSum = freq.values()
                .stream()
                .mapToInt(i -> i)
                .sum();

        PriorityQueue<Node> pq;
        pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));

        for (var entry: freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() != 1)
        {

            Node left = pq.poll();
            Node right = pq.poll();

            int sum = left.freq + right.freq;
            pq.add(new Node(null, sum, left, right));
        }

        Node root = pq.peek();

        Map<Character, String> huffmanCode = new TreeMap<>();
        encode(root, "", huffmanCode);

        System.out.println("Char  | Huffman code ");
        huffmanCode.forEach((k, v) -> System.out.println("\""+k+"\""+"   |  "+"\""+v+"\""));
        System.out.println("--------------------");
        StringBuilder sb = new StringBuilder();
        for (char c: text.toCharArray()) {
            sb.append(huffmanCode.get(c));
        }
        int codedBytesNeeded = sb.length()+(huffmanCode.keySet().size()*8)+freqSum;
        System.out.println("The encoded string is: " + sb);
        //Print bytes needed to code including character coding, frequency sum and sizes sum
        //System.out.println("Ilość bitów potrzebna do zakodowania po kompresji: " +codedBytesNeeded);
        System.out.println("Ilość bitów potrzebna do zakodowania po kompresji: " +sb.length());

        if (isLeaf(root))
        {
            while (root.freq-- > 0) {
                System.out.print(root.ch);
            }
        }
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Proszę o wprowadzenie ciągu znaków: ");
        String text = sc.nextLine();
        System.out.println("Ilość bitów potrzebna do zakodowania:"+text.length()*8);
        buildHuffmanTree(text);
    }
}
