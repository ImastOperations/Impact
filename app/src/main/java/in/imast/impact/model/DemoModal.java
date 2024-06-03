package in.imast.impact.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DemoModal {

    @SerializedName("data")
    @Expose
    private List<CustomerInfo> data = null;

    public List<CustomerInfo> getData() {
        return data;
    }

    public void setData(List<CustomerInfo> data) {
        this.data = data;
    }

    public static class CustomerInfo {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("product_name")
        @Expose
        private String firmName;
        private String prductName;
        private String productQuanty;
        private String value;
        private String unit;
        private String code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirmName() {
            return firmName;
        }

        public void setFirmName(String firmName) {
            this.firmName = firmName;
        }

        public void setPrductName(String prductName) {



            this.prductName = prductName;
        }

        public String getPrductName() {
            return prductName;
        }

        public void setProductQuanty(String productQuanty) {


            this.productQuanty = productQuanty;
        }

        public String getProductQuanty() {
            return productQuanty;
        }

        public void setValue(String value) {

            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setUnit(String unit) {


            this.unit = unit;
        }

        public String getUnit() {
            return unit;
        }

        public void setCode(String code) {


            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
