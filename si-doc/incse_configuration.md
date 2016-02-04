# SI oneM2M 서버 설정

SI oneM2M서버 설정방법을 설명합니다.

설정은 XML 파일로 작성되며 설치 루트 폴더에 "incse.xml"로 저장됩니다.

```
incse.xml
```
<br>
<br>

설정은 XML 파일로 작성되며 설치 루트 폴더에 "incse.xml"로 저장됩니다.
설정항목별 의미는 아래와 같습니다.


Item              | Description
----------------- | --------------------------
database     | 데이터베이스 호스트명
database.host     | 데이터베이스 호스트명
database.port     | 데이터베이스 포트번호
database.dbname   | 데이터베이스 이름
database.user     | 데이터베이스 유저명
database.password | 데이터베이스 유저 비밀번호
cse          | IN CSE 서버 정보
cse.host          | IN CSE 호스트명 (IP주소 또는 도메인)
cse.basename      | IN CSE의 CSE명
cse.resourceId    | IN CSE CSE ID
cse.poa           | IN CSE의 접근 주소 (PointOfAccess)
cse.httpPort      | IN CSE의 HTTP 바인딩 포트번호
cse.httpsPort      | IN CSE의 HTTPS 바인딩 포트번호
cse.restPort      | IN CSE의 RESTful API 포트번호
cmdh     | IN CSE의 전송정책
cmdh.commandTimeout     | 메시지 전송 타임아웃 시간 (초)
cmdh.commandExpireTimerInterval     | 메시지 타임아웃 체커 타이머 주기
remoteCSEs   | 연동 플랫폼 정보 목록
remoteCSEs.remoteCSE     | 연동 플랫폼의의 IN CSE 정보 (복수의 설정 가능)
remoteCSEs.remoteCSE.cseId | 연동 플랫폼의 CSE ID
remoteCSEs.remoteCSE.cseName | 연동 플랫폼의 CSE 이름 (baseName)
remoteCSEs.remoteCSE.cseHost | 연동 플랫폼의 CSE 호스트명
remoteCSEs.remoteCSE.maxTPS | 플랫폼 연동시 허용하는 최대 TPS
dms | 연동 DM서버 정보
dms.hitdm | 연동 DM서버 중 Herit DM서버 정보
dms.hitdm.address | Herit DM서버 주소
qos | QoS 정책
qos.maxPollingSessionNo | 최대 폴링 세션 수
qos.maxAENo | 최대 등록 AE 수
qos.maxCSENo | 최대 등록 CSE 수
resourcePolicy | 리소스 운영 정책
resourcePolicy.maxCIPerContainer | Container당 최대 contentInstance 수
mqtt | MQTT 바인딩 관련 정보
mqtt.broker | 바인딩을 위해서 연동하는 MQTT 브로커 정보
mqtt.broker.address | MQTT 브로커 주소

<br>
<br>

아래는 설정파일 샘플입니다.

```
<?xml version="1.0" encoding="UTF-8"?>
<incse>
        <!-- <database>
                <host>10.101.101.195</host>
                <port>27017</port>
                <dbname>IITP-IOT</dbname>
                <user></user>
                <password></password>
        </database> -->
        <database>
                <host>166.104.112.36</host>
                <port>27017</port>
                <dbname>IITP-IOT</dbname>
                <user></user>
                <password></password>
        </database>
        <cse>
                <host>10.101.101.195</host>
                <baseName>herit-cse</baseName>
                <resourceId>herit-in</resourceId>
                <poa>http://10.101.101.195:8090</poa>
                <httpPort>8080</httpPort>
                <restPort>8081</restPort>
        </cse>
        <cmdh>
                <commandTimeout>10</commandTimeout>
                <commandExpireTimerInterval>1</commandExpireTimerInterval>
        </cmdh>

		<remoteCSEs>
			<remoteCSE>
				<cseId>herit-mn1</cseId>
				<cseName>herit-cse</cseName>
				<cseHost>mn1.onem2m.herit.net</cseHost>
				<maxTPS>1</maxTPS>
			</remoteCSE>
			<remoteCSE>
				<cseId>herit-mn2</cseId>
				<cseName>herit-cse</cseName>
				<cseHost>mn1.onem2m.herit.net</cseHost>
				<maxTPS>1</maxTPS>
			</remoteCSE>
			<remoteCSE>
				<cseId>herit-mn3</cseId>
				<cseName>herit-cse</cseName>
				<cseHost>mn1.onem2m.herit.net</cseHost>
				<maxTPS>1</maxTPS>
			</remoteCSE>
		</remoteCSEs>
		<dms>
			<hitdm>
				<address>http://10.101.101.107:8888</address>
			</hitdm>
		</dms>
		<qos>
			<maxPollingSessionNo>-1</maxPollingSessionNo>
			<maxAENo>10000</maxAENo>
			<maxCSENo>10000</maxCSENo>
		</qos>
		
		<resourcePolicy>
			<maxCIPerContainer>50</maxCIPerContainer>		
		</resourcePolicy>
        <mqtt>
                <broker>
                        <address>tcp://iot.eclipse.org:1883</address>
                </broker>
        </mqtt>

</incse>


```

<br>
<br>
