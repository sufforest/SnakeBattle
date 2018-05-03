import java.awt.*;

/**
 * Created by zywang on 1/5/2018.
 */
public class Food {

    public static final int DEFAULT_BONUS=10;

    private double x;
    private double y;

    //bonus decide the addup of the length of the snake
    private int bonus;

    //food size
    private int size;

    public Food(double X,double Y,int Bonus){
        x=X;
        y=Y;
        bonus=Bonus;

        //simple assignment
        size=bonus;
    }

    public int getSize() {
        return size;
    }
    public int getBonus(){
        return bonus;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void paint(Graphics2D g){
        Color tmp=g.getColor();
        g.setColor(Color.RED);
        g.fillOval((int)x,(int)y,size,size);
        g.setColor(tmp);
    }
}
