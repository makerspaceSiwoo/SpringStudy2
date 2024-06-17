package com.spring.file.controller;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.file.dto.FileinfoDto;
import com.spring.file.service.FileinfoService;

@Controller
public class UploadController {
	
	@Autowired
	FileinfoService fservice;
	
	@GetMapping("/upload")
	public String form() {
		return "file/fileform";
	}
	
	@PostMapping("/upload")
	public String submit(FileinfoDto dto, @RequestParam("file") MultipartFile file) {
		if(!file.getOriginalFilename().equals("")) {
			// 파일이 있다면 업로드
			String fileName=upload(file);
			//db insert
			dto.setName(file.getOriginalFilename());
			dto.setPath("/upload/"+fileName); //jsp 파일에서 대신 upload를 붙일 수도 있음 - 그때는 빼주기
			dto.setFilesize(file.getSize());
			//model에 업로드 정보 추가
			fservice.insertFile(dto);
			
		}
		
		return "file/result";
	}
	private String makeFileName(String origName) {
		// ms(upload시간)+_random(시간이 같은경우방지).확장자
		long currentTime = System.currentTimeMillis();//현재 시간 - 서버
		Random random = new Random();
		int r = random.nextInt(50); // 0부터 49사이 랜덤값
		int index = origName.lastIndexOf("."); // 마지막 점의 위치
		String ext = origName.substring(index); // 점부터 확장자까지(끝까지)
		return currentTime + "_" + r + ext; 
	}
	
	private String upload(MultipartFile file) {
		String newFileName = makeFileName(file.getOriginalFilename());
		File newFile = null;
		try {
			String path = 
					ResourceUtils.getFile("classpath:static/upload/").toPath().toString();
			newFile = new File(path, newFileName);
			file.transferTo(newFile);
		} catch (IOException | IllegalStateException e) {
			e.printStackTrace();
		}//toPath는 객체로 저장 -> toString / 없으면 예외처리 하기
		return newFileName;//db저장용 - 파일 이름만 있으면 ok
	}
	
	
}
