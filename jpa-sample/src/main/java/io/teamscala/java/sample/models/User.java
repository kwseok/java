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
@Table(name = "t_user")
@org.hibernate.annotations.DynamicInsert(true)
@org.hibernate.annotations.DynamicUpdate(true)
@org.hibernate.annotations.BatchSize(size = 20)
public class User extends Model<Long> {

    // Finder

    public static final Finder<Long, User> find = new Finder<Long, User>(User.class);

    // Constants

    public static final String DEFAULT_ORDER = "id desc";

    // Fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(length = 20, nullable = false)
    private String username;

    @NotEmpty
    @Column(length = 128, nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(UserRole.DEFAULT_ORDER)
    @org.hibernate.annotations.BatchSize(size = 20)
    @JsonManagedReference
    private Set<UserRole> roles = new HashSet<UserRole>();

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
        if (this.regDate == null) {
            this.regDate = new Date();
        }
    }

    // Constructors

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    // Override for Model

    @Override
    public Long identifier() {
        return id;
    }

    // Generated Getters and Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public void addRole(UserRole role) {
        this.roles.add(role);
        role.setUser(this);
    }

    public void addRole(UserRole.Type type) {
        addRole(new UserRole(type));
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
