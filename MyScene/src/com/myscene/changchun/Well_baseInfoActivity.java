package com.myscene.changchun;

import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MapActivity;
import com.myscene.adapter.BaseAutoCompeleteAdapter;
import com.myscene.adapter.BaseWorkAdapter;
import com.myscene.adapter.SuggAdapter;
import com.myscene.data.BaseInfo;
import com.myscene.data.DAO;
import com.myscene.data.DBHelper;
import com.myscene.data.Suggestion;
import com.myscene.data.Util;

import android.location.Location;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Well_baseInfoActivity extends MapActivity {

    private MyScene app;
    private EditText project_gps;
    private LocationListener loc_listener;
    private Button back;
    private Button base_save;
    private EditText project_id;
    private EditText well_num;
    private EditText base_date;
    private EditText base_weather;
    private AutoCompleteTextView base_work;
    private EditText base_depth;
    private EditText base_mradis;
    private EditText base_radis;
    private EditText base_way;
    private EditText base_writer;
    private EditText base_first_depth;
    // private boolean flag; // 保存是否本地已有基本信息
    private DAO dao;
    private ProgressDialog prgDialog;
    private BaseInfo local_baseinfo;
    private BaseAutoCompeleteAdapter adapter;
    static long lastModify = 0; //上次gps定位时间，目前 十分钟定位 修改一次
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_base_info);
        project_gps = (EditText) findViewById(R.id.project_gps);
        back = (Button) findViewById(R.id.base_back);
        base_save = (Button) findViewById(R.id.base_save);
        well_num = (EditText) findViewById(R.id.well_num);
        base_weather = (EditText) findViewById(R.id.base_weather);
        base_date = (EditText) findViewById(R.id.base_date);
        base_date.setText(Util.getDate());
        base_depth = (EditText) findViewById(R.id.base_depth);
        base_first_depth = (EditText) findViewById(R.id.base_first_depth);
        base_mradis = (EditText) findViewById(R.id.base_mradis);
        base_radis = (EditText) findViewById(R.id.base_radis);
        base_way = (EditText) findViewById(R.id.base_dig_method);
        base_work = (AutoCompleteTextView) findViewById(R.id.base_work);
        base_writer = (EditText) findViewById(R.id.base_writer);
        project_id = (EditText) findViewById(R.id.project_id);
        dao = new DAO(this);
        adapter = new BaseAutoCompeleteAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, null, DBHelper.SUG_TABLE, android.R.id.text1);
        base_work.setThreshold(1);
        base_work.setAdapter(adapter);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("back clcik");
                finish();
            }
        });
        base_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String well_num_ = well_num.getText().toString();
                String base_date_ = base_date.getText().toString();
                String base_weather_ = base_weather.getText().toString();
                String base_work_ = base_work.getText().toString();
                String base_depth_ = base_depth.getText().toString();
                String base_gps_ = project_gps.getText().toString();
                String base_mradis_ = base_mradis.getText().toString();
                String base_radis_ = base_radis.getText().toString();
                String base_way_ = base_way.getText().toString();
                String base_writer_ = base_writer.getText().toString();
                String base_first_depth_ = base_first_depth.getText().toString();
                if (well_num_ == null || well_num_.trim().equals("")) {
                    toast("请填写井号");
                    return;
                }

                if (local_baseinfo != null
                        && dao.checkInBaseinfo(local_baseinfo.get_id() + "", local_baseinfo.getProject_id(), well_num_)) {
                    toast("该项目下该井号已经存在，请修改");
                    return;
                }

                BaseInfo baseinfo = new BaseInfo();
                baseinfo.setBase_date(base_date_);
                baseinfo.setBase_depth(base_depth_);
                baseinfo.setBase_first_depth(base_first_depth_);
                baseinfo.setBase_gps(base_gps_);
                baseinfo.setBase_mradis(base_mradis_);
                baseinfo.setBase_radis(base_radis_);
                baseinfo.setBase_way(base_way_);
                baseinfo.setBase_weather(base_weather_);
                baseinfo.setWell_num(well_num_);
                baseinfo.setBase_work(base_work_);
                baseinfo.setBase_writer(base_writer_);
                baseinfo.setCreate_time(Util.getDate());
                baseinfo.setProject_id(CreateActivity.projectId);
                boolean flags;
                if (null == local_baseinfo) {
                    flags = dao.insertBaseInfo(baseinfo);
                    if (flags) {
                        toast("保存成功");
                        WellActivity.well_num = well_num_;
                        finish();
                    } else {
                        toast("抱歉，保存失败");
                    }
                } else {

                    if (well_num_.equals(WellActivity.well_num)) {
                        flags = dao.updateBaseInfo(local_baseinfo.get_id(), baseinfo, false, WellActivity.well_num);
                    } else
                        showProgressDialog();
                    flags = dao.updateBaseInfo(local_baseinfo.get_id(), baseinfo, true, WellActivity.well_num);
                    dismissDialog();
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
        app = (MyScene) this.getApplication();
        if (app.mBMapMan == null) {
            app.mBMapMan = new BMapManager(getApplication());
            app.mBMapMan.init(app.mStrKey, new MyScene.MyGeneralListener());
        }
        app.mBMapMan.start();
        // 如果使用地图SDK，请初始化地图Activity
        // super.initMapActivity(app.mBMapMan);
        // 注册定位事件
        loc_listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    String strLog = String.format("您当前的位置:\r\n" + "纬度:%f\r\n" + "经度:%f", location.getLongitude(),
                            location.getLatitude());
                    Util.Print("location", strLog);
                    Util.Print("location", location.toString());
                    if((System.currentTimeMillis() -lastModify) >10*60*1000){
                    	project_gps.setText(location.getLatitude() + "," + location.getLongitude());
                    	lastModify = System.currentTimeMillis();
                    }
                    // Toast.makeText(MainActivity.this,
                    // "location:"+location.toString(),
                    // Toast.LENGTH_LONG).show();
                } else
                    System.out.println("location is null");
            }
            
        };
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.left, R.anim.right);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (dao == null) {
            dao = new DAO(this);
            dao.open();
        }
//        fillData();
        System.out.println("on start");
    }

    public void toast(String text) {
        Toast.makeText(Well_baseInfoActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (dao != null) {
            dao.close();
            dao = null;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (dao != null) {
            dao.close();
            dao = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_base_info, menu);
        return true;
    }

    // @Override
    // protected boolean isRouteDisplayed() {
    // // TODO Auto-generated method stub
    // return false;
    // }

    @Override
    protected void onPause() {
        if (null == app)
            app = (MyScene) this.getApplication();
        app.mBMapMan.getLocationManager().removeUpdates(loc_listener);
        app.mBMapMan.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
    	System.out.println("on resume");
        if (null == app)
            app = (MyScene) this.getApplication();
        app.mBMapMan.start();
        app.mBMapMan.getLocationManager().requestLocationUpdates(loc_listener);// 定位
        if (dao == null) {
            dao = new DAO(this);
        }
        dao.open();
        fillData();
        List<Suggestion> sugs = dao.getAllSugs("");
        System.out.println("sug size:" + sugs.size());
        // BaseWorkAdapter adapter = new
        // BaseWorkAdapter(Well_baseInfoActivity.this, sugs);
        SuggAdapter adapter = new SuggAdapter(this, android.R.layout.simple_dropdown_item_1line, sugs);
        base_work.setAdapter(adapter);
        super.onResume();
    }

    /**
     * 如果 数据库中 有此井号的信息，那么填写次井号的基本内容
     */
    public void fillData() {
        local_baseinfo = dao.getBaseInfo(CreateActivity.projectId, WellActivity.well_colunm_id);
        System.out.println("well num:" + WellActivity.well_num);
        if (null != local_baseinfo) {
            Util.Print("baseinfo", "load local data");
            Util.Print("baseinfo", local_baseinfo.toFile());
            well_num.setText(local_baseinfo.getWell_num());
            project_gps.setText(local_baseinfo.getBase_gps());
            base_weather.setText(local_baseinfo.getBase_weather());
            base_date.setText(local_baseinfo.getBase_date());
            base_depth.setText(local_baseinfo.getBase_depth());
            base_first_depth.setText(local_baseinfo.getBase_first_depth());
            base_mradis.setText(local_baseinfo.getBase_mradis());
            base_radis.setText(local_baseinfo.getBase_radis());
            base_way.setText(local_baseinfo.getBase_way());
            base_work.setText(local_baseinfo.getBase_work());
            base_writer.setText(local_baseinfo.getBase_writer());
            project_id.setText(local_baseinfo.getProject_id());
        } else {
            Util.Print("baseinfo", "null");
        }
        if (WellActivity.well_num != null) {
            well_num.setText(WellActivity.well_num);
        }
        project_id.setText(CreateActivity.projectId);
    }

    public void showProgressDialog() {
        if (prgDialog == null) {
            prgDialog = new ProgressDialog(Well_baseInfoActivity.this);
            prgDialog.setTitle("请稍等");
            prgDialog.setMessage("正在修改数据");
            prgDialog.setCancelable(true);
            prgDialog.show();
        }
    }

    public void dismissDialog() {
        try {
            if (prgDialog != null) {
                prgDialog.dismiss();
                prgDialog = null;
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

    }

    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }
}
