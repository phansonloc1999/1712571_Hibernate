-- CREATE DATABASE DiemDanh;
USE DiemDanh;
-- CREATE USER 'diemdanhuser'@'%' IDENTIFIED BY '24061999';
-- GRANT ALL ON DiemDanh.* TO 'diemdanhuser'@'%';

-- DROP DATABASE DiemDanh;
-- DROP USER 'diemdanhuser'@'%';

-- USE DiemDanh;
-- DROP TABLE mon_hoc;
-- CREATE TABLE mon_hoc (
--     ma_mh CHAR(15) NOT NULL PRIMARY KEY,
--     ten_mh CHAR(30) NOT NULL
-- );

-- DROP TABLE sinh_vien;
-- CREATE TABLE sinh_vien (
--     mssv MEDIUMINT UNSIGNED NOT NULL,
-- 	ten_sv CHAR(30) NOT NULL,
--     PRIMARY KEY (mssv)
-- );

-- DROP TABLE thoi_khoa_bieu;
-- CREATE TABLE thoi_khoa_bieu (
-- 	mssv MEDIUMINT UNSIGNED NOT NULL,
--     ngay_bd DATE NOT NULL,
--     ngay_kt DATE NOT NULL,
--     thu TINYINT UNSIGNED NOT NULL,
--     gio_bd TIME NOT NULL,
--     gio_kt TIME NOT NULL,
--     ten_phong_hoc CHAR(10) NOT NULL,
--     ma_mh CHAR(15) NOT NULL,
--     PRIMARY KEY (mssv, ma_mh),
--     FOREIGN KEY (mssv) REFERENCES sinh_vien(mssv),
--     FOREIGN KEY (ma_mh) REFERENCES mon_hoc(ma_mh)
-- );

-- CREATE TABLE danh_sach_sv_mh (
-- 	ma_mh CHAR(15) NOT NULL,
--     mssv MEDIUMINT UNSIGNED NOT NULL,
--     PRIMARY KEY (ma_mh, mssv),
--     FOREIGN KEY (ma_mh) REFERENCES mon_hoc(ma_mh),
--     FOREIGN KEY (mssv) REFERENCES sinh_vien(mssv)
-- );

-- CREATE TABLE diem_danh_sv (
-- 	ma_mh CHAR(15) NOT NULL,
--     mssv MEDIUMINT UNSIGNED NOT NULL,
--     tuan TINYINT UNSIGNED NOT NULL,
--     co_mat BOOL NOT NULL,
--     PRIMARY KEY (ma_mh, mssv, tuan),
--     FOREIGN KEY (ma_mh) REFERENCES mon_hoc(ma_mh),
--     FOREIGN KEY (mssv) REFERENCES sinh_vien(mssv)
-- );