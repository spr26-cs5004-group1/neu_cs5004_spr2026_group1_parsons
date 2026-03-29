import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class DragAndDrop {

    private static final int MAX_COLS = 80;
    private static final int BLOCK_PANEL_WIDTH = 624;   // 80 cols * 7.8px each
    private static final int FRAME_WIDTH = 1280;        // two panels rounded up
    private static final int FRAME_HEIGHT = 720;        // 16:9 ratio, ~35 lines
    private static final int DIVIDER_LOCATION = FRAME_WIDTH / 2;

    // tracks which label is currently being dragged
    private static JLabel draggedLabel = null;

    /**
     * Creates a draggable code block label with monospace font and blue border.
     *
     * @param code the code string to display in the block.
     * @return a styled draggable JLabel.
     */
    private static JLabel makeCodeBlock(String code) {
        JLabel label = new JLabel(code, JLabel.LEFT);
        label.setFont(new Font("Courier New", Font.PLAIN, 13));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLUE),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);

        // custom TransferHandler that exports the label text as a string
        label.setTransferHandler(new TransferHandler() {
            @Override
            public int getSourceActions(JComponent c) {
                return TransferHandler.MOVE;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                return new StringSelection(((JLabel) c).getText());
            }
        });

        // remember which label is being dragged when mouse is pressed
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JLabel l = (JLabel) e.getSource();
                draggedLabel = l;
                l.getTransferHandler().exportAsDrag(l, e, TransferHandler.MOVE);
            }
        });

        return label;
    }

    /**
     * Makes a JPanel a drop target that accepts dragged code blocks.
     * Removes the block from its source panel and adds it to this panel.
     *
     * @param panel the panel to make a drop target.
     */
    private static void makeDropTarget(JPanel panel) {
        panel.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    String text = (String) support.getTransferable()
                        .getTransferData(DataFlavor.stringFlavor);

                    // remove from source panel
                    if (draggedLabel != null) {
                        Container sourceParent = draggedLabel.getParent();
                        if (sourceParent != null) {
                            sourceParent.remove(draggedLabel);
                            sourceParent.revalidate();
                            sourceParent.repaint();
                        }
                        draggedLabel = null;
                    }

                    // add new block to target panel
                    panel.add(makeCodeBlock(text));
                    panel.revalidate();
                    panel.repaint();
                    return true;

                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Parsons Problems");
            frame.setLayout(new BorderLayout());
            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // instructions panel at top
            JTextArea instrTextArea = new JTextArea(
                "Instructions\n"
                + "Related to: While loop in Java.\n"
                + "Drag code blocks from the left panel to the right panel "
                + "to form the correct sequence of code to print \"Hello\" " 
                + "exactly 10 times."
            );
            instrTextArea.setEditable(false);
            instrTextArea.setFont(new Font("Arial", Font.PLAIN, 13));
            instrTextArea.setBackground(null);
            JPanel instrPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            instrPanel.add(instrTextArea);

            // code blocks
            JPanel blocksPanel = new JPanel(new GridLayout(20, 1, 5, 5));
            blocksPanel.setBorder(BorderFactory.createTitledBorder("Blocks"));
            blocksPanel.add(makeCodeBlock("int i;"));
            blocksPanel.add(makeCodeBlock("i = 0;"));
            blocksPanel.add(makeCodeBlock("while (i < 10) {"));
            blocksPanel.add(makeCodeBlock("    printf(\"Hello\\n\");"));
            blocksPanel.add(makeCodeBlock("    i++;"));
            blocksPanel.add(makeCodeBlock("}"));
            blocksPanel.add(makeCodeBlock("while (i <= 10) {"));  // distractor

            // answer panel
            JPanel answerPanel = new JPanel(new GridLayout(20, 1, 5, 5));
            answerPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE, 2), "Answer"
            ));

            // make both panels drop targets
            makeDropTarget(blocksPanel);
            makeDropTarget(answerPanel);

            // scroll panes for both panels
            JScrollPane leftScroll = new JScrollPane(blocksPanel);
            JScrollPane rightScroll = new JScrollPane(answerPanel);

            // split pane side by side
            JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftScroll,
                rightScroll
            );
            splitPane.setDividerLocation(DIVIDER_LOCATION);

            frame.add(instrPanel, BorderLayout.NORTH);
            frame.add(splitPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}