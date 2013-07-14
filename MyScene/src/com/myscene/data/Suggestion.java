package com.myscene.data;

/**
 * 输入建议类
 * 
 * @author liudongqi
 * @since 2012-12-21
 */
public class Suggestion {

    /** 自增id */
    private int _id;
    /** 建议名称 */
    private String sug_name;
    /** 建议类型 **/
    private String sug_type;
    private long sug_time;
    public int get_id() {
        return _id;
    }
    public void set_id(int _id) {
        this._id = _id;
    }
    public String getSug_name() {
        return sug_name;
    }
    public void setSug_name(String sug_name) {
        this.sug_name = sug_name;
    }
    public String getSug_type() {
        return sug_type;
    }
    public void setSug_type(String sug_type) {
        this.sug_type = sug_type;
    }
    
    public long getSug_time() {
        return sug_time;
    }
    public void setSug_time(long sug_time) {
        this.sug_time = sug_time;
    }
}
