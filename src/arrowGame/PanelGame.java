package arrowGame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanelGame extends JPanel  implements ActionListener {
    private static PanelGame instance;

    public static HandleKey handleKey;
    private final JPanel contentPane;
    private final int width;
    private final int height;
    public final int paddingVertical=60;
    public final int paddingHorizontal=20;
    private final int timeUpdate = 50;
    private Timer timer;

    private Arrow[] arrows;
    private int arrowWidth=40;

    private int playerPosX;
    private int numberArr=0;
    private int playerPosY;
    private int playerHeight=70;

    private int targetPosX;
    private int targetPosY;
    private int targetWidth=80;
    private int targetHeight=20;

    private int angle=0;
    private int v0 =0;

    private int score=0;

    public static PanelGame getInstance(){
        return instance;
    }

    public PanelGame(JPanel jPanel, int width, int height){
        instance=this;
        handleKey = new HandleKey();
        contentPane = jPanel;
        this.width=width;
        this.height=height;
        playerPosX=paddingHorizontal*2;
        playerPosY=height-paddingVertical-playerHeight;
        timer = new Timer(timeUpdate,this);
        timer.start();
        arrows = new Arrow[20];
        targetPosX = width-paddingHorizontal-targetWidth-50;
        targetPosY = height -paddingVertical-targetHeight;

        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JLabel label = new JLabel("PLAYING......!!!!!");
        label.setFont(new Font("System",Font.PLAIN,20));
        add(label);
        JSlider sliderAngle = new JSlider(JSlider.HORIZONTAL,0,90,1);
        sliderAngle.setMajorTickSpacing(30);
        sliderAngle.setMinorTickSpacing(5);
        sliderAngle.setPaintTicks(true);
        sliderAngle.setPaintLabels(true);
        sliderAngle.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                angle = sliderAngle.getValue();
            }
        });
        add(sliderAngle);

        JSlider sliderV0 = new JSlider(JSlider.HORIZONTAL,0,150,1);
        sliderV0.setMajorTickSpacing(50);
        sliderV0.setMinorTickSpacing(10);
        sliderV0.setPaintTicks(true);
        sliderV0.setPaintLabels(true);
        sliderV0.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                v0 = sliderV0.getValue();
            }
        });
        add(sliderV0);

        JButton btnFire = new JButton("FIRE");
        btnFire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Angle: "+angle+", Velocity: "+ v0);
                arrows[numberArr] = new Arrow(playerPosX,playerPosY,angle, v0);
                arrows[numberArr].start();
                numberArr+=1;
            }
        });
        add(btnFire);
    }

    public void setUpData(){

    }


    private void doDrawing(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("System",Font.PLAIN,24));
        g.drawString("Time: "+score,200,paddingVertical/2+10);

        g.drawRect(paddingHorizontal,paddingVertical,width-paddingHorizontal*2,height-paddingVertical*2);

        //------draw player------//
        g.setColor(Color.red);
        g.fillRect(playerPosX,playerPosY,40,70);

        //------draw arrow------//
        g.setColor(Color.BLACK);
        for(Arrow arrow: arrows){
            if(arrow!=null){
                int arrowHeadX = arrow.currentX + (int)(arrowWidth*Math.cos(arrow.arrowAngle));
                int arrowHeadY = arrow.currentY - (int)(arrowWidth*Math.sin(arrow.arrowAngle));
                g.drawLine(arrow.currentX,arrow.currentY, arrowHeadX, arrowHeadY);
            }
        }

        //------draw target------//
        g.setColor(Color.BLUE);
        g.fillRect(targetPosX,targetPosY,targetWidth,targetHeight);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
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

    class Arrow extends Thread{
        private int x,y;
        float angle;
        public float arrowAngle;
        float time;
        int weight;
        float aY;
        int v0;
        int currentX,currentY;
        int length;

        public Arrow(int x, int y, int angle, int v0){
            this.x = x;
            this.y = y;
            this.angle = (float) ((angle*Math.PI)/180f);
            this.time = 0;
            this.weight = 10;
            this.aY = -20;
            this.v0 = v0;
            arrowAngle = angle;
            time = 0;
            currentX=0;
            currentY=0;
            length= (int) -((float)(v0*v0*Math.sin(2*this.angle))/aY);
            System.out.println("Fly "+length+" , "+angle);
        }

        public void run() {
            while (true){
                time += timeUpdate/200f;
                currentY = y - (int) (v0*Math.sin(angle)*time+1/2f*aY*time*time);
                currentX = x + (int) (v0*Math.cos(angle)*time);
                arrowAngle = -(( (float)(currentX-x)/(float) length)-0.5f)*2*angle;
                System.out.println("Fly "+arrowAngle);
                if(currentY+ arrowWidth> height-paddingVertical){
                    break;
                }
                try {
                    Thread.sleep(timeUpdate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}






