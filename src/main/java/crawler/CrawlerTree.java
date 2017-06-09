package crawler;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lostincoding on 09.05.17.
 */
public class CrawlerTree {

    private int chunkSize;
    private CrawlerNode<DataContainer> root = null;

    public CrawlerTree(int chunkSize) {
        this.root = new CrawlerNode<>(new DataContainer('X'));

        this.chunkSize = chunkSize;
    }

    public CrawlerTree(int chunkSize, CrawlerNode<DataContainer> root) {
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


        CrawlerNode<DataContainer> previous = root;

        for (int i = 0; i < chunk.length; i++) {
            ( root.getData()).incrementCount();
            CrawlerNode<DataContainer> workingon = previous.getChild(chunk[i]);

            if (workingon == null) {
                CrawlerNode<DataContainer> tmp = new CrawlerNode<>(new DataContainer(chunk[i]));
                previous.addChild(tmp);
                previous = tmp;
            } else {
                ((DataContainer) workingon.getData()).incrementCount();
                previous = workingon;
            }
        }
    }


    private ArrayList<CrawlerNode<DataContainer>> getLeafs(CrawlerNode<DataContainer> start) {
        ArrayList<CrawlerNode<DataContainer>> leafs = new ArrayList<>();

        if (start.isLeaf()) {
            leafs.add(start);
            return leafs;
        } else {
            for (CrawlerNode<DataContainer> n : start.getChildrenAsArrayList()) {
                leafs.addAll(getLeafs(n));
            }
        }
        return leafs;
    }


    public CrawlerNode<DataContainer> getRoot() {
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
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c == ' ') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static CrawlerTree loadFromFile(String path) {
        CrawlerTree readTree = null;
        TreeReader reader = new TreeReader();
        try {
            readTree = reader.getTreeFromFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readTree;
    }

    public boolean equals(CrawlerTree tree2) {
        return toString().equals(tree2.toString());
    }
}
