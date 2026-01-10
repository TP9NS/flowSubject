# 파일 확장자 차단 시스템 (File Extension Control System)

## 1. 과제 개요

**파일 업로드 시 확장자를 기준으로 보안 정책을 적용하여 파일 업로드를 제어하는 시스템**입니다.  
관리자는 확장자 차단 정책을 관리할 수 있으며, 사용자는 해당 정책에 따라 파일을 업로드·조회·다운로드할 수 있습니다.

---

## 2. 기술 스택

### Backend
- Java 17
- Spring Boot 3.5.x
- Spring Security
- Spring Data JPA
- JWT 기반 인증

### Frontend
- Thymeleaf
- Vanilla JavaScript (Fetch API)
- CSS (Custom UI)

### Database
- MySQL 8.0
- H2 (로컬 개발용)

### Infra / DevOps
- Docker / Docker Compose
- AWS EC2 (ubuntu22) t3.small
- GitHub

> 과제 제출용으로 별도의 Web Server(Nginx)는 사용하지 않고,  
> **Spring Boot 내장 Tomcat + Thymeleaf** 구조로 단순하게 구성했습니다.

---

## 3. 기능 설명

### 3-1. 회원 / 인증

- 회원가입 / 로그인 / 로그아웃 기능 제공
- JWT 기반 인증 방식 사용
- **Access Token만 사용**
    - Refresh Token을 분리하지 않은 이유:
        - 과제 범위 내에서 인증 구조를 단순화
        - 토큰 관리는 쿠키 기반으로 처리하여 프론트 구현 부담 최소화


---

### 3-2. 사용자 기능

- 파일 업로드
    - 서버 업로드 시 확장자 검사
    - 차단된 확장자는 업로드 즉시 차단
      - 파일은 서버 로컬 디렉터리에 저장
      - DB에는 메타 정보만 저장
- 내가 업로드한 파일 목록 조회
- 파일 다운로드
- 파일 삭제
- 페이지네이션 적용


---

### 3-3. 관리자 기능

#### 1) 확장자 정책 관리
- **고정 확장자**
    - 미리 정의된 확장자 목록
    - 체크박스를 통해 차단 여부 ON/OFF
- **커스텀 확장자**
    - 관리자가 직접 확장자 추가 및 삭제
    - 추가된 확장자는 항상 차단
    - 최대 200개 제한
    - 확장자 중복 X
    - 대소문자 통합 관리

#### 2) 파일 관리
- 전체 사용자 업로드 파일 조회
- 확장자 기준 검색
- 관리자 권한으로 파일 삭제 가능
- 페이지네이션 적용

#### 3) 변경 로그 관리
-  확장자 정책 변경 이력 기록
- 기록 항목
    - 변경 시각
    - 변경 타입 (CREATE, UPDATE, DELETE, CHECK, UNCHECK)
    - 대상 확장자
    - 수행 관리자
- 로그 목록은 페이지 단위로 조회 가능

> 모든 정책 변경은 **로그로 남기도록 강제**하여  
> “누가, 언제, 무엇을 변경했는지”를 추적할 수 있도록 했습니다.

---

## 4. API 명세 및 접근 정책

### API 접근 정책
- `/api/auth/**`
    - 인증 없이 접근 가능
- `/api/user/**`
    - 로그인 사용자만 접근 가능
- `/api/admin/**`
    - ADMIN 권한 사용자만 접근 가능
---

### 주요 API 예시

#### 인증

- POST /api/auth/signup  
  회원 가입을 수행합니다.

- POST /api/auth/login  
  로그인 후 JWT Access Token을 쿠키에 저장합니다.

- POST /api/auth/logout  
  쿠키에 저장된 JWT 토큰을 제거하여 로그아웃합니다.

---

#### 사용자

- POST /api/user/files  
  파일을 업로드합니다.  
  서버에서 확장자 정책을 검사하며, 차단된 확장자는 업로드가 거부됩니다.

- GET /api/user/files  
  로그인한 사용자가 업로드한 파일 목록을 페이지 단위로 조회합니다.

- DELETE /api/user/files/{id}  
  로그인한 사용자가 본인이 업로드한 파일을 삭제합니다.

- GET /api/user/files/{id}/download  
  로그인한 사용자가 본인이 업로드한 파일을 다운로드합니다.

---

#### 관리자

- GET /api/admin/extensions/logs  
  확장자 정책 변경 로그를 최신순으로 조회합니다.

- POST /api/admin/extensions/fixed/{id}  
  고정 확장자의 차단 여부를 ON / OFF 합니다.

- POST /api/admin/extensions/custom  
  커스텀 확장자를 추가합니다.  
  추가된 확장자는 항상 차단 대상입니다.

- GET /api/admin/extensions/custom/count  
  현재 등록된 커스텀 확장자 개수를 조회합니다.

- DELETE /api/admin/extensions/custom/{id}  
  등록된 커스텀 확장자를 삭제합니다.

- GET /api/admin/files  
  전체 사용자 업로드 파일을 페이지 단위로 조회합니다.

- GET /api/admin/files/{id}/download  
  관리자 권한으로 파일을 다운로드합니다.

- DELETE /api/admin/files/{id}  
  관리자 권한으로 사용자 파일을 삭제합니다.
---

## 5. 로컬 실행 방법

### 5-1. 사전 준비
- Java 17
- Docker & Docker Compose

---

### 5-2. 환경 변수 설정

프로젝트 루트에 `.env` 파일 생성:

```env
DB_USERNAME=file
DB_PASSWORD=root
DB_ROOT_PASSWORD=root
JWT_SECRET=jwtsecretjwtsecretjwtsecretjwtsecret
UPLOAD_DIR=/data/uploads
```
### 5-3. Docker 실행
프로젝트 루트로 이동 

docker compose up -d --build

- 애플리케이션 접속:
  http://localhost:8080
- Swagger UI:
  http://localhost:8080/swagger-ui.html
### 5-4. 테스트

#### 관리자 계정
- 이메일: admin@admin.com
- 비밀번호: admin123

#### 사용자 계정
- 이메일: user@user.com
- 비밀번호: user123

※ 회원가입 기능 제공  

---
## 6. 배포 환경

- AWS EC2 (Ubuntu 22.04)
- Docker Compose 기반 배포
- 단일 인스턴스 환경에서 실행
