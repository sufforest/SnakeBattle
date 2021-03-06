/**
 * Created by zywang on 1/5/2018.
 */

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Snake implements Serializable {

    public static final int DEFAULT_BODY_LENGTH = 100;
    public static final int DEFAULT_BODY_WIDTH = 20;

    public static final int WIDTH = 2000;
    public static final int HEIGHT = 2000;


    private LinkedList<Node> body;
    private boolean isAlive;
    private boolean isAI;
    private int length;
    private double bodyWidth;
    private Skin skin;
    private SecureRandom r = new SecureRandom();
    private String name;
    private double speed = 1;
    private TimeManager rebornCnt;

    //initialization on the top-left
    public Snake() {
        this.isAlive = true;
        this.isAI = true;
        rebornCnt = null;
        name = null;
        skin = new Skin();
        length = DEFAULT_BODY_LENGTH;
        bodyWidth = DEFAULT_BODY_WIDTH;
        this.body = new LinkedList<Node>();
        int x = 0;
        int y = 0;
        for (int i = 1; i < length; i++) {
            body.add(new Node(x, y + i, 0));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getDefaultBodyLength() {
        return DEFAULT_BODY_LENGTH;
    }

    public static int getDefaultBodyWidth() {
        return DEFAULT_BODY_WIDTH;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public Skin getSkin() {
        return skin;
    }

    public SecureRandom getR() {
        return r;
    }

    //random initialization on map
    public Snake(int width, int height) {
        name = "UserSNake";
        this.isAlive = true;
        this.isAI = true;
        skin = new Skin();

        length = DEFAULT_BODY_LENGTH;
        bodyWidth = DEFAULT_BODY_WIDTH;
        this.body = new LinkedList<Node>();
        int x = r.nextInt(width);
        int y = r.nextInt(height);
        for (int i = 1; i < length; i++) {
            body.add(new Node(x, y + i, 0));
        }

    }

    public Snake(int width, int height,boolean isAI) {
        name = "UserSNake";
        this.isAlive = true;
        this.isAI = isAI;
        skin = new Skin();

        length = DEFAULT_BODY_LENGTH;
        bodyWidth = DEFAULT_BODY_WIDTH;
        this.body = new LinkedList<Node>();
        int x = r.nextInt(width);
        int y = r.nextInt(height);
        for (int i = 1; i < length; i++) {
            body.add(new Node(x, y + i, 0));
        }

    }

    //
    public Snake(int x,int y,boolean isAI,String name){
        this.isAlive = true;
        this.isAI = isAI;
        skin = new Skin();
        this.name=name;
        length = DEFAULT_BODY_LENGTH;
        bodyWidth = DEFAULT_BODY_WIDTH;
        this.body = new LinkedList<Node>();
        for (int i = 1; i < length; i++) {
            body.add(new Node(x, y + i, 0));
        }
    }

    //x y is the relative coord
    public void setDirection(int x, int y) {
        Node head = body.peekFirst();
        double curX = head.getX() + bodyWidth;
        double curY = head.getY() + bodyWidth;
        double dx = x - curX;
        double dy = y - curY;
        double angle = Math.atan2(dy, dx);
        head.setAngle(angle);
    }


    public void setDirection(Double angle) {
        double a = Math.atan2(angle, 1);
        getHead().setAngle(a);
    }


    //move n steps

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void move(int n) {

        if (speed > 1 && body.size() > DEFAULT_BODY_LENGTH) {
            n = (int) (n * speed);
        }
        while (n > 0) {

            if (speed > 1 && body.size() > DEFAULT_BODY_LENGTH) {
                body.removeLast();
                length--;
            }

            //no add on length
            if (length == body.size()) {
                body.removeLast();
            }

            Node newHead = new Node();
            Node curHead = body.getFirst();
            newHead.setX(curHead.getX() + Math.cos(curHead.getAngle()));
            newHead.setY(curHead.getY() + Math.sin(curHead.getAngle()));
            newHead.setAngle(curHead.getAngle());
            body.addFirst(newHead);
            n--;
        }
    }

    public void eat(int bonus) {
        length += bonus;

    }

    public Node getHead() {
        return body.peekFirst();
    }


    //paint
    //reverse order to avoid shadow
    public void paint(Graphics2D g) {
        for (int i = body.size() - 1; i >= 0; --i) {

            //only display certain nodes
            if (i % (bodyWidth) == 0 || i == body.size() - 1) {
                Node node = body.get(i);
                BufferedImage img;

                if (i == 0) {
                    img = skin.getHead();

                    //add name near head
                    if (getName() != null) {
                        g.setColor(Color.black);
                        g.setFont(new Font("TimesRoman", Font.BOLD, (int) getBodyWidth()));
                        g.drawString(getName(), (int) (getHead().getX() - getBodyWidth()), (int) (getHead().getY() - getBodyWidth() / 2));
                    }

                } else if (i == body.size() - 1) {
                    img = skin.getTail();
                } else {
                    img = skin.getBody();
                }

                //scale and transform
                AffineTransform affineTransform = new AffineTransform();
                affineTransform.setToTranslation(node.getX(), node.getY());
                affineTransform.scale(bodyWidth / img.getWidth(), bodyWidth / img.getHeight());
                affineTransform.rotate(node.getAngle(), img.getWidth() / 2, img.getHeight() / 2);
                g.drawImage(img, affineTransform, null);
            }

        }

    }

    public void reborn(int width, int height) {
        this.isAlive = true;
        length = DEFAULT_BODY_LENGTH;
        bodyWidth = DEFAULT_BODY_WIDTH;
        this.body = new LinkedList<Node>();
        int x = r.nextInt(width);
        int y = r.nextInt(height);
        for (int i = 1; i < length; i++) {
            body.add(new Node(x, y + i, 0));
        }
    }

    public void reborn(int x, int y,boolean fix) {
        this.isAlive = true;
        length = DEFAULT_BODY_LENGTH;
        bodyWidth = DEFAULT_BODY_WIDTH;
        if(fix) {
            this.body = new LinkedList<Node>();
            for (int i = 1; i < length; i++) {
                body.add(new Node(x, y + i, 0));
            }
        }
    }


    public int getRebornCnt(){
        if(rebornCnt !=null)
            return rebornCnt.getCntDown();
        return 0;
    }
    public boolean updateAlive() {
        if (!isAlive()) {
            if (rebornCnt == null) {
                rebornCnt = new TimeManager(5);
                rebornCnt.start();
            } else if (rebornCnt.getCntDown() == 0) {
                reborn(WIDTH, HEIGHT);
                rebornCnt = null;
                return true;
            }
        }
        return false;
    }

    public double getBodyWidth() {
        return bodyWidth;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public LinkedList<Node> getBody() {
        return body;
    }

    public List<Food> die() {
        setAlive(false);
        List<Food> foods = new ArrayList<>();

        for (int i = 0; i < body.size(); i = i + 2) {

            //only display certain nodes
            if (i % (bodyWidth) == 0 || i == body.size() - 1) {
                Node node = body.get(i);
                Food newfood = new Food(node.getX(), node.getY(), r.nextInt((int) (bodyWidth / 4)));
                foods.add(newfood);
            }
        }
        return foods;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}