package crawler;

import crawler.CrawlerNode;
import crawler.CrawlerTree;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
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
        JSONObject jsonTree = new JSONObject();
        jsonTree.put("chunksize", tree.getChunkSize());

        JSONArray rootChildren = new JSONArray();
        for (CrawlerNode rootChild : tree.getRoot().getChildren()) {
            rootChildren.add(createJSONObjectFromCrawlerNode(rootChild));
        }
        jsonTree.put("rootchildren", rootChildren);
        //write JSON to String
        PrintWriter writer = new PrintWriter(path);
        writer.append(jsonTree.toJSONString());
        writer.close();
    }

    /**
     * This method recieves a node an converts it to a JSONObject which contains all information about the node.
     * Including all children as JSONObjects
     *
     * It does this using recursion
     *
     * @param node
     * @return JSONObject containing all children
     */
    private JSONObject createJSONObjectFromCrawlerNode(CrawlerNode node) {
        //use numbers instead of descriptive strings to get json file smaller
        // 1 : char
        // 2 : count
        // 3 : children
        JSONObject jsonNode = new JSONObject();
        jsonNode.put("1", node.getData().getChar()); //char
        jsonNode.put("2", node.getData().getCount()); //count

        JSONArray jsonChildren = new JSONArray();
        for (CrawlerNode child : node.getChildren()) {
            jsonChildren.add(createJSONObjectFromCrawlerNode(child));
        }

        jsonNode.put("3", jsonChildren); //children

        return jsonNode;
    }

}
