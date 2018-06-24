import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zywang on 1/5/2018.
 */
public class FoodManager implements Serializable{
    private List<Food> foods;
    private SecureRandom random;
    private Thread t;
    private String mode;
    private ArrayList<GameClient> players=null;
    int width;
    int height;

    int capacity;
    public FoodManager(int Width, int Height) {
        capacity=width*height/10000;
        foods = new ArrayList<>();
        random = new SecureRandom();
        width=Width;
        height=Height;
    }

    //init some food on the given area
    public void init() {
        int nFood = width * height / 10000;
        for (int i = 0; i < nFood; ++i)
            foods.add(new Food(random.nextDouble() * width, random.nextDouble() * height, random.nextInt(Food.DEFAULT_BONUS) + 1));
    }

    public void init(String mode) {
        this.mode=mode;
    }

    public void init(String mode,ArrayList<GameClient> players){
        this.players=players;
        this.mode=mode;
        for (int i = 0; i < 10; ++i)
            addFood(new Food(random.nextDouble() * width, random.nextDouble() * height, random.nextInt(Food.DEFAULT_BONUS) + 1));
    }

    public void product(int n) {
        for (int i = 0; i < n; ++i)
            addFood(new Food(random.nextDouble() * width, random.nextDouble() * height, random.nextInt(Food.DEFAULT_BONUS) + 1));
    }

    public void addFoods(List<Food> newfoods){
        for(Food food:newfoods)addFood(food);
    }
    //check if food ate by snake
    //only need to check head
    public void check(Snake snake) {
        Node head = snake.getHead();
        double hr = snake.getBodyWidth() / 2;
        double hx = head.getX() + hr;
        double hy = head.getY() + hr;
        for (int i = 0; i < foods.size(); i++) {
            Food food = foods.get(i);
            double fr = food.getSize() / 2;
            double fx = food.getX() + fr;
            double fy = food.getY() + fr;
            if ((hr + fr) > Math.abs(fx - hx) && (hr + fr) > Math.abs(fy - hy)) {
                snake.eat(food.getBonus());
                if(players!=null){
                    sendEAT(food,snake.getName());
                }
                removeFood(food);

                //check the number and add a food on map
                if(getFoodNumber()<capacity)
                    product(1);
            }
        }
    }
    public Food getRandomFood(){

        int i=random.nextInt(foods.size());
        return foods.get(i);
    }

    public boolean contains(Food food){
        return foods.contains(food);
    }
    public void paint(Graphics2D g){
        //for(Food food:foods)
          //  food.paint(g);
        for (int i = 0; i < foods.size(); ++i)
            foods.get(i).paint(g);
    }
    public int getFoodNumber(){
        return foods.size();
    }


    public Food getClosestFood(Snake snake){
        Node head=snake.getHead();
        double hx=head.getX();
        double hy=head.getY();
        Food choosen=null;
        double d=width*width+height*height;
        for(Food food:foods){
            double fx=food.getX();
            double fy=food.getY();
            double curDistance=(fx-hx)*(fx-hx)+(fy-hy)*(fy-hy);
            if(curDistance<d){
                d=curDistance;
                choosen=food;
            }
        }

        if(choosen==null){
            System.out.println("Random");
            choosen=getRandomFood();
        }
        return choosen;
    }

    public void addFood(Food food){
        foods.add(food);
        if(players!=null){
            sendFood(food,"ADD");
        }
    }
    public void removeFood(Food food){
        foods.remove(food);
        if(players!=null){
            sendFood(food,"REMOVE");
        }
    }

    public void sendFood(Food food,String op) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "FOOD");
            jsonObject.put("op", op);
            jsonObject.put("x", food.getX());
            jsonObject.put("y", food.getY());
            jsonObject.put("bonus", food.getBonus());
            //System.out.println(jsonObject.toString());
            for (GameClient p : players) {
                p.getOutput().write(jsonObject.toString() + "\n");
                p.getOutput().flush();
            }
        }catch (IOException e){}
    }

    public void sendEAT(Food food,String name) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "FOOD");
            jsonObject.put("op", "EAT");
            jsonObject.put("food", food.getBonus());
            jsonObject.put("name",name);
            //System.out.println(jsonObject.toString());
            for (GameClient p : players) {
                p.getOutput().write(jsonObject.toString() + "\n");
                p.getOutput().flush();
            }
        }catch (IOException e){}
    }

    public List<Food> getFoods() {
        return foods;
    }
}

