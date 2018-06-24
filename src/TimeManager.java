import java.io.Serializable;

/**
 * Created by zywang on 3/5/2018.
 */
public class TimeManager implements Runnable, Serializable {
    private Thread t;
    private int cntDown;

    public TimeManager(int times) {

        cntDown = times;
    }

    @Override
    public void run() {

        try {
            while (cntDown > 0) {
                cntDown--;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    public int getCntDown() {
        return cntDown;
    }
}

