import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by zywang on 23/6/2018.
 */
public class GameRoom {
    public int ROOM_ID;
    public int MAX_PLAYERS = 8;
    public int AI_NUMBERS = 10;
    private boolean haveStarted = false;
    private ArrayList<GameClient> players = new ArrayList<>();
    private GameClient host;
    private InfoManager infoManager;
    public int WIDTH = 2000;
    public int HEIGHT = 2000;
    public GameRoom(int id, GameClient chost) {
        ROOM_ID = id;
        host = chost;
        addClient(chost);
    }

    public GameRoom(int id) {
        ROOM_ID = id;

    }

    int getROOM_ID() {
        return ROOM_ID;
    }

    void send(GameClient client, String msg) {
        try {
            client.getOutput().write(msg + "\n");
            client.getOutput().flush();
        } catch (IOException e) {
            removeClient(client);
        }
    }

    void sendAll(String msg) {
        for (GameClient client : players) {
            send(client, msg);
        }
    }


    void addClient(GameClient client) {

        synchronized (players) {

            if (players.size() == 0) {
                client.setHost(true);
                host = client;
            }

            players.add(client);
            JSONObject jmsg = new JSONObject();
            jmsg.put("type", "ENTER");
            jmsg.put("name", client.getNickname());
            sendAll(jmsg.toString());
            System.out.println("Room " + getROOM_ID() + " Add " + client.getNickname());

            for (GameClient c : players) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "ENTER");
                jsonObject.put("name", c.getNickname());
                send(client, jsonObject.toString());
            }

            JSONObject hostmsg = new JSONObject();
            hostmsg.put("type", "HOST");
            hostmsg.put("name", host.getNickname());
            send(client, hostmsg.toString());


            new ClientListener(client, this).start();
        }
    }

    void removeClient(GameClient client) {
        players.remove(client);

        if (isHaveStarted()) {
            //remove from game

        } else {
            JSONObject jmsg = new JSONObject();
            jmsg.put("type", "LEAVE");
            jmsg.put("name", client.getNickname());
            sendAll(jmsg.toString());
        }
    }

    boolean isHaveStarted() {
        return haveStarted;
    }

    boolean isFull() {
        return players.size() == MAX_PLAYERS;
    }

    GameClient getHost() {
        return host;
    }

    boolean allReady() {
        boolean flag = true;
        for (GameClient c : players) {
            if (c != host) {
                if (!c.isReady())
                    flag = false;
            }
        }
        System.out.println("All are ready! \t Room " + getROOM_ID());
        return flag;
    }

    void startGame() {
        for (GameClient c : players) {
            JSONObject hostmsg = new JSONObject();
            hostmsg.put("type", "START");
            send(c, hostmsg.toString());
        }
        infoManager = new InfoManager(WIDTH, HEIGHT);
        infoManager.init("online", players);
        haveStarted = true;

        System.out.println("Game Start at Room "+getROOM_ID());

    }

    public void setAngle(GameClient gameClient, double angle) {
            String name=gameClient.getNickname();
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("type","direction");
            jsonObject.put("angle",angle);
            jsonObject.put("name",name);
            System.out.println(jsonObject.toString());
            sendAll(jsonObject.toString());
    }

    public void sync(GameClient client){

    }

    public InfoManager getInfoManager() {
        return infoManager;
    }
}


