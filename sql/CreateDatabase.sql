-- NOTE: Run this script with root/system admin or database administrator account
CREATE DATABASE DiemDanh;
USE DiemDanh;

-- DROP TABLE mon_hoc;
CREATE TABLE mon_hoc (
    ma_mh CHAR(15) NOT NULL PRIMARY KEY,
    ten_mh VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
);

-- DROP TABLE sinh_vien;
CREATE TABLE sinh_vien (
    mssv MEDIUMINT UNSIGNED NOT NULL,
	ten_sv VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL DEFAULT '',
    PRIMARY KEY (mssv)
);

-- DROP TABLE thoi_khoa_bieu;
CREATE TABLE thoi_khoa_bieu (
    id INT NOT NULL AUTO_INCREMENT,
    ngay_bd DATE NOT NULL,
    ngay_kt DATE NOT NULL,
    thu TINYINT UNSIGNED NOT NULL,
    gio_bd TIME NOT NULL,
    gio_kt TIME NOT NULL,
    ten_phong_hoc CHAR(10) NOT NULL,
    ma_mh CHAR(15) NOT NULL,
    PRIMARY KEY (id),
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

CREATE USER 'sinhvien'@'%' IDENTIFIED BY 'sinhvien';
GRANT SELECT ON DiemDanh.* TO 'sinhvien'@'%';
GRANT UPDATE ON DiemDanh.diem_danh_sv TO 'sinhvien'@'%';
CREATE USER 'giaovu'@'%' IDENTIFIED BY 'giaovu';
GRANT ALL PRIVILEGES ON DiemDanh.* TO 'giaovu'@'%';

CREATE TRIGGER after_insert_sv_to_mh
AFTER INSERT ON danh_sach_sv_mh
FOR EACH ROW
BEGIN
	INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 1, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 2, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 3, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 4, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 5, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 6, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 7, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 8, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 9, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 10, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 11, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 12, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 13, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 14, 0);
    INSERT INTO diem_danh_sv VALUES (NEW.ma_mh, NEW.mssv, 15, 0);
END;

CREATE TRIGGER after_insert_new_sv
BEFORE INSERT ON sinh_vien
FOR EACH ROW SET NEW.password = NEW.mssv;