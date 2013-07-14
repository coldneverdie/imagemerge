package com.myscene.changchun;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.myscene.adapter.BaseInfoAdapter;
import com.myscene.adapter.LocalListAdapter;
import com.myscene.data.BaseInfo;
import com.myscene.data.DAO;
import com.myscene.data.Dirt;
import com.myscene.data.Pid;
import com.myscene.data.Project;
import com.myscene.data.Util;
import com.myscene.data.Washwell;
import com.myscene.net.Upload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import com.myscene.changchun.R;

public class CreateActivity extends Activity implements OnClickListener {

	private TextView create_localnone;
	private Button create_back;
	private Button create_upload;
	// private Button create_save;
	private Button create_new;
	private ProgressDialog prgDialog;
	/** 当前已有的项目列表  **/
	private ListView base_list;
	private BaseInfoAdapter localAdapter;
	private List<BaseInfo> baseinfos;
	private int _id;
	public static String projectId;
	public static String projectName;
	public final static String filename="upload.log";
	private DAO dao;
	AlertDialog dlg;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 0:
				Toast.makeText(CreateActivity.this, "抱歉，上传失败", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(CreateActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		create_localnone = (TextView) findViewById(R.id.create_localtext);
		base_list = (ListView) findViewById(R.id.create_list);
		create_back = (Button) findViewById(R.id.create_back);
		create_upload = (Button) findViewById(R.id.create_upload);
		// create_save = (Button) findViewById(R.id.create_save);
		create_new = (Button) findViewById(R.id.create_new);
		create_back.setOnClickListener(this);
		create_new.setOnClickListener(this);
		create_upload.setOnClickListener(this);
		// create_save.setOnClickListener(this);
		base_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				// 根据 这个井号 跳转到 对应井号的 信息
				Intent intent = new Intent(CreateActivity.this, WellActivity.class);
				intent.setAction("project_local");
				intent.putExtra("well_colunm_id", baseinfos.get(position).get_id());
				intent.putExtra("well_num", baseinfos.get(position).getWell_num());
				startActivity(intent);
			}
		});
		base_list.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,final int position, long arg3) {
                // TODO Auto-generated method stub
                if(null == dlg)
                    dlg = new AlertDialog.Builder(
                            CreateActivity.this)
                            .create();
                    dlg.show();
                    final Window dlgwin = dlg
                            .getWindow();
                    dlgwin.setContentView(R.layout.list_menu);
                    Button edit = (Button) dlgwin
                            .findViewById(R.id.button_edit);
                    Button del = (Button) dlgwin
                            .findViewById(R.id.button_delete);
                    edit.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(CreateActivity.this, WellActivity.class);
                            intent.setAction("project_local");
                            intent.putExtra("well_colunm_id", baseinfos.get(position).get_id());
                            intent.putExtra("well_num", baseinfos.get(position).getWell_num());
                            startActivity(intent);
                            dlg.cancel();
                        }
                    });
                    del.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
                                builder.setTitle("确定要删除?")
                                .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                                    
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
//                                        dao.deleteProject(""+projects.get(position).get_id());
//                                        dao.delete
                                        dao.deleteWellData(baseinfos.get(position).getProject_id(), baseinfos.get(position).getWell_num());
                                        Toast.makeText(CreateActivity.this, "删除成功", Toast.LENGTH_SHORT).show();    
                                        fillData();
                                        dlg.cancel();
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                                    
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.cancel();
                                        dlg.cancel();
                                    }
                                })
                                .setCancelable(true);
                            builder.create();
                            builder.show();
                        } 
                    });
                return true;
            }
		    
        });
	}

	
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.left, R.anim.right);
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
		Intent intent = getIntent();
		if (null != intent) {
			String action = intent.getAction();
			if (null != action) {
				if (action.equals("project_edit")) {
					Util.Print("create", "from local data edit");
					_id = intent.getExtras().getInt("_id");
					projectId = intent.getExtras().getString("project_id");
					projectName = intent.getExtras().getString("project_name");
					Util.Print("create", projectId);
					Util.Print("create", projectName);
					intent.setAction("");
					intent.removeExtra("_id");
					intent.removeExtra("project_id");
					intent.removeExtra("project_name");
				} else {
					if (action.equals("project_create")) {
						Util.Print("create", "new create");
						projectId = intent.getExtras().getString("project_id");
						projectName = intent.getExtras().getString(
								"project_name");
						intent.setAction("");
						intent.removeExtra("project_id");
						intent.removeExtra("project_name");
					}
				}
			}

		}
		fillData();
	}

	/**
	 * load data
	 */
	public void fillData(){
	    baseinfos = dao.getAllBaseInfosShort(projectId);
        if (baseinfos.size() == 0) {
            Util.Print("project" + projectName + "local data is null", "");
            create_localnone.setVisibility(View.VISIBLE);
        } else {
            Util.Print("project" + projectName + "local data count",
                    baseinfos.size() + "");
            create_localnone.setVisibility(View.GONE);
            getLocalProjectData(baseinfos);
        }
	}
	/**
	 * check the local data by the id project id
	 */
	public void getLocalProjectData(List<BaseInfo> baseinfos) {
		try {
			localAdapter = new BaseInfoAdapter(CreateActivity.this, baseinfos,
					projectName);
			if (localAdapter == null) {
				Util.Print("local", "null");
			}
			if (null == base_list) {
				Util.Print("list view", "null");
				base_list = (ListView) findViewById(R.id.create_list);
			}
			base_list.setAdapter(localAdapter);
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if (dao == null) {
			dao = new DAO(this);
			dao.open();
		}
		if (null != intent) {
			String action = intent.getAction();
			if (null != action) {
				if (action.equals("project_edit")) {
					Util.Print("create", "from local data edit");
					_id = intent.getExtras().getInt("_id");
					projectId = intent.getExtras().getString("project_id");
					projectName = intent.getExtras().getString("project_name");
					Util.Print("create", projectId);
					Util.Print("create", projectName);
					intent.setAction("");
					intent.removeExtra("_id");
					intent.removeExtra("project_id");
					intent.removeExtra("project_name");
				} else {
					if (action.equals("project_create")) {
						Util.Print("create", "new create");
						projectId = intent.getExtras().getString("project_id");
						projectName = intent.getExtras().getString(
								"project_name");
						intent.setAction("");
						intent.removeExtra("project_id");
						intent.removeExtra("project_name");
					}
				}
			}

		}
		baseinfos = dao.getAllBaseInfosShort(projectId.trim());
		System.out.println("get local data");
		if (baseinfos.size() == 0) {
			Util.Print("project" + projectName + "local data is null", "");
			create_localnone.setVisibility(View.VISIBLE);
		} else {
			Util.Print("project" + projectName + "local data count",
					baseinfos.size() + "");
			create_localnone.setVisibility(View.GONE);
			getLocalProjectData(baseinfos);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println(v.getId());
		switch (v.getId()) {
		case R.id.create_back:
			finish();
			break;
		case R.id.create_upload:
			doUpload();
			break;
		// case R.id.create_save:
		// break;
		case R.id.create_new:
			WellActivity.well_colunm_id = null;
			WellActivity.well_num = null;
			Intent intent = new Intent(CreateActivity.this, WellActivity.class);
			startActivity(intent);
			break;
		}
	}
	/**
	 * upload this project
	 */
	public void doUpload(){
		showProgressDialog();
		System.out.println("shangchuan");
		if(Util.checkInternet(CreateActivity.this)){
			new Thread(){
				public void run(){
					Looper.prepare();
					try {
//						showProgressDialog();
						upload();
						dismissDialog();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}.start();
		}else{
			Toast.makeText(CreateActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void upload(){
		OutputStream out = null;
//		if(!log.exists())
//		if(!Util.checkPrivateExist(CreateActivity.this,filename))
			this.deleteFile(filename);
			try{
				try {
					out = openFileOutput(filename,Context.MODE_PRIVATE);
					Project p = dao.getProjectByProjectId(projectId);
					out.write(p.toFile().getBytes());
					out.write("&&".getBytes());
					List<BaseInfo> infos = dao.getBaseInfoByProjectId(projectId);
					for(int i=0;i<infos.size();i++){
						out.write(infos.get(i).toFile().getBytes());
						out.write(";".getBytes());
					}
					out.write("&&".getBytes());
					List<Washwell> ww = dao.getWashWellByProjectId(projectId);
					for(int i=0;i<ww.size();i++){
						out.write(ww.get(i).toFile().getBytes());
						out.write(";".getBytes());
					}
					out.write("&&".getBytes());
					List<Dirt> dirs = dao.getDirtByProjectId(projectId);
					for(int i=0;i<dirs.size();i++){
						out.write(dirs.get(i).toFile().getBytes());
						out.write(";".getBytes());
					}
					out.write("&&".getBytes());
					List<Pid> pids = dao.getPidByProjectId(projectId);
					for(int i=0;i<pids.size();i++){
						out.write(pids.get(i).toFile().getBytes());
						out.write(";".getBytes());
					}
					out.write("\r\n".getBytes());
					out.flush();
					Upload.doSingleUpload(CreateActivity.this, filename,handler);
//					if(Upload.doUpload(this, filename)) {
//						Toast.makeText(CreateActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
//					} else {
//						Toast.makeText(CreateActivity.this, "抱歉，上传失败", Toast.LENGTH_SHORT).show();
//					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}finally{
				try {
					if(null != out)
						out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	public void showProgressDialog(){
		if(prgDialog == null){
			prgDialog =new ProgressDialog(
					CreateActivity.this);
			prgDialog.setTitle("请稍等");
			prgDialog.setMessage("正在上传数据");
			prgDialog.setCancelable(true);
			prgDialog.show();
		} else{
			prgDialog.cancel();
			prgDialog = null;
			showProgressDialog();
		}
	}
	
	public void dismissDialog(){
		try{
			if(prgDialog != null){
				prgDialog.dismiss();
				prgDialog = null;
			}
		}catch(Exception err){
			err.printStackTrace();
		}
	}
	
	
}
