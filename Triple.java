
/**
 * The Triple class is a subclass of Hand class and is used to model
 * a hand of triple in a Big Two card game.
 * @author Li Jianxiang
 *
 */
public class Triple extends Hand{
	/**
	 * a constructor of building a hand of triple with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Triple(CardGamePlayer player, CardList cards){
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid triple hand.
	 */
	public boolean isValid(){
		if(this.size()==3){
			if (this.getCard(0).getRank()==this.getCard(1).getRank() && this.getCard(1).getRank()==this.getCard(2).getRank())
				return true;
		}
		return false;
	}
	/**
	 * a method for returning a string "Triple" specifying the type of this hand.
	 */
	public String getType(){
		return "Triple";
	}
}