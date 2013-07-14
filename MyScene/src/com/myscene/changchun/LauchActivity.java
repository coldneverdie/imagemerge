package com.myscene.changchun;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.support.v4.app.NavUtils;

public class LauchActivity extends Activity {

	public  ImageView[] imgs= new ImageView[6];
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 0:
				Intent intent = new Intent(LauchActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case 1:
				imgs[1].setImageResource(R.drawable.point_fill);
				break;
			case 2:
				imgs[2].setImageResource(R.drawable.point_fill);
				break;
			case 3:
				imgs[3].setImageResource(R.drawable.point_fill);
				break;
			case 4:
				imgs[4].setImageResource(R.drawable.point_fill);
				break;
			case 5:
				imgs[5].setImageResource(R.drawable.point_fill);
				break;
			case 6:
				imgs[6].setImageResource(R.drawable.point_fill);
				break;
			}
			
			super.handleMessage(msg);
		}
		
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauch);
        imgs[0] = (ImageView)findViewById(R.id.point_1);
        imgs[1] = (ImageView)findViewById(R.id.point_2);
        imgs[2] = (ImageView)findViewById(R.id.point_3);
        imgs[3]  = (ImageView)findViewById(R.id.point_4);
        imgs[4] = (ImageView)findViewById(R.id.point_5);
        imgs[5]  = (ImageView)findViewById(R.id.point_6);
        new Thread(){
        	
        	public void run(){
        		try {
    				imgs[0].setImageResource(R.drawable.point_fill);
    				Thread.sleep(500);
        			for(int i=1;i<6;i++){
        				handler.sendEmptyMessage(i);
        				Thread.sleep(200);
        			}
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }.start();
    }

    
}
