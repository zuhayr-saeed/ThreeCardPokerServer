import java.util.ArrayList;
import java.util.Collections;

public class ThreeCardLogic {

    public static int evalHand(ArrayList<Card> hand) {
        boolean flush = isFlush(hand);
        boolean straight = isStraight(hand);
        boolean threeOfKind = isThreeOfKind(hand);
        boolean pair = isPair(hand);

        if (flush && straight) return 1; // Straight Flush
        else if (threeOfKind) return 2; // Three of a Kind
        else if (straight) return 3; // Straight
        else if (flush) return 4; // Flush
        else if (pair) return 5; // Pair
        else return 0; // High Card
    }

    private static boolean isFlush(ArrayList<Card> hand) {
        char suit = hand.get(0).getSuit();
        for (Card c : hand) {
            if (c.getSuit() != suit) return false;
        }
        return true;
    }

    private static boolean isStraight(ArrayList<Card> hand) {
        ArrayList<Integer> values = new ArrayList<>();
        for (Card c : hand) {
            int val = c.getValue() == 14 ? 1 : c.getValue();
            values.add(val);
        }
        Collections.sort(values);
        if (values.get(0)+1 == values.get(1) && values.get(1)+1 == values.get(2)) return true;

        // Check Ace high straight
        values.clear();
        for (Card c : hand) values.add(c.getValue());
        Collections.sort(values);
        return (values.get(0)+1 == values.get(1) && values.get(1)+1 == values.get(2));
    }

    private static boolean isThreeOfKind(ArrayList<Card> hand) {
        int v = hand.get(0).getValue();
        return hand.get(1).getValue() == v && hand.get(2).getValue() == v;
    }

    private static boolean isPair(ArrayList<Card> hand) {
        int v1=hand.get(0).getValue(), v2=hand.get(1).getValue(), v3=hand.get(2).getValue();
        return (v1==v2 || v1==v3 || v2==v3);
    }

    public static boolean dealerQualifies(ArrayList<Card> hand) {
        int highestValue = getHighCardValue(hand);
        return highestValue >= 12; // Queen or better
    }

    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        int handValue = evalHand(hand);
        switch (handValue) {
            case 1: return bet*40; // Straight Flush
            case 2: return bet*30; // Three of a Kind
            case 3: return bet*6;  // Straight
            case 4: return bet*3;  // Flush
            case 5: return bet;    // Pair
            default: return 0;     // No pair
        }
    }

    public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
        int dealerVal = evalHand(dealer);
        int playerVal = evalHand(player);
        if (playerVal > dealerVal) return 2; // player wins
        else if (playerVal < dealerVal) return 1; // dealer wins
        else {
            int playerHigh = getHighCardValue(player);
            int dealerHigh = getHighCardValue(dealer);
            if (playerHigh > dealerHigh) return 2;
            else if (playerHigh < dealerHigh) return 1;
            else return 0; // tie
        }
    }

    public static int getHighCardValue(ArrayList<Card> hand) {
        int high = 0;
        for (Card c : hand) {
            int val = c.getValue();
            if (val > high) high = val;
        }
        return high;
    }
}
