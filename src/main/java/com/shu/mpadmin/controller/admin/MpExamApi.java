package com.shu.mpadmin.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shu.mpadmin.controller.MpUserController;
import com.shu.mpadmin.entity.*;
import com.shu.mpadmin.mapper.MpExamMapper;
import com.shu.mpadmin.mapper.MpOptionMapper;
import com.shu.mpadmin.mapper.MpQuestionMapper;
import com.shu.mpadmin.mapper.MpUserMapper;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Api(tags = "后台系统-测试Exam管理接口")
@RestController
@CrossOrigin
public class MpExamApi {

    @Autowired
    private MpExamMapper examMapper;
    @Autowired
    private MpQuestionMapper questionMapper;
    @Autowired
    private MpOptionMapper optionMapper;

    /*
     *   增加-接口类型：POST
     *   删除-接口类型：DELETE
     *   修改-接口类型：PUT
     *   查找-接口类型：GET
     * */

    //日志记录器
    private static final Logger logger = LoggerFactory.getLogger(MpUserController.class);

    /**
     * 分页查询所有Exam测试
     * @param currentPage 查询当前页的数据
     * @param pageSize    每页包含多少条数据
     * @return
     * @throws JSONException
     */
    @ApiOperation("分页查询所有Exam测试")
    @GetMapping("/shu/admin/getAllExam")
    public String getAllExam(Integer currentPage,Integer pageSize) throws JSONException {
        logger.info("分页查询所有Exam测试,/shu/admin/getAllExam, param: currentPage="+currentPage+", pageSize="+pageSize);
        JSONObject json=new JSONObject();
        Page<MpExam> page=new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        IPage<MpExam> selectPage = examMapper.selectPage(page, null);
        List<MpExam> records = selectPage.getRecords();
        JSONArray jsonArray = new JSONArray();
        for (MpExam e:records){
            //根据examId 查询测试题库数量
            QueryWrapper<MpQuestion> query1 = new QueryWrapper<>();
            query1.eq("exam_id",e.getId());
            List<MpQuestion> qlist = questionMapper.selectList(query1);

            JSONObject json2=new JSONObject();
            json2.put("examId",e.getId());
            json2.put("examName",e.getName());
            json2.put("examType",e.getType());
            //转换时间格式
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simple.format(e.getCreateTime());
            json2.put("createTime",format);
            json2.put("examStatus",e.getStatus());
            json2.put("questionNumber",qlist.size());
            jsonArray.put(json2);
        }
        json.put("tableData",jsonArray);
        json.put("totalNumber",selectPage.getTotal());
        return json.toString();
    }

    @ApiOperation("新增Exam测试")
    @PostMapping("/shu/admin/exam")
    public String postExam(MpExam exam){
        logger.info("新增Exam测试/shu/admin/exam,参数 exam = "+exam.toString());
        JSONObject json=new JSONObject();
        //添加创建时间
        exam.setCreateTime(new Date());
        int a = examMapper.insert(exam);
        if (a==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("查询Exam测试")
    @GetMapping("/shu/admin/exam")
    public String getExam(Integer examId) throws JSONException {
        logger.info("查询Exam测试,/shu/admin/exam,参数 examId ="+examId);
        JSONObject json=new JSONObject();
        MpExam one = examMapper.selectById(examId);
        if(one!=null){
            json.put("isSuccess",1);
            json.put("examId",one.getId());
            json.put("examName",one.getName());
            json.put("examType",one.getType());
            json.put("examStatus",one.getStatus());
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("更新Exam测试")
    @PutMapping("/shu/admin/exam")
    public String putExam(Integer examId,String examName,String examType,Integer examStatus){
        logger.info("/shu/admin/user,更新Exam测试。参数：examId="+examId+",examName="+examName+",examType="+examType+",examStatus="+examStatus);
        MpExam one = new MpExam();
        one.setId(examId);
        one.setCreateTime(new Date());
        one.setName(examName);
        one.setStatus(examStatus);
        one.setType(examType);
        int i = examMapper.updateById(one);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("删除Exam测试")
    @DeleteMapping("/shu/admin/exam")
    public String deleteUser(Integer examId){
        logger.info("删除Exam测试/shu/admin/exam,examId = "+examId);
        JSONObject json = new JSONObject();
        //查询测试信息
        MpExam mpExam = examMapper.selectById(examId);
        //查询问题信息
        QueryWrapper<MpQuestion> query1 = new QueryWrapper<>();
        query1.eq("exam_id",examId);
        List<MpQuestion> mpQuestions = questionMapper.selectList(query1);
        //查询这些问题下的选项信息
        for(MpQuestion m:mpQuestions){
            QueryWrapper<MpOption> query2 = new QueryWrapper<>();
            query2.eq("question_id",m.getId());
            List<MpOption> mpOptions = optionMapper.selectList(query2);
            //删除问题下的选项
            for(MpOption o:mpOptions){
                optionMapper.deleteById(o.getId());
            }

        }
        //删除问题
        for(MpQuestion m:mpQuestions){
            int i = questionMapper.deleteById(m.getId());
        }
        //删除测试
        int i = examMapper.deleteById(examId);
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("分页查询Question题库")
    @GetMapping("/shu/admin/getAllQuestion")
    public String getAllQuestion(Integer examId,String questionType,Integer currentPage,Integer pageSize) throws JSONException {
        logger.info("分页查询Question题库,/shu/admin/getAllQuestion.param:" +
                "参数examId = "+examId+",questionType="+questionType+",currentPage="+currentPage+",pageSize="+pageSize);
        JSONObject json=new JSONObject();
        //查询条件
        QueryWrapper<MpQuestion> query1 = new QueryWrapper<>();
        if(examId!=null){
            query1.eq("exam_id",examId);
        }
        if(StringUtils.isNotEmpty(questionType)){
            query1.eq("type",questionType);
        }
        Page<MpQuestion> page=new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        IPage<MpQuestion> selectPage = questionMapper.selectPage(page, query1);
        if(selectPage!=null){
            //分页对象中的题库数据
            List<MpQuestion> records = selectPage.getRecords();
            JSONArray jsonArray = new JSONArray();
            for (MpQuestion e:records){
                JSONObject json2=new JSONObject();
                //根据examId查询exam数据
                Integer ed = e.getExamId();
                MpExam mpExam = examMapper.selectById(ed);

                //查询问题选项的数量
                QueryWrapper<MpOption> query2 = new QueryWrapper<>();
                query2.eq("question_id",e.getId());
                List<MpOption> mpOptions = optionMapper.selectList(query2);

                json2.put("examId",mpExam.getId());
                json2.put("examName",mpExam.getName());
                json2.put("questionId",e.getId());
                json2.put("questionName",e.getName());
                json2.put("questionType",e.getType());
                //转换时间格式
                SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = simple.format(e.getCreateTime());
                json2.put("createTime",format);
                json2.put("questionStatus",e.getStatus());
                if(mpOptions!=null){
                    json2.put("optionNumber",mpOptions.size());
                }else{
                    json2.put("optionNumber",0);
                }
                jsonArray.put(json2);
            }
            json.put("isSuccess",1);
            json.put("tableData",jsonArray);
            json.put("totalNumber",selectPage.getTotal());
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("Question题库下拉框搜索测试名称接口")
    @GetMapping("/shu/admin/getSearchExamName")
    public String getSearchExamName(){
        logger.info("Question题库下拉框搜索测试名称接口,/shu/admin/getSearchExamName");
        List<MpExam> mpExams = examMapper.selectAll();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if(mpExams!=null){
            jsonObject.put("isSuccess",1);
            for(MpExam m:mpExams){
                JSONObject json = new JSONObject();
                json.put("label",m.getName());
                json.put("value",m.getId());
                jsonArray.put(json);
            }
            jsonObject.put("examNameOptions",jsonArray);
        }else{
            jsonObject.put("isSuccess",0);
        }
        return jsonObject.toString();
    }

    @ApiOperation("更新问题Question")
    @PutMapping("/shu/admin/question")
    public String putQuestion(Integer questionId,String questionName,String questionType,Integer questionStatus){
        logger.info("/shu/admin/question,更新问题Question。参数：questionId="+questionId+",questionName="+questionName+",questionType="+questionType+",questionStatus="+questionStatus);
        MpQuestion one = new MpQuestion();
        one.setId(questionId);
        one.setCreateTime(new Date());
        one.setName(questionName);
        one.setStatus(questionStatus);
        one.setType(questionType);
        int i = questionMapper.updateById(one);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("新增问题Question")
    @PostMapping("/shu/admin/question")
    public String postQuestion(Integer examId,String questionName,String questionType,Integer questionStatus){
        logger.info("/shu/admin/question,新增问题Question。param：examId="+examId+",questionName="+questionName+",questionType="+questionType+",questionStatus="+questionStatus);
        MpQuestion one = new MpQuestion();
        one.setExamId(examId);
        one.setCreateTime(new Date());
        one.setName(questionName);
        one.setStatus(questionStatus);
        one.setType(questionType);
        int i = questionMapper.insert(one);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("删除问题Question")
    @DeleteMapping("/shu/admin/question")
    public String deleteQuestion(Integer questionId){
        logger.info("/shu/admin/question,删除问题Question。参数：questionId="+questionId);
        //查询问题下的选项信息
        QueryWrapper<MpOption> query1 = new QueryWrapper<>();
        query1.eq("question_id",questionId);
        List<MpOption> mpOptions = optionMapper.selectList(query1);
        //删除问题下的选项
        for(MpOption o:mpOptions){
            optionMapper.deleteById(o.getId());
        }
        //删除问题
        int i = questionMapper.deleteById(questionId);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("分页查询Option选项")
    @GetMapping("/shu/admin/getAllOption")
    public String getAllOption(Integer questionId,Integer currentPage,Integer pageSize) throws JSONException {
        logger.info("分页查询Option选项,/shu/admin/getAllOption.param:" +
                "questionId = "+questionId+",currentPage="+currentPage+",pageSize="+pageSize);
        JSONObject json=new JSONObject();

        //查询条件
        QueryWrapper<MpOption> query1 = new QueryWrapper<>();
        if(questionId!=null){
            query1.eq("question_id",questionId);
        }
        Page<MpOption> page=new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        IPage<MpOption> selectPage = optionMapper.selectPage(page, query1);
        if(selectPage!=null){
            //分页对象中的选项库数据
            List<MpOption> records = selectPage.getRecords();
            JSONArray jsonArray = new JSONArray();
            for (MpOption e:records){
                JSONObject json2=new JSONObject();
                //根据questionId查询question数据
                Integer qid = e.getQuestionId();
                MpQuestion mpQuestion = questionMapper.selectById(qid);

                json2.put("questionId",mpQuestion.getId());
                json2.put("questionName",mpQuestion.getName());
                json2.put("questionType",mpQuestion.getType());
                json2.put("optionId",e.getId());
                json2.put("optionContent",e.getContent());
                json2.put("optionScore",e.getScore());
                json2.put("optionStatus",e.getStatus());
                //转换时间格式
                SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = simple.format(e.getCreateTime());
                json2.put("createTime",format);

                jsonArray.put(json2);
            }
            json.put("isSuccess",1);
            json.put("tableData",jsonArray);
            json.put("totalNumber",selectPage.getTotal());
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("下拉框搜索问题编号接口")
    @GetMapping("/shu/admin/getSearchQuestionId")
    public String getSearchQuestionId(){
        logger.info("下拉框搜索问题编号接口,/shu/admin/getSearchQuestionId");
        List<MpQuestion> mpQuestions = questionMapper.selectList(null);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if(mpQuestions!=null){
            jsonObject.put("isSuccess",1);
            for(MpQuestion m:mpQuestions){
                JSONObject json = new JSONObject();
                json.put("label",m.getId());
                json.put("value",m.getId());
                jsonArray.put(json);
            }
            jsonObject.put("questionIdOptions",jsonArray);
        }else{
            jsonObject.put("isSuccess",0);
        }
        return jsonObject.toString();
    }

    @ApiOperation("修改选项Option")
    @PutMapping("/shu/admin/option")
    public String putOption(Integer optionId,String optionContent,Double optionScore,Integer optionStatus){
        logger.info("/shu/admin/option,更新选项Option。参数：optionId="+optionId+",optionContent="+optionContent+",optionScore="+optionScore+",optionStatus="+optionStatus);
        MpOption one = new MpOption();
        one.setId(optionId);
        one.setCreateTime(new Date());
        one.setContent(optionContent);
        one.setStatus(optionStatus);
        one.setScore(optionScore);
        int i = optionMapper.updateById(one);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("新增选项Option")
    @PostMapping("/shu/admin/option")
    public String postOption(Integer questionId,String optionContent,Double optionScore,Integer optionStatus){
        logger.info("/shu/admin/option,新增选项Option。param：questionId="+questionId+",optionContent="+optionContent+",optionScore="+optionScore+",optionStatus="+optionStatus);
        MpOption one = new MpOption();
        one.setQuestionId(questionId);
        one.setCreateTime(new Date());
        one.setContent(optionContent);
        one.setStatus(optionStatus);
        one.setScore(optionScore);
        int i = optionMapper.insert(one);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }

    @ApiOperation("删除选项Option")
    @DeleteMapping("/shu/admin/option")
    public String deleteOption(Integer optionId){
        logger.info("/shu/admin/option,删除选项Option。参数：optionId="+optionId);
        int i = optionMapper.deleteById(optionId);
        JSONObject json = new JSONObject();
        if(i==1){
            json.put("isSuccess",1);
        }else{
            json.put("isSuccess",0);
        }
        return json.toString();
    }



}
