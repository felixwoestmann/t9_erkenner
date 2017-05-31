import org.junit.BeforeClass;
import org.junit.Test;
import utilitiy.TimeUnit;
import utilitiy.Timer;

import java.io.IOException;

/**
 * Created by lostincoding on 31.05.17.
 */
public class TreeReaderTest {


    @Test
    public void testLoadingOfChunk3Tree() throws IOException {
        TreeReader reader=new TreeReader();
        Timer timer=new Timer();
        timer.start();
        CrawlerTree tree=reader.getTreeFromFile("tree_3.json");
        timer.stop();
        timer.printTimeWithMessage("Das Laden eines WikiBaumes mit der Chunk Größe 3\nSekunden:",TimeUnit.SECONDS);
    }
    @Test
    public void testLoadingOfChunk4Tree() throws IOException {
        TreeReader reader=new TreeReader();
        Timer timer=new Timer();
        timer.start();
        CrawlerTree tree=reader.getTreeFromFile("tree_4.json");
        timer.stop();
        timer.printTimeWithMessage("Das Laden eines WikiBaumes mit der Chunk Größe 4\nSekunden:",TimeUnit.SECONDS);
    }
    @Test
    public void testLoadingOfChunk5Tree() throws IOException {
        TreeReader reader=new TreeReader();
        Timer timer=new Timer();
        timer.start();
        CrawlerTree tree=reader.getTreeFromFile("tree_5.json");
        timer.stop();
        timer.printTimeWithMessage("Das Laden eines WikiBaumes mit der Chunk Größe 5\nSekunden:",TimeUnit.MILLISECONDS);
    }
}
