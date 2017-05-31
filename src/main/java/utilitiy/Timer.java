package utilitiy;

/**
 * Created by lostincoding on 31.05.17.
 */
public class Timer {
    long start = -1;
    long stop = -1;

    public void start() {
        start = System.currentTimeMillis();
        stop = -1;
    }

    public void stop() {
        if (start == -1) {
            throw new IllegalStateException("You must start the timer before stopping it");
        }

        stop = System.currentTimeMillis();
    }


    public void printTime(TimeUnit unit) {
        validateStateForPrinting();
        System.out.print(convertToUnit(unit, stop - start) + "\n");
    }


    public void printTimeWithMessage(String message, TimeUnit unit) {
        validateStateForPrinting();
        System.out.print(message + " : ");
        printTime(unit);
    }

    private long convertToUnit(TimeUnit unit, long milliSeconds) {
        switch (unit) {
            case MILLISECONDS:
                return milliSeconds;

            case SECONDS:
                return milliSeconds / 1000;

            case MINUTES:
                return (milliSeconds / 1000) / 60;

            case HOURS:
                return ((milliSeconds / 1000) / 60) / 60;
            default:
                return milliSeconds;
        }
    }

    private void validateStateForPrinting() {
        if (start == -1 || stop == -1) {
            throw new IllegalStateException("You must stop the timer before printing the time");
        }
    }

}
