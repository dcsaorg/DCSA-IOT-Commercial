
CREATE TABLE party (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    party_name varchar(100) NULL,
    carrier_id uuid NOT NULL REFERENCES carrier(id)
);

CREATE TABLE event (
    event_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    event_classifier_code varchar(3) NOT NULL REFERENCES event_classifier (event_classifier_code),
    event_created_date_time timestamp with time zone DEFAULT now() NOT NULL,
    event_date_time timestamp with time zone NOT NULL
);

CREATE TABLE iot_commercial_event (
    publisher_id uuid NOT NULL REFERENCES party(id),
    publisher_role varchar(3) NOT NULL REFERENCES party_function(party_function_code),
    iot_event_type_code varchar(4) NOT NULL REFERENCES iot_event_type(iot_event_type_code),
    iot_event_code varchar(4) NOT NULL REFERENCES iot_event_code(iot_event_code),
    equipment_reference varchar(15) NULL,
    event_location_id uuid NULL REFERENCES location(id)
) INHERITS (event);

CREATE TABLE document_reference (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    document_reference_value varchar(35),
    document_reference_type varchar(3) CHECK (document_reference_type IN ('CBR','BKG','SHI','TRD')),
    event_id uuid
);

-- TRIGGER FOR CACHING event_cache_queue

CREATE OR REPLACE FUNCTION queue_iot_event() RETURNS TRIGGER AS $$
    BEGIN
      INSERT INTO event_cache_queue (event_id, event_type) VALUES(NEW.event_id, 'IOT_COMMERCIAL');
      RETURN NULL;
    END;
$$ LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS queue_iot_events ON iot_commercial_event;
CREATE TRIGGER queue_iot_events AFTER INSERT ON iot_commercial_event
    FOR EACH ROW EXECUTE PROCEDURE queue_iot_event();
