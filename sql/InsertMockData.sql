-- NOTE: Run this script with root/system admin or database administrator account
USE DiemDanh;

INSERT INTO mon_hoc VALUES ('CSC001', 'Lập trình cơ bản');
INSERT INTO mon_hoc VALUES ('CSC002', 'Lập trình hướng đối tượng');

INSERT INTO sinh_vien VALUES (1712571, 'Phan Sơn Lộc', '1712571', '1712571');
INSERT INTO sinh_vien VALUES (1712572, 'Phan Sơn Phúc', '1712572', '1712572');

-- INSERT INTO thoi_khoa_bieu VALUES (1, CURRENT_DATE(), '2022-4-14', 2, CURRENT_TIME(), CURRENT_TIME(), 'E101', 'CSC002');
-- INSERT INTO thoi_khoa_bieu VALUES (2, CURRENT_DATE(), '2022-4-14', 2, CURRENT_TIME(), CURRENT_TIME(), 'E101', 'CSC002');

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
