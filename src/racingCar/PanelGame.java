package racingCar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class PanelGame extends JPanel  implements ActionListener {
    private static PanelGame instance;

    public static HandleKey handleKey;
    private final JPanel contentPane;
    private final int width;
    private final int height;
    public final int paddingVertical=0;
    public final int paddingHorizontal=20;
    Timer timer;
    int timeUpdate=30;

    private MyCar myCar;
    private Car[] cars;
    private boolean[] isLaneHasCar;
    private int carHeight;
    private int sizeRoadLane;
    private float velocityMax=320;
    private float velocityMin=200;
    private float acceleration=20;
    private boolean isEndGame;

    private float score=0;

    public static PanelGame getInstance(){
        return instance;
    }

    public PanelGame(JPanel jPanel, int width, int height){
        instance=this;
        handleKey = new HandleKey();
        contentPane = jPanel;
        this.width=width;
        this.height=height;
       // setUpData();

        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JLabel label = new JLabel("PLAYING......!!!!!");
        label.setFont(new Font("System",Font.PLAIN,20));
        add(label);
    }

    public void setUpData(){
        velocityMax=320;
        velocityMin=200;
        score=0;
        isEndGame = false;
        sizeRoadLane = (width-paddingHorizontal*2)/4;
        carHeight = 80;
        isLaneHasCar = new boolean[5];

        cars = new Car[10];
        cars[0] = new Car(randomLane(),carHeight,Color.blue,randomDistance(),randomVelocity());
        cars[0].start();
        cars[1] = new Car(randomLane(),carHeight,Color.blue,randomDistance(),randomVelocity());
        cars[1].start();
        cars[2] = new Car(randomLane(),carHeight,Color.blue,randomDistance(),randomVelocity());
        cars[2].start();
        myCar = new MyCar(2,carHeight,Color.red,height-paddingVertical-carHeight-10);
        timer = new Timer(timeUpdate,this);
        timer.start();
    }

    int randomLane(){
        Random random = new Random();
        int lane = random.nextInt(4)+1;
        while (isLaneHasCar[lane]){
            lane = random.nextInt(4)+1;
        }
        isLaneHasCar[lane]=true;
        return lane;
    }

    int randomDistance(){
        Random random = new Random();
        return -(random.nextInt(200)+90);
    }

    int randomVelocity(){
        Random random = new Random();
        return (int) (random.nextInt((int) (velocityMax-velocityMin))+velocityMin);
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("System",Font.PLAIN,24));
        g.drawString("Time: "+(int)score,140,30);

        g.drawRect(paddingHorizontal,paddingVertical,width-paddingHorizontal*2,height-paddingVertical*2);

        //-----draw road lane-------//
        g.setColor(Color.BLACK);
        for(int i=1;i<=4;i++){
            g.drawLine((i-1)*sizeRoadLane+paddingHorizontal,paddingVertical,(i-1)*sizeRoadLane+paddingHorizontal,height-paddingVertical);
        }

        drawMyCar(g);

        for(Car car: cars){
            if(car!=null){
                drawCar(g,car);
            }
        }

    }

    void drawMyCar(Graphics g){
        if(myCar!=null){
            g.setColor(myCar.color);
            g.fillRect((myCar.lane-1)*sizeRoadLane+paddingHorizontal+10,
                    myCar.distance,sizeRoadLane-20,myCar.height);
        }
    }

    void drawCar(Graphics g, Car car){
        g.setColor(Color.BLACK);
        g.fillRect((car.lane-1)*sizeRoadLane+paddingHorizontal+10,
                    car.distance,sizeRoadLane-20,car.carHeight);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        checkCollision();
        score+=timeUpdate/1000f;
        velocityMax+=acceleration*(timeUpdate/1000f);
        velocityMin+=acceleration*(timeUpdate/1000f);
        repaint();
    }

    private boolean checkCollision(){
        for(Car car: cars){
            if(car!=null){
                if(myCar.lane==car.lane){
                    if (car.distance+car.carHeight>myCar.distance&&car.distance<myCar.distance+myCar.height){
                        endGame();
                    }
                }
            }
        }
        return false;
    }

    private void endGame(){
        if(!isEndGame){
            isEndGame=true;
            timer.stop();
            //  GameDBUtils.getInstance().writeDB(score);
            JOptionPane.showMessageDialog(this,"Your Score:"+(int)score);

            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            PanelHistory.update();
            cardLayout.previous(contentPane);
        }

//        removeAll();
//        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
//        JLabel jLabel1 = new JLabel("YOU LOSE!!");
//        add(jLabel1);
//        JButton btnStop = new JButton("PLAY AGAIN");
//        add(btnStop);
//        JLabel jLabel2 = new JLabel("YOU LOSE!!");
//        add(jLabel2);
//        updateUI();
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
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(myCar.lane>1){
                        myCar.lane-=1;
                    }
                    System.out.println("LEFT");
                    break;
                case KeyEvent.VK_RIGHT:
                    if(myCar.lane<4){
                        myCar.lane+=1;
                    }
                    System.out.println("RIGHT");
                    break;
                default:
                    break;
            }
        }
    }

    class Car extends Thread{
        int lane, carHeight,distance,velocity;
        Color color;
        float time;

        public Car(int lane, int height, Color color,int distance,int velocity) {
            this.lane=lane;
            this.carHeight = height;
            this.color = color;
            this.distance=distance;
            this.velocity = velocity;
            time=0;
        }

        public void run() {
            while (true){
           //     time+=timeUpdate/1000f;
                distance += (int)(velocity*(timeUpdate/1000f));
                if(distance>carHeight+120){
                    isLaneHasCar[lane]=false;
                }
                if(distance>height){
//                    try {
//                        Thread.sleep(600);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    System.out.println(distance);
//                    distance = randomDistance();
                    distance = -carHeight;
                    isLaneHasCar[lane]=false;
                    lane=randomLane();
//                    velocity=randomVelocity();
                    velocity= (int) velocityMax;
                }
//                System.out.println(distance);
                try {
                    Thread.sleep(timeUpdate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class MyCar {
        int lane, height,distance;
        Color color;

        public MyCar(int lane, int height, Color color,int distance) {
            this.lane=lane;
            this.height = height;
            this.color = color;
            this.distance=distance;
        }
    }
}




