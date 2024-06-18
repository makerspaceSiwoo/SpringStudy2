package com.spring.file.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.file.dao.FileinfoDao;
import com.spring.file.dto.FileinfoDto;

@Service
public class FileinfoService {
	@Autowired
	FileinfoDao fdao;
	
	public int insertFile(FileinfoDto dto) {
		System.out.println("dto.getFileid() 전: "+dto.getFileid());
		int x = fdao.insertFile(dto);
		System.out.println("dto.getFileid() 후:"+dto.getFileid());
		return x;
	}
	
	public List<FileinfoDto> fileList(){
		return fdao.fileList();
	}
	
	public FileinfoDto fileOne(int id) {
		return fdao.fileOne(id);
	}
}
