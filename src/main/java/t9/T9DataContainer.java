package t9;

/**
 * Created by lostincoding on 09.06.17.
 */
public class T9DataContainer {
    private double probability=1;
    private String achar;

    public String getCharAsString() {
        return achar;
    }

    public char getChar() {
        return achar.charAt(0);
    }

    public void setAchar(String achar) {
        this.achar = achar;
    }

    public double getProbability() {

        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public T9DataContainer(double probability, String achar) {

        this.probability = probability;
        this.achar = achar;
    }

    public String toString() {
        return achar + " : " + probability;
    }
}
