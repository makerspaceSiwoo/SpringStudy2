package com.spring.file.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.spring.file.dto.FileinfoDto;

@Mapper
public interface FileinfoDao {

	int insertFile(FileinfoDto dto);//Mapper 아이디랑 똑같은 이름의 메소드
	
	List<FileinfoDto> fileList();
	
	@Select("select * from fileinfo where fileid=#{fileid}")
	FileinfoDto fileOne(@Param("fileid") int id);
}
