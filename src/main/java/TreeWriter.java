import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by lostincoding on 09.05.17.
 */
public class TreeWriter {
    private CrawlerTree tree;

    public TreeWriter(CrawlerTree tree) {
        this.tree = tree;
    }


    public void writeToFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("Started creating json");
        long start = System.currentTimeMillis();
        String json = createJSONFromTree();
        long end = System.currentTimeMillis();
        long time = (end - start) / 1000;
        System.out.println("Finished creating json " + time + " seconds");
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.println(json);
        writer.close();
    }

    public String createJSONFromTree() {


        ArrayList<CrawlerNode> nodes = getListOfNodes(tree.getRoot());
        System.out.print("Tree consists of " + nodes.size() + " Nodes");
        JSONObject jtree = new JSONObject();
        JSONArray jarray = new JSONArray();


        for (CrawlerNode node : nodes) {
            JSONObject n = new JSONObject();
            n.put("char", node.getData().getData() + "");
            n.put("count", node.getData().getCount());
            n.put("id", node.getId());
            CrawlerNode parent = node.getParent();
            if (parent != null) {
                n.put("parent", node.getParent().getId());
            } else {
                n.put("parent", "-1");
            }

            jarray.add(n);

        }
        jtree.put("chunksize", tree.getChunkSize());

        jtree.put("nodes", jarray);

        return jtree.toJSONString();
    }


    private ArrayList<CrawlerNode> getListOfNodes(CrawlerNode node) {
        ArrayList<CrawlerNode> nodes = new ArrayList<>();
        ArrayList<CrawlerNode> children;

        nodes.add(node);

        if ((children = node.getChildren()) != null) {
            for (CrawlerNode n : children) {
                nodes.addAll(getListOfNodes(n));
            }
        } else {
            nodes.add(node);
        }
        return nodes;
    }

 

}
