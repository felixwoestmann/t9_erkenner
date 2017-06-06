import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    protected T data = null;
    protected List<Node> children = new ArrayList<>();
    protected Node parent = null;

    public Node(T data) {
        this.data = data;
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(T data) {
        Node<T> newChild = new Node<>(data);
        newChild.setParent(this);
        children.add(newChild);
    }

    public void addChildren(List<Node> children) {
        for (Node t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<Node> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
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
            ((Node<T>) children.get(i)).getStringRepresentation(prefix + (isTail ? "    " : "│   "), sb, false);
        }
        if (children.size() > 0) {
            ((Node<T>) children.get(children.size() - 1))
                    .getStringRepresentation(prefix + (isTail ? "    " : "│   "), sb, true);
        }
    }
}