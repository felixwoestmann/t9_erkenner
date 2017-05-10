import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by lostincoding on 09.05.17.
 */
public class TreeWriter {
    private CrawlerTree tree;

    public TreeWriter(CrawlerTree tree) {
        this.tree = tree;
    }


    public String createJSONFromTree() {

        JSONObject treejson = new JSONObject();
        ArrayList<CrawlerNode> nodes = getListOfNodes(tree.getRoot());
        JSONArray jarray = new JSONArray();

        for (CrawlerNode node : nodes) {
            JSONObject n = new JSONObject();
            n.put("char", node.getData().getData());
            n.put("count", node.getData().getCount());

            CrawlerNode parent = node.getParent();
            if (parent != null) {
                n.put("parent", node.getParent().getId());
            } else {
                n.put("parent", "-1");
            }

            jarray.add(n);

        }

        return jarray.toJSONString();
    }


    private ArrayList<CrawlerNode> getListOfNodes(CrawlerNode node) {
        ArrayList<CrawlerNode> nodes = new ArrayList<CrawlerNode>();
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
