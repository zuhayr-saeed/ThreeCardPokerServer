import java.util.ArrayList;
import java.util.Collections;

/**
 * Implements the game logic for Three Card Poker.
 */
public class ThreeCardLogic {

    /**
     * Evaluates the hand and returns its ranking.
     *
     * @param hand The list of Cards in the hand.
     * @return An integer representing the hand's rank.
     */
    public static int evalHand(ArrayList<Card> hand) {
        boolean flush = isFlush(hand);
        boolean straight = isStraight(hand);
        boolean threeOfKind = isThreeOfKind(hand);
        boolean pair = isPair(hand);

        if (flush && straight) {
            return 1; // Straight Flush
        } else if (threeOfKind) {
            return 2; // Three of a Kind
        } else if (straight) {
            return 3; // Straight
        } else if (flush) {
            return 4; // Flush
        } else if (pair) {
            return 5; // Pair
        } else {
            return 0; // High Card
        }
    }

    // Helper methods

    /**
     * Determines if the hand is a flush.
     *
     * @param hand The list of Cards in the hand.
     * @return True if all cards have the same suit, false otherwise.
     */
    private static boolean isFlush(ArrayList<Card> hand) {
        char suit = hand.get(0).getSuit();
        for (Card card : hand) {
            if (card.getSuit() != suit) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the hand is a straight.
     *
     * @param hand The list of Cards in the hand.
     * @return True if the hand forms a straight, false otherwise.
     */
    private static boolean isStraight(ArrayList<Card> hand) {
        ArrayList<Integer> values = new ArrayList<>();
        for (Card card : hand) {
            int value = card.getValue();
            values.add(value == 14 ? 1 : value); // Treat Ace as 1 for low straight
        }
        Collections.sort(values);
        if (values.get(0) + 1 == values.get(1) && values.get(1) + 1 == values.get(2)) {
            return true;
        }
        // Check for Ace-high straight (e.g., Q-K-A)
        values.clear();
        for (Card card : hand) {
            values.add(card.getValue());
        }
        Collections.sort(values);
        return values.get(0) + 1 == values.get(1) && values.get(1) + 1 == values.get(2);
    }

    /**
     * Determines if the hand is three of a kind.
     *
     * @param hand The list of Cards in the hand.
     * @return True if all three cards have the same value, false otherwise.
     */
    private static boolean isThreeOfKind(ArrayList<Card> hand) {
        int value = hand.get(0).getValue();
        return hand.get(1).getValue() == value && hand.get(2).getValue() == value;
    }

    /**
     * Determines if the hand contains a pair.
     *
     * @param hand The list of Cards in the hand.
     * @return True if any two cards have the same value, false otherwise.
     */
    private static boolean isPair(ArrayList<Card> hand) {
        int v1 = hand.get(0).getValue();
        int v2 = hand.get(1).getValue();
        int v3 = hand.get(2).getValue();
        return v1 == v2 || v1 == v3 || v2 == v3;
    }
    
    /**
     * Determines if the dealer qualifies based on their highest card.
     *
     * @param hand The dealer's hand.
     * @return True if the dealer qualifies, false otherwise.
     */
    public static boolean dealerQualifies(ArrayList<Card> hand) {
        int highestValue = getHighCardValue(hand);
        return highestValue >= 12; // 12 represents Queen
    }

    /**
     * Evaluates the Pair Plus winnings based on the hand and bet.
     *
     * @param hand The player's hand.
     * @param bet  The pair plus bet amount.
     * @return The winnings based on the hand ranking.
     */
    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        int handValue = evalHand(hand);

        switch (handValue) {
            case 1: // Straight Flush
                return bet * 40;
            case 2: // Three of a Kind
                return bet * 30;
            case 3: // Straight
                return bet * 6;
            case 4: // Flush
                return bet * 3;
            case 5: // Pair
                return bet * 1;
            default:
                return 0; // No winnings
        }
    }

    /**
     * Compares the player's hand with the dealer's hand to determine the winner.
     *
     * @param dealer  The dealer's hand.
     * @param player  The player's hand.
     * @return 2 if player wins, 1 if dealer wins, 0 for a tie.
     */
    public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
        int dealerHandValue = evalHand(dealer);
        int playerHandValue = evalHand(player);

        if (playerHandValue > dealerHandValue) {
            return 2; // Player wins
        } else if (playerHandValue < dealerHandValue) {
            return 1; // Dealer wins
        } else {
            // Hands are of the same type, compare highest cards
            int playerHighCard = getHighCardValue(player);
            int dealerHighCard = getHighCardValue(dealer);

            if (playerHighCard > dealerHighCard) {
                return 2; // Player wins
            } else if (playerHighCard < dealerHighCard) {
                return 1; // Dealer wins
            } else {
                return 0; // Tie
            }
        }
    }

    /**
     * Retrieves the highest card value in the hand.
     *
     * @param hand The list of Cards in the hand.
     * @return The highest card value.
     */
    public static int getHighCardValue(ArrayList<Card> hand) {
        int high = 0;
        for (Card card : hand) {
            int value = card.getValue();
            if (value > high) {
                high = value;
            }
        }
        return high;
    }
}