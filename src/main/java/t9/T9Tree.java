package t9;

import crawler.ProbabilityCalculator;
import general.Node;

import java.util.ArrayList;

public class T9Tree {
    private ArrayList<Node<T9DataContainer>> leafs = null;
    private ProbabilityCalculator probCalc=null;
    private int historySize;

    private Node<T9DataContainer> root = null;
    public static int count = 0;

    public T9Tree(ProbabilityCalculator probCalc,int historySize) {
        root = new Node<>(new T9DataContainer(-1, "root"));
        leafs = getLeafs(root);
        count = 0;
        this.probCalc=probCalc;
        this.historySize=historySize;
    }

    public void processButton(char button) {
        ArrayList<String> list = null;

        try {
            list = mapButton(button);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        for (Node<T9DataContainer> n : leafs) {
            for (String s : list) {
                n.addChild(new T9DataContainer(1,s));
            }
        }

        updateLeafs();

    }

    private void updateProbabilitys() {

    }

    private ArrayList<Node<T9DataContainer>> getLeafs(Node<T9DataContainer> start) {
        ArrayList<Node<T9DataContainer>> leafs = new ArrayList<>();

        if (start.isLeaf()) {
            leafs.add(start);
            return leafs;
        } else {
            for (Node<T9DataContainer> n : start.getChildren()) {
                leafs.addAll(getLeafs(n));
            }
        }

        return leafs;
    }

    private void updateLeafs() {
        ArrayList<Node<T9DataContainer>> tmplist = new ArrayList<>();

        count += leafs.size();
        for (Node<T9DataContainer> n : leafs) {
            tmplist.addAll(getLeafs(n));
        }

        leafs = tmplist;
    }


    public Node<T9DataContainer> getRoot() {
        return root;
    }


    public ArrayList<Node<T9DataContainer>> getLeafs() {
        return leafs;
    }

    private ArrayList<String> mapButton(char button) throws IllegalArgumentException {

        ArrayList<String> list = new ArrayList<>();
        switch (button) {
            case '1':
                list.add("1");
                list.add(".");
                list.add(",");
                return list;
            case '2':
                list.add("2");
                list.add("a");
                list.add("b");
                list.add("c");
                return list;
            case '3':
                list.add("3");
                list.add("d");
                list.add("e");
                list.add("f");
                return list;
            case '4':
                list.add("4");
                list.add("g");
                list.add("h");
                list.add("i");
                return list;
            case '5':
                list.add("5");
                list.add("j");
                list.add("k");
                list.add("l");
                return list;
            case '6':
                list.add("6");
                list.add("m");
                list.add("n");
                list.add("o");
                return list;
            case '7':
                list.add("7");
                list.add("p");
                list.add("q");
                list.add("r");
                list.add("s");
                return list;
            case '8':
                list.add("8");
                list.add("t");
                list.add("u");
                list.add("v");
                return list;
            case '9':
                list.add("9");
                list.add("w");
                list.add("x");
                list.add("y");
                list.add("z");
                return list;
            case '0':
                list.add("0");
                list.add(" ");
                return list;

            default:
                throw new IllegalArgumentException("Zeichen ist nicht bekannt");
        }
    }

    public void printTree() {
        root.print();
    }

}