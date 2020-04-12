package com.shu.mpadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("mp_user_option")
public class MpUserOption {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    private Integer userId;
    @TableField("exam_id")
    private Integer examId;
    @TableField("user_exam_id")
    private Integer userExamId;
    @TableField("question_id")
    private Integer questionId;
    @TableField("option_id")
    private Integer optionId;
    @TableField("is_duoxue")
    private Integer isDuoxue;
    @TableField("is_right")
    private Integer isRight;

    public MpUserOption() {
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
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

    public Integer getUserExamId() {
        return userExamId;
    }

    public void setUserExamId(Integer userExamId) {
        this.userExamId = userExamId;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public Integer getIsDuoxue() {
        return isDuoxue;
    }

    public void setIsDuoxue(Integer isDuoxue) {
        this.isDuoxue = isDuoxue;
    }

    public Integer getIsRight() {
        return isRight;
    }

    public void setIsRight(Integer isRight) {
        this.isRight = isRight;
    }

    @Override
    public String toString() {
        return "UserOptionEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", examId=" + examId +
                ", userExamId=" + userExamId +
                ", questionId=" + questionId +
                ", optionId=" + optionId +
                ", isDuoxue=" + isDuoxue +
                ", isRight=" + isRight +
                '}';
    }
}
