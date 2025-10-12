package scr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel extends JPanel {
    private JLabel currentPlayerLabel;
    private GameFrame frame;

    public MenuPanel(GameFrame frame){
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(220,0));
        setOpaque(false);

        add(Box.createRigidArea(new Dimension(0,20)));

        JLabel avatar = new JLabel();
        avatar.setIcon(new ImageIcon("avatar.png"));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(avatar);

        JLabel playerName = new JLabel("Người X");
        playerName.setForeground(Color.WHITE);
        playerName.setFont(new Font("Arial",Font.BOLD,16));
        playerName.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(playerName);

        add(Box.createRigidArea(new Dimension(0,20)));

        currentPlayerLabel = new JLabel("Lượt đi: X");
        currentPlayerLabel.setForeground(Color.YELLOW);
        currentPlayerLabel.setFont(new Font("Arial",Font.BOLD,14));
        currentPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(currentPlayerLabel);

        add(Box.createRigidArea(new Dimension(0,30)));

        // Chỉ còn 3 nút: Undo, Restart, Exit
        String[] buttons = {"Undo","Restart","Exit"};

        for(String text: buttons){
            JButton btn = new JButton(text);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180,45));
            btn.setFocusPainted(false);
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(50,50,50));
            btn.setFont(new Font("Arial",Font.BOLD,14));
            btn.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));

            btn.addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){ btn.setBackground(new Color(70,130,180)); }
                public void mouseExited(MouseEvent e){ btn.setBackground(new Color(50,50,50)); }
            });

            btn.addActionListener(e->{
                switch(text){
                    case "Undo": frame.undoMove(); break;
                    case "Restart": frame.restartGame(); break;
                    case "Exit": System.exit(0); break;
                }
            });

            add(Box.createRigidArea(new Dimension(0,15)));
            add(btn);
        }
    }

    public void updateCurrentPlayer(String symbol){
        currentPlayerLabel.setText("Lượt đi: " + symbol);
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = new Color(40,40,40);
        Color color2 = new Color(20,20,60);
        GradientPaint gp = new GradientPaint(0,0,color1,0,getHeight(),color2);
        g2d.setPaint(gp);
        g2d.fillRect(0,0,getWidth(),getHeight());
    }
}
