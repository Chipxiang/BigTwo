
/**
 * The Straight class is a subclass of Hand class and is used to model
 * a hand of straight in a Big Two card game.
 * @author Li Jianxiang
 *
 */
public class Straight extends Hand{
	/**
	 * a constructor of building a hand of straight with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Straight(CardGamePlayer player, CardList cards){
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid straight hand.
	 */
	public boolean isValid(){
		int prev=this.getCard(0).getRank();
		if (prev>1 && prev < 11){
			if (this.size()==5){
				this.sort();
				for (int i=1; i<this.size(); i++){
					if(this.getCard(i).getRank()!=prev+1)
						return false;
					prev++;
					if (prev >11){
						prev=-1;
					}
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * a method for returning a string "Straight" specifying the type of this hand.
	 */
	public String getType(){
		return "Straight";
	}
}