import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private DataContainer data = null;
    private ArrayList<Node> children = new ArrayList<>();
    private Node parent = null;

    public Node(DataContainer data) {
        this.data = data;
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(DataContainer data) {
        Node newChild = new Node(data);
        newChild.setParent(this);
        children.add(newChild);
    }

    public void addChildren(List<Node> children) {
        for (Node t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public DataContainer getData() {
        return data;
    }

    public void setData(DataContainer data) {
        this.data = data;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + data.toString());
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1)
                    .print(prefix + (isTail ? "    " : "│   "), true);
        }
    }


    public Node getChild(char c) {


        for (Node child : children) {
            if (child.getData().getData() == c) {
                return child;
            }
        }

        return null;
    }
}