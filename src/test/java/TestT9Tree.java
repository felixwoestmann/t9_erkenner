import crawler.CrawlerTree;
import crawler.ProbabilityCalculator;
import crawler.TreeReader;
import crawler.WikiDumpReader;
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
        LinkedList<String> words=loadWordFile("words.txt");

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

            countTotal+=word.length();
            //check if result matches
            diff += calcWordDifference(word, tree.getBestGuess());
            tree.newWord();
        }

        System.out.format("Error Rate: %f", diff / countTotal);
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


    private LinkedList<String > loadWordFile(String path) throws IOException {
        LinkedList<String> list=new LinkedList<>();

        BufferedReader reader=new BufferedReader(new FileReader(new File(path)));
        String line;
        while ((line = reader.readLine()) != null) {
            list.add(line);
        }
        return list;
    }
    private void writeWikiStringToFile(String wikidir,String out) throws FileNotFoundException {
        LinkedList<String> words=WikiDumpReader.getWords(wikidir);
        PrintWriter printWriter=new PrintWriter(out);
        words.forEach(printWriter::println);
        printWriter.close();
        System.out.println(words.size()+" Word were printed");
    }
}
