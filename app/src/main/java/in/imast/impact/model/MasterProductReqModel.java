package in.imast.impact.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MasterProductReqModel {

    @SerializedName("invoice_seller_id")
    @Expose
    private String invoice_seller_id;

    @SerializedName("invoice_buyer_id")
    @Expose
    private String invoice_buyer_id;

    @SerializedName("invoice_value")
    @Expose
    private String invoice_value;

    @SerializedName("invoice_quantity")
    @Expose
    private String invoice_quantity;

    @SerializedName("invoice_status")
    @Expose
    private String invoice_status;

    @SerializedName("invoice_number")
    @Expose
    private String invoice_number;

    @SerializedName("invoice_date")
    @Expose
    private String invoice_date;

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    @SerializedName("invoice_image")
    @Expose
    private ArrayList<String> image;

    @SerializedName("product_id")
    @Expose
    private ArrayList<String> product_id;

    @SerializedName("product_qty")
    @Expose
    private ArrayList<String> product_qty;

    @SerializedName("product_quantity")
    @Expose
    private ArrayList<String> product_quantity;

    @SerializedName("product_amount")
    @Expose
    private ArrayList<String> product_amount;

    @SerializedName("product_value")
    @Expose
    private ArrayList<String> product_value;

    @SerializedName("disbursed_quantity")
    @Expose
    private ArrayList<String> disbursed_quantity;

    @SerializedName("product_subtotal")
    @Expose
    private ArrayList<String> product_subtotal;


    public ArrayList<String> getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(ArrayList<String> product_quantity) {
        this.product_quantity = product_quantity;
    }

    public ArrayList<String> getProduct_value() {
        return product_value;
    }

    public void setProduct_value(ArrayList<String> product_value) {
        this.product_value = product_value;
    }

    public ArrayList<String> getDisbursed_quantity() {
        return disbursed_quantity;
    }

    public void setDisbursed_quantity(ArrayList<String> disbursed_quantity) {
        this.disbursed_quantity = disbursed_quantity;
    }

    public ArrayList<String> getProduct_id() {
        return product_id;
    }

    public void setProduct_id(ArrayList<String> product_id) {
        this.product_id = product_id;
    }

    public ArrayList<String> getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(ArrayList<String> product_qty) {
        this.product_qty = product_qty;
    }

    public ArrayList<String> getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(ArrayList<String> product_amount) {
        this.product_amount = product_amount;
    }

    public ArrayList<String> getProduct_subtotal() {
        return product_subtotal;
    }

    public void setProduct_subtotal(ArrayList<String> product_subtotal) {
        this.product_subtotal = product_subtotal;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }



  public String getInvoice_seller_id() {
        return invoice_seller_id;
    }

    public void setInvoice_seller_id(String invoice_seller_id) {
        this.invoice_seller_id = invoice_seller_id;
    }

    public String getInvoice_buyer_id() {
        return invoice_buyer_id;
    }

    public void setInvoice_buyer_id(String invoice_buyer_id) {
        this.invoice_buyer_id = invoice_buyer_id;
    }

    public String getInvoice_value() {
        return invoice_value;
    }

    public void setInvoice_value(String invoice_value) {
        this.invoice_value = invoice_value;
    }

    public String getInvoice_quantity() {
        return invoice_quantity;
    }

    public void setInvoice_quantity(String invoice_quantity) {
        this.invoice_quantity = invoice_quantity;
    }

    public String getInvoice_status() {
        return invoice_status;
    }

    public void setInvoice_status(String invoice_status) {
        this.invoice_status = invoice_status;
    }





}