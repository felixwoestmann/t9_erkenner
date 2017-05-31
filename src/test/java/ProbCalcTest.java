import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utilitiy.FileReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lostincoding on 23.05.17.
 */
public class ProbCalcTest {
    private CrawlerTree tree;
    private ProbabilityCalculator probCalc;
    private char c1, c2, c3, c4;
    private double r1, r2, r3, r4;

    @Before
    public void setUp() {
        int chunksize = 6;
        String testFilePath = "./test_text.txt";


        tree = new CrawlerTree(chunksize);
        String filecontent = "";
        try {

            filecontent = FileReader.readFile(new File(testFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        filecontent = filecontent.replaceAll("\n+", "\n");
        tree.processString(filecontent);
        probCalc = new ProbabilityCalculator(tree);
        System.out.format("Erzeuge Baum mit %d großen Chunks. \nLade Datei %s. \n\n", chunksize, testFilePath);
    }

    @Test
    public void testProbOfChar() {

        c1 = 'a';
        c2 = 'b';
        c3 = 'e';
        c4 = 'x';


        r1 = probCalc.probOfChar(c1);
        r2 = probCalc.probOfChar(c2);
        r3 = probCalc.probOfChar(c3);
        r4 = probCalc.probOfChar(c4);

        System.out.format("Die Wahrscheinlichkeit für das Auftreten des chars %c ist %f\n", c1, r1);
        System.out.format("Die Wahrscheinlichkeit für das Auftreten des chars %c ist %f\n", c2, r2);
        System.out.format("Die Wahrscheinlichkeit für das Auftreten des chars %c ist %f\n", c3, r3);
        System.out.format("Die Wahrscheinlichkeit für das Auftreten des chars %c ist %f\n", c4, r4);
    }

    @Test
    public void testProbOfTwoChars() {

        c1 = 'a';
        c2 = 'b';
        c3 = 'e';
        c4 = 'x';

        r1 = probCalc.probOfTwoChars(c1, c2);
        r2 = probCalc.probOfTwoChars(c2, c3);
        r3 = probCalc.probOfTwoChars(c3, c4);
        r4 = probCalc.probOfTwoChars(c1, c4);

        System.out.format("Die Wahrscheinlichkeit für das Auftreten der chars %c %c ist %f\n", c1, c2, r1);
        System.out.format("Die Wahrscheinlichkeit für das Auftreten der chars %c %c ist %f\n", c2, c3, r2);
        System.out.format("Die Wahrscheinlichkeit für das Auftreten der chars %c %c ist %f\n", c3, c4, r3);
        System.out.format("Die Wahrscheinlichkeit für das Auftreten der chars %c %c ist %f\n", c1, c4, r4);
    }

    @Test
    public void testProbOfStringWithProbOfTwoChars() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("th");
        strings.add("ab");
        strings.add("fe");
        strings.add("ef");
        strings.add("rt");
        strings.add("hr");

        for (String string : strings) {
            double twoCharProb = probCalc.probOfTwoChars(string.charAt(0), string.charAt(1));
            double stringProb = probCalc.probOfString(string);
            System.out.format("Probability for %s is with twoChar Method %f and with string method %f.\n", string, twoCharProb, stringProb);
            Assert.assertEquals(twoCharProb, stringProb, 0.0005);
        }
    }

    @Test
    public void testProbOfCharWithDefinedPrefix() {
        String prefix = "cybe";
        char s1 = 'r';
        char s2 = 'l';
        char s3 = 'h';
        r1 = probCalc.probOfCharWithDefinedPrefix(prefix, s1);
        r2 = probCalc.probOfCharWithDefinedPrefix(prefix, s2);
        r3 = probCalc.probOfCharWithDefinedPrefix(prefix, s3);

        System.out.format("Probability for prefix %s with suffix %c is %f.\n", prefix, s1,r1);
        System.out.format("Probability for prefix %s with suffix %c is %f.\n", prefix, s2,r2);
        System.out.format("Probability for prefix %s with suffix %c is %f.\n", prefix, s3,r3);

    }

}
