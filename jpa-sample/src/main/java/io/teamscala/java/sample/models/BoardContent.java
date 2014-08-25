package io.teamscala.java.sample.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.teamscala.java.jpa.Finder;
import io.teamscala.java.jpa.Model;
import io.teamscala.java.jpa.SimpleSearchListener;
import io.teamscala.java.sample.security.util.SecurityUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_board_content")
@org.hibernate.annotations.DynamicInsert(true)
@org.hibernate.annotations.DynamicUpdate(true)
@org.hibernate.annotations.BatchSize(size = 20)
public class BoardContent extends Model<Long> {

    // Finder

    public static final Finder<Long, BoardContent> find =
        new Finder<Long, BoardContent>(BoardContent.class,
            new SimpleSearchListener(2)
                .put("title", QBoardContent.boardContent.title)
                .put("content", QBoardContent.boardContent.content)
        );

    // Constants

    public static final String DEFAULT_ORDER = "id desc";

    // Fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(length = 200, nullable = false)
    private String title;

    @NotEmpty
    @Column(length = 1000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(updatable = false)
    private User regUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false)
    private User modUser;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDate;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(BoardComment.DEFAULT_ORDER)
    @JsonManagedReference
    private Set<BoardComment> comments = new HashSet<BoardComment>();

    // Entity events

    @PrePersist
    public void prePersist() {
        if (this.regDate == null) {
            this.regDate = new Date();
        }
        if (this.regUser == null) {
            this.regUser = SecurityUtils.getUser();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (this.modUser == null) {
            this.modUser = SecurityUtils.getUser();
        }
    }

    // Constructors

    public BoardContent() {
    }

    public BoardContent(Long id) {
        this.id = id;
    }

    // Generated Getters and Setters...

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getRegUser() {
        return regUser;
    }

    public void setRegUser(User regUser) {
        this.regUser = regUser;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public User getModUser() {
        return modUser;
    }

    public void setModUser(User modUser) {
        this.modUser = modUser;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public Set<BoardComment> getComments() {
        return comments;
    }

    public void setComments(Set<BoardComment> comments) {
        this.comments = comments;
    }

    public void addComment(BoardComment comment) {
        this.comments.add(comment);
        comment.setParent(this);
    }
}
