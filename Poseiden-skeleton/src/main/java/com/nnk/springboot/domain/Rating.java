package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="moodysRating")
    private String moodysRating;

    @Column(name="sandPRating")
    private String sandPRating;

    @Column(name="fitchRating")
    private String fitchRating;

    @Column(name="orderNumber")
    private int orderNumber;

    public Rating(String moodysRating, String sandPRating, String fitchRating, int orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.orderNumber = orderNumber;
        this.id = id;
    }

    public Rating() {

    }
}
