package com.myscene.data;
/**
 * 土层
 * @author liudongqi
 *
 */
public class Dirt {

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
	private String dirt_depth;
	private String dirt_nature;
	private String dirt_descrip;
	private String dirt_extra;
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
	public String getDirt_depth() {
		return dirt_depth;
	}
	public void setDirt_depth(String dirt_depth) {
		this.dirt_depth = dirt_depth;
	}
	public String getDirt_nature() {
		return dirt_nature;
	}
	public void setDirt_nature(String dirt_nature) {
		this.dirt_nature = dirt_nature;
	}
	public String getDirt_descrip() {
		return dirt_descrip;
	}
	public void setDirt_descrip(String dirt_descrip) {
		this.dirt_descrip = dirt_descrip;
	}
	public String getDirt_extra() {
		return dirt_extra;
	}
	public void setDirt_extra(String dirt_extra) {
		this.dirt_extra = dirt_extra;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("_id:"+get_id());
		sb.append("\r\n");
		sb.append("crete_time:"+getCreate_time());
		sb.append("\r\n");
		sb.append("depth:"+getDirt_depth());
		sb.append("\r\n");
		sb.append("nature:"+getDirt_nature());
		sb.append("\r\n");
		sb.append("extra:"+getDirt_extra());
		sb.append("\r\n");
		sb.append("descrip:"+getDirt_descrip());
		return sb.toString();
	}
	public String toFile(){
		StringBuilder sb = new StringBuilder();
		sb.append("_id"+get_id());
		sb.append("%%"+getProject_id());
		sb.append("%%"+getWell_num());
		sb.append("%%"+getDirt_depth());
		sb.append("%%"+getDirt_nature());
		sb.append("%%"+getDirt_descrip());
		sb.append("%%"+getDirt_extra());
		sb.append("%%"+getCreate_time());
		return sb.toString();
	}
}
