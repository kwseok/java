package io.teamscala.java.sample.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.teamscala.java.jpa.Finder;
import io.teamscala.java.jpa.Model;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_code_group")
@org.hibernate.annotations.DynamicInsert(true)
@org.hibernate.annotations.DynamicUpdate(true)
@org.hibernate.annotations.BatchSize(size = 20)
public class CodeGroup extends Model<String> {

    // Finder

    public static final Finder<String, CodeGroup> find = new Finder<String, CodeGroup>(CodeGroup.class);

    // Constants

    public static final String DEFAULT_ORDER = "code asc";

    // Fields

    @Id
    @Column(length = 4)
    private String code;

    @NotEmpty
    @Column(length = 20, nullable = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date regDate;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDate;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<CodeRefer> refers = new HashSet<>(0);

    // Entity events

    @PrePersist
    public void prePersist() {
        if (this.regDate == null) this.regDate = new Date();
    }

    // Constructors

    public CodeGroup() {
    }

    public CodeGroup(String code) {
        this.code = code;
    }

    // Transeit methods

    @Transient
    @Override
    public String getId() {
        return code;
    }

    // Generated Getters and Setters...

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Set<CodeRefer> getRefers() {
        return refers;
    }

    public void setRefers(Set<CodeRefer> refers) {
        this.refers = refers;
    }

    public void addRefer(CodeRefer refer) {
        this.refers.add(refer);
        refer.setGroup(this);
    }
}
