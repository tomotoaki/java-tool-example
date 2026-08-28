package com.example.tool.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.tool.entity.AccessToken;

/**
 * access_token テーブルへの CRUD を行う MyBatis マッパー。
 * 実装は resources/mapper/AccessTokenMapper.xml に記述する。
 */
@Mapper
public interface AccessTokenMapper {

    int insert(AccessToken accessToken);

    Optional<AccessToken> findById(@Param("id") Long id);

    List<AccessToken> findAll();

    int update(AccessToken accessToken);

    int deleteById(@Param("id") Long id);
}
