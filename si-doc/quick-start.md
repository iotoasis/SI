# Quick Start

Oasis SI oneM2M Server(이하 oneM2M Server)를 처음 접하는 분들이 소스를 다운받고 쉽게 시험할 수 있도록 안내합니다.

oneM2M Server 시험은 아래의 순서로 진행할 수 있습니다.

> 1. MongoDB 다운로드 및 설치
> 2. Oasis SI oneM2M Server 소스 다운로드
> 3. oneM2M Server 빌드
> 4. oneM2M Server 설정 및 실행
> 5. HTTP 애뮬레이터(PostMan)를 이용한 시험

## Requirements
* JDK 7+ 
* Mongodb 3.x 
* Windows / Linux  

## 따라하기

#### (1) MongoDB, JDK 다운로드 및 설치
 - [mongodb 설치안내](https://docs.mongodb.org/v3.0/installation/)
 - [JDK 설치안내](http://docs.oracle.com/javase/7/docs/webnotes/install/)

#### (2) Oasis SI oneM2M Server 소스 다운로드
 - [릴리즈 페이지](https://github.com/iotoasis/SI/releases)에서 oneM2M Server 소스 및 설치관련 파일을 다운받는다.

#### (3) mongoDB 기본 셋팅
 - [릴리즈 페이지](https://github.com/iotoasis/SI/releases)에서 다운받은 mongodb 스크립트파일(mongodb_script.txt)를 실행시켜서 기본 컬렉션 및 색인을 생성한다.

#### (4) oneM2M Server 빌드
 - [릴리즈 페이지](https://github.com/iotoasis/SI/releases)에서 다운받은 oneM2M Server 소스를 이클립스에서 불러와서 Build한다.
 - oneM2M Server 소스를 빌드하는 방법은 [소스 Build방법](./build_eclipse.md)페이지를 참고한다.

#### (5) oneM2M Server 설정 및 실행
 - incse.xml파일을 오픈하여 oneM2M Server 설정을 수정한다.
 - oneM2M Server 설정방법은 [oneM2M서버 설정방법]((./configuration.md))페이지를 참고한다.

#### (6) HTTP 애뮬레이터(PostMan)을 이용한 시험
 - PostMan 프로그램을 설치한다. [PostMan 다운로드](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop)
 - [릴리즈 페이지](https://github.com/iotoasis/SI/releases)에서 다운받은 PostMan 스크립트 파일을 PostMan에 import한다.
 - PostMan에서 URL의 "localhost" 부분을 oneM2M서버를 실행한 서버의 IP로 수정하여 메시지 전송을 시험한다.

<br>
<br>
