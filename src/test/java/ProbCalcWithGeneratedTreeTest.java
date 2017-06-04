import org.junit.BeforeClass;
import org.junit.Test;

public class ProbCalcWithGeneratedTreeTest {
    private static ProbabilityCalculator c;

    @BeforeClass
    public static void setUp() {
        CrawlerTree tree = CrawlerTree.loadFromFile("./tree_3.json");
        c = new ProbabilityCalculator(tree);
    }

    @Test
    public void testGivenStringInWikiFileTree() {
        String s = "In Steinfurt regnet es fast nie";
        System.out.format("%s\n%.3g\n", s, c.probOfString(s));
    }
}
