import crawler.CrawlerTree;
import crawler.TreeReader;
import crawler.WikiDumpReader;
import org.junit.Assert;
import org.junit.Test;
import utility.TimeUnit;
import utility.Timer;

import java.io.IOException;

/**
 * Created by mh on 12.06.17.
 */
public class TestTreeReader {

    @Test
    public void testTreeReadingFromFile() throws IOException {
        TreeReader reader = new TreeReader();

        Timer timer = new Timer();
        timer.start();
        CrawlerTree readTree = reader.getTreeFromFile("./tree_5.json");
        timer.stop();
        int chunksize = readTree.getChunkSize();

        timer.printTimeWithMessage("Parsing tree with chunksize " + chunksize + " took seconds", TimeUnit.SECONDS);
    }

    @Test
    public void testCompareTreeReadingFromFile() throws IOException {
        CrawlerTree generatedTree = new CrawlerTree(3);
        WikiDumpReader.processWikiDump(generatedTree, "../wikidump-whole");

        TreeReader reader = new TreeReader();
        CrawlerTree readTree = reader.getTreeFromFile("./tree_3.json");

        Assert.assertTrue(readTree.equals(generatedTree));
    }
}
