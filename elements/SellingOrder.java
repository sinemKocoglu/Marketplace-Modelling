package elements;

public class SellingOrder extends Order implements Comparable<SellingOrder>{

	public SellingOrder(int traderID, double amount, double price) {
		super(traderID, amount, price);
	}

	/**
	 * PQ should be sorted in ascending price order
	 * if two orders have the same price, sorting should be in descending amount order
	 * if it is still the same, sorting should be in ascending traderID order.
	 */
	@Override
	public int compareTo(SellingOrder o) {
		if(this.getPrice() != o.getPrice()) {
			return (int)(this.getPrice()-o.getPrice());
		}
		else if(this.getAmount() != o.getAmount()) {
			return (int) (o.getAmount()-this.getAmount());
		}
		else {
			return this.traderID-o.traderID;
		}	
	}
}
