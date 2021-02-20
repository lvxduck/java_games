package connectPuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelLogin extends JPanel {
    private final JPanel contentPane;
    private final int width;
    private final int height;

    public PanelLogin(JPanel panel,int width,int height){
//        GameDBUtils.getInstance();

        contentPane = panel;
        this.width=width;
        this.height=height;

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

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

                if(GameDBUtils.getInstance().authenticate(fieldUsername.getText(),fieldPassword.getText())){
                    System.out.println("LOGIN SUCCESS");
                    CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                    cardLayout.next(contentPane);
                    PanelHistory.update();
                }else{
                    System.out.println("LOGIN FAIL");
                }

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
        add(panelTitle);
        add(panelCenter);
        add(panelBottom);
        setBorder(BorderFactory.createEmptyBorder(80,40,80,40));

        this.setVisible(true);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return (new Dimension(width, height));
    }
}
