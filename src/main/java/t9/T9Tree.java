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
        //get a list of literals
       final ArrayList<String> list =  T9Keyboard.mapButton(button);


        //append every literal to every leaf if it is active
        leafs.forEach(leaf -> {
            if (leaf.getData().isActive()) {
                for (String s : list) {
                    leaf.addChild(new T9DataContainer(1, s));
                }
            }
        });
        //update the list of leafs to the new leafs
        updateLeafs();

        //set Probability for every leaf
        leafs.forEach(this::calcProbabilityForNode);

        //mark unprobable leafs as inactive
        markLeafsInactive(5);
        //try to mark as much of the path to a inactive leaf as inactive ,too
        leafs.forEach(leaf -> {
            if (!leaf.getData().isActive()) {
                markPathAsInactive(leaf);
            }
        });
        //delete all inactive nodes
        leafs.forEach(leaf -> {
            if (!leaf.getData().isActive()) {
                cleanTreeFromInactiveNodes(leaf);
            }
        });


    }

    public void printTree() {
        root.print();
    }

    public void printBestPaths(int pathcount) {
        for (T9Node<T9DataContainer> leaf : getKBestPaths(pathcount)) {
            System.out.println(getPathAsString(leaf));
        }
    }

    private void cleanTreeFromInactiveNodes(T9Node<T9DataContainer> node) {
        //cleans tree from all inactive paths
        T9Node<T9DataContainer> parent = node.getParent();

        if (!node.getData().isActive()) {
            parent.getChildren().remove(node);
            cleanTreeFromInactiveNodes(parent);
        }


    }


    private void markLeafsInactive(int pathcount) {
        ArrayList<T9Node<T9DataContainer>> kbestPaths = getKBestPaths(pathcount);
        ArrayList<T9Node<T9DataContainer>> bestSymbolPaths = getBestPathForEveryLeafSymbol();


        //mark all leafs as inactive whch are in neither of those lists
        for (T9Node<T9DataContainer> leaf : leafs) {
            if (!kbestPaths.contains(leaf) && !bestSymbolPaths.contains(leaf)) {
                leaf.getData().setActive(false);
            }
        }

    }


    private void markPathAsInactive(T9Node<T9DataContainer> node) {

        T9Node<T9DataContainer> parent = node.getParent();
        //check if parent is root
        if (isRoot(parent)) {
            return;
        }
        //if any child of the parent is active the parent also has to be active
        boolean parentIsActive = false;
        for (T9Node<T9DataContainer> child : parent.getChildren()) {
            if (child.getData().isActive()) {
                parentIsActive = true;
                break;
            }

        }
        //if all childs are inactive set parent as inactive and call this method again for the next higher level
        if (!parentIsActive) {
            parent.getData().setActive(false);
            markPathAsInactive(parent);
        }
    }

    private boolean isRoot(T9Node<T9DataContainer> node) {
        return node.getData().getCharAsString().equals(root.getData().getCharAsString());
    }

    private void calcProbabilityForNode(T9Node<T9DataContainer> leaf) {
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