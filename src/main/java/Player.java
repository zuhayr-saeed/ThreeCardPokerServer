import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand;
    private int anteBet;
    private int pairPlusBet;
    private int winnings;

    public Player() {
        hand = new ArrayList<>();
        anteBet = 0;
        pairPlusBet = 0;
        winnings = 0;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setAnteBet(int anteBet) {
        this.anteBet = anteBet;
    }

    public void setPairPlusBet(int pairPlusBet) {
        this.pairPlusBet = pairPlusBet;
    }

    public int getAnteBet() {
        return anteBet;
    }

    public int getPairPlusBet() {
        return pairPlusBet;
    }

    public int getWinnings() {
        return winnings;
    }

    public void addWinnings(int amount) {
        winnings += amount;
    }

    public void subtractWinnings(int amount) {
        winnings -= amount;
    }

    public void reset() {
        hand.clear();
        anteBet = 0;
        pairPlusBet = 0;
        winnings = 0;
    }
}
