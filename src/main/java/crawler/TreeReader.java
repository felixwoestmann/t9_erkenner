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
    /*
    Returns a CrawlerTree from a valid JSON File
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

    /*
    Returns a CrawlerTree from a valid JSON String
     */
    private CrawlerTree parseJSON(String json) throws ParseException {
        //load everything in JSON Objects
        JSONParser parser = new JSONParser();
        JSONObject jtree = (JSONObject) parser.parse(json);
        JSONArray nodes = (JSONArray) jtree.get("nodes");
        int chunksize = ((Long) jtree.get("chunksize")).intValue();

        CrawlerNode root = mapNodes(getNodesFromJSONArray(nodes));
        CrawlerTree tree = new CrawlerTree(chunksize, root);

        return tree;
    }
    /*
    Takes a JSON Array of Nodes and returns a List of NodeContainers
     */
    private ArrayList<CrawlerNodeContainer> getNodesFromJSONArray(JSONArray array) {
        ArrayList<CrawlerNodeContainer> container = new ArrayList<>();


        //create container with an node, id and parent id
        for (Object o : array) {
            JSONObject jo = (JSONObject) o;

            char c = ((String) jo.get("1")).charAt(0);  //char
            int count = ((Long) jo.get("2")).intValue(); //count
            int id = ((Long) jo.get("3")).intValue(); //id
            int parent;
            Object todecide = jo.get("4"); //parent

            // parent id can be -1 which is interpreted as a string
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
    /*
    Takes a list of node containers and tries to stitch them together.
    Returns the root node of the newly formed tree
     */
    private CrawlerNode mapNodes(ArrayList<CrawlerNodeContainer> list) {
        CrawlerNode root = null;
        CrawlerNodeContainer rootContainer = null;

        //get root node an container
        rootContainer = list.get(0);
        root = rootContainer.getNode();

        // get children from root node
        ArrayList<CrawlerNodeContainer> rootChildren = getChildrenNodesFromList(list, rootContainer.getId());

        //split list into one for each child of root
        ArrayList<ArrayList<CrawlerNodeContainer>> lists = new ArrayList<>();
        for (int i = 0; i < rootChildren.size(); i++) {
            int startId = rootChildren.get(i).getId();
            int stopId = 0;
            if (i == rootChildren.size() - 1) {
                stopId = Integer.MAX_VALUE;
            } else {
                stopId = rootChildren.get(i + 1).getId();
            }
            ArrayList<CrawlerNodeContainer> partList = getNodesBetweenIds(startId, stopId, list);
            lists.add(partList);
        }


        //create executor service with number of threads = count of processors
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<?>[] threads = new Future<?>[rootChildren.size()];
        for (int i = 0; i < rootChildren.size(); i++) {
            int j = i;
            threads[i] = executorService.submit(() -> getChildren(lists.get(j), rootChildren.get(j)));
        }

        //wait until all threads are finished
        executorService.shutdown();

        //stitch everything back together
        root.addChildren(containerListToNodeList(rootChildren));

        return root;
    }

    /**
     * searches in list for children of the node in parameter container
     *
     * @param container The Container of the node the
     * @param list A list of NodeContainers
     */
    private void getChildren(ArrayList<CrawlerNodeContainer> list, CrawlerNodeContainer container) {
        CrawlerNode node = container.getNode();
        ArrayList<CrawlerNodeContainer> nodes = getChildrenNodesFromList(list, container.getId());

        if (!nodes.isEmpty()) {
            node.addChildren(containerListToNodeList(nodes));
            for (CrawlerNodeContainer crawlerNodeContainer : nodes) {
                getChildren(list, crawlerNodeContainer);
            }
        }
    }


    private ArrayList<CrawlerNodeContainer> getNodesBetweenIds(int startId, int stopId, ArrayList<CrawlerNodeContainer> list) {
        ArrayList<CrawlerNodeContainer> nodes = new ArrayList<>();

        for (CrawlerNodeContainer crawlerNodeContainer : list) {
            if (crawlerNodeContainer.getId() >= startId && crawlerNodeContainer.getId() < stopId) {
                nodes.add(crawlerNodeContainer);
            }
        }
        return nodes;
    }

    private ArrayList<CrawlerNodeContainer> getChildrenNodesFromList(ArrayList<CrawlerNodeContainer> containers, int id) {

        ArrayList<CrawlerNodeContainer> nodes = new ArrayList<>();

        for (CrawlerNodeContainer container : containers) {

            if (container.getParent() == id) {
                nodes.add(container);

            }
        }
        return nodes;
    }

    /*
    Converts a list of container to a list of nodes by extractng the node from every container
     */
    private ArrayList<CrawlerNode> containerListToNodeList(ArrayList<CrawlerNodeContainer> list) {
        ArrayList<CrawlerNode> nodes = new ArrayList<>();

        for (CrawlerNodeContainer container : list) {
            nodes.add(container.getNode());
        }

        return nodes;
    }
}


