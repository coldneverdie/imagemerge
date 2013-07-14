package com.myscene.changchun;

import com.myscene.data.Util;

import android.os.Bundle;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class WellActivity extends Activity implements OnClickListener {

    private Button well_baseinfo;
    private Button well_digwell;
    private Button well_washwell;
    private Button well_back;
    public static String well_colunm_id;// 保存过来的井号
    public static String well_num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well);
        well_baseinfo = (Button) findViewById(R.id.create_baseinfo);
        well_digwell = (Button) findViewById(R.id.create_digwell);
        well_washwell = (Button) findViewById(R.id.create_washwell);
        well_back = (Button) findViewById(R.id.well_back);
        well_baseinfo.setOnClickListener(this);
        well_digwell.setOnClickListener(this);
        well_washwell.setOnClickListener(this);
        well_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        switch (v.getId()) {
        case R.id.create_baseinfo:
            intent.setClass(WellActivity.this, Well_baseInfoActivity.class);
            break;
        case R.id.create_digwell:
            intent.setClass(WellActivity.this, Well_digWellActivity.class);
            break;
        case R.id.create_washwell:
            intent.setClass(WellActivity.this, Well_washWellActivity.class);
            break;
        case R.id.well_back:
            finish();
        }
        startActivity(intent);
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
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.left, R.anim.right);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Util.Print("well activity", "onresume");
        Intent intent = getIntent();
        if (null != intent) {
            String action = intent.getAction();
            if (null != action && action.equals("project_local")) {
                well_colunm_id = "" + intent.getExtras().getInt("well_colunm_id");
                well_num = intent.getExtras().getString("well_num");
                intent.setAction("");
                intent.removeExtra("well_colunm_id");
                intent.removeExtra("well_num");
            }
        }
        Util.Print("well_colunm_id is null?", "" + (well_colunm_id == null));
        Util.Print("well_num is null?", "" + (well_num == null));
    }

}
