package crawler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utility.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by lostincoding on 10.05.17.
 */
public class TreeReader {
    /**
     * Loads the JSON File from the given path.
     * Than the parseJSON is called and the generated trees is returned
     *
     * @param path
     * @return
     * @throws IOException
     */
    public CrawlerTree getTreeFromFile(String path) throws IOException {
        String json = FileReader.readFile(path);
        CrawlerTree tree = null;
        try {
            tree = parseJSON(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return tree;
    }

    /**
     * Creats a CrawlerTree and returns it from given JSON String
     * @param json
     * @return
     * @throws ParseException
     */
    private CrawlerTree parseJSON(String json) throws ParseException {
        int chunksize;
        CrawlerNode root;
        CrawlerTree tree;
        JSONParser parser = new JSONParser();

        //load jsonTree
        JSONObject jsonTree = (JSONObject) parser.parse(json);
        //get ChunkSize and root Node
        chunksize = ((Long) jsonTree.get("chunksize")).intValue();
        JSONObject jsonRoot = (JSONObject) jsonTree.get("root");

        root = new CrawlerNode(new DataContainer( ((String)jsonRoot.get("1")).charAt(0), ((Long) jsonRoot.get("2")).intValue()));

        //load every child from root seperatly
        JSONArray rootChildren = (JSONArray) jsonRoot.get("3");
        for (Object o : rootChildren) {
            JSONObject child = (JSONObject) o;
            root.addChild(jsonNodeToCrawlerNode(child));
        }

        tree = new CrawlerTree(chunksize, root);
        return tree;
    }

    /**
     * Converts a JSONObject recursively which contains a node to a CrawlerNode
     * @param node
     * @return
     */
    private CrawlerNode jsonNodeToCrawlerNode(JSONObject node) {
        String character = (String) node.get("1");
        int count = ((Long) node.get("2")).intValue();
        JSONArray jsonChildren = (JSONArray) node.get("3");
        CrawlerNode crawlerNode = new CrawlerNode(new DataContainer(character.charAt(0), count));

        for (Object jsonChild : jsonChildren) {
            crawlerNode.addChild(jsonNodeToCrawlerNode((JSONObject) jsonChild));
        }
        return crawlerNode;
    }

}


