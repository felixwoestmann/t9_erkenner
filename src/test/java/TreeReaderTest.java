import org.junit.Assert;
import org.junit.Test;
import utility.TimeUnit;
import utility.Timer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by lostincoding on 31.05.17.
 */
public class TreeReaderTest {


    @Test
    public void testTimeConsumptionLoadingOfChunk3Tree() throws IOException {
        TreeReader reader = new TreeReader();
        Timer timer = new Timer();
        timer.start();
        CrawlerTree tree = reader.getTreeFromFile("tree_3.json");
        timer.stop();
        timer.printTimeWithMessage("Das Laden eines WikiBaumes mit der Chunk Größe 3\nSekunden:", TimeUnit.SECONDS);
    }

    @Test
    public void testTimeConsumptionLoadingOfChunk4Tree() throws IOException {
        TreeReader reader = new TreeReader();
        Timer timer = new Timer();
        timer.start();
        CrawlerTree tree = reader.getTreeFromFile("tree_4.json");
        timer.stop();
        timer.printTimeWithMessage("Das Laden eines WikiBaumes mit der Chunk Größe 4\nSekunden:", TimeUnit.SECONDS);
    }

    @Test
    public void testTimeConsumptionLoadingOfChunk5Tree() throws IOException {
        TreeReader reader = new TreeReader();
        Timer timer = new Timer();
        timer.start();
        CrawlerTree tree = reader.getTreeFromFile("tree_5.json");
        timer.stop();
        timer.printTimeWithMessage("Das Laden eines WikiBaumes mit der Chunk Größe 5\nSekunden:", TimeUnit.SECONDS);
    }


    @Test
    public void testAccuracyOfLoadingTree() throws IOException {

        String path = "test.json";

        CrawlerTree wikipediaTree = new CrawlerTree(3);
        WikiDumpReader.processWikiDump(wikipediaTree, "../wikidump/");

        wikipediaTree.printTree();
        TreeWriter writer = new TreeWriter(wikipediaTree);
        try {
            writer.writeToFile(path);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Persisting finished.");
        TreeReader reader = new TreeReader();

        CrawlerTree tree = reader.getTreeFromFile(path);
        Assert.assertTrue(wikipediaTree.equals(tree));
    }

    @Test
    public void compareReadToProcessSpeed() throws IOException {
        Timer processTimer = new Timer();
        Timer readTimer = new Timer();
        String path = "test.json";
        int chunksize = 5;
        CrawlerTree wikipediaTree = new CrawlerTree(chunksize);
        processTimer.start();
        WikiDumpReader.processWikiDump(wikipediaTree, "../wikidump-whole");
        processTimer.stop();
        System.out.println("Persist Tree on HardDrive. Directory: " + path);
        TreeWriter writer = new TreeWriter(wikipediaTree);
        try {
            writer.writeToFile(path);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Persisting finished.\n\n\n\n\n");
        TreeReader reader = new TreeReader();

        readTimer.start();
        CrawlerTree tree = reader.getTreeFromFile(path);
        readTimer.stop();

        System.out.format("Read File took %d seconds for chunksize %d\n", readTimer.getTime(TimeUnit.SECONDS), chunksize);
        System.out.format("Process String took %d seconds for chunksize %d\n", processTimer.getTime(TimeUnit.SECONDS), chunksize);

        long processTime = processTimer.getTime(TimeUnit.SECONDS);
        long readTime = readTimer.getTime(TimeUnit.SECONDS);

        if (readTime == processTime) {
            System.out.println("Both methods took the same amount of time.");
        }

        if (readTime > processTime) {
            System.out.println("Processing the string was " + (readTime - processTime) + " seconds faster.");
        } else {
            System.out.println("Reading the file was " + (processTime - readTime) + " seconds faster.");
        }

    }
}
