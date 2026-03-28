import java.awt.*;
import javax.swing.*;


public class DragAndDrop {
    private static final int MAX_COLS = 80;
    private static final int BLOCK_PANEL_WIDTH = 624;   // 80 cols 7.8 px each
    private static final int FRAME_WIDTH = 1280;        // two such panels rounded up
    private static final int FRAME_HEIGHT = 720;        // keeping 16:9 ratio, about 35 lines
    private static final int DIVIDER_LOCATION = 1280 / 2;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Parsons Problems");
        frame.setLayout(new GridLayout(1, 2));
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setDividerLocation(DIVIDER_LOCATION);
        JPanel blocksPanel = new JPanel(new GridLayout(20, 1, 5, 5));  // allowing 20 code blocks
        JPanel answerPanel = new JPanel(new GridLayout(20, 1, 5, 5));
        JScrollPane leftScroll = new JScrollPane(blocksPanel);
        JScrollPane rightScroll = new JScrollPane(answerPanel);
        frame.add(leftScroll);
        frame.add(rightScroll);
        frame.setVisible(); 
    }
}