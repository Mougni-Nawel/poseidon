package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "trade")
public class Trade {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="TradeId")
    private int TradeId;
    @Column(name="account")
    private String account;
    @Column(name="type")
    private String type;
    @Column(name="buyQuantity")
    private Double buyQuantity;
    @Column(name="sellQuantity")
    private Double sellQuantity;
    @Column(name="buyPrice")
    private Double buyPrice;
    @Column(name="sellPrice")
    private Double sellPrice;
    @Column(name="tradeDate")
    private Date tradeDate;
    @Column(name="security")
    private String security;
    @Column(name="status")
    private String status;
    @Column(name="trader")
    private String trader;
    @Column(name="benchmark")
    private String benchmark;
    @Column(name="book")
    private String book;
    @Column(name="creationName")
    private String creationName;
    @Column(name="creationDate")
    private Date creationDate;
    @Column(name="revisionName")
    private String revisionName;
    @Column(name="revisionDate")
    private Date revisionDate;
    @Column(name="dealName")
    private String dealName;
    @Column(name="dealType")
    private String dealType;
    @Column(name="sourceListId")
    private String sourceListId;
    @Column(name="side")
    private String side;


}
