package general;

import crawler.*;
import gui.Window;
import t9.T9Tree;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

/**
 * Created by lostincoding on 09.05.17.
 */
public class Main {
    private static String treeLocationPath = "./tree.json";

    public static void main(String args[]) throws IOException {

        TreeReader reader = new TreeReader();
        CrawlerTree parseTree = reader.getTreeFromFile("tree_5.json");
        ProbabilityCalculator probabilityCalculator = new ProbabilityCalculator(parseTree);

        T9Tree inputTree = new T9Tree(probabilityCalculator, 2);
        String input = "42883";

        for (int i = 0; i < input.length(); i++) {
            inputTree.processButton(input.charAt(i));
            inputTree.printTree();
        }

        System.out.println("Finished everything");
    }

    private static void calculateProbOfWordInTree(String searchString, CrawlerTree tree) {
        ProbabilityCalculator probabilityCalculator = new ProbabilityCalculator(tree);
        double probOfString = probabilityCalculator.probOfStringShorterThanChunkSize(searchString.toLowerCase());

        System.out.format(
                "Die Wahrscheinlichkeit für %s im Korpus ist %5f.\n",
                searchString,
                probOfString
        );
    }

    private static void persistTree(CrawlerTree tree, String path) {
        System.out.println("Persist Tree on HardDrive. Directory: " + path);
        TreeWriter writer = new TreeWriter(tree);
        try {
            writer.writeToFile(path);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Persisting finished.");
    }

}
