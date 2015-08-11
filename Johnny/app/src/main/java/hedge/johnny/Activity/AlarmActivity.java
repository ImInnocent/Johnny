package hedge.johnny.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.Integer;
import java.util.ArrayList;

import hedge.johnny.HedgeObject.HedgeDBHelper;
import hedge.johnny.R;

/**
 * Created by Administrator on 2015-07-21.
 */
public class AlarmActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView mListView = null;
    private AlarmAdapter mAdapter = null;
    private ArrayList<Alarm> array;
    private int selectPos;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        mListView = (ListView) findViewById(R.id.my_alarm_list);
        mListView.setDivider(new ColorDrawable(Color.WHITE)); // 리스트내 아이템간 경계선
        mListView.setDividerHeight(5);

//        array.add(new Alarm(false, 8, 35, new boolean[]{true, false, true, false, true, false, true}));  // 오전 8시 월수금일
//        array.add(new Alarm(true, 1, 0, new boolean[]{true, true, true, true, true, false, false}));    // 오후 1시 월화수목금

        // 데이터 베이스
        HedgeDBHelper DBHelper = new HedgeDBHelper(this);
        db = DBHelper.getWritableDatabase();

        fillMyAlarmArray();

        // 어댑터
        mAdapter = new AlarmAdapter(this, R.layout.alarm_row, array);
        mListView.setAdapter(mAdapter);

        // 클릭 리스너
        //mListView.setOnItemClickListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                //refreshList();
                array.clear();
                onCreate(null);
                break;
            default:
                break;
        }
    }

    private void fillMyAlarmArray(){
        Cursor cursor = db.rawQuery("SELECT * from HedgeAlarm", null);

        // 데이터 삽입
        array = new ArrayList<Alarm>();

        cursor.moveToFirst();
        for(int i=0; i < cursor.getCount(); i++){
            int id = cursor.getInt(0);
            boolean daynight = cursor.getInt(2) >= 12 ? true : false;       //오전인지 오후인지 확인
            int hour = cursor.getInt(2) - (daynight ? 12 : 0);                //오후라면 12시간을 뺌
            int minute = cursor.getInt(3);
            boolean day_of_week[] = new boolean[7];

            for(int j=0; j < 7; j++)
                day_of_week[j] = cursor.getInt(4 + j) == 1 ?  true : false;

            array.add(new Alarm(id, daynight, hour, minute, day_of_week));

            cursor.moveToNext();
        }
    }

    private void refreshList(){
        Cursor cursor = db.rawQuery("SELECT * from HedgeAlarm", null);

        if(array.size() < cursor.getCount())
        {
            cursor.moveToLast();

            for(int i=0; i < cursor.getCount() - array.size() - 1; i++){
                cursor.moveToPrevious();
            }

            for(int i=0; i < cursor.getCount() - array.size(); i++){
                int id = cursor.getInt(0);
                boolean daynight = cursor.getInt(2) >= 12 ? true : false;       //오전인지 오후인지 확인
                int hour = cursor.getInt(2) - (daynight ? 12 : 0);                //오후라면 12시간을 뺌
                int minute = cursor.getInt(3);
                boolean day_of_week[] = new boolean[7];

                for(int j=0; j < 7; j++)
                    day_of_week[j] = cursor.getInt(4 + j) == 1 ?  true : false;

                array.add(new Alarm(id, daynight, hour, minute, day_of_week));

                cursor.moveToNext();
            }

            mAdapter.notifyDataSetChanged();
        }
    }

//    @Override
//    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//        if(columnIndex == 1)
//        {
//            TextView t = (TextView) view;
//            t.setText( String.format( "%s", cursor.getInt(columnIndex) ) );
//        }
//        if(columnIndex == 2)
//        {
//            TextView t = (TextView) view;
//            t.setText( String.format( "%2d:", cursor.getInt(columnIndex) ) );
//        }
//        if(columnIndex == 3)
//        {
//            TextView t = (TextView) view;
//            t.setText( String.format( "%2d", cursor.getInt(columnIndex) ) );
//        }
//        if(columnIndex >= 4 && columnIndex <= 10)
//        {
//            String[] str = {"월","화","수","목","금","토","일"};
//            TextView et = (TextView)view;
//            if( cursor.getString(columnIndex).equals("1")) {
//                et.setTextColor(Color.parseColor("#00ff00"));
//            }
//            else {
//                et.setTextColor(Color.parseColor("#000000"));
//            }
//            et.setText(str[columnIndex-4]);
//        }
//        return true;
//    }

    public void btnClickAlarm(View v) {
        switch (v.getId()) {
            case R.id.btn_add_alarm:
                Intent intent = new Intent(AlarmActivity.this, AddAlarmActivity.class);
                startActivityForResult(intent, 0);
                break;

            case R.id.btn_del:
                deleteFromDB(v);
                int index = (Integer)v.getTag();
                array.remove(index);
                onCreate(null);
                //Toast.makeText(getApplicationContext(), (Integer)v.getTag() + " del", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_modify:
                Toast.makeText(getApplicationContext(), (Integer)v.getTag() + " mod", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    void deleteFromDB(View v){
        int number = (Integer) v.getTag();
        db.delete("HedgeAlarm", "_id =" + array.get(number).getDb_Id(), null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), (CharSequence) array.get(position), Toast.LENGTH_SHORT).show();
        selectPos = position;
    }
}
