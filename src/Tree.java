import java.util.ArrayList;

/**
 * Created by lostincoding on 09.05.17.
 */
public class Tree {
    private ArrayList<Node<String>> leafs = null;

    private Node<String> root = null;

    public Tree() {
        root = new Node<String>("root");
        leafs = getLeafs(root);
    }

   public

    private ArrayList<Node<String>> getLeafs(Node<String> start) {
        ArrayList<Node<String>> leafs = new ArrayList<>();

        if (start.isLeaf()) {
            leafs.add(start);
            return leafs;
        } else {
            for (Node<String> n : start.getChildren()) {
                leafs.addAll(getLeafs(n));
            }
        }


        return leafs;
    }

    private void updateLeafs() {
        ArrayList<Node<String>> tmplist = new ArrayList<>();

        for (Node<String> n : leafs) {
            tmplist.addAll(getLeafs(n));
        }

        leafs=tmplist;
    }


    public Node<String> getRoot() {
        return root;
    }


    public ArrayList<Node<String>> getLeafs() {
        return leafs;
    }


}
