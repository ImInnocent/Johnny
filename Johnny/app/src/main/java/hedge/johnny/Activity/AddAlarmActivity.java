package hedge.johnny.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hedge.johnny.HedgeObject.HedgeDBHelper;
import hedge.johnny.R;

/**
 * Created by Administrator on 2015-07-21.
 */
public class AddAlarmActivity extends Activity {
    private boolean mDayOfWeek[] = new boolean[7];
    private int mHour;
    private int mMinute;
    private String mAlarmType;
    private boolean mWeatherAlarm;
    private HedgeDBHelper dbHelper = new HedgeDBHelper(this);

    private String sound = "소리";
    private String sound_vibe = "소리+진동";
    private String vibe = "진동";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        // 현재 시간을 msec으로 구한다.
        long now = System.currentTimeMillis();
        // 현재 시간을 저장 한다.
        Date date = new Date(now);
        // 시간 포맷으로 만든다.
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
        SimpleDateFormat sdfMinute = new SimpleDateFormat("mm");
        String strHour = sdfHour.format(date);
        String strMinute = sdfMinute.format(date);

        EditText hour, minute;
        hour = (EditText)findViewById(R.id.hour_add_alarm);
        minute = (EditText)findViewById(R.id.minute_add_alarm);
        hour.setText(strHour);
        minute.setText(strMinute);
    }

    void initMembers() {
        //시간 저장
        EditText hour = (EditText) findViewById(R.id.hour_add_alarm);
        CheckBox daynight = (CheckBox) findViewById(R.id.daynight_add_alarm);

        mHour = Integer.parseInt(hour.getText().toString());

        if(daynight.isChecked())
            mHour += 12;

        EditText minute = (EditText)findViewById(R.id.minute_add_alarm);

        mMinute = Integer.parseInt(minute.getText().toString());

        for(int i = 0; i < 7; i++){
            int id = getResources().getIdentifier("@id/day" + (i + 1) + "_add_alarm", "id", this.getPackageName());
            mDayOfWeek[i] = ((CheckBox) findViewById(id)).isChecked();
        }

        mWeatherAlarm = ((CheckBox)findViewById(R.id.weather_add_alarm)).isChecked();

        if(((RadioButton)findViewById(R.id.sound_add_alarm)).isChecked())
            mAlarmType = sound;
        if(((RadioButton)findViewById(R.id.sound_vibe_add_alarm)).isChecked())
            mAlarmType = sound_vibe;
        if(((RadioButton)findViewById(R.id.vibe_add_alarm)).isChecked())
            mAlarmType = vibe;
    }

    void insertToDB(){
        //데이터 베이스에 저장
        ContentValues row = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        row.put("title", "제목");
        row.put("hour", mHour);
        row.put("min", mMinute);

        for(int i=0; i<7; i++)
            row.put("d" + i, mDayOfWeek[i] ? 1 : 0 );

        db.insert("HedgeAlarm", null, row);
    }

    void setAlarm(){
        //알람을 설정
        AlarmManager alarm = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TimeoutActivity.class);
        intent.putExtra("day_of_week", mDayOfWeek);    //intent에 요일 on/off정보를 넣음.
        intent.putExtra("weather_alarm", mWeatherAlarm);
        intent.putExtra("alarm_type", mAlarmType);
        PendingIntent pender = PendingIntent.getActivity(this, 0, intent, 0);

        long OneDay = 24 * 60 * 60 * 1000;  //하루를 MilliSecond로 표현

        Calendar current = Calendar.getInstance();
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);
        long cTime = current.getTimeInMillis(); //currentTime = cTime

        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, mHour);
        target.set(Calendar.MINUTE, mMinute);
        target.set(Calendar.SECOND, 0);
        target.set(Calendar.MILLISECOND, 0);
        long tTime = target.getTimeInMillis();  //targetTime = tTime

        long trigger = cTime > tTime ? tTime + OneDay - cTime : tTime - cTime;

        long intervalTime = OneDay; //하루마다 반복하고, 알람시 켜지는 클래스에서 요일반복을 처리한다.

        //alarm.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pender);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cTime + trigger, intervalTime, pender);
    }

    public void btnClickAddAlarm(View v){
        switch (v.getId()){
            case R.id.btn_save_add_alarm:
                initMembers();      //설정값을 변수에 저장
                insertToDB();       //데이터 베이스에 저장
                setAlarm();         //알람 설정
                setResult(1);
                finish();
                break;
            case R.id.btn_cancel_add_alarm:
                finish();
                break;
        }
    }
}
