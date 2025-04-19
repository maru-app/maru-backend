alter table diaries alter column title type VARCHAR(256) using title::VARCHAR(256);
alter table diaries add emoji VARCHAR(50) default '😊' not null;
