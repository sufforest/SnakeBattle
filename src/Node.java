import java.io.Serializable;

/**
 * Created by zywang on 2/5/2018.
 */
public class Node implements Serializable{
    private double x;
    private double y;
    private double angle;

    public Node(){

    }
    public Node(Node node){
        x=node.getX();
        y=node.getY();
        angle=node.getAngle();

    }

    public void copy(Node node)
    {
        x=node.getX();
        y=node.getY();
        angle=node.getAngle();

    }

    public Node(double X, double Y,double Angle){
        x=X;
        y=Y;
        angle=Angle;
    }

    public double getAngle() {
        return angle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
