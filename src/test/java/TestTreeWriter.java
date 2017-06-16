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


        CrawlerTree wikipediaTree = new CrawlerTree(3);
        WikiDumpReader.processWikiDump(wikipediaTree, "../wikidump-whole");

        TreeWriter writer = new TreeWriter(wikipediaTree);
        writer.writeToFile("./tree_3.json");

        wikipediaTree=new CrawlerTree(4);
        WikiDumpReader.processWikiDump(wikipediaTree, "../wikidump-whole");
        writer=new TreeWriter(wikipediaTree);
        writer.writeToFile("./tree_4.json");

        wikipediaTree=new CrawlerTree(5);
        WikiDumpReader.processWikiDump(wikipediaTree, "../wikidump-whole");
        writer=new TreeWriter(wikipediaTree);
        writer.writeToFile("./tree_5.json");

/*        TreeReader reader = new TreeReader();

        Timer timer = new Timer();
        timer.start();
        CrawlerTree readTree = reader.getTreeFromFile(persistpath);
        timer.stop();

        timer.printTimeWithMessage("Parsing tree with chunksize "+chunksize+" took seconds", TimeUnit.SECONDS);
        Assert.assertTrue(readTree.equals(wikipediaTree));*/
    }
}
