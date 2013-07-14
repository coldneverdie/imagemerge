package com.myscene.data;
/**
 * 基本信息
 * @author liudongqi
 *
 */
public class BaseInfo {

	private String project_id;
	private String well_num;
	public String getWell_num() {
		return well_num;
	}
	public void setWell_num(String well_num) {
		this.well_num = well_num;
	}
	private int _id;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	private String base_weather;
	private String base_date;
	private String base_work;
	private String base_depth;
	private String base_mradis;
	private String base_radis;
	private String base_way;
	private String base_writer;
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
	private String base_gps;
	private String base_first_depth;
	public String getBase_weather() {
		return base_weather;
	}
	public void setBase_weather(String base_weather) {
		this.base_weather = base_weather;
	}
	public String getBase_date() {
		return base_date;
	}
	public void setBase_date(String base_date) {
		this.base_date = base_date;
	}
	public String getBase_work() {
		return base_work;
	}
	public void setBase_work(String base_work) {
		this.base_work = base_work;
	}
	public String getBase_depth() {
		return base_depth;
	}
	public void setBase_depth(String base_depth) {
		this.base_depth = base_depth;
	}
	public String getBase_mradis() {
		return base_mradis;
	}
	public void setBase_mradis(String base_mradis) {
		this.base_mradis = base_mradis;
	}
	public String getBase_radis() {
		return base_radis;
	}
	public void setBase_radis(String base_radis) {
		this.base_radis = base_radis;
	}
	public String getBase_way() {
		return base_way;
	}
	public void setBase_way(String base_way) {
		this.base_way = base_way;
	}
	public String getBase_writer() {
		return base_writer;
	}
	public void setBase_writer(String base_writer) {
		this.base_writer = base_writer;
	}
	public String getBase_gps() {
		return base_gps;
	}
	public void setBase_gps(String base_gps) {
		this.base_gps = base_gps;
	}
	public String getBase_first_depth() {
		return base_first_depth;
	}
	public void setBase_first_depth(String base_first_depth) {
		this.base_first_depth = base_first_depth;
	}
	public String toFile(){
		StringBuilder sb = new StringBuilder();
		sb.append(get_id());
		sb.append("%%"+getProject_id());
		sb.append("%%"+getWell_num());
		sb.append("%%"+getBase_weather());
		sb.append("%%"+getBase_date());
		sb.append("%%"+getBase_work());
		sb.append("%%"+getBase_depth());
		sb.append("%%"+getBase_mradis());
		sb.append("%%"+getBase_radis());
		sb.append("%%"+getBase_way());
		sb.append("%%"+getBase_writer());
		sb.append("%%"+getBase_gps());
		sb.append("%%"+getBase_first_depth());
		sb.append("%%"+getCreate_time());
		return sb.toString();
	}
	
}
