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

    /**
     * This method takes the tree given to the constructor, converts it to JSON
     * and writes it to the path.
     * @param path
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void writeToFile(String path) throws FileNotFoundException, UnsupportedEncodingException {


        //create JSON for root Node
        JSONObject jsonRootNode=new JSONObject();
        jsonRootNode.put("1", tree.getRoot().getData().getChar()+""); //char
        jsonRootNode.put("2", tree.getRoot().getData().getCount()); //count

        //create JSONObjects for children of root
        JSONArray rootChildren = new JSONArray();
        for (CrawlerNode rootChild : tree.getRoot().getChildren()) {
            rootChildren.add(createJSONObjectFromCrawlerNode(rootChild));
        }
        jsonRootNode.put("3", rootChildren);

        JSONObject jsonTree = new JSONObject();
        jsonTree.put("chunksize", tree.getChunkSize());
        jsonTree.put("root",jsonRootNode);
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
        jsonNode.put("1", node.getData().getChar()+""); //char
        jsonNode.put("2", node.getData().getCount()); //count

        JSONArray jsonChildren = new JSONArray();
        for (CrawlerNode child : node.getChildren()) {
            jsonChildren.add(createJSONObjectFromCrawlerNode(child));
        }

        jsonNode.put("3", jsonChildren); //children

        return jsonNode;
    }

}
