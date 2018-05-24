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
        int n = width*height/1000000*4;
        for (int i = 0; i < n; ++i)
            snakes.add(new AISnake(width, height,this));
    }


    public void addSnake(int n) {
        for (int i = 0; i < n; ++i)
            snakes.add(new Snake(width, height));
    }

    public List<Snake> getSnakes() {
        return snakes;
    }


    //for ai snake
    public void setDirection(FoodManager foodManager) {
        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            if (snake.isAI() && snake.isAlive()) {
                AISnake aiSnake = (AISnake) snake;
                Food target = aiSnake.getTarget();
                //System.out.println("Head at"+aiSnake.getHead().getX()+" "+aiSnake.getHead().getY());
                if(target==null || !foodManager.contains(target)) {
                    Food food = foodManager.getClosestFood(aiSnake);
                    //System.out.println("Target at" + aiSnake.getHead().getAngle());
                    //System.out.println("Head at"+aiSnake.getHead().getX()+" "+aiSnake.getHead().getY());
                    //System.out.println("Food is " + (int) food.getX() + " " + (int) (food.getY()));
                    aiSnake.setDirection((int) (food.getX()+food.getSize()/2), (int) (food.getY()+food.getSize()/2));
                    aiSnake.setTarget(food);
                    //System.out.println("Target at" + aiSnake.getHead().getAngle());
                    //System.out.println("Head at"+aiSnake.getHead().getX()+" "+aiSnake.getHead().getY());

                }

            }
        }
    }

    public void check(FoodManager foodManager) {
        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            if (snake.isAlive()) {
                Node head = snake.getHead();
                double hr = snake.getBodyWidth() / 2;
                double hx = head.getX() + hr;
                double hy = head.getY() + hr;
                for (int j = 0; j < snakes.size(); j++) {
                    if (j == i) continue;
                    Snake snake1 = snakes.get(j);
                    if (!snake1.isAlive()) continue;
                    double sr = snake1.getBodyWidth() / 2;
                    for (Node node : snake1.getBody()) {
                        double fx = node.getX();
                        double fy = node.getY();
                        if ((hr + sr)*(hr+sr) > (fx - hx)*(fx-hx)+(fy-hy)*(fy-hy) ) {
                            List<Food> newfs = snake.die();
                            foodManager.addFoods(newfs);
                            if (node == snake1.getHead()) {
                                List<Food> newfs1 = snake1.die();
                                foodManager.addFoods(newfs1);
                            }
                        }

                    }
                }
            }


        }
    }

    public void addSnake(Snake snake) {
        snakes.add(snake);
    }

    public void move(int n) {
        for (Snake snake : snakes)
            if (snake.isAlive()) {
                snake.move(n);
                Node head = snake.getHead();
                double hr = snake.getBodyWidth();
                if (head.getX() + hr / 2 > width || head.getY() + hr / 2 > height || head.getX() < hr / 2 || head.getY() < hr / 2)
                    snake.setAlive(false);
            }
    }

    public void paint(Graphics2D g) {
        for (Snake snake : snakes)
            if (snake.isAlive())
                snake.paint(g);
    }

    public void reborn() {
        for (int i = 0; i < snakes.size(); i++) {
            Snake snake = snakes.get(i);
            if (snake.isAI() && !snake.isAlive()) {
                AISnake aiSnake = (AISnake) (snake);
                aiSnake.reborn(width,height);

            }
        }
    }
}
