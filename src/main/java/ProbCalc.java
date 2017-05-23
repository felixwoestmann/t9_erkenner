import java.util.ArrayList;

/**
 * Created by lostincoding on 23.05.17.
 */
public class ProbCalc {
    private CrawlerTree tree;

    public ProbCalc(CrawlerTree tree) {
        this.tree = tree;
    }


    public double probOfChar(char c) {
        return getProbOfCharOnLevel(tree.getRoot(), c);
    }

    public double probOfTwoChars(String chars) {
        if (chars.length() != 2) {
            return -1;
        }

        char c1 = chars.charAt(0);
        char c2 = chars.charAt(1);

        double c1prob = getProbOfCharOnLevel(tree.getRoot(), c1);
        CrawlerNode c1node = getChildWithCharAsData(tree.getRoot(), c2);
        double c2prob = getProbOfCharOnLevel(c1node, c2);

        return c1prob * c2prob;
    }

    public double probOfString(String string) {
        if(string.length()>tree.getChunkSize()) {
            throw new IllegalArgumentException("Length of String is too big");
        }


        char[] chars = string.toCharArray();

        double returnval = 1;

        CrawlerNode actNode = tree.getRoot();
        for (char c : chars) {
            double prob = getProbOfCharOnLevel(actNode, c);
            actNode = getChildWithCharAsData(actNode, c);
           returnval*=prob;
        }


        return returnval;
    }

    public double probOfCharWithDefinedPrefix(String prefix, char c) {
        return 0.0;
    }


    private double getProbOfCharOnLevel(CrawlerNode node, char c) {
        double levelcount = -1;
        ArrayList<CrawlerNode> children = node.getChildren();
        if (children == null) {
         throw new NullPointerException("node has no childs");
        }
        for (CrawlerNode crawlerNode : children) {
            levelcount += crawlerNode.getData().getCount();
        }
        //get appearance of char
        double appearanceOfChar = -1;
        for (CrawlerNode crawlerNode : children) {
            if (crawlerNode.getData().getData() == c) {
                appearanceOfChar = crawlerNode.getData().getCount();
            }
        }

        return appearanceOfChar / levelcount;
    }

    private CrawlerNode getChildWithCharAsData(CrawlerNode node, char c) {
        for (CrawlerNode crawlerNode : node.getChildren()) {
            if (crawlerNode.getData().getData() == c) {
                return crawlerNode;
            }
        }
        return null;
    }


}
