import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by zywang on 23/6/2018.
 */
public class GameClient {
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean host; // to start a game
    private String nickname;

    public GameClient(Socket s, BufferedReader i, BufferedWriter o, boolean h, String name) {

        clientSocket = s;
        in = i;
        out = o;
        host = h;
        nickname = name;
    }

    public String getNickname() {
        return nickname;
    }

    BufferedReader getInput() {
        return in;
    }

    BufferedWriter getOutput() {

        return out;
    }

    boolean isHost() {
        return host;
    }

    void setHost(boolean flag){
        host=flag;

    }
    void disconnect() {
        try {
            clientSocket.close();
            in.close();
            out.close();
        }
        catch(Exception e) {}
    }
}
