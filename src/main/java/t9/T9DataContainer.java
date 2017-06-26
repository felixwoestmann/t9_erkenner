package t9;

/**
 * Created by lostincoding on 09.06.17.
 */
public class T9DataContainer {
    private double probability = 0;
    private String achar;
    private boolean active;


    public String getCharAsString() {
        return achar;
    }

    public char getChar() {
        return achar.charAt(0);
    }

    public void setAchar(String achar) {
        this.achar = achar;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getProbability() {

        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public T9DataContainer(String achar) {

        this.probability = 0;
        this.achar = achar;
        this.active = true;
    }
    public T9DataContainer(double probability, String achar) {

        this.probability = probability;
        this.achar = achar;
        this.active = true;
    }

    public String toString() {
        return achar + " : " + probability;
    }
}
