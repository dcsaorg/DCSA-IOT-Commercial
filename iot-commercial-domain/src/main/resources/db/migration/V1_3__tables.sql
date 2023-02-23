
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
/*
-- example value from swagger
INSERT INTO location (
  id,
  location_name,
  address_id,
  latitude,
  longitude,
  un_location_code,
  facility_id
) VALUES (
  '8791f557-fe69-42c9-a420-f39f023d6207'::uuid,
  'location_name',
  null,
  '48.85855',
  '2.294492036',
  null,
  null
  );
insert into party (id, party_name, carrier_id)
  values(
    '8791f557-fe69-42c9-a420-f39f09dd6207'::uuid,
    'Henrik',
    (SELECT id FROM carrier WHERE carrier_name = 'Hapag Lloyd')
);

INSERT INTO iot_commercial_event (
  event_id,
  event_classifier_code,
  event_created_date_time,
  event_date_time,
  publisher_id,
  publisher_role,
  iot_event_type_code,
  iot_event_code,
  equipment_reference,
  event_location_id
) VALUES (
  '123e4567-e89b-12d3-a456-426614174000'::uuid,
  'ACT',
  TO_DATE('2022/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),
  TO_DATE('2021/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),
  '8791f557-fe69-42c9-a420-f39f09dd6207'::uuid,
  'CA',

  'DRO',
  'MSKU9070323',
  '8791f557-fe69-42c9-a420-f39f023d6207'::uuid
);
*/
