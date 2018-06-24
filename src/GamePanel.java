import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;


/**
 * Created by zywang on 3/5/2018.
 */
public class GamePanel extends JPanel implements MouseMotionListener,Runnable,KeyListener{
    public  int WIDTH = 2000;
    public  int HEIGHT = 2000;
    private Snake userSnake;
    private InfoManager infoManager;


    //reborn count down

    //private TimeManager reborn;

    //mouse location
    private Integer x;
    private Integer y;

    //slide window, save for mouse clicked
    public static int dx=0;
    public static int dy=0;

    public GamePanel(String username) {
        //reborn = null;
        userSnake = new Snake(WIDTH, HEIGHT);
        userSnake.setName(username);
        userSnake.setAI(false);
        infoManager = new InfoManager(WIDTH, HEIGHT);
        infoManager.init();
        infoManager.join(userSnake);
    }

    public GamePanel() {
        //reborn = null;
        userSnake = new Snake(WIDTH, HEIGHT);
        userSnake.setName("JAVA LAB");
        userSnake.setAI(false);
        infoManager = new InfoManager(WIDTH, HEIGHT);
        infoManager.init();
        infoManager.join(userSnake);
    }

    public GamePanel(Snake user,InfoManager info) {
        //reborn = null;
        userSnake = user;
        userSnake.setName("User");
        userSnake.setAI(false);
        infoManager =info;
    }


    //use to
    @Override
    public void run() {
        while(infoManager.getCntDown()>0){
            infoManager.check();
            repaint();

            //if die , add reborn thread
            /*
            if(!userSnake.isAlive()){
                if(reborn==null) {
                    reborn = new TimeManager(5);
                    reborn.start();
                }
                else if(reborn.getCntDown()==0){
                    userSnake.reborn(WIDTH,HEIGHT);
                    reborn=null;
                }
            }
            */
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
        if(!userSnake.isAlive() )score=0;
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
        BufferedImage gameMap = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D tg = gameMap.createGraphics();
        drawBackGround(tg);
        infoManager.paint(tg);



        tg.dispose();

        g.drawImage(gameMap.getSubimage(dx, dy, WIDTH/2, HEIGHT/2), 0, 0, null);

        Font currentFont=g.getFont();
        Color currentColor=g.getColor();
        g.setColor(Color.lightGray);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString(String.format("%02d", (infoManager.getCntDown() / 60)) + ":" + String.format("%02d", (infoManager.getCntDown() % 60)), WIDTH/4 - 50, 50);

        if(!userSnake.isAlive()){
            g.drawString(String.format("Wait\t%02d", (userSnake.getRebornCnt())), WIDTH/4 - 50, HEIGHT/4);
        }
        if(userSnake.isAlive()) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
            g.drawString("Length :" + String.format("%04d", (userSnake.getLength())), 0, 50);
        }

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

    @Override
    public void keyTyped(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE){


        }
    }


    @Override
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            userSnake.setSpeed(1);

        }
    }

    @Override
    public void keyPressed(KeyEvent e){
        //space to speedup
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            userSnake.setSpeed(2);

        }
        //direction control
        Node head=userSnake.getHead();
        double angle=head.getAngle();
        double s=Math.sin(angle);
        double c=Math.cos(angle);
        double update=2*Math.PI*10/360;
        if(e.getKeyCode()==KeyEvent.VK_UP||e.getKeyChar()=='w'){
            if(c>0)angle-=update;
            else angle+=update;

        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN||e.getKeyChar()=='s'){
            if(c>0)angle+=update;
            else angle-=update;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT||e.getKeyChar()=='a'){
            if(s>0)angle+=update;
            else angle-=update;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT||e.getKeyChar()=='d'){
            if(s>0)angle-=update;
            else angle+=update;
        }
        head.setAngle(angle);

    }

    public void drawBackGround(Graphics2D g2){
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,WIDTH,HEIGHT);
        g2.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(153,153,153));
        for (int i = 0; i < WIDTH; i += 100) {
           g2.drawLine(i,0,i,HEIGHT);
        }
        for (int i = 0; i < HEIGHT; i += 100) {
            g2.drawLine(0,i,WIDTH,i);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GamePanel gamePanel=new GamePanel();
        frame.add(gamePanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
