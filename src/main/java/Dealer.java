import java.util.ArrayList;

/**
 * Represents the Dealer in the Three Card Poker game.
 * Manages the deck and deals cards to players.
 */
public class Dealer {
    private Deck deck;
    private ArrayList<Card> hand;

    /**
     * Constructs a Dealer with a new deck.
     */
    public Dealer() {
        deck = new Deck();
        hand = new ArrayList<>();
    }

    /**
     * Deals cards to Player One and optionally Player Two.
     *
     * @param playerOne The first player.
     * @param playerTwo The second player (can be null for single-player mode).
     */
    public void deal(Player playerOne, Player playerTwo) {
        deck.shuffle();
        hand = deck.dealHand(3);
        playerOne.setHand(deck.dealHand(3));
        if (playerTwo != null) {
            playerTwo.setHand(deck.dealHand(3));
        }
    }

    /**
     * Returns the dealer's hand.
     *
     * @return The dealer's hand as a list of Cards.
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Resets the dealer by resetting the deck and clearing the hand.
     */
    public void reset() {
        deck.reset();
        hand.clear();
    }
}