import java.util.ArrayList;
import java.util.List;

public class CrawlerNode {
    private DataContainer data = null;
    private int id;
    private ArrayList<CrawlerNode> children = new ArrayList<>();
    private CrawlerNode parent = null;
    public static int idcount = 1;

    public CrawlerNode(DataContainer data) {
        this.data = data;
        id = idcount;
        idcount++;
    }


    public void addChild(CrawlerNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(DataContainer data) {
        CrawlerNode newChild = new CrawlerNode(data);
        newChild.setParent(this);
        children.add(newChild);
    }

    public void addChildren(List<CrawlerNode> children) {
        for (CrawlerNode t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public ArrayList<CrawlerNode> getChildren() {
        return children;
    }

    public DataContainer getData() {
        return data;
    }

    public void setData(DataContainer data) {
        this.data = data;
    }

    private void setParent(CrawlerNode parent) {
        this.parent = parent;
    }

    public CrawlerNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public void print() {
        System.out.println(getStringRepresentation());
    }


    public String getStringRepresentation() {
        StringBuilder sb = new StringBuilder();
        getStringRepresentation("", sb, true);
        return sb.toString();
    }

    private void getStringRepresentation(String prefix, StringBuilder sb, boolean isTail) {
        sb.append(prefix + (isTail ? "└── " : "├── ") + data.toString()).append("\n");
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).getStringRepresentation(prefix + (isTail ? "    " : "│   "), sb, false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1)
                    .getStringRepresentation(prefix + (isTail ? "    " : "│   "), sb, true);
        }
    }


    public CrawlerNode getChild(char c) {


        for (CrawlerNode child : children) {
            if (child.getData().getChar() == c) {
                return child;
            }
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTreePathAsString() {
        StringBuilder res = new StringBuilder();
        CrawlerNode node = this;
        while (node.getParent().getData().getChar() != 'X') {
            res.insert(0, String.valueOf(node.getParent().getData().getChar()));
            node = node.getParent();
        }
        return res.length() == 0 ? "X" : res.toString();
    }
}