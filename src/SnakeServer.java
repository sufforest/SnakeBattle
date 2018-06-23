import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Created by zywang on 23/6/2018.
 */


public class SnakeServer implements Runnable {
    private int port;
    List<GameClient> clients = new ArrayList<GameClient>();
    List<GameRoom> rooms = new ArrayList<GameRoom>();
    private ServerSocket serverSocket;

    public SnakeServer(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server listen to " + port);
        } catch (IOException e) {
            System.out.println("Can not set up a server!");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                Socket socket = serverSocket.accept();
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                JSONObject jmsg = new JSONObject(in.readLine());
                if (jmsg.get("type").equals("CONNECT")) {
                    String nickname = jmsg.getString("name");
                    int room_id = jmsg.getInt("room");
                    System.out.println(nickname + "wants to enter room " + room_id);


                    if (rooms.size() > 0) {
                        boolean found = false;
                        for (GameRoom room : rooms) {
                            if (room.getROOM_ID() == room_id) {
                                found = true;
                                if (room.isFull()) {
                                    JSONObject feedback = new JSONObject();
                                    feedback.put("type", "ENTER");
                                    feedback.put("status", false);
                                    feedback.put("msg", "Room is full");
                                    out.write(feedback.toString() + "\n");
                                } else if (room.isHaveStarted()) {
                                    JSONObject feedback = new JSONObject();
                                    feedback.put("status", false);
                                    feedback.put("type", "ENTER");
                                    feedback.put("msg", "Game has started");
                                    out.write(feedback.toString() + "\n");
                                } else {
                                    JSONObject feedback = new JSONObject();
                                    feedback.put("status", true);
                                    feedback.put("type", "ENTER");
                                    out.write(feedback.toString() + "\n");
                                    room.addClient(new GameClient(socket, in, out, false, nickname));
                                }
                                break;
                            }
                        }


                        if (!found) {
                            JSONObject feedback = new JSONObject();
                            feedback.put("status", true);
                            feedback.put("type", "ENTER");
                            out.write(feedback.toString() + "\n");
                            createRoom(new GameClient(socket, in, out, false, nickname), room_id);
                        }
                    } else {
                        JSONObject feedback = new JSONObject();
                        feedback.put("status", true);
                        feedback.put("type", "ENTER");
                        out.write(feedback.toString() + "\n");
                        createRoom(new GameClient(socket, in, out, false, nickname), room_id);
                    }
                }
            }
        } catch (Exception e) {
        }
    }


    void createRoom(GameClient client, int id) {
        // Add a new game lobby & add p to the lobby
        GameRoom room = new GameRoom(id, client);
    }

    public static void main(String[] args) {
        new SnakeServer(8848).run();
    }

}

