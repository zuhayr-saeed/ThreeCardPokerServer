import java.util.ArrayList;

/**
 * Represents a player in the Three Card Poker game.
 */
public class Player {
    private ArrayList<Card> hand;
    private int anteBet;
    private int pairPlusBet;
    private int winnings;

    /**
     * Constructs a Player with no initial hand or bets.
     */
    public Player() {
        hand = new ArrayList<>();
        anteBet = 0;
        pairPlusBet = 0;
        winnings = 0;
    }

    /**
     * Sets the player's hand.
     *
     * @param hand The list of Cards dealt to the player.
     */
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    /**
     * Returns the player's hand.
     *
     * @return The player's hand as a list of Cards.
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Sets the player's ante bet.
     *
     * @param anteBet The ante bet amount.
     */
    public void setAnteBet(int anteBet) {
        this.anteBet = anteBet;
    }

    /**
     * Sets the player's pair plus bet.
     *
     * @param pairPlusBet The pair plus bet amount.
     */
    public void setPairPlusBet(int pairPlusBet) {
        this.pairPlusBet = pairPlusBet;
    }

    /**
     * Returns the player's ante bet.
     *
     * @return The ante bet amount.
     */
    public int getAnteBet() {
        return anteBet;
    }

    /**
     * Returns the player's pair plus bet.
     *
     * @return The pair plus bet amount.
     */
    public int getPairPlusBet() {
        return pairPlusBet;
    }

    /**
     * Returns the player's total winnings.
     *
     * @return The total winnings.
     */
    public int getWinnings() {
        return winnings;
    }

    /**
     * Adds winnings to the player's total.
     *
     * @param amount The amount to add to winnings.
     */
    public void addWinnings(int amount) {
        winnings += amount;
    }
    
    /**
     * Subtracts winnings from the player's total.
     *
     * @param amount The amount to subtract from the winnings.
     */
    public void subtractWinnings(int amount) {
        winnings -= amount;
    }

    /**
     * Resets the player's hand and bets for a new round.
     */
    public void reset() {
        hand.clear();
        anteBet = 0;
        pairPlusBet = 0;
        winnings = 0;
    }
}