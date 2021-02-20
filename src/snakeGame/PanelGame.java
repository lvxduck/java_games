package snakeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class PanelGame extends JPanel  implements ActionListener {
    public static HandleKey handleKey;
    private final JPanel contentPane;
    private final int width;
    private final int height;
    private final int numX;
    private final int padding=20;
    private final int paddingTop=60;
    private final int numY;
    private final int size=20;

    private boolean isEatFood=true;
    private Body food=new Body(5,5);
    private Body tail=new Body(5,5);
    private boolean[][] isBody;

    public static Timer timer;

    int timeUpdate=100;
    int direction = 0;
    int score = 0;
    String time;

    ArrayList<Body> bodies;

    private void doDrawing(Graphics g) {

        g.setColor(Color.BLACK);
        g.setFont(new Font("System",Font.PLAIN,32));
        g.drawString("Score: "+score,120,paddingTop/2+10);

        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(padding,paddingTop,width-padding*2,height-paddingTop*2);
        for (int i=0;i<numX;i++){
            g.drawLine(i*size+padding,paddingTop,i*size+padding,height-paddingTop);
        }
        for (int i=0;i<numY;i++){
            g.drawLine(padding,i*size+paddingTop,width-padding,i*size+paddingTop);
        }

        g.setColor(Color.BLACK);
        for (Body body : bodies) {
            drawBody(g,body);
        }

        g.setColor(Color.red);
        if(isEatFood){
            generateFood();
            isEatFood=false;
        }else{
            drawBody(g,food);
        }
    }

    private void generateFood(){
        Random generator = new Random();
        int x,y;
        x=generator.nextInt(numX-2)+1;
        y=generator.nextInt(numY-2)+1;
        while (!isValidIndexFood(x,y)){
            x=generator.nextInt(numX);
            y=generator.nextInt(numY);
        }
        food.x=x;
        food.y=y;
        System.out.println("X="+x+", Y="+y);
    }

    private boolean isValidIndexFood(int x, int y){
        if(isBody[x][y]) return false;
        return true;
    }

    private void drawBody(Graphics g, Body body){
        g.fillRect((body.x-1)*size+padding,(body.y-1)*size+paddingTop,size,size);
        g.setColor(Color.GRAY);
        g.drawRect((body.x-1)*size+padding,(body.y-1)*size+paddingTop,size,size);
        g.setColor(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public PanelGame(JPanel jPanel, int width, int height){
        handleKey = new HandleKey();
        contentPane = jPanel;
        isEatFood=true;
        this.width=width;
        this.height=height;
        this.numX=(width-padding*2)/size;
        this.numY=(height-paddingTop*2)/size;
        isBody = new boolean[numX+1][numY+1];
        bodies = new ArrayList<>();

//        this.addKeyListener(handleKey);
//        this.setFocusable(true);
        timer = new Timer(timeUpdate, this);
        timer.start();

        bodies.add(new Body(8,8));

        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JLabel label = new JLabel("PLAYING......!!!!!");
        label.setFont(new Font("System",Font.PLAIN,20));
        add(label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timeUpdate-=2;
        tail.x = bodies.get(bodies.size()-1).x;
        tail.y = bodies.get(bodies.size()-1).y;
        switch (direction){
            case 1:
                moveLeft();
                break;
            case 2:
                moveRight();
                break;
            case 3:
                moveDown();
                break;
            case 4:
                moveUp();
                break;
            default:
                break;
        }
        if(bodies.get(0).x==food.x&&bodies.get(0).y==food.y){
            isEatFood=true;
            score+=1;
            bodies.add(new Body(tail.x,tail.y));
            isBody[tail.x][tail.y]=false;
        }
        if(isBody[bodies.get(0).x][bodies.get(0).y]&&bodies.size()>1){
            endGame();
        }else{
            repaint();
        }
    }

    private void endGame(){
        timer.stop();


        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String time = dateFormat.format(date);
        GameDBUtils.getInstance().writeDB(score,time);

        removeAll();
        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JLabel jLabel1 = new JLabel("YOU LOSE!!");
        add(jLabel1);
        JButton btnStop = new JButton("PLAY AGAIN");
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                timer.start();
                score = 0;
                bodies = new ArrayList<>();
                isBody = new boolean[numX+1][numY+1];
                isEatFood=true;
                food=new Body(5,5);
                tail=new Body(5,5);
                bodies.add(new Body(8,8));
                direction = 0;
                removeAll();
                cardLayout.previous(contentPane);
                PanelHistory.update();
            }
        });
        add(btnStop);
        JLabel jLabel2 = new JLabel("YOU LOSE!!");
        add(jLabel2);
        updateUI();

    }

    // 1:left
    // 2:right
    // 3:down
    // 4:up
    private void moveLeft(){
        if(bodies.size()>1) isBody[bodies.get(bodies.size()-1).x][bodies.get(bodies.size()-1).y]=false;
        for (int i=bodies.size()-1;i>0 ;i--) {
            bodies.get(i).x=bodies.get(i-1).x;
            bodies.get(i).y=bodies.get(i-1).y;
        }
        bodies.get(0).x = bodies.get(0).x<=1?numX:bodies.get(0).x-1;
        if(bodies.size()>1){
            isBody[bodies.get(1).x][bodies.get(1).y]=true;
        }
    }

    private void moveRight(){
        if(bodies.size()>1)isBody[bodies.get(bodies.size()-1).x][bodies.get(bodies.size()-1).y]=false;
        for (int i=bodies.size()-1;i>0 ;i--) {
            bodies.get(i).x=bodies.get(i-1).x;
            bodies.get(i).y=bodies.get(i-1).y;
        }
        bodies.get(0).x = bodies.get(0).x>=numX?1:bodies.get(0).x+1;
        if(bodies.size()>1){
            isBody[bodies.get(1).x][bodies.get(1).y]=true;
        }
    }

    private void moveDown(){
        if(bodies.size()>1)isBody[bodies.get(bodies.size()-1).x][bodies.get(bodies.size()-1).y]=false;
        for (int i=bodies.size()-1;i>0 ;i--) {
            bodies.get(i).x=bodies.get(i-1).x;
            bodies.get(i).y=bodies.get(i-1).y;
        }
        bodies.get(0).y = bodies.get(0).y>=numY?1:bodies.get(0).y+1;
        if(bodies.size()>1){
            isBody[bodies.get(1).x][bodies.get(1).y]=true;
        }
    }

    private void moveUp(){
        if(bodies.size()>1)isBody[bodies.get(bodies.size()-1).x][bodies.get(bodies.size()-1).y]=false;
        for (int i=bodies.size()-1;i>0 ;i--) {
            bodies.get(i).x=bodies.get(i-1).x;
            bodies.get(i).y=bodies.get(i-1).y;
        }
        bodies.get(0).y = bodies.get(0).y<=1?numY:bodies.get(0).y-1;
        if(bodies.size()>1){
            isBody[bodies.get(1).x][bodies.get(1).y]=true;
        }
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
                    if(direction!=2)
                        direction=1;
                    System.out.println("LEFT");
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!=1)
                        direction=2;
                    System.out.println("RIGHT");
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!=4)
                        direction=3;
                    System.out.println("DOWN");
                    break;
                case KeyEvent.VK_UP:
                    if(direction!=3)
                        direction = 4;
                    System.out.println("UP");
                    break;
                default:
                    break;
            }
        }
    }
}

class Body {
    public int x;
    public int y;

    public Body(int x, int y) {
        this.x = x;
        this.y = y;
    }
}



