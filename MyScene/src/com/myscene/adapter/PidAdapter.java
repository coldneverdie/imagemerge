package com.myscene.adapter;
import java.util.List;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.myscene.changchun.R;
import com.myscene.data.Pid;
public class PidAdapter extends BaseAdapter{

	private Context context;
	List<Pid> pids;
	public PidAdapter(Context context,List<Pid> projects){
		this.context = context;
		this.pids = projects;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return pids.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pids.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = View.inflate(context, R.layout.list_pid_item, null);
			holder = new ViewHolder();
			holder.pid_well_num = (TextView)convertView.findViewById(R.id.dig_well_num);
			holder.pid_depth = (TextView)convertView.findViewById(R.id.dig_depth);
			holder.pid_create_time = (TextView)convertView.findViewById(R.id.dig_createtime);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.pid_well_num.setText(pids.get(position).getWell_num());
		holder.pid_depth.setText(pids.get(position).getPid_value());
		holder.pid_create_time.setText(pids.get(position).getCreate_time());
		holder.pid_create_time.setTextColor(Color.parseColor("#878787"));
		holder.pid_depth.setTextColor(Color.parseColor("#878787"));
		holder.pid_well_num.setTextColor(Color.parseColor("#878787"));
		return convertView;
	}

	private class ViewHolder{
		TextView pid_well_num;
		TextView pid_depth;
		TextView pid_create_time;
	}
}
