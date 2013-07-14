package com.myscene.changchun;

import com.myscene.data.DAO;
import com.myscene.data.Dirt;
import com.myscene.data.Pid;
import com.myscene.data.Util;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class Well_Dig_Pid extends Activity {

    private Button back;
    private Button save;
    private DAO dao;
    private Pid pid;
    private EditText pid_depth;
    private EditText pid_value;
    private EditText pid_memo;
    private EditText pid_extra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_dig_pid);
        if (WellActivity.well_num == null || WellActivity.well_num.equals("")) {
            Toast.makeText(this, "请先在基本信息中填写井号!", Toast.LENGTH_SHORT).show();
        }
        back = (Button) findViewById(R.id.pid_back);
        save = (Button) findViewById(R.id.pid_save);
        pid_depth = (EditText) findViewById(R.id.pid_depth);
        pid_value = (EditText) findViewById(R.id.pid_value);
        pid_memo = (EditText) findViewById(R.id.pid_memo);
        pid_extra = (EditText) findViewById(R.id.pid_extra);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        dao = new DAO(this);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (WellActivity.well_num == null || WellActivity.well_num.equals("")) {
                    Toast.makeText(Well_Dig_Pid.this, "请先在基本信息中填写井号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pid_value_ = pid_value.getText().toString();
                String pid_depth_ = pid_depth.getText().toString();
                String pid_memo_ = pid_memo.getText().toString();
                String pid_extra_ = pid_extra.getText().toString();
                Pid p = new Pid();
                p.setWell_num(WellActivity.well_num);
                p.setProject_id(CreateActivity.projectId);
                p.setCreate_time(Util.getDate());
                p.setPid_depth(pid_depth_);
                p.setPid_memo(pid_memo_);
                p.setPid_value(pid_value_);
                p.setPid_extra(pid_extra_);
                Util.Print("Well pid", p.toString());
                if (pid != null) {
                    // update
                    Util.Print("Well Dig", "update pid");
                    if (dao.updatePid(pid.get_id(), p)) {
                        Toast.makeText(Well_Dig_Pid.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(Well_Dig_Pid.this, "抱歉，修改失败", Toast.LENGTH_SHORT).show();
                } else {
                    // insert
                    Util.Print("Well Dig", "insert dirt");
                    if (dao.insertPid(p)) {
                        Toast.makeText(Well_Dig_Pid.this, "保存成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Well_Dig_Pid.this, "抱歉，保存失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.left, R.anim.right);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (dao != null) {
            dao.close();
            dao = null;
        }
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

    public void fillData() {
        Intent intent = getIntent();
        if (null != intent) {
            String action = intent.getAction();
            if (action != null && action.equals("pid")) {
                System.out.println("pid fill data");
                String _id = "" + intent.getExtras().getInt("_id");
                pid = dao.getPid(_id);
                if (null != pid) {
                    pid_depth.setText(pid.getPid_depth());
                    pid_value.setText(pid.getPid_value());
                    pid_memo.setText(pid.getPid_memo());
                    pid_extra.setText(pid.getPid_extra());
                }
                intent.setAction("");
                intent.removeExtra("dirt");
            } else {
                Util.Print("well pid", "null intent");
            }
        }
    }
}
