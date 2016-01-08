# SCR(Scroller)

네이버 한자사전 웹페이지의 정보를 스크롤러 한다

## Usage

### 소스 컴파일

* 서버 컴파일
```
> lein uberjar
> cd target
> java -jar scr.jar
```

* 클라이언트 컴파일 (만약 클라이언트의 소스를 수정시에 아래와 같이 함)
서버 컴파일보다 선행되어야 함
```
> gulp dist
```


실행 시 (옵션없이 실행시) user.home 에 생성되지만
아래 옵션과 같이 지정된 경로에 DB파일이 생성하게 할 수 있음.

```
> java -jar scr.jar -DH2_URL=/home/clonekim
```

 
TODO
* 중복배치 
* go block중 에러 발생 처리
* 화면 개선
 


## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
