package com.myscene.changchun;

import java.util.List;

import com.myscene.adapter.DirtAdapter;
import com.myscene.adapter.PidAdapter;
import com.myscene.data.DAO;
import com.myscene.data.Dirt;
import com.myscene.data.Pid;
import com.myscene.data.Util;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Well_digWellActivity extends Activity implements OnClickListener {

    private Button dirt_back;
    // private Button dirt_save;
    private Button new_dirt;
    private Button new_pid;

    private TextView dirt_text_none;
    private TextView pid_text_none;
    private ListView dirt_list;
    private ListView pid_list;
    private List<Dirt> dirts;
    private List<Pid> pids;
    private DAO dao;
    private DirtAdapter dirtAdapter;
    private PidAdapter pidAdapter;
    AlertDialog dlg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_dig_well);
        dirt_back = (Button) findViewById(R.id.dirt_back);
        // dirt_save = (Button)findViewById(R.id.dirt_save);
        new_dirt = (Button) findViewById(R.id.new_dirt);
        new_pid = (Button) findViewById(R.id.new_pid);
        dirt_back.setOnClickListener(this);
        new_dirt.setOnClickListener(this);
        new_pid.setOnClickListener(this);

        dirt_text_none = (TextView) findViewById(R.id.dirt_text_none);
        pid_text_none = (TextView) findViewById(R.id.pid_text_none);
        dirt_list = (ListView) findViewById(R.id.dirt_list);
        pid_list = (ListView) findViewById(R.id.pid_list);
        dao = new DAO(this);

        dirt_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Well_digWellActivity.this, Well_Dig_Dirt.class);
                intent.setAction("dirt");
                intent.putExtra("_id", dirts.get(position).get_id());
                startActivity(intent);
            }
        });
        dirt_list.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                // TODO Auto-generated method stub
                if (null == dlg)
                    dlg = new AlertDialog.Builder(Well_digWellActivity.this).create();
                dlg.show();
                final Window dlgwin = dlg.getWindow();
                dlgwin.setContentView(R.layout.list_menu);
                Button edit = (Button) dlgwin.findViewById(R.id.button_edit);
                Button del = (Button) dlgwin.findViewById(R.id.button_delete);
                edit.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(Well_digWellActivity.this, Well_Dig_Dirt.class);
                        intent.setAction("dirt");
                        intent.putExtra("_id", dirts.get(position).get_id());
                        startActivity(intent);
                        dlg.cancel();
                    }
                });
                del.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dao.deleteDirt(dirts.get(position).get_id());
                        fillData();
                        Toast.makeText(Well_digWellActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        dlg.cancel();
                    }
                });
                return false;
            }

        });

        pid_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Well_digWellActivity.this, Well_Dig_Pid.class);
                intent.setAction("pid");
                intent.putExtra("_id", pids.get(position).get_id());
                startActivity(intent);
            }
        });
        pid_list.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                // TODO Auto-generated method stub
                if (null == dlg)
                    dlg = new AlertDialog.Builder(Well_digWellActivity.this).create();
                dlg.show();
                final Window dlgwin = dlg.getWindow();
                dlgwin.setContentView(R.layout.list_menu);
                Button edit = (Button) dlgwin.findViewById(R.id.button_edit);
                Button del = (Button) dlgwin.findViewById(R.id.button_delete);
                edit.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Util.Print("dig pid", "edit");
                        Intent intent = new Intent(Well_digWellActivity.this, Well_Dig_Pid.class);
                        intent.setAction("pid");
                        intent.putExtra("_id", pids.get(position).get_id());
                        startActivity(intent);
                        dlg.dismiss();
                    }
                });
                del.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Util.Print("dig pid", "delete");
                        dao.deletePid(pids.get(position).get_id());
                        fillData();
                        Toast.makeText(Well_digWellActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        dlg.dismiss();
                    }
                });
                return false;
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_well_dig_well, menu);
        return true;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.left, R.anim.right);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.dirt_back:
            finish();
            break;
        case R.id.dirt_save:
            break;
        case R.id.new_dirt:
            Intent intent = new Intent(Well_digWellActivity.this, Well_Dig_Dirt.class);
            startActivity(intent);
            break;
        case R.id.new_pid:
            intent = new Intent(Well_digWellActivity.this, Well_Dig_Pid.class);
            startActivity(intent);
            break;
        }
    }

    /**
     * fill data
     */
    public void fillData() {
        dirts = dao.getAllDirts(CreateActivity.projectId, WellActivity.well_num);
        pids = dao.getAllPids(CreateActivity.projectId, WellActivity.well_num);
        if (null == dirts || dirts.size() == 0) {
            Util.Print("dig main ac", "dirts null");
            dirt_text_none.setVisibility(View.VISIBLE);
        } else {
            dirt_text_none.setVisibility(View.GONE);
            dirtAdapter = new DirtAdapter(Well_digWellActivity.this, dirts);
            dirt_list.setAdapter(dirtAdapter);
        }
        if (null == pids || pids.size() == 0) {
            Util.Print("dig main ac", "pids null");
            pid_text_none.setVisibility(View.VISIBLE);
        } else {
            pid_text_none.setVisibility(View.GONE);
            pidAdapter = new PidAdapter(Well_digWellActivity.this, pids);
            pid_list.setAdapter(pidAdapter);
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

}
