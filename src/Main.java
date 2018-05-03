import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GamePanel game = new GamePanel();
        JFrame frame = new JFrame();
        frame.add(game);
        frame.addMouseMotionListener(game);
        game.setBounds(0, 30, 1000, 1000);
        frame.setSize(1000, 1000 + 30);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Thread t = new Thread(game);
        t.start();
    }
}
