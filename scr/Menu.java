package scr;

import javax.swing.*;
import java.awt.*;



public class Menu {

    public static void main(String[] args) {
        int size = 15;
        JFrame frame = new JFrame("Cờ Caro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        // Tạo menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem settingItem = new JMenuItem("Cài đặt");
        JMenuItem exitItem = new JMenuItem("Thoát");

        menu.add(settingItem);
        menu.add(exitItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(size, size));

        ImageIcon xIcon = new ImageIcon("x.png");
        ImageIcon oIcon = new ImageIcon("o.png");

        JButton[][] buttons = new JButton[size][size];
        boolean[] isXTurn = {true};

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton();
                int row = i, col = j;
                buttons[i][j].addActionListener(e -> {
                    if (buttons[row][col].getIcon() == null) {
                        if (isXTurn[0]) {
                            buttons[row][col].setIcon(xIcon);
                        } else {
                            buttons[row][col].setIcon(oIcon);
                        }
                        isXTurn[0] = !isXTurn[0];
                    }
                });
                panel.add(buttons[i][j]);
            }
        }

        // Xử lý sự kiện "Cài đặt"
        settingItem.addActionListener(e -> {
            String[] options = {
                "Đánh 2 người",
                "1 người đánh với máy",
                "1 người đánh với người Asia"
            };
            int choice = JOptionPane.showOptionDialog(
                frame,
                "Chọn chế độ chơi:",
                "Cài đặt",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            if (choice == 0) {
                JOptionPane.showMessageDialog(frame, "Bạn đã chọn chế độ đánh 2 người.");
            } else if (choice == 1) {
                JOptionPane.showMessageDialog(frame, "Bạn đã chọn chế độ 1 người đánh với máy.");
            } else if (choice == 2) {
                JOptionPane.showMessageDialog(frame, "Bạn đã chọn chế độ 1 người đánh với người Asia.");
            }
        });

        // Xử lý sự kiện "Thoát"
        exitItem.addActionListener(e -> System.exit(0));

        frame.add(panel);
        frame.setVisible(true);
    }
    
}