
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

CREATE TABLE iot_event_type (
    iot_event_type_code varchar(4) PRIMARY KEY,
    iot_event_type_name varchar(30) NOT NULL,
    iot_event_type_description varchar(350) NULL
);

CREATE TABLE iot_event_code (
    iot_event_code varchar(4) PRIMARY KEY,
    iot_event_code_name varchar(30) NOT NULL,
    iot_event_code_description varchar(350) NULL
);
COMMIT;
