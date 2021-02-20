package uiGame;

import javax.swing.*;
import java.awt.*;

public class uigame1 extends JFrame {

    public static void main(String[] args) {
        new uigame1();
    }

    private int width=300;
    private int height=600;

    public uigame1(){
        JLabel statusbar;
        statusbar = new JLabel("Label");
        this.add(statusbar, BorderLayout.SOUTH);

        this.setTitle("Co Caro");
        this.setSize(width,height);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);

    }

}
