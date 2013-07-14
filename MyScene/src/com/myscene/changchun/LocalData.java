package com.myscene.changchun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.myscene.adapter.LocalListAdapter;
import com.myscene.data.DAO;
import com.myscene.data.Project;
import com.myscene.data.Util;
import com.myscene.net.Upload;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LocalData extends Activity {

    ListView local_lsit;
    DAO dao;
    LocalListAdapter localAdapter;
    TextView local_none;
    private ProgressDialog prgDialog;
    private View contextMenuView;
    private AlertDialog contextMenuDialog;
    private Button local_back;
    private Button local_upload;
    AlertDialog dlg;
    static List<Project> projects;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
            case 0:
                Toast.makeText(LocalData.this, "抱歉，上传失败", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(LocalData.this, "上传成功", Toast.LENGTH_SHORT).show();
                break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_data);
        local_back = (Button) findViewById(R.id.local_back);
        local_upload = (Button) findViewById(R.id.local_upload);
        local_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        local_upload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 上传
                if (Util.checkInternet(LocalData.this)) {
                    if (null == projects || projects.size() == 0) {
                        Toast.makeText(LocalData.this, "本地数据为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showProgressDialog();
                    doUpload();
                } else {
                    Toast.makeText(LocalData.this, "请连接网络", Toast.LENGTH_SHORT).show();
                }

            }
        });

        local_lsit = (ListView) findViewById(R.id.local_lsit);
        // registerForContextMenu(local_lsit);
        local_lsit.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long id) {
                // TODO Auto-generated method stub
                if (null == dlg)
                    dlg = new AlertDialog.Builder(LocalData.this).create();
                dlg.show();
                final Window dlgwin = dlg.getWindow();
                dlgwin.setContentView(R.layout.list_menu);
                Button edit = (Button) dlgwin.findViewById(R.id.button_edit);
                Button del = (Button) dlgwin.findViewById(R.id.button_delete);
                edit.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(LocalData.this, ProjectCreateActivity.class);
                        intent.setAction("project_edit");
                        intent.putExtra("_id", projects.get(position).get_id());
                        intent.putExtra("project_id", projects.get(position).getProject_id());
                        intent.putExtra("project_name", projects.get(position).getProject_name());
                        startActivity(intent);
                        dlg.cancel();
                    }
                });
                del.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        AlertDialog.Builder builder = new AlertDialog.Builder(LocalData.this);
                        builder.setTitle("确定要删除?")
                                .setPositiveButton(getString(R.string.button_ok),
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO Auto-generated method
                                                // stub
                                                // dao.deleteProject(""+projects.get(position).get_id());
                                                dao.deleteProjectData(LocalData.this, projects.get(position)
                                                        .getProject_id());
                                                fillData();
                                                Toast.makeText(LocalData.this, "删除成功", Toast.LENGTH_SHORT).show();
                                                dlg.cancel();
                                                dialog.cancel();
                                            }
                                        })
                                .setNegativeButton(getString(R.string.button_cancel),
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO Auto-generated method
                                                // stub
                                                dialog.cancel();
                                                dlg.cancel();
                                            }
                                        }).setCancelable(true);
                        builder.create();
                        builder.show();
                    }
                });
                return false;
            }

        });
        local_lsit.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(LocalData.this, CreateActivity.class);
                intent.setAction("project_edit");
                intent.putExtra("project_id", projects.get(position).getProject_id());
                intent.putExtra("_id", projects.get(position).get_id());
                intent.putExtra("project_name", projects.get(position).getProject_name());
                startActivity(intent);
            }
        });
        local_none = (TextView) findViewById(R.id.local_text);
        contextMenuView = View.inflate(this, R.layout.context_menu, null);
        contextMenuDialog = new AlertDialog.Builder(this).create();
        contextMenuDialog.setCancelable(true);
        contextMenuDialog.setCanceledOnTouchOutside(true);
        // contextMenuGrid = (GridView)
        // contextMenuView.findViewById(R.id.context_menu_gridview);
    }

    /*
     * @Override public void onCreateContextMenu(ContextMenu menu, View v,
     * ContextMenuInfo menuInfo) { // TODO Auto-generated method stub
     * super.onCreateContextMenu(menu, v, menuInfo); final long databaseId =
     * ((AdapterContextMenuInfo) menuInfo).id; final int pos =
     * ((AdapterContextMenuInfo) menuInfo).position;
     * System.out.println("position:"+ ((AdapterContextMenuInfo)
     * menuInfo).position); if(databaseId == -1){ return; } Log.i("bookmarkId",
     * "select id : " + String.valueOf(databaseId));
     * System.out.println("id:"+databaseId); // Project p =
     * dao.getProjectByColunmId((int)databaseId); //
     * System.out.println(p.toString()); contextMenuGrid.setNumColumns(2);
     * ArrayList<HashMap<String, Object>> maps = new ArrayList<HashMap<String,
     * Object>>(); HashMap<String, Object> map = new HashMap<String, Object>();
     * map.put("menu", "编辑"); maps.add(map); HashMap<String, Object> map2 = new
     * HashMap<String, Object>(); map2.put("menu", "删除"); maps.add(map2);
     * SimpleAdapter adapter = new SimpleAdapter(this, maps,
     * R.layout.menu_long_list, new String[] { "menu"}, new int[] {
     * R.id.menu_more_image }); //,R.id.menu_more_txt
     * contextMenuGrid.setAdapter(adapter);
     * 
     * contextMenuGrid.setOnItemClickListener(new OnItemClickListener() {
     * 
     * public void onItemClick(AdapterView<?> parent, View view, int position,
     * long id) { switch(position){ case 0: //编辑 Intent intent = new
     * Intent(LocalData.this,ProjectCreateActivity.class);
     * intent.putExtra("id",projects.get(position).getProject_id()); break; case
     * 1: //删除 System.out.println("删除："); System.out.println("pos:"+pos);
     * System.out.println(projects.size());
     * System.out.println(projects.get(pos).toString());
     * dao.deleteProject(""+projects.get(pos).get_id()); fillData();
     * Toast.makeText(LocalData.this, "删除成功", Toast.LENGTH_SHORT).show(); break;
     * } contextMenuDialog.dismiss(); } });
     * 
     * if(maps.size() > 0){ contextMenuDialog.show(); Window MenuDialogWindow =
     * contextMenuDialog.getWindow();
     * MenuDialogWindow.setContentView(contextMenuView); } }
     */

    public void doUpload() {
        new Thread() {
            public void run() {
                Looper.prepare();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Upload.doUpload(LocalData.this, projects, handler);
                // handler.sendEmptyMessage(1);
                dismissDialog();
            }
        }.start();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (dao != null)
            dao.close();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        System.out.println("local data on resume");
        if (dao == null) {
            dao = new DAO(this);
            dao.open();
        }
        // put data in to list
        fillData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        if (dao == null) {
            dao = new DAO(this);
            dao.open();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_local_data, menu);
        return true;
    }

    public void fillData() {
        System.out.println("fill data");
        dao.open();
        projects = dao.getAllProject();
        for (int i = 0; i < projects.size(); i++) {
            System.out.println(projects.get(i).toString());
        }
        if (projects.size() == 0) {
            local_none.setVisibility(View.VISIBLE);
        } else {
            local_none.setVisibility(View.GONE);
            localAdapter = new LocalListAdapter(this, projects);
            local_lsit.setAdapter(localAdapter);
        }

    }

    public synchronized void showProgressDialog() {
        Util.Print(this.getClass().getSimpleName(), "显示进度条");
        if (prgDialog == null) {
            prgDialog = new ProgressDialog(LocalData.this);
            prgDialog.setTitle("请稍等");
            prgDialog.setMessage("正在上传数据");
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

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.left, R.anim.right);
    }
}
