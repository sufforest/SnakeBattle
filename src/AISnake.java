import java.util.LinkedList;
import java.util.List;

/**
 * Created by zywang on 3/5/2018.
 */


//Forward a random food position
public class AISnake extends Snake {
    private Food target;
    public static int MaxLength = 400;
    public static int MAX_ESCAPE = 10;
    private int depart;

    private Double escape_angle = null;
    private Integer escape_times = null;

    private SnakeManager context;

    AISnake() {
        super();
        target = null;
        depart = 0;
    }

    AISnake(int width, int height, SnakeManager snakeManager) {
        super(width, height);
        target = null;
        depart = 0;
        context = snakeManager;

    }

    public Food getTarget() {
        return target;
    }

    public void setTarget(Food target) {
        this.target = target;
    }

    public void reborn(int width, int height) {
        super.reborn(width, height);
        System.out.println("AI Reborn");
        target = null;

    }

    //return angle
    public Double escape(int n) {
        Node head = getHead();
        List<Snake> snakes = context.getSnakes();
        double hr = getBodyWidth() / 2;
        double hx = head.getX() + hr * n;
        double hy = head.getY() + hr * n;
        for (int j = 0; j < snakes.size(); j++) {
            Snake snake1 = snakes.get(j);
            if (snake1 == this) continue;
            if (!snake1.isAlive()) continue;
            double sr = snake1.getBodyWidth() / 2;
            for (Node node : snake1.getBody()) {
                double fx = node.getX();
                double fy = node.getY();

                //escape
                if ((hr + sr) * (hr + sr) > (fx - hx) * (fx - hx) + (fy - hy) * (fy - hy)) {
                    return (hy - fy) / (hx - fx);
                }
            }
        }
        return null;
    }


    @Override
    public void move(int n) {
        if (target != null) {


            double hx = getHead().getX();
            double hy = getHead().getY();
            double fx = target.getX();
            double fy = target.getY();
            double lastDistance = (fx - hx) * (fx - hx) + (fy - hy) * (fy - hy);


            //if near, speed up
            if (lastDistance < getLength() * getBodyWidth()) {
                setSpeed(2);
            } else {
                setSpeed(1);
            }

            //escape
            if (escape_times == null || escape_times == 0) {
                if (escape_times != null) {
                    setDirection((int) (target.getX()), (int) (target.getY()));
                    escape_times = null;
                }
                else {
                    Double angle = escape(n);
                    if (angle != null) {
                        setDirection(angle);
                        escape_times = MAX_ESCAPE;
                    }
                }
            }
            else {
                escape_times--;
            }
            super.move(n);
            hx = getHead().getX();
            hy = getHead().getY();
            double curDistance = (fx - hx) * (fx - hx) + (fy - hy) * (fy - hy);
            if (curDistance > lastDistance) {
                depart++;
            }

            if (depart >= 100) {
                target = null;
                depart = 0;
            }

        } else {
            setSpeed(1);
            super.move(n);
        }
    }

}
