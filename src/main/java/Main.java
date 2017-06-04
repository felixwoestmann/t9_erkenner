import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by lostincoding on 09.05.17.
 */
public class Main {
    private static String treeLocationPath = "./tree.json";

    public static void main(String args[]) {

        CrawlerTree wikipediaTree = new CrawlerTree(5);
        WikiDumpReader.processWikiDump(wikipediaTree, "../wikidump");

        calculateProbOfWordInTree("Felix", wikipediaTree);
    }

    private static void calculateProbOfWordInTree(String searchString, CrawlerTree tree) {
        ProbabilityCalculator probabilityCalculator = new ProbabilityCalculator(tree);
        double probOfString = probabilityCalculator.probOfStringSmallerThanChunkSize(searchString.toLowerCase());

        System.out.format(
            "Die Wahrscheinlichkeit für %s in der deutschen Wikipedia ist %5f.\n",
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

    private static boolean areTreeIdentical(CrawlerTree tree1, CrawlerTree tree2) {
        return tree1.toString().equals(tree2.toString());
    }
}
