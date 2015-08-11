package hedge.johnny.Activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import hedge.johnny.HedgeObject.DayForecast;
import hedge.johnny.HedgeObject.HttpClient.WeatherHttpClient;
import hedge.johnny.HedgeObject.Weather;
import hedge.johnny.HedgeObject.WeatherForecast;
import hedge.johnny.R;

public class TimeoutActivity extends Activity implements OnInitListener {
    TextToSpeech myTTS = null;
    TextView txtMsg = null;
    JSONWeatherTask task = new JSONWeatherTask();
    JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
    MediaPlayer music = null;
    private boolean mWeatherAlarm;

    Timer timer = new Timer(true);
    float volume = 1.f;
    float time = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent(); // 값을 받아온다.
        mWeatherAlarm = intent.getBooleanExtra("weather_alarm", false);

        if(mWeatherAlarm)
            setContentView(R.layout.activity_timeout_weather);
        else
            setContentView(R.layout.activity_timeout);

        musicInit();

        textInit();

    }

    public void btnClickTimeOut(View v){
        switch (v.getId()){
            case R.id.btn_goto_weatherquiz:
                Intent i = new Intent(TimeoutActivity.this, WeatherQuizActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btn_end_alarm:
                stopAlarm();
                finish();
                break;
        }
    }

    private void musicInit(){
        if(mWeatherAlarm){
            timer.scheduleAtFixedRate(new UpdateTimeTask(), 3500, 100);

            music = MediaPlayer.create(this, R.raw.love_passion);
            music.setLooping(true);
            music.start();
            music.setVolume(1.0f, 1.0f);

            myTTS = new TextToSpeech(this, this);
            final String city = "Seoul,KR";
            final String lang = "en";

            task.execute(new String[]{city, lang});
            task1.execute(new String[]{city, lang, "3"});
        }
        else{
            music = MediaPlayer.create(this, R.raw.love_passion);
            music.setLooping(true);
            music.start();
            music.setVolume(1.0f, 1.0f);
        }

    }

    private void textInit(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        TextView nowTimeHour = (TextView)findViewById(R.id.tNowTimeHour);

        SimpleDateFormat sdfHour = new SimpleDateFormat("hh");
        String strHour= sdfHour.format(date);

        nowTimeHour.setText(strHour);

        TextView nowTimeMinute = (TextView)findViewById(R.id.tNowTimeMinute);

        SimpleDateFormat sdfMinute = new SimpleDateFormat("mm");
        String strMinute= sdfMinute.format(date);

        nowTimeMinute.setText(strMinute);
    }

    private void stopAlarm() {
        music.stop();

        if(mWeatherAlarm)
            myTTS.stop();
    }

    public void SpeeachWeather(WeatherForecast speechData)
    {
        DayForecast today = speechData.getForecast(0);
        String weather = today.weather.currentCondition.getDescr();
        int temp_day = (int)((float) (today.forecastTemp.day - 273.15));
        int temp_min = (int)((float) (today.forecastTemp.min - 273.15));
        int temp_max = (int)((float) (today.forecastTemp.max - 273.15));

        int date = today.getintDate();
        int time = today.getTimeData();

        if( weather.equals("sky is clear") )
        {
            weather = "맑음";
        }

        String temp = date/100 + "월 " + date%100 + "일, " + time + "시의 서울 날씨는, " + weather + ", 입니다. ";
        myTTS.speak(temp, TextToSpeech.QUEUE_FLUSH, null);
        temp = ", 기온은 섭씨 " + temp_day + "도, " + "최저기온은 " + temp_min + "도, " + "최고기온은 " + temp_max + "도, 입니다.";
        myTTS.speak(temp, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void onInit(int status) {
        myTTS.setLanguage(Locale.KOREA);
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherHttpClient()).getWeatherData(params[0],params[1]));
           /* try {
               // weather = JSONWeatherParser.getWeather(data);
                // Let's retrieve the icon
                weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

        }
    }

    private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {

        @Override
        protected WeatherForecast doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getForecastWeatherData(params[0], params[1], params[2]));
            WeatherForecast forecast = new WeatherForecast();
            /*try {
                forecast = JSONWeatherParser.getForecastWeather(data);
                System.out.println("Weather [" + forecast + "]");
                // Let's retrieve the icon
                //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            return forecast;
        }
    }

    private class UpdateTimeTask extends TimerTask {
        private boolean briefTag = true;

        private void briefStart()
        {
            WeatherForecast speechData;
            try {
                speechData = task1.get();
                SpeeachWeather(speechData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            final float BRTIME = 12.0f;

            time += 0.1f;
            if(time > 15.0f + BRTIME){
                time = 0.f;
                briefTag = true;
            }

            if( time > 5.0f + BRTIME )
            {
                volume += 0.01f; if(volume > 1.0f) volume = 1.0f;
            }

            else if( time >= 0.0f )
            {
                volume -= 0.02f;
                if(volume <= 0.00f)
                {
                    volume = 0.00f;
                    if(briefTag == true)
                    {
                        briefTag = false;
                        //breif start
                        briefStart();
                    }
                }
            }

            music.setVolume( volume, volume );
        }
    }
}
