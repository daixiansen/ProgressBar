package daixiansen.customdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private HorizontalProgress hpprogress;
    private static final int FLAG = 1;
    private RoundProgress progress_3;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int progress = hpprogress.getProgress();
            int progress2 = progress_2.getProgress();
            int progress3 = progress_3.getProgress();
            hpprogress.setProgress(++progress);
            progress_2.setProgress(++progress2);
            progress_3.setProgress(++progress3);
            if(progress >= 100){
                mHandler.removeMessages(FLAG);
            }
            mHandler.sendEmptyMessageDelayed(FLAG,100);
        }
    };
    private HorizontalProgress progress_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.hpprogress = (HorizontalProgress) findViewById(R.id.hp_progress);
        progress_2 = (HorizontalProgress) findViewById(R.id.progress_2);
        progress_3 = (RoundProgress) findViewById(R.id.progress_3);
        mHandler.sendEmptyMessageDelayed(FLAG,100);
    }
}
