package puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class PanelGame extends JPanel  implements ActionListener {
    public static PanelGame instance;
    public static HandleKey handleKey;
    private final JPanel contentPane;
    private final int width;
    private final int height;
    public final int paddingVertical;
    public final int paddingHorizontal;
    private int time =0;
    private final int size=3;
    private final int puzzleSize;
    private Puzzle[][] puzzles;
    private boolean isEndGame = false;
    int timeUpdate=1000;
    private Puzzle emptyPuzzle;

    public static Timer timer;

    public static PanelGame getInstance(){
        return instance;
    }

    public void setUpData(){
        System.out.println("setUp");
        time=0;
        timer.start();
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("System",Font.PLAIN,24));
        g.drawString("Time: "+time,120,paddingHorizontal/2);
        if(isWin()&&!isEndGame){
            isEndGame=true;
            endGame();
        }
        g.drawRect(paddingVertical,paddingHorizontal,width-paddingVertical*2,height-paddingHorizontal*2);

        for(int i=1;i<=size;i++){
            for(int j=1;j<=size;j++){
                if(!puzzles[i][j].content.equals("9")) drawPuzzle(g,puzzles[i][j]);
            }
        }
    }

    private void drawPuzzle(Graphics g,Puzzle puzzle){
        g.setColor(Color.lightGray);
        g.fillRect((puzzle.xCurrent-1)*puzzleSize+paddingVertical,
                (puzzle.yCurrent-1)*puzzleSize+paddingHorizontal,
                puzzleSize,puzzleSize);
        g.setColor(Color.BLACK);
        g.setFont(new Font("System",Font.PLAIN,24));
        g.drawString(puzzle.content,
                (puzzle.xCurrent-1)*puzzleSize+paddingVertical+puzzleSize/2,
                (puzzle.yCurrent-1)*puzzleSize+paddingHorizontal+puzzleSize/2);
        g.setColor(Color.white);
        g.drawRect((puzzle.xCurrent-1)*puzzleSize+paddingVertical,
                (puzzle.yCurrent-1)*puzzleSize+paddingHorizontal,
                puzzleSize,puzzleSize);

    }

    private void randomPuzzle(){

//        int[] randoms = randomArray(size*size);
        int[] randoms = {0,1,2,3,4,5,6,7,9,8};


        for(int i=1;i<=size;i++){
            for(int j=1;j<=size;j++){
                int random = randoms[(j - 1) * size + i];
                puzzles[i][j]=new Puzzle(i,j,random+"");
                if(random==9) emptyPuzzle = new Puzzle(i,j,random+"");
            }
        }
    }

    private int[] randomArray(int n){
        Random generator = new Random();
        int[] arr= new int[n+1];
        for(int i=1;i<=n;i++) arr[i]=i;
        for(int i=1;i<=7;i++){
            int x = generator.nextInt(size)+1;
            int y = generator.nextInt(size)+1;
            int tam = arr[x];
            arr[x]=arr[y];
            arr[y]=tam;
        }
        return arr;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public PanelGame(JPanel jPanel, int width, int height){
        instance = this;
        handleKey = new HandleKey();
        contentPane = jPanel;
        this.width=width;
        this.height=height;
        puzzles = new Puzzle[size+1][size+1];
        for(int i=1;i<=size;i++){
            for(int j=1;j<=size;j++){
                puzzles[i][j]=new Puzzle(i,j,(j-1)*size+i+"");
            }
        }
        randomPuzzle();
        paddingVertical=20;
        paddingHorizontal=(height-(width-paddingVertical*2))/2;
        puzzleSize = (width-paddingVertical*2)/3;

        timer = new Timer(timeUpdate, this);
        timer.stop();

        initUi();
    }

    private void initUi(){
        setBorder(BorderFactory.createEmptyBorder(height-50,10,10,10));
        JLabel label = new JLabel("PLAYING......!!!!!");
        label.setFont(new Font("System",Font.PLAIN,20));
        add(label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        time+=1;
    }

    private void endGame(){

        timer.stop();
        repaint();
        JOptionPane.showMessageDialog(this,"END GAME");

        System.out.println("YOU WIN");

        GameDBUtils.getInstance().writeDB(this.time);
        this.time=0;

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

    private void hoanvi(Puzzle p1, Puzzle p2){
        Puzzle temp = new Puzzle(p1.xCurrent,p1.yCurrent,p1.content);
        System.out.println("Empty");
        System.out.println(p1.xCurrent+" , "+p1.yCurrent);
        p1.xCurrent = p2.xCurrent;
        p1.yCurrent = p2.yCurrent;
        p1.content = p2.content;

        p2.xCurrent = temp.xCurrent;
        p2.yCurrent = temp.yCurrent;
        p2.content = temp.content;
        System.out.println(p1.xCurrent+" , "+p1.yCurrent);

    }

    private boolean isWin(){
        for(int i=1;i<=size;i++){
            for(int j=1;j<=size;j++){
                if(!puzzles[i][j].content.equals((j-1)*size+i+"")){
                    return false;
                }
            }
        }
        return true;
    }

    //puzzles[size][size] is a empty puzzle
    private void moveLeft(){
        System.out.println(emptyPuzzle.xCurrent+" , "+emptyPuzzle.yCurrent);
        if(emptyPuzzle.xCurrent!=size){
            int px = emptyPuzzle.xCurrent+1;
            int py = emptyPuzzle.yCurrent;
            puzzles[px][py].xCurrent-=1;
            puzzles[emptyPuzzle.xCurrent][emptyPuzzle.yCurrent].xCurrent+=1;
            hoanvi(puzzles[px][py],puzzles[emptyPuzzle.xCurrent][emptyPuzzle.yCurrent]);

            emptyPuzzle.xCurrent+=1;
        }
        repaint();
    }

    private void moveRight(){
        System.out.println(emptyPuzzle.xCurrent+" , "+emptyPuzzle.yCurrent);
        if(emptyPuzzle.xCurrent!=1){
            int px = emptyPuzzle.xCurrent-1;
            int py = emptyPuzzle.yCurrent;
            puzzles[px][py].xCurrent+=1;
            puzzles[emptyPuzzle.xCurrent][emptyPuzzle.yCurrent].xCurrent-=1;
            hoanvi(puzzles[px][py],puzzles[emptyPuzzle.xCurrent][emptyPuzzle.yCurrent]);

            emptyPuzzle.xCurrent-=1;
        }
        repaint();
    }

    private void moveDown(){
        System.out.println(emptyPuzzle.xCurrent+" , "+emptyPuzzle.yCurrent);
        if(emptyPuzzle.yCurrent!=1){
            int px = emptyPuzzle.xCurrent;
            int py = emptyPuzzle.yCurrent-1;
            puzzles[px][py].yCurrent+=1;
            puzzles[emptyPuzzle.xCurrent][emptyPuzzle.yCurrent].yCurrent-=1;
            hoanvi(puzzles[px][py],puzzles[emptyPuzzle.xCurrent][emptyPuzzle.yCurrent]);

            emptyPuzzle.yCurrent-=1;
        }
        repaint();
    }

    private void moveUp(){
        System.out.println(emptyPuzzle.xCurrent+" , "+emptyPuzzle.yCurrent);
        if(emptyPuzzle.yCurrent!=size){
            int px = emptyPuzzle.xCurrent;
            int py = emptyPuzzle.yCurrent+1;
            puzzles[px][py].yCurrent-=1;
            puzzles[emptyPuzzle.xCurrent][emptyPuzzle.yCurrent].yCurrent+=1;
            hoanvi(puzzles[px][py],puzzles[emptyPuzzle.xCurrent][emptyPuzzle.yCurrent]);

            emptyPuzzle.yCurrent+=1;
        }
        repaint();
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
                    moveLeft();
                    System.out.println("LEFT");
                    break;
                case KeyEvent.VK_RIGHT:
                    moveRight();
                    System.out.println("RIGHT");
                    break;
                case KeyEvent.VK_DOWN:
                    moveDown();
                    System.out.println("DOWN");
                    break;
                case KeyEvent.VK_UP:
                    moveUp();
                    System.out.println("UP");
                    break;
                default:
                    break;
            }
        }
    }
}

class Puzzle{
    public int x;
    public int y;
    public int xCurrent;
    public int yCurrent;
    public String content;

    public Puzzle(int x, int y, String content) {
        this.x = x;
        this.y = y;
        this.xCurrent=x;
        this.yCurrent=y;
        this.content = content;
    }
}

