package hedge.johnny.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import hedge.johnny.R;

/**
 * Created by Administrator on 2015-08-08.
 */
public class InitialActivity extends Activity {
    Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(InitialActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1200);
    }

    @Override
    protected void onDestroy(){
        mTimer.cancel();
        super.onDestroy();
    }
}
