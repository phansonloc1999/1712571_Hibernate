-- DROP DATABASE DiemDanh;
-- DROP USER 'diemdanhuser'@'%';

CREATE DATABASE DiemDanh;
-- CREATE USER 'diemdanhuser'@'%' IDENTIFIED BY '24061999';
-- GRANT ALL ON DiemDanh.* TO 'diemdanhuser'@'%';

-- DROP DATABASE DiemDanh;

USE DiemDanh;

-- DROP TABLE mon_hoc IF EXISTS;
CREATE TABLE mon_hoc (
    ma_mh CHAR(15) NOT NULL PRIMARY KEY,
    ten_mh VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
);

-- DROP TABLE sinh_vien;
CREATE TABLE sinh_vien (
    mssv MEDIUMINT UNSIGNED NOT NULL,
	ten_sv VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL, 
    PRIMARY KEY (mssv)
);

-- DROP TABLE thoi_khoa_bieu;
CREATE TABLE thoi_khoa_bieu (
	mssv MEDIUMINT UNSIGNED NOT NULL,
    ngay_bd DATE NOT NULL,
    ngay_kt DATE NOT NULL,
    thu TINYINT UNSIGNED NOT NULL,
    gio_bd TIME NOT NULL,
    gio_kt TIME NOT NULL,
    ten_phong_hoc CHAR(10) NOT NULL,
    ma_mh CHAR(15) NOT NULL,
    PRIMARY KEY (mssv, ma_mh),
    FOREIGN KEY (mssv) REFERENCES sinh_vien(mssv),
    FOREIGN KEY (ma_mh) REFERENCES mon_hoc(ma_mh)
);

-- DROP TABLE danh_sach_sv_mh;
CREATE TABLE danh_sach_sv_mh (
	ma_mh CHAR(15) NOT NULL,
    mssv MEDIUMINT UNSIGNED NOT NULL,
    PRIMARY KEY (ma_mh, mssv),
    FOREIGN KEY (ma_mh) REFERENCES mon_hoc(ma_mh),
    FOREIGN KEY (mssv) REFERENCES sinh_vien(mssv)
);

-- DROP TABLE diem_danh_sv;
CREATE TABLE diem_danh_sv (
	ma_mh CHAR(15) NOT NULL,
    mssv MEDIUMINT UNSIGNED NOT NULL,
    tuan TINYINT UNSIGNED NOT NULL,
    co_mat BOOL NOT NULL,
    PRIMARY KEY (ma_mh, mssv, tuan),
    FOREIGN KEY (ma_mh) REFERENCES mon_hoc(ma_mh),
    FOREIGN KEY (mssv) REFERENCES sinh_vien(mssv)
);

-- DELETE FROM mon_hoc;
INSERT INTO mon_hoc VALUES ('CSC001', 'Lập trình cơ bản');
INSERT INTO mon_hoc VALUES ('CSC002', 'Lập trình hướng đối tượng');

INSERT INTO sinh_vien VALUES (1712571, 'Phan Sơn Lộc');
INSERT INTO sinh_vien VALUES (1712572, 'Phan Sơn Phúc');

INSERT INTO thoi_khoa_bieu VALUES (1712571, CURRENT_DATE(), '2022-4-14', 2, CURRENT_TIME(), CURRENT_TIME(), 'E101', 'CSC002');
INSERT INTO thoi_khoa_bieu VALUES (1712572, CURRENT_DATE(), '2022-4-14', 2, CURRENT_TIME(), CURRENT_TIME(), 'E101', 'CSC002');

INSERT INTO danh_sach_sv_mh VALUES ('CSC002', 1712571);
INSERT INTO danh_sach_sv_mh VALUES ('CSC002', 1712572);

INSERT INTO diem_danh_sv (ma_mh, mssv, tuan, co_mat )
VALUES 
    ('CSC002', 1712571, 1, 0),
    ('CSC002', 1712571, 2, 0),
    ('CSC002', 1712571, 3, 0),
    ('CSC002', 1712571, 4, 0),
    ('CSC002', 1712571, 5, 0),
    ('CSC002', 1712571, 6, 0),
    ('CSC002', 1712571, 7, 0),
    ('CSC002', 1712571, 8, 0),
    ('CSC002', 1712571, 9, 0),
    ('CSC002', 1712571, 10, 0),
    ('CSC002', 1712571, 11, 0),
    ('CSC002', 1712571, 12, 0),
    ('CSC002', 1712571, 13, 0),
    ('CSC002', 1712571, 14, 0),
    ('CSC002', 1712571, 15, 0);
