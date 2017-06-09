package crawler;

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


}
