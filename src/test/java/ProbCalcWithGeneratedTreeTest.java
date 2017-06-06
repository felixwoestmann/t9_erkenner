import org.junit.Assert;
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
    public void testProbabilityForGivenStringLongerThanChunkSize() {
        String s;
        s = "wet";
        double probOfWet = c.probOfString(s);
        System.out.format("P(%s):  %g\n", s, probOfWet);
        String prefix = "et";
        char c = 't';
        double probOfTUnderPrefixET = ProbCalcWithGeneratedTreeTest.c.probOfCharWithDefinedPrefix(prefix, c);
        System.out.format("P(t|et): %g\n", probOfTUnderPrefixET);
        System.out.format("P(%s) * P(%c|%s):\n\t\t %g\n", s, c, prefix, probOfWet * probOfTUnderPrefixET);
        s = "wett";
        double probOfWett = ProbCalcWithGeneratedTreeTest.c.probOfString(s);
        System.out.format("P(%s): %g\n", s, probOfWett);
        Assert.assertEquals(probOfWet*probOfTUnderPrefixET, probOfWett, 1.0E-6);
    }

    @Test
    public void testGivenStringInWikiFileTree() {
        String s = "In Steinfurt regnet es fast nie";
        System.out.format("%s\n%.3g\n", s, c.probOfString(s));
        System.out.format("By Log calculation\n%.3g\n", c.probOfStringByLogProduct(s));
    }
}
