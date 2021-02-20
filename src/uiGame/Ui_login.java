package uiGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ui_login extends JFrame {

    private int width=400;
    private int height=600;

    public static void main(String[] args) {
        new Ui_login();
    }

    public Ui_login(){
        this.setSize(width,height);
        setCenterTitle("LOGIN");
        this.setDefaultCloseOperation(3);
        setLayout(new BorderLayout());

        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain,BoxLayout.Y_AXIS));

        //----top-----
        JPanel panelTitle = new JPanel();
        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("System", Font.PLAIN, 24));
        panelTitle.add(title);
        panelTitle.setBorder(BorderFactory.createEmptyBorder(20,10,0,10));


        //----center-----
        JPanel panelCenter = new JPanel( new GridLayout(1,0));
        panelCenter.setBorder(BorderFactory.createLineBorder(Color.gray));

        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(new GridLayout(3,2));

        JLabel lb = new JLabel("Username");
        lb.setFont(new Font("System", Font.PLAIN, 16));
        JTextField fieldUsername = new JTextField();

        JLabel lb2 = new JLabel("Password");
        lb2.setFont(new Font("System", Font.PLAIN, 16));
        JTextField fieldPassword = new JTextField();

        panelLogin.add(lb);
        panelLogin.add(fieldUsername);
        panelLogin.add(lb2);
        panelLogin.add(fieldPassword);

        JButton btnReset = new JButton("RESET");
        panelLogin.add(btnReset);

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldPassword.setText("");
                fieldUsername.setText("");
            }
        });
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("LOGIN");
            //    GameDBUtils
            }
        });
        panelLogin.add(btnLogin);
        panelLogin.setBorder(BorderFactory.createEmptyBorder(50,20,50,20));
        panelCenter.add(panelLogin);

        //----bottom-----
        JPanel panelBottom = new JPanel();
        panelBottom.add(new JLabel("Made by humanduck"));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(30,140,0,0));

        //----add panel to main panel----
        panelMain.add(panelTitle);
        panelMain.add(panelCenter);
        panelMain.add(panelBottom);
        panelMain.setBorder(BorderFactory.createEmptyBorder(80,40,80,40));

        add(panelMain,BorderLayout.CENTER);
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
