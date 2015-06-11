SET serveroutput ON;
-- ${作成者}
DECLARE
  CURSOR c1 IS
  select * from sak.受注m
    where 品番 = 'a001' order by 受注番号;
  -- rec c1%rowtype;
BEGIN
  dbms_output.put_line ('start...');
  FOR rec IN c1 LOOP
    dbms_output.put_line (rec.受注番号 || ' ' || rec.品番);
  END LOOP;
  dbms_output.put_line ('end');

EXCEPTION
  WHEN OTHERS THEN
    dbms_output.put_line ('** エラー **');
END;
/