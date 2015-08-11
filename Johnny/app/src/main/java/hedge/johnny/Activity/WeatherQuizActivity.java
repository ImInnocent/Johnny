package hedge.johnny.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hedge.johnny.R;


/**
 * Created by EDGE01 on 2015-07-24.
 */
public class WeatherQuizActivity  extends Activity {
    private String iSelectedWeather = null;
    private String iInputedWeather = null;
    Button bWSelect;
    EditText eWInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_quiz);

        bWSelect = (Button)findViewById(R.id.bWeatherSelect);
        eWInput = (EditText)findViewById(R.id.eWeatherInput);
    }

    public void button(View v)
    {
        switch (v.getId())
        {
            case R.id.bSunny:
                iSelectedWeather = "sunny";
                bWSelect.setText("맑음");
                break;
            case R.id.bCloud:
                iSelectedWeather = "cloud";
                bWSelect.setText("구름");
                break;
            case R.id.bRainy:
                iSelectedWeather = "rainy";
                bWSelect.setText("비옴");
                break;
            case R.id.bSnowy:
                iSelectedWeather = "snowy";
                bWSelect.setText("눈옴");
                break;
            case R.id.bWeatherSelect:
                iInputedWeather = eWInput.getText().toString();
                if( iSelectedWeather.equals(iInputedWeather) )
                {
                    //음악 끄기
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "틀렸습니다", Toast.LENGTH_LONG);
                }
                break;
        }
    }
}
