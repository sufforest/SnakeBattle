/**
 * Created by zywang on 1/5/2018.
 */

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;


public class Snake {

    public static final int DEFAULT_BODY_LENGTH = 50;
    public static final int DEFAULT_BODY_WIDTH = 10;

    private LinkedList<Node> body;
    private boolean isAlive;
    private boolean isAI;
    private int length;
    private double bodyWidth;
    private Skin skin;
    private SecureRandom r = new SecureRandom();


    //initialization on the top-left
    public Snake() {
        this.isAlive = true;
        this.isAI = true;
        skin=new Skin();
        length = DEFAULT_BODY_LENGTH;
        bodyWidth = DEFAULT_BODY_WIDTH;
        this.body = new LinkedList<Node>();
        int x = 0;
        int y = 0;
        for (int i = 1; i < length; i++) {
            body.add(new Node(x, y + i, 0));
        }
    }

    //random initialization on map
    public Snake(int width, int height) {
        this.isAlive = true;
        this.isAI = true;
        skin=new Skin();
        length = DEFAULT_BODY_LENGTH;
        bodyWidth = DEFAULT_BODY_WIDTH;
        this.body = new LinkedList<Node>();
        int x = r.nextInt(width);
        int y = r.nextInt(height);
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

    //move n steps
    public void move(int n) {
        while (n > 0) {

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
    public void paint(Graphics2D g) {
        for (int i = 0; i < body.size(); ++i) {

            //only display certain nodes
            if (i % (bodyWidth/3) == 0 || i == body.size() - 1) {
                Node node = body.get(i);
                BufferedImage img;

                if (i == 0) {
                    img = skin.getHead();
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
        this.isAI = false;
        length = DEFAULT_BODY_LENGTH;
        bodyWidth = DEFAULT_BODY_WIDTH;
        this.body = new LinkedList<Node>();
        int x = r.nextInt(width);
        int y = r.nextInt(height);
        for (int i = 1; i < length; i++) {
            body.add(new Node(x, y + i, 0));
        }
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
}