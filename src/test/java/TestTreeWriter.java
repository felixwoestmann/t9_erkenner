import crawler.CrawlerTree;
import crawler.TreeWriter;
import crawler.WikiDumpReader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * Created by lostincoding on 10.06.17.
 */
public class TestTreeWriter {

    @Test
    public void testWriteToFile() throws FileNotFoundException, UnsupportedEncodingException {
        CrawlerTree wikipediaTree = new CrawlerTree(5);
        WikiDumpReader.processWikiDump(wikipediaTree, "/home/lostincoding/Schreibtisch/wikidump-out/whole");

        TreeWriter writer = new TreeWriter(wikipediaTree);
        writer.writeToFile("/home/lostincoding/Schreibtisch/tree_test.json");
    }
}
