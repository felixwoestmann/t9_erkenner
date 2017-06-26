import crawler.CrawlerTree;
import crawler.ProbabilityCalculator;
import crawler.TreeReader;
import crawler.WikiDumpReader;
import org.junit.Test;
import t9.T9Keyboard;
import t9.T9Tree;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.max;

/**
 * Created by lostincoding on 17.06.17.
 */
public class TestT9Tree {
    private double testErrorRate(short historysize, short pathcount) throws IOException {
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
        T9Tree tree = initTree((short) 3, (short) 2);
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
        final class Container {
            private final short key;
            private final double error;

            /**
             * @param key index value
             * @param error the corresponding error
             */
            private Container(short key, double error) {
                this.key = key;
                this.error = error;
            }
        }

        T9Tree tree;
        TreeReader reader = new TreeReader();
        CrawlerTree parseTree = reader.getTreeFromFile("tree_5.json");
        LinkedList<String> words = loadWordFile("words.txt");
        List<Container> probabilities = new ArrayList<>();

        for (short i : new short[]{2, 4, 6, 8, 10, 15, 20, 50, 100}) {
//        for (short i = 2; i <= 100; i++) {
            tree = new T9Tree(new ProbabilityCalculator(parseTree), i);
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
            probabilities.add(new Container(i, diff / countTotal));
        }

        System.out.println("+-----+------------+");
        System.out.println("|  k  | Error Rate |");
        System.out.println("+=====+============+");
        for (Container c : probabilities) {
            System.out.format("| %3d |   %5.2f%%   |\n", c.key, c.error * 100);
        }
        System.out.println("+-----+------------+\n");

        probabilities.sort(Comparator.comparingDouble(p -> p.error));
        Container min = probabilities.get(0);
        System.out.format("Min: %4.2f%% at k=%d", min.error * 100, min.key);
    }

    @Test
    public void testForPathCountAndHistorySize() throws IOException {
        final class Container {
            private final int pathCount;
            private final int historysize;
            private final double errorRate;

            private Container(int pathCount, int historysize, double errorRate) {
                this.pathCount = pathCount;
                this.historysize = historysize;
                this.errorRate = errorRate;
            }
        }

        short[] pathcounts = {5, 10, 50, 100};
        short[] historysizes = {3, 4, 5};
        List<Container> errorRates = new ArrayList<>();

        for (short pathcount : pathcounts) {
            for (short historysize : historysizes) {
                errorRates.add(new Container(pathcount, historysize, testErrorRate(historysize, pathcount)));
            }
        }

        errorRates.sort(Comparator.comparingDouble(c -> c.errorRate));
        System.out.format("Path | History | Error Rate\n-----+---------+-----------\n");
        for (Container c : errorRates) {
            System.out.format("%3d  |    %1d    | %.4f\n", c.pathCount, c.historysize, c.errorRate);
        }
    }

    private T9Tree initTree(short historysize, short pathcount) throws IOException {
        TreeReader reader = new TreeReader();
        CrawlerTree parseTree = reader.getTreeFromFile(String.format("tree_%d.json", historysize));
        return new T9Tree(new ProbabilityCalculator(parseTree), pathcount);
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
