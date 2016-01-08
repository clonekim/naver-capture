# SCR(Scroller)

네이버 한자사전 웹페이지 스크롤러 툴임
UI는 AngularJS 로 구성됨

## Usage

준비물

0. jdk8
1. node.js
2. leiningen

### 클라이언트 소스(자바스크립트) 컴파일

클라이언트의 소스를 수정 시 반드시 컴파일 하기 위해 node.js를 사용한다
모듈을 설치하기 위해 아래와 같이 한다

```
 npm install
```

소스 컴파일및 번들화 하기

```
 gulp dist
```

### 서버 소스 컴파일

```
 lein uberjar
```

### 실행

fatjar(standalone)로 형태로 target 경로 이하에 xxx.jar 이름으로
결과물이 생성됨

DB파일은 현재 로그인 사용자 디렉로리에 db라는 디렉토리로 생성됨

```
 java -jar xxx.jar
```

서버 실행 후 http://127.0.0.1:5678로 접근한다

db 디렉토리를 변경 시

```
 set H2_URL=C:/sample
 java -jar xxx.jar
```

 
TODO
* 중복배치 
* go block중 에러 발생 처리
* 화면 개선
 


## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
