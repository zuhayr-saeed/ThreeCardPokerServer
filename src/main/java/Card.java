public class Card {
    private int value; // 2-14 (11=Jack, 12=Queen, 13=King, 14=Ace)
    private char suit; // 'H' = Hearts, 'D' = Diamonds, 'S' = Spades, 'C' = Clubs

    /**
     * Constructs a Card with the specified value and suit.
     *
     * @param value The numerical value of the card (2-14).
     * @param suit  The suit of the card ('H', 'D', 'S', 'C').
     */
    public Card(int value, char suit) {
        this.value = value;
        this.suit = suit;
    }

    /**
     * Returns the numerical value of the card.
     *
     * @return The card's value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the suit of the card.
     *
     * @return The card's suit.
     */
    public char getSuit() {
        return suit;
    }

    /**
     * Returns a string representation of the card (e.g., "AH" for Ace of Hearts).
     *
     * @return The string representation of the card.
     */
    @Override
    public String toString() {
        String valueStr;
        switch (value) {
            case 11:
                valueStr = "J";
                break;
            case 12:
                valueStr = "Q";
                break;
            case 13:
                valueStr = "K";
                break;
            case 14:
                valueStr = "A";
                break;
            default:
                valueStr = String.valueOf(value);
        }
        return valueStr + suit;
    }
}