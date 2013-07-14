package com.myscene.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.myscene.changchun.CreateActivity;
import com.myscene.data.BaseInfo;
import com.myscene.data.DAO;
import com.myscene.data.Dirt;
import com.myscene.data.Pid;
import com.myscene.data.Project;
import com.myscene.data.Util;
import com.myscene.data.Washwell;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class Upload {
	public static final String filename = "upload.log";
	public static final String uploadUrl = "http://coldneverdie.gicp.net:8080/MyField/uploadserver.jsp";

	public static void zipPrivateFile(Context context, String fileName)
			throws Exception {
		try {
			if (!checkPrivateExist(context, fileName))
				return;
			FileInputStream fis = context.openFileInput(fileName);
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] buf = new byte[1024];
			int len;
			FileOutputStream fos = context.openFileOutput(fileName + ".zip",
					Context.MODE_PRIVATE);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ZipOutputStream zos = new ZipOutputStream(bos);// 压缩包
			ZipEntry ze = new ZipEntry(fileName);// 这是压缩包名里的文件名
			zos.putNextEntry(ze);// 写入新的ZIP文件条目并将流定位到条目数据的开始处
			while ((len = bis.read(buf)) != -1) {
				zos.write(buf, 0, len);
				zos.flush();
			}
			bis.close();
			zos.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	/**
	 * 压缩单一个文件
	 * 
	 * @param zipPath
	 *            生成的zip文件路径
	 * @param filePath
	 *            需要压缩的文件路径
	 * @throws Exception
	 */
	public static void zipFile(String zipPath, String filePath)
			throws Exception {
		try {
			File f = new File(filePath);
			if (!f.exists())
				return;
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] buf = new byte[1024];
			int len;
			FileOutputStream fos = new FileOutputStream(zipPath);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ZipOutputStream zos = new ZipOutputStream(bos);// 压缩包
			ZipEntry ze = new ZipEntry(f.getName());// 这是压缩包名里的文件名
			zos.putNextEntry(ze);// 写入新的ZIP文件条目并将流定位到条目数据的开始处

			while ((len = bis.read(buf)) != -1) {
				zos.write(buf, 0, len);
				zos.flush();
			}
			bis.close();
			zos.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	/**
	 * 判断私有文件是否存在
	 * 
	 * @return
	 */
	public static boolean checkPrivateExist(Context context, String fileName) {
		System.out.println("检测是否存在");
		String[] fileNameArray = context.fileList();
		for (int i = 0; i < fileNameArray.length; i++) {
			if (fileNameArray[i].equals(fileName)) {
				// System.out.println("存在");
				return true;
			}
		}
		System.out.println("不存在");
		return false;
	}

	public synchronized static boolean doSingleUpload(Context context, String name,Handler handler) {
		System.out.println("上传");
		try {
			try {
				zipPrivateFile(context, name);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("exception from datasend");
				e1.printStackTrace();
			}
			System.out.println("开始上传");
			String boundary = "*****";
			try {
				URL url = new URL(uploadUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Connection", "Kepp-Alive");
				connection.setRequestProperty("Charset", "UTF-8");
				connection.setRequestProperty("filename", name);
				connection.setRequestProperty("Content-type",
						"multipart/form-data;boundary=" + boundary);
				DataOutputStream out = new DataOutputStream(
						connection.getOutputStream());
				if (!checkPrivateExist(context, name + ".zip")) {
					System.out.println("压缩包不存在");
					return false;
				}

				FileInputStream in = context.openFileInput(name + ".zip");
				byte[] buffer = new byte[4096];
				int length = -1;
				while ((length = in.read(buffer)) != -1) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.flush();
				in.close();
				Util.Print("Upload response code",""+ connection.getResponseCode());
				Util.Print("Upload response msg",""+ connection.getResponseMessage());
				if(connection.getResponseCode() == 1){
					Util.Print("upload state", "success");
					handler.sendEmptyMessage(1);
				}else
					handler.sendEmptyMessage(0);
//				// 获取respond 内容
//				InputStream is = connection.getInputStream();
//				int ch;
//				StringBuffer sb = new StringBuffer();
//				while ((ch = is.read()) != -1) {
//					sb.append((char) ch);
//				}
//				System.out.println(sb.toString());
//				if (sb.toString().trim().contains("success")) {
//					System.out.println("success ");
//					// 应该是返回成功再删除
//					context.deleteFile(name);
//					context.deleteFile(name + ".zip");
//				} else
//					System.out.println("fail upload");
//				is.close();
				context.deleteFile(name);
				context.deleteFile(name + ".zip");
			} catch (Exception e) {
				System.out.println("exception from data upload");
				e.printStackTrace();
				return false;
			} finally {
			}
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 上传所有本地数据
	 * @param context
	 * @param projects
	 */
	public static void doUpload(final Context context, List<Project> projects,Handler handler) {
		OutputStream out = null;
		context.deleteFile(filename);
		DAO dao = new DAO(context);
		dao.open();
		try {
			try {
				out = context.openFileOutput(filename, Context.MODE_PRIVATE);
				for (int j = 0; j < projects.size(); j++) {
					Project p = projects.get(j);
					out.write(p.toFile().getBytes());
					out.write("&&".getBytes());
					String projectId = p.getProject_id();
					List<BaseInfo> infos = dao
							.getBaseInfoByProjectId(projectId);
					for (int i = 0; i < infos.size(); i++) {
						out.write(infos.get(i).toFile().getBytes());
						out.write(";".getBytes());
					}
					out.write("&&".getBytes());
					List<Washwell> ww = dao.getWashWellByProjectId(projectId);
					for (int i = 0; i < ww.size(); i++) {
						out.write(ww.get(i).toFile().getBytes());
						out.write(";".getBytes());
					}
					out.write("&&".getBytes());
					List<Dirt> dirs = dao.getDirtByProjectId(projectId);
					for (int i = 0; i < dirs.size(); i++) {
						out.write(dirs.get(i).toFile().getBytes());
						out.write(";".getBytes());
					}
					out.write("&&".getBytes());
					List<Pid> pids = dao.getPidByProjectId(projectId);
					for (int i = 0; i < pids.size(); i++) {
						out.write(pids.get(i).toFile().getBytes());
						out.write(";".getBytes());
					}
					out.write("\r\n".getBytes());
				}
				out.flush();
				boolean flag = doSingleUpload(context, filename,handler);
//				if(flag){
//					handler.sendEmptyMessage(1);
//				}else
//					handler.sendEmptyMessage(0);
//				if(doUpload(context, filename)) {
//					Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
//				} else {
//					Toast.makeText(context, "抱歉，上传失败", Toast.LENGTH_SHORT).show();
//				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			try {
				if (null != out)
					out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != dao) {
				dao.close();
				dao = null;
			}
		}
	}
}
