package com.shu.mpadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

@TableName("mp_option")
public class MpOption {

    @TableId(value="id",type = IdType.AUTO)
    private Integer id;
    @TableField("question_id")
    private Integer questionId;
    @TableField("content")
    private String content;
    @TableField("create_time")
    private Date createTime;
    @TableField("is_ban")
    private Integer isBan;
    @TableField("score")
    private Double score;
    @TableField("status")
    private Integer status;

    public MpOption() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MpOption{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", isBan=" + isBan +
                ", score=" + score +
                ", status=" + status +
                '}';
    }

    //实体类转换为json
    public JSONObject convertToJson() throws JsonProcessingException, JSONException {
        JSONObject json=new JSONObject();
        if(getId()!=null){
            json.put("id",getId());
        }else{
            json.put("id","");
        }
        if(getQuestionId()!=null){
            json.put("questionId",getQuestionId());
        }else{
            json.put("questionId","");
        }
        if(StringUtils.isNotEmpty(getContent())){
            json.put("name",getContent());
        }else{
            json.put("name","");
        }
        if(getScore()!=null){
            json.put("score",getScore());
        }else{
            json.put("score","");
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

