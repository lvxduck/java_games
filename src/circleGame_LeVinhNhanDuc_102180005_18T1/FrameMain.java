package circleGame;

import javax.swing.*;
import java.awt.*;

public class FrameMain extends JFrame {
    private int width=800;
    private int height=500;

    public static void main(String[] args) {
        new FrameMain();
    }

    public FrameMain(){

        setSize(width,height);
        setCenterTitle("GAME");
        setDefaultCloseOperation(3);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new CardLayout());

        PanelHistory panelHistory = new PanelHistory(contentPane,width,height);
        PanelGame panelGame = new PanelGame(contentPane,width,height);
        addKeyListener(PanelGame.handleKey);
        setFocusable(true);

        contentPane.add(panelGame, "Panel 2");
        contentPane.add(panelHistory, "Panel 3");
        setContentPane(contentPane);
        pack();
        setLocationByPlatform(true);

        setMaximumSize(getPreferredSize());
        this.setVisible(true);

    }

    private void setCenterTitle(String title) {
        setFont(new Font("System", Font.PLAIN, 14));
        Font f = getFont();
        FontMetrics fm = getFontMetrics(f);
        int x = fm.stringWidth(title);
        int y = fm.stringWidth(" ");
        int z = getWidth()/2 - (x/2);
        int w = z/y;
        String pad ="";
        pad = String.format("%"+w+"s", pad);
        setTitle(pad+title);
    }
}
