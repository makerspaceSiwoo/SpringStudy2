package com.spring.file.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.spring.file.dto.FileinfoDto;

@Mapper
public interface FileinfoDao {

	int insertFile(FileinfoDto dto);//Mapper 아이디랑 똑같은 이름의 메소드
}
