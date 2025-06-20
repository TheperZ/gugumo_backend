# 구구모

<p align="center">
  <img src="https://github.com/gugumo-service/gugumo_frontend/assets/96280450/a25dc9db-e28d-4310-b252-21f14b9147b9">
</p>

> **개발기간** 2024.04 ~

<br/>

## 서비스 주소

https://gugumo.vercel.app/

<br />

## 프로젝트 소개

배드민턴, 농구, 풋살, 테니스, 탁구, 야구 등 여러 가지 구기종목들을 좋아하거나 취미로 활동하시는데 같이 하실 분이 없거나 구하기 힘드신 분들을 위해서 만든 구기종목 모집 매칭 서비스입니다. 해당 매칭을 이용해서
근처 지역들에 사시는 분들끼리의 담소를 권유해 취미활동을 공유하도록 도와주고 있습니다.

<br />

## 기술 스택

<p><strong>Language</strong></p>
<div>
    <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white">
</div>
<p><strong>Environment</strong></p>
<div>
    <img src="https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=intellijidea&logoColor=white">
    <img src="https://img.shields.io/badge/git-F05033.svg?style=for-the-badge&logo=git&logoColor=white" />&nbsp
    <img src="https://img.shields.io/badge/github-181717.svg?style=for-the-badge&logo=github&logoColor=white" />&nbsp
</div>
<p><strong>Database</strong></p>
<div>
    <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=PostgreSQL&logoColor=white"> 
    <img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=Redis&logoColor=white"> 
</div>
<p><strong>Framework</strong></p>
<div>
    <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"> 
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
    <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> 
    <img src="https://img.shields.io/badge/QueryDSL-4479A1?style=for-the-badge">
    <img src="https://img.shields.io/badge/Spring Security-4479A1?style=for-the-badge&logo=springsecurity&logoColor=white">

</div>
<p><strong>Test</strong></p>
<div>
  <img src="https://img.shields.io/badge/Postman-FF6C37.svg?style=for-the-badge&logo=Postman&logoColor=white">
</div>
<p><strong>Communication</strong></p>
<div>
    <img src="https://img.shields.io/badge/Notion-F3F3F3.svg?style=for-the-badge&logo=notion&logoColor=black">
    <img src="https://img.shields.io/badge/Discord-5865F2.svg?style=for-the-badge&logo=Discord&logoColor=white">  
</div>
<p><strong>CI/CD</strong></p>
<div>
      <img src="https://img.shields.io/badge/Amazon EC2-FF9900.svg?style=for-the-badge&logo=amazonec2&logoColor=white">
      <img src="https://img.shields.io/badge/Amazon S3-569A31.svg?style=for-the-badge&logo=amazons3&logoColor=white">
      <img src="https://img.shields.io/badge/GitHub Actions-2088FF.svg?style=for-the-badge&logo=githubactions&logoColor=white">
</div>
<br />

## 주요 기능

### ⭐ 간단한 매칭시스템

- 단기 모집, 장기 모집을 선택해서 원하는 모집으로 게시글을 작성할 수 있습니다
- 작성한글과 태그를 이용하여 원하는 구기종목의 게시글을 조회할 수 있습니다.
- 오픈카톡을 이용해서 원하는 사람들과의 매칭을 이루게 해줍니다.

### ⭐ 작성한글 조회

- 작성했던 글들을 조회할 수 있습니다.

### ⭐ 북마크 등록

- 원하는 게시글을 북마크를 해서 저장할 수 있습니다.

### ⭐ JWT을 이용한 토큰방식 로그인

- JWT을 이용한 토큰방식으로 로그인을 구현 하였습니다.

<br />

## 아키텍처

<img src="https://drive.google.com/uc?export=download&id=14J7BaWmlvBb0JUTBKTV-snkh5eTRlAcU">

<br />

## 화면 구성

|                                                      메인페이지                                                       |                                                      북마크페이지                                                      |                                                       작성한글                                                       |
|:----------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------:|
| ![image](https://github.com/gugumo-service/gugumo_frontend/assets/96280450/58eaf5be-547f-4215-b3c5-19013d8b0c2f) | ![image](https://github.com/gugumo-service/gugumo_frontend/assets/96280450/b1651a82-619e-42d9-b812-109f63d97f96) | ![image](https://github.com/gugumo-service/gugumo_frontend/assets/96280450/2d1a59d6-edec-49ee-b629-93a139cbb90b) |
|                                                       로그인                                                        |                                                       회원가입                                                       |                                                      마이페이지                                                       |
| ![image](https://github.com/gugumo-service/gugumo_frontend/assets/96280450/b3b29943-bdd9-4e5e-b4c4-a3d091ffea24) | ![image](https://github.com/gugumo-service/gugumo_frontend/assets/96280450/3a6b143c-fd57-4d4c-aca3-733132f49788) | ![image](https://github.com/gugumo-service/gugumo_frontend/assets/96280450/65cf7af3-39c0-4686-bf4f-b1614ead2a52) |
|                                                    작성페이지 단기모집                                                    |                                                    작성페이지 장기모집                                                    |                                                      상세페이지                                                       |
| ![image](https://github.com/gugumo-service/gugumo_frontend/assets/96280450/d5c1c430-b7b2-46e5-a37f-0008b494338f) | ![image](https://github.com/gugumo-service/gugumo_frontend/assets/96280450/fef819c3-f418-4af7-9dcd-94600f4c1559) | ![image](https://github.com/gugumo-service/gugumo_frontend/assets/96280450/0621da6f-0fe7-4ba3-9706-80cf423fb1d1) |

<br />

## 웹개발팀

<table>
  <tr>
    <th style="width: 200px; text-align : center;">김지유</th>
    <th style="width: 200px; text-align : center;">박희성</th>
    <th style="width: 200px; text-align : center;">김창호</th>
  </tr>
  <tr style="border-bottom: 1px solid white;">
    <td>
        <img src="https://github.com/gugumo-service/gugumo_frontend/assets/96280450/d6716133-cc01-451c-af07-0da997725785">
    </td>
    <td style="border-left: 1px solid white;">
        <img src="https://github.com/gugumo-service/gugumo_frontend/assets/96280450/6c18d80c-5aed-48ec-90ad-f847437e83a0">
    </td>
    <td style="border-left: 1px solid white;">
        <img src="https://github.com/gugumo-service/gugumo_frontend/assets/96280450/412dbdcb-8dad-4bc9-8ae4-bc28fba73f6c">
    </td>
  </tr>
  <tr style="border-bottom: 1px solid white; text-align : center;">
    <td>FE</td>
    <td style="border-left: 1px solid white;">BE</td>
    <td style="border-left: 1px solid white;">BE</td>
  </tr>
</table>

<br />

## 디자인팀

<table>
  <tr>
    <th style="width: 200px; text-align: center">정하은</th>
  </tr>
  <tr style="border-bottom: 1px solid white;">
    <td>
        <img src="https://github.com/gugumo-service/gugumo_frontend/assets/96280450/757d814c-6dd6-4546-b950-3d9505b4e1b7">
    </td>
  </tr>
  <tr style="border-bottom: 1px solid white; text-align : center;">
    <td>
        디자이너
    </td>
  </tr>
</table>
