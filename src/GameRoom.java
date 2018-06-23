import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zywang on 23/6/2018.
 */
public class GameRoom  {
    public int ROOM_ID;
    public int  MAX_PLAYERS = 4;
    public int AI_NUMBERS=10;
    private boolean haveStarted=false;
    private ArrayList<GameClient> players;
    private GameClient host;
    private InfoManager infoManager;
    public  int WIDTH = 2000;
    public  int HEIGHT = 2000;

    public GameRoom(int id, GameClient chost){
        ROOM_ID=id;
        host=chost;
        addClient(chost);
    }
    public GameRoom(int id){
        ROOM_ID=id;

    }

    int getROOM_ID(){
        return ROOM_ID;
    }
    void send(GameClient client,String msg){
        try {
            client.getOutput().write(msg + "\n");
            client.getOutput().flush();
        }
        catch (IOException e){
            removeClient(client);
        }
    }

    void sendAll(String msg){
        for(GameClient client : players) {
            send(client, msg);
        }
    }


    void addClient(GameClient client) {

        synchronized(players) {

            if(players.size() == 0) {
                client.setHost(true);
                host=client;
            }

            players.add(client);
            JSONObject jmsg = new JSONObject();
            jmsg.put("type","ENTER");
            jmsg.put("name",client.getNickname());
            sendAll(jmsg.toString());
            new ClientListener(client, this).start();
        }
    }

    void removeClient(GameClient client){
        players.remove(client);

        if (isHaveStarted()) {
            //remove from game

        }
        else {
            JSONObject jmsg = new JSONObject();
            jmsg.put("type","LEAVE");
            jmsg.put("name",client.getNickname());
            sendAll(jmsg.toString());
        }
    }

    boolean isHaveStarted(){
        return haveStarted;
    }

    boolean isFull(){
        return players.size()==MAX_PLAYERS;
    }



}
