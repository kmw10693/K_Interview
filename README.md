# K_Ticket
TPS 10,000명을 목표로 하는 대량 트래픽 환경에서의 티켓 예매 서비스

## 주요 기능 아키텍처
<img width="761" alt="image" src="https://github.com/user-attachments/assets/ea202772-d4af-4573-937b-f3aab9b8816d" />


## 기술 스택

- **Backend**: Spring Boot + Spring Security + JPA + Redisson
- **Database**: PostgreSQL
- **Cache & Lock**: Redis + Redisson, RabbitMQ
- **Logging & Monitoring**: ELK (ElasticSearch, Logstash, Kibana)
- **Infra**: Docker + Nginx + AWS EC2

## 프로젝트 목표

- 대량 트래픽 환경에서의 **티켓 예매 동시성 문제 해결**
- 성능 병목 지점 파악 및 **지표 기반 개선**
- 비동기 처리 / Message Queue 사용
- JMeter를 이용한 부하 테스트

## 1. **공연/이벤트 관리**

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/v1/events` | [Admin] 공연 등록 |
| GET | `/api/v1/events` | 공연 리스트 조회 (필터: 날짜, 지역) |
| GET | `/api/v1/events/{eventId}` | 공연 상세 조회 |

## 2. **좌석 및 티켓 예매**

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/v1/events/{eventId}/seats` | 좌석 정보 및 잔여 좌석 표시 |
| POST | `/api/v1/tickets` | 티켓 예매 요청 (Redisson 사용) |
| GET | `/api/v1/tickets` | 내 예매 목록 조회 |
| DELETE | `/api/v1/tickets/{ticketId}` | 예매 취소 |
