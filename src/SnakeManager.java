import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by zywang on 3/5/2018.
 */
public class SnakeManager {
    private List<Snake> snakes;
    private SecureRandom random;
    private int width;
    private int height;

    public SnakeManager(int Width, int Height) {
        snakes = new ArrayList<>();
        random = new SecureRandom();
        width = Width;
        height = Height;
    }

    //init some naive snake on the given area
    public void init() {
        int n = width * height / 1000000;
        for (int i = 0; i < n; ++i)
            snakes.add(new AISnake(width, height));
    }


    public void addSnake(int n) {
        for (int i = 0; i < n; ++i)
            snakes.add(new Snake(width, height));
    }

    public List<Snake> getSnakes() {
        return snakes;
    }

    public void setDirection(FoodManager foodManager) {
        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            if (snake.isAI() && snake.isAlive()){
                AISnake aiSnake=(AISnake)snake;
                Food target=aiSnake.getTarget();
                if(target==null || !foodManager.contains(target)){
                Food food=foodManager.getRandomFood();
                aiSnake.setDirection((int)food.getX(),(int)(food.getY()));
                aiSnake.setTarget(food);
            }
        }
    }}
    public void check() {
        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            if ((!snake.isAI()) && snake.isAlive()) {
                Node head = snake.getHead();
                double hr = snake.getBodyWidth() / 2;
                double hx = head.getX() + hr;
                double hy = head.getY() + hr;
                for (int j = 0; j < snakes.size() && j != i; j++) {
                    Snake snake1 = snakes.get(j);
                    if(!snake.isAlive()) continue;
                    double sr = snake1.getBodyWidth() / 2;
                    for (Node node : snake1.getBody()) {
                        double fx = node.getX();
                        double fy = node.getY();
                        if ((hr + sr) > Math.abs(fx - hx) && (hr + sr) > Math.abs(fy - hy)) {
                            snake.setAlive(false);
                            if (node == snake1.getHead())
                                snake1.setAlive(false);
                        }

                    }
                }
            }


        }
    }
    public void addSnake(Snake snake){
        snakes.add(snake);
    }

    public void move(int n){
        for(Snake snake:snakes)
            if(snake.isAlive())
            snake.move(n);
    }
    public void paint(Graphics2D g){
        for(Snake snake:snakes)
            if(snake.isAlive())
            snake.paint(g);
    }
}
