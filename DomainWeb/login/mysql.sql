#
# Table structure for table 'members'
#

CREATE TABLE `members` (
  `member_id` int(11) unsigned NOT NULL auto_increment,
  `firstname` varchar(100) default NULL,
  `lastname` varchar(100) default NULL,
  `login` varchar(100) NOT NULL default '',
  `passwd` varchar(32) NOT NULL default '',
  PRIMARY KEY  (`member_id`)
) TYPE=MyISAM;



#
# Dumping data for table 'members'
#
INSERT INTO `members` (`member_id`, `firstname`, `lastname`, `login`, `passwd`) VALUES("1", "Jatinder", "Thind", "phpsense",
 "ba018360fc26e0cc2e929b8e071f052d");
INSERT INTO `members` (`member_id`, `firstname`, `lastname`, `login`, `passwd`) VALUES("2", "lie", "lie", "lie", "ffacb8898");

SELECT A.domain, A.reg_email,A.reg_name, A.reg_phone, A.reg_country,C.price,C.pricetype,C.pricedate FROM 
	(SELECT domain, reg_email, reg_name, reg_phone, reg_country FROM domainwhois WHERE update_date='2014-05-14' and type=4) as A
	JOIN
	(SELECT domain FROM domainwhois WHERE type=4 group by domain having count(update_date)>1 ) as B
	ON A.domain=B.domain
	LEFT JOIN 
	(select aa.domain,aa.price,aa.pricedate,aa.pricetype from
	(SELECT domain, price, pricedate,pricetype FROM domainprice WHERE type=4) as aa
	join (SELECT domain, max(pricedate) pricedate  FROM domainprice WHERE type=4 and pricedate<='2014-05-14' group by domain) as bb
	on aa.domain=bb.domain and aa.pricedate=bb.pricedate) as C
	ON A.domain = C.domain;
	

	
SELECT domain, type FROM domainwhois where type=4  HAVING SUBSTRING_INDEX(domain,'.',2)='cn';