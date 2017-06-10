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
    public void testWriteToFile() throws IOException {
        String persistpath = "/home/lostincoding/Schreibtisch/tree_test.json";
        int chunksize=5;

        CrawlerTree wikipediaTree = new CrawlerTree(chunksize);
        WikiDumpReader.processWikiDump(wikipediaTree, "/home/lostincoding/Schreibtisch/wikidump-out/whole");

        TreeWriter writer = new TreeWriter(wikipediaTree);
        writer.writeToFile(persistpath);

        TreeReader reader = new TreeReader();

        Timer timer = new Timer();
        timer.start();
        CrawlerTree readTree = reader.getTreeFromFile(persistpath);
        timer.stop();

        timer.printTimeWithMessage("Parsing tree with chunksize "+chunksize+" took seconds", TimeUnit.SECONDS);
        Assert.assertTrue(readTree.equals(wikipediaTree));
    }
}
