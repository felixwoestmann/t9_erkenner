package crawler;

/**
 * Created by lostincoding on 09.05.17.
 */
public class DataContainer {
    private char aChar;
    private int count;

    public DataContainer(char data) {
        this.setChar(data);
        this.count = 1;
    }

    public DataContainer(char data, int count) {
        this.setChar(data);
        this.count = count;
    }


    public char getChar() { return aChar; }

    public void setChar(char aChar) {
        this.aChar = aChar;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incrementCount() {
        setCount(getCount() + 1);
    }

    public String toString() {
        return aChar + ":" + count;
    }
}
