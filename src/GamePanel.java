import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * Created by zywang on 3/5/2018.
 */
public class GamePanel extends JPanel implements MouseMotionListener,Runnable{
    public final static int WIDTH = 2000;
    public final static int HEIGHT = 2000;
    private Snake userSnake;
    private InfoManager infoManager;
    BufferedImage gameMap = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    //mouse location
    private Integer x;
    private Integer y;

    //slide window, save for mouse clicked
    public static int dx=0;
    public static int dy=0;

    public GamePanel(){
        userSnake = new Snake(WIDTH,HEIGHT);
        userSnake.setAI(false);
        infoManager = new InfoManager(WIDTH,HEIGHT);
        infoManager.init();
        infoManager.join(userSnake);

    }


    //use to
    @Override
    public void run() {
        while(infoManager.getCntDown()>0){
            infoManager.check();
            repaint();
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Color currentColor=getGraphics().getColor();
        getGraphics().setColor(Color.lightGray);
        int score=userSnake.getBody().size();
        getGraphics().clearRect(WIDTH/4-100,HEIGHT/4-50,200,100);
        getGraphics().setFont(new Font("TimesRoman", Font.PLAIN, 50));
        getGraphics().drawString("Final Length\t"+score,WIDTH/4-50,HEIGHT/4);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);


        Node head = userSnake.getBody().getFirst();
        double hx = head.getX();
        double hy = head.getY();

        //window size is half of whole map
        //let head on the center (so is 3/4)
        double ddx=0;
        double ddy=0;
        if(hx-WIDTH/4>0)
            ddx=hx-WIDTH/4;
        if(hy-HEIGHT/4>0)
            ddy=hy-HEIGHT/4;

        double maxX = WIDTH/2;
        double maxY = HEIGHT/2;


        dx = (int) (ddx>maxX?maxX:ddx);
        dy = (int) (ddy>maxY?maxY:ddy);
        Graphics2D tg = gameMap.createGraphics();
        infoManager.paint(tg);



        tg.dispose();

        g.drawImage(gameMap.getSubimage(dx, dy, WIDTH/2, HEIGHT/2), 0, 0, null);

        Font currentFont=g.getFont();
        Color currentColor=g.getColor();
        g.setColor(Color.lightGray);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString(String.format("%02d", (infoManager.getCntDown() / 60)) + ":" + String.format("%02d", (infoManager.getCntDown() % 60)), WIDTH/4 - 50, 50);
        g.setFont(currentFont);
        g.setColor(currentColor);


        g.dispose();

        if(x != null && y != null){
            userSnake.setDirection(x, y);
            x = null;
            y = null;
        }


    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX()+dx;
        y = e.getY()+dy;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }


}
