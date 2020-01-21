
 DROP TABLE IF EXISTS message_info;

 CREATE TABLE IF NOT EXISTS message_info(id varchar(50) unsigned not null primary key,content varchar(10),create_time DATE);

 INSERT INTO message_info(id, content, create_time) VALUES ('1', 'tom：哈哈哈', parsedatetime('17-09-2012 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'))
