> 팀명 : SALT
  팀원 : 김군순, 김다희, 김훈종, 이대섭, 백지수
  주제 : 딥러닝 기반 수어번역 서비스

> DB 모델링 주소 

  URL : https://aquerytool.com/aquerymain/index/?rurl=cf437a8c-394e-4743-ac52-c50ae7589363&
  Password : xrf0u5


> 데이터베이스 설계순서?

  요구사항분석 > 개념설계 > 논리설계 > 물리설계 > 데이터베이스구현(개.눈.물)

> 말풍선(Verbose, 많이많은) : 상세모드

> int, int unsigned 차이 

  int         ... 32 bit 정수(-2147483648~-1, 0~2147483647)  * 대략 -21억~+21억 
  int unsigned... 32 bit 정수(0~2147483647 + 2147483648)     * 대략     0~+42억 

> VARCHAR(65535)

  이메일        이름         주제              내용 
  VARCHAR(30)   VARCHAR(30)  VARCHAR(2000)    TEXT

* 레코드 하나 속에 있는 VARCHAR의 전체 합의 크기가 65535
* TEXT는 VARCHAR와 저장되는 영역이 다름. 

> 회원(tb_user)  -------------- 게시판(tb_board)

       1                :       0, 1, N(Numerous,여러개의)

  ERD 
  ER-Diagram
  Entity Relationship Diagram 

  Relation     : 일반적인 관계('날씨와 우산판매량의 관계')
  Relations    : 국가적인 관계(relations) between the two countries.
  Relationship : 개인적인 관계 

> 첨부파일의 처리 

  file_folder = "d:/uploads/"
  file_name   = "file001.jpg"
  file_ori_name = "file001+23049512344581324012349.jpg"
  file_thumb_name = thumb_file001+23049512344581324012349.jpg
  file_size   = 1234401234
  file_ext    = jpg 

> 데이터 객체 : VO(Value Object)=DTO(Data Transfer Object)=Java Model=Java Bean=POJO(Plain Old Java Object)

UserVO.java, MemberDTO.java 

// 회원 
public class UserDTO {

    // 회원 이메일 
    private String user_email;

    // 회원 비밀번호 
    private String user_pw;

    // 회원 닉네임 
    private String user_nick;

    // 회원 가입일자 
    private Timestamp joined_at;

    public String getUserEmail() {
        return user_email;
    }

    public void setUserEmail(String userEmail) {
        this.user_email = user_email;
    }

    public String getUserPw() {
        return user_pw;
    }

    public void setUserPw(String userPw) {
        this.user_pw = user_pw;
    }

    public String getUserNick() {
        return user_nick;
    }

    public void setUserNick(String userNick) {
        this.user_nick = user_nick;
    }

    public Timestamp getJoinedAt() {
        return joined_at;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joined_at = joined_at;
    }

    // tb_user 모델 복사
    public void CopyData(tb_user param)
    {
        this.user_email = param.getUserEmail();
        this.user_pw = param.getUserPw();
        this.user_nick = param.getUserNick();
        this.joined_at = param.getJoinedAt();
    }
}

> 비밀번호 암호화 

-- 비밀번호 암호화 : AES / MD5 / SHA / SHA2

SELECT MD5('1234'); # 32글자 81dc9bdb52d04dc20036dbd8313ed055
SELECT SHA('1234'); # 40글자 7110eda4d09e062aa5e4a390b0a572ac0d2c0220
SELECT SHA2('1234', 224); # 56글자 99fb2f48c6af4761f904fc85f95eb56190e5d40b1f44ec3a9c1fa319
SELECT SHA2('1234', 256); # 64글자 03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4
SELECT SHA2('1234', 384); # 96글자 504f008c8fcf8b2ed5dfcde752fc5464ab8ba064215d9c5b5fc486af3d9ab8c81b14785180d2ad7cee1ab792ad44798c
SELECT SHA2('1234', 512); #128글자 d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db

INSERT INTO tb_user (user_email, user_pw, user_nick, joined_at) 
VALUES ('user_email 1', SHA2('user_pw 1', 256), 'user_nick 1', NOW());

SELECT * FROM tb_user ORDER BY joined_at DESC LIMIT 10;

-- 자바에서 insert 처리 
-- INSERT INTO tb_user VALUES(?, SHA2(?, 256), ?, ?,....);

-- 자바에서 login 처리 

SELECT COUNT(*) FROM tb_user 
WHERE user_email = p_user_email AND user_pw = SHA2(p_user_pw, 256);

SELECT COUNT(*) FROM tb_user 
WHERE user_email = 'user_email 1' AND user_pw = 'user_pw 1';
#결과값 : 0 (회원 존재하지 않음)

-- login = SELECT COUNT(*) FROM tb_user 
-- WHERE user_email = 'user_email 1' AND user_pw = SHA2('user_pw 1', 256);
-- if login > 0 then '로그인 성공' else '로그인 실패'


CREATE TABLE tb_board
(
    `bd_idx`       INT UNSIGNED     NOT NULL    AUTO_INCREMENT COMMENT '글 번호', 
    `bd_category`  VARCHAR(30)      NOT NULL    COMMENT '글 카테고리', 
    `bd_title`     VARCHAR(1200)    NOT NULL    COMMENT '글 제목', 
    `bd_content`   TEXT             NOT NULL    COMMENT '글 내용', 
    `bd_file`      VARCHAR(1200)    NOT NULL    COMMENT '글 첨부파일', 
    `bd_views`     INT              NOT NULL    COMMENT '글 조회수', 
    `bd_likes`     INT              NOT NULL    COMMENT '글 좋아요수', 
    `user_email`   VARCHAR(30)      NOT NULL    COMMENT '글 작성자', 
    `created_at`   DATETIME         NOT NULL    COMMENT '글 작성일자', 
     PRIMARY KEY (bd_idx)
) AUTO_INCREMENT=253001;

ALTER TABLE tb_board AUTO_INCREMENT=253001;


SELECT * FROM tb_board;

SELECT UUID_SHORT(); #Universally Unique IDentifier(우주에서 유일한 식별자)
# 100577525570207744
# 100577525570207745


-- 테이블 생성 SQL - tb_board3
CREATE TABLE tb_board3
(
    `bd_idx`       BIGINT UNSIGNED  NOT NULL    DEFAULT (UUID_SHORT()) COMMENT '글 번호', 
    `bd_category`  VARCHAR(30)      NOT NULL    COMMENT '글 카테고리', 
    `bd_title`     VARCHAR(1200)    NOT NULL    COMMENT '글 제목', 
    `bd_content`   TEXT             NOT NULL    COMMENT '글 내용', 
    `bd_file`      VARCHAR(1200)    NOT NULL    COMMENT '글 첨부파일', 
    `bd_views`     INT              NOT NULL    COMMENT '글 조회수', 
    `bd_likes`     INT              NOT NULL    COMMENT '글 좋아요수', 
    `user_email`   VARCHAR(30)      NOT NULL    COMMENT '글 작성자', 
    `created_at`   DATETIME         NOT NULL    COMMENT '글 작성일자', 
     PRIMARY KEY (bd_idx)
);


-- 9223372036854775808(922경)
-- 100577525570207745 (100경

-- 테이블 Comment 설정 SQL - tb_board3
ALTER TABLE tb_board3 COMMENT '게시판';

SELECT * FROM tb_board3;

SELECT UUID();       # 53e7097b-842d-11ee-95fc-0242c0a81002
SELECT UUID_SHORT(); # 100577525570207756

SELECT LENGTH('53e7097b-842d-11ee-95fc-0242c0a81002');



-- 테이블 생성 SQL - tb_board4
CREATE TABLE tb_board4
(
    `bd_idx`       VARCHAR(40)      NOT NULL    DEFAULT (UUID()) COMMENT '글 번호', 
    `bd_category`  VARCHAR(30)      NOT NULL    COMMENT '글 카테고리', 
    `bd_title`     VARCHAR(1200)    NOT NULL    COMMENT '글 제목', 
    `bd_content`   TEXT             NOT NULL    COMMENT '글 내용', 
    `bd_file`      VARCHAR(1200)    NOT NULL    COMMENT '글 첨부파일', 
    `bd_views`     INT              NOT NULL    COMMENT '글 조회수', 
    `bd_likes`     INT              NOT NULL    COMMENT '글 좋아요수', 
    `user_email`   VARCHAR(30)      NOT NULL    COMMENT '글 작성자', 
    `created_at`   DATETIME         NOT NULL    COMMENT '글 작성일자', 
     PRIMARY KEY (bd_idx)
);

INSERT INTO tb_board4 (bd_category, bd_title, bd_content, bd_file, bd_views, bd_likes, user_email, created_at) VALUES ('bd_category 01', 'bd_title 01', 'bd_content 01', 'bd_file 01', 01, 01, 'user_email 01', NOW());
INSERT INTO tb_board4 (bd_category, bd_title, bd_content, bd_file, bd_views, bd_likes, user_email, created_at) VALUES ('bd_category 02', 'bd_title 02', 'bd_content 02', 'bd_file 02', 02, 02, 'user_email 02', NOW());
INSERT INTO tb_board4 (bd_category, bd_title, bd_content, bd_file, bd_views, bd_likes, user_email, created_at) VALUES ('bd_category 03', 'bd_title 03', 'bd_content 03', 'bd_file 03', 03, 03, 'user_email 03', NOW());
INSERT INTO tb_board4 (bd_category, bd_title, bd_content, bd_file, bd_views, bd_likes, user_email, created_at) VALUES ('bd_category 04', 'bd_title 04', 'bd_content 04', 'bd_file 04', 04, 04, 'user_email 04', NOW());
INSERT INTO tb_board4 (bd_category, bd_title, bd_content, bd_file, bd_views, bd_likes, user_email, created_at) VALUES ('bd_category 05', 'bd_title 05', 'bd_content 05', 'bd_file 05', 05, 05, 'user_email 05', NOW());
INSERT INTO tb_board4 (bd_category, bd_title, bd_content, bd_file, bd_views, bd_likes, user_email, created_at) VALUES ('bd_category 06', 'bd_title 06', 'bd_content 06', 'bd_file 06', 06, 06, 'user_email 06', NOW());
INSERT INTO tb_board4 (bd_category, bd_title, bd_content, bd_file, bd_views, bd_likes, user_email, created_at) VALUES ('bd_category 07', 'bd_title 07', 'bd_content 07', 'bd_file 07', 07, 07, 'user_email 07', NOW());
INSERT INTO tb_board4 (bd_category, bd_title, bd_content, bd_file, bd_views, bd_likes, user_email, created_at) VALUES ('bd_category 08', 'bd_title 08', 'bd_content 08', 'bd_file 08', 08, 08, 'user_email 08', NOW());
INSERT INTO tb_board4 (bd_category, bd_title, bd_content, bd_file, bd_views, bd_likes, user_email, created_at) VALUES ('bd_category 09', 'bd_title 09', 'bd_content 09', 'bd_file 09', 09, 09, 'user_email 09', NOW());
INSERT INTO tb_board4 (bd_category, bd_title, bd_content, bd_file, bd_views, bd_likes, user_email, created_at) VALUES ('bd_category 10', 'bd_title 10', 'bd_content 10', 'bd_file 10', 10, 10, 'user_email 10', NOW());

SELECT  * FROM tb_board4;
-- 하이픈 제거
SELECT REPLACE(UUID(),'-','');

-- 대문자로
SELECT UPPER(REPLACE(UUID(),'-',''));

-- 년도까지 포함해서 
SELECT UPPER(REPLACE(UUID(),'-',''));

SELECT YEAR(NOW());

-- 문자열 합치기
-- JAVA : 'A' + 'B'
-- 오라클 : 'A'||'B'
-- MySQL : CONCATENATE -> CONCAT('A','B',.....);
SELECT CONCAT('A','B'); # AB

SELECT CONCAT(YEAR(NOW()),UPPER(REPLACE(UUID(),'-','')));
# 20238975BE6C842E11EE95FC0242C0A81002

SELECT SUBSTRING(YEAR(NOW()), 3);

SELECT CONCAT(SUBSTRING(YEAR(NOW()), 3),UPPER(REPLACE(UUID(),'-','')));
# 20238975BE6C842E11EE95FC0242C0A81002


-------------------------------------------------------------------
(실전프로젝트 MySQL SQL문)

-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- tb_user Table Create SQL
-- 테이블 생성 SQL - tb_user

use godsoon;

CREATE TABLE tb_user
(
    `user_email`  VARCHAR(30)    NOT NULL    COMMENT '회원 이메일', 
    `user_pw`     VARCHAR(30)    NOT NULL    COMMENT '회원 비밀번호', 
    `user_nick`   VARCHAR(20)    NOT NULL    COMMENT '회원 닉네임', 
    `joined_at`   DATETIME       NOT NULL    COMMENT '회원 가입일자', 
     PRIMARY KEY (user_email)
); 

-- 테이블 Comment 설정 SQL - tb_user
ALTER TABLE tb_user COMMENT '회원';


-- tb_board Table Create SQL
-- 테이블 생성 SQL - tb_board
CREATE TABLE tb_board
(
    `bd_idx`       INT UNSIGNED     NOT NULL    AUTO_INCREMENT COMMENT '글 번호', 
    `bd_category`  VARCHAR(30)      NOT NULL    COMMENT '글 카테고리', 
    `bd_title`     VARCHAR(1200)    NOT NULL    COMMENT '글 제목', 
    `bd_content`   TEXT             NOT NULL    COMMENT '글 내용', 
    `bd_file`      VARCHAR(1200)    NOT NULL    COMMENT '글 첨부파일', 
    `bd_views`     INT              NOT NULL    COMMENT '글 조회수', 
    `bd_likes`     INT              NOT NULL    COMMENT '글 좋아요수', 
    `user_email`   VARCHAR(30)      NOT NULL    COMMENT '글 작성자', 
    `created_at`   DATETIME         NOT NULL    COMMENT '글 작성일자', 
     PRIMARY KEY (bd_idx)
);

-- 테이블 Comment 설정 SQL - tb_board
ALTER TABLE tb_board COMMENT '게시판';

-- Foreign Key 설정 SQL - tb_board(user_email) -> tb_user(user_email)
ALTER TABLE tb_board
    ADD CONSTRAINT FK_tb_board_user_email_tb_user_user_email FOREIGN KEY (user_email)
        REFERENCES tb_user (user_email) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - tb_board(user_email)
-- ALTER TABLE tb_board
-- DROP FOREIGN KEY FK_tb_board_user_email_tb_user_user_email;


-- tb_file Table Create SQL
-- 테이블 생성 SQL - tb_file
CREATE TABLE tb_file
(
    `file_idx`         INT UNSIGNED     NOT NULL    AUTO_INCREMENT COMMENT '파일 번호', 
    `bd_idx`           INT UNSIGNED     NOT NULL    COMMENT '글 번호', 
    `file_name`        VARCHAR(1000)    NOT NULL    COMMENT '파일 명', 
    `file_ori_name`    VARCHAR(1000)    NOT NULL    COMMENT '파일 원본명', 
    `file_thumb_name`  VARCHAR(1000)    NOT NULL    COMMENT '파일 썸네일', 
    `file_size`        INT              NOT NULL    COMMENT '파일 사이즈', 
    `file_ext`         VARCHAR(10)      NOT NULL    COMMENT '파일 확장자', 
    `uploaded_at`      DATETIME         NOT NULL    COMMENT '파일 업로드날짜', 
    `file_category`    VARCHAR(30)      NOT NULL    COMMENT '첨부파일 카테고리', 
     PRIMARY KEY (file_idx)
);

-- 테이블 Comment 설정 SQL - tb_file
ALTER TABLE tb_file COMMENT '첨부파일';

-- Foreign Key 설정 SQL - tb_file(bd_idx) -> tb_board(bd_idx)
ALTER TABLE tb_file
    ADD CONSTRAINT FK_tb_file_bd_idx_tb_board_bd_idx FOREIGN KEY (bd_idx)
        REFERENCES tb_board (bd_idx) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - tb_file(bd_idx)
-- ALTER TABLE tb_file
-- DROP FOREIGN KEY FK_tb_file_bd_idx_tb_board_bd_idx;


-- tb_comment Table Create SQL
-- 테이블 생성 SQL - tb_comment
CREATE TABLE tb_comment
(
    `cmt_idx`      INT UNSIGNED    NOT NULL    AUTO_INCREMENT COMMENT '댓글 번호', 
    `bd_idx`       INT UNSIGNED    NOT NULL    COMMENT '원글 번호', 
    `cmt_content`  TEXT            NOT NULL    COMMENT '댓글 내용', 
    `created_at`   DATETIME        NOT NULL    COMMENT '댓글 작성일자', 
    `user_email`   VARCHAR(30)     NOT NULL    COMMENT '댓글 작성자', 
    `cmt_likes`    INT             NOT NULL    COMMENT '댓글 좋아요수', 
     PRIMARY KEY (cmt_idx)
);

-- 테이블 Comment 설정 SQL - tb_comment
ALTER TABLE tb_comment COMMENT '댓글';

-- Foreign Key 설정 SQL - tb_comment(bd_idx) -> tb_board(bd_idx)
ALTER TABLE tb_comment
    ADD CONSTRAINT FK_tb_comment_bd_idx_tb_board_bd_idx FOREIGN KEY (bd_idx)
        REFERENCES tb_board (bd_idx) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - tb_comment(bd_idx)
-- ALTER TABLE tb_comment
-- DROP FOREIGN KEY FK_tb_comment_bd_idx_tb_board_bd_idx;

-- Foreign Key 설정 SQL - tb_comment(user_email) -> tb_user(user_email)
ALTER TABLE tb_comment
    ADD CONSTRAINT FK_tb_comment_user_email_tb_user_user_email FOREIGN KEY (user_email)
        REFERENCES tb_user (user_email) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - tb_comment(user_email)
-- ALTER TABLE tb_comment
-- DROP FOREIGN KEY FK_tb_comment_user_email_tb_user_user_email;


-- tb_likes Table Create SQL
-- 테이블 생성 SQL - tb_likes
CREATE TABLE tb_likes
(
    `like_idx`    INT UNSIGNED    NOT NULL    AUTO_INCREMENT COMMENT '좋아요 번호', 
    `bd_idx`      INT UNSIGNED    NOT NULL    COMMENT '글 번호', 
    `user_email`  VARCHAR(30)     NOT NULL    COMMENT '작성자 아이디', 
    `created_at`  DATETIME        NOT NULL    COMMENT '작성 일자', 
     PRIMARY KEY (like_idx)
);

-- 테이블 Comment 설정 SQL - tb_likes
ALTER TABLE tb_likes COMMENT '게시판 좋아요';

-- Foreign Key 설정 SQL - tb_likes(bd_idx) -> tb_board(bd_idx)
ALTER TABLE tb_likes
    ADD CONSTRAINT FK_tb_likes_bd_idx_tb_board_bd_idx FOREIGN KEY (bd_idx)
        REFERENCES tb_board (bd_idx) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - tb_likes(bd_idx)
-- ALTER TABLE tb_likes
-- DROP FOREIGN KEY FK_tb_likes_bd_idx_tb_board_bd_idx;

-- Foreign Key 설정 SQL - tb_likes(user_email) -> tb_user(user_email)
ALTER TABLE tb_likes
    ADD CONSTRAINT FK_tb_likes_user_email_tb_user_user_email FOREIGN KEY (user_email)
        REFERENCES tb_user (user_email) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Foreign Key 삭제 SQL - tb_likes(user_email)
-- ALTER TABLE tb_likes
-- DROP FOREIGN KEY FK_tb_likes_user_email_tb_user_user_email;


-- tb_signlang Table Create SQL
-- 테이블 생성 SQL - tb_signlang
CREATE TABLE tb_signlang
(
    `slang_idx`       INT UNSIGNED      NOT NULL    AUTO_INCREMENT COMMENT '수어 번호', 
    `slang_category`  VARCHAR(30)       NOT NULL    COMMENT '수어 카테고리', 
    `slang_text`      TEXT              NOT NULL    COMMENT '수어 텍스트', 
    `slang_video`     VARCHAR(1200 )    NOT NULL    COMMENT '수어 영상', 
     PRIMARY KEY (slang_idx)
);

-- 테이블 Comment 설정 SQL - tb_signlang
ALTER TABLE tb_signlang COMMENT '통합수어';




