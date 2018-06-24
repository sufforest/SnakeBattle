import org.json.JSONObject;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zywang on 23/6/2018.
 */
public class ServerListener implements Runnable{
    RoomPanel roomPanel;
    InfoManager infoManager=null;
    BufferedReader in;
    Socket server;
    public ServerListener(RoomPanel roomPanel,BufferedReader in,Socket server){
        this.roomPanel=roomPanel;
        this.in=in;
        this.server=server;
    }

    @Override
    public void run(){
        try {
            while(true) {
                String jmsg = in.readLine();
                if(jmsg != null) {
                    JSONObject jobj=new JSONObject(jmsg);
                    String type=jobj.getString("type");
                    if(type.equals("ENTER")){
                        String nick=jobj.getString("name");
                        roomPanel.addPlayer(nick);
                    }
                    if(type.equals("READY")) {
                        String nick=jobj.getString("name");
                        roomPanel.setReady(nick);
                    }
                    if(type.equals("START")){
                        System.out.println("Game Start");
                        if(infoManager==null) {
                            infoManager = new InfoManager(roomPanel.WIDTH, roomPanel.HEIGHT);
                            infoManager.init("Client");
                        }

                    }
                    if(type.equals("SNAKE")){
                        if(jobj.getString("op").equals("ADD")) {
                            System.out.println("SNAKE ADD");
                            int x=jobj.getInt("x");
                            boolean isAI=jobj.getBoolean("isAI");
                           int y= jobj.getInt("y");
                            String name=jobj.getString("name");
                            Snake snake=new Snake(x,y,isAI,name);
                            infoManager.join( snake);
                            if(infoManager.getSnakeNumber()==roomPanel.number){
                                System.out.println("Initial Map\t Player: "+roomPanel.number);
                                PlayPanel gamepanel=new PlayPanel(infoManager,roomPanel.getUsername(),roomPanel.out);
                                roomPanel.playPanel=gamepanel;
                                JFrame jFrame=roomPanel.jFrame;
                                jFrame.remove(roomPanel);
                                jFrame.add(gamepanel);
                                jFrame.addMouseMotionListener(gamepanel);
                                gamepanel.setBounds(0, 30, 1000, 1000);
                                jFrame.setSize(1000, 1000 + 30);
                                jFrame.validate();
                                jFrame.setLocationRelativeTo(null);
                                jFrame.setVisible(true);
                                //Thread t = new Thread(gamepanel);
                                //t.start();

                            }
                        }
                        else if(jobj.getString("op").equals("DIE")){
                            String name=jobj.getString("name");
                            for(Snake snake:infoManager.getSnakes()){
                                if(snake.getName().equals(name)){
                                    snake.die();
                                    break;
                                }
                            }
                        }
                        else if(jobj.getString("op").equals("reborn")){
                            String name=jobj.getString("name");
                            for(Snake snake:infoManager.getSnakes()){
                                if(snake.getName().equals(name)){
                                    snake.reborn(jobj.getInt("x"),jobj.getInt("y"),true);
                                    break;
                                }
                            }
                        }
                    }
                    if(type.equals("time")){
                            roomPanel.playPanel.cntDown = jobj.getInt("cnt");
                           SnakeManager smg=infoManager.getSnakeManager();
                           //only update itself
                            Snake usersnake=roomPanel.playPanel.getUserSnake();
                           boolean ret=usersnake.updateAlive();
                           if(ret){roomPanel.playPanel.SendReBorn();}

                            roomPanel.playPanel.repaint();

                    }

                    if(type.equals("FOOD")){
                        FoodManager fmg=infoManager.getFoodManager();
                        String op=jobj.getString("op");

                        if(op.equals("ADD")){
                            Food food=new Food(jobj.getDouble("x"),jobj.getDouble("y"),jobj.getInt("bonus"));
                            fmg.addFood(food);
                        }
                        else if(op.equals("REMOVE")){
                            Food food=new Food(jobj.getDouble("x"),jobj.getDouble("y"),jobj.getInt("bonus"));
                            for(int i = fmg.getFoods().size() - 1; i >= 0; i--){
                                Food item = fmg.getFoods().get(i);
                                if(food.equals(item)){
                                    fmg.getFoods().remove(item);
                                }
                            }
                        }
                        else if(op.equals("EAT")){
                            int bonus=jobj.getInt("food");
                            String name=jobj.getString("name");
                            for(Snake s:infoManager.getSnakes()){
                                if(s.getName().equals(name)){
                                    s.eat(bonus);
                                    break;
                                }
                            }
                        }
                    }

                    if(type.equals("move")){
                        SnakeManager smg=infoManager.getSnakeManager();
                        int step=jobj.getInt("step");
                        smg.move(step);
                    }

                    if(type.equals("direction")) {
                        String name = jobj.getString("name");
                        Double angle = jobj.getDouble("angle");
                        System.out.println("direction "+name+"\t"+angle);
                        for (Snake snake : infoManager.getSnakes()) {
                            if (snake.getName().equals(name)) {
                                snake.getHead().setAngle(angle);
                                break;
                            }
                        }
                    }

                    if(type.equals("HOST")){
                        String nick=jobj.getString("name");
                        roomPanel.setHost(nick);
                    }
                    if(type.equals("Transfer")){
                        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(server.getInputStream()));
                        Object obj = is.readObject();
                        if (obj != null) {
                            infoManager = (InfoManager)obj;
                            System.out.println("SYNC");
                            roomPanel.playPanel.repaint();
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
}
