
CREATE TABLE iot_commercial_event (
                                         event_id varchar(100) NOT NULL PRIMARY KEY,
                                         content jsonb NOT NULL,
                                         event_created_date_time timestamp with time zone NOT NULL,
                                         event_date_time timestamp with time zone NULL
);
CREATE INDEX ON iot_commercial_event (event_created_date_time);
CREATE INDEX ON iot_commercial_event (event_date_time);
CREATE INDEX iot_event_type_code ON iot_commercial_event USING btree ((content->>'iotEventTypeCode'));
CREATE INDEX equipment_reference_idx ON iot_commercial_event USING btree ((content->>'equipmentReference'));

CREATE TABLE iot_commercial_event_document_reference (
                                                            id uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
                                                            event_id varchar(100) NOT NULL REFERENCES iot_commercial_event (event_id),
                                                            document_reference_type varchar(3) NOT NULL,
                                                            document_reference_value  varchar(100) NOT NULL
);

CREATE UNIQUE INDEX ON iot_commercial_event_document_reference (event_id, document_reference_type, document_reference_value);
/**/
CREATE TABLE iot_event_type (
                                iot_event_type_code varchar(4) PRIMARY KEY,
                                iot_event_type_name varchar(30) NOT NULL,
                                iot_event_type_description varchar(350) NULL
);
CREATE TABLE event_subscription (
                                    subscription_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                                    callback_url text NOT NULL,
                                    carrier_booking_reference varchar(35) NULL,
                                    equipment_reference varchar(11) NULL,
                                    iot_event_type_code varchar(4) NOT NULL REFERENCES iot_event_type(iot_event_type_code),
                                    secret bytea NOT NULL,
                                    created_date_time timestamp with time zone NOT NULL default now(),
                                    updated_date_time timestamp with time zone default now()
);

