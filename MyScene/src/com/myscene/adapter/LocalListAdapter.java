package com.myscene.adapter;
import java.util.List;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.myscene.changchun.R;
import com.myscene.data.Project;
public class LocalListAdapter extends BaseAdapter{

	private Context context;
	List<Project> projects;
	public LocalListAdapter(Context context,List<Project> projects){
		this.context = context;
		this.projects = projects;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return projects.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return projects.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = View.inflate(context, R.layout.list_local_item, null);
			holder = new ViewHolder();
			holder.project_name = (TextView)convertView.findViewById(R.id.project_name);
			holder.project_id = (TextView)convertView.findViewById(R.id.project_id);
			holder.project_createtime = (TextView)convertView.findViewById(R.id.project_createtime);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.project_createtime.setTextColor(Color.parseColor("#878787"));
		holder.project_createtime.setText(projects.get(position).getCreate_time());
		holder.project_id.setText(projects.get(position).getProject_id());
		holder.project_id.setTextColor(Color.parseColor("#878787"));
		holder.project_name.setText(projects.get(position).getProject_name());
		holder.project_name.setTextColor(Color.parseColor("#878787"));
		return convertView;
	}

	private class ViewHolder{
		TextView project_name;
		TextView project_id;
		TextView project_createtime;
	}
}
