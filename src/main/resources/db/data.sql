insert into user_tb(username, password, email) values('ssar', '$2a$10$QhizfX4/c7DCLmu1RCY0sOnAHRAeWzk3u5JAeJGV/dZi5zQ/fve5S', 'ssar@nate.com');
insert into user_tb(username, password, email) values('cos', '$2a$10$QhizfX4/c7DCLmu1RCY0sOnAHRAeWzk3u5JAeJGV/dZi5zQ/fve5S', 'cos@nate.com');
insert into board_tb(title, content, user_id, created_at) values('제목2', '내용1', 1, now());
insert into board_tb(title, content, user_id, created_at) values('2', '내용2', 1, now());
insert into board_tb(title, content, user_id, created_at) values('22', '내용3', 1, now());
insert into board_tb(title, content, user_id, created_at) values('222', '내용4', 2, now());
insert into board_tb(title, content, user_id, created_at) values('제목5', '내용5', 2, now());
insert into reply_tb(comment, board_id, user_id) values('댓글1', 4, 1);
insert into reply_tb(comment, board_id, user_id) values('댓글2', 4, 2);
insert into reply_tb(comment, board_id, user_id) values('댓글1', 5, 1);
insert into reply_tb(comment, board_id, user_id) values('댓글2', 5, 2);