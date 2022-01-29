package elements;

public class Transaction {
	private SellingOrder sellingOrder;
	private BuyingOrder buyingOrder;
	
	/**
	 * constructor of Transaction
	 * @param sellingOrder
	 * @param buyingOrder
	 */
	public Transaction(SellingOrder sellingOrder, BuyingOrder buyingOrder) {
		this.sellingOrder = sellingOrder;
		this.buyingOrder = buyingOrder;
	}
}
