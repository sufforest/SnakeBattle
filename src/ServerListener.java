import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.ServerSocket;

/**
 * Created by zywang on 23/6/2018.
 */
public class ServerListener implements Runnable{
    RoomPanel roomPanel;
    BufferedReader in;

    public ServerListener(RoomPanel roomPanel,BufferedReader in){
        this.roomPanel=roomPanel;
        this.in=in;
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

                    if(type.equals("GAME")){

                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
}
