import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zywang on 1/5/2018.
 */
public class FoodManager {
    private List<Food> foods;
    private SecureRandom random;
    private Thread t;
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


    public void product(int n) {
        for (int i = 0; i < n; ++i)
            foods.add(new Food(random.nextDouble() * width, random.nextDouble() * height, random.nextInt(Food.DEFAULT_BONUS) + 1));
    }

    public void addFoods(List<Food> newfoods){
        foods.addAll(newfoods);
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
                foods.remove(i);

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
        for(Food food:foods)
            food.paint(g);
        //for (int i = 0; i < foods.size(); ++i)
            //foods.get(i).paint(g);
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
}

