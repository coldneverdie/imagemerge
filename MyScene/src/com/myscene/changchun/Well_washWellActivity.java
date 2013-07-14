package com.myscene.changchun;

import com.myscene.data.DAO;
import com.myscene.data.Util;
import com.myscene.data.Washwell;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Well_washWellActivity extends Activity {

    private Button wash_back;
    private Button wash_save;
    private EditText well_num;
    private EditText wash_date;
    private EditText wash_time;
    private EditText wash_weather;
    private EditText wash_method;
    private EditText wash_temp;
    private EditText wash_cond;
    private EditText wash_ph;
    private EditText wash_water;
    private EditText wash_gaocheng;
    private Washwell local_washwell;
    private EditText project_id;
    private DAO dao;
    private boolean flag;// 控制是否已经有记录

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_wash_well);
        if (WellActivity.well_num == null || WellActivity.well_num.equals("")) {
            Toast.makeText(this, "请先在基本信息中填写井号!", Toast.LENGTH_SHORT).show();
        }
        well_num = (EditText) findViewById(R.id.well_num);
        wash_back = (Button) findViewById(R.id.wash_back);
        wash_save = (Button) findViewById(R.id.wash_save);
        wash_date = (EditText) findViewById(R.id.wash_date);
        wash_gaocheng = (EditText) findViewById(R.id.wash_gaocheng);
        wash_method = (EditText) findViewById(R.id.wash_method);
        wash_ph = (EditText) findViewById(R.id.wash_ph);
        wash_temp = (EditText) findViewById(R.id.wash_temp);
        wash_time = (EditText) findViewById(R.id.wash_time);
        wash_water = (EditText) findViewById(R.id.wash_water);
        wash_weather = (EditText) findViewById(R.id.wash_weather);
        wash_cond = (EditText) findViewById(R.id.wash_cond);
        project_id = (EditText) findViewById(R.id.project_id);
        dao = new DAO(this);
        wash_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String well_num_ = WellActivity.well_num;
                if (well_num_ == null || well_num_.trim().equals("")) {
                    toast("请先在基本信息中填写井号");
                    return;
                }
                String wash_date_ = wash_date.getText().toString();
                String wash_gaocheng_ = wash_gaocheng.getText().toString();
                String wash_method_ = wash_method.getText().toString();
                String wash_ph_ = wash_ph.getText().toString();
                String wash_temp_ = wash_temp.getText().toString();
                String wash_time_ = wash_time.getText().toString();
                String wash_cond_ = wash_cond.getText().toString();
                String wash_water_ = wash_water.getText().toString();
                String wash_weather_ = wash_weather.getText().toString();
                Washwell ww = new Washwell();
                if (local_washwell != null)
                    ww.set_id(local_washwell.get_id());
                ww.setProject_id(CreateActivity.projectId);
                ww.setWell_num(well_num_);
                ww.setWw_cond(wash_cond_);
                ww.setWw_date(wash_date_);
                ww.setWw_gaocheng(wash_gaocheng_);
                ww.setWw_method(wash_method_);
                ww.setWw_ph(wash_ph_);
                ww.setWw_temp(wash_temp_);
                ww.setWw_time(wash_time_);
                ww.setWw_water(wash_water_);
                ww.setWw_weather(wash_weather_);
                boolean flags;
                if (!flag) {
                    flags = dao.insertWashWell(ww);
                    if (flags) {
                        toast("保存成功");
                        finish();
                    } else {
                        toast("抱歉，保存失败");
                    }
                } else {
                    flags = dao.updateWashWell(local_washwell.get_id(), ww);
                    if (flags) {
                        WellActivity.well_num = well_num_;
                        toast("修改成功");
                        finish();
                    } else {
                        toast("抱歉，修改失败");
                    }
                }
            }
        });
        wash_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        dao.close();
        dao = null;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (dao == null) {
            dao = new DAO(this);
            dao.open();
        }
        fillData();
    }

    public void toast(String text) {
        Toast.makeText(Well_washWellActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 如果 数据库中 有此井号的信息，那么填写次井号的基本内容
     */
    public void fillData() {
        System.out.println("washwell num:" + WellActivity.well_num);
        local_washwell = dao.getWashWell(CreateActivity.projectId, WellActivity.well_num);
        if (null != local_washwell) {
            flag = true;
            Util.Print("wash well", "load local data");
            System.out.println(local_washwell.toString());
            well_num.setText(local_washwell.getWell_num());
            project_id.setText(local_washwell.getProject_id());
            wash_date.setText(local_washwell.getWw_date());
            wash_gaocheng.setText(local_washwell.getWw_gaocheng());
            wash_method.setText(local_washwell.getWw_method());
            wash_ph.setText(local_washwell.getWw_ph());
            wash_temp.setText(local_washwell.getWw_temp());
            wash_time.setText(local_washwell.getWw_time());
            wash_water.setText(local_washwell.getWw_water());
            wash_cond.setText(local_washwell.getWw_cond());
            wash_weather.setText(local_washwell.getWw_weather());
        } else {
            flag = false;
            Util.Print("wash wash", "null");
            if (WellActivity.well_num != null) {
                well_num.setText(WellActivity.well_num);
            }
            project_id.setText(CreateActivity.projectId);
            wash_date.setText(Util.getDate());
            // wash_time.setText(Util.getTime());
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.left, R.anim.right);
    }
}
