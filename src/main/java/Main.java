import crawler.CrawlerTree;
import crawler.ProbabilityCalculator;
import crawler.TreeReader;
import crawler.TreeWriter;
import t9.T9_Tree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by lostincoding on 09.05.17.
 */
public class Main {
    private static String treeLocationPath = "./tree.json";

    public static void main(String args[]) throws IOException {
//        crawler.CrawlerTree wikipediaTree = new crawler.CrawlerTree(5);
//        crawler.WikiDumpReader.processWikiDump(wikipediaTree, "../wikidump");
//
//        persistTree(wikipediaTree, treeLocationPath);
//        calculateProbOfWordInTree("Felix", wikipediaTree);

        TreeReader reader = new TreeReader();
        CrawlerTree parseTree = reader.getTreeFromFile("tree_3.json");
        ProbabilityCalculator c = new ProbabilityCalculator(parseTree);

        T9_Tree inputTree = new T9_Tree();
        String input = "42556";

        for (int i = 0; i < input.length(); i++) {
            System.out.println("i: " + (i+1));
            inputTree.processButton(input.charAt(i));
        }
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
