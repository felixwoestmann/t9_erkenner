package t9;

import crawler.ProbabilityCalculator;

import java.util.*;

public class T9Tree {
    private ArrayList<T9Node<T9DataContainer>> leafs = null;
    private ProbabilityCalculator probCalc = null;
    private int historySize;
    private T9Node<T9DataContainer> root = null;


    public T9Tree(ProbabilityCalculator probCalc, int historySize) {
        root = new T9Node<>(new T9DataContainer(-1, "root"));
        leafs = getLeafs(root);
        this.probCalc = probCalc;
        this.historySize = historySize;
    }

    public void processButton(char button) {
        ArrayList<String> list = new ArrayList<>();

        try {
            list = mapButton(button);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        for (T9Node<T9DataContainer> n : leafs) {
            if (n.getData().isActive()) {
                for (String s : list) {
                    n.addChild(new T9DataContainer(1, s));
                }
            }

        }

        updateLeafs();
        for (T9Node<T9DataContainer> leaf : leafs) {
            updateProbability(leaf);
        }
        markPathsInactive(10);
    }

    private void clean() {
        //cleans tree from all inactive paths

        //therefore we will delete all nodes, beginning by the leafes, as long as it is inactive or all children are inactive

    }

    private void markPathsInactive(int pathcount) {
        ArrayList<T9Node<T9DataContainer>> kbestPaths = getKBestPaths(pathcount);
        ArrayList<T9Node<T9DataContainer>> bestSymbolPaths = getBestPathForEveryLeafSymbol();

        for (T9Node<T9DataContainer> leaf : leafs) {
            if (!kbestPaths.contains(leaf) && !bestSymbolPaths.contains(leaf)) {
                leaf.getData().setActive(false);
            }
        }
    }

    private void updateProbability(T9Node<T9DataContainer> leaf) {
        double probability = 0;
        char c = leaf.getData().getChar();
        double historyProbability = leaf.getData().getProbability();

        //prob ln P (bn|b1...bn-1) prob of char with prefix
        double probOfCharWithPrefix = probCalc.probOfCharWithDefinedPrefix(leaf.getHistory(historySize), c);
        //prob ln P (tn|bn)
        double probOfPressedButton = probCalc.probOfPressedButtonAndChar(c);
        //calc ln
        probOfCharWithPrefix = Math.log(probOfCharWithPrefix) * -1;
        probOfPressedButton = Math.log(probOfPressedButton) * -1;

        probability = historyProbability + probOfCharWithPrefix + probOfPressedButton;

        leaf.getData().setProbability(probability);
    }

    private void updateLeafs() {
        ArrayList<T9Node<T9DataContainer>> tmplist = new ArrayList<>();

        for (T9Node<T9DataContainer> n : leafs) {
            tmplist.addAll(getLeafs(n));
        }

        leafs = tmplist;
    }

    private ArrayList<T9Node<T9DataContainer>> getBestPathForEveryLeafSymbol() {
        HashMap<String, T9Node<T9DataContainer>> map = new HashMap<>();

        //save the best node of every character in a map
        for (T9Node<T9DataContainer> leaf : leafs) {
            String character = leaf.getData().getCharAsString();
            T9Node<T9DataContainer> bestNode = map.get(character);
            if (bestNode == null) {
                map.put(character, leaf);
                break;
            }
            if (bestNode.getData().getProbability() < leaf.getData().getProbability()) {
                map.put(character, leaf);
            }

        }
        return new ArrayList<>(map.values());

    }


    public void printBestPaths(int pathcount) {
        for (T9Node<T9DataContainer> leaf : getKBestPaths(pathcount)) {
            System.out.println(getPathAsString(leaf));
        }
    }

    private ArrayList<T9Node<T9DataContainer>> getKBestPaths(int K) {
        //the best path is at the same time the path of the best leaf
        //search best leafs

        //sort leafs so it is sorted by path quality
        leafs.sort((o1, o2) -> {
            //since we have to return an integer we multiply the probability by 1000 to get more precise
            double returnValue = o2.getData().getProbability() - o1.getData().getProbability();
            return (int) (returnValue * 1000);
        });

        //cut list if it is bigger then the path count
        if (leafs.size() < K) {
            return new ArrayList<>(leafs);
        } else {
            return new ArrayList<>(leafs.subList(0, K));
        }


    }

    private String getPathAsString(T9Node<T9DataContainer> node) {
        LinkedList<String> strings = new LinkedList<>();


        //print out the path of every leaf

        T9Node<T9DataContainer> actnode = node;
        while (!actnode.getData().getCharAsString().equals(root.getData().getCharAsString())) {
            String line = actnode.getData().getChar() + " : " + actnode.getData().getProbability() + "-->" + ("\n");
            strings.add(line);
            actnode = actnode.getParent();
        }
        Collections.reverse(strings);


        String returnval = "";
        for (String string : strings) {
            returnval += string;
        }

        return returnval;
    }

    public void printTree() {
        root.print();
    }


    private ArrayList<T9Node<T9DataContainer>> getLeafs(T9Node<T9DataContainer> start) {
        ArrayList<T9Node<T9DataContainer>> leafs = new ArrayList<>();

        if (start.isLeaf()) {
            leafs.add(start);
            return leafs;
        } else {

            for (T9Node<T9DataContainer> n : start.getChildren()) {
                leafs.addAll(getLeafs(n));
            }
        }

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

}