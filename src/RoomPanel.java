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
    int room_id;
    JPanel p[][];
    boolean s[][]; // true if that place occupied
    Map<String,Integer>position;
    JPanel players;

    BufferedWriter out;

    JButton ready;

    public RoomPanel(BufferedWriter jout, int room_id) {
        position=new HashMap<String,Integer>();
        this.room_id = room_id;
        out = jout;
        int i, j;
        p = new JPanel[2][4];
        GridLayout grid = new GridLayout(2, 4, 2, 2);
        ready = new JButton("Ready");
        players = new JPanel();
        players.setLayout(grid);
        for (i = 0; i < 2; i++) {
            for (j = 0; j < 4; j++) {
                p[i][j] = new JPanel();
                p[i][j].setLayout(new BorderLayout());
                p[i][j].add(new JLabel("Wait new player"), BorderLayout.CENTER);
                s[i][j] = false;
                players.add(p[i][j]);
            }
        }

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ready) {
            try {
                JSONObject jmsg = new JSONObject();
                jmsg.put("type", "ROOM");
                jmsg.put("status", true);
                out.write(jmsg.toString() + "\n");
                out.flush();
            } catch (IOException exception) {

            }
        }

    }

    public void addPlayer(String nick) {
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
                        position.put(nick,2+j*4);
                    }
                }

            }
        }
        revalidate();
    }

    public void setReady(String nick) {
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
                    }
                }

            }
        }
        revalidate();
    }

}

