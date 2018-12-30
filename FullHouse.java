
/**
 * The FullHouse class is a subclass of Hand class and is used to model
 * a hand of full house in a Big Two card game.
 * @author Li Jianxiang
 *
 */
public class FullHouse extends Hand{
	/**
	 * a constructor of building a hand of full house with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public FullHouse(CardGamePlayer player, CardList cards){
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid full house hand.
	 */
	public boolean isValid(){
		this.sort();
		if (this.size()==5){
			if (this.getCard(0).getRank()!= this.getCard(1).getRank())
				return false;
			if (this.getCard(1).getRank()!= this.getCard(2).getRank()){
				if (this.getCard(2).getRank()!= this.getCard(3).getRank())
					return false;
				if (this.getCard(3).getRank()!= this.getCard(4).getRank())
					return false;
				return true;
			}
			else if (this.getCard(1).getRank()==this.getCard(2).getRank()){
				if (this.getCard(2).getRank()==this.getCard(3).getRank())
					return false;
				else if (this.getCard(2).getRank()!=this.getCard(3).getRank()){
					if (this.getCard(3).getRank()!= this.getCard(4).getRank())
						return false;
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * an overriding method for getting the top card of a hand of full house.
	 */
	public Card getTopCard(){
		this.sort();
		if (this.getCard(1).getRank()==this.getCard(2).getRank()){
			return this.getCard(2);
		}
		else{
			return this.getCard(4);
		}
	}
	/**
	 * a method for returning a string "FullHouse" specifying the type of this hand.
	 */
	public String getType(){
		return "FullHouse";
	}
}