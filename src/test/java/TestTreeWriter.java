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

        for (int chunksize = 2; chunksize <= 6; chunksize++) {
            wikipediaTree = new CrawlerTree(chunksize);
            /*
             * wikidump-training is missing the following folders from the entire dump:
             * AE
             * AJ
             * AO
             * AT
             * AY
             * BD
             * BI
             * BN
             * BS
             */
            WikiDumpReader.processWikiDump(wikipediaTree, "../wikidump-training");

            writer = new TreeWriter(wikipediaTree);
            writer.writeToFile(String.format("./tree_%d.json", chunksize));
        }
    }
}
