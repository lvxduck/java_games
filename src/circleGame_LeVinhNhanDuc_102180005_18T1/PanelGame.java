package circleGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class PanelGame extends JPanel  implements ActionListener {
    private static PanelGame instance;

    public static HandleKey handleKey;
    private final JPanel contentPane;
    private final int width;
    private final int height;
    public final int paddingVertical=60;
    public final int paddingHorizontal=20;
    Timer timer;
    int timeUpdate=2000;
    int numberOfCircle;

    private Circle[] circles;
    private int radiusMax=50;
    private int radiusMin=10;

    private boolean isEndGame;
    private boolean isStartGame;

    private float score=0;

    public static PanelGame getInstance(){
        return instance;
    }

    public PanelGame(JPanel jPanel, int width, int height){
        instance=this;
        handleKey = new HandleKey();
        addMouseListener(new HandleMouse());
        contentPane = jPanel;
        this.width=width;
        this.height=height;
        setUpData();

        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JButton btnStart = new JButton("Bat Dau");
        JButton btnStop = new JButton("Tam Dung");
        JButton btnHistory = new JButton("Xem Lich Su");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isStartGame=true;
                timer.start();
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
            }
        });
        btnHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                PanelHistory.update();
                cardLayout.previous(contentPane);
            }
        });
        add(btnStart);
        add(btnStop);
        add(btnHistory);

    }

    public void setUpData(){
        isEndGame = false;
        isStartGame = false;
        numberOfCircle=1;
        circles = new Circle[20];
        setUpCircle(numberOfCircle);
        timer = new Timer(timeUpdate,this);
    }

    void setUpCircle(int n){
        for(int i=0;i<n;i++){
            int radius = randomRadius();
            circles[i] = new Circle(randomPosWidth(radius),randomPosHeight(radius),radius);
        }
    }

    int randomRadius(){
        Random random = new Random();
        return random.nextInt(radiusMax-radiusMin)+radiusMin;
    }

    int randomPosWidth(int radius){
        Random random = new Random();
        return random.nextInt(width-paddingHorizontal*2-radius*2)+paddingHorizontal;
    }

    int randomPosHeight(int radius){
        Random random = new Random();
        return random.nextInt(height-paddingVertical*2-radius*2)+paddingVertical;
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("System",Font.PLAIN,24));
        g.drawString("SCORE: "+(int)score,140,30);

        g.drawString("N = "+numberOfCircle,340,30);

        g.drawRect(paddingHorizontal,paddingVertical,width-paddingHorizontal*2,height-paddingVertical*2);

        g.setColor(Color.BLUE);
        for(Circle circle : circles){
            if(circle!=null&&!circle.isDestroy){
                g.drawOval(circle.x,circle.y,circle.r*2,circle.r*2);
            }
        }
    }

    void checkClick(int posX,int posY){
        if(isStartGame){
            for(Circle circle : circles){
                if(circle!=null&&!circle.isDestroy){
                    float distance = (float) Math.sqrt(Math.pow(Math.abs(posX-(circle.x+circle.r)),2)+Math.pow(Math.abs(posY-(circle.y+circle.r)),2));
                    System.out.println(distance);
                    if(distance<=circle.r){
                        circle.isDestroy=true;
                        score+=100f/circle.r;
                        repaint();
                        return;
                    }
                }
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        numberOfCircle+=1;
        setUpCircle(numberOfCircle);
        if(numberOfCircle>=10){
            endGame();
        }else{
            repaint();
        }
    }

    private void endGame(){
        if(!isEndGame){
            isEndGame=true;
            timer.stop();
            GameDBUtils.getInstance().writeDB((int)score);
            JOptionPane.showMessageDialog(this,"Your Score:"+(int)score);
        }
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
    class HandleMouse implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            checkClick(e.getX(),e.getY());
         //   getPuzzleClick(e.getPoint().x,e.getPoint().y);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    class Circle{
        int x,y,r;
        boolean isDestroy;

        public Circle(int x, int y, int r) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.isDestroy = false;
        }
    }

}




