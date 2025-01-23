package com.teamproject.back.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    public Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public Users users;

    @Override
    public String toString() {
        return "Likes{" +
                "id=" + id +
                '}';
    }

    public void fetchUser(Users user){
        this.users = user;
        this.users.getLikes().add(this);
    }

    public void fetchComment(Comment comment){
        this.comment = comment;
        this.comment.getLikes().add(this);
    }
}
