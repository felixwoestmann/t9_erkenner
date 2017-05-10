import sun.reflect.generics.tree.Tree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by lostincoding on 09.05.17.
 */
public class Main {

    public static void main(String args[]) {
        CrawlerTree tree = new CrawlerTree(3);
        tree.processString("all app");
        System.out.println("Aufbau des Baums abgeschlossen");
        tree.printTree();


        TreeWriter writer = new TreeWriter(tree);
        System.out.println(writer.createJSONFromTree());

        try {
            writer.writeToFile("/home/lostincoding/Schreibtisch/tree.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        TreeReader reader = new TreeReader();
        CrawlerTree readTree = null;
        try {
            readTree = reader.getTreeFromFile("/home/lostincoding/Schreibtisch/tree.json");
        } catch (IOException e) {
            e.printStackTrace();
        }


        readTree.printTree();

    }
}
