package com.shu.mpadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("mp_user_exam")
public class MpUserExam {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    private Integer userId;
    @TableField("exam_id")
    private Integer examId;
    @TableField("page_no")
    private Integer pageNo;
    @TableField("score")
    private Double score;
    @TableField("create_time")
    private Date createTime;
    @TableField("finish_time")
    private Date finishTime;

    public MpUserExam() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return "UserExamEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", examId=" + examId +
                ", pageNo=" + pageNo +
                ", score=" + score +
                ", createTime=" + createTime +
                ", finishTime=" + finishTime +
                '}';
    }
}

