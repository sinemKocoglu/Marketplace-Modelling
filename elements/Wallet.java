package elements;

public class Wallet {
	/**
	 * dollars: dollars that can be spent
	 * coins: coins that can be sold
	 * blockedDollars: dollars that have been assigned in buyingOrders
	 * blockedCoins: coins that have been assigned in sellingOrders
	 */
	private double dollars;
	private double coins;
	private double blockedDollars;
	private double blockedCoins;
	
	public Wallet(double dollars, double coins) {
		this.dollars=dollars;
		this.coins=coins;
	}

	/**
	 * adding given quantity m to the appropriate asset.
	 * @param S  determines field that value m will be added to.
	 * @param m  value to add
	 */
	public void add(String S, double m) {
		if(S.equals("c")) {
			coins+=m;
		}
		else if(S.equals("d")) {
			dollars+=m;
		}
		else if(S.equals("bd")) {
			blockedDollars+=m;
		}
		else if(S.equals("bc")) {
			blockedCoins+=m;
		}
	}
	
	/**
	 * subtracting given quantity m from the appropriate asset.
	 * @param S  determines field that value m will be subtracted from.
	 * @param m  value to be subtracted
	 */
	public void subtract(String S, double m) {
		if(S.equals("c")) {
			coins-=m;
		}
		else if(S.equals("d")) {
			dollars-=m;
		}
		else if(S.equals("bd")) {
			blockedDollars-=m;
		}
		else if(S.equals("bc")) {
			blockedCoins-=m;
		}
	}
	
	/**
	 * calculates total dollars or total coins 
	 * @param s "d" for dollars, "c" for coins
	 * @return total amount of s
	 */
	public double total(String s) {
		if(s.equals("d")) {
			return dollars+blockedDollars;
		}
		else if(s.equals("c")) {
			return coins+blockedCoins;
		}
		else {
			return 0;
		}
	}

	/**
	 * @return the dollars
	 */
	public double getDollars() {
		return dollars;
	}


	/**
	 * @return the coins
	 */
	public double getCoins() {
		return coins;
	}

}
