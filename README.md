# K_Interview
동시 사용자 트래픽 환경에서의 면접 예약 서비스

## 주요 기능 아키텍처
<img width="761" alt="image" src="https://github.com/user-attachments/assets/ca39a1bf-5333-4d89-83a1-03512d0bfecf" />

## 기술 스택

- **Backend**: Spring Boot + Spring Security + JPA + Redisson
- **Database**: PostgreSQL
- **Cache & Lock**: Redis + Redisson, RabbitMQ
- **Infra**: Docker + Nginx + AWS EC2

## 프로젝트 목표

- 대량 트래픽 환경에서의 **인터뷰 예약 동시성 문제 해결**
- 성능 병목 지점 파악 및 **지표 기반 개선**
- 비동기 처리 / Message Queue 사용
- JMeter를 이용한 부하 테스트
