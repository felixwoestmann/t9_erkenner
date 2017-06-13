package t9;

import crawler.ProbabilityCalculator;

import java.util.*;

public class T9Tree {
    private ArrayList<T9Node<T9DataContainer>> leafs = null;
    private ProbabilityCalculator probCalc = null;
    private int historySize;
    private T9Node<T9DataContainer> root = null;
    private int size = 0;

    public T9Tree(ProbabilityCalculator probCalc, int historySize) {
        root = new T9Node<>(new T9DataContainer(-1, "root"));
        leafs = getLeafs(root);
        this.probCalc = probCalc;
        this.historySize = historySize;
    }

    public void processButton(char button) {
        ArrayList<String> list = new ArrayList<>();

        try {
            list = T9Keyboard.mapButton(button);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        for (T9Node<T9DataContainer> n : leafs) {
            //just expand node if i is active
            if (n.getData().isActive()) {
                size += list.size();
                for (String s : list) {
                    n.addChild(new T9DataContainer(1, s));
                }
            } else {
                System.out.print("Skip node " + n.getStringRepresentation() + " cause it is not active\n");
            }

        }

        updateLeafs();
        for (T9Node<T9DataContainer> leaf : leafs) {
            updateProbability(leaf);
        }
        markLeafsInactive(5);
        cleanTreeFromInactiveNodes();
    }

    private void cleanTreeFromInactiveNodes() {
        //cleans tree from all inactive paths
        //therefore we will delete all nodes, beginning by the leafes, as long as it is inactive or all children are inactive

        for (T9Node<T9DataContainer> leaf : leafs) {
            if (!leaf.getData().isActive()) {
                deletePathFromBottom(leaf);
            }
        }
    }

    private void deletePathFromBottom(T9Node<T9DataContainer> leaf) {
        T9Node<T9DataContainer> actnode = leaf;
        T9Node<T9DataContainer> parent = actnode.getParent();


        if (!actnode.getData().isActive()) {
            parent.getChildren().remove(actnode);

            actnode = actnode.getParent();
            parent = actnode.getParent();
            size--;
        }


        System.out.println("Size of T9Tree :" + size);
    }


    private void markLeafsInactive(int pathcount) {
        ArrayList<T9Node<T9DataContainer>> kbestPaths = getKBestPaths(pathcount);
        ArrayList<T9Node<T9DataContainer>> bestSymbolPaths = getBestPathForEveryLeafSymbol();


        //mark all paths as inactive whch are in neither of those lists
        for (T9Node<T9DataContainer> leaf : leafs) {
            if (!kbestPaths.contains(leaf) && !bestSymbolPaths.contains(leaf)) {
                leaf.getData().setActive(false);
            }
        }


    }

    private void markPathAsInactive(T9Node<T9DataContainer> node) {
        //first check if node has children at all
        T9Node<T9DataContainer> parent = node.getParent();
        if (isRoot(parent)) {

        }
        if (node.getChildren() == null || node.getChildren().isEmpty()) {

        }
    }

    private boolean isRoot(T9Node<T9DataContainer> node) {
        return node.getData().getCharAsString().equals(root.getData().getCharAsString());
    }

    private void updateProbability(T9Node<T9DataContainer> leaf) {
        double probability = 0;
        char c = leaf.getData().getChar();
        double historyProbability = 0;
        if (isRoot(leaf)) {
            historyProbability = leaf.getData().getProbability();
        }


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
            //just update leaf if node is active
            if (n.getData().isActive()) {
                tmplist.addAll(getLeafs(n));
            }

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
        while (!isRoot(actnode)) {
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


}