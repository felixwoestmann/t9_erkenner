import crawler.CrawlerTree;
import crawler.ProbabilityCalculator;
import crawler.TreeReader;
import crawler.WikiDumpReader;
import org.junit.Test;
import t9.T9Keyboard;
import t9.T9Tree;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.lang.Integer.max;

/**
 * Created by lostincoding on 17.06.17.
 */
public class TestT9Tree {
    private double testErrorRate(int historysize, short pathcount) throws IOException {
        T9Tree tree = initTree(historysize, pathcount);
        LinkedList<String> words = loadWordFile("words.txt");

        double diff = 0;
        double countTotal = 0;

        for (String word : words) {
            //translate every string to button presses
            ArrayList<Character> buttons = T9Keyboard.mapStringToButtons(word);
            //then simulate that these buttons are pressed
            try {
                buttons.forEach(tree::processButton);
            } catch (NullPointerException ex) {
                //on error just skip the word
                tree.newWord();
                continue;
            }

            countTotal += word.length();
            //check if result matches
            diff += calcWordDifference(word, tree.getBestGuess());
            tree.newWord();
        }

        return diff / countTotal;
    }

    @Test
    public void testParseTree() throws IOException {
        T9Tree tree = initTree();
        TreeReader reader = new TreeReader();
        CrawlerTree parseTree = reader.getTreeFromFile("tree_5.json");
        ProbabilityCalculator c = new ProbabilityCalculator(parseTree);
        String s = "hallo";

        c.probabilityOfString(s);
        ArrayList<Character> buttons = T9Keyboard.mapStringToButtons(s);
        buttons.forEach(character -> {
            tree.processButton(character);
        });
        tree.printTree();
        System.out.format("\"%s\"\n", tree.getBestGuess());
    }

    @Test
    public void testBestKValue() throws IOException {
        T9Tree tree;
        TreeReader reader = new TreeReader();
        CrawlerTree parseTree = reader.getTreeFromFile("tree_5.json");
        ProbabilityCalculator c = new ProbabilityCalculator(parseTree);
        LinkedList<String> words = loadWordFile("words.txt");
        Map<Integer, Double> probabilites = new HashMap<>();

        System.out.println("+-----+------------+");
        System.out.println("|  k  | Error Rate |");
        System.out.println("+=====+============+");
        int[] array = {2, 4, 6, 8, 10, 15, 20, 50, 100};
        for (int i : array) { // for (int i = 2; i <= 100; i++) {
            tree = new T9Tree(new ProbabilityCalculator(parseTree), (short) i);
            double diff = 0;
            double countTotal = 0;

            for (String word : words) {
                tree.newWord();
                ArrayList<Character> buttons = T9Keyboard.mapStringToButtons(word);
                try {
                    buttons.forEach(tree::processButton);
                } catch (NullPointerException ex) {
                    continue;
                }

                countTotal += word.length();
                //check if result matches
                diff += calcWordDifference(word, tree.getBestGuess());
            }
            System.out.format("| %3d |   %5.2f%%   |\n", i, diff / countTotal * 100);
            probabilites.put(i, diff / countTotal);
        }
        System.out.println("+-----+------------+\n");

        Map.Entry<Integer, Double> min = null;
        for (Map.Entry<Integer, Double> entry: probabilites.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        System.out.format("Min: %4.2f%% at k=%d", (min != null ? min.getValue() : 0) * 100, min.getKey());
    }

    @Test
    public void testForPathCountAndHistorySize() throws IOException {
        short[] pathcounts = {5, 10, 50, 100};
        int[] historysizes = {3, 4, 5};

        System.out.format("Path | History | Error Rate\n-----+---------+-----------\n");
        for (short pathcount : pathcounts) {
            for (int historysize : historysizes) {
                System.out.format("%3d  |    %1d    | %.4f\n", pathcount, historysize, testErrorRate(historysize, pathcount));
            }
        }
    }

    private T9Tree initTree() throws IOException {
        return this.initTree((short) 2);
    }

    private T9Tree initTree(int historysize, short pathcount) throws IOException {
        TreeReader reader = new TreeReader();
        CrawlerTree parseTree = reader.getTreeFromFile(String.format("tree_%d.json", historysize));
        return new T9Tree(new ProbabilityCalculator(parseTree), pathcount);
    }

    private T9Tree initTree(short kPathCount) throws IOException {
        TreeReader reader = new TreeReader();
        CrawlerTree parseTree = reader.getTreeFromFile("tree_3.json");
        return new T9Tree(new ProbabilityCalculator(parseTree), kPathCount);
    }

    private int calcWordDifference(String one, String two) {
        int diff = 0;
        char[] oneArr = one.toCharArray();
        char[] twoArr = two.toCharArray();
        int length = max(oneArr.length, twoArr.length);

        for (int i = 0; i < length; i++) {
            if (i > oneArr.length - 1 || i > twoArr.length - 1) {
                diff++;
                continue;
            }
            if (oneArr[i] != twoArr[i]) {
                diff++;
            }
        }
        return diff;
    }


    private LinkedList<String> loadWordFile(String path) throws IOException {
        LinkedList<String> list = new LinkedList<>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        String line;
        while ((line = reader.readLine()) != null) {
            list.add(line);
        }
        return list;
    }

    private void writeWikiStringToFile(String wikidir, String out) throws FileNotFoundException {
        LinkedList<String> words = WikiDumpReader.getWords(wikidir);
        PrintWriter printWriter = new PrintWriter(out);
        words.forEach(printWriter::println);
        printWriter.close();
        System.out.println(words.size() + " Word were printed");
    }
}
