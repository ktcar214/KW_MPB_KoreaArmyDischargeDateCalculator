package com.ktcar214.mpbproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    SharedPreferences pref;
    MilitaryType mil;
    Integer addedDuration;
    DateFormat dateFormat = DateFormat.getDateInstance();
    Calendar startDate = Calendar.getInstance();
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        if(pref.getString("Military",null) == null) {
            i=new Intent(getApplicationContext(), SetupWizardActivity.class);
            startActivityForResult(i,0);
        }
        else {
            load();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        mil=(MilitaryType) data.getSerializableExtra("Military");
        startDate.set(data.getIntExtra("startYear",1970),data.getIntExtra("startMonth",1),data.getIntExtra("startDate",1));
        save();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*--------------함수-----------------*/
    private void load() {
        /*
        3가지 설정값 불러오기
        1. 군종
        2. 추가된 일자
        3. 시작일자
         */
        mil=MilitaryType.valueOf(pref.getString("Military",null));
        startDate.set(pref.getInt("startYear",1970),pref.getInt("startMonth",1),pref.getInt("startDate",1));
        addedDuration=pref.getInt("addedDuration",0);
    }

    private void loadtime() throws ParseException {
        String t = pref.getString("startDate", "");
        if (!t.equals("")) startDate.setTime(dateFormat.parse(t));
    }

    private void save() {
        SharedPreferences.Editor e= pref.edit();
        e.putString("Military",mil.name());
        e.putInt("startYear",startDate.get(Calendar.YEAR));
        e.putInt("startMonth",startDate.get(Calendar.MONTH));
        e.putInt("startDay",startDate.get(Calendar.DATE));
        e.apply();
    }
}
