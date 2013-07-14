package com.myscene.adapter;
import java.util.List;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.myscene.changchun.R;
import com.myscene.data.BaseInfo;
import com.myscene.data.Project;
public class BaseInfoAdapter extends BaseAdapter{

	private Context context;
	List<BaseInfo> baseinfos;
	private String projectName;
	public BaseInfoAdapter(Context context,List<BaseInfo> baseinfos,String projectName){
		this.context = context;
		this.baseinfos = baseinfos;
		this.projectName = projectName;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return baseinfos.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return baseinfos.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = View.inflate(context, R.layout.list_project_item, null);
			holder = new ViewHolder();
			holder.project_name = (TextView)convertView.findViewById(R.id.project_name);
			holder.project_id = (TextView)convertView.findViewById(R.id.project_id);
			holder.project_createtime = (TextView)convertView.findViewById(R.id.project_createtime);
			holder.well_num = (TextView)convertView.findViewById(R.id.well_num);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.project_createtime.setText(baseinfos.get(position).getCreate_time());
		holder.project_id.setText(baseinfos.get(position).getProject_id());
		holder.project_name.setText(projectName);
		holder.well_num.setText(baseinfos.get(position).getWell_num());
		holder.project_createtime.setTextColor(Color.parseColor("#878787"));
		holder.project_id.setTextColor(Color.parseColor("#878787"));
		holder.project_name.setTextColor(Color.parseColor("#878787"));
		holder.well_num.setTextColor(Color.parseColor("#878787"));
		return convertView;
	}

	private class ViewHolder{
		TextView project_name;
		TextView project_id;
		TextView well_num; 
		TextView project_createtime;
	}
}
