package io.teamscala.java.sample.models;

import io.teamscala.java.jpa.Finder;
import io.teamscala.java.jpa.Model;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * The type User.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "users", schema = "public", catalog = "glas")
public class User extends Model<Long> {

    /**
     * The constant find.
     */
    public static final Finder<Long, User> find = new Finder<Long, User>(User.class);

    /**
     * The enum Level.
     */
    public static enum Level {
        /** NONE */ N("NONE"),
        /** 사용자 */ U("사용자"),
        /** 관리자 */ A("관리자");

        private final String label;
        Level(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    /**
     * The enum Service type.
     */
    public static enum ServiceType {
        /** PREMIUM */ P("프리미엄"),
        /** BASIC   */ B("베이직");

        private final String label;
        ServiceType(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    /**
     * Gets created at.
     *
     * @return the created at
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", insertable = false, updatable = false)
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets created at.
     *
     * @param createdAt the created at
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    private Date createdAt;

    /**
     * Gets updated at.
     *
     * @return the updated at
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", insertable = false, updatable = false)
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets updated at.
     *
     * @param updatedAt the updated at
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    private Date updatedAt;

    /**
     * Gets username.
     *
     * @return the username
     */
    @NotNull
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    /**
     * Gets password.
     *
     * @return the password
     */
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    /**
     * Gets level.
     *
     * @return the level
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    public Level getLevel() {
        return level;
    }

    /**
     * Sets level.
     *
     * @param level the level
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    private Level level;

    /**
     * Gets name.
     *
     * @return the name
     */
    @Column(name = "name")
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    private String name;

    /**
     * Gets email.
     *
     * @return the email
     */
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    /**
     * Gets tel no.
     *
     * @return the tel no
     */
    @Column(name = "tel_no")
    public String getTelNo() {
        return telNo;
    }

    /**
     * Sets tel no.
     *
     * @param telNo the tel no
     */
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    private String telNo;

    /**
     * Gets mobile no.
     *
     * @return the mobile no
     */
    @Column(name = "mobile_no")
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets mobile no.
     *
     * @param mobileNo the mobile no
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    private String mobileNo;

    /**
     * Gets remark.
     *
     * @return the remark
     */
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    /**
     * Sets remark.
     *
     * @param remark the remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String remark;

    /**
     * Gets service type.
     *
     * @return the service type
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    public ServiceType getServiceType() {
        return serviceType;
    }

    /**
     * Sets service type.
     *
     * @param serviceType the service type
     */
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    private ServiceType serviceType;

    /**
     * Gets service start date.
     *
     * @return the service start date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "service_start_date")
    public Date getServiceStartDate() {
        return serviceStartDate;
    }

    /**
     * Sets service start date.
     *
     * @param serviceStartDate the service start date
     */
    public void setServiceStartDate(Date serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    private Date serviceStartDate;

    /**
     * Gets service end date.
     *
     * @return the service end date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "service_end_date")
    public Date getServiceEndDate() {
        return serviceEndDate;
    }

    /**
     * Sets service end date.
     *
     * @param serviceEndDate the service end date
     */
    public void setServiceEndDate(Date serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    private Date serviceEndDate;

    /**
     * Is approved.
     *
     * @return the boolean
     */
    @Column(name = "approved")
    public boolean isApproved() {
        return approved;
    }

    /**
     * Sets approved.
     *
     * @param approved the approved
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    private boolean approved;

    /**
     * Gets approved at.
     *
     * @return the approved at
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approved_at")
    public Date getApprovedAt() {
        return approvedAt;
    }

    /**
     * Sets approved at.
     *
     * @param approvedAt the approved at
     */
    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    private Date approvedAt;

    /**
     * Gets ipad udid.
     *
     * @return the ipad udid
     */
    @Column(name = "ipad_udid")
    public String getIpadUdid() {
        return ipadUdid;
    }

    /**
     * Sets ipad udid.
     *
     * @param ipadUdid the ipad udid
     */
    public void setIpadUdid(String ipadUdid) {
        this.ipadUdid = ipadUdid;
    }

    private String ipadUdid;

    /**
     * Gets ipad serial no.
     *
     * @return the ipad serial no
     */
    @Column(name = "ipad_serial_no")
    public String getIpadSerialNo() {
        return ipadSerialNo;
    }

    /**
     * Sets ipad serial no.
     *
     * @param ipadSerialNo the ipad serial no
     */
    public void setIpadSerialNo(String ipadSerialNo) {
        this.ipadSerialNo = ipadSerialNo;
    }

    private String ipadSerialNo;

    /**
     * Gets company message.
     *
     * @return the company message
     */
    @Column(name = "company_message")
    public String getCompanyMessage() {
        return companyMessage;
    }

    /**
     * Sets company message.
     *
     * @param companyMessage the company message
     */
    public void setCompanyMessage(String companyMessage) {
        this.companyMessage = companyMessage;
    }

    private String companyMessage;

    /**
     * Gets company name.
     *
     * @return the company name
     */
    @Column(name = "company_name")
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets company name.
     *
     * @param companyName the company name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    private String companyName;

    /**
     * Gets crn.
     *
     * @return the crn
     */
    @Column(name = "crn")
    public String getCrn() {
        return crn;
    }

    /**
     * Sets crn.
     *
     * @param crn the crn
     */
    public void setCrn(String crn) {
        this.crn = crn;
    }

    private String crn;

    /**
     * Gets ceo.
     *
     * @return the ceo
     */
    @Column(name = "ceo")
    public String getCeo() {
        return ceo;
    }

    /**
     * Sets ceo.
     *
     * @param ceo the ceo
     */
    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    private String ceo;

    /**
     * Gets post code.
     *
     * @return the post code
     */
    @Column(name = "post_code")
    public String getPostCode() {
        return postCode;
    }

    /**
     * Sets post code.
     *
     * @param postCode the post code
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    private String postCode;

    /**
     * Gets address 1.
     *
     * @return the address 1
     */
    @Column(name = "address1")
    public String getAddress1() {
        return address1;
    }

    /**
     * Sets address 1.
     *
     * @param address1 the address 1
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    private String address1;

    /**
     * Gets address 2.
     *
     * @return the address 2
     */
    @Column(name = "address2")
    public String getAddress2() {
        return address2;
    }

    /**
     * Sets address 2.
     *
     * @param address2 the address 2
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    private String address2;

    /**
     * Gets condition.
     *
     * @return the condition
     */
    @Column(name = "condition")
    public String getCondition() {
        return condition;
    }

    /**
     * Sets condition.
     *
     * @param condition the condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    private String condition;

    /**
     * Gets business type.
     *
     * @return the business type
     */
    @Column(name = "business_type")
    public String getBusinessType() {
        return businessType;
    }

    /**
     * Sets business type.
     *
     * @param businessType the business type
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    private String businessType;

    /**
     * Gets department.
     *
     * @return the department
     */
    @Column(name = "department")
    public String getDepartment() {
        return department;
    }

    /**
     * Sets department.
     *
     * @param department the department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    private String department;

    /**
     * Gets position.
     *
     * @return the position
     */
    @Column(name = "position")
    public String getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(String position) {
        this.position = position;
    }

    private String position;

    /**
     * Gets vat company name.
     *
     * @return the vat company name
     */
    @Column(name = "vat_company_name")
    public String getVatCompanyName() {
        return vatCompanyName;
    }

    /**
     * Sets vat company name.
     *
     * @param vatCompanyName the vat company name
     */
    public void setVatCompanyName(String vatCompanyName) {
        this.vatCompanyName = vatCompanyName;
    }

    private String vatCompanyName;

    /**
     * Gets vat crn.
     *
     * @return the vat crn
     */
    @Column(name = "vat_crn")
    public String getVatCrn() {
        return vatCrn;
    }

    /**
     * Sets vat crn.
     *
     * @param vatCrn the vat crn
     */
    public void setVatCrn(String vatCrn) {
        this.vatCrn = vatCrn;
    }

    private String vatCrn;

    /**
     * Gets vat ceo.
     *
     * @return the vat ceo
     */
    @Column(name = "vat_ceo")
    public String getVatCeo() {
        return vatCeo;
    }

    /**
     * Sets vat ceo.
     *
     * @param vatCeo the vat ceo
     */
    public void setVatCeo(String vatCeo) {
        this.vatCeo = vatCeo;
    }

    private String vatCeo;

    /**
     * Gets vat post code.
     *
     * @return the vat post code
     */
    @Column(name = "vat_post_code")
    public String getVatPostCode() {
        return vatPostCode;
    }

    /**
     * Sets vat post code.
     *
     * @param vatPostCode the vat post code
     */
    public void setVatPostCode(String vatPostCode) {
        this.vatPostCode = vatPostCode;
    }

    private String vatPostCode;

    /**
     * Gets vat address 1.
     *
     * @return the vat address 1
     */
    @Column(name = "vat_address1")
    public String getVatAddress1() {
        return vatAddress1;
    }

    /**
     * Sets vat address 1.
     *
     * @param vatAddress1 the vat address 1
     */
    public void setVatAddress1(String vatAddress1) {
        this.vatAddress1 = vatAddress1;
    }

    private String vatAddress1;

    /**
     * Gets vat address 2.
     *
     * @return the vat address 2
     */
    @Column(name = "vat_address2")
    public String getVatAddress2() {
        return vatAddress2;
    }

    /**
     * Sets vat address 2.
     *
     * @param vatAddress2 the vat address 2
     */
    public void setVatAddress2(String vatAddress2) {
        this.vatAddress2 = vatAddress2;
    }

    private String vatAddress2;

    /**
     * Gets vat condition.
     *
     * @return the vat condition
     */
    @Column(name = "vat_condition")
    public String getVatCondition() {
        return vatCondition;
    }

    /**
     * Sets vat condition.
     *
     * @param vatCondition the vat condition
     */
    public void setVatCondition(String vatCondition) {
        this.vatCondition = vatCondition;
    }

    private String vatCondition;

    /**
     * Gets vat business type.
     *
     * @return the vat business type
     */
    @Column(name = "vat_business_type")
    public String getVatBusinessType() {
        return vatBusinessType;
    }

    /**
     * Sets vat business type.
     *
     * @param vatBusinessType the vat business type
     */
    public void setVatBusinessType(String vatBusinessType) {
        this.vatBusinessType = vatBusinessType;
    }

    private String vatBusinessType;

    /**
     * Gets vat charge name.
     *
     * @return the vat charge name
     */
    @Column(name = "vat_charge_name")
    public String getVatChargeName() {
        return vatChargeName;
    }

    /**
     * Sets vat charge name.
     *
     * @param vatChargeName the vat charge name
     */
    public void setVatChargeName(String vatChargeName) {
        this.vatChargeName = vatChargeName;
    }

    private String vatChargeName;

    /**
     * Gets vat charge email.
     *
     * @return the vat charge email
     */
    @Column(name = "vat_charge_email")
    public String getVatChargeEmail() {
        return vatChargeEmail;
    }

    /**
     * Sets vat charge email.
     *
     * @param vatChargeEmail the vat charge email
     */
    public void setVatChargeEmail(String vatChargeEmail) {
        this.vatChargeEmail = vatChargeEmail;
    }

    private String vatChargeEmail;

    /**
     * Gets vat charge tel no.
     *
     * @return the vat charge tel no
     */
    @Column(name = "vat_charge_tel_no")
    public String getVatChargeTelNo() {
        return vatChargeTelNo;
    }

    /**
     * Sets vat charge tel no.
     *
     * @param vatChargeTelNo the vat charge tel no
     */
    public void setVatChargeTelNo(String vatChargeTelNo) {
        this.vatChargeTelNo = vatChargeTelNo;
    }

    private String vatChargeTelNo;
}
