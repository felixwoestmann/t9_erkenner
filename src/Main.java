import java.util.ArrayList;

/**
 * Created by lostincoding on 09.05.17.
 */
public class Main {

    public static void main(String args[]) {
        Tree tree = new Tree(3);
        tree.processString("Hallo-Welt");
        System.out.println("Aufbau des Baums abgeschlossen");
        tree.printTree();
    }
}
