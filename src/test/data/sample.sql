SET serveroutput ON;
-- ${�쐬��}
DECLARE
  CURSOR c1 IS
  select * from sak.��m
    where �i�� = 'a001' order by �󒍔ԍ�;
  -- rec c1%rowtype;
BEGIN
  dbms_output.put_line ('start...');
  FOR rec IN c1 LOOP
    dbms_output.put_line (rec.�󒍔ԍ� || ' ' || rec.�i��);
  END LOOP;
  dbms_output.put_line ('end');

EXCEPTION
  WHEN OTHERS THEN
    dbms_output.put_line ('** �G���[ **');
END;
/