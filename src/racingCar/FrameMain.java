package racingCar;

import javax.swing.*;
import java.awt.*;

public class FrameMain extends JFrame {
    private int width=400;
    private int height=600;

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

//        PanelLogin panelLogin = new PanelLogin(contentPane,width,height);
        PanelHistory panelHistory = new PanelHistory(contentPane,width,height);
        PanelGame panelGame = new PanelGame(contentPane,width,height);
        addKeyListener(racingCar.PanelGame.handleKey);
        setFocusable(true);

//        contentPane.add(panelLogin, "Panel 1");
        contentPane.add(panelHistory, "Panel 2");
        contentPane.add(panelGame, "Panel 3");
        setContentPane(contentPane);
        pack();
        setLocationByPlatform(true);

//        add(panelLogin,BorderLayout.CENTER);
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
