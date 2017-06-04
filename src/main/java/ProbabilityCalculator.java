import java.util.ArrayList;

/**
 * Created by lostincoding on 23.05.17.
 */
public class ProbabilityCalculator {
    private CrawlerTree tree;

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

    public double probOfStringSmallerThanChunkSize(String string) {
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

    public double probOfCharWithDefinedPrefix(String prefix, char c) {
        String complete = prefix + c;
        double completeProb = probOfStringSmallerThanChunkSize(complete);
        double prefixProb = probOfStringSmallerThanChunkSize(prefix);
        return completeProb / prefixProb;
    }


    private double getProbOfCharOnLevel(CrawlerNode node, char c) {
        double levelcount = 0;
        ArrayList<CrawlerNode> children = node.getChildren();
        if (children == null) {
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


}
