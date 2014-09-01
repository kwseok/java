package io.teamscala.java.sample.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.teamscala.java.jpa.Finder;
import io.teamscala.java.jpa.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "t_user_role")
@org.hibernate.annotations.DynamicInsert(true)
@org.hibernate.annotations.DynamicUpdate(true)
@org.hibernate.annotations.BatchSize(size = 20)
public class UserRole extends Model<Long> {

    // Finder

    public static final Finder<Long, UserRole> find = new Finder<Long, UserRole>(UserRole.class);

    // Constants

    public static final String DEFAULT_ORDER = "id desc";

    // Enums

    public enum Type {ROLE_ADMIN, ROLE_MEMBER}

    // Fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date regDate;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // Entity events

    @PrePersist
    public void prePersist() {
        if (this.regDate == null) {
            this.regDate = new Date();
        }
    }

    // Constructors

    public UserRole() {
    }

    public UserRole(Type type) {
        this.type = type;
    }

    // Generated Getters and Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
