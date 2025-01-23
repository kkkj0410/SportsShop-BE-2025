package com.teamproject.back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Integer rating;

    private LocalDateTime created_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users users;

    //***1.20 - 추가 제안***
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentComment_id")
    @JsonBackReference
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> childComments = new ArrayList<>();

    // ***(1.22)*** : int likes -> Likes likes
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<Likes> likes = new ArrayList<>();



    @PrePersist
    public void prePersist() {
        if (this.created_date == null) {
            this.created_date = LocalDateTime.now();
        }
    }


    public int depth(){
        int count = 0;
        Comment currentComment = this;
        while(currentComment.getParentComment() != null){
            count++;
            currentComment = this.getParentComment();
        }
        return count;
    }


    public void fetchItem(Item item){
        this.item = item;
        this.item.getComments().add(this);
    }

    public void fetchUsers(Users users){
        this.users = users;
        this.users.getComments().add(this);
    }

    public void fetchParentComment(Comment parentComment){
        this.item = parentComment.getItem();
        this.parentComment = parentComment;
        this.parentComment.getChildComments().add(this);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", likes=" + likes +
                ", created_date=" + created_date +
                '}';
    }
}
