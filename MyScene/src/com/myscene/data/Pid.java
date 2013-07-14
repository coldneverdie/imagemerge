package com.myscene.data;

/**
 * Pid 读数
 * @author liudongqi
 *
 */
public class Pid {
	private int _id;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	private String project_id;
	private String well_num;
	public String getWell_num() {
		return well_num;
	}
	public void setWell_num(String well_num) {
		this.well_num = well_num;
	}
	private String pid_depth;
	private String pid_value;
	private String pid_memo;
	private String pid_extra;
	private String create_time;
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getPid_depth() {
		return pid_depth;
	}
	public void setPid_depth(String pid_depth) {
		this.pid_depth = pid_depth;
	}
	public String getPid_value() {
		return pid_value;
	}
	public void setPid_value(String pid_value) {
		this.pid_value = pid_value;
	}
	public String getPid_memo() {
		return pid_memo;
	}
	public void setPid_memo(String pid_memo) {
		this.pid_memo = pid_memo;
	}
	public String getPid_extra() {
		return pid_extra;
	}
	public void setPid_extra(String pid_extra) {
		this.pid_extra = pid_extra;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("pid id:"+get_id());
		sb.append("\r\n");
		sb.append("pid well num:"+getWell_num());
		sb.append("\r\n");
		sb.append("pid depth:"+getPid_depth());
		sb.append("\r\n");
		sb.append("extra:"+getPid_extra());
		sb.append("\r\n");
		sb.append("memo:"+getPid_memo());
		sb.append("\r\n");
		sb.append("value:"+getPid_value());
		return sb.toString();
	}
	public String toFile(){
		StringBuilder sb = new StringBuilder();
		sb.append(get_id());
		sb.append("%%"+getProject_id());
		sb.append("%%"+getWell_num());
		sb.append("%%"+getPid_depth());
		sb.append("%%"+getPid_value());
		sb.append("%%"+getPid_memo());
		sb.append("%%"+getPid_extra());
		sb.append("%%"+getCreate_time());
		return sb.toString();
	}
}
