import crawler.CrawlerTree;
import crawler.ProbabilityCalculator;
import crawler.TreeReader;
import org.junit.Test;
import t9.T9Keyboard;
import t9.T9Tree;
import utility.TimeUnit;
import utility.Timer;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;

import static java.lang.Integer.max;

/**
 * Created by lostincoding on 17.06.17.
 */
public class TestT9Tree {
    @Test
    public void testErrorRate() throws IOException {
        T9Tree tree = initTree();

        LinkedList<String> words = new LinkedList<>();
        File wordfile = new File("30k-words.txt");
        BufferedReader br = new BufferedReader(new FileReader(wordfile));
        String line;
        while ((line = br.readLine()) != null) {
            words.add(line.toLowerCase());
        }

        double countTotal = 0;
        double countSuccess = 0;
        double countFailure = 0;

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

            countTotal++;
            //check if result matches
            if (word.equals(tree.getBestGuess())) {
                countSuccess++;
            } else {
                countFailure++;
            }
            tree.newWord();
        }

        System.out.format("Processed: %d\nSuccess: %d\nFailure: %d\n\n\n", (int) countTotal, (int) countSuccess, (int) countFailure);
        System.out.format("Sucess Rate: %f\nFailure Rate: %f\n", countSuccess / countTotal, countFailure / countTotal);
    }

    private T9Tree initTree() throws IOException {
        TreeReader reader = new TreeReader();
        CrawlerTree parseTree = reader.getTreeFromFile("tree_5.json");
        return new T9Tree(new ProbabilityCalculator(parseTree), 2);
    }

    private int calcWordDifference(String one, String two) {
        int diff = 0;
        char[] oneArr = one.toCharArray();
        char[] twoArr = two.toCharArray();
        int length = max(oneArr.length, twoArr.length);
        for (int i = 0; i < length; i++) {
            if (i > oneArr.length || i > twoArr.length) {
                diff++;
                continue;
            }
            if (oneArr[i] != twoArr[i]) {
                diff++;
            }
        }
        return diff;
    }

}
