# 137.교통약자 주행 영상 서버 소스코드

## 스프링 설치

- https://spring.io/ 에서 스프링 다운로드 (개발은 3.9.8 버전의 STS에서 개발됨.)
- https://www.oracle.com/java/technologies/javase-downloads.html 에서 jdk 설치

## 환경 설정

- com.spring.controller의 Yolov5Controller 파일의 [29~31 Line](https://github.com/zlstl1/yolov5_server/blob/3ed875d7ebc6477eb8004449af8cc0ebff36a2cc/src/main/java/com/spring/controller/Yolov5Contoller.java#L29-L31) 부분을 사용자 PC 환경에 맞게 수정(절대경로 입력)
  - String save_folder = "/home/di-01/Downloads/yolov5/save/";
  - String yolov5_folder = "/home/di-01/Downloads/yolov5/";
  - String run_folder = "/home/di-01/Downloads/yolov5/runs/yolov5_server_run/";
  - save_folder : 업로드된 이미지가 저장될 폴더
  - yolov5_folder : https://github.com/zlstl1/yolov5_custom 을 통해 다운받은 Yolov5 폴더
  - run_folder : 분석된 이미지가 저장될 위치  
- [74](https://github.com/zlstl1/yolov5_server/blob/3ed875d7ebc6477eb8004449af8cc0ebff36a2cc/src/main/java/com/spring/controller/Yolov5Contoller.java#L74), [104 Line](https://github.com/zlstl1/yolov5_server/blob/3ed875d7ebc6477eb8004449af8cc0ebff36a2cc/src/main/java/com/spring/controller/Yolov5Contoller.java#L104)에 weights 파일명 부분 수정
  
- 다운받은 Yolov5 폴더의 detect.py를 서버용 [detect.py](https://github.com/zlstl1/yolov5_server/blob/master/detect.py)로 변경
- detect.py의 [129 Line](https://github.com/zlstl1/yolov5_server/blob/3ed875d7ebc6477eb8004449af8cc0ebff36a2cc/detect.py#L129)의 저장 경로를 위의 run_folder값으로 변경

## 모바일 앱 연동
- 모바일 앱으로부터 서버로 이미지 업로드 
  - 노면 데이터 : http://서버주소/yolov5/road
  - 표지판 검출 데이터 : http://서버주소/yolov5/sign
- 리턴 되는 Json 파싱 아래는 리턴되는 Json 예시
{
  "code":200,  
  "message":"성공했습니다.",
  "return_photo" : "~", // byte[]로 떨어지는 결과 이미지
  "return_resolution" : "640X480", // 결과 해상도
  "return_speed" : "0.019", // 결과 속도
   "road_data" : [ // 노면등급 확인하기 기능에 사용
      {
         "block_grade" : "A",
         "vibrate_grade" : "1"
      }
   ],
  "sign_data":[ // 점자표지판 확인하기 기능에 사용 
     {
        "odject_name":"stair_normal",
        "odject_cnt":"2"
     },
     {
        "odject_name":"handle_vertical",
        "odject_cnt":"1"
     }
  ]
}

## 응용서비스 개략도
![image](https://user-images.githubusercontent.com/35329451/107865319-73207480-6ea8-11eb-9a9d-c1a1864df1d8.png){: display = "block" margin = "auto"}

## 서버 리턴값 예시
![image](https://user-images.githubusercontent.com/35329451/107865320-76b3fb80-6ea8-11eb-9a86-0876a05f2612.png){: display = "block" margin = "auto"}

