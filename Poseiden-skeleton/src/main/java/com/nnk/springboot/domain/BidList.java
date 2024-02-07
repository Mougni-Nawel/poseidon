package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "bidlist")
public class BidList {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="BidListId")
    int bidListId;

    @Column(name="account")
    String account;

    @Column(name="type")
    String type;

    @Column(name="bidQuantity")
    double bidQuantity;

    @Column(name="askQuantity")
    double askQuantity;

    @Column(name="bid")
    double bid;

    @Column(name="ask")
    double ask;

    @Column(name="benchmark")
    String benchmark;

    @Column(name="bidListDate")
    Timestamp bidListDate;

    @Column(name="commentary")
    String commentary;

    @Column(name="security")
    String security;

    @Column(name="status")
    String status;

    @Column(name="trader")
    String trader;

    @Column(name="book")
    String book;

    @Column(name="creationName")
    String creationName;

    @Column(name="creationDate")
    Timestamp creationDate;

    @Column(name="revisionName")
    String revisionName;

    @Column(name="revisionDate")
    Timestamp revisionDate;

    @Column(name="dealName")
    String dealName;

    @Column(name="dealType")
    String dealType;

    @Column(name="sourceListId")
    String sourceListId;

    @Column(name="side")
    String side;

    public BidList(String account, String type, double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

    public BidList() {

    }

    public int getBidListId() {
        return bidListId;
    }
}
