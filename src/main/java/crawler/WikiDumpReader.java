package crawler;

import crawler.CrawlerTree;
import utility.FileReader;
import utility.TimeUnit;
import utility.Timer;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by lostincoding on 09.05.17
 */
public class WikiDumpReader {


    public static void processWikiDump(CrawlerTree tree, String path) {
        Timer timer = new Timer();
        System.out.println("Start processing wiki dump");

        timer.start();
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

        timer.stop();
        System.out.println("Processing wiki dump took " + timer.getTime(TimeUnit.SECONDS) + " seconds");
    }

    /**
     * Returns a list of words from a wiki dir
     * remove all space,.üäö
     * ans turns it to lower case
     * @param path
     * @return
     */

    public static LinkedList<String> getWords(String path) {
        File wikidumpdir = new File(path);
        LinkedList<String> list=new LinkedList<>();


        if (!wikidumpdir.isDirectory()) {
            throw new IllegalArgumentException("Error: Path has to be directory");
        }

        File[] dircontent = wikidumpdir.listFiles();
        assert dircontent != null;
        for (File file : dircontent) {
            if (file.isDirectory()) {
                for (String s : processDirectory(file).toLowerCase().split(" ")) {
                    s=s.trim();
                    boolean containsinvalidchars=false;

                    for (char c : s.toCharArray()) {
                        if (!(c >= '0' && c <= '9') && !(c >= 'a' && c <= 'z') ){
                            containsinvalidchars=true;
                            break;
                        }
                    }

                    if (!containsinvalidchars) {
                        if (!list.contains(s)) {
                            list.add(s);
                        }

                    }
                }

            }
        }
        return list;

    }


    private static String processDirectory(File file) {
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


    private static String getTextFromFile(File file) {
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

    private static String extractTextFromArticle(String article) {
        //remove the doc lines and unnecessary \n
        article = article.replaceAll("<doc.*>", "");
        article = article.replaceAll("\n+", "\n");
        article = article.toLowerCase();
        return article;
    }

    private static long milliSecondsToSecond(long millis) {
        return millis / 1000;
    }

}
