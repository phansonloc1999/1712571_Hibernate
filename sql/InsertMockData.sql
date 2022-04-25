-- NOTE: Run this script with root/system admin or database administrator account
USE DiemDanh;

INSERT INTO mon_hoc VALUES ('CSC001', 'Lập trình cơ bản');
INSERT INTO mon_hoc VALUES ('CSC002', 'Lập trình hướng đối tượng');

INSERT INTO sinh_vien VALUES (1712571, 'Phan Sơn Lộc', '1712571', SHA1('1712571'), 1);
INSERT INTO sinh_vien VALUES (1712572, 'Phan Sơn Phúc', '1712572', SHA1('1712572'), 1);
INSERT INTO sinh_vien VALUES (1712573, 'Phan Sơn Thọ', '1712573', SHA1('1712573'), 1);

INSERT INTO thoi_khoa_bieu VALUES (1, '2022-04-14', '2022-12-31', 2, '00:00:01', '23:59:59', 'E101', 'CSC001');
INSERT INTO thoi_khoa_bieu VALUES (2, '2022-04-14', '2022-12-31', 3, '00:00:01', '23:59:59', 'E102', 'CSC002');

INSERT INTO danh_sach_sv_mh VALUES ('CSC001', 1712571);
INSERT INTO danh_sach_sv_mh VALUES ('CSC002', 1712571);
INSERT INTO danh_sach_sv_mh VALUES ('CSC002', 1712572);