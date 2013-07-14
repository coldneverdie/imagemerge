package com.myscene.changchun;

import java.util.List;

import com.myscene.data.DAO;
import com.myscene.data.Project;
import com.myscene.data.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 创建项目|搜索项目页面
 * @author liudongqi
 *
 */
public class ProjectCreateActivity extends Activity{

	
	private EditText pro_name;
	private EditText pro_id;
	private DAO dao;
	private Button pro_back;
	private Button pro_search;
	private final String TAG = "PROJECT CREATE AC";
	private boolean editflag;
	private int _id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_create);
		dao =new DAO(this);
		pro_id = (EditText)findViewById(R.id.project_id);
		pro_name = (EditText)findViewById(R.id.project_name);
		
		pro_back = (Button)findViewById(R.id.pro_back);
		pro_search = (Button)findViewById(R.id.pro_search);
		
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
				switch(checkNameAndId()){
				case 0:
//					List<Project> pros = dao.getAllProject();
//					Util.Print(TAG, "pros");
//					if(null != pros){
//						Util.Print(TAG, ""+pros.size());
//						for(int i=0;i<pros.size();i++){
//							Util.Print(TAG, pros.get(i).toString());
//						}
//					}
//					else
//						Util.Print(TAG, "pros null");
					if(editflag){
						Project project = new Project();
						project.setProject_id(pro_id.getText().toString().trim());
						project.setProject_name(pro_name.getText().toString().trim());
						project.setCreate_time(Util.getDate());
						System.out.println("project:");
						System.out.println(project.toString());
						dao.updateProject(_id, project);
						Toast.makeText(ProjectCreateActivity.this, "項目修改成功", Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Project project = new Project();
						project.setProject_id(pro_id.getText().toString().trim());
						project.setProject_name(pro_name.getText().toString().trim());
						project.setCreate_time(Util.getDate());
						
						if(!dao.checkInProject(project.getProject_id(), project.getProject_name()))
							dao.insertPorject(project);
						else{
							Toast.makeText(ProjectCreateActivity.this, "请修改项目号或项目名称，本地数据已经存在!", Toast.LENGTH_SHORT).show();
							return;
						}
						Toast.makeText(ProjectCreateActivity.this, project.getProject_name()+"创建成功", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(ProjectCreateActivity.this,CreateActivity.class);
						intent.setAction("project_create");
						intent.putExtra("project_id", project.getProject_id());
						intent.putExtra("project_name", project.getProject_name());
						startActivity(intent);
						finish();
					}
					
					break;
				case 1:
					Toast.makeText(ProjectCreateActivity.this, "请填写项目名称", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(ProjectCreateActivity.this, "请填写项目id", Toast.LENGTH_SHORT).show();
					break;
				}
				
			}
		});
	}

	/**
	 * 检测是否填写
	 * @return
	 */
	private int checkNameAndId(){
		if(pro_id.getText().toString() == null || pro_id.getText().toString().trim().equals(""))
			return 2;
		if(pro_name.getText().toString() == null || pro_name.getText().toString().trim().equals(""))
			return 1;
		return 0;
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
		if(dao != null){
			dao.close();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(dao == null){
			dao =new DAO(this);
			dao.open();
		}
		Intent intent = getIntent();
		if(null != intent){
			String action = intent.getAction();
			if(null != action &&action.equals("project_edit")){
				String projectId = intent.getStringExtra("project_id");
				String projectName = intent.getStringExtra("project_name");
				if(null != projectId && null != projectName){
					pro_id.setText(projectId);
					pro_name.setText(projectName);
					editflag = true;
					_id = intent.getExtras().getInt("_id");
				}
			}
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(dao != null){
			dao.close();
		}
	}
   
	
}
