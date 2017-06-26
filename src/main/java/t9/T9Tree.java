package t9;

import crawler.ProbabilityCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class T9Tree {
    private ArrayList<T9Node<T9DataContainer>> leafs = null;
    private ProbabilityCalculator probCalc = null;
    private int pathCount;
    private T9Node<T9DataContainer> root = null;

    public T9Tree(ProbabilityCalculator probCalc, short kPathCount) {
        root = new T9Node<>(new T9DataContainer("root"));
        leafs = getLeafs(root);
        this.probCalc = probCalc;
        this.pathCount = kPathCount;
    }

    public T9Tree(ProbabilityCalculator probCalc, int historySize) {
        this(probCalc, (short) 10);
        if (probCalc.getTreeChunksize() != historySize) throw new RuntimeException("history doesn't match chunksize");
    }


    public void processButton(char button) {
        //     System.out.println("Process button "+button);
        //get a list of literals
        final ArrayList<String> list = T9Keyboard.mapButton(button);
        //append every literal to every leaf if it is active
        leafs.forEach(leaf -> {
            if (leaf.getData().isActive()) {
                for (String s : list) {
                    leaf.addChild(new T9DataContainer(s));
                }
            }
        });
        //update the list of leafs to the new leafs
        updateLeafs();
        //set Probability for every leaf
        leafs.forEach(this::calcProbabilityForNode);
        //mark unprobable leafs as inactive
        markLeafsInactive(this.pathCount);
        //mark as much of the path to a inactive leaf as inactive ,too
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

    /**
     * Method prepares the tree for a new word
     */
    public void newWord() {
        T9Node<T9DataContainer> newroot = new T9Node<>(new T9DataContainer(-1, "root"));
        root = newroot;
        leafs = getLeafs(root);
    }

    /**
     * Removes all nodes which are inactive.
     * Starts at bottom and works its way up
     *
     * @param node
     */
    private void cleanTreeFromInactiveNodes(T9Node<T9DataContainer> node) {
        //cleans tree from all inactive paths
        T9Node<T9DataContainer> parent = node.getParent();

        if (!node.getData().isActive()) {
            parent.getChildren().remove(node);
            cleanTreeFromInactiveNodes(parent);
        }
    }

    /**
     * Marks all leafes of the tree as inactive which aren't
     * - in the best K paths (K is a positive integer)
     * OR
     * - the best path which ends in that symbol
     * <p>
     * compare P11
     *
     * @param pathCount
     */
    private void markLeafsInactive(int pathCount) {
        ArrayList<T9Node<T9DataContainer>> kbestPaths = getKBestPaths(pathCount);
        ArrayList<T9Node<T9DataContainer>> bestSymbolPaths = getBestPathForEveryLeafSymbol();

        //mark all leafs as inactive whch are in neither of those lists
        leafs.forEach(leaf -> {
            if (!kbestPaths.contains(leaf) && !bestSymbolPaths.contains(leaf)) {
                leaf.getData().setActive(false);
            }
        });
    }


    /**
     * Method gets a node and works its way up from the bottom
     * If all childs of the parent node are inactive the parent node is set inactive too
     * <p>
     * Stops if a parent node can't be set as inactive
     *
     * @param node
     */
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
        double probability;
        char c = leaf.getData().getChar();
        double historyProbability = 0;

        T9Node<T9DataContainer> historyLeaf = leaf;
        for (int i = 0; i < probCalc.getTreeChunksize(); i++) {
            if (!isRoot(historyLeaf)) {
                historyProbability += historyLeaf.getData().getProbability();
                historyLeaf = historyLeaf.getParent();
            }
        }

        //prob ln P (bn|b1...bn-1) prob of char with prefix
        double probOfCharWithPrefix;
        probOfCharWithPrefix = -Math.log(probCalc.conditionalProbabilityOfLastChar(getPathToRootAsString(leaf)));
        //prob ln P (tn|bn)
        double probOfPressedButton;
        probOfPressedButton = -Math.log(probCalc.probOfPressedButtonAndChar(c));

        probability = probOfCharWithPrefix + historyProbability;
        leaf.getData().setProbability(probability);
    }

    private void updateLeafs() {
        ArrayList<T9Node<T9DataContainer>> tmplist = new ArrayList<>();

        leafs.forEach(leaf -> {
            //just update leaf if node is active
            if (leaf.getData().isActive()) {
                tmplist.addAll(getLeafs(leaf));
            }
        });

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
            if (bestNode.getData().getProbability() > leaf.getData().getProbability()) {
                map.put(character, leaf);
            }

        }
        return new ArrayList<>(map.values());

    }

    /**
     * Returns a list of the K best paths.
     * If K=10 then you get the 10 best paths
     *
     * @param K
     * @return
     */
    private ArrayList<T9Node<T9DataContainer>> getKBestPaths(int K) {
        //the best path is at the same time the path of the best leaf
        //search best leafs

        //sort leafs so it is sorted by path quality
        leafs.sort((o1, o2) -> {
            //since we have to return an integer we multiply the probability by 1000 to get more precise
            double returnValue = o1.getData().getProbability() - o2.getData().getProbability();
            return (int) (returnValue * 1000);
        });

        //cut list if it is bigger then the path count
        if (leafs.size() < K) {
            return new ArrayList<>(leafs);
        } else {
            return new ArrayList<>(leafs.subList(0, K));
        }


    }


    /**
     * Returns the path from the given node to the root node as String
     *
     * @param node
     * @return
     */
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


        StringBuilder returnval = new StringBuilder();
        strings.forEach(returnval::append);
        return returnval.toString();
    }

    private String getPathToRootAsString(T9Node<T9DataContainer> node) {
        LinkedList<String> strings = new LinkedList<>();

        //print out the path of every leaf
        T9Node<T9DataContainer> actnode = node;
        while (!isRoot(actnode)) {
            strings.add(actnode.getData().getChar() + "");
            actnode = actnode.getParent();
        }
        Collections.reverse(strings);

        StringBuilder returnval = new StringBuilder();
        strings.forEach(returnval::append);
        return returnval.toString();
    }

    /**
     * Returns the best guess for the typed word
     *
     * @return
     */
    public String getBestGuess() {
        //sort leafs so it is sorted by path quality


        LinkedList<Character> bestguess = new LinkedList<>();

        T9Node<T9DataContainer> actnode = getKBestPaths(1).get(0);
        while (!isRoot(actnode)) {
            bestguess.add(actnode.getData().getChar());
            actnode = actnode.getParent();
        }
        Collections.reverse(bestguess);


        StringBuilder returnval = new StringBuilder();
        bestguess.forEach(returnval::append);
        return returnval.toString();
    }

    private ArrayList<T9Node<T9DataContainer>> getLeafs(T9Node<T9DataContainer> start) {
        ArrayList<T9Node<T9DataContainer>> leafs = new ArrayList<>();

        if (start.isLeaf()) {
            leafs.add(start);
            return leafs;
        } else {
            start.getChildren().forEach(node -> leafs.addAll(getLeafs(node)));
        }

        return leafs;
    }

}