CREATE TABLE IF NOT EXISTS `global_variables` (
  `var`  VARCHAR(255) NOT NULL DEFAULT '',
  `value` VARCHAR(255) ,
  PRIMARY KEY (`var`)
);