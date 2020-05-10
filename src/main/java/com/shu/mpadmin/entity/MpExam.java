package com.shu.mpadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import java.util.Date;

@TableName("mp_exam")
public class MpExam {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("type")
    private String type;
    @TableField("create_time")
    private Date createTime;
    @TableField("is_ban")
    private Integer isBan;
    @TableField("status")
    private Integer status;

    public MpExam(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsBan() {
        return isBan;
    }

    public void setIsBan(Integer isBan) {
        this.isBan = isBan;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MpExam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", createTime=" + createTime +
                ", isBan=" + isBan +
                ", status=" + status +
                '}';
    }

    //实体类转换为json
    public JSONObject convertToJson() throws JSONException, JsonProcessingException {
        JSONObject json=new JSONObject();
        if(getId()!=null){
            json.put("id",getId());
        }else{
            json.put("id","");
        }
        if(StringUtils.isNotEmpty(getName())){
            json.put("name",getName());
        }else{
            json.put("name","");
        }
        if(StringUtils.isNotEmpty(getType())){
            json.put("type",getType());
        }else{
            json.put("type","");
        }
        if(getCreateTime()!=null){
            json.put("createtime",getCreateTime());
        }else{
            json.put("createtime","");
        }
        if(getIsBan()!=null){
            json.put("isBan",getIsBan());
        }else{
            json.put("isBan","");
        }
        return json;
    }

}
