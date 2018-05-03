import java.util.LinkedList;

/**
 * Created by zywang on 3/5/2018.
 */


//Forward a random food position
public class AISnake extends Snake {
    private Food target;
    public static int MaxLength=400;
    private int depart;
    AISnake(){
        super();
        target=null;
        depart=0;
    }
    AISnake(int width, int height){
        super(width,height);
        target=null;
        depart=0;

    }

    public Food getTarget() {
        return target;
    }

    public void setTarget(Food target) {
        this.target = target;
    }

    public void reborn(int width, int height) {
        super.reborn(width,height);
        System.out.println("AI Reborn");
        target=null;

    }

    @Override
    public void move(int n) {
        if(target!=null){
            double hx=getHead().getX();
            double hy=getHead().getY();
            double fx=target.getX();
            double fy=target.getY();
            double lastDistance=(fx-hx)*(fx-hx)+(fy-hy)*(fy-hy);
            super.move(n);
            hx=getHead().getX();
            hy=getHead().getY();
            double curDistance=(fx-hx)*(fx-hx)+(fy-hy)*(fy-hy);
            if(curDistance>lastDistance){
                depart++;
            }

            if(depart>=100) {
                target = null;
                depart=0;
            }

        }
        else {
            super.move(n);
        }
    }


}
