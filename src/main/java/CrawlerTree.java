
import java.util.ArrayList;

/**
 * Created by lostincoding on 09.05.17.
 */
public class CrawlerTree {

    private int chunkSize;
    private CrawlerNode root = null;

    public CrawlerTree(int chunkSize) {
        this.root = new CrawlerNode(new DataContainer('X'));

        this.chunkSize = chunkSize;
    }

    public CrawlerTree(int chunkSize, CrawlerNode root) {
        this.root = root;

        this.chunkSize = chunkSize;
    }

    public void processString(String input) {
        input = filterString(input);
        ArrayList<char[]> chunks = stringToChunkList(input);
        for (char[] array : chunks) {
            processChunk(array);
        }
    }

    private void processChunk(char[] chunk) {


        CrawlerNode previous = root;

        for (int i = 0; i < chunk.length; i++) {
            root.getData().incrementCount();
            CrawlerNode workingon = previous.getChild(chunk[i]);

            if (workingon == null) {
                CrawlerNode tmp = new CrawlerNode(new DataContainer(chunk[i]));
                previous.addChild(tmp);
                previous = tmp;
            } else {
                workingon.getData().incrementCount();
                previous = workingon;
            }
        }
    }


    private ArrayList<CrawlerNode> getLeafs(CrawlerNode start) {
        ArrayList<CrawlerNode> leafs = new ArrayList<CrawlerNode>();

        if (start.isLeaf()) {
            leafs.add(start);
            return leafs;
        } else {
            for (CrawlerNode n : start.getChildren()) {
                leafs.addAll(getLeafs(n));
            }
        }


        return leafs;
    }


    public CrawlerNode getRoot() {
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

    public String toString() {
        return root.getStringRepresentation();
    }


    public int getChunkSize() {
        return chunkSize;
    }

    public String filterString(String input) {
        input = input.toLowerCase();
        char[] chararray = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : chararray) {
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c==' ') {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public   boolean equals(CrawlerTree tree2) {
        return toString().equals(tree2.toString());
    }

}
