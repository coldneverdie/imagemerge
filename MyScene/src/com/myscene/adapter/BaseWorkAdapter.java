package com.myscene.adapter;

import java.util.List;

import com.myscene.data.Suggestion;

import com.myscene.changchun.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;;

public class BaseWorkAdapter extends BaseAdapter{

    Context context;
    List<Suggestion> sugs;
    SugListener suglisten = new SugListener();
    public BaseWorkAdapter(Context context,List<Suggestion> sugs){
        this.context = context;
        this.sugs = sugs;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        HolderView hold;
        if ( convertView == null){
            convertView =  ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.suggestion, parent);
            hold = new HolderView();
            hold.sug_name = (TextView)convertView.findViewById(R.id.sug_name);
            convertView.setTag(hold);
//            hold.sug_type // 暂时不用
        } else {
            hold = ((HolderView)convertView.getTag());
        }
        hold.sug_name.setText(sugs.get(position).getSug_name());
        hold.sug_name.setOnClickListener(suglisten);
        return convertView;
    }

    private class HolderView{
        private ImageView sug_type;
        private TextView  sug_name;
    }
    
    private class SugListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            System.out.println(((TextView)v).getText().toString());
        }
    }
}
