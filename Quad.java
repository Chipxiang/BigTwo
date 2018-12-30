
/**
 * The Pair class is a subclass of Hand class and is used to model
 * a hand of quad in a Big Two card game.
 * @author Li Jianxiang
 *
 */
public class Quad extends Hand{
	/**
	 * a constructor of building a hand of quad with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Quad(CardGamePlayer player, CardList cards){
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid quad hand.
	 */
	public boolean isValid(){
		this.sort();
		if (this.size()==5){
			if (this.getCard(0).getRank()!= this.getCard(1).getRank()){
				for (int i = 2 ; i<5; i++){
					if (this.getCard(i).getRank()!=this.getCard(i-1).getRank())
						return false;
				}
				return true;
			}else if (this.getCard(0).getRank()== this.getCard(1).getRank()){
				for (int i=2; i<4; i++){
					if (this.getCard(i).getRank()!=this.getCard(i-1).getRank())
						return false;
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * an overriding method for getting the top card of a hand of quad.
	 */
	public Card getTopCard(){
		this.sort();
		if (this.getCard(0).getRank()==this.getCard(1).getRank()){
			return this.getCard(3);
		}
		else{
			return this.getCard(4);
		}
	}
	/**
	 * a method for returning a string "Quad" specifying the type of this hand.
	 */
	public String getType(){
		return "Quad";
	}
}