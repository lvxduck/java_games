package brickgame;

import javax.swing.*;
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

    private final int numBrickX =4;
    private final int numBrickY =3;
    private int brickWidth;
    private int brickHeight;

    private Timer timer;
    private int timeUpdate = 50;

    private Brick[][] bricks;
    private BottomBrick bottomBrick;
    private final int bottomBrickWidth=100;
    private final int bottomBrickHeight=20;

    private Ball ball;
    private final int ballRadius=15;

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
        setUpData();



        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JLabel label = new JLabel("PLAYING......!!!!!");
        label.setFont(new Font("System",Font.PLAIN,20));
        add(label);

//
//        try{
//            final BufferedImage image = ImageIO.read(new File("E:\\Documents\\JAVA\\image.jpg"));
//            JLabel picLabel = new JLabel(new ImageIcon(image));
//            add(picLabel);
//           // repaint();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void setUpData(){
        timer = new Timer(timeUpdate,this);
        timer.start();

        brickWidth = (width-paddingHorizontal*2)/numBrickX;
        brickHeight = (height/2-paddingVertical*2)/numBrickY;

        ball = new Ball(100+bottomBrickWidth/2,height-paddingVertical-40-ballRadius,80,-150,ballRadius);
        ball.start();
        bottomBrick = new BottomBrick(100,height-paddingVertical-40);
        bricks = new Brick[numBrickX +1][numBrickY +1];
        for(int i = 1; i<= numBrickX; i++){
            for(int j = 1; j<= numBrickY; j++){
                bricks[i][j] = new Brick(i,j);
            }
        }

    }


    private void doDrawing(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("System",Font.PLAIN,24));
        g.drawString("Time: "+score,200,paddingVertical/2+10);

        g.drawRect(paddingHorizontal,paddingVertical,width-paddingHorizontal*2,height-paddingVertical*2);




        for(int i = 1; i<= numBrickX; i++){
            for(int j = 1; j<= numBrickY; j++){
                if(!bricks[i][j].isDestroy)
                    drawBrick(g,bricks[i][j]);
            }
        }

        drawBottomBrick(g,bottomBrick);
        drawBall(g,ball);
    }

    private void drawBrick(Graphics g, Brick brick){
        g.setColor(Color.lightGray);
        g.fillRect(brick.posX,brick.posY,brickWidth,brickHeight);
        g.setColor(Color.white);
        g.drawRect(brick.posX,brick.posY,brickWidth,brickHeight);
    }

    private void drawBottomBrick(Graphics g, BottomBrick bottomBrick){
        g.setColor(Color.lightGray);
        g.fillRect(bottomBrick.posX,bottomBrick.posY,bottomBrickWidth,bottomBrickHeight);
        g.setColor(Color.white);
        g.drawRect(bottomBrick.posX,bottomBrick.posY,bottomBrickWidth,bottomBrickHeight);
    }

    private void drawBall(Graphics g, Ball ball){
        g.setColor(Color.BLUE);
        g.fillOval((int)ball.x-ball.r,(int)ball.y-ball.r,ball.r*2,ball.r*2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(getMousePosition()!=null){
            bottomBrick.setPosX(getMousePosition().x-bottomBrickWidth/2);
        }
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
    class BottomBrick{
        private int posX;
        private int posY;

        public BottomBrick(int x, int y) {
            this.posX = x;
            this.posY = y;
        }

        public int getPosX() {
            return posX;
        }

        public void setPosX(int x) {
            if(x<paddingHorizontal){
                this.posX=paddingHorizontal;
            }else
            if(x+bottomBrickWidth>width-paddingHorizontal){
                this.posX=width-paddingHorizontal-bottomBrickWidth;
            }
            else{
                this.posX = x;
            }
        }
    }
    class Ball extends Thread {
        double x,y,vx,vy;
        int r;

        public Ball(double x, double y, double vx, double vy, int r) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.r = r;
        }

        public void run() {
            while (true){
                checkCollision();
                if(x<r+paddingHorizontal || x+r>=width-paddingHorizontal) vx*=-1;
                if(y<r+paddingVertical || y+r>=height-paddingVertical) vy*=-1;
                if(y+r>bottomBrick.posY && x+r>=bottomBrick.posX && x+r<=bottomBrick.posX+bottomBrickWidth) vy*=-1;

                x += vx * ((float) timeUpdate / 1000f);
                y += vy * ((float) timeUpdate / 1000f);
                try{
                    Thread.sleep(50);
                }catch (InterruptedException e){
                }
            }
        }

        private void checkCollision(){
//            int bX = (int) (x-paddingHorizontal)/brickWidth+1;
//            int bY = (int) (y-paddingVertical)/brickHeight+1;
//            if(bX<=numBrickX&&bY<=numBrickY){
//                bricks[bX][bY].isDestroy=true;
//
//            }

            for(int i = 1; i<= numBrickX; i++){
                for(int j = 1; j<= numBrickY; j++){
                    if(!bricks[i][j].isDestroy){
                        if(x-r<=bricks[i][j].posX+brickWidth && x+r>=bricks[i][j].posX
                                && y-r<=bricks[i][j].posY+brickHeight && y+r>=bricks[i][j].posY){
                            if(x>bricks[i][j].posX+brickWidth||x<bricks[i][j].posX) vx*=-1;
                            if(y>bricks[i][j].posY+brickHeight||y<bricks[i][j].posY) vy*=-1;
                            bricks[i][j].isDestroy=true;
                        }
                    }
                }
            }
        }
    }

    class Brick{
        public int x;
        public int y;
        public int posX;
        public int posY;
        public boolean isDestroy;

        public Brick(int x, int y) {
            this.x = x;
            this.y = y;
            this.posX = (x-1)*brickWidth+paddingHorizontal;
            this.posY = (y-1)*brickHeight+paddingVertical;
            this.isDestroy = false;
        }
    }
}










