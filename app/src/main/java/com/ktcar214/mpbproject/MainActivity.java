package com.ktcar214.mpbproject;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    //내부 변수
    SharedPreferences pref;
    MilitaryType mil;
    Integer addedDuration;
    Long remaining;
    Calendar startDate = Calendar.getInstance(), endDate;
    Intent i;
    //UI 관련
    TextView tv_d, tv_t, tv_s, tv_m, tv_p;
    CountDown timer_tv;
    ProgressBar p;

    //----------생명 주기-----------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        tv_d = findViewById(R.id.tv_timeD);
        tv_t=findViewById(R.id.tv_timeT);
        tv_s=findViewById(R.id.tv_timeS);
        tv_m=findViewById(R.id.tv_timeM);
        tv_p=findViewById(R.id.tv_timeP);
        p=findViewById(R.id.progressBar);
        if (pref.getString("Military", null) == null) {
            openSetupActivity();
        } else {
            start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            assert data != null;
        } catch (AssertionError e) {
            start();
            e.printStackTrace();
            return;
        }
        mil=(MilitaryType) data.getSerializableExtra("Military");
        startDate.set(data.getIntExtra("startYear",1970),data.getIntExtra("startMonth",1),data.getIntExtra("startDate",1));
        save();
        start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }
    /*--------------메뉴-----------------*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater m=getMenuInflater();
        m.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.reset) openSetupActivity();
        return super.onOptionsItemSelected(item);
    }

    /*--------------함수-----------------*/
    private void load() {
        /*
        3가지 설정값 불러오기
        1. 군종
        2. 추가된 일자
        3. 시작일자
         */
        mil = MilitaryType.valueOf(pref.getString("Military", "UNKNOWN"));
        startDate.set(pref.getInt("startYear", 1970), pref.getInt("startMonth", 1), pref.getInt("startDay", 1), 0, 0, 0);
        Log.i("StartDate", startDate.getTime().toString());
        endDate = Calendar.getInstance();
        endDate.set(pref.getInt("startYear", 1970), pref.getInt("startMonth", 1), pref.getInt("startDay", 1), 0, 0, 0);
        endDate.add(Calendar.YEAR, mil.getServiceYear());
        endDate.add(Calendar.MONTH, mil.getServiceMonth());
        endDate.add(Calendar.DATE,-1);//초일산입
        Log.i("EndDate", endDate.getTime().toString());
    }

    private void save() {
        SharedPreferences.Editor e = pref.edit();
        e.putString("Military", mil.name());
        e.putInt("startYear", startDate.get(Calendar.YEAR));
        e.putInt("startMonth", startDate.get(Calendar.MONTH));
        e.putInt("startDay", startDate.get(Calendar.DATE));
        e.apply();
    }
    private void openSetupActivity(){
        i = new Intent(getApplicationContext(), SetupWizardActivity.class);
        startActivityForResult(i, 0);
    }
    private void start(){
        load();
        //만약 입대일이 미래라면 일자 대신 "입대 전" 표기
        if(startDate.compareTo(Calendar.getInstance())==1){
            if(timer_tv !=null) {
                timer_tv.cancel();
            }
            tv_s.setText(
                    "입대일: "
                    +startDate.get(Calendar.YEAR)+"년 "
                    +(startDate.get(Calendar.MONTH)+1)+"월 "
                    +startDate.get(Calendar.DATE)+"일");
            tv_d.setTextSize(Dimension.SP,120);
            String remainingDates="복무일: "+ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant())+"일";
            tv_t.setText(remainingDates);
            tv_d.setText("입대 전");
            tv_m.setText("");
            tv_p.setText("");
            p.setProgress(0);
        }
        else{
            if(timer_tv !=null) {
                timer_tv.cancel();
            }
            tv_d.setTextSize(Dimension.SP,80);
            remaining = endDate.getTimeInMillis() - System.currentTimeMillis();
            timer_tv=new CountDown(remaining,1);
            timer_tv.start();
        }
    }
    class CountDown extends CountDownTimer {
        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            Float progress= (float) l/(endDate.getTimeInMillis()-startDate.getTimeInMillis());
            Float percentage=(1-progress)*100;
            Float pw=(percentage*100000);
            p.setMax(10000000);
            tv_p.setText(percentage+"%");
            p.setProgress(pw.intValue());
            String remaining;
            Long remainingDays=l/86400000;
            Long remainingHours=l%86400000/3600000;
            Long remainingMinutes=l%86400000%3600000/60000;
            Long remainingSeconds=l%86400000%3600000%60000/1000;
            Long remainingMilis=l%1000;
            tv_d.setText(remainingDays +" 일");
            tv_t.setText(remainingHours+"시간 "+remainingMinutes+"분");
            tv_s.setText(remainingSeconds+ "초");
            tv_m.setText(remainingMilis.toString());
        }

        @Override
        public void onFinish() {
        }
    }
}


