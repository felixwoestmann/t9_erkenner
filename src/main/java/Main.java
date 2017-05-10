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
    }
}
