package utility;

import java.io.File;
import java.io.IOException;

/**
 * Created by lostincoding on 10.05.17.
 */
public class FileReader {

    public static String readFile(String path) throws IOException {
        String content = null;
        File file = new File(path); //for ex foo.txt
        java.io.FileReader reader = null;
        try {
            reader = new java.io.FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return content;
    }


    public static String readFile(File file) throws IOException {
        return readFile(file.getAbsolutePath());
    }
}
