package com.spring.file.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.file.command.ReportCommand;


@Controller
public class ReportSubmissionController {

	@GetMapping("/report/submission")
	public String form() {
		return "report/submissionForm";
	}
	
	@PostMapping("/report/submitReport1")
	public String submitReport1(@RequestParam("studentNumber") String studentNumber,
			@RequestParam("report") MultipartFile report) {// 임시경로에 업로드 된 파일에 접근 가능
		printInfo(studentNumber, report);
		String filePath = upload(report); // filePath는 DB에 저장하는 용도
		
		return "report/submissionComplete";
	}
	
	private void printInfo(String studentNumber, MultipartFile file) {
		System.out.println(studentNumber + "가 업로드한 파일: "+file.getOriginalFilename());

	}
	
	private String makeFileName(String origName) {
		// ms(upload시간)+_random(시간이 같은경우방지).확장자
		long currentTime = System.currentTimeMillis();//현재 시간 - 서버
		Random random = new Random();
		int r = random.nextInt(50); // 0부터 49사이 랜덤값
		int index = origName.lastIndexOf("."); // 마지막 점의 위치
		String ext = origName.substring(index);
		return currentTime + "_" + r + ext; 
	}
	
	// c://upload 폴더에 저장
	private String upload(MultipartFile tempFile) {
		// 파일을 upload 폴더로 이동, 이동한 경로를 리턴 -> 이동 경로는 DB에 저장
		String filename = makeFileName(tempFile.getOriginalFilename());
		File newfile = new File("c:/upload/",filename); // 파일 객체 만들기/ 있으면 있는 거 쓰기
		try {
			tempFile.transferTo(newfile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return newfile.getPath();//파일 경로 리턴
	}
	
	//MultiPartFile 접근방법 2
	// 업로드 파일을 프로젝트 내 폴더에 저장시 클라이언트 화면에서 볼 수 있음
	// MultipartHttpServletRequest
	@PostMapping("/report/submitReport2")
	public String submitReport2(MultipartHttpServletRequest request, Model m) {
		// Multipart req와 HttpServlet req 인터페이스를 구현하는 클래스로, 
		//모든 request를 한번에 저장함 -> 꺼내서 사용
		//일반 파라미터 (이름=값)
		String studentNumber = request.getParameter("studentNumber");//Http servlet req의 메소드
		// 파일 파라미터(이름=파일내용,파일이름,파일종류)
		MultipartFile report = request.getFile("report");
		// 같은 이름으로 여러개의 파일이 전송되는 경우/ form tag 속성에 multiple 속성
//		List<MultipartFile> flist = request.getFiles("report");
		printInfo(studentNumber, report);
		// 프로젝트 내 mainImg 경로 찾아서 파일 만들고 데이터 이동
		String path = request.getServletContext().getRealPath("/mainImg"); // 프로젝트 안의 webapp 폴더
		String newFileName = makeFileName(report.getOriginalFilename());
		File newFile = new File(path, newFileName);
		try {
			report.transferTo(newFile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		m.addAttribute("path",newFileName); // mainImg 경로는 jsp에 있어서 파일 이름만 보냄
		
		return "report/submissionComplete";
	}
	
	//MultipartFile 접근 방법 3
	// 업로드 파일을 프로젝트 내 폴더(resources) 에 저장 시 클라이언트 화면에서 볼 수 있음
	@PostMapping("/report/submitReport3")
	public String submitReport3(ReportCommand command, Model m) { // 자동으로 객체 저장 (command 내부 변수 이름과 파라미터 이름이 일치해야 함)
		String newFileName= makeFileName(command.getReport().getOriginalFilename()); // multipartFile의 메소드
		File newFile = null;
		try {
			String path = 
					ResourceUtils.getFile("classpath:static/upload/").toPath().toString();
			newFile = new File(path, newFileName);
			System.out.println(path);
			System.out.println(newFileName);
			command.getReport().transferTo(newFile);
		} catch (IOException | IllegalStateException e) {
			e.printStackTrace();
		}//toPath는 객체로 저장 -> toString / 없으면 예외처리 하기
		m.addAttribute("path", newFileName);
		return "report/submissionComplete";
	}
	
	
	
	
}
