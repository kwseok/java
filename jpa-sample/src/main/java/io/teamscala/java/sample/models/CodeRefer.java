package io.teamscala.java.sample.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.teamscala.java.jpa.Finder;
import io.teamscala.java.jpa.Model;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_code_refer")
@org.hibernate.annotations.DynamicInsert(true)
@org.hibernate.annotations.DynamicUpdate(true)
@org.hibernate.annotations.BatchSize(size = 20)
public class CodeRefer extends Model<CodeReferId> {

    // Finder

    public static final Finder<CodeReferId, CodeRefer> find = new Finder<CodeReferId, CodeRefer>(CodeRefer.class);

    // Constants

    public static final String DEFAULT_ORDER = "id asc";

    // Static inner classes

    // Fields

    @EmbeddedId
    private CodeReferId id;

    @MapsId("groupCode")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_code")
    @JsonBackReference
    private CodeGroup group;

    @NotEmpty
    @Column(length = 20, nullable = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date regDate;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDate;

    // Entity events

    @PrePersist
    public void prePersist() {
        System.out.println("=======================================");
        System.out.println("prePersist");
        System.out.println("=======================================");
        if (this.regDate == null) this.regDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println("=======================================");
        System.out.println("preUpdate");
        System.out.println("=======================================");
    }

    // Constructors

    public CodeRefer() {
    }

    public CodeRefer(CodeReferId id) {
        this.id = id;
    }

    // Transient methods

    @Transient
    public CodeReferId getOrCreateId() {
        if (id == null) id = new CodeReferId();
        return id;
    }

    @Transient
    public String getCode() {
        return this.id != null ? this.id.getCode() : null;
    }

    public void setCode(String code) {
        this.getOrCreateId().setCode(code);
    }

    // Generated Getters and Setters...

    @Override
    public CodeReferId getId() {
        return id;
    }

    public void setId(CodeReferId id) {
        this.id = id;
    }

    public CodeGroup getGroup() {
        return group;
    }

    public void setGroup(CodeGroup group) {
        this.group = group;
        this.getOrCreateId().setGroupCode(group.getCode());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }
}
