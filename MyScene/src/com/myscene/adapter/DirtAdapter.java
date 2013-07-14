package com.myscene.adapter;
import java.util.List;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.myscene.changchun.R;
import com.myscene.data.Dirt;
public class DirtAdapter extends BaseAdapter{

	private Context context;
	List<Dirt> dirts;
	public DirtAdapter(Context context,List<Dirt> projects){
		this.context = context;
		this.dirts = projects;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return dirts.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dirts.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = View.inflate(context, R.layout.list_dirt_item, null);
			holder = new ViewHolder();
			holder.dirt_well_num = (TextView)convertView.findViewById(R.id.dig_well_num);
			holder.dirt_depth = (TextView)convertView.findViewById(R.id.dig_depth);
			holder.dirt_create_time = (TextView)convertView.findViewById(R.id.dig_createtime);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.dirt_well_num.setText(dirts.get(position).getWell_num());
		holder.dirt_well_num.setTextColor(Color.parseColor("#878787"));
		holder.dirt_depth.setText(dirts.get(position).getDirt_depth());
		holder.dirt_depth.setTextColor(Color.parseColor("#878787"));
		holder.dirt_create_time.setText(dirts.get(position).getCreate_time());
		holder.dirt_create_time.setTextColor(Color.parseColor("#878787"));
		return convertView;
	}

	private class ViewHolder{
		TextView dirt_well_num;
		TextView dirt_depth;
		TextView dirt_create_time;
	}
}
