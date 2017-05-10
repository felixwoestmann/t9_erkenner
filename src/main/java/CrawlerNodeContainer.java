import java.util.ArrayList;

/**
 * Created by lostincoding on 10.05.17.
 */
public class CrawlerNodeContainer {

    private int parent;
    private int id;
    private CrawlerNode node;


    public CrawlerNodeContainer(int parent, int id, CrawlerNode node) {
        this.parent = parent;
        this.id = id;
        this.node = node;
    }

    public int getParent() {
        return parent;
    }

    public int getId() {
        return id;
    }

    public CrawlerNode getNode() {
        return node;
    }


    public static ArrayList<CrawlerNodeContainer> getChildNodesFromList(ArrayList<CrawlerNodeContainer> containers, int id) {
        ArrayList<CrawlerNodeContainer> nodes = new ArrayList<>();


        for (CrawlerNodeContainer container : containers) {
            if (container.getParent() == id) {
                nodes.add(container);
            }
        }

        return nodes;
    }

    public static ArrayList<CrawlerNode> containerListToNodeList(ArrayList<CrawlerNodeContainer> list) {
        ArrayList<CrawlerNode> nodes = new ArrayList<>();

        for (CrawlerNodeContainer container : list) {
            nodes.add(container.getNode());
        }

        return nodes;
    }
}
