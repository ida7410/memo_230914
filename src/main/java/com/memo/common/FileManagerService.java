package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component // Spring bean
public class FileManagerService {
	
	// 실제 업로드 된 이미지가 저장될 경로(서버)
	public static final String FILE_UPLOAD_PATH = "C:\\megastudy\\6_spring_project\\MEMO\\memo_workspace\\images/";
//	public static final String FILE_UPLOAD_PATH = "";
	
	/**
	 * 
	 * @param loginId
	 * @param file
	 * @return 이미지 경로
	 */
	public String saveFile(String loginId, MultipartFile file) {
		// folder(directory) 생성
		// e.g.: aaaa_13854324684/sun.png
		//       = (loginId)_(miliseconds)/(file name)
		String direcotryName = loginId + "_" + System.currentTimeMillis();
		
		// C:\\megastudy\\6_spring_project\\MEMO\\memo_workspace\\images/aaaa_13854324684
		String filePath = FILE_UPLOAD_PATH + direcotryName;
		
		File directory = new File(filePath); // import java.io
		if (!directory.mkdir()) { // 폴더 생성 실패
			return null;
		}
		
		// 파일 업로드 = byte 단위
		try {
			byte[] bytes = file.getBytes();
			// ★★★★★ 한글 이름 이미지는 올릴 수 없으므로 나중에 영문자로 바꿔서 올리기
			// import java.nio
			Path path = Paths.get(filePath + "/" + file.getOriginalFilename());
			Files.write(path, bytes); // 실제 파일 업로드
		} catch (IOException e) {
			e.printStackTrace();
			return null; // 이미지 업로드 실패
		}
		
		// 파일 업로드가 성공했다면 웹 이미지 url path를 return
		// /images/aaaa_13854324684/sun.png
		return "/images/" + direcotryName + "/" + file.getOriginalFilename();
	}
	
	
	public void deleteFile(String imagePath) {
		// 주소에 겹치는 /images/ 지우기
		imagePath = imagePath.replace("/images/", "");
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath);
		
		// 삭제할 이미지 존재 확인
		if (Files.exists(path)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				log.info("[FileManagerService delete file] Failed to delete file. path:{}", path.toString());
			}
			
			// 폴더(directory) 삭제
			path = path.getParent();
			if (Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					log.info("[FileManagerService delete directory] Failed to delete file. path:{}", path.toString());					
				}
			}
		}
	}
}
