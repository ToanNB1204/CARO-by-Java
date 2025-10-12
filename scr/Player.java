package scr;

public class Player {
    private String name;
    private String symbol; // "X" hoặc "O"
    private boolean isAI;

    public Player(String name, String symbol, boolean isAI) {
        this.name = name;
        this.symbol = symbol;
        this.isAI = isAI;
    }

    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public boolean isAI() { return isAI; }

    public void setName(String name) { this.name = name; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setAI(boolean isAI) { this.isAI = isAI; }
}
