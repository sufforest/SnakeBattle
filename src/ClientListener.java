import org.json.JSONObject;

/**
 * Created by zywang on 23/6/2018.
 */


//Listen to the message from client
public class ClientListener extends Thread {
    private GameClient client;
    private GameRoom room;

    public ClientListener(GameClient client, GameRoom room) {

        this.client = client;
        this.room = room;
    }

    public void run() {
        try {
            while (true) {
                String message = client.getInput().readLine();
                if (message != null) {
                    System.out.println(message);
                    JSONObject jmsg = new JSONObject(message);
                    //parser jmsg

                    if (room.isHaveStarted()) {
                        if (jmsg.getString("type").equals("DIRECTION")) {
                            double angle = jmsg.getDouble("angle");
                            room.setAngle(client,angle);
                            System.out.println("set angle");
                        }

                        if(jmsg.getString("type").equals("SNAKE")){
                            if(jmsg.getString("op").equals("reborn")){
                                InfoManager infoManager= room.getInfoManager();
                                String name=jmsg.getString("name");
                                for(Snake snake:infoManager.getSnakes()){
                                        if(snake.getName().equals(name)){
                                            snake.reborn(jmsg.getInt("x"),jmsg.getInt("y"),true);
                                            break;
                                        }
                                }
                                room.sendAll(jmsg.toString());
                            }
                        }
                    } else {
                        if (jmsg.getString("type").equals("ROOM")) {
                            if (jmsg.getBoolean("status")) {
                                if (client == room.getHost()) {
                                    if (room.allReady()) {
                                        room.startGame();
                                    }
                                } else {
                                    JSONObject jresponse = new JSONObject();
                                    jresponse.put("type", "READY");
                                    jresponse.put("name", client.getNickname());
                                    client.setReady(true);
                                    room.sendAll(jresponse.toString());
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
        }

        //disconnect from server (room)
        room.removeClient(client);
    }

}
