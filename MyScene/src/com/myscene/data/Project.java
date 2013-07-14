package com.myscene.data;
/**
 * 项目类
 * @author liudongqi
 *
 */
public class Project {

	private int _id;
	private String project_id;
	private String project_name;
	private String create_time;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("id:+"+get_id()+"\r\n");
		sb.append("project id:"+getProject_id()+"\r\n");
		sb.append("name:"+getProject_name()+"\r\n");
		sb.append("time:"+getCreate_time()+"\r\n");
		return sb.toString();
	}
	public String toFile(){
		StringBuilder sb = new StringBuilder();
		sb.append(get_id());
		sb.append("%%"+getProject_id());
		sb.append("%%"+getProject_name());
		sb.append("%%"+getCreate_time());
		return sb.toString();
	}
}
