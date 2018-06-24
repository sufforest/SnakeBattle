import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zywang on 3/5/2018.
 */
public class InfoManager implements Runnable,Serializable{
    private FoodManager foodManager;
    private SnakeManager snakeManager;
    private TimeManager timeManager;
    private int width;
    private int height;
    private String mode=null;
    private ArrayList<GameClient>players=null;
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

    public void init(String mode){
            foodManager.init(mode);
            snakeManager.init(mode);
            if(!mode.equals("Client")) {
                timeManager.start();
            }

    }

    public void init(String mode,ArrayList<GameClient> players){
        this.players=players;
        foodManager.init(mode,players);
        snakeManager.init(mode,players);
        timeManager.start();
        if(mode.equals("online")) {
            System.out.println("Game became active");
            new Thread(this).start();
        }
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

    public SnakeManager getSnakeManager() {
        return snakeManager;
    }

    @Override
    public void run() {
        while (getCntDown() > 0) {
            check();
            sendTime(getCntDown());
            if(players!=null){
                try {
                    for (GameClient p : players) {
                        JSONObject jmsg = new JSONObject();
                        jmsg.put("type", "time");
                        jmsg.put("cnt", getCntDown());
                        p.getOutput().write(jmsg.toString() + "\n");
                        p.getOutput().flush();
                    }
                }
                catch (IOException e){}
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    public void join(Snake snake){
        snakeManager.addSnake(snake);
    }

    public int getCntDown(){
       return timeManager.getCntDown();
    }


    public int getSnakeNumber(){
        return snakeManager.getSnakes().size();
    }

    public List<Snake> getSnakes(){
        return snakeManager.getSnakes();
    }
    public void paint(Graphics2D g){
        foodManager.paint(g);
        snakeManager.paint(g);

    }
    public void sendTime(int cntdown){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "time");
                jsonObject.put("cnt", cntdown);
                for (GameClient p : players) {
                    p.getOutput().write(jsonObject.toString() + "\n");
                    p.getOutput().flush();
                }
            } catch (IOException e) {
            }
    }

    public FoodManager getFoodManager() {
        return foodManager;
    }
}
