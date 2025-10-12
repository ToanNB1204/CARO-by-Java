package scr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import java.awt.Point;

public class GameFrame extends JFrame {
    private JPanel boardContainer;
    private MenuPanel menuPanel;
    private GameEngine engine;
    private BoardPanel boardPanel;
    private Stack<Point> moveHistory = new Stack<>();
    private AI ai;
    private final int BOARD_SIZE = 50;

    public GameFrame() {
        setTitle("Cờ Caro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu bên trái
        menuPanel = new MenuPanel(this);
        add(menuPanel, BorderLayout.WEST);

        // Container bàn cờ
        boardContainer = new JPanel(new BorderLayout());
        add(boardContainer, BorderLayout.CENTER);

        setSize(1200, 900);
        setLocationRelativeTo(null);
        setVisible(true);

        startGame();
    }

    public void startGame() {
        Player human = new Player("Người X", "X", false);
        Player computer = new Player("Máy O", "O", true);

        // AI đồng bộ với board
        ai = new AI(BOARD_SIZE, 5);

        engine = new GameEngine(human, computer);

        boardPanel = new BoardPanel(engine, ai) {
            @Override
            public void repaint() {
                super.repaint();
                menuPanel.updateCurrentPlayer(engine.getCurrentPlayer().getSymbol());
            }
        };

        moveHistory.clear();
        boardContainer.removeAll();
        boardContainer.add(boardPanel, BorderLayout.CENTER);
        boardContainer.revalidate();
        boardContainer.repaint();
        boardPanel.requestFocusInWindow();
    }

    public void undoMove() {
        if (moveHistory.isEmpty()) return;

        Point last = moveHistory.pop();
        engine.getBoard().remove(last);

        // Đồng bộ AI
        ai.setCell(last.y, last.x, 0);

        engine.switchTurn();
        boardPanel.repaint();
    }

    public void restartGame() {
        startGame();
    }

    public void recordMove(Point p) {
        moveHistory.push(p);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}
