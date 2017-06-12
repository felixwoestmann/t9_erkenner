import crawler.CrawlerTree;
import crawler.TreeWriter;
import crawler.WikiDumpReader;
import org.junit.Test;

import java.io.IOException;

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
    }
}
