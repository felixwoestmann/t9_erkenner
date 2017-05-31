import java.io.File;
import java.io.IOException;

/**
 * Created by lostincoding on 09.05.17
 */
public class WikiDumpReader {


    public void processWikiDump(CrawlerTree tree, String path) {
        File wikidumpdir = new File(path);

        if (!wikidumpdir.isDirectory()) {
            throw new IllegalArgumentException("Error: Path has to be directory");
        }

        File[] dircontent = wikidumpdir.listFiles();
        int dircount = 0;
        for (File file : dircontent) {
            if (file.isDirectory()) {
                tree.processString(processDirectory(file));
                dircount++;
                System.out.println("Processed " + dircount + " directories of " + dircontent.length);
            }
        }

    }


    private String processDirectory(File file) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Error: Path has to be directory");
        }

        File[] dircontent = file.listFiles();
        for (File txt : dircontent) {
            if (!txt.isDirectory()) {
                return getTextFromFile(txt);
            }
        }

        return "";
    }


    private String getTextFromFile(File file) {
        String filecontent = "";
        try {
            filecontent = FileReader.readFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] articles = filecontent.split("</doc>");

        StringBuilder sb = new StringBuilder();

        for (String article : articles) {
            sb.append(extractTextFromArticle(article));
        }
        return sb.toString();
    }

    private String extractTextFromArticle(String article) {
        //remove the doc lines and unnecessary \n
        article = article.replaceAll("<doc.*>", "");
        article = article.replaceAll("\n+", "\n");
        article=article.toLowerCase();
        return article;
    }


}
