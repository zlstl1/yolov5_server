package com.spring.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.service.Yolov5Service;

@Controller
public class Yolov5Contoller {

	@Autowired
	Yolov5Service yolov5Service;
	
	String save_folder = "/home/di-01/Downloads/yolov5/save/";
	String yolov5_folder = "/home/di-01/Downloads/yolov5/";
	String run_folder = "/home/di-01/Downloads/yolov5/runs/yolov5_server_run/";
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String index() {
		return "test";
	}
	
//	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
//	public @ResponseBody byte[] upload(@RequestParam("image") MultipartFile img, HttpServletRequest request) throws IOException{
//		String filename = yolov5Service.saveFile(save_folder, img);
//		
//		String[] cmd = new String[] {
//				"python3",
//				"detect.py",
//				"--source",
//				save_folder + filename,
//				"--weight",
//				"yolov5_model.pt"
//		};
//		yolov5Service.road_runCli(cmd, yolov5_folder);
//		
//		InputStream in = new BufferedInputStream(new FileInputStream(run_folder + filename));
//		
//	    return IOUtils.toByteArray(in);
//			
//	}
	
	@RequestMapping(value = "/road", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> road(@RequestParam("image") MultipartFile img, HttpServletRequest request) throws IOException{
		if(img.isEmpty()) {
			HashMap<String, Object> error_json = new LinkedHashMap<String, Object>();
			error_json.put("code", "400");
			error_json.put("message", "이미지 파일을 찾을 수 없음");
			return error_json;
		}
		String filename = yolov5Service.saveFile(save_folder, img);
		
		String[] cmd = new String[] {
				"python3",
				"detect.py",
				"--source",
				save_folder + filename,
				"--weight",
				"yolov5_model.pt"
		};
		
		HashMap<String, Object> return_json = yolov5Service.road_runCli(cmd, yolov5_folder);
		
		if (return_json.get("code") == "400") {
			return return_json;
		}
		InputStream in = new BufferedInputStream(new FileInputStream(run_folder + filename));
		return_json.put("return_photo", IOUtils.toByteArray(in));
		
		return return_json;
	}
	
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> sign(@RequestParam("image") MultipartFile img, HttpServletRequest request) throws IOException{
		if(img.isEmpty()) {
			HashMap<String, Object> error_json = new LinkedHashMap<String, Object>();
			error_json.put("code", "400");
			error_json.put("message", "이미지 파일을 찾을 수 없음");
			return error_json;
		}
		String filename = yolov5Service.saveFile(save_folder, img);
		
		String[] cmd = new String[] {
				"python3",
				"detect.py",
				"--source",
				save_folder + filename,
				"--weight",
				"yolov5_model.pt"
		};
		
		HashMap<String, Object> return_json = yolov5Service.sign_runCli(cmd, yolov5_folder);
		
		if (return_json.get("code") == "400") {
			return return_json;
		}
		InputStream in = new BufferedInputStream(new FileInputStream(run_folder + filename));
		return_json.put("return_photo", IOUtils.toByteArray(in));
		
		return return_json;
	}
	
}

