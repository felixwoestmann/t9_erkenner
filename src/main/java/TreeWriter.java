import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by lostincoding on 09.05.17.
 */
public class TreeWriter {
    private CrawlerTree tree;
    private int count = 1;

    public TreeWriter(CrawlerTree tree) {
        this.tree = tree;
    }


    public void writeToFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
        numberTree(tree.getRoot());
        //open print writer
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        CrawlerNode<DataContainer> root = tree.getRoot();
        //print begin of json with tree data
        StringBuilder sb = new StringBuilder();
        sb.append("{\"nodes\":[");

        sb.append("\n");
        //add root
        sb.append(createJSONObjectStringFromSingleNode(root)).append(",");

        writer.println(sb.toString());
        sb = new StringBuilder();
        // add other nodes
        for (CrawlerNode<DataContainer> crawlerNode : root.getChildrenAsArrayList()) {
            sb.append(createJSONArrayStringFromNodes(crawlerNode));
            sb.append(",\n");
            writer.println(sb.toString());
            sb = new StringBuilder();
        }


        //finalize everything
        sb.append("],\"chunksize\":").append(tree.getChunkSize()).append("}");


        writer.println(sb.toString());
        writer.close();
    }

    private String createJSONArrayStringFromNodes(CrawlerNode node) {
        ArrayList<CrawlerNode> nodes = getListOfNodes(node);
        StringBuilder sb = new StringBuilder();

        for (CrawlerNode crawlerNode : nodes) {
            sb.append(createJSONObjectStringFromSingleNode(crawlerNode));
            sb.append(",");
        }

        //remove last komma
        String returnvalue = sb.toString();
        return returnvalue.substring(0, returnvalue.length() - 1);
    }


    private String createJSONObjectStringFromSingleNode(CrawlerNode<DataContainer> node) {
        //use numbers instead of strings to save disk space
        JSONObject n = new JSONObject();
        n.put("1", node.getData().getChar() + ""); //char
        n.put("2", node.getData().getCount()); //count
        n.put("3", node.getId()); //id
        CrawlerNode<DataContainer> parent = ((CrawlerNode<DataContainer>) node.getParent());

        if (parent != null) {
            n.put("4", ((CrawlerNode<DataContainer>) node.getParent()).getId()); //parent
        } else {
            n.put("4", "-1"); //parent
        }

        return n.toJSONString();
    }


    private ArrayList<CrawlerNode> getListOfNodes(CrawlerNode node) {
        ArrayList<CrawlerNode> nodes = new ArrayList<CrawlerNode>();
        ArrayList<CrawlerNode> children;

        nodes.add(node);

        if ((children = node.getChildrenAsArrayList()) != null) {
            for (CrawlerNode n : children) {
                nodes.addAll(getListOfNodes(n));
            }
        } else {
            nodes.add(node);
        }
        return nodes;
    }

    /*
    Number tree so that each child of the root has a consistent room of numbers.

    child 1 id: 2
        2 - 1336
    child 2 id: 1337
        1337 - 2999
    child 3 id: 3000


     */
    private void numberTree(CrawlerNode node) {
        node.setId(count++);

        ArrayList<CrawlerNode> children = node.getChildrenAsArrayList();
        if (!children.isEmpty()) {
            for (CrawlerNode child : children) {
                numberTree(child);
            }
        }
    }
}
