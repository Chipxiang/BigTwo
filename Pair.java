
/**
 * The Pair class is a subclass of Hand class and is used to model
 * a hand of pair in a Big Two card game.
 * @author Li Jianxiang
 *
 */
public class Pair extends Hand{
	/**
	 * a constructor of building a hand of pair with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Pair(CardGamePlayer player, CardList cards){
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid pair hand.
	 */
	public boolean isValid(){
		if (this.size()==2){
			if (this.getCard(0).getRank()==this.getCard(1).getRank()){
				return true;
			}
		}
		return false;
	}
	/**
	 * a method for returning a string "Pair" specifying the type of this hand.
	 */
	public String getType(){
		return "Pair";
	}
}
