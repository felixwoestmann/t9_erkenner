/**
 * Created by lostincoding on 09.05.17.
 */
public class DataContainer {
    private char data;
    private int count;

    public DataContainer(char data) {
        this.setData(data);
        this.count=1;
    }
    public DataContainer(char data,int count) {
        this.setData(data);
        this.count=count;
    }


    public char getData() {
        return data;
    }

    public void setData(char data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incrementCount() {
        setCount(getCount()+1);
    }

    public String toString() {
        return data+":"+count;
    }
}
