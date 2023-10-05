package com.firstProject.usefulltools.entity;
//データーベースから取得したデータを格納するクラス
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
//データを取得したいテーブル名を指定
@Table(name="user_info")
@Data

public class UserInfo {
    
    @Id
    //変数名がColumn　nameと=の場合は不要
    @Column(name = "login_id")
    private String loginId;
    private String Password;

}
