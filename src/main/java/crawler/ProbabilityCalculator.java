package crawler;

import java.util.ArrayList;

/**
 * Created by lostincoding on 23.05.17.
 */
public class ProbabilityCalculator {
    private CrawlerTree tree;
    private final double CORRECTBUTTONPROB = 0.95;

    public ProbabilityCalculator(CrawlerTree tree) {
        this.tree = tree;
    }


    public double probOfChar(char c) {
        return getProbOfCharOnLevel(tree.getRoot(), c);
    }

    public double probOfTwoChars(char one, char two) {
        CrawlerNode c1node = getChildWithCharAsData(tree.getRoot(), one);

        double probOfOne = getProbOfCharOnLevel(tree.getRoot(), one);
        double probOfTwo = getProbOfCharOnLevel(c1node, two);

        return probOfOne * probOfTwo;
    }

    public double probOfStringShorterThanChunkSize(String string) {
        if (string.length() > tree.getChunkSize()) {
            throw new IllegalArgumentException("Length of String is too big");
        }

        char[] chars = string.toCharArray();

        double overallProbability = 1;

        CrawlerNode actNode = tree.getRoot();
        for (char c : chars) {
            double singleCharProbabilityOnGivenNode = getProbOfCharOnLevel(actNode, c);
            actNode = getChildWithCharAsData(actNode, c);
            overallProbability *= singleCharProbabilityOnGivenNode;
        }

        return overallProbability;
    }

    public double probabilityOfString(String string) {
        string = string.toLowerCase();
        if (string.length() < tree.getChunkSize()) {
            return probOfStringShorterThanChunkSize(string);
        }

        double overallProbability = 1;
        double prob;

        for (int i = 1; i <= string.length(); i++) {
            int startIndex = i - tree.getChunkSize() < 0 ? 0 : i - tree.getChunkSize();
            String substring = string.substring(startIndex, i);
            prob = conditionalProbabilityOfLastChar(substring);
            overallProbability *= prob;
            // System.out.format("Sub: %s Char: %c Prob: %f, overallProb: %f\n", substring, substring.charAt(substring.length() - 1), prob, overallProbability);
        }

        return overallProbability;
    }

    public double probOfStringByLogProduct(String string) {
        string = string.toLowerCase();

        double overallProbability = 0;
        for (int i = 0; i <= string.length() - tree.getChunkSize(); i++) {
            String substring = string.substring(i, i + tree.getChunkSize());
            double prob = probOfStringShorterThanChunkSize(substring);
            overallProbability += Math.log(prob);
        }

        return Math.exp(overallProbability);
    }

    public double probOfCharWithDefinedPrefix(String prefix, char c) {
        String complete = prefix + c;
        double completeProb = probOfStringShorterThanChunkSize(complete);
        double prefixProb = probOfStringShorterThanChunkSize(prefix);
        return completeProb / prefixProb;
    }

    public double conditionalProbabilityOfLastChar(String string) {
        // Only use the last n chars for the condition, with n = chunksize
        string = string.length() < tree.getChunkSize() ? string : string.substring(string.length() - tree.getChunkSize());
        CrawlerNode node = tree.getRoot();
        for (int i = 0; i < string.length() - 1; i++) {
            for (CrawlerNode child : node.getChildren()) {
                if (child.getData().getChar() == string.charAt(i)) {
                    node = child;
                    break;
                }
            }
        }
        return getProbOfCharOnLevel(node, string.charAt(string.length() - 1));
    }

    private double getProbOfCharOnLevel(CrawlerNode node, char c) {
        double levelcount = 0;
        if (node == null) {
            //@ToDo: this should return the smallest value for a probability that is possible!
            return 0.0;
        }
        ArrayList<CrawlerNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            throw new NullPointerException("node has no childs");
        }
        for (CrawlerNode crawlerNode : children) {
            levelcount += crawlerNode.getData().getCount();
        }
        //get appearance of char
        double appearanceOfChar = 0;
        for (CrawlerNode crawlerNode : children) {

            if (crawlerNode.getData().getChar() == c) {
                appearanceOfChar = crawlerNode.getData().getCount();
            }
        }

        return appearanceOfChar / levelcount;
    }

    private CrawlerNode getChildWithCharAsData(CrawlerNode node, char c) {

        for (CrawlerNode crawlerNode : node.getChildren()) {

            if (crawlerNode.getData().getChar() == c) {
                return crawlerNode;
            }
        }


        return null;
    }


    public double probOfPressedButtonAndChar(char c) {
        return probOfChar(c) * CORRECTBUTTONPROB;
    }


}
