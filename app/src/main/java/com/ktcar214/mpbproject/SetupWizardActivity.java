package com.ktcar214.mpbproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

public class SetupWizardActivity extends AppCompatActivity {
    private enum settingStatus {
        NONE, STARTDATE, MILTYPE
    }

    CalendarView cv;
    Button b;
    RadioGroup r;
    TextView t;
    Calendar ct;
    settingStatus s = settingStatus.NONE;
    MilitaryType mt;
    Intent ii = getIntent();
    Intent io;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_wizard);
        cv = findViewById(R.id.calendar);
        r = findViewById(R.id.rg);
        b = findViewById(R.id.b_proceed);
        t = findViewById(R.id.tv_setting);
        io= new Intent(getApplicationContext(), MainActivity.class);

        cv.setVisibility(View.VISIBLE);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                //ct.set(i, i1, i2);
                io.putExtra("startYear",i);
                io.putExtra("startMonth",i1);
                io.putExtra("startDate",i2);
                s = settingStatus.STARTDATE;
                b.setEnabled(true);
            }
        });
        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (r.getCheckedRadioButtonId()) {
                    case R.id.rb_army:
                        mt = MilitaryType.ARMY;
                        break;
                    case R.id.rb_navy:
                        mt = MilitaryType.NAVY;
                        break;
                    case R.id.rb_marine:
                        mt = MilitaryType.MARINE;
                        break;
                    case R.id.rb_af:
                        mt = MilitaryType.AIRFORCE;
                        break;
                    case R.id.rb_socialsvc:
                        mt = MilitaryType.SOCIALSVC;
                        break;
                }
                s=settingStatus.MILTYPE;
                b.setEnabled(true);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (s) {
                    case STARTDATE:
                        cv.setVisibility(View.GONE);
                        r.setVisibility(View.VISIBLE);
                        t.setText("군종 지정");
                        b.setEnabled(false);
                        b.setText("마침");
                        break;
                    case MILTYPE:
                        io.putExtra("Military", mt);
                        setResult(RESULT_OK,io);
                        finish();
                }
            }
        });
    }
}