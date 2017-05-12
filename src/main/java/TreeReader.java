import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.misc.IOUtils;

import javax.xml.crypto.Data;
import javax.xml.soap.Node;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lostincoding on 10.05.17.
 */
public class TreeReader {

    public TreeReader() {

    }

    public CrawlerTree parseJSON(String json) throws ParseException {

        JSONParser parser = new JSONParser();
        JSONObject jtree = (JSONObject) parser.parse(json);
        JSONArray nodes = (JSONArray) jtree.get("nodes");

        CrawlerNode root = mapNodes(getNodesFromJSONArray(nodes));

        int chunksize = ((Long) jtree.get("chunksize")).intValue();
        CrawlerTree tree = new CrawlerTree(chunksize, root);

        return tree;
    }

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



    private ArrayList<CrawlerNodeContainer> getNodesFromJSONArray(JSONArray array) {
        ArrayList<CrawlerNodeContainer> container = new ArrayList<>();


        //create container with an node, id and parent id
        for (Object o : array) {
            JSONObject jo = (JSONObject) o;

            char c = ((String) jo.get("char")).charAt(0);
            int count = ((Long) jo.get("count")).intValue();
            int id = ((Long) jo.get("id")).intValue();
            int parent;
            Object todecide = jo.get("parent");

            if (todecide instanceof String) {
                parent = Integer.parseInt((String) todecide);
            } else {
                parent = ((Long) todecide).intValue();
            }

            CrawlerNode cn = new CrawlerNode(new DataContainer(c, count));
            CrawlerNodeContainer con = new CrawlerNodeContainer(parent, id, cn);
            container.add(con);
        }

        return container;
    }

    private CrawlerNode mapNodes(ArrayList<CrawlerNodeContainer> list) {

        CrawlerNode root = null;
        CrawlerNodeContainer rootContainer=null;

        for (CrawlerNodeContainer container : list) {
            if (container.getParent()==-1) {
                root=container.getNode();
                rootContainer=container;
            }
        }


        getChilds(list,rootContainer);

        return root;
    }

    private void getChilds(ArrayList<CrawlerNodeContainer> list, CrawlerNodeContainer container) {
        CrawlerNode node=container.getNode();
        ArrayList<CrawlerNodeContainer> nodes=CrawlerNodeContainer.getChildNodesFromList(list,container.getId());

        if(!nodes.isEmpty()) {
            node.addChildren(CrawlerNodeContainer.containerListToNodeList(nodes));
            for (CrawlerNodeContainer crawlerNodeContainer : nodes) {
                getChilds(list,crawlerNodeContainer);
            }
        }
    }


}


