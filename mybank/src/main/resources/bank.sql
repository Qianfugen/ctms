--账号表
CREATE TABLE T_ACCOUNT
(
    ACC_NO      varchar2(25) not null primary key, --账号
    CURR_TYPE   varchar2(10) not null,             --固定值为“人民币”
    ACC_BANKID  varchar2(10) not null,             --银行代码
    USER_ID     varchar2(20) NOT NULL,             --客户号
    ACC_STATUS  NUMBER(1)    NOT NULL,             -- 0-冻结，1-可用
    ACC_LIMIT   decimal(14, 2),                    --账号交易限额，个人账户20000，企业账户1000000
    ACC_FUND    decimal(14, 2),                    --账号资金 0-100000000
    ACC_TYPE    varchar2(10),                      --卡类型，个人，企业
    ACC_DATE    date,                              --开卡时间
    ACC_PWD     varchar2(50),--支付密码
    COLL_STATUS varchar2(30),
    reserved2   varchar2(30)--预留字段
);
insert into T_ACCOUNT(acc_no, curr_type, acc_bankid, user_id, acc_status, acc_limit, acc_fund, acc_type, acc_date,
                      acc_pwd, coll_status)
values ('6222303626811324642', 'CNY', '622230', '33242606355194662036', 1, 20000.00, 3000.00, '个人',
        to_date('1995-11-02', 'yyyy-mm-dd'), '5CB40BE463F94DD6A4346BDF87062D4E', '未签约');
insert into T_ACCOUNT(acc_no, curr_type, acc_bankid, user_id, acc_status, acc_limit, acc_fund, acc_type, acc_date,
                      acc_pwd, coll_status)
values ('6222304497903198673', 'CNY', '622230', '49597884308271840241', 1, 20000.00, 3000.00, '个人',
        to_date('1996-11-02', 'yyyy-mm-dd'), '5A8D38EFE60A4467904998D0B2A7ED34', '已签约');
--管理员
CREATE TABLE T_ADMIN
(
    NAME     VARCHAR2(20),
    PASSWORD VARCHAR2(16)
);
insert into T_ADMIN(name, password)
values ('君柒', '123456');

--银行表
CREATE TABLE T_BANK
(
    BANKID    VARCHAR2(30) NOT NULL PRIMARY KEY, --银行代码
    BANK_NAME VARCHAR2(40) NOT NULL--银行名称
);
insert into T_BANK(bankid, bank_name)
values ('622230', '工商银行');
insert into T_BANK(bankid, bank_name)
values ('622848', '农业银行');
insert into T_BANK(bankid, bank_name)
values ('CH040026', '瑞士银行');

--归集表
CREATE TABLE T_COLL
(
    COLL_ID     varchar2(32) not null PRIMARY KEY,--签约单号
    MAIN_ACC    varchar2(25) not null,--主账号
    FOLLOW_ACC  varchar2(25),                     --子账号
    SIGN_DATE   date,--签约时间
    SIGN_FUND   decimal(14, 2),--签约金额
    KIND        varchar2(30),
    MAIN_USER   VARCHAR2(20),
    MAIN_BANK   VARCHAR2(20),
    FOLLOW_USER VARCHAR2(20),
    FOLLOW_BANK VARCHAR2(20),
    reserved2   varchar2(30)--预留字段
);

INSERT INTO T_COLL(COLL_ID, MAIN_ACC, FOLLOW_ACC, SIGN_DATE, SIGN_FUND, MAIN_USER, MAIN_BANK, FOLLOW_USER, FOLLOW_BANK)
VALUES ('6516', '6222307120415995107', '6222302919195600644', TO_DATE('2019-12-13 19:05:10', 'YYYY-MM-DD HH24:MI:SS'),
        5000.00, '逯茗泉', '工商银行', '蒲淳', '工商银行');

--任务表
create table T_JOB
(
    ID         NUMBER(6) PRIMARY KEY,
    ACC_IN     VARCHAR2(30),
    ACC_OUT    VARCHAR2(30),
    TRANS_FUND DECIMAL(16, 2),
    CRON       VARCHAR2(50),
    CURRENCY   VARCHAR2(3)
);

INSERT INTO T_JOB (ID, ACC_IN, ACC_OUT, TRANS_FUND, CRON, CURRENCY)
VALUES ('1', '6222308239911999317', '6222305891736516103', '1.00', '43 01 10 14 12 ? 2019', 'CNY');

--登录记录表
CREATE TABLE T_LOGIN
(
    ACC_NO       VARCHAR2(25),
    LOGIN_ADD    VARCHAR2(40),
    LOGIN_IP     VARCHAR2(20),
    LOGIN_STATUS VARCHAR2(10),
    LOGIN_COUNT  NUMBER(3),
    LOGIN_ERROR  NUMBER(3),
    LOGIN_ODD    VARCHAR2(50),
    HOSTNAME     VARCHAR2(30),
    LOGIN_TIME   DATE
);
INSERT INTO T_LOGIN(ACC_NO, LOGIN_ADD, LOGIN_IP, LOGIN_STATUS, LOGIN_COUNT, LOGIN_ERROR, LOGIN_ODD, HOSTNAME,
                    LOGIN_TIME)
VALUES ('6222308834408716123', '广东省深圳市', '10.1.13.149', '成功', 1, 0, 'NOTHING', '张三',
        to_date('2019-12-14 12:00:00', 'yyyy-mm-dd hh24:mi:ss'));

--异常表
CREATE TABLE T_ODD
(
    ODT_ID    number(10)   not null PRIMARY KEY,--序号
    ODT_TYPE  varchar2(20) not null,--异常类型		0 登录异常，1 转账异常
    ODT_CAUSE varchar2(20) not null,--异常原因		0 频繁登录，1 异地登录，2 转账超时，3 转账失败
    reserved1 varchar2(30),
    reserved2 varchar2(30)--预留字段
);
insert into T_ODD(odd_id, odd_type, odd_cause)
values (1081, '登录异常', '异地登录');

--收款表
CREATE TABLE T_PAYEE
(
    CREDITOR_ACC  varchar2(25) not null, --贷方账号
    DEBTOR        varchar2(25) Not null,--借方账号
    FUND          decimal(14, 2),--借贷金额
    CREDITOR_NAME varchar2(30),
    DEBTOR_NAME   varchar2(30)--预留字段
);

insert into T_PAYEE(creditor_acc, debtor, fund, creditor_name, debtor_name)
VALUES ('6222305114281967460', '6222305308023348173', 1241097.00, '', '');

--催款通知
CREATE TABLE T_PAYINFO
(
    CREDITOR_ACC  varchar2(25) not null,--贷方账号
    CREDITOR_NAME varchar2(40) not null,--贷方用户名
    FUND          decimal(14, 2),--支付金额
    iNFO_TIME     date,--催款时间
    DEBTOR        varchar2(30),
    DEBTOR_NAME   varchar2(30)--预留字段
);
insert into T_PAYINFO(creditor_acc, creditor_name, fund, info_time, debtor, debtor_name)
values ('6222302755507988922', '邬洋卿', 283383.00, to_date('2019-08-08', 'yyyy-mm-dd'), '', '');

--系统通知表
CREATE TABLE T_SYSINFO
(
    ACC_IN     varchar2(25) not null,--转入账号
    ACC_INNAME varchar2(40) not null,--转入用户名
    TRANS_FUND decimal(14, 2),--支付金额
    DEAL_DATE  date,--到账时间
    reserved1  varchar2(30),
    reserved2  varchar2(30)--预留字段
);

--转账交易表
CREATE TABLE T_TRANSFER
(
    DEAL_NO      varchar2(32) not null PRIMARY KEY,--交易流水号，sysguid()
    DEAL_DATE    date         not null,--交易日期
    TRANS_TYPE   number(1)    not null,--	0 同行转账  1 跨行转账  2 跨境转账
    TRANS_STATUS varchar2(10) not null,--0处理中 1成功  2失败
    ACC_OUT      varchar2(25) not null,--转出账号
    ACC_OUTNAME  varchar2(40),--转出账号用户名
    ACC_OUTBANK  varchar2(40),--转出行
    ACC_IN       varchar2(25) not null,--转入账号
    ACC_INNAME   varchar2(40),--转入账号用户名
    ACC_INBANK   varchar2(50),--转入行
    CURRENCY     varchar2(10),--币种
    TRANS_FUND   decimal(14, 2),--转账金额
    kind         varchar2(30),
    reserved2    varchar2(30)--预留字段
);

--用户表
CREATE TABLE T_USER
(
    USER_ID        varchar2(20) not null PRIMARY KEY, --客户号
    USER_NAME      varchar2(40),                      --用户姓名
    USER_SEX       number(1),                         --1 男  2 女
    USER_BIRTHDATE date,                              --生日
    USER_CERTNO    varchar2(30) not null,             --身份证号码
    USER_ADDR      varchar2(80),                      --住址
    USER_ZIPCODE   varchar2(6),                       --邮政编码
    USER_TELNO     varchar2(20),                      --固话
    USER_PHONENO   varchar2(20),                      --手机号码
    USER_EMAIL     varchar2(60),                      --邮箱
    USER_PWD       VARCHAR2(50),--登录密码
    reserved1      varchar2(30),
    reserved2      varchar2(30)--预留字段
);

--归集异常表
CREATE TABLE T_USUALCOLL
(
    MAIN_ACC   VARCHAR2(25),
    ACC_IN     VARCHAR2(25),
    ACC_INNAME VARCHAR2(40),
    TRANS_FUND DECIMAL(14, 2)
);

--创建视图
create view custom as
select u.user_id,
       u.user_name,
       u.user_sex,
       u.user_birthdate,
       u.user_certno,
       u.user_addr,
       u.user_zipcode,
       u.user_telno,
       u.user_phoneno,
       u.user_email,
       u.user_pwd,
       a.acc_no,
       a.curr_type,
       a.acc_bankid,
       a.acc_status,
       a.acc_limit,
       a.acc_fund,
       a.acc_type,
       a.acc_date,
       a.acc_pwd,
       a.COLL_STATUS
from T_ACCOUNT a,
     T_USER u
where a.USER_ID = u.USER_ID;


--存储过程
create or replace procedure add_data is
    v_c          number(8)      := 1;--循环次数

    --用户表变量
    v_USER_ID    varchar2(20);--客户号
    v_name       varchar2(80);--客户户姓名
    v_sex        number(1)      := 0;--性别
    v_date       varchar2(10);--日期字符串
    v_telno      varchar2(20)   := '86-10-';--固话区号

    --账号表变量
    v_ACC_NO     varchar2(25);--银行卡号前六位
    v_ACC_STATUS number(1)      := 1;--账号状态，冻结或者可用
    v_ACC_LIMIT  decimal(14, 2) := 20000;--账号交易限额
    v_ACC_TYPE   varchar2(10)   := '个人';--卡类型,个人,企业

    --归集表变量
    v_MAIN_ACC   varchar2(25);--主账号
    v_FOLLOW_ACC varchar2(25);--子账号

    --异常表变量
    v_ODT_TYPE   varchar2(20);
    v_ODT_CAUSE  varchar2(20);

begin
    --循环插入10000万条数据
    while v_c <= 10000
        loop
        --用户表变量处理
        --客户号变量处理
            v_USER_ID := round(dbms_random.value(1000000000000000000, 99999999999999999999));
            --用户姓名变量处理
            --姓氏
            v_name := substr(
                    '赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊于惠甄曲家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘景詹束龙叶幸司韶郜黎蓟溥印宿白怀蒲邰从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍却璩桑桂濮牛寿通边扈燕冀浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庾终暨居衡步都耿满弘匡国文寇广禄阙东欧殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后荆红游郏竺权逯盖益桓公仉督岳帅缑亢况郈有琴归海晋楚闫法汝鄢涂钦商牟佘佴伯赏墨哈谯篁年爱阳佟言福南火铁迟漆官冼真展繁檀祭密敬揭舜楼疏冒浑挚胶随高皋原种练弥仓眭蹇覃阿门恽来綦召仪风介巨木京狐郇虎枚抗达杞苌折麦庆过竹端鲜皇亓老是秘畅邝还宾闾辜纵侴',
                    round(dbms_random.value(1, 517)), 1);
            --名字，两个或者一个字，随机数取到2的时候就两个字
            v_name := v_name || substr(
                    '嘉哲俊博妍乐佳涵晨宇怡泽子凡悦思奕依浩泓彤冰媛凯伊淇淳一洁茹清吉源渊和函妤宜云琪菱宣沂健信欣可洋萍荣榕含佑明雄梅芝英义淑卿乾亦芬萱昊芸天岚昕尧鸿棋琳孜娟宸林乔琦丞安毅凌泉坤晴竹娴婕恒渝菁龄弘佩勋宁元栋盈江卓春晋逸沅倩昱绮海圣承民智棠容羚峰钰涓新莉恩羽妮旭维家泰诗谚阳彬书苓汉蔚坚茵耘喆国仑良裕融致富德易虹纲筠奇平蓓真之凰桦玫强村沛汶锋彦延庭霞冠益劭钧薇亭瀚桓东滢恬瑾达群茜先洲溢楠基轩月美心茗丹森学文',
                    round(dbms_random.value(1, 200)), 1);
            if round(dbms_random.value(1, 2)) = 2 then
                v_name := v_name || substr(
                        '嘉哲俊博妍乐佳涵晨宇怡泽子凡悦思奕依浩泓彤冰媛凯伊淇淳一洁茹清吉源渊和函妤宜云琪菱宣沂健信欣可洋萍荣榕含佑明雄梅芝英义淑卿乾亦芬萱昊芸天岚昕尧鸿棋琳孜娟宸林乔琦丞安毅凌泉坤晴竹娴婕恒渝菁龄弘佩勋宁元栋盈江卓春晋逸沅倩昱绮海圣承民智棠容羚峰钰涓新莉恩羽妮旭维家泰诗谚阳彬书苓汉蔚坚茵耘喆国仑良裕融致富德易虹纲筠奇平蓓真之凰桦玫强村沛汶锋彦延庭霞冠益劭钧薇亭瀚桓东滢恬瑾达群茜先洲溢楠基轩月美心茗丹森学文',
                        round(dbms_random.value(1, 200)), 1);
            end if;
            --性别，一半男一半女
            if mod(v_c, 2) = 0 then
                v_sex := 2;
            else
                v_sex := 1;
            end if;
            --日期字符串
            v_date := round(dbms_random.value(1950, 2019));--年
            v_date := v_date || '-' || round(dbms_random.value(1, 12));--月
            v_date := v_date || '-' || round(dbms_random.value(1, 28));
            --日，考虑到2月份，最大只给到28

            --账号表变量处理
            --账号
            v_ACC_NO := '622230' || round(dbms_random.value(1000000000000, 9999999999999));
            --账户状态，每300个有一个冻结
            if mod(v_c, 300) = 0 then
                v_ACC_STATUS := 0;
            else
                v_ACC_STATUS := 1;
            end if;
            --账号交易限额和企业个人账户,每500个一个企业账户
            if mod(v_c, 500) = 0 then
                v_ACC_LIMIT := 1000000;
                v_ACC_TYPE := '企业';
            else
                v_ACC_LIMIT := 20000;
                v_ACC_TYPE := '个人';
            end if;

            --归集表变量处理
            --主账号,每十个有一个主账号
            if v_c = 1 then
                v_MAIN_ACC := v_ACC_NO;
            end if;
            if mod(v_c, 10) = 0 then
                v_MAIN_ACC := v_ACC_NO;
            end if;
            v_FOLLOW_ACC := v_ACC_NO;

            --异常表变量处理
            --异常类型，随机登录异常、转账异常
            if round(dbms_random.value(1, 2)) = 1 then
                v_ODT_TYPE := '转账异常';
                if round(dbms_random.value(1, 2)) = 1 then
                    v_ODT_CAUSE := '转账超时';
                else
                    v_ODT_CAUSE := '转账失败';
                end if;
            else
                v_ODT_TYPE := '登录异常';
                if round(dbms_random.value(1, 2)) = 1 then
                    v_ODT_CAUSE := '频繁登录';
                else
                    v_ODT_CAUSE := '异地登录';
                end if;
            end if;

            --插入用户表
            insert into T_USER
            values (v_USER_ID,
                    v_name,
                    v_sex,
                    to_date(v_date, 'YYYY-MM-DD'),
                    round(dbms_random.value(100000000000000000, 999999999999999999)),
                    '广东省深圳市',
                    round(dbms_random.value(100000, 999999)),
                    v_telno || round(dbms_random.value(10000000, 99999999)),
                    round(dbms_random.value(10000000000, 99999999999)),
                    round(dbms_random.value(1000000000, 9999999999)) || '@qq.com',
                    null,
                    null,
                    null);
            commit;

            --插入账号表
            insert into T_ACCOUNT
            values (v_ACC_NO,
                    'CNY',
                    '622230',
                       --客户号
                    v_USER_ID,
                    v_ACC_STATUS,
                    v_ACC_LIMIT,
                    round(dbms_random.value(0, 1000000)),
                    v_ACC_TYPE,
                    to_date(v_date, 'YYYY-MM-DD'),
                    sys_guid(),
                    '未签约',
                    null);
            commit;

            --插入转账交易表
            insert into T_TRANSFER
            values (sys_guid(),
                    to_date(v_date, 'YYYY-MM-DD'),
                    0,--全是同行转账，垃圾数据
                    '成功',
                    v_ACC_NO,
                    v_name,
                    '工商银行',
                    v_ACC_NO,
                    v_name,
                    '工商银行',
                    'CNY',
                    round(dbms_random.value(1, 20000)),--转账金额
                    '同行转账',
                    null);
            commit;


            --插入常用收款人
            insert into T_USUALCOLL
            values (v_MAIN_ACC,
                    v_FOLLOW_ACC,
                    v_name,
                    round(dbms_random.value(1, 2000000)));
            commit;


            v_c := v_c + 1;
        end loop;
end;

--执行过程函数
begin
    add_data();
end;



--跨行转账
--存储过程
create or replace procedure add_data2 is
    v_c          number(8)      := 1;--循环次数

    --用户表变量
    v_USER_ID    varchar2(20);--客户号
    v_name       varchar2(80);--客户户姓名
    v_sex        number(1)      := 0;--性别
    v_date       varchar2(10);--日期字符串
    v_telno      varchar2(20)   := '86-10-';--固话区号

    --账号表变量
    v_ACC_NO     varchar2(25);--银行卡号前六位
    v_ACC_STATUS number(1)      := 1;--账号状态，冻结或者可用
    v_ACC_LIMIT  decimal(14, 2) := 20000;--账号交易限额
    v_ACC_TYPE   varchar2(10)   := '个人';--卡类型,个人,企业

    --归集表变量
    v_MAIN_ACC   varchar2(25);--主账号
    v_FOLLOW_ACC varchar2(25);--子账号

    --异常表变量
    v_ODT_TYPE   varchar2(20);
    v_ODT_CAUSE  varchar2(20);

begin
    --循环插入10000万条数据
    while v_c <= 10000
        loop
        --用户表变量处理
        --客户号变量处理
            v_USER_ID := round(dbms_random.value(1000000000000000000, 99999999999999999999));
            --用户姓名变量处理
            --姓氏
            v_name := substr(
                    '赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊于惠甄曲家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘景詹束龙叶幸司韶郜黎蓟溥印宿白怀蒲邰从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍却璩桑桂濮牛寿通边扈燕冀浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庾终暨居衡步都耿满弘匡国文寇广禄阙东欧殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后荆红游郏竺权逯盖益桓公仉督岳帅缑亢况郈有琴归海晋楚闫法汝鄢涂钦商牟佘佴伯赏墨哈谯篁年爱阳佟言福南火铁迟漆官冼真展繁檀祭密敬揭舜楼疏冒浑挚胶随高皋原种练弥仓眭蹇覃阿门恽来綦召仪风介巨木京狐郇虎枚抗达杞苌折麦庆过竹端鲜皇亓老是秘畅邝还宾闾辜纵侴',
                    round(dbms_random.value(1, 517)), 1);
            --名字，两个或者一个字，随机数取到2的时候就两个字
            v_name := v_name || substr(
                    '嘉哲俊博妍乐佳涵晨宇怡泽子凡悦思奕依浩泓彤冰媛凯伊淇淳一洁茹清吉源渊和函妤宜云琪菱宣沂健信欣可洋萍荣榕含佑明雄梅芝英义淑卿乾亦芬萱昊芸天岚昕尧鸿棋琳孜娟宸林乔琦丞安毅凌泉坤晴竹娴婕恒渝菁龄弘佩勋宁元栋盈江卓春晋逸沅倩昱绮海圣承民智棠容羚峰钰涓新莉恩羽妮旭维家泰诗谚阳彬书苓汉蔚坚茵耘喆国仑良裕融致富德易虹纲筠奇平蓓真之凰桦玫强村沛汶锋彦延庭霞冠益劭钧薇亭瀚桓东滢恬瑾达群茜先洲溢楠基轩月美心茗丹森学文',
                    round(dbms_random.value(1, 200)), 1);
            if round(dbms_random.value(1, 2)) = 2 then
                v_name := v_name || substr(
                        '嘉哲俊博妍乐佳涵晨宇怡泽子凡悦思奕依浩泓彤冰媛凯伊淇淳一洁茹清吉源渊和函妤宜云琪菱宣沂健信欣可洋萍荣榕含佑明雄梅芝英义淑卿乾亦芬萱昊芸天岚昕尧鸿棋琳孜娟宸林乔琦丞安毅凌泉坤晴竹娴婕恒渝菁龄弘佩勋宁元栋盈江卓春晋逸沅倩昱绮海圣承民智棠容羚峰钰涓新莉恩羽妮旭维家泰诗谚阳彬书苓汉蔚坚茵耘喆国仑良裕融致富德易虹纲筠奇平蓓真之凰桦玫强村沛汶锋彦延庭霞冠益劭钧薇亭瀚桓东滢恬瑾达群茜先洲溢楠基轩月美心茗丹森学文',
                        round(dbms_random.value(1, 200)), 1);
            end if;
            --性别，一半男一半女
            if mod(v_c, 2) = 0 then
                v_sex := 2;
            else
                v_sex := 1;
            end if;
            --日期字符串
            v_date := round(dbms_random.value(1950, 2019));--年
            v_date := v_date || '-' || round(dbms_random.value(1, 12));--月
            v_date := v_date || '-' || round(dbms_random.value(1, 28));
            --日，考虑到2月份，最大只给到28

            --账号表变量处理
            --账号
            v_ACC_NO := '622230' || round(dbms_random.value(1000000000000, 9999999999999));
            --账户状态，每300个有一个冻结
            if mod(v_c, 300) = 0 then
                v_ACC_STATUS := 0;
            else
                v_ACC_STATUS := 1;
            end if;
            --账号交易限额和企业个人账户,每500个一个企业账户
            if mod(v_c, 500) = 0 then
                v_ACC_LIMIT := 1000000;
                v_ACC_TYPE := '企业';
            else
                v_ACC_LIMIT := 20000;
                v_ACC_TYPE := '个人';
            end if;

            --归集表变量处理
            --主账号,每十个有一个主账号
            if v_c = 1 then
                v_MAIN_ACC := v_ACC_NO;
            end if;
            if mod(v_c, 10) = 0 then
                v_MAIN_ACC := v_ACC_NO;
            end if;
            v_FOLLOW_ACC := v_ACC_NO;

            --异常表变量处理
            --异常类型，随机登录异常、转账异常
            if round(dbms_random.value(1, 2)) = 1 then
                v_ODT_TYPE := '转账异常';
                if round(dbms_random.value(1, 2)) = 1 then
                    v_ODT_CAUSE := '转账超时';
                else
                    v_ODT_CAUSE := '转账失败';
                end if;
            else
                v_ODT_TYPE := '登录异常';
                if round(dbms_random.value(1, 2)) = 1 then
                    v_ODT_CAUSE := '频繁登录';
                else
                    v_ODT_CAUSE := '异地登录';
                end if;
            end if;

            --插入用户表
            insert into T_USER
            values (v_USER_ID,
                    v_name,
                    v_sex,
                    to_date(v_date, 'YYYY-MM-DD'),
                    round(dbms_random.value(100000000000000000, 999999999999999999)),
                    '广东省深圳市',
                    round(dbms_random.value(100000, 999999)),
                    v_telno || round(dbms_random.value(10000000, 99999999)),
                    round(dbms_random.value(10000000000, 99999999999)),
                    round(dbms_random.value(1000000000, 9999999999)) || '@qq.com',
                    null,
                    null,
                    null);
            commit;

            --插入账号表
            insert into T_ACCOUNT
            values (v_ACC_NO,
                    'CNY',
                    '622848',
                       --客户号
                    v_USER_ID,
                    v_ACC_STATUS,
                    v_ACC_LIMIT,
                    round(dbms_random.value(0, 1000000)),
                    v_ACC_TYPE,
                    to_date(v_date, 'YYYY-MM-DD'),
                    sys_guid(),
                    '未签约',
                    null);
            commit;

            --插入转账交易表
            insert into T_TRANSFER
            values (sys_guid(),
                    to_date(v_date, 'YYYY-MM-DD'),
                    0,--全是同行转账，垃圾数据
                    '成功',
                    v_ACC_NO,
                    v_name,
                    '农业银行',
                    v_ACC_NO,
                    v_name,
                    '农业银行',
                    'CNY',
                    round(dbms_random.value(1, 20000)),--转账金额
                    '同行转账',
                    null);
            commit;


            --插入常用收款人
            insert into T_USUALCOLL
            values (v_MAIN_ACC,
                    v_FOLLOW_ACC,
                    v_name,
                    round(dbms_random.value(1, 2000000)));
            commit;


            v_c := v_c + 1;
        end loop;
end;

--执行过程函数
begin
    add_data2();
end;