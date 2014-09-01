package io.teamscala.java.sample.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.teamscala.java.jpa.Finder;
import io.teamscala.java.jpa.Model;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "t_board_comment")
@org.hibernate.annotations.DynamicInsert(true)
@org.hibernate.annotations.DynamicUpdate(true)
@org.hibernate.annotations.BatchSize(size = 20)
public class BoardComment extends Model<Long> {

    // Finder

    public static final Finder<Long, BoardComment> find = new Finder<Long, BoardComment>(BoardComment.class);

    // Constants

    public static final String DEFAULT_ORDER = "id desc";

    // Fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(length = 1000, nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date regDate;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "content_id")
    @JsonBackReference
    private BoardContent parent;

    // Entity events

    @PrePersist
    public void prePersist() {
        if (this.regDate == null) {
            this.regDate = new Date();
        }
    }

    // Constructors

    public BoardComment() {
    }

    public BoardComment(Long id) {
        this.id = id;
    }

    public BoardComment(BoardContent parent) {
        this.parent = parent;
    }

    // Generated Getters and Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public BoardContent getParent() {
        return parent;
    }

    public void setParent(BoardContent parent) {
        this.parent = parent;
    }
}
