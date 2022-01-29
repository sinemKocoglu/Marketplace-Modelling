package elements;

public class Order{
	/**
	 * amount amount of order
	 * price price of order
	 * traderID traderID of trader who orders
	 */
	private double amount;
	private double price;
	protected int traderID;

	public Order(int traderID, double amount, double price) {
		this.traderID = traderID;
		this.price = price;
		this.amount = amount;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
