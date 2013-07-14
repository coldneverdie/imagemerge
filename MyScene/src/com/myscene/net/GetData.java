package com.myscene.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.myscene.changchun.CreateActivity;
import com.myscene.changchun.ProjectCreateActivity;
import com.myscene.data.BaseInfo;
import com.myscene.data.DAO;
import com.myscene.data.DBHelper;
import com.myscene.data.Dirt;
import com.myscene.data.Pid;
import com.myscene.data.Project;
import com.myscene.data.Util;
import com.myscene.data.Washwell;


public class GetData {
//	coldneverdie.gicp.net
    public static final String getDataUrl = "http://222.129.195.39:8080/MyField/project.jsp";
    public static AlertDialog alert;
    public static DAO dao;

    /**
     * 从服务器获取 数据
     * 
     * @param project_id
     * @param project_name
     */
    public static void getProjectData(final Context context, final String project_id, final String project_name,
            final Handler handler) {
        new Thread() {
            @SuppressWarnings("deprecation")
			public void run() {
                Looper.prepare();
                Util.Print("GET DATA", "从服务器获取数据");
                String plus = null;
                if (project_id == null || null == project_name)
                    return;
                Util.Print("project_id", project_id);
                Util.Print("project_name", URLEncoder.encode(project_name));
                if (!"".equals(project_id))
                    plus = "?projectId=" + project_id;
                if (!"".equals(project_id) && !"".equals(project_name)) {
                    plus += "&projectName=" + URLEncoder.encode(project_name);
                } else {
                    if (!"".equals(project_name))
                        plus = "?projectName=" + URLEncoder.encode(project_name);
                }
                Util.Print("url", getDataUrl + plus);
                HttpGet httpGet = new HttpGet(getDataUrl + plus);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = null;
                String result = null;
                try {
                    httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (null != httpEntity) {
                        InputStream in = httpEntity.getContent();
                        result = convertStreamToString(in);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } finally {
                    httpClient.getConnectionManager().shutdown();
                    httpResponse = null;
                }
                if (null != result)
                    Util.Print("GET DATA result", result);
                else
                    Util.Print("get data result", "null");
                Message msg = new Message();
                msg.obj = result;
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "utf-8"),// 防止模拟器上的乱码
                    512 * 1024);
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        }

        catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 把从网络获取的数据 存放到本地
     * 
     * @param jsonString
     */
    public static void putToLocal(final String jsonString, final Context context,final Handler handler) {
        JSONObject json;
        if (null == dao)
            dao = new DAO(context.getApplicationContext());
        try {
            json = new JSONObject(jsonString);
            final String project = json.getString("project");
            final String baseinfo = json.getString("baseinfo");
            final String dirt = json.getString("dirt");
            final String pid = json.getString("pid");
            final String washwell = json.getString("xijinginfo");
            if(project == null || project.equals("")){
            	handler.sendEmptyMessage(3);
            	return;
            }
            Util.Print("project", project);
            Util.Print("baseinfo", baseinfo);
            Util.Print("dirt", dirt);
            Util.Print("pid", pid);
            Util.Print("washwell", washwell);
            String[] pros = project.split(";");
            for (int i = 0; i < pros.length; i++) {
                if (!"".equals(pros[i])) {
                    final String[] pro_data = pros[i].split("%%");
                    
                    final Project p = dao.getProjectByProjectId(pro_data[0]);
                    if (null != p ) {
                        // 本地存在 是否覆盖
                    	 Util.Print(DAO.class.getSimpleName(), p.toString());
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("本地存在该项目，是否覆盖？").setCancelable(true)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Util.Print("tag", "确定覆盖");
                                        // Project proj = new Project();
                                        // proj.setCreate_time(pro_data[2]);
                                        // proj.setProject_id(pro_data[0]);
                                        // proj.setProject_name(pro_data[1]);
                                        //此处可以先删除本地数据，然后再插入
//                                        dao.close();
//                                        dao= null;
//                                        dao = new DAO(context);
                                        dao.open();
                                        dao.deleteProjectData(context,pro_data[0]);
                                      	Project pp = new Project();
                                    	pp.setProject_id(pro_data[0]);
                                    	pp.setProject_name(pro_data[1]);
                                    	pp.setCreate_time(pro_data[2]);
                                    	dao.insertPorject(pp);
                                        Util.Print("tag", "本地项目不存在");
                                        // 保存项目 到项目表
                                        String[] project_infos = project.split("%%");
                                        if (project_infos.length != 0) {
                                            if (4 == project_infos.length) {
                                                Project pro = new Project();
                                                pro.setProject_id(project_infos[1]);
                                                pro.setProject_name(project_infos[2]);
                                                pro.setCreate_time(project_infos[3]);
                                                dao.insertPorject(pro);
                                            }
                                        }
                                        // 保存 baseinfo
                                        String[] baseinfos = baseinfo.split("%%");
                                        if (baseinfos.length != 0) {
                                            {
                                                BaseInfo base = new BaseInfo();
                                                base.setProject_id(baseinfos[0]);
                                                base.setWell_num(baseinfos[1]);
                                                base.setBase_weather(baseinfos[2]);
                                                base.setBase_date(baseinfos[3]);
                                                base.setBase_work(baseinfos[4]);
                                                base.setBase_depth(baseinfos[5]);
                                                base.setBase_mradis(baseinfos[6]);
                                                base.setBase_radis(baseinfos[7]);
                                                base.setBase_way(baseinfos[8]);
                                                base.setBase_writer(baseinfos[9]);
                                                base.setBase_gps(baseinfos[10]);
                                                base.setBase_first_depth(baseinfos[11]);
                                                base.setCreate_time(baseinfos[12]);
                                                dao.insertBaseInfo(base);
                                            }
                                        }
                                        // 保存dirt
                                        if (dirt != null && !"".equals(dirt)) {
                                            String[] dirts = dirt.split(";");
                                            for (int j = 0; j < dirts.length; j++) {
                                                String[] temp = dirts[j].split("%%");
                                                if (temp.length > 0) {
                                                    Dirt d = new Dirt();
                                                    d.setProject_id(temp[0]);
                                                    d.setWell_num(temp[1]);
                                                    d.setDirt_depth(temp[2]);
                                                    d.setDirt_nature(temp[3]);
                                                    d.setDirt_descrip(temp[4]);
                                                    d.setDirt_extra(temp[5]);
                                                    d.setCreate_time(temp[6]);
                                                    dao.insertDirt(d);
                                                }
                                            }
                                        }
                                        // 保存Pid
                                        if (pid != null && !"".equals(pid)) {
                                            String[] pids = pid.split(";");
                                            for (int j = 0; j < pids.length; j++) {
                                                String[] temp = pids[j].split("%%");
                                                if (temp.length > 0) {
                                                    Pid p1 = new Pid();
                                                    p1.setProject_id(temp[0]);
                                                    p1.setWell_num(temp[1]);
                                                    p1.setPid_depth(temp[2]);
                                                    p1.setPid_value(temp[3]);
                                                    p1.setPid_memo(temp[4]);
                                                    p1.setCreate_time(temp[5]);
                                                    dao.insertPid(p1);
                                                }
                                            }
                                        }
                                        // 保存 washwell
                                        if (washwell != null && !"".equals(washwell)) {
                                            String[] wws = washwell.split(";");
                                            for (int j = 0; j < wws.length; j++) {
                                                String[] ww = wws[j].split("%%");
                                                if (ww.length > 0) {
                                                    Washwell w = new Washwell();
                                                    w.setProject_id(ww[0]);
                                                    w.setWell_num(ww[1]);
                                                    w.setWw_date(ww[2]);
                                                    w.setWw_time(ww[3]);
                                                    w.setWw_weather(ww[4]);
                                                    w.setWw_method(ww[5]);
                                                    w.setWw_temp(ww[6]);
                                                    w.setWw_cond(ww[7]);
                                                    w.setWw_ph(ww[8]);
                                                    w.setWw_water(ww[9]);
                                                    w.setWw_gaocheng(ww[10]);
                                                    w.setCreate_time(ww[11]);
                                                    dao.insertWashWell(w);
                                                }
                                            }
                                        }
                                        ///////////////////////////////
                                        Intent intent = new Intent(context, CreateActivity.class);
                                        intent.putExtra("project_id", pro_data[0]);
                                        intent.putExtra("project_name", pro_data[1]);
                                        CreateActivity.projectId = pro_data[0];
                                        CreateActivity.projectName = pro_data[1];
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        handler.sendEmptyMessage(2);
                                    }
                                }).setNegativeButton("取消", new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        Util.Print("tag", "取消覆盖");
                                        dialog.cancel();
                                        return;
                                    }
                                });
                        alert = builder.create();
                        alert.show();
                    } else {
                        // 直接存在本地 然后 进入 本地项目页面
                    	Project pp = new Project();
                    	pp.setProject_id(pro_data[0]);
                    	pp.setProject_name(pro_data[1]);
                    	pp.setCreate_time(pro_data[2]);
                    	dao.insertPorject(pp);
                        Util.Print("tag", "本地项目不存在");
                        // 保存项目 到项目表
                        String[] project_infos = project.split("%%");
                        if (project_infos.length != 0) {
                            if (4 == project_infos.length) {
                                Project pro = new Project();
                                pro.setProject_id(project_infos[1]);
                                pro.setProject_name(project_infos[2]);
                                pro.setCreate_time(project_infos[3]);
                                dao.insertPorject(pro);
                            }
                        }
                        // 保存 baseinfo
                        String[] baseinfos = baseinfo.split("%%");
                        if (baseinfos.length != 0) {
                            {
                            	 BaseInfo base = new BaseInfo();
                                 base.setProject_id(baseinfos[0]);
                                 base.setWell_num(baseinfos[1]);
                                 base.setBase_weather(baseinfos[2]);
                                 base.setBase_date(baseinfos[3]);
                                 base.setBase_work(baseinfos[4]);
                                 base.setBase_depth(baseinfos[5]);
                                 base.setBase_mradis(baseinfos[6]);
                                 base.setBase_radis(baseinfos[7]);
                                 base.setBase_way(baseinfos[8]);
                                 base.setBase_writer(baseinfos[9]);
                                 base.setBase_gps(baseinfos[10]);
                                 base.setBase_first_depth(baseinfos[11]);
                                 base.setCreate_time(baseinfos[12]);
                                 dao.insertBaseInfo(base);
                            }
                        }
                        // 保存dirt
                        if (dirt != null && !"".equals(dirt)) {
                            String[] dirts = dirt.split(";");
                            for (int j = 0; j < dirts.length; j++) {
                                String[] temp = dirts[j].split("%%");
                                if (temp.length > 0) {
                                    Dirt d = new Dirt();
                                    d.setProject_id(temp[0]);
                                    d.setWell_num(temp[1]);
                                    d.setDirt_depth(temp[2]);
                                    d.setDirt_nature(temp[3]);
                                    d.setDirt_descrip(temp[4]);
                                    d.setDirt_extra(temp[5]);
                                    d.setCreate_time(temp[6]);
                                    dao.insertDirt(d);
                                }
                            }
                        }
                        // 保存Pid
                        if (pid != null && !"".equals(pid)) {
                            String[] pids = pid.split(";");
                            for (int j = 0; j < pids.length; j++) {
                                String[] temp = pids[j].split("%%");
                                if (temp.length > 0) {
                                    Pid p1 = new Pid();
                                    p1.setProject_id(temp[0]);
                                    p1.setWell_num(temp[1]);
                                    p1.setPid_depth(temp[2]);
                                    p1.setPid_value(temp[3]);
                                    p1.setPid_memo(temp[4]);
                                    p1.setCreate_time(temp[5]);
                                    dao.insertPid(p1);
                                }
                            }
                        }
                        // 保存 washwell
                        if (washwell != null && !"".equals(washwell)) {
                            String[] wws = washwell.split(";");
                            for (int j = 0; j < wws.length; j++) {
                                String[] ww = wws[j].split("%%");
                                if (ww.length > 0) {
                                    Washwell w = new Washwell();
                                    w.setProject_id(ww[0]);
                                    w.setWell_num(ww[1]);
                                    w.setWw_date(ww[2]);
                                    w.setWw_time(ww[3]);
                                    w.setWw_weather(ww[4]);
                                    w.setWw_method(ww[5]);
                                    w.setWw_temp(ww[6]);
                                    w.setWw_cond(ww[7]);
                                    w.setWw_ph(ww[8]);
                                    w.setWw_water(ww[9]);
                                    w.setWw_gaocheng(ww[10]);
                                    w.setCreate_time(ww[11]);
                                    dao.insertWashWell(w);
                                }
                            }
                        }
                        //////////////////////////////////////
                        dao.close();
                        dao = null;
                        //go to project main
                        Intent intent = new Intent(context, CreateActivity.class);
                        intent.putExtra("project_id", pro_data[0]);
                        intent.putExtra("project_name", pro_data[1]);
                        CreateActivity.projectId = pro_data[0];
                        CreateActivity.projectName = pro_data[1];
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        handler.sendEmptyMessage(2);
                        ////////////////////////////////////////
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (null != dao) {
            dao.close();
        }
    }

    /**
     * 搜索结果跳转到 项目首页
     * 
     * @param context
     * @param projectId
     * @param projectName
     */
    public static void goToProjectMain(final Context context, final String projectId, final String projectName) {
        Intent intent = new Intent(context, CreateActivity.class);
        intent.putExtra("project_id", projectId);
        intent.putExtra("project_name", projectName);
        context.startActivity(intent);
    }
}
