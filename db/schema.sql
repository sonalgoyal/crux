-- ----------------------------------------------------------------------------------------------------------
-- * Copyright 10011 Nube Technologies
-- *
-- * Licensed under the Apache License, Version 2.0 (the "License");
-- * you may not use this file except in compliance with the License.
-- * You may obtain a copy of the License at
-- *
-- * http://www.apache.org/licenses/LICENSE-2.0
-- *
-- * Unless required by applicable law or agreed to in writing, software distributed
-- * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
-- * CONDITIONS OF ANY KIND, either express or implied.
-- * See the License for the specific language governing permissions and limitations under the License.
-- ----------------------------------------------------------------------------------------------------------

-- -----------------------------------------------------
-- Table `datastore`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `datastore` ;

CREATE  TABLE IF NOT EXISTS `datastore`(
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`name` VARCHAR(100) NOT NULL ,
PRIMARY KEY (`id`) ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `reportType`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `reportType` ;

CREATE  TABLE IF NOT EXISTS `reportType` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`type` VARCHAR(100) NOT NULL, 
PRIMARY KEY (`id`),
UNIQUE INDEX `reportType_unique_keys` (`type`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `user` ;

CREATE  TABLE IF NOT EXISTS `user` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`name` VARCHAR(100) NOT NULL , 
`password` VARCHAR(100) NOT NULL, 
PRIMARY KEY (`id`),
UNIQUE INDEX `user_unique_keys` (`name`)
) ENGINE = InnoDB; 

-- -----------------------------------------------------
-- Table `valueType`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `valueType` ;

CREATE  TABLE IF NOT EXISTS `valueType`(
`id` BIGINT(100) NOT NULL AUTO_INCREMENT , 
`datastoreId` BIGINT(100) NOT NULL ,
`name` VARCHAR(100) NOT NULL ,
`className` VARCHAR(100) NOT NULL ,
`promotedValueClassName` VARCHAR(100) NOT NULL,
`isNumeric` BOOLEAN NOT NULL,
PRIMARY KEY (`id`) ,
UNIQUE INDEX `valueType_unique_keys` (`datastoreId`, `name`),
INDEX `fk_valueType_datastoreId` (`datastoreId` ASC) , 
CONSTRAINT `fk_valueType_datastoreId` 
FOREIGN KEY (`datastoreId`) 
REFERENCES `datastore` (`id` )     
ON DELETE NO ACTION     
ON UPDATE NO ACTION) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `connection`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `connection` ;

CREATE  TABLE IF NOT EXISTS `connection` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`datastoreId` BIGINT(100) NOT NULL ,
`userId` BIGINT(100) NOT NULL ,
`name` VARCHAR(100) NOT NULL, 
PRIMARY KEY (`id`) ,
UNIQUE INDEX `connection_unique_keys` (`datastoreId`, `userId`, `name`) ,
INDEX `fk_connection_datastoreId` (`datastoreId` ASC) ,
INDEX `fk_connection_userId` (`userId` ASC) ,
CONSTRAINT `fk_connection_datastoreId`
FOREIGN KEY (`datastoreId`)
REFERENCES `datastore` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION, 
CONSTRAINT `fk_connection_userId`
FOREIGN KEY (`userId`)
REFERENCES `user` (`id` )
ON DELETE NO ACTION
ON UPDATE NO ACTION) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `connectionProperty`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `connectionProperty` ;

CREATE  TABLE IF NOT EXISTS `connectionProperty` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`connectionId` BIGINT(100) NOT NULL ,
`property` VARCHAR(100) NOT NULL ,
`value` VARCHAR(100) NOT NULL, 
PRIMARY KEY (`id`) , 
UNIQUE INDEX `connectionProperty_unique_keys` (`connectionId`, `property`) ,
INDEX `fk_connectionProperty_connectionId` (`connectionId` ASC) ,
CONSTRAINT `fk_connectionProperty_connectionId` 
FOREIGN KEY (`connectionId`) 
REFERENCES `connection` (`id` )  
ON DELETE NO ACTION
ON UPDATE NO ACTION) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mapping` ;

CREATE  TABLE `mapping` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`connectionId` BIGINT(100) NOT NULL , 
`name` VARCHAR(100) NOT NULL,
`tableName`VARCHAR(100) NOT NULL ,  
PRIMARY KEY (`id`),
UNIQUE INDEX `mapping_unique_keys1` (`connectionId`, `name`) ,
UNIQUE INDEX `mapping_unique_keys2` (`connectionId`, `tableName`) ,
INDEX `fk_mapping_connectionId` (`connectionId` ASC) ,
CONSTRAINT `fk_mapping_connectionId` 
FOREIGN KEY (`connectionId`) 
REFERENCES `connection` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION) ENGINE = InnoDB; 

-- -----------------------------------------------------
-- Table `columnAlias`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `columnAlias` ;

CREATE  TABLE IF NOT EXISTS `columnAlias` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`mappingId` BIGINT(100) NOT NULL ,
`valueTypeId` BIGINT(100) NOT NULL ,
`columnFamily` VARCHAR(100) NOT NULL ,
`qualifier` VARCHAR(100) NOT NULL ,
`alias` VARCHAR(100) NOT NULL ,
PRIMARY KEY (`id`) , 
UNIQUE INDEX `columnAlias_unique_keys` (`mappingId`,`columnFamily`, `qualifier`) ,
INDEX `fk_columnAlias_mappingId` (`mappingId` ASC) ,
CONSTRAINT `fk_columnAlias_mappingId` 
FOREIGN KEY (`mappingId`) 
REFERENCES `mapping` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION, 
INDEX `fk_columnAlias_valueTypeId` (`valueTypeId`  ASC) ,
CONSTRAINT `fk_columnAlias_valueTypeId` 
FOREIGN KEY (`valueTypeId`) 
REFERENCES `valueType` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rowAlias`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `rowAlias` ;

CREATE  TABLE IF NOT EXISTS `rowAlias` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`mappingId` BIGINT(100) NOT NULL ,
`alias` VARCHAR(100) NOT NULL ,
`valueTypeId` BIGINT(100) NOT NULL ,
`length` BIGINT(100),
PRIMARY KEY (`id`) , 
UNIQUE INDEX `rowAlias_unique_keys` (`mappingId`, `alias`, `valueTypeId`) ,
INDEX `fk_rowAlias_mappingId` (`mappingId` ASC) ,
CONSTRAINT `fk_rowAlias_mappingId` 
FOREIGN KEY (`mappingId`) 
REFERENCES `mapping` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION, 
INDEX `fk_rowAlias_valueTypeId` (`valueTypeId`  ASC) ,
CONSTRAINT `fk_rowAlias_valueTypeId` 
FOREIGN KEY (`valueTypeId`) 
REFERENCES `valueType` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dashboard`
-- -----------------------------------------------------

CREATE  TABLE IF NOT EXISTS `dashboard` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT,
`indexNo` INT NOT NULL ,
`columnNo`INT NOT NULL ,
PRIMARY KEY (`id`)
) ENGINE = InnoDB;
-- -----------------------------------------------------
-- Table `report`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `report` ;

CREATE  TABLE IF NOT EXISTS report (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
 `userId` BIGINT(100) NOT NULL ,
 `reportTypeId` BIGINT(100) NOT NULL ,
 `name` VARCHAR(100) NOT NULL,
 `dashboardId` BIGINT,
 `numRecordsPerPage` BIGINT DEFAULT 0,
 PRIMARY KEY (`id`) ,
 UNIQUE INDEX `report_unique_keys` (`userId`, `name`) ,
INDEX `fk_report_userId` (`userId` ASC) ,
CONSTRAINT `fk_report_userId` 
FOREIGN KEY (`userId`) 
REFERENCES `user` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_report_reportTypeId` (`reportTypeId` ASC),
CONSTRAINT `fk_report_reportTypeId` 
FOREIGN KEY (`reportTypeId`) 
REFERENCES `reportType` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_report_dashboardId` (`dashboardId` ASC),
CONSTRAINT `fk_report_dashboardId` 
FOREIGN KEY (`dashboardId`) 
REFERENCES `dashboard` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reportDesign`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `reportDesign` ;

CREATE  TABLE IF NOT EXISTS `reportDesign` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`reportId` BIGINT(100) NOT NULL ,
`columnAliasId` BIGINT(100)  ,
`rowAliasId` BIGINT(100)  ,
`mappingAxis` VARCHAR(100) NOT NULL ,
PRIMARY KEY (`id`) ,
UNIQUE INDEX `report_unique_keys` (`reportId`, `columnAliasId`, `mappingAxis`) ,
INDEX `fk_reportDesign_reportId` (`reportId` ASC),
CONSTRAINT `fk_reportDesign_reportId`  
FOREIGN KEY (`reportId`) 
REFERENCES `report` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_reportDesign_rowAliasId` (`rowAliasId` ASC),
CONSTRAINT `fk_reportDesign_rowAliasId` 
FOREIGN KEY (`rowAliasId`) 
REFERENCES `rowAlias` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_reportDesign_columnAliasId` (`columnAliasId` ASC),
CONSTRAINT `fk_reportDesign_columnAliasId` 
FOREIGN KEY (`columnAliasId`) 
REFERENCES `columnAlias` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `filterType`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `filterType` ;

CREATE  TABLE IF NOT EXISTS `filterType` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`type` VARCHAR(100) NOT NULL, 
PRIMARY KEY (`id`),
UNIQUE INDEX `filterType_unique_keys` (`type`)
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rowFilter`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `rowFilter` ;

CREATE  TABLE IF NOT EXISTS `rowFilter` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`reportId` BIGINT(100) NOT NULL ,
`rowAliasId` BIGINT(100) NOT NULL ,
`filterTypeId` BIGINT(100) NOT NULL ,
`value` VARCHAR(100),
PRIMARY KEY (`id`),
UNIQUE INDEX `rowFilter_unique_keys` (`reportId`, `rowAliasId`,`filterTypeId`) ,
INDEX `fk_rowFilter_reportId` (`reportId` ASC),
CONSTRAINT `fk_rowFilter_reportId`  
FOREIGN KEY (`reportId`) 
REFERENCES `report` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_rowFilter_rowAliasId` (`rowAliasId` ASC),
CONSTRAINT `fk_rowFilter_rowAliasId`  
FOREIGN KEY (`rowAliasId`) 
REFERENCES `rowAlias` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_rowFilter_filterTypeId` (`filterTypeId` ASC),
CONSTRAINT `fk_rowFilter_filterTypeId`  
FOREIGN KEY (`filterTypeId`) 
REFERENCES `filterType` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `columnFilter`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `columnFilter` ;

CREATE  TABLE IF NOT EXISTS `columnFilter` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`reportId` BIGINT(100) NOT NULL ,
`columnAliasId` BIGINT(100) NOT NULL ,
`filterTypeId` BIGINT(100) NOT NULL ,
`value` VARCHAR(100),
PRIMARY KEY (`id`),
UNIQUE INDEX `columnFilter_unique_keys` (`reportId`, `columnAliasId`,`filterTypeId`) ,
INDEX `fk_columnFilter_reportId` (`reportId` ASC),
CONSTRAINT `fk_columnFilter_reportId`  
FOREIGN KEY (`reportId`) 
REFERENCES `report` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_columnFilter_columnAliasId` (`columnAliasId` ASC),
CONSTRAINT `fk_columnFilter_columnAliasId`  
FOREIGN KEY (`columnAliasId`) 
REFERENCES `columnAlias` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_columnFilter_filterTypeId` (`filterTypeId` ASC),
CONSTRAINT `fk_columnFilter_filterTypeId`  
FOREIGN KEY (`filterTypeId`) 
REFERENCES `filterType` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `valueFilterType`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `valueFilterType` ;

CREATE  TABLE IF NOT EXISTS `valueFilterType` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT,
`valueTypeId` BIGINT(100) NOT NULL,
`filterTypeId` BIGINT(100) NOT NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `valueFilterType_unique_keys` (`valueTypeId`,`filterTypeId`),
INDEX `fk_valueFilterType_valueTypeId` (`valueTypeId` ASC),
CONSTRAINT `fk_valueFilterType_valueTypeId`  
FOREIGN KEY (`valueTypeId`) 
REFERENCES `valueType` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_valueFilterType_filterTypeId` (`filterTypeId` ASC),
CONSTRAINT `fk_valueFilterType_filterTypeId`  
FOREIGN KEY (`filterTypeId`) 
REFERENCES `filterType` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `function`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `function` ;

CREATE  TABLE IF NOT EXISTS `function` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`functionName` VARCHAR(100) NOT NULL ,
`functionClass` VARCHAR(100) NOT NULL ,
`functionType` TINYINT NOT NULL ,
PRIMARY KEY (`id`),
UNIQUE INDEX `function_unique_keys` (`functionName`,`functionClass`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `functionTypeMapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `functionTypeMapping` ;

CREATE  TABLE IF NOT EXISTS `functionTypeMapping` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`functionId` BIGINT(100) NOT NULL ,
`valueTypeId` BIGINT(100) NOT NULL ,
`returnValueTypeId` BIGINT(100) NOT NULL ,
PRIMARY KEY (`id`),
INDEX `fk_functionTypeMapping_valueTypeId` (`valueTypeId` ASC),
CONSTRAINT `fk_functionTypeMapping_valueTypeId`  
FOREIGN KEY (`valueTypeId`) 
REFERENCES `valueType` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_functionTypeMapping_returnValueTypeId` (`returnValueTypeId` ASC),
CONSTRAINT `fk_functionTypeMapping_returnValueTypeId`  
FOREIGN KEY (`returnValueTypeId`) 
REFERENCES `valueType` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_functionTypeMapping_functionId` (`functionId` ASC),
CONSTRAINT `fk_functionTypeMapping_functionId`  
FOREIGN KEY (`functionId`) 
REFERENCES `function` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `reportDesignFunction`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `reportDesignFunction` ;

CREATE  TABLE IF NOT EXISTS `reportDesignFunction` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT,
`reportDesignId` BIGINT(100) NOT NULL,
`functionId` BIGINT(100) NOT NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `reportDesignFunction_unique_keys` (`reportDesignId`,`functionId`),
INDEX `fk_reportDesignFunction_reportDesignId` (`reportDesignId` ASC),
CONSTRAINT `fk_reportDesignFunction_reportDesignId`  
FOREIGN KEY (`reportDesignId`) 
REFERENCES `reportDesign` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION,
INDEX `fk_reportDesignFunction_functionId` (`functionId` ASC),
CONSTRAINT `fk_reportDesignFunction_functionId`  
FOREIGN KEY (`functionId`) 
REFERENCES `function` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `group bys`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groupBys` ;

CREATE  TABLE IF NOT EXISTS `groupBys` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT,
`reportId` BIGINT(100) NOT NULL,
PRIMARY KEY (`id`) , 
UNIQUE INDEX `groupBys_unique_keys` (`reportId`) ,
CONSTRAINT `fk_groupBys_reportId` 
FOREIGN KEY (`reportId`) 
REFERENCES `report` (`id` )  
ON DELETE NO ACTION 
ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `group by`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `groupBy` ;

CREATE  TABLE IF NOT EXISTS `groupBy` (
`id` BIGINT(100) NOT NULL AUTO_INCREMENT,
`groupBysId` BIGINT(100) NOT NULL,
`rowAliasId` BIGINT(100) NOT NULL,
`position` INT NOT NULL,
PRIMARY KEY (`id`) , 
UNIQUE INDEX `groupBy_unique_keys` (`groupBysId`, `rowAliasId`) ,
CONSTRAINT `fk_groupBy_rowAliasId` 
FOREIGN KEY (`rowAliasId`) 
REFERENCES `rowAlias` (`id` ),
CONSTRAINT `fk_groupBy_groupBysId` 
FOREIGN KEY (`groupBysId`) 
REFERENCES `groupBys` (`id` ) 
ON DELETE NO ACTION 
ON UPDATE NO ACTION 
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `analysis`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `analysis` ;

CREATE  TABLE IF NOT EXISTS `analysis`(
`id` BIGINT(100) NOT NULL AUTO_INCREMENT ,
`name` VARCHAR(100) NOT NULL ,
`text` VARCHAR(100) NOT NULL ,
PRIMARY KEY (`id`) ) ENGINE = InnoDB;



-- -----------------------------------------------------
-- insert queries
-- -----------------------------------------------------

INSERT  INTO `datastore`(`id`,`name`) VALUES (1, 'HBase');

INSERT  INTO `user`(`id`,`name`,`password`) VALUES (1, 'nube','nube');

INSERT  INTO `valueType`(`id`,`datastoreId`, `name`,`className`,`promotedValueClassName`, `isNumeric`) 
	VALUES (1, 1, 'String','java.lang.String','java.lang.String', false);
INSERT  INTO `valueType`(`id`,`datastoreId`, `name`,`className`,`promotedValueClassName`, `isNumeric`)
	VALUES (2, 1, 'Boolean','java.lang.Boolean', 'java.lang.Boolean', true);
INSERT  INTO `valueType`(`id`,`datastoreId`, `name`,`className`,`promotedValueClassName`, `isNumeric`)
	VALUES (3, 1, 'Integer','java.lang.Integer','java.lang.Long', true);
INSERT  INTO `valueType`(`id`,`datastoreId`, `name`,`className`,`promotedValueClassName`, `isNumeric`)
	VALUES (4, 1, 'Long','java.lang.Long','java.lang.Long', true);
INSERT  INTO `valueType`(`id`,`datastoreId`, `name`,`className`,`promotedValueClassName`, `isNumeric`)
	VALUES (5, 1, 'Float','java.lang.Float','java.lang.Double', true);
INSERT  INTO `valueType`(`id`,`datastoreId`, `name`,`className`,`promotedValueClassName`, `isNumeric`)
	VALUES (6, 1, 'Double','java.lang.Double','java.lang.Double', true);
INSERT  INTO `valueType`(`id`,`datastoreId`, `name`,`className`,`promotedValueClassName`, `isNumeric`)
	VALUES (7, 1, 'Short','java.lang.Short','java.lang.Long', true);


INSERT  INTO `reportType`(`id`,`type`) VALUES (1, 'Columns');
INSERT  INTO `reportType`(`id`,`type`) VALUES (2, 'Lines');
INSERT  INTO `reportType`(`id`,`type`) VALUES (3, 'Pie');
INSERT  INTO `reportType`(`id`,`type`) VALUES (4, 'Areas');
INSERT  INTO `reportType`(`id`,`type`) VALUES (5, 'Scatter');
INSERT  INTO `reportType`(`id`,`type`) VALUES (6, 'Table');
INSERT  INTO `reportType`(`id`,`type`) VALUES (7, 'ClusteredColumns');
INSERT  INTO `reportType`(`id`,`type`) VALUES (8, 'StackedColumns');
INSERT  INTO `reportType`(`id`,`type`) VALUES (9, 'StackedAreas');
INSERT  INTO `reportType`(`id`,`type`) VALUES (10, 'StackedLines');
INSERT  INTO `reportType`(`id`,`type`) VALUES (11, 'Spider');
-- INSERT  INTO `reportType`(`id`,`type`) VALUES (12, 'Choropleth');

-- these values are used directly by the server. So we should be very cautious about changing them.
INSERT  INTO `filterType`(`id`,`type`) VALUES (1, 'Equals');
INSERT  INTO `filterType`(`id`,`type`) VALUES (2, 'Not Equals');
INSERT  INTO `filterType`(`id`,`type`) VALUES (3, 'Less Than');
INSERT  INTO `filterType`(`id`,`type`) VALUES (4, 'Greater Than');
INSERT  INTO `filterType`(`id`,`type`) VALUES (5, 'Less Than Equals');
INSERT  INTO `filterType`(`id`,`type`) VALUES (6, 'Greater Than Equals');
INSERT  INTO `filterType`(`id`,`type`) VALUES (7, 'Starts With');
INSERT  INTO `filterType`(`id`,`type`) VALUES (8, 'Substring');
INSERT  INTO `filterType`(`id`,`type`) VALUES (9, 'Ends With');
INSERT  INTO `filterType`(`id`,`type`) VALUES (10, 'Pattern Matches');
INSERT  INTO `filterType`(`id`,`type`) VALUES (11, 'Pattern Not Matches');
-- INSERT  INTO `filterType`(`id`,`type`) VALUES (12, 'NULL');
INSERT  INTO `filterType`(`id`,`type`) VALUES (13, 'Substring Not Matches');

INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 1);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 2);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 3);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 4);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 6);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 7);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 8);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 9);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 10);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 11);
-- INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (1, 12);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (2, 1);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (2, 2);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (3, 1);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (3, 2);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (3, 3);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (3, 4);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (3, 5);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (3, 6);
-- INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (3, 7);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (4, 1);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (4, 2);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (4, 3);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (4, 4);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (4, 5);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (4, 6);
-- INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (4, 7);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (5, 1);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (5, 2);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (5, 3);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (5, 4);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (5, 5);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (5, 6);
-- INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (5, 7);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (6, 1);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (6, 2);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (6, 3);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (6, 4);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (6, 5);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (6, 6);
-- INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (6, 7);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (7, 1);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (7, 2);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (7, 3);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (7, 4);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (7, 5);
INSERT  INTO `valueFilterType`(`valueTypeId`,`filterTypeId`) VALUES (7, 6);
-- INSERT  INTO `valueFilterType`(`id`,`valueTypeId`,`filterTypeId`) VALUES (7, 7);

INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('Ceil','co.nubetech.crux.functions.Ceil',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('date','co.nubetech.crux.functions.DateFunction',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('day','co.nubetech.crux.functions.DateFunction',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('month','co.nubetech.crux.functions.DateFunction',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('year','co.nubetech.crux.functions.DateFunction',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('hour','co.nubetech.crux.functions.DateFunction',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('minute','co.nubetech.crux.functions.DateFunction',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('second','co.nubetech.crux.functions.DateFunction',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('LowerCase','co.nubetech.crux.functions.LowerCase',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('SubString','co.nubetech.crux.functions.SubString',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('Trim','co.nubetech.crux.functions.Trim',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('UpperCase','co.nubetech.crux.functions.UpperCase',1);
INSERT  INTO `function`(`functionName`,`functionClass`,`functionType`) VALUES ('Round','co.nubetech.crux.functions.Round',1);

INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (1,6,6);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (2,1,3);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (2,1,4);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (3,1,3);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (3,1,4);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (4,1,3);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (4,1,4);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (5,1,3);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (5,1,4);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (6,1,3);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (6,1,4);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (7,1,3);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (7,1,4);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (8,1,3);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (8,1,4);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (9,1,1);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (10,1,1);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (11,1,1);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (12,1,1);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (13,1,5);
INSERT  INTO `functionTypeMapping`(`functionId`,`valueTypeId`,`returnValueTypeId`) VALUES (13,1,6);


