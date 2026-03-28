import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DragAndDrop {
    private static final int MAX_COLS = 80;
    private static final int BLOCK_PANEL_WIDTH = 624;   // 80 cols 7.8 px each
    private static final int FRAME_WIDTH = 1280;        // two such panels rounded up
    private static final int FRAME_HEIGHT = 720;        // keeping 16:9 ratio, about 35 lines
    private static final int DIVIDER_LOCATION = 1280 / 2;

    private static JLabel makeCodeBlock(String code) {
        JLabel label = new JLabel(code, JLabel.LEFT);
        label.setFont(new Font("Courier New", Font.PLAIN, 13));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLUE),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)  // padding inside border
        ));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        return label;
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Parsons Problems");
            frame.setLayout(new BorderLayout());
            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JTextArea instrTextArea = new JTextArea("Instructions\n" 
                + "Related to: While loop in Java.\n"
                + "Add Code Blocks from left to the right panel"
                + "to make the correct sequence.");
            instrTextArea.setEditable(false);
            JPanel instrPanel = new JPanel(new FlowLayout());
            instrPanel.add(instrTextArea);

            JLabel label1 = makeCodeBlock("int i;");
            JLabel label2 = makeCodeBlock("i = 0;");
            JLabel label3 = makeCodeBlock("while (i < 10) {");

            label.setTransferHandler(new TransferHandler("text"));
            label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JLabel l = (JLabel) e.getSource();
                TransferHandler handler = l.getTransferHandler();
                handler.exportAsDrag(l, e, TransferHandler.MOVE);
            }
            });

            // two panels side by side
            // allowing 20 code blocks in height
            JPanel blocksPanel = new JPanel(new GridLayout(20, 1, 5, 5));
            blocksPanel.add(label1);
            blocksPanel.add(label2);
            blocksPanel.add(label3);
            blocksPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            JPanel answerPanel = new JPanel(new GridLayout(20, 1, 5, 5));
            JScrollPane leftScroll = new JScrollPane(blocksPanel);
            JScrollPane rightScroll = new JScrollPane(answerPanel);
            JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
                leftScroll,
                rightScroll
            );
            splitPane.setDividerLocation(DIVIDER_LOCATION);
            // add to frame in sequence
            frame.add(instrPanel, BorderLayout.NORTH);
            frame.add(splitPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}