package com.myscene.changchun;

import com.myscene.data.DAO;
import com.myscene.data.Dirt;
import com.myscene.data.Util;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

public class Well_Dig_Dirt extends Activity {

    private Button back;
    private Button save;
    private DAO dao;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private PopupWindow spinner_down;
    // private Button depth_1;
    private ArrayAdapter spinnerAdapter;
    private ArrayAdapter spinner_detailAdapter;
    private EditText dirt_nature;
    private EditText dirt_detail;
    private EditText dirt_extra;
    private Dirt d;
    private final static String[] numbers = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    private Spinner spinner_detail;
    private final static String[] dirt_types = { "岩石", "碎石土", "砂土", "粉土", "粘性土", "填土", "软土", "湿陷性土", "红粘土", "多年冻土",
            "膨胀岩土", "盐渍岩土" };
    private LinearLayout spinner_layout; // 土层类别，细节描述 父布局，动态增加内容

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_dig_dirt);
        if (WellActivity.well_num == null || WellActivity.well_num.equals("")) {
            Toast.makeText(this, "请先在基本信息中填写井号!", Toast.LENGTH_SHORT).show();
        }
        // depth_1 = (Button) findViewById(R.id.depth_1);
        back = (Button) findViewById(R.id.dirt_back);
        save = (Button) findViewById(R.id.dirt_save);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        // spinner_detail = (Spinner)findViewById(R.id.spinner_detail);
        spinner_detailAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                dirt_types);
        // spinner_layout = (LinearLayout)findViewById(R.id.spinner_layout);
        // spinnerAdapter = new ArrayAdapter<String>(this,
        // android.R.layout.simple_spinner_item, numbers){
        //
        // @Override
        // public View getDropDownView(int position, View convertView, ViewGroup
        // parent) {
        // // TODO Auto-generated method stub
        // View view = getLayoutInflater().inflate(R.layout.spinner_lay,
        // parent, false);
        // TextView label = (TextView) view.findViewById(R.id.label);
        // label.setText((CharSequence) getItem(position));
        //
        // if (spinner1.getSelectedItemPosition() == position) {
        // view.findViewById(R.id.icon).setVisibility(View.VISIBLE);
        // }
        // return view;
        // }
        // };
        // spinner_detail.setOnItemSelectedListener( new
        // OnItemSelectedListener() {
        //
        // @Override
        // public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
        // long arg3) {
        // // TODO Auto-generated method stub
        //
        // }
        //
        // @Override
        // public void onNothingSelected(AdapterView<?> arg0) {
        // // TODO Auto-generated method stub
        //
        // }
        //
        // });
        @SuppressWarnings("rawtypes")
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.spinner_lay, parent, false);
                TextView label = (TextView) view.findViewById(R.id.label);
                label.setText((CharSequence) getItem(position));

                if (spinner1.getSelectedItemPosition() == position) {
                    view.findViewById(R.id.icon).setVisibility(View.VISIBLE);
                }

                return view;
            }
        };

        @SuppressWarnings("rawtypes")
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.spinner_lay, parent, false);
                TextView label = (TextView) view.findViewById(R.id.label);
                label.setText((CharSequence) getItem(position));

                if (spinner2.getSelectedItemPosition() == position) {
                    view.findViewById(R.id.icon).setVisibility(View.VISIBLE);
                }

                return view;
            }
        };
        @SuppressWarnings("rawtypes")
        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.spinner_lay, parent, false);
                TextView label = (TextView) view.findViewById(R.id.label);
                label.setText((CharSequence) getItem(position));

                if (spinner3.getSelectedItemPosition() == position) {
                    view.findViewById(R.id.icon).setVisibility(View.VISIBLE);
                }

                return view;
            }
        };
        @SuppressWarnings("rawtypes")
        ArrayAdapter adapter4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.spinner_lay, parent, false);
                TextView label = (TextView) view.findViewById(R.id.label);
                label.setText((CharSequence) getItem(position));

                if (spinner4.getSelectedItemPosition() == position) {
                    view.findViewById(R.id.icon).setVisibility(View.VISIBLE);
                }

                return view;
            }
        };
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        spinner4.setAdapter(adapter4);
        dirt_nature = (EditText) findViewById(R.id.dirt_nature);
        dirt_detail = (EditText) findViewById(R.id.dirt_detail);
        dirt_extra = (EditText) findViewById(R.id.dirt_extra);
        dao = new DAO(this);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // /*
                if (WellActivity.well_num == null || WellActivity.well_num.equals("")) {
                    Toast.makeText(Well_Dig_Dirt.this, "请先在基本信息中填写井号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(spinner1.getSelectedItem().toString());
                sb.append(spinner2.getSelectedItem().toString());
                sb.append(".");
                sb.append(spinner3.getSelectedItem().toString());
                sb.append(spinner4.getSelectedItem().toString());
                String nature = dirt_nature.getText().toString();
                String detail = dirt_detail.getText().toString();
                String extra = dirt_extra.getText().toString();
                Dirt dirt = new Dirt();
                dirt.setWell_num(WellActivity.well_num);
                dirt.setProject_id(CreateActivity.projectId);
                dirt.setCreate_time(Util.getCurrentTime());
                dirt.setDirt_depth(sb.toString().trim());
                dirt.setDirt_descrip(detail);
                dirt.setDirt_nature(nature);
                dirt.setDirt_extra(extra);
                Util.Print("Well Dig", dirt.toString());
                if (d != null) {
                    // update
                    Util.Print("Well Dig", "update dirt");
                    if (dao.updateDirt(d.get_id(), dirt)) {
                        Toast.makeText(Well_Dig_Dirt.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(Well_Dig_Dirt.this, "抱歉，修改失败", Toast.LENGTH_SHORT).show();
                } else {
                    // insert
                    Util.Print("Well Dig", "insert dirt");
                    if (dao.insertDirt(dirt)) {
                        Toast.makeText(Well_Dig_Dirt.this, "保存成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Well_Dig_Dirt.this, "抱歉，保存失败", Toast.LENGTH_SHORT).show();
                    }
                }
                // */
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        overridePendingTransition(R.anim.left, R.anim.right);
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

    public void fillData() {
        Intent intent = getIntent();
        if (null != intent) {
            String action = intent.getAction();
            if (action != null && action.equals("dirt")) {
                System.out.println("dirt fill data");
                String _id = "" + intent.getExtras().getInt("_id");
                d = dao.getDirt(_id);
                if (null != d) {
                    // put dirt into edittext
                    System.out.println(d.toString());
                    String depth = d.getDirt_depth();
                    depth = depth.replace(".", "").trim();
                    char[] list = depth.toCharArray();
                    // System.out.println("depth:"+depth.toString());
                    // System.out.println(Double.valueOf(list[0]+list[1]+"."+list[2]+list[3]));
                    switch (list.length) {
                    case 1:
                        // zero do nothing
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        // 应该是4 位 都保存的
                        spinner1.setSelection(Integer.valueOf(list[0] + ""));
                        spinner2.setSelection(Integer.valueOf(list[1] + ""));
                        spinner3.setSelection(Integer.valueOf(list[2] + ""));
                        spinner4.setSelection(Integer.valueOf(list[3] + ""));
                        break;
                    }
                    dirt_nature.setText(d.getDirt_nature());
                    dirt_detail.setText(d.getDirt_descrip());
                    dirt_extra.setText(d.getDirt_extra());
                }
                intent.setAction("");
                intent.removeExtra("dirt");
            } else {
                Util.Print("well dirt", "null intent");
            }
        }
    }
}
