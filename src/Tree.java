import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lostincoding on 09.05.17.
 */
public class Tree {

    private int chunkSize;
    private Node root = null;

    public Tree(int chunkSize) {
        this.root = new Node(new DataContainer('X'));

        this.chunkSize = chunkSize;
    }

    public void processString(String input) {
        ArrayList<char[]> chunks = stringToChunkList(input);
        for (char[] array : chunks) {
            processChunk(array);
        }
    }

    private void processChunk(char[] chunk) {


        Node previous = root;

        for (int i = 0; i < chunk.length; i++) {
            root.getData().incrementCount();
            Node workingon = previous.getChild(chunk[i]);

            if (workingon == null) {
                Node tmp = new Node(new DataContainer(chunk[i]));
                previous.addChild(tmp);
                previous = tmp;
            } else {
                workingon.getData().incrementCount();
                previous = workingon;
            }
        }
    }


    private ArrayList<Node> getLeafs(Node start) {
        ArrayList<Node> leafs = new ArrayList<>();

        if (start.isLeaf()) {
            leafs.add(start);
            return leafs;
        } else {
            for (Node n : start.getChildren()) {
                leafs.addAll(getLeafs(n));
            }
        }


        return leafs;
    }



    public Node getRoot() {
        return root;
    }

    private ArrayList<char[]> stringToChunkList(String input) {
        ArrayList<char[]> chunks = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {

            String chunk = "";
            if (i + chunkSize <= input.length()) {
                chunk = input.substring(i, i + chunkSize);
            } else {
                int tmpchunksize = input.length() - i;
                chunk = input.substring(i, i + tmpchunksize);
            }
            chunks.add(chunk.toCharArray());
        }

        return chunks;
    }


    public void printTree() {
        root.print();
    }
}
