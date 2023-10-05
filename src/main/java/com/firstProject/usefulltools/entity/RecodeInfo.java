package com.firstProject.usefulltools.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="item_info")
@Data

public class RecodeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動増分方式
    private Long save_id;

    private String username;

    private String event;

    private String item;

    private String date_column;

    private int set_column;

    private int rep;

    private int weight;

}
