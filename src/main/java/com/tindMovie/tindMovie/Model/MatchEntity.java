package com.tindMovie.tindMovie.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "match")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "user_id1")
    private Long userId1;

    @Getter
    @Column(name = "user_id2")
    private Long userId2;

    @Getter
    @Column(name = "film_id")
    private Long filmId;


}
