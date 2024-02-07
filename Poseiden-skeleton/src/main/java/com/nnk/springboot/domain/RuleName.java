package com.nnk.springboot.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "rulename")
public class RuleName {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="json")
    private String json;

    @Column(name="template")
    private String template;

    @Column(name="sqlStr")
    private String sqlStr;

    @Column(name="sqlPart")
    private String sqlPart;

    public RuleName(String ruleName, String description, String json, String template, String sql, String sqlPart) {
        this.name =  ruleName;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sql;
        this.sqlPart = sqlPart;
    }

    public RuleName() {

    }
}
