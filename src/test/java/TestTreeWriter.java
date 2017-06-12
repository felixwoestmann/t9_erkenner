import crawler.CrawlerTree;
import crawler.TreeReader;
import crawler.TreeWriter;
import crawler.WikiDumpReader;
import org.junit.Assert;
import org.junit.Test;
import sun.reflect.generics.tree.Tree;
import utility.TimeUnit;
import utility.Timer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by lostincoding on 10.06.17.
 */
public class TestTreeWriter {

    @Test
    public void testWriteToFileForDifferentChunkSizes() throws IOException {
        CrawlerTree wikipediaTree;
        TreeWriter writer;

        for (int chunksize = 3; chunksize <= 5; chunksize++) {
            wikipediaTree = new CrawlerTree(chunksize);
            WikiDumpReader.processWikiDump(wikipediaTree, "../wikidump-whole");

            writer = new TreeWriter(wikipediaTree);
            writer.writeToFile(String.format("./tree_%d.json", chunksize));
        }
/*        TreeReader reader = new TreeReader();

        Timer timer = new Timer();
        timer.start();
        CrawlerTree readTree = reader.getTreeFromFile(persistpath);
        timer.stop();

        timer.printTimeWithMessage("Parsing tree with chunksize "+chunksize+" took seconds", TimeUnit.SECONDS);
        Assert.assertTrue(readTree.equals(wikipediaTree));*/
    }
}
