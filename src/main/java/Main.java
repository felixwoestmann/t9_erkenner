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

        CrawlerTree tree = new CrawlerTree(3);
        CorpusReader corpusReader=new CorpusReader();

        System.out.println("Start processing wiki dump");
        long start=System.currentTimeMillis();
        corpusReader.processWikiDump(tree,"/home/lostincoding/Schreibtisch/wikidump-out");
        long end=System.currentTimeMillis();
        System.out.println("Processing wiki dump took "+milliSecondsToSecond(end-start)+" seconds");

       // tree.printTree();

        String persistingPath="/home/lostincoding/git/asp_crawler/tree.json";
        System.out.println("Persist Tree on HardDrive. Directory: "+persistingPath);
        TreeWriter writer = new TreeWriter(tree);
        try {
            writer.writeToFile(persistingPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Persisting finished.");
       /* try {
            tree.processString(FileReader.readFile("/home/lostincoding/Schreibtisch/lorem_ipsum_txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Aufbau des Baums abgeschlossen");
        System.out.println("Baum:\n\n");
        tree.printTree();


        TreeWriter writer = new TreeWriter(tree);
        System.out.println("JSON:\n\n");
        System.out.println(writer.createJSONFromTree());

        try {
            writer.writeToFile("/home/lostincoding/Schreibtisch/tree.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("Gelesener Baum:\n\n");
        TreeReader reader = new TreeReader();
        CrawlerTree readTree = null;
        try {
            readTree = reader.getTreeFromFile("/home/lostincoding/Schreibtisch/tree.json");
        } catch (IOException e) {
            e.printStackTrace();
        }


        readTree.printTree();*/

    }

    private static long milliSecondsToSecond(long millis) {
        return millis/1000;
    }

}
