
/**
 * The Single class is a subclass of Hand class and is used to model
 * a hand of single in a Big Two card game.
 * @author Li Jianxiang
 *
 */
public class Single extends Hand{
	/**
	 * a constructor of building a hand of single with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Single(CardGamePlayer player, CardList cards){
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid single hand.
	 */
	public boolean isValid(){
		if (this.size()==1)
			return true;
		return false;
	}
	/**
	 * a method for returning a string "Single" specifying the type of this hand.
	 */
	public String getType(){
		return "Single";
	}
}
