package connectPuzzle;

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
    private Timer timer;

    private int sizeY=3;
    private int sizeX=4;
    private float time=0;
    private int puzzleSizeX;
    private int puzzleSizeY;
    private int timeUpdate=500;
    private Puzzle[][] puzzles;
    private Puzzle puzzle1;
    private Puzzle puzzle2;
    private int numPuzzleOpen;

    public static PanelGame getInstance(){
        return instance;
    }

    public PanelGame(JPanel jPanel, int width, int height){
        instance=this;
        handleKey = new HandleKey();
        contentPane = jPanel;
        this.width=width;
        this.height=height;
        addMouseListener(new HandleMouse());
        timer = new Timer(timeUpdate, this);
        timer.stop();

        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JLabel label = new JLabel("PLAYING......!!!!!");
        label.setFont(new Font("System",Font.PLAIN,20));
        add(label);
    }


    public void setUpData(){
        timer.start();
        time=0;
        numPuzzleOpen=0;
        puzzleSizeX=(width-paddingHorizontal*2)/sizeX;
        puzzleSizeY=(height-paddingVertical*2)/sizeY;
        int[] randoms = randomArray(sizeX*sizeY);
        System.out.println(puzzleSizeX+","+puzzleSizeY);
        puzzles=new Puzzle[sizeX+1][sizeY+1];
        for (int i=1;i<=sizeX;i++){
            for(int j=1;j<=sizeY;j++){
                puzzles[i][j] = new Puzzle(i,j,randoms[(i-1)*sizeY+j]+"");
            }
        }
    }

    private int[] randomArray(int n){
        Random generator = new Random();
        int[] arr= new int[n+1];
        for(int i=1;i<=n/2;i++) arr[i]=i;
        for(int i=1;i<=n/2;i++) arr[i+n/2]=i;
        for(int i=1;i<=7;i++){
            int x = generator.nextInt(n)+1;
            int y = generator.nextInt(n)+1;
            int tam = arr[x];
            arr[x]=arr[y];
            arr[y]=tam;
        }
        return arr;
    }

    private void drawPuzzle(Graphics g, Puzzle puzzle){
        g.setColor(Color.lightGray);
        g.fillRect((puzzle.x-1)*puzzleSizeX+paddingHorizontal,
                (puzzle.y-1)*puzzleSizeY+paddingVertical,
                puzzleSizeX,puzzleSizeY);
        g.setColor(Color.white);
        g.drawRect((puzzle.x-1)*puzzleSizeX+paddingHorizontal,
                (puzzle.y-1)*puzzleSizeY+paddingVertical,
                puzzleSizeX,puzzleSizeY);

    }

    private void drawContent(Graphics g, Puzzle puzzle){
        g.setColor(Color.BLACK);
        g.setFont(new Font("System",Font.PLAIN,24));
        g.drawString(puzzle.content,
                (puzzle.x-1)*puzzleSizeX+paddingHorizontal+puzzleSizeX/2-10,
                (puzzle.y-1)*puzzleSizeY+paddingVertical+puzzleSizeY/2+5);
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("System",Font.PLAIN,24));
        g.drawString("Time: "+(int)time,200,paddingVertical/2+10);

        g.drawRect(paddingHorizontal,paddingVertical,width-paddingHorizontal*2,height-paddingVertical*2);

        for (int i=1;i<=sizeX;i++){
            for(int j=1;j<=sizeY;j++){
                if(!puzzles[i][j].isClear){
                    if(puzzles[i][j].isOpen){
                        drawPuzzle(g,puzzles[i][j]);
                        drawContent(g,puzzles[i][j]);
                    }
                    else{
                        drawPuzzle(g,puzzles[i][j]);
                    }
                }
            }
        }
    }

    private void getPuzzleClick(int pointX, int pointY){
        if(numPuzzleOpen<2){
            if(pointX<paddingHorizontal||pointX>width-paddingHorizontal) return;
            if(pointY<paddingVertical||pointY>height-paddingVertical) return;
            int x,y;
            x = (pointX-paddingHorizontal)/puzzleSizeX+1;
            y = (pointY-paddingVertical)/puzzleSizeY+1;
            System.out.println("click: "+x+" , "+y);
            if(!puzzles[x][y].isOpen){
                if(numPuzzleOpen==0) puzzle1 = puzzles[x][y];
                if(numPuzzleOpen==1) puzzle2 = puzzles[x][y];
                puzzles[x][y].isOpen=true;
                numPuzzleOpen+=1;
                repaint();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        time+=(float) timeUpdate/1000f;
        if(numPuzzleOpen==2){
            numPuzzleOpen=0;
            if(puzzle1.content.equals(puzzle2.content)){
                puzzle1.isClear=true;
                puzzle2.isClear=true;
            }
            if(checkEndGame()){
                endGame();
            }
            puzzle1.isOpen=false;
            puzzle2.isOpen=false;
        }


    }

    private boolean checkEndGame(){
        for (int i=1;i<=sizeX;i++){
            for(int j=1;j<=sizeY;j++){
                if(!puzzles[i][j].isClear) return false;
            }
        }
        return true;
    }

    private void endGame(){
        timer.stop();

        JOptionPane.showMessageDialog(this,"END GAME");
        GameDBUtils.getInstance().writeDB((int) this.time);

        removeAll();
        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JLabel jLabel1 = new JLabel("YOU LOSE!!");
        add(jLabel1);
        JButton btnPlayAgain = new JButton("PLAY AGAIN");
        add(btnPlayAgain);
        btnPlayAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                timer.start();
                PanelHistory.update();
                cardLayout.previous(contentPane);
            }
        });
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

    class HandleMouse implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("CLICK");
            getPuzzleClick(e.getPoint().x,e.getPoint().y);
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
}

class Puzzle{
    public int x;
    public int y;
    public Boolean isOpen;
    public Boolean isClear;
    public String content;

    public Puzzle(int x, int y, String content) {
        this.x = x;
        this.y = y;
        this.content = content;
        isClear=false;
        isOpen=false;
    }
}



