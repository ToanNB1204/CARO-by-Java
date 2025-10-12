package scr;

import javax.swing.*; //Import Swing Library for GUI components
import java.awt.*; //Import AWT Library for GUI components



public class Board {
    //Constant Variables
    int size = 50;
    JFrame frame = new JFrame("Cờ Caro");
    JPanel panel = new JPanel();
    ImageIcon iconX = new ImageIcon("assests/x.png");
    ImageIcon iconO = new ImageIcon("assests/o.png");
    JButton[][] buttons = new JButton[size][size];
    boolean[] isXTurn = {true};

    public Board() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);

        panel.setLayout(new GridLayout(size,size));

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton();
                int row = i, col = j;
                buttons[i][j].addActionListener(e -> {
                    if (buttons[row][col].getIcon() == null) { // Nếu ô chưa đánh
                        if (isXTurn[0]) {
                            buttons[row][col].setIcon(iconX);
                        } else {
                            buttons[row][col].setIcon(iconO);
                        }
                        isXTurn[0] = !isXTurn[0]; // Đổi lượt
                    }
                });
                panel.add(buttons[i][j]);
            }
        }
        frame.add(panel);
        frame.setVisible(true);
    }
}