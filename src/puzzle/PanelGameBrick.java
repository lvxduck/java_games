package puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanelGameBrick extends JPanel  implements ActionListener {
    private static PanelGame instance;

    public static HandleKey handleKey;
    private final JPanel contentPane;
    private final int width;
    private final int height;

    private int score=0;

    public PanelGame getInstance(){
        return instance;
    }

    public void setUpData(){

    }

    public PanelGameBrick(JPanel jPanel, int width, int height){
        handleKey = new HandleKey();
        contentPane = jPanel;
        this.width=width;
        this.height=height;

        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JLabel label = new JLabel("PLAYING......!!!!!");
        label.setFont(new Font("System",Font.PLAIN,20));
        add(label);
    }

    private void doDrawing(Graphics g) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void endGame(){

        GameDBUtils.getInstance().writeDB(score);

        removeAll();
        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JLabel jLabel1 = new JLabel("YOU LOSE!!");
        add(jLabel1);
        JButton btnStop = new JButton("PLAY AGAIN");
        add(btnStop);
        JLabel jLabel2 = new JLabel("YOU LOSE!!");
        add(jLabel2);
        updateUI();
    }


    @Override
    public Dimension getPreferredSize()
    {
        return (new Dimension(width, height));
    }

    class HandleKey extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

        }
    }
}




