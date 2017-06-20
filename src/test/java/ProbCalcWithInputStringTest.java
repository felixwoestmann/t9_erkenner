import crawler.CrawlerTree;
import crawler.ProbabilityCalculator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProbCalcWithInputStringTest {
    public static final double DELTA = 1E-8;
    private int chunksize = 3;
    private CrawlerTree t = new CrawlerTree(chunksize);
    private String input;
    private ProbabilityCalculator c;

    @Before
    public void setUp() {
        input = "Stanleys Expeditionszug quer durch Afrika wird von jedermann bewundert";

        t.processString(input);
        this.c = new ProbabilityCalculator(t);

        System.out.format("Erzeuge Baum mit %d großen Chunks aus folgendem Text:\n%s\n\n", chunksize, input);
//        t.printTree();
    }

    @Test
    public void testProbabilityOfSingleChar() {
        double delta = 1E-6;
        Assert.assertEquals("Probability of 'a' calculated wrong", ((double) 4 / 70), c.probOfChar('a'), delta);
        Assert.assertEquals("Probability of 'q' calculated wrong", ((double) 1 / 70), c.probOfChar('q'), delta);
        Assert.assertEquals("Probability of 'z' calculated wrong", ((double) 1 / 70), c.probOfChar('z'), delta);
    }

    @Test
    public void testProbabilityOfDoubleChar() {
        Assert.assertEquals("Probability of 'an' calculated wrong", (double) 2 / 4 * (4 / (double) 70), c.probOfTwoChars('a', 'n'), DELTA);
        Assert.assertEquals("Probability of 'er' calculated wrong", (double) 3 / 8 * (8 / (double) 70), c.probOfTwoChars('e', 'r'), DELTA);
        Assert.assertEquals("Probability of 'qu' calculated wrong", (double) 1 / 1 * (1 / (double) 70), c.probOfTwoChars('q', 'u'), DELTA);
    }

    @Test
    public void testProbabilityOfStringShorterThanChunkSize() {
        Assert.assertTrue("Chunksize too small", 2 <= t.getChunkSize());
        Assert.assertEquals("Probability of \"an\" calculated wrong", (double) 2 / 4 * (4 / (double) 70), c.probabilityOfString("an"), DELTA);
        Assert.assertEquals("Probability of \"er\" calculated wrong", (double) 3 / 8 * (8 / (double) 70), c.probabilityOfString("er"), DELTA);
        Assert.assertEquals("Probability of \"qu\" calculated wrong", (double) 1 / 1 * (1 / (double) 70), c.probabilityOfString("qu"), DELTA);
    }

    @Test
    public void testProbabilityOfString() {
        double p_a = (4 / 70.0);
        double p_an = (2 / 4.0);
        double p_anl = (1 / 2.0);
        double p_nle = (1.0);
        Assert.assertEquals(p_a * p_an * p_anl * p_nle, c.probabilityOfString("anle"), DELTA);
    }
}
