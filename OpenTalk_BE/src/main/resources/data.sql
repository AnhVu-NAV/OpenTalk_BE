INSERT INTO "company_branch" (id, name) VALUES (1, 'Head Office');
INSERT INTO "role" (id, name) VALUES (2, 'User');

INSERT INTO company_branch (created_at, updated_at, name)
VALUES
    ('2025-07-14 20:51:00', '2025-07-14 20:51:00', 'Chi nhánh Hà Nội'),
    ('2025-07-14 20:51:00', '2025-07-14 20:51:00', 'Chi nhánh Thanh Hóa'),
    ('2025-07-14 20:51:00', '2025-07-14 20:51:00', 'Chi nhánh Đà Nẵng'),
    ('2025-07-14 20:51:00', '2025-07-14 20:51:00', 'Chi nhánh TP. HCM');

INSERT INTO opentalk_meeting
(created_at, updated_at, meeting_link,      meeting_name,        scheduled_date, status,  company_branch_id, topic_id)
VALUES
    ('2025-07-14 21:12:43','2025-07-14 21:12:43','https://zoom.us/j/100000001','Opentalk Meeting 1','2025-07-20','pending',1, NULL),
    ('2025-07-14 21:12:43','2025-07-14 21:12:43','https://zoom.us/j/100000002','Opentalk Meeting 2','2025-07-21','pending',2, NULL),
    ('2025-07-14 21:12:43','2025-07-14 21:12:43','https://zoom.us/j/100000003','Opentalk Meeting 3','2025-07-22','pending',3, NULL),
    ('2025-07-14 21:12:43','2025-07-14 21:12:43','https://zoom.us/j/100000004','Opentalk Meeting 4','2025-07-23','pending',1, NULL),
    ('2025-07-14 21:12:43','2025-07-14 21:12:43','https://zoom.us/j/100000005','Opentalk Meeting 5','2025-07-24','pending',1, NULL),
    ('2025-07-14 21:12:43','2025-07-14 21:12:43','https://zoom.us/j/100000006','Opentalk Meeting 6','2025-07-25','pending',2, NULL),
    ('2025-07-14 21:12:43','2025-07-14 21:12:43','https://zoom.us/j/100000007','Opentalk Meeting 7','2025-07-26','pending',3, NULL),
    ('2025-07-14 21:12:43','2025-07-14 21:12:43','https://zoom.us/j/100000008','Opentalk Meeting 8','2025-07-27','pending',1, NULL),
    ('2025-07-14 21:12:43','2025-07-14 21:12:43','https://zoom.us/j/100000009','Opentalk Meeting 9','2025-07-28','pending',2, NULL),
    ('2025-07-14 21:12:43','2025-07-14 21:12:43','https://zoom.us/j/100000010','Opentalk Meeting 10','2025-07-29','pending',2, NULL);
