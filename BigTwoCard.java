
/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card 
 * used in a Big Two card game. 
 * It should override the compareTo() method it inherited from the Card class to 
 * reflect the ordering of cards used in a Big Two card game.
 * @author Li Jianxiang
 *
 */
public class BigTwoCard extends Card {
	/**
	 * a constructor for building a card with the specified suit and rank. 
	 * suit is an integer between 0 and 3, and rank is an integer between 0 and 12.
	 * @param suit
	 * an int value between 0 and 3 representing the suit of a card:
	 * <p>
	 * 0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank
	 * an int value between 0 and 12 representing the rank of a card:
	 * <p>
	 * 0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11
	 * = 'Q', 12 = 'K'
	 */
	public BigTwoCard(int suit, int rank){
		super(suit,rank);
	}
	
	/** 
	 *  a method for comparing this card with the specified card for order. 
	 *  Returns a negative integer, zero, or a positive integer as this card is 
	 *  less than, equal to, or greater than the specified card.
	 */
	public int compareTo(Card card){
		int thisRank,cardRank;
		if (this.getRank() < 2){
			thisRank = this.getRank() + 13;
		}else thisRank = this.getRank();
		if (card.getRank() < 2){
			cardRank = card.getRank() + 13;
		}else cardRank = card.getRank();
		
		if (thisRank > cardRank) {
			return 1;
		} else if (thisRank < cardRank) {
			return -1;
		} else if (this.getSuit() > card.getSuit()) {
			return 1;
		} else if (this.getSuit() < card.getSuit()) {
			return -1;
		} else {
			return 0;
		}
	}
}
