package in.imast.impact.model;

public class ProductModal {


    private String productId;
    private String productName;
    private String productQuantity;
    private String productValue;
    private String productUnit;
    private String productCode;

    public void setProductId(String productId) {

        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductName(String productName) {


        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }


    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductValue(String productValue) {
        this.productValue = productValue;
    }

    public String getProductValue() {
        return productValue;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductCode() {
        return productCode;
    }
}