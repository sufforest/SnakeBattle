

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;

/**
 * Created by zywang on 3/5/2018.
 */
public class Skin {
    public BufferedImage head;
    public BufferedImage body;
    public BufferedImage tail;


    public Skin(){
        SecureRandom random=new SecureRandom();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        Color color=new Color(r,g,b);
        head=new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);
        body=new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);
        tail=new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);

        Graphics2D headimg = head.createGraphics();
        headimg.setColor(new Color(0,0,0,0));
        headimg.fillRect(0,0,100,100);
        headimg.setColor(color);
        headimg.fillOval(0,0,100,100);
        headimg.setColor(Color.BLACK);
        headimg.fillOval(60,20,10,10);
        headimg.fillOval(60,60,10,10);

        Graphics2D bodyimg = body.createGraphics();
        bodyimg.setColor(new Color(0,0,0,0));
        bodyimg.fillRect(0,0,100,100);
        bodyimg.setColor(color);
        bodyimg.fillOval(0,0,100,100);

        Graphics2D tailimg = tail.createGraphics();
        tailimg.setColor(new Color(0,0,0,0));
        tailimg.fillRect(0,0,100,100);
        tailimg.setColor(color);
        tailimg.fillOval(0,0,100,100);

        tailimg.setColor(Color.BLACK);
        tailimg.fillOval(40,40,20,20);

    }

    //now only finish the initialization using color
    public Skin(Color color){
        head=new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);
        body=new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);
        tail=new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);

        Graphics2D headimg = head.createGraphics();
        headimg.setColor(new Color(0,0,0,0));
        headimg.fillRect(0,0,100,100);
        headimg.setColor(color);
        headimg.fillOval(0,0,100,100);
        headimg.setColor(Color.BLACK);
        headimg.fillOval(60,20,10,10);
        headimg.fillOval(60,60,10,10);

        Graphics2D bodyimg = body.createGraphics();
        bodyimg.setColor(new Color(0,0,0,0));
        bodyimg.fillRect(0,0,100,100);
        bodyimg.setColor(color);
        bodyimg.fillOval(0,0,100,100);

        Graphics2D tailimg = tail.createGraphics();

        tailimg.setColor(new Color(0,0,0,0));
        tailimg.fillRect(0,0,100,100);

        tailimg.setColor(color);
        tailimg.fillOval(0,0,100,100);

        tailimg.setColor(Color.BLACK);
        tailimg.fillOval(40,40,20,20);

    }

    public BufferedImage getHead() {
        return head;
    }

    public BufferedImage getBody() {
        return body;
    }

    public BufferedImage getTail() {
        return tail;
    }
}
