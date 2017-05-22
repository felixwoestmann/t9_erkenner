import sun.reflect.generics.tree.Tree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by lostincoding on 09.05.17.
 */
public class Main {

    public static void main(String args[]) {

        CrawlerTree writeTree = new CrawlerTree(3);
        CorpusReader corpusReader = new CorpusReader();

        System.out.println("Start processing wiki dump");
        long start = System.currentTimeMillis();
        corpusReader.processWikiDump(writeTree, "/home/lostincoding/Schreibtisch/wikidump-out");
        long end = System.currentTimeMillis();
        System.out.println("Processing wiki dump took " + milliSecondsToSecond(end - start) + " seconds");

        // tree.printTree();

        String persistingPath = "/home/lostincoding/git/asp_crawler/tree.json";
        System.out.println("Persist Tree on HardDrive. Directory: " + persistingPath);
        TreeWriter writer = new TreeWriter(writeTree);
        try {
            writer.writeToFile(persistingPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Persisting finished.");

        CrawlerTree readTree = null;
        System.out.println("Read persisted tree.");
        TreeReader reader = new TreeReader();
        try {
            readTree = reader.getTreeFromFile(persistingPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Aufbau des Baums abgeschlossen");
        System.out.println("Baum:\n\n");
        System.out.println("Sind die beiden Bäume identisch? " + areTreeIdentical(writeTree, readTree));

    }

    private static long milliSecondsToSecond(long millis) {
        return millis / 1000;
    }

    private static boolean areTreeIdentical(CrawlerTree tree1, CrawlerTree tree2) {
        return tree1.toString().equals(tree2.toString());
    }
}
