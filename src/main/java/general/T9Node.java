package general;

import java.util.ArrayList;
import java.util.List;

public class T9Node<T> {
    protected T data = null;
    protected List<T9Node<T>> children = new ArrayList<>();
    protected T9Node<T> parent = null;

    public T9Node(T data) {
        this.data = data;
    }

    public void addChild(T9Node<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(T data) {
        T9Node<T> newChild = new T9Node<>(data);
        newChild.setParent(this);
        children.add(newChild);
    }

    public void addChildren(List<T9Node<T>> children) {
        for (T9Node<T> t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<T9Node<T>> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(T9Node<T> parent) {
        this.parent = parent;
    }

    public T9Node<T> getParent() {
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
        sb.append(prefix).append(isTail ? "└── " : "├── ").append(data.toString()).append("\n");
        for (int i = 0; i < children.size() - 1; i++) {
            ((T9Node<T>) children.get(i)).getStringRepresentation(prefix + (isTail ? "    " : "│   "), sb, false);
        }
        if (children.size() > 0) {
            ((T9Node<T>) children.get(children.size() - 1))
                    .getStringRepresentation(prefix + (isTail ? "    " : "│   "), sb, true);
        }
    }
}