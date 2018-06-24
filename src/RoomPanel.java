import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zywang on 23/6/2018.
 */
public class RoomPanel extends JPanel implements ActionListener {
    public PlayPanel playPanel;
    public String username;
    public int WIDTH = 2000;
    public int HEIGHT = 2000;
    public JFrame jFrame=null;

    public int number=0;
    int room_id;
    JPanel p[][];
    boolean s[][]; // true if that place occupied
    Map<String,Integer>position;
    JPanel players;

    BufferedWriter out;

    JButton ready;

    public RoomPanel(BufferedWriter jout, int room_id,String username,JFrame jFrame) {
        this.jFrame=jFrame;
        this.username=username;
        position=new HashMap<String,Integer>();
        this.room_id = room_id;
        out = jout;
        int i, j;
        p = new JPanel[2][4];
        s= new boolean[2][4];
        GridLayout grid = new GridLayout(2, 4, 2, 2);
        ready = new JButton("Ready");
        players = new JPanel();
        players.setLayout(grid);
        for (i = 0; i < 2; i++) {

            for (j = 0; j < 4; j++) {
                p[i][j] = new JPanel();
                p[i][j].setLayout(new BorderLayout());
                p[i][j].add(new JLabel("Waiting New Player"), BorderLayout.CENTER);
                s[i][j] = false;
                players.add(p[i][j]);
            }
        }
        setLayout(new BorderLayout());
        add(players,BorderLayout.CENTER);
        add(ready,BorderLayout.EAST);
        ready.addActionListener(this);


    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ready) {
            try {
                JSONObject jmsg = new JSONObject();
                jmsg.put("type", "ROOM");
                jmsg.put("status", true);
                out.write(jmsg.toString() + "\n");
                out.flush();
                System.out.println("SEND:"+jmsg.toString());
            } catch (IOException exception) {

            }
        }

    }

    public void addPlayer(String nick) {
        if(position.get(nick)!=null){
            return;
        }
        int i, j;
        boolean findPos = false;
        for (i = 0; i < 2; i++) {
            for (j = 0; j < 4; j++) {
                if (!findPos) {
                    if (!s[i][j]) {
                        findPos = true;
                        s[i][j] = true;
                        p[i][j].removeAll();
                        p[i][j].add(new JLabel(nick), BorderLayout.CENTER);
                        position.put(nick,j+i*4);
                    }
                }

            }
        }
        number++;
        revalidate();
    }

    public void setReady(String nick) {
        System.out.println("Set Ready:"+nick);
        int i, j;
        int pos=position.get(nick);
        i=pos/4;
        j=pos%4;
        p[i][j].removeAll();
        p[i][j].add(new JLabel(nick), BorderLayout.CENTER);
        p[i][j].add(new JLabel("Ready"),BorderLayout.SOUTH);

        revalidate();
    }

    public void startGame(){

    }

    public void setHost(String nick){
        System.out.println("Set Host:"+nick);
        int i, j;
        int pos=position.get(nick);
        i=pos/4;
        j=pos%4;
        p[i][j].removeAll();
        p[i][j].add(new JLabel(nick), BorderLayout.CENTER);
        p[i][j].add(new JLabel("Host"),BorderLayout.SOUTH);

        if(username.equals(nick)){
            ready.setText("Start");
        }
        revalidate();

    }

    public String getUsername(){
        return username;
    }

    public static void main(String []args){
        JFrame jFrame =new JFrame();
        jFrame.add(new RoomPanel(null,2,"JAVA",jFrame));
        jFrame.setSize(600,300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}

