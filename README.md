# K_Interview
동시 사용자 트래픽 환경에서의 면접 예약 서비스

## 주요 기능 아키텍처
<img width="761" alt="image" src="https://github.com/user-attachments/assets/ea202772-d4af-4573-937b-f3aab9b8816d" />


## 기술 스택

- **Backend**: Spring Boot + Spring Security + JPA + Redisson
- **Database**: PostgreSQL
- **Cache & Lock**: Redis + Redisson, RabbitMQ
- **Infra**: Docker + Nginx + AWS EC2

## 프로젝트 목표

- 대량 트래픽 환경에서의 **티켓 예매 동시성 문제 해결**
- 성능 병목 지점 파악 및 **지표 기반 개선**
- 비동기 처리 / Message Queue 사용
- JMeter를 이용한 부하 테스트
