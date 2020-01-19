package de.skuzzle.cmp.collaborativeorder.domain;

public class RestLineItem {

    private final RestMoney singlePrice;
    private final String productName;
    private final RestAmount amount;

    private RestLineItem(RestMoney singlePrice, String productName, RestAmount amount) {
        this.singlePrice = singlePrice;
        this.productName = productName;
        this.amount = amount;
    }

    public static RestLineItem fromDomain(LineItem lineItem) {
        return new RestLineItem(
                RestMoney.fromDomain(lineItem.getSinglePrice()),
                lineItem.getProductName(),
                RestAmount.fromDomain(lineItem.getAmount()));
    }

    public RestMoney getSinglePrice() {
        return this.singlePrice;
    }

    public String getProductName() {
        return this.productName;
    }

    public RestAmount getAmount() {
        return this.amount;
    }

}
