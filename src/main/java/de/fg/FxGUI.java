package de.fg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FxGUI implements ActionListener{

    private static Player player;

    private JFrame mainFrame;
    private  JPanel mainContent;
    private JPanel center;
    private  JButton buttonPlay;

    public static void main (String[] args) {
        player = new Player();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FxGUI().guiCreateAndShow();
            }
        });
    }

    public void guiCreateAndShow() {
        guiFrameAndContentPanel();
        guiAddContent();
    }

    private void guiFrameAndContentPanel() {
        mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setPreferredSize(new Dimension(500,500));
        mainContent.setOpaque(true);

        mainFrame = new JFrame("Timing Check");
        mainFrame.setContentPane(mainContent);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void guiAddContent() {
        JPanel center = new JPanel();
        center.setOpaque(true);

        buttonPlay = new JButton("PLAY / STOP");
        buttonPlay.setActionCommand("play");
        buttonPlay.addActionListener(this);
        buttonPlay.setPreferredSize(new Dimension(200, 50));

        center.add(buttonPlay);
        mainContent.add(center, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        player.play();
    }
}
