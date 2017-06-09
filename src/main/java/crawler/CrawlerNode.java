package crawler;

import general.Node;

import java.util.ArrayList;

public class CrawlerNode<T> extends Node<T> {
    private int id;
    public static int idcount = 1;

    public CrawlerNode(T data) {
        super(data);
        id = idcount;
        idcount++;
    }

    public ArrayList<CrawlerNode<T>> getChildrenAsArrayList() {
        ArrayList<CrawlerNode<T>> list = new ArrayList<>();
        for (Node node : super.getChildren()
             ) {
            list.add((CrawlerNode) node);
        }
        return list;
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
            ((CrawlerNode) children.get(i)).getStringRepresentation(prefix + (isTail ? "    " : "│   "), sb, false);
        }
        if (children.size() > 0) {
            ((CrawlerNode) children.get(children.size() - 1))
                    .getStringRepresentation(prefix + (isTail ? "    " : "│   "), sb, true);
        }
    }

    public CrawlerNode<T> getChild(char c) {
        for (Node child : children) {
            if (((DataContainer) child.getData()).getChar() == c) {
                return (CrawlerNode) child;
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
        Node node = this;
        while (((DataContainer) node.getParent().getData()).getChar() != 'X') {
            res.insert(0, String.valueOf(((DataContainer) node.getParent().getData()).getChar()));
            node = node.getParent();
        }
        return res.length() == 0 ? "X" : res.toString();
    }
}