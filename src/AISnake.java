/**
 * Created by zywang on 3/5/2018.
 */


//Forward a random food position
public class AISnake extends Snake {
    private Food target;
    AISnake(){
        super();
        target=null;
    }
    AISnake(int width, int height){
        super(width,height);
        target=null;
    }

    public Food getTarget() {
        return target;
    }

    public void setTarget(Food target) {
        this.target = target;
    }
}
