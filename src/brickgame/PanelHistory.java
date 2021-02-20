package brickgame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelHistory extends JPanel{

    private final JPanel contentPane;
    private final int width;
    private final int height;
    private String[][] data;
    private static PanelHistory instance;

    private static PanelHistory getInstance(){
        return instance;
    }

    public PanelHistory(JPanel jPanel,int width,int height){
        instance = this;
        contentPane = jPanel;
        this.width=width;
        this.height=height;

        initUI();

    }

    private void initUI(){
        setLayout(new BorderLayout());

        //----top----
        Panel panelTop = new Panel();
        JLabel title = new JLabel("HISTORY");
        title.setFont(new Font("System", Font.PLAIN, 22));
        panelTop.add(title);
        add(panelTop,BorderLayout.NORTH);

        //----center----
        JPanel panelTable = new JPanel(new GridLayout(1,0));
        panelTable.setBorder(BorderFactory.createEmptyBorder(10,30,30,10));

        System.out.println("haha");
        data = new String[2][2];
      //  data = GameDBUtils.getInstance().readDB();
        System.out.println("haha");

        String[] column = { "TIME", "SCORE"};

        JTable table = new JTable(data, column);
        DefaultTableCellRenderer rendar = new DefaultTableCellRenderer();
        rendar.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(rendar);
        table.getColumnModel().getColumn(1).setCellRenderer(rendar);

        table.setRowHeight(50);

        JScrollPane scrollPane = new JScrollPane(table);
        panelTable.add(scrollPane);

        add(panelTable,BorderLayout.CENTER);

        //----bottom
        JPanel panelBottom = new JPanel();

        JButton btnPlay = new JButton("PLAY");
        btnPlay.setFont(new Font("System",Font.PLAIN,20));
        btnPlay.setPreferredSize(new Dimension(100,50));

        JButton btnBack = new JButton("BACK");
        btnBack.setFont(new Font("System",Font.PLAIN,20));
        btnBack.setPreferredSize(new Dimension(100,50));

        panelBottom.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        panelBottom.add(btnBack);
        panelBottom.add(btnPlay);
        panelBottom.setPreferredSize(new Dimension(width, 150));
        add(panelBottom,BorderLayout.SOUTH);

        btnPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelGame.getInstance().setUpData();
                CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                cardLayout.next(contentPane);
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                cardLayout.previous(contentPane);
            }
        });
    }

    public static void update(){
        System.out.println("Update history");
        instance.removeAll();
        instance.initUI();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return (new Dimension(width, height));
    }

}
