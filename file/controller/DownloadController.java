package com.spring.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.file.dto.FileinfoDto;
import com.spring.file.service.FileinfoService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DownloadController {
	@Autowired
	FileinfoService fservice;
	
	@GetMapping("/list")
	public String list(Model m) {
		List<FileinfoDto> flist = fservice.fileList();
		m.addAttribute("flist",flist);
		return "file/list";
	}
	
	@GetMapping("/filedownload/{fileid}")
	public void filedownload(@PathVariable("fileid") int id
			,HttpServletResponse response)  throws IOException { // 클라이언트에게는 response가 전달됨 -> 브라우저에서 실행됨
		
		//DB select-id에 맞는 레코드 한 줄 꺼내옴
		FileinfoDto dto = fservice.fileOne(id);
		String path = ResourceUtils.getFile("classpath:static").toPath().toString();
		//다운로드 받은 파일 선택
		File file = new File(path, dto.getPath());
		//다운로드 받을 파일 이름을 인코딩(한글 이름인 경우)
		String fileName = new String(dto.getName().getBytes("utf-8"), "iso-8859-1");
	      response.setContentType("application/octet-stream; charset=utf-8");
	      response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");// 다운로드 받을 파일명 지정
	      // atachment : 보여줄 수 있는 형식의 파일도 무조건 다운로드
	      // filename: 다운로드 받을 파일명 지정
	      response.setHeader("Content-Transfer-Encoding", "binary");
	      // 여기까지 response header 설정
	      
	      //response 객체 body - 파일 내용을 넣어야
	      OutputStream out = response.getOutputStream(); // response에 연결된 outputStream 가져옴
	      
	      try (FileInputStream fis = new FileInputStream(file);){
	         FileCopyUtils.copy(fis, out);
	      } catch (IOException ex) {
	    	  System.out.println("파일 없음");
	      }
	      out.flush(); // oupput stram에 남아있는 내용이 있다면 보냄
	      // outputstream은 버퍼가 꽉 차게 되면 한번에 보내는데, 마지막 남은 부분은 버퍼를 다 채우지 못할 수 있으므로 끝까지 보냄

	}
	
	
}
