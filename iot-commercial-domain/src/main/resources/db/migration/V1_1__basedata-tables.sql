
CREATE TABLE party_function (
    party_function_code varchar(3) PRIMARY KEY,
    party_function_name varchar(100) NOT NULL,
    party_function_description varchar(350) NOT NULL
);

CREATE TABLE event_classifier (
    event_classifier_code varchar(3) PRIMARY KEY,
    event_classifier_name varchar(30) NOT NULL,
    event_classifier_description varchar(250) NOT NULL
);
/*
insert into party_function (party_function_code, party_function_name, party_function_description)
  values
('CA','',''),
('AG','',''),
('VSP','',''),
('SVP','','')
;

insert into event_classifier (event_classifier_code, event_classifier_name, event_classifier_description)
  values
    ('ACT','Actual','')
;

insert into carrier (carrier_name, smdg_code)
    values
  ('CMA CGM','CMA'),
  ('Evergreen Marine Corporation','EMC'),
  ('Hapag Lloyd','HLC'),
  ('Hyundai','HMM'),
  ('Maersk','MSK'),
  ('Mediterranean Shipping Company','MSC'),
  ('Ocean Network Express Pte. Ltd','ONE'),
  ('Yang Ming Line','YML'),
  ('Zim Israel Navigation Company','ZIM')
  ;


*/
/*

insert into iot_event_type (iot_event_type_code, iot_event_type_name, iot_event_type_description)
  values
    ('DETC','Detected','')
;


insert into iot_event_code (iot_event_code, iot_event_code_name, iot_event_code_description)
  values ('DRO','Door Opened','');
*/
CREATE TABLE iot_event_type (
    iot_event_type_code varchar(4) PRIMARY KEY,
    iot_event_type_name varchar(30) NOT NULL,
    iot_event_type_description varchar(350) NOT NULL
);

CREATE TABLE iot_event_code (
    iot_event_code varchar(4) PRIMARY KEY,
    iot_event_code_name varchar(30) NOT NULL,
    iot_event_code_description varchar(350) NOT NULL
);
COMMIT;
