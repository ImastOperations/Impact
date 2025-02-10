package in.imast.impact.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {


    @SerializedName("success")
    @Expose
    private Integer success;

    @SerializedName("access_token")
    @Expose
    String access_token;

    @SerializedName("verifey")
    private String verifey;


    @SerializedName("gstin_number")
    @Expose
    private String gstin_number;

    @SerializedName("user_details")
    @Expose
    private UserInfo userinfo;

    public String getAccess_token() {
        return access_token;
    }

    public String getVerifey() {
        return verifey;
    }


    public Integer getSuccess() {
        return success;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public String getGstin_number() {
        return gstin_number;
    }


    public class UserInfo {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("customer_id")
        @Expose
        private Integer customer_id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email = "";
        @SerializedName("username")
        @Expose
        private String mobile;
        @SerializedName("avatar")
        @Expose
        private Object avatar;
        @SerializedName("state_id")
        @Expose
        private Integer stateId;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("district_id")
        @Expose
        private Integer districtId;
        @SerializedName("district_name")
        @Expose
        private String districtName;
        @SerializedName("city_id")
        @Expose
        private Integer cityId;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("pincode_id")
        @Expose
        private Integer pincodeId;
        @SerializedName("pincode_name")
        @Expose
        private String pincodeName;
        @SerializedName("customer_type_id")
        @Expose
        private Integer customerType;
        @SerializedName("customer_type_name")
        @Expose
        private String customerTypeName;
        @SerializedName("firm_name")
        @Expose
        private String firmName;
        @SerializedName("parent_id")
        @Expose
        private Integer parentId;
        @SerializedName("parent_name")
        @Expose
        private String parentName;
        @SerializedName("customer_code")
        @Expose
        private Object customerCode;
        @SerializedName("gst_no")
        @Expose
        private String gstNo;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("region_id")
        @Expose
        private Integer regionId;
        @SerializedName("region_name")
        @Expose
        private Object regionName;
        @SerializedName("access_token")
        @Expose
        private String accessToken;

        @SerializedName("date_of_birth")
        @Expose
        private String dob;

        @SerializedName("date_of_anniversary")
        @Expose
        private String doa;

        @SerializedName("marital_status")
        @Expose
        private String maritalStatus;

        @SerializedName("gst")
        @Expose
        private String gst;

        @SerializedName("pan")
        @Expose
        private String pan;

        @SerializedName("aadhar_no")
        @Expose
        private String adhar;

        @SerializedName("other_id_name")
        @Expose
        private String other;

        @SerializedName("paytm_no")
        @Expose
        private String paytm_no;

        @SerializedName("pan_no")
        @Expose
        private String pan_no;




        public Integer getCustomer_id() {
            return customer_id;
        }

        public String getPaytm_no() {
            return paytm_no;
        }

        public String getPan_no() {
            return pan_no;
        }

        public String getGst() {
            return gst;
        }

        public String getPan() {
            return pan;
        }

        public String getAdhar() {
            return adhar;
        }

        public String getOther() {
            return other;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public String getDoa() {
            return doa;
        }

        public String getDob() {
            return dob;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getMobile() {
            return mobile;
        }


        public Object getAvatar() {
            return avatar;
        }

        public Integer getStateId() {
            return stateId;
        }

        public String getStateName() {
            return stateName;
        }

        public Integer getDistrictId() {
            return districtId;
        }

        public String getDistrictName() {
            return districtName;
        }

        public Integer getCityId() {
            return cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public Integer getPincodeId() {
            return pincodeId;
        }

        public String getPincodeName() {
            return pincodeName;
        }

        public Integer getCustomerType() {
            return customerType;
        }

        public String getCustomerTypeName() {
            return customerTypeName;
        }

        public String getFirmName() {
            return firmName;
        }

        public Integer getParentId() {
            return parentId;
        }

        public String getParentName() {
            return parentName;
        }

        public Object getCustomerCode() {
            return customerCode;
        }

        public String getGstNo() {
            return gstNo;
        }

        public Integer getStatus() {
            return status;
        }

        public Integer getRegionId() {
            return regionId;
        }

        public Object getRegionName() {
            return regionName;
        }

        public String getAccessToken() {
            return accessToken;
        }


    }
}