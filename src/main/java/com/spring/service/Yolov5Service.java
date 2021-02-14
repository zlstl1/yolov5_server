package com.spring.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Yolov5Service {
	
	public String getUuid() {
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmssSSS");
		Calendar time = Calendar.getInstance();
		String format_time1 = format1.format(time.getTime());
		return format_time1+".jpg";
//		return UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
	}
	
	public void makeDir(String outputDir) {
		File desti = new File(outputDir);	
		
		if(!desti.exists()){				
			desti.mkdirs(); 
		}
	}
	
	public String saveFile(String workDir, MultipartFile file) {
		String filename = "";
		try {
			makeDir(workDir);
			filename = getUuid();
			File dumpfile = new File(workDir + "/" + filename);
			file.transferTo(dumpfile);
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
		return filename;
	}
	
	public HashMap<String, Object> road_runCli(String[] cli, String workDir) {
		HashMap<String, Object> return_json = new LinkedHashMap<String, Object>();
		return_json.put("code", "200");
		return_json.put("message", "성공했습니다.");
		return_json.put("return_photo", new byte[1]);
		BufferedReader br = null;
		Process p = null;
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.redirectErrorStream(true);
			builder.command(cli);
			builder.directory(new File(workDir));
			
			p = builder.start();
			p.getErrorStream().close();
			p.getOutputStream().close();
			
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			String return_resolution = "";
			String return_speed = "";
			List<String> det_arr = new ArrayList<String>();
			List<HashMap<String, Object>> road_data = new ArrayList<HashMap<String, Object>>();
			while((line = br.readLine()) != null) {
				if(line.contains("return_resolution : ")) {
					line = line.replaceFirst("return_resolution : ", ""); 
					line = line.trim();
					return_resolution = line;
				}
				if(line.contains("det_list : ")) {
					line = line.replaceFirst("det_list : ", ""); 
					line = line.trim();
					det_arr.add(line);
				}
				if(line.contains("return_speed : ")) {
					line = line.replaceFirst("return_speed : ", ""); 
					line = line.trim();
					return_speed = line;
				}
			}
			return_json.put("return_resolution",return_resolution);
			return_json.put("return_speed",return_speed);
			
			for(int i=0;i<det_arr.size();i++) {
				String det = det_arr.get(i);
				String det_class = det.split(" ")[1];
				if (det_class.equals("flatness_A")) {
					HashMap<String, Object> det_road_data = new HashMap<String, Object>();
					det_road_data.put("block_grade", "A");
					det_road_data.put("vibrate_grade", "1");
					road_data.add(det_road_data);
				}else if (det_class.equals("flatness_B")) {
					HashMap<String, Object> det_road_data = new HashMap<String, Object>();
					det_road_data.put("block_grade", "B");
					det_road_data.put("vibrate_grade", "2");
					road_data.add(det_road_data);
				}else if (det_class.equals("flatness_C")) {
					HashMap<String, Object> det_road_data = new HashMap<String, Object>();
					det_road_data.put("block_grade", "C");
					det_road_data.put("vibrate_grade", "3");
					road_data.add(det_road_data);
				}else if (det_class.equals("flatness_D")) {
					HashMap<String, Object> det_road_data = new HashMap<String, Object>();
					det_road_data.put("block_grade", "D");
					det_road_data.put("vibrate_grade", "4");
					road_data.add(det_road_data);
				}else if (det_class.equals("flatness_E")) {
					HashMap<String, Object> det_road_data = new HashMap<String, Object>();
					det_road_data.put("block_grade", "E");
					det_road_data.put("vibrate_grade", "5");
					road_data.add(det_road_data);
				}
			}
			return_json.put("road_data",road_data);
			return_json.put("sign_data",new ArrayList<String>());
			p.waitFor();
		}catch(Exception e) {
			HashMap<String, Object> error_json = new LinkedHashMap<String, Object>();
			error_json.put("code", 400);
			error_json.put("message", e);
			e.printStackTrace();
			return error_json;
		}finally {
			try {
				if(br != null) {
					br.close();
				}
				if(p != null) {
					p.getInputStream().close();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return return_json;
	}
	
	public HashMap<String, Object> sign_runCli(String[] cli, String workDir) {
		
		HashMap<String,String> eng_kor_map = new HashMap<String,String>(){{
			put("walkway_paved","포장도로"); put("walkway_block","보도블럭"); put("paved_state_broken","포장도로 파손"); put("paved_state_normal","포장도로 정상");
			put("block_state_broken","보도블럭 파손"); put("block_state_normal","보도블럭 정상"); put("block_kind_bad","주행성 나쁨"); put("block_kind_good","주행성 좋음");
			put("outcurb_rectangle","사각모서리 연석 정상"); put("outcurb_slide","경사형 연석 정상"); put("outcurb_rectangle_broken","사각모서리 연석 파손"); put("outcurb_slide_broken","경사형 연석 파손");
			put("restspace","휴식참"); put("sidegap_in","문턱 (실내)"); put("sidegap_out","문턱 (실외)");
			put("sewer_cross","격자형 배수구"); put("sewer_line","비 격자형 배수구");
			put("brailleblock_dot","점형블록"); put("brailleblock_line","선형블록"); put("brailleblock_dot_broken","점형블록 파손"); put("brailleblock_line_broken","선형블록 파손");
			put("continuity_tree","연속하지 않음 (가로수영역)"); put("continuity_manhole","연속하지 않음 (맨홀)"); put("ramp_yes","미끄럼방지 있는 경사로"); put("ramp_no","미끄럼방지 없는 경사로");
			put("bicycleroad_broken","자전거 도로 파손"); put("bicycleroad_normal","자전거 도로 정상"); put("planecrosswalk_broken","평면횡단보도 파손"); put("planecrosswalk_normal","평면횡단보도 정상");
			put("steepramp","간이 부분경사로"); put("bump_slow","과속방지턱"); put("bump_zigzag","지그재크형 도로"); put("weed","잡초");
			put("floor_normal","복도 바닥 정상"); put("floor_broken","복도바닥 파손"); put("flowerbed","화단"); put("parkspace","주차공간");
			put("tierbump","타이어 방지턱"); put("stone","경관용 돌"); put("enterrail","주출입문 레일"); put("fireshutter","방화셔터 바닥홈");

			put("stair_normal","계단"); put("stair_broken","계단 파손"); put("wall","벽");
			put("window_sliding","미서기창"); put("window_casement","여닫이창"); put("pillar","기둥"); put("lift","승강기 (시설)");
			put("door_normal","문"); put("door_rotation","회전문"); put("lift_door","승강기 출입문"); put("resting_place_roof","휴게시설");
			put("reception_desk","하부공간이 있는 접수대"); put("protect_wall_protective","방호울타리"); put("protect_wall_guardrail","접근방지용 난간"); put("protect_wall_kickplate","킥플레이트");
			put("handle_vertical","수직막대형 손잡이"); put("handle_lever","레버형 손잡이"); put("handle_circular","원형 손잡이");
			put("lift_button_normal","일반(조작설비영역) 버튼"); put("lift_button_openarea","문열림닫침 영역 버튼"); put("lift_button_layer","층수영역 버튼"); put("lift_button_emergency","비상벨영역 버튼");
			put("direction_sign_left","왼쪽이동표식"); put("direction_sign_right","오른쪽이동표식"); put("direction_sign_straight","화살표 (후진/직직 포함)"); put("direction_sign_exit","출구표식");
			put("sign_disabled_toilet","(장애인) 화장실표시판"); put("sign_disabled_parking","(장애인) 주차표지판"); put("sign_disabled_elevator","(장애인) 교통약자 전용 엘리베이터 표지판");
			put("sign_disabled_ramp","(장애인) 경사로 안내 표지판"); put("sign_disabled_callbell","(장애인) 비상 호출벨 안내표지판"); put("sign_disabled_icon","장애인 아이콘 표시");
			put("braille_sign","표지판 내 점자 영역"); put("chair_multi","다인용 평의자"); put("chair_one","일인용 의자"); put("chair_circular","원형 의자");
			put("chair_back","등받이가 있는 휴게 의자"); put("chair_handle","손잡이가 있는 휴게 의자"); put("number_ticket_machine","번호표");
			put("beverage_vending_machine","판매기"); put("beverage_desk","음료대"); put("trash_can","휴지통"); put("mailbox","우체통");
		}};
		
		HashMap<String, Object> return_json = new LinkedHashMap<String, Object>();
		return_json.put("code", "200");
		return_json.put("message", "성공했습니다.");
		return_json.put("return_photo", new byte[1]);
		BufferedReader br = null;
		Process p = null;
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.redirectErrorStream(true);
			builder.command(cli);
			builder.directory(new File(workDir));
			
			p = builder.start();
			p.getErrorStream().close();
			p.getOutputStream().close();
			
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			String return_resolution = "";
			String return_speed = "";
			List<String> det_arr = new ArrayList<String>();
			List<String> road_arr = new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					add("flatness_A");
					add("flatness_B");
					add("flatness_C");
					add("flatness_D");
					add("flatness_E");
				}
			};
			List<HashMap<String, Object>> sign_data = new ArrayList<HashMap<String, Object>>();
			while((line = br.readLine()) != null) {
				if(line.contains("return_resolution : ")) {
					line = line.replaceFirst("return_resolution : ", ""); 
					line = line.trim();
					return_resolution = line;
				}
				if(line.contains("det_list : ")) {
					line = line.replaceFirst("det_list : ", ""); 
					line = line.trim();
					det_arr.add(line);
				}
				if(line.contains("return_speed : ")) {
					line = line.replaceFirst("return_speed : ", ""); 
					line = line.trim();
					return_speed = line;
				}
			}
			return_json.put("return_resolution",return_resolution);
			return_json.put("return_speed",return_speed);
			
			for(int i=0;i<det_arr.size();i++) {
				String det = det_arr.get(i);
				String det_count = det.split(" ")[0];
				String det_class = det.split(" ")[1];
				if (road_arr.contains(det_class)) {
					continue;
				}else {
					HashMap<String, Object> det_road_data = new LinkedHashMap<String, Object>();
					det_road_data.put("object_name", eng_kor_map.get(det_class));
					det_road_data.put("object_cnt", det_count);
					sign_data.add(det_road_data);
				}
			}
			return_json.put("road_data",new ArrayList<String>());
			return_json.put("sign_data",sign_data);
			p.waitFor();
		}catch(Exception e) {
			HashMap<String, Object> error_json = new LinkedHashMap<String, Object>();
			error_json.put("code", "400");
			error_json.put("message", e);
			e.printStackTrace();
			return error_json;
		}finally {
			try {
				if(br != null) {
					br.close();
				}
				if(p != null) {
					p.getInputStream().close();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return return_json;
	}
}
