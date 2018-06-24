import org.json.JSONObject;

import javax.sound.sampled.Line;
import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zywang on 23/6/2018.
 */
public class PlayPanel extends JPanel implements MouseMotionListener, Runnable {
    public static int WIDTH = 2000;
    public static int HEIGHT = 2000;
    private Snake userSnake;
    private String username=null;
    private InfoManager infoManager=null;
    public  int cntDown=5*60;
    BufferedWriter out=null;

    //mouse location
    private Integer x;
    private Integer y;


    //slide window, save for mouse clicked
    public static int dx = 0;
    public static int dy = 0;



    public PlayPanel(String username, BufferedWriter out) {
        this.out=out;
        this.username=username;
        infoManager=new InfoManager(WIDTH,HEIGHT);
        infoManager.init("Client");

    }
    public PlayPanel(InfoManager infoManager,String username, BufferedWriter out) {
        this.out=out;
        this.username=username;
        for(Snake snake:infoManager.getSnakes()){
            System.out.println("DEBUG "+snake.getName());
            if(snake.getName().equals(username)) {
                userSnake = snake;
                System.out.println("SET UserSnake");
                break;
            }
        }
        this.infoManager=infoManager;


    }
    public void setUserSnake(Snake userSnake){
        this.userSnake=userSnake;
    }


    //use to
    @Override
    public void run() {
        while (cntDown > 0) {

            repaint();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        getGraphics().setColor(Color.lightGray);
        int score = userSnake.getBody().size();
        if (!userSnake.isAlive()) score = 0;
        getGraphics().clearRect(WIDTH / 4 - 100, HEIGHT / 4 - 50, 200, 100);
        getGraphics().setFont(new Font("TimesRoman", Font.PLAIN, 50));
        getGraphics().drawString("Final Length\t" + score, WIDTH / 4 - 50, HEIGHT / 4);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);



        Node head = userSnake.getBody().getFirst();
        double hx = head.getX();
        double hy = head.getY();

        double ddx = 0;
        double ddy = 0;
        if (hx - WIDTH / 4 > 0)
            ddx = hx - WIDTH / 4;
        if (hy - HEIGHT / 4 > 0)
            ddy = hy - HEIGHT / 4;

        double maxX = WIDTH / 2;
        double maxY = HEIGHT / 2;


        dx = (int) (ddx > maxX ? maxX : ddx);
        dy = (int) (ddy > maxY ? maxY : ddy);
        BufferedImage gameMap = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D tg = gameMap.createGraphics();
        drawBackGround(tg);
        infoManager.paint(tg);


        tg.dispose();

        g.drawImage(gameMap.getSubimage(dx, dy, WIDTH / 2, HEIGHT / 2), 0, 0, null);

        Font currentFont = g.getFont();
        Color currentColor = g.getColor();
        g.setColor(Color.lightGray);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString(String.format("%02d", (cntDown / 60)) + ":" + String.format("%02d", (cntDown % 60)), WIDTH / 4 - 50, 50);

        if (!userSnake.isAlive()) {
            g.drawString(String.format("Wait\t%02d", (userSnake.getRebornCnt())), WIDTH / 4 - 50, HEIGHT / 4);
        }
        if (userSnake.isAlive()) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
            g.drawString("Length :" + String.format("%04d", (userSnake.getLength())), 0, 50);
        }
        g.setFont(currentFont);
        g.setColor(currentColor);
        g.dispose();

        if (x != null && y != null) {
            userSnake.setDirection(x, y);
            double angle=userSnake.getHead().getAngle();
            sendDirection(angle);
            x = null;
            y = null;
        }


    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX() + dx;
        y = e.getY() + dy;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }



    public void sendDirection(double angle){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "DIRECTION");
            jsonObject.put("angle", angle);
            out.write(jsonObject.toString()+"\n");
            out.flush();
        }
        catch (IOException e){}
    }




    public void drawBackGround(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(153, 153, 153));
        for (int i = 0; i < WIDTH; i += 100) {
            g2.drawLine(i, 0, i, HEIGHT);
        }
        for (int i = 0; i < HEIGHT; i += 100) {
            g2.drawLine(0, i, WIDTH, i);
        }
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame();
        InfoManager infoManager=new InfoManager(WIDTH,HEIGHT);
        java.util.List<Snake> snakeList=infoManager.getSnakes();
        Snake snake=new Snake(2000,2000);
        snakeList.add(snake);
        PlayPanel playPanel=new PlayPanel(infoManager,snake.getName(),null);
        frame.add(playPanel);
       // playPanel.setBounds(0, 30, 1000, 1000);
        frame.setSize(1000, 1000 + 30);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    public Snake getUserSnake() {
        return userSnake;
    }

    public void SendReBorn(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "SNAKE");
            jsonObject.put("op", "reborn");
            jsonObject.put("x", userSnake.getHead().getX());
            jsonObject.put("y", userSnake.getHead().getY());
            jsonObject.put("name", userSnake.getName());
            out.write(jsonObject.toString() + "\n");
        }
        catch (IOException e){}
    }
    }

