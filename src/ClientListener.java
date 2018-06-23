import org.json.JSONObject;

/**
 * Created by zywang on 23/6/2018.
 */


//Listen to the message from client
public class ClientListener extends Thread {
    private GameClient client;
    private GameRoom room;
    private String message;

    public ClientListener(GameClient client, GameRoom room) {

        this.client = client;
        this.room = room;
    }

    public void run() {
        try {
            while ((message = client.getInput().readLine()) != null) {
                JSONObject jmsg = new JSONObject(message);
                //parser jmsg
                if(room.isHaveStarted()) {
                    if (jmsg.get("type").equals("move")) {
                        double angle = jmsg.getDouble("angle");

                    }
                }
            }
        } catch (Exception e) {
        }

        //disconnect from server (room)
        room.removeClient(client);
    }

}
