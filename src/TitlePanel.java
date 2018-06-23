

import com.sun.codemodel.internal.JOp;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by zywang on 3/5/2018.
 */
public class TitlePanel extends JPanel implements ActionListener {
    private JButton single, multiplayer;
   // private JLabel title;
    private JFrame frame;

    private JTextField nickname;

    private Socket server=null;
    private BufferedWriter out=null;
    private BufferedReader in=null;


    public TitlePanel(JFrame jframe) {
        single = new JButton("Single Player");
        nickname= new JTextField("Java Lab");
        nickname.setEditable(true);
        multiplayer = new JButton("Multi Player");
        //title = new JLabel("Snake Battle");
        frame = jframe;
        JPanel choose = new JPanel();
        choose.add(single);
        choose.add(multiplayer);
        single.addActionListener(this);
        multiplayer.addActionListener(this);
        setLayout(new BorderLayout());
        //add(title, BorderLayout.CENTER);
        add(choose, BorderLayout.SOUTH);
        add(nickname,BorderLayout.CENTER);
        //setVisible(true);
        frame.setSize(300,200);
        //frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == single) {
            String name=nickname.getText();
            frame.remove(this);

            GamePanel game = new GamePanel(name);
            frame.add(game);
            frame.addMouseMotionListener(game);
            frame.addKeyListener(game);
            game.setBounds(0, 30, 1000, 1000);
            frame.setSize(1000, 1000 + 30);
            frame.setLocationRelativeTo(null);

            Thread t = new Thread(game);
            t.start();
        } else if (e.getSource() == multiplayer) {
            JOptionPane.showInputDialog("Enter room number");

        }
    }

    void enterRoom(int room_id) {
        try {
            server = new Socket("localhost", 8848);
            out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));


            JSONObject jmsg = new JSONObject();
            jmsg.put("type", "CONNECT");
            jmsg.put("name",nickname.getText());
            jmsg.put("room",room_id);

            out.write(jmsg.toString()+"\n");
            JSONObject ret = new JSONObject(in.readLine());
            if(!ret.getBoolean("status")){
                String msg=ret.getString("msg");
                JOptionPane.showMessageDialog(null,msg);
            }
            else {
                frame.remove(this);
                RoomPanel roomPanel=new RoomPanel(out,room_id);
                frame.add(roomPanel);
                new Thread(new ServerListener(roomPanel, in)).start();

            }
        }
        catch(Exception e) {}
    }
}
