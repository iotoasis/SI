![iotoasis](https://github.com/iotoasis/SO/blob/master/logo_oasis_m.png)


## Oasis SI oneM2M Server

SI oneM2M Server는  IoT 국제표준인 oneM2M 기반의 디바이스 및 애플리케이션 연동을 지원하는 서버 프레임워크(IN-CSE)입니다. SI oneM2M Server 소스를 활용하여 oneM2M 기반의 디바이스 및 애플리케이션 연동 서버를 구축할 수 있습니다. 또한 oneM2M 코어 소스를 활용하여 AE, MN-CSE 등 다양한 oneM2M 컴포넌트를 개발할 수 있습니다.

 - Feature
   - oneM2M IN-CSE 서비스 Capabilities 기능 지원
   - HTTP, CoAP, MQTT 바인딩 기능 지원
   - oneM2M Mca, Mcc, Mcc' 레퍼런스 포인트 지원
   - SO, SDA 연동 기능 지원
   - MongoDB 기반의 리소스 데이터 관리


## Downloads
 - [Latest Release](https://github.com/iotoasis/SI/releases/)


## Documents
 - SI Server User Guide
     https://github.com/iotoasis/SI/tree/master/si-user-guide/SI_Server


## Modules
SI oneM2M Server를 시험하기 위한 디바이스 및 애플리케이션 애뮬레이터를 이용하여 테스트 할 수 있습니다.

- **DM Web Server** : Device를 제어하기 위한 웹 모듈
  - **Source Path** : /si-modules/DM_Web_Server
  - **User Guide** : /si-user-guide/DM_Web_Server
  - **DB query script** : /si-onem2m-res
  
- **LWM2M DM/IPE** : LWM2M Device 관리 및 IPE Server를 통한 SI Server와 인터워킹이 가능한 모듈
  - **Source Path1** : /si-modules/LWM2M_IPE_Server
  - **Source Path2** : /si-modules/LWM2M_IPE_Client
  - **User Guide** : /si-user-guide/LWM2M_DM_IPE
  
- **OIC IPE** : OIC 기반 Device Handling 및 IPE 를 통한 SI Server와 인터워킹이 가능한 모듈
  - **Source Path1** : /si-modules/OIC-IPE
  - **Source Path2** : /si-modules/OIC-IPE-Client
  - **User Guide** : /si-user-guide/OIC_IPE
  

## Q&A
 - [IoT Oasis Q&A -- Coming soon]


## License
Licensed under the BSD License, Version 2.0

