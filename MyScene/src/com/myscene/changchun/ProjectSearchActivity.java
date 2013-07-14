package com.myscene.changchun;

import com.myscene.data.Util;
import com.myscene.net.GetData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 搜索项目页面
 * 
 * @author liudongqi
 * 
 */
public class ProjectSearchActivity extends Activity {

    private EditText pro_name;
    private EditText pro_id;
    private ProgressDialog prgDialog;
    private Button pro_back;
    private Button pro_search;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
            case 0:
                Toast.makeText(ProjectSearchActivity.this, "抱歉，搜索失败", Toast.LENGTH_SHORT).show();
                dismissDialog();
                break;
            case 1:
                dismissDialog();
                if (msg.obj.toString().contains("project")) {
                    Toast.makeText(ProjectSearchActivity.this, "搜索数据成功", Toast.LENGTH_SHORT).show();
                    GetData.putToLocal(msg.obj.toString(), ProjectSearchActivity.this, handler);
                } else {
                    Toast.makeText(ProjectSearchActivity.this, "未搜到该项目", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                finish();
                break;
            case 3:
                break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.left, R.anim.right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project);

        pro_id = (EditText) findViewById(R.id.project_id);
        pro_name = (EditText) findViewById(R.id.project_name);

        pro_back = (Button) findViewById(R.id.pro_back);
        pro_search = (Button) findViewById(R.id.pro_search);

        pro_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        pro_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final String project_id = pro_id.getText().toString().trim();
                final String project_name = pro_name.getText().toString().trim();
                if ("".equals(project_id) && "".equals(project_name)) {
                    Toast.makeText(ProjectSearchActivity.this, "请输入项目id或者项目名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                // new Thread(){
                //
                // public void run(){
                // Looper.prepare();
                showProgressDialog();
                Util.Print("project name", project_name);
                Util.Print("project id", project_id);
                GetData.getProjectData(ProjectSearchActivity.this, project_id, project_name, handler);
                // }
                // }.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    public synchronized void showProgressDialog() {
        Util.Print(this.getClass().getSimpleName(), "显示进度条");
        if (prgDialog == null) {
            prgDialog = new ProgressDialog(ProjectSearchActivity.this);
            prgDialog.setTitle("请稍等");
            prgDialog.setMessage("正在搜索数据...");
            prgDialog.setCancelable(true);
            prgDialog.show();
        } else
            prgDialog.show();
    }

    public synchronized void dismissDialog() {
        Util.Print(this.getClass().getSimpleName(), "结束进度条");
        try {
            if (prgDialog != null) {
                prgDialog.dismiss();
                prgDialog = null;
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
