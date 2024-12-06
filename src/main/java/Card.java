import java.io.Serializable;

public class Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private int value; 
    private char suit;

    public Card(int value, char suit) {
        this.value = value;
        this.suit = suit;
    }

    public int getValue() {return value;}
    public char getSuit() {return suit;}

    @Override
    public String toString() {
        String valStr;
        switch(value) {
            case 11: valStr="J"; break;
            case 12: valStr="Q"; break;
            case 13: valStr="K"; break;
            case 14: valStr="A"; break;
            default: valStr=String.valueOf(value);
        }
        return valStr + suit;
    }
}
