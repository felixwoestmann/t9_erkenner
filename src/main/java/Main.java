import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by lostincoding on 09.05.17.
 */
public class Main {
    private static String persistingPath = "./tree.json";

    public static void main(String args[]) {

        CrawlerTree wikipediaTree = new CrawlerTree(5);
        WikiDumpReader corpusReader = new WikiDumpReader();
        corpusReader.processWikiDump(wikipediaTree, "/home/lostincoding/Schreibtisch/wikidump-out/one");
        persistTree(wikipediaTree, "/home/lostincoding/Schreibtisch/tree_tree.json");
        //    calculateProbOfWordInTree("Felix", wikipediaTree);
    }

    private static void calculateProbOfWordInTree(String searchString, CrawlerTree tree) {
        ProbabilityCalculator probabilityCalculator = new ProbabilityCalculator(tree);
        double probOfString = probabilityCalculator.probOfString(searchString.toLowerCase());

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

    private static CrawlerTree loadTree(String path) {
        CrawlerTree readTree = null;
        System.out.println("Read persisted tree.");
        TreeReader reader = new TreeReader();
        try {
            readTree = reader.getTreeFromFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Aufbau des Baums abgeschlossen");
        return readTree;
    }


}
