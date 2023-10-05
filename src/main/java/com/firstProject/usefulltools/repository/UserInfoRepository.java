package com.firstProject.usefulltools.repository;
//データベースから情報を取得してくるクラス
import org.springframework.data.jpa.repository.JpaRepository;

import com.firstProject.usefulltools.entity.UserInfo;
//JpaRepository<x,y>　x=Entityクラス名　y=fieldの型　今回は String loginidでString型なのでString
public interface UserInfoRepository extends JpaRepository<UserInfo, String>{

}

