package model;

public class RecordedPurchases {

    private Product product;
    private Double totalPrice;
    private Double quantity;

    RecordedPurchases(Product product, Double totalPrice, Double quantity) {

        double productUnitPrice = product.getUnitPrice();
        double allegedUnitPrice = totalPrice / quantity;

        if (allegedUnitPrice != productUnitPrice) {
            System.out.println("O valor do produto não está condizente.");
            return;
        }

        this.product = product;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
    }
}
