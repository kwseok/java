package io.teamscala.java.sample.models;

import io.teamscala.java.jpa.Finder;
import io.teamscala.java.jpa.Model;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * The model Patent.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "patents", schema = "public", catalog = "glas")
public class Patent extends Model<String> {

    public static final String DEFAULT_ORDER = "no desc";

    public static final Finder<String, Patent> find = new Finder<String, Patent>(Patent.class);

    /**
     * Gets no.
     *
     * @return the no
     */
    @Id
    @Column(name = "no")
    public String getNo() {
        return no;
    }

    /**
     * Sets no.
     *
     * @param no the no
     */
    public void setNo(String no) {
        this.no = no;
    }

    private String no;

    /**
     * Gets date.
     *
     * @return the date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

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
     * Gets original open file name.
     *
     * @return the original open file name
     */
    @Column(name = "original_open_file_name")
    public String getOriginalOpenFileName() {
        return originalOpenFileName;
    }

    /**
     * Sets original open file name.
     *
     * @param originalOpenFileName the original open file name
     */
    public void setOriginalOpenFileName(String originalOpenFileName) {
        this.originalOpenFileName = originalOpenFileName;
    }

    private String originalOpenFileName;

    /**
     * Gets original open file path.
     *
     * @return the original open file path
     */
    @Column(name = "original_open_file_path")
    public String getOriginalOpenFilePath() {
        return originalOpenFilePath;
    }

    /**
     * Sets original open file path.
     *
     * @param originalOpenFilePath the original open file path
     */
    public void setOriginalOpenFilePath(String originalOpenFilePath) {
        this.originalOpenFilePath = originalOpenFilePath;
    }

    private String originalOpenFilePath;

    /**
     * Gets original register file name.
     *
     * @return the original register file name
     */
    @Column(name = "original_register_file_name")
    public String getOriginalRegisterFileName() {
        return originalRegisterFileName;
    }

    /**
     * Sets original register file name.
     *
     * @param originalRegisterFileName the original register file name
     */
    public void setOriginalRegisterFileName(String originalRegisterFileName) {
        this.originalRegisterFileName = originalRegisterFileName;
    }

    private String originalRegisterFileName;

    /**
     * Gets original register file path.
     *
     * @return the original register file path
     */
    @Column(name = "original_register_file_path")
    public String getOriginalRegisterFilePath() {
        return originalRegisterFilePath;
    }

    /**
     * Sets original register file path.
     *
     * @param originalRegisterFilePath the original register file path
     */
    public void setOriginalRegisterFilePath(String originalRegisterFilePath) {
        this.originalRegisterFilePath = originalRegisterFilePath;
    }

    private String originalRegisterFilePath;

    /**
     * Gets correction file name.
     *
     * @return the correction file name
     */
    @Column(name = "correction_file_name")
    public String getCorrectionFileName() {
        return correctionFileName;
    }

    /**
     * Sets correction file name.
     *
     * @param correctionFileName the correction file name
     */
    public void setCorrectionFileName(String correctionFileName) {
        this.correctionFileName = correctionFileName;
    }

    private String correctionFileName;

    /**
     * Gets correction file path.
     *
     * @return the correction file path
     */
    @Column(name = "correction_file_path")
    public String getCorrectionFilePath() {
        return correctionFilePath;
    }

    /**
     * Sets correction file path.
     *
     * @param correctionFilePath the correction file path
     */
    public void setCorrectionFilePath(String correctionFilePath) {
        this.correctionFilePath = correctionFilePath;
    }

    private String correctionFilePath;

    /**
     * Gets all lawsuit image name.
     *
     * @return the all lawsuit image name
     */
    @Column(name = "all_lawsuit_image_name")
    public String getAllLawsuitImageName() {
        return allLawsuitImageName;
    }

    /**
     * Sets all lawsuit image name.
     *
     * @param allLawsuitImageName the all lawsuit image name
     */
    public void setAllLawsuitImageName(String allLawsuitImageName) {
        this.allLawsuitImageName = allLawsuitImageName;
    }

    private String allLawsuitImageName;

    /**
     * Gets all lawsuit image path.
     *
     * @return the all lawsuit image path
     */
    @Column(name = "all_lawsuit_image_path")
    public String getAllLawsuitImagePath() {
        return allLawsuitImagePath;
    }

    /**
     * Sets all lawsuit image path.
     *
     * @param allLawsuitImagePath the all lawsuit image path
     */
    public void setAllLawsuitImagePath(String allLawsuitImagePath) {
        this.allLawsuitImagePath = allLawsuitImagePath;
    }

    private String allLawsuitImagePath;

    /**
     * Gets application no.
     *
     * @return the application no
     */
    @Column(name = "application_no")
    public String getApplicationNo() {
        return applicationNo;
    }

    /**
     * Sets application no.
     *
     * @param applicationNo the application no
     */
    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    private String applicationNo;

    /**
     * Gets application date.
     *
     * @return the application date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "application_date")
    public Date getApplicationDate() {
        return applicationDate;
    }

    /**
     * Sets application date.
     *
     * @param applicationDate the application date
     */
    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    private Date applicationDate;

    /**
     * Gets applicant.
     *
     * @return the applicant
     */
    @Column(name = "applicant")
    public String getApplicant() {
        return applicant;
    }

    /**
     * Sets applicant.
     *
     * @param applicant the applicant
     */
    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    private String applicant;

    /**
     * Gets priority country.
     *
     * @return the priority country
     */
    @Column(name = "priority_country")
    public String getPriorityCountry() {
        return priorityCountry;
    }

    /**
     * Sets priority country.
     *
     * @param priorityCountry the priority country
     */
    public void setPriorityCountry(String priorityCountry) {
        this.priorityCountry = priorityCountry;
    }

    private String priorityCountry;

    /**
     * Gets priority no.
     *
     * @return the priority no
     */
    @Column(name = "priority_no")
    public String getPriorityNo() {
        return priorityNo;
    }

    /**
     * Sets priority no.
     *
     * @param priorityNo the priority no
     */
    public void setPriorityNo(String priorityNo) {
        this.priorityNo = priorityNo;
    }

    private String priorityNo;

    /**
     * Gets priority date.
     *
     * @return the priority date
     */
    @Column(name = "priority_date")
    public String getPriorityDate() {
        return priorityDate;
    }

    /**
     * Sets priority date.
     *
     * @param priorityDate the priority date
     */
    public void setPriorityDate(String priorityDate) {
        this.priorityDate = priorityDate;
    }

    private String priorityDate;

    /**
     * Gets publication no.
     *
     * @return the publication no
     */
    @Column(name = "publication_no")
    public String getPublicationNo() {
        return publicationNo;
    }

    /**
     * Sets publication no.
     *
     * @param publicationNo the publication no
     */
    public void setPublicationNo(String publicationNo) {
        this.publicationNo = publicationNo;
    }

    private String publicationNo;

    /**
     * Gets publication date.
     *
     * @return the publication date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "publication_date")
    public Date getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets publication date.
     *
     * @param publicationDate the publication date
     */
    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    private Date publicationDate;

    /**
     * Gets pct application no.
     *
     * @return the pct application no
     */
    @Column(name = "pct_application_no")
    public String getPctApplicationNo() {
        return pctApplicationNo;
    }

    /**
     * Sets pct application no.
     *
     * @param pctApplicationNo the pct application no
     */
    public void setPctApplicationNo(String pctApplicationNo) {
        this.pctApplicationNo = pctApplicationNo;
    }

    private String pctApplicationNo;

    /**
     * Gets pct application date.
     *
     * @return the pct application date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "pct_application_date")
    public Date getPctApplicationDate() {
        return pctApplicationDate;
    }

    /**
     * Sets pct application date.
     *
     * @param pctApplicationDate the pct application date
     */
    public void setPctApplicationDate(Date pctApplicationDate) {
        this.pctApplicationDate = pctApplicationDate;
    }

    private Date pctApplicationDate;

    /**
     * Gets pct publication no.
     *
     * @return the pct publication no
     */
    @Column(name = "pct_publication_no")
    public String getPctPublicationNo() {
        return pctPublicationNo;
    }

    /**
     * Sets pct publication no.
     *
     * @param pctPublicationNo the pct publication no
     */
    public void setPctPublicationNo(String pctPublicationNo) {
        this.pctPublicationNo = pctPublicationNo;
    }

    private String pctPublicationNo;

    /**
     * Gets pct publication date.
     *
     * @return the pct publication date
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "pct_publication_date")
    public Date getPctPublicationDate() {
        return pctPublicationDate;
    }

    /**
     * Sets pct publication date.
     *
     * @param pctPublicationDate the pct publication date
     */
    public void setPctPublicationDate(Date pctPublicationDate) {
        this.pctPublicationDate = pctPublicationDate;
    }

    private Date pctPublicationDate;

    /**
     * Gets abstracts.
     *
     * @return the abstracts
     */
    @Column(name = "abstracts")
    public String getAbstracts() {
        return abstracts;
    }

    /**
     * Sets abstracts.
     *
     * @param abstracts the abstracts
     */
    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    private String abstracts;

    /**
     * Gets exemplary claim.
     *
     * @return the exemplary claim
     */
    @Column(name = "exemplary_claim")
    public String getExemplaryClaim() {
        return exemplaryClaim;
    }

    /**
     * Sets exemplary claim.
     *
     * @param exemplaryClaim the exemplary claim
     */
    public void setExemplaryClaim(String exemplaryClaim) {
        this.exemplaryClaim = exemplaryClaim;
    }

    private String exemplaryClaim;

    /**
     * Gets country.
     *
     * @return the country
     */
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    /**
     * Sets country.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    private String country;

    /**
     * Gets wips on key.
     *
     * @return the wips on key
     */
    @Column(name = "wips_on_key")
    public String getWipsOnKey() {
        return wipsOnKey;
    }

    /**
     * Sets wips on key.
     *
     * @param wipsOnKey the wips on key
     */
    public void setWipsOnKey(String wipsOnKey) {
        this.wipsOnKey = wipsOnKey;
    }

    private String wipsOnKey;
}
