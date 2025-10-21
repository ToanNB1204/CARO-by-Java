package scr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class GameFrame extends JFrame {
    private JPanel boardContainer;
    private MenuPanel menuPanel;
    private GameEngine engine;
    private BoardPanel boardPanel;
    private Stack<Point> moveHistory = new Stack<>();
    private AI ai;
    private final int BOARD_SIZE = 50;

    private GameEngine.Cell playerSymbol = GameEngine.Cell.X;
    private GameEngine.Cell aiSymbol = GameEngine.Cell.O;

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

        choosePlayerSymbol();
        startGame();
    }

    public void startGame() {
        Player human = new Player("Người " + playerSymbol, playerSymbol.toString(), false);
        Player computer = new Player("Máy " + aiSymbol, aiSymbol.toString(), true);

        ai = new AI(BOARD_SIZE, 5);
        engine = new GameEngine(human, computer);
        engine.setPlayerSymbol(playerSymbol);
        engine.setAISymbol(aiSymbol);

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

        // Nếu người chọn O thì AI đi trước
        if (playerSymbol == GameEngine.Cell.O) {
            Point aiMove = ai.getBestMove(engine.getBoard(), aiSymbol);
            engine.makeMove(aiMove, aiSymbol);
            boardPanel.repaint();
        }
    }


    public void undoMove() {
        if (moveHistory.isEmpty()) return;

        // Undo 2 nước gần nhất (người + AI)
        for (int i = 0; i < 2 && !moveHistory.isEmpty(); i++) {
            Point last = moveHistory.pop();
            engine.getBoard().remove(last);         // Xóa nước đi khỏi GameEngine
            ai.setCell(last.y, last.x, 0);         // Đồng bộ AI
            engine.switchTurn();                    // Đổi lượt về người chơi
        }

        // Xóa highlight nước đi cuối và vẽ lại bàn cờ
        boardPanel.repaint();
    }

    private void choosePlayerSymbol() {
        String[] options = {"X", "O"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose your symbol:",
                "Player Choice",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            playerSymbol = GameEngine.Cell.X;
            aiSymbol = GameEngine.Cell.O;
        } else {
            playerSymbol = GameEngine.Cell.O;
            aiSymbol = GameEngine.Cell.X;
        }
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
