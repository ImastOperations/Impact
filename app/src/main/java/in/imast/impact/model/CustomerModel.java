package in.imast.impact.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerModel {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Customer> datas = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Customer> getDatas() {
        return datas;
    }

    public void setDatas(List<Customer> datas) {
        this.datas = datas;
    }

    public class Customer {

        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("firm_name")
        @Expose
        private String firm_name;

         @SerializedName("customer_address")
        @Expose
        private String customer_address;

         @SerializedName("customer_email")
        @Expose
        private String customer_email;

         @SerializedName("customer_mobile_number")
        @Expose
        private String customer_mobile_number;

        public String getFirm_name() {
            return firm_name;
        }

        public String getCustomer_address() {
            return customer_address;
        }

        public void setCustomer_address(String customer_address) {
            this.customer_address = customer_address;
        }

        public String getCustomer_email() {
            return customer_email;
        }

        public void setCustomer_email(String customer_email) {
            this.customer_email = customer_email;
        }

        public String getCustomer_mobile_number() {
            return customer_mobile_number;
        }

        public void setCustomer_mobile_number(String customer_mobile_number) {
            this.customer_mobile_number = customer_mobile_number;
        }

        public void setFirm_name(String firm_name) {
            this.firm_name = firm_name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

}
