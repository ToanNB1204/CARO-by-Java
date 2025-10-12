package scr;

import java.awt.Point;
import java.util.*;

public class GameEngine {
    public enum Cell { X,O }

    private Map<Point, Cell> board = new HashMap<>();
    private Player currentPlayer;
    private Player player1, player2;
    private Point lastPlayerMove = null; // Nước đi cuối của người chơi

    public GameEngine(Player p1, Player p2){
        player1 = p1;
        player2 = p2;
        currentPlayer = p1;
    }

    public Map<Point, Cell> getBoard(){ return board; }
    public Player getCurrentPlayer(){ return currentPlayer; }
    public Point getLastMoveOfPlayer(){ return lastPlayerMove; }

    public boolean makeMove(Point p){
        if(board.containsKey(p)) return false;
        board.put(p, currentPlayer.getSymbol().equals("X") ? Cell.X : Cell.O);
        if(!currentPlayer.isAI()) lastPlayerMove = p;
        return true;
    }

    public void switchTurn(){
        currentPlayer = (currentPlayer==player1? player2: player1);
    }

    public boolean checkWin(Point p){
        Cell c = board.get(p);
        int[][] dirs={{1,0},{0,1},{1,1},{1,-1}};
        for(int[] d: dirs){
            int count = 1 + countDir(p,d[0],d[1],c) + countDir(p,-d[0],-d[1],c);
            if(count >= 5) return true;
        }
        return false;
    }

    public List<Point> getWinningLine(Point p){
        Cell c = board.get(p);
        int[][] dirs={{1,0},{0,1},{1,1},{1,-1}};
        for(int[] d: dirs){
            List<Point> line = new ArrayList<>();
            line.add(p);
            line.addAll(getLineDir(p,d[0],d[1],c));
            line.addAll(getLineDir(p,-d[0],-d[1],c));
            if(line.size()>=5) return line.subList(0,5);
        }
        return new ArrayList<>();
    }

    private int countDir(Point p, int dx, int dy, Cell c){
        int cnt=0, x=p.x+dx, y=p.y+dy;
        while(board.getOrDefault(new Point(x,y),null)==c){ cnt++; x+=dx; y+=dy; }
        return cnt;
    }

    private List<Point> getLineDir(Point p, int dx, int dy, Cell c){
        List<Point> line = new ArrayList<>();
        int x=p.x+dx, y=p.y+dy;
        while(board.getOrDefault(new Point(x,y),null)==c){ line.add(new Point(x,y)); x+=dx; y+=dy; }
        return line;
    }
}
