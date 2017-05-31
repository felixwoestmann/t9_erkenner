import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by lostincoding on 09.05.17.
 */
public class Main {
    private static String persistingPath = "/home/lostincoding/git/asp_crawler/tree.json";

    public static void main(String args[]) {

        CrawlerTree tree = new CrawlerTree(5);
        WikiDumpReader corpusReader = new WikiDumpReader();

        System.out.println("Start processing wiki dump");
        long start = System.currentTimeMillis();
        corpusReader.processWikiDump(tree, "/home/lostincoding/Schreibtisch/wikidump-out/one");
        long end = System.currentTimeMillis();
        System.out.println("Processing wiki dump took " + milliSecondsToSecond(end - start) + " seconds");

        ProbabilityCalculator probabilityCalculator=new ProbabilityCalculator(tree);
        double probOfFelix=probabilityCalculator.probOfString("felix");

    System.out.format("Die Wahrscheinlichkeit für Felix in der deutschen Wikipedia ist %f.\n",probOfFelix);
    }

    private static void persistTree(CrawlerTree tree) {
        System.out.println("Persist Tree on HardDrive. Directory: " + persistingPath);
        TreeWriter writer = new TreeWriter(tree);
        try {
            writer.writeToFile(persistingPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Persisting finished.");
    }

    private static CrawlerTree loadTree() {
        CrawlerTree readTree = null;
        System.out.println("Read persisted tree.");
        TreeReader reader = new TreeReader();
        try {
            readTree = reader.getTreeFromFile(persistingPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Aufbau des Baums abgeschlossen");
        return readTree;
    }

    private static long milliSecondsToSecond(long millis) {
        return millis / 1000;
    }

    private static boolean areTreeIdentical(CrawlerTree tree1, CrawlerTree tree2) {
        return tree1.toString().equals(tree2.toString());
    }
}
