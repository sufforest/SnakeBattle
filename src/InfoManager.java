import java.awt.*;
import java.lang.annotation.Native;
import java.util.List;

/**
 * Created by zywang on 3/5/2018.
 */
public class InfoManager {
    private FoodManager foodManager;
    private SnakeManager snakeManager;
    private TimeManager timeManager;
    private int width;
    private int height;
    public InfoManager(int Width,int Height){
        width=Width;
        height=Height;
        foodManager=new FoodManager(width,height);
        snakeManager=new SnakeManager(width,height);
        timeManager=new TimeManager(300);

    }
    public void init(){
        foodManager.init();
        snakeManager.init();
        timeManager.start();
    }


    public void check() {

        if (timeManager.getCntDown() > 0) {
            List<Snake> snakes = snakeManager.getSnakes();
            for (int i = 0; i < snakes.size(); ++i)
                foodManager.check(snakes.get(i));
            snakeManager.check(foodManager);
            snakeManager.setDirection(foodManager);
            snakeManager.move(2);

            snakeManager.reborn();

        }
    }


    public void join(Snake snake){
        snakeManager.addSnake(snake);
    }

    public int getCntDown(){
        return timeManager.getCntDown();
    }

    public void paint(Graphics2D g){
        foodManager.paint(g);
        snakeManager.paint(g);



    }

}
