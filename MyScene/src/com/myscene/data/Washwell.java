package com.myscene.data;
/**
 * 洗井记录
 * @author liudongqi
 *
 */
public class Washwell {

	private int _id;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	private String project_id;
	private String well_num;
	private String ww_date;
	public String getWell_num() {
		return well_num;
	}
	public void setWell_num(String well_num) {
		this.well_num = well_num;
	}
	private String ww_time;
	public String getWw_time() {
		return ww_time;
	}
	public void setWw_time(String ww_time) {
		this.ww_time = ww_time;
	}
	private String ww_weather;
	private String ww_method;
	private String ww_temp;
	private String ww_cond;
	private String ww_ph;
	private String ww_water;
	private String ww_gaocheng;
	private String create_time;
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getWw_method() {
		return ww_method;
	}
	public void setWw_method(String ww_method) {
		this.ww_method = ww_method;
	}
	public String getWw_temp() {
		return ww_temp;
	}
	public void setWw_temp(String ww_temp) {
		this.ww_temp = ww_temp;
	}
	public String getWw_cond() {
		return ww_cond;
	}
	public void setWw_cond(String ww_cond) {
		this.ww_cond = ww_cond;
	}
	public String getWw_ph() {
		return ww_ph;
	}
	public void setWw_ph(String ww_ph) {
		this.ww_ph = ww_ph;
	}
	public String getWw_water() {
		return ww_water;
	}
	public void setWw_water(String ww_water) {
		this.ww_water = ww_water;
	}
	public String getWw_gaocheng() {
		return ww_gaocheng;
	}
	public void setWw_gaocheng(String ww_gaocheng) {
		this.ww_gaocheng = ww_gaocheng;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getWw_date() {
		return ww_date;
	}
	public void setWw_date(String ww_date) {
		this.ww_date = ww_date;
	}
	public String getWw_weather() {
		return ww_weather;
	}
	public void setWw_weather(String ww_weather) {
		this.ww_weather = ww_weather;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("_id:"+get_id());
		sb.append("\r\n");
		sb.append("project id:"+getProject_id());
		sb.append("\r\n");
		sb.append("well num:"+getWell_num());
		sb.append("\r\n");
		sb.append("date"+getWw_date());
		sb.append("\r\n");
		sb.append("date:"+getWw_date());
		sb.append("\r\n");
		sb.append("wash well time:"+getWw_time());
		sb.append("\r\n");
		sb.append("weather"+getWw_weather());
		return sb.toString();
	}
	public String toFile(){
		StringBuilder sb = new StringBuilder();
		sb.append(get_id());
		sb.append("%%"+getProject_id());
		sb.append("%%"+getWell_num());
		sb.append("%%"+getWw_date());
		sb.append("%%"+getWw_time());
		sb.append("%%"+getWw_weather());
		sb.append("%%"+getWw_method());
		sb.append("%%"+getWw_temp());
		sb.append("%%"+getWw_cond());
		sb.append("%%"+getWw_ph());
		sb.append("%%"+getWw_water());
		sb.append("%%"+getWw_gaocheng());
		sb.append("%%"+getCreate_time());
		return sb.toString();
	}
}
