
/**
 * The Flush class is a subclass of Hand class and is used to model
 * a hand of flush in a Big Two card game.
 * @author Li Jianxiang
 *
 */
public class Flush extends Hand{
	/**
	 * a constructor of building a hand of flush with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Flush(CardGamePlayer player, CardList cards){
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid flush hand.
	 */
	public boolean isValid(){
		int suit = this.getCard(0).getSuit();
		if (this.size()==5){
			for (int i=1; i<this.size();i++){
				if (this.getCard(i).getSuit()!=suit)
					return false;
			}
			return true;
		}
		return false;
	}
	/**
	 * a method for returning a string "Flush" specifying the type of this hand.
	 */
	public String getType(){
		return "Flush";
	}
}