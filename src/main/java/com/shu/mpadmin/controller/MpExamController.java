package com.shu.mpadmin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shu.mpadmin.entity.*;
import com.shu.mpadmin.mapper.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Api(tags = "微信小程序-测试Exam接口")
@RestController
public class MpExamController {
    //日志记录器
    private static final Logger logger = LoggerFactory.getLogger(MpUserController.class);

    @Autowired
    private MpExamMapper examMapper;
    @Autowired
    private MpQuestionMapper examQuestionMapper;
    @Autowired
    private MpOptionMapper examQuestionOptionMapper;
    @Autowired
    private MpUserExamMapper userExamMapper;
    @Autowired
    private MpUserOptionMapper userOptionMapper;

    /**
     * 获取所有测试列表
     * @return
     */
    @ApiOperation("获取所有测试列表")
    @PostMapping("/shu/exam/getExamList")
    public String getExamList() throws JSONException, JsonProcessingException {
        logger.info("/shu/exam/getExamList,获取测试列表接口");
        List<MpExam> list = examMapper.selectList(null);
        JSONArray jsonArray=new JSONArray();
        for(MpExam e:list){
            //若测试没有被禁止
            if(e.getIsBan()!=1){
                JSONObject json=new JSONObject();
                json.put("examName",e.getName());
                json.put("examId",e.getId());
                json.put("examType",e.getType());
                jsonArray.put(json);
            }
        }
        return jsonArray.toString();
    }

    /**
     * 测试进度接口
     * @param userId
     * @param examId
     * @return
     * @throws JSONException
     */
    @ApiOperation("获取测试进度")
    @PostMapping("/shu/exam/getExamPageNo")
    public String getExamPageNo(Integer userId,Integer examId) throws JSONException {
        logger.info("/shu/exam/getExamPageNo,获取测试进度接口,参数：examId = "+examId+", userId = "+userId);
        JSONObject json=new JSONObject();
        //查询测试进度
        QueryWrapper<MpUserExam> query=new QueryWrapper<>();
        query.eq("user_id",userId).eq("exam_id",examId).isNull("finish_time");
        List<MpUserExam> uelist = userExamMapper.selectList(query);
        if(uelist!=null&&uelist.size()==0){
            //该用户测试都做完了。测试进度为0
            json.put("exam_pageNo",0);
        }else{
            json.put("exam_pageNo",uelist.get(0).getPageNo());
        }
        return json.toString();
    }


    /**
     * 获取问题
     * @param examId
     * @return
     */
    @ApiOperation("获取测试问题")
    @PostMapping("/shu/exam/getQuestionList")
    public String getQuestionList(Integer examId) throws JsonProcessingException, JSONException {
        logger.info("/shu/exam/getQuestionList,获取问题接口,参数：examId = "+examId);
        JSONArray jsonArray=new JSONArray();

        //查询问题列表
        QueryWrapper<MpQuestion> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("exam_id",examId);
        List<MpQuestion> list = examQuestionMapper.selectList(queryWrapper);

        for(MpQuestion e:list){
            JSONObject jsonObject = new JSONObject();
            //若该问题没有被禁止
            if(e.getStatus()!=0){
                jsonObject.put("examId",e.getExamId());
                jsonObject.put("questionId",e.getId());
                jsonObject.put("questionType",e.getType());
                jsonObject.put("name",e.getName());

                //根据questionId找对应的选项
                QueryWrapper<MpOption> queryWrapper2=new QueryWrapper<>();
                queryWrapper2.eq("question_id",e.getId());
                List<MpOption> list2 = examQuestionOptionMapper.selectList(queryWrapper2);

                JSONArray jsonArray2=new JSONArray();
                for(MpOption op:list2){
                    //若该选项没有被禁止
                    if(op.getStatus()!=0){
                        JSONObject json2=new JSONObject();
                        json2.put("questionId",op.getQuestionId());
                        json2.put("content",op.getContent());
                        json2.put("optionId",op.getId());
                        jsonArray2.put(json2);
                    }
                }
                jsonObject.put("options",jsonArray2);
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    /**
     * 单选题答题接口
     * @param userId
     * @param examId
     * @param questionId
     * @param optionId
     * @param pageNo
     * @return
     */
    @ApiOperation("单选题答题接口")
    @PostMapping("/shu/exam/danxue_Answer")
    public void danxueAnswer(Integer userId,Integer examId,Integer questionId,Integer optionId,Integer pageNo) throws JSONException {
        logger.info("/shu/exam/danxue_Answer,单选题答题接口，参数：userId="+userId+",examId="+examId+", questionId="+questionId+", optionId="+optionId+",pageNo="+pageNo);
        JSONObject json=new JSONObject();

        //根据optionId查询该选项信息
        QueryWrapper<MpOption> query2=new QueryWrapper<>();
        query2.eq("id",optionId);
        MpOption one = examQuestionOptionMapper.selectOne(query2);

        //根据examId查询题目个数
        QueryWrapper<MpQuestion> query3=new QueryWrapper<>();
        query3.eq("exam_id",examId).eq("status",1);
        List<MpQuestion> questionlist = examQuestionMapper.selectList(query3);

        //查询该用户没做完的记录
        QueryWrapper<MpUserExam> query=new QueryWrapper<>();
        query.eq("user_id",userId).eq("exam_id",examId).isNull("finish_time");
        List<MpUserExam> list = userExamMapper.selectList(query);
        if(list!=null&&list.size()==0){
            //该用户测试都做完了。创建新的测试记录
            logger.info("之前的测试都做完了。创建新的测试记录");
            MpUserExam u=new MpUserExam();
            u.setCreateTime(new Date());
            u.setExamId(examId);
            u.setPageNo(pageNo);
            u.setScore(one.getScore());
            u.setUserId(userId);
            userExamMapper.insert(u);

            //提交选项
            MpUserOption uoption=new MpUserOption();
            uoption.setExamId(examId);
            uoption.setIsDuoxue(0);
            uoption.setQuestionId(questionId);
            uoption.setOptionId(optionId);
            uoption.setUserExamId(u.getId());
            //该选项是否为正确选项
            if(one.getScore() == 1){
                uoption.setIsRight(1);
            }else if(one.getScore() == 0){
                uoption.setIsRight(0);
            }
            uoption.setUserId(userId);
            int userOptionId = userOptionMapper.insert(uoption);
        }else{
            //该用户测试尚未做完
            logger.info("该用户测试尚未做完.继续做测试");
            MpUserExam u=list.get(0);
            u.setPageNo(pageNo);
            u.setScore(u.getScore()+one.getScore());
            //若用户做到最后一题
            if(pageNo==questionlist.size()){
                logger.info("该用户做到最后一题");
                u.setFinishTime(new Date());
            }
            userExamMapper.updateById(u);

            //提交选项
            MpUserOption uoption=new MpUserOption();
            uoption.setExamId(examId);
            uoption.setIsDuoxue(0);
            uoption.setQuestionId(questionId);
            uoption.setOptionId(optionId);
            uoption.setUserExamId(u.getId());
            //该选项是否为正确选项
            if(one.getScore() == 1){
                uoption.setIsRight(1);
            }else if(one.getScore() == 0){
                uoption.setIsRight(0);
            }
            uoption.setUserId(userId);
            int userOptionId = userOptionMapper.insert(uoption);
        }

    }

    /**
     * 多选题答题接口（ps:对于多选题，只有全部选对，才能得1分）
     *
     * @param userId
     * @param examId
     * @param questionId
     * @param optionIds
     * @param pageNo
     *
     */
    @ApiOperation("多选题答题接口")
    @PostMapping("/shu/exam/duoxue_Answer")
    public void duoxue_Answer(Integer userId,Integer examId,Integer questionId,Integer[] optionIds,Integer pageNo){
        logger.info("/shu/exam/duoxue_Answer,多选题答题接口，参数：userId="+userId+",examId="+examId+", questionId="+questionId+", optionIds="+optionIds.toString()+",pageNo="+pageNo);
        JSONObject json=new JSONObject();

        //根据questionId,查询正确选项集合
        QueryWrapper<MpOption> query1 = new QueryWrapper<>();
        query1.eq("question_id",questionId).eq("score",1);
        List<MpOption> rightlist = examQuestionOptionMapper.selectList(query1);
        List<Integer> rightIds=new ArrayList<Integer>();
        for(MpOption e:rightlist){
            if(e!=null){
                rightIds.add(e.getId());
            }
        }

        //将正确选项集合与用户选择的数组进行对比
        boolean isSame;
        List<Integer> choiceIds = Arrays.asList(optionIds);
        if(choiceIds.size() == rightIds.size()){
            //判断用户选择的选项是否全部包含正确选项
            isSame = choiceIds.containsAll(rightIds);
        }else{
            isSame = false;
        }
        logger.info("用户选择的选项:"+choiceIds.toString()+", 正确选项："+rightIds.toString()+", isSame="+isSame);

        //根据examId查询题目个数
        QueryWrapper<MpQuestion> query2=new QueryWrapper<>();
        query2.eq("exam_id",examId).eq("is_ban",0);
        List<MpQuestion> questionlist = examQuestionMapper.selectList(query2);

        //先查询该用户做该测试的未完成记录
        QueryWrapper<MpUserExam> query3=new QueryWrapper<>();
        query3.eq("user_id",userId).eq("exam_id",examId).isNull("finish_time");
        List<MpUserExam> list = userExamMapper.selectList(query3);
        if(list!=null&&list.size()==0){
            //该用户测试都做完了。创建新的测试记录
            logger.info("之前的测试都做完了。创建新的测试记录");
            MpUserExam u=new MpUserExam();
            u.setCreateTime(new Date());
            u.setExamId(examId);
            u.setPageNo(pageNo);
            //若多选题全对，则得1分
            if(isSame){
                u.setScore(1.0);
            }else{
                u.setScore(0.0);
            }
            u.setUserId(userId);
            userExamMapper.insert(u);

            //提交选项
            for (int i = 0; i <optionIds.length; i++) {
                MpUserOption uoption=new MpUserOption();
                uoption.setExamId(examId);
                uoption.setIsDuoxue(1);
                uoption.setQuestionId(questionId);
                uoption.setOptionId(optionIds[i]);
                uoption.setUserExamId(u.getId());
                //该选项是否为正确选项
                if(rightIds.contains(optionIds[i])){
                    uoption.setIsRight(1);
                }else{
                    uoption.setIsRight(0);
                }
                uoption.setUserId(userId);
                int userOptionId = userOptionMapper.insert(uoption);
            }

        }else{
            //该用户测试尚未做完
            logger.info("该用户测试尚未做完.继续做测试");
            MpUserExam u=list.get(0);
            u.setPageNo(pageNo);
            if(isSame){
                u.setScore(u.getScore()+1);
            }else{
            }
            //若用户做到最后一题
            if(pageNo==questionlist.size()){
                logger.info("该用户做到最后一题");
                u.setFinishTime(new Date());
            }
            userExamMapper.updateById(u);

            //提交选项
            for (int i = 0; i <optionIds.length; i++) {
                MpUserOption uoption=new MpUserOption();
                uoption.setExamId(examId);
                uoption.setIsDuoxue(1);
                uoption.setQuestionId(questionId);
                uoption.setOptionId(optionIds[i]);
                uoption.setUserExamId(u.getId());
                //该选项是否为正确选项
                if(rightIds.contains(optionIds[i])){
                    uoption.setIsRight(1);
                }else{
                    uoption.setIsRight(0);
                }
                uoption.setUserId(userId);
                int userOptionId = userOptionMapper.insert(uoption);
            }
        }
    }

    /**
     * 测试结果接口
     * @param userId
     * @param examId
     * @return
     */
    @ApiOperation("测试结果接口")
    @PostMapping("/shu/exam/result")
    public String result(Integer userId,Integer examId) throws JSONException {
        logger.info("/shu/exam/result,测试结果接口，参数：userId="+userId+",examId="+examId);
        JSONArray jsonArray=new JSONArray();
        JSONObject json=new JSONObject();
        //查询最近的测试记录
        QueryWrapper<MpUserExam> query1=new QueryWrapper<>();
        query1.eq("user_id",userId).eq("exam_id",examId).isNotNull("finish_time");
        List<MpUserExam> list1 = userExamMapper.selectList(query1);
        MpUserExam last=list1.get(list1.size()-1);

        json.put("right_num",last.getScore());
        json.put("error_num",last.getPageNo()-last.getScore());
        json.put("user_exam_id",last.getId());
        jsonArray.put(json);

        //查询全部历史记录
        QueryWrapper<MpUserExam> query2=new QueryWrapper<>();
        query1.eq("user_id",userId).eq("exam_id",examId).isNotNull("finish_time");
        List<MpUserExam> list2 = userExamMapper.selectList(query1);
        JSONArray jsonArray2=new JSONArray();
        for(MpUserExam u:list2){
            JSONObject json2=new JSONObject();
            json2.put("sum_num",u.getPageNo());
            json2.put("right_num",u.getScore());
            json2.put("error_num",u.getPageNo()-u.getScore());
            //转换时间格式
            SimpleDateFormat simpleDate=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            System.out.println(u);
            String time = simpleDate.format(u.getFinishTime());
            json2.put("time_num",time);
            jsonArray2.put(json2);
        }
        jsonArray.put(jsonArray2);

        //返给前台数据
        return jsonArray.toString();
    }

    /**
     * 获取全部测试的历史记录
     * @param userId
     * @return
     */
    @ApiOperation("获取全部测试的历史记录接口")
    @PostMapping("/shu/exam/history")
    public String getHistoryList(Integer userId) throws JSONException {
        logger.info("/shu/exam/history,获取全部测试的历史记录。参数：userId = "+userId);
        JSONArray jsonArray=new JSONArray();

        //获取数据库中全部测试的examid
        List<Integer> examids=new  ArrayList<Integer>();
        List<MpExam> examEntities = examMapper.selectAll();
        for(MpExam e:examEntities){
            examids.add(e.getId());
        }

        //根据测试id找出该用户最近的测试记录
        for(Integer i:examids){
            JSONObject json=new JSONObject();
            QueryWrapper<MpUserExam> query=new QueryWrapper<>();
            query.eq("user_id",userId).isNotNull("finish_time").eq("exam_id",i).orderByDesc("finish_time");
            List<MpUserExam> userExamEntities = userExamMapper.selectList(query);
            //查询测试信息
            if(userExamEntities!=null&&userExamEntities.size()>0){
                MpExam MpExam = examMapper.selectById(userExamEntities.get(0).getExamId());
                json.put("examId",userExamEntities.get(0).getExamId());
                json.put("examName",MpExam.getName());
                SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String format = simple.format(userExamEntities.get(0).getFinishTime());
                json.put("finishTime",format);
                jsonArray.put(json);
            }
        }
        return jsonArray.toString();
    }

    /**
     * 用户选择的答案分析接口
     * @param userId
     * @param examId
     * @return
     */
    @ApiOperation("用户选择的答案分析接口")
    @PostMapping("/shu/exam/questionAnalyse")
    public String questionAnalyse(Integer userId,Integer examId) throws JSONException {
        logger.info("/shu/exam/questionAnalyse,答案分析接口。参数：userId = "+userId+",examId = "+examId);
        JSONArray jsonArray=new JSONArray();

        //获取该用户最近一次的user_exam_id
        QueryWrapper<MpUserExam> query=new QueryWrapper<>();
        query.eq("user_id",userId).isNotNull("finish_time").eq("exam_id",examId).orderByDesc("finish_time");
        List<MpUserExam> userExamEntities = userExamMapper.selectList(query);
        Integer userExamId = userExamEntities.get(0).getId();

        //根据examid获取对应的所有questionid
        QueryWrapper<MpQuestion> query2=new QueryWrapper<>();
        query2.eq("exam_id",examId).ne("is_ban",1);
        List<MpQuestion> questionList = examQuestionMapper.selectList(query2);

        for(MpQuestion e:questionList){
            JSONObject json=new JSONObject();
            json.put("questionId",e.getId());
            Integer qid=e.getId();
            Integer qtypeId = e.getTypeId();

            //获取该用户测试的选项数据
            QueryWrapper<MpUserOption> query3=new QueryWrapper<>();
            query3.eq("user_exam_id",userExamId).eq("question_id",qid);
            List<MpUserOption> uOption = userOptionMapper.selectList(query3);

            if(qtypeId==1){
                //单选题
                Integer isRight = uOption.get(0).getIsRight();
                if(isRight==1){
                    json.put("isAnswerCorrect",1);
                }else{
                    json.put("isAnswerCorrect",0);
                }
            }else if(qtypeId==2){
                //多选题
                //查询用户选择的选项集合
                List<Integer> choiceIds=new ArrayList<>();
                for(MpUserOption ue:uOption){
                    choiceIds.add(ue.getOptionId());
                }
                //根据questionId,查询正确选项集合
                QueryWrapper<MpOption> query4 = new QueryWrapper<>();
                query4.eq("question_id",qid).eq("score",1);
                List<MpOption> rightlist = examQuestionOptionMapper.selectList(query4);
                List<Integer> rightIds=new ArrayList<Integer>();
                for(MpOption object:rightlist){
                    if(object!=null){
                        rightIds.add(object.getId());
                    }
                }
                //将正确选项集合与用户选择的数组进行对比
                boolean isSame;
                if(choiceIds.size() == rightIds.size()){
                    //判断用户选择的选项是否全部包含正确选项
                    isSame = choiceIds.containsAll(rightIds);
                }else{
                    isSame = false;
                }
                if(isSame){
                    json.put("isAnswerCorrect",1);
                }else{
                    json.put("isAnswerCorrect",0);
                }
            }
            jsonArray.put(json);
        }
        return jsonArray.toString();
    }

    /**
     * 用户选择的选项分析接口
     * @param questionId
     * @return
     */
    @ApiOperation("用户选择的选项分析接口")
    @PostMapping("/shu/exam/optionAnalyse")
    public String optionAnalyse(Integer userExamId,Integer questionId) throws JSONException {
        logger.info("/shu/exam/optionAnalyse,用户选择的选项分析接口,参数：questionId="+questionId);
        JSONArray jsonArray=new JSONArray();
        JSONObject json=new JSONObject();

        //用户选择的选项集合
        List<String> choiceNums=new ArrayList<>();

        //查询用户选择的选项id集合
        List<Integer> choiceIds=new ArrayList<>();
        QueryWrapper<MpUserOption> query=new QueryWrapper<>();
        query.eq("user_exam_id",userExamId).eq("question_id",questionId);
        List<MpUserOption> uoptions = userOptionMapper.selectList(query);
        for(MpUserOption ue:uoptions){
            if(ue!=null){
                choiceIds.add(ue.getOptionId());
            }
        }
        //根据question_id查询问题内容
        MpQuestion MpQuestion = examQuestionMapper.selectById(questionId);
        json.put("question_name",MpQuestion.getName());
        jsonArray.put(json);
        //根据question_id查询选项内容
        QueryWrapper<MpOption> query1=new QueryWrapper<>();
        query1.eq("question_id",questionId);
        List<MpOption> list = examQuestionOptionMapper.selectList(query1);
        JSONArray jsonArray2=new JSONArray();
        Integer num=0;
        for(MpOption e:list){
            char option_num=(char)('A'+num);
            JSONObject json2=new JSONObject();
            json2.put("option_num",option_num+"");
            json2.put("option_name",e.getContent());
            if(e.getScore()>0){
                json2.put("isRight",1);
            }else{
                json2.put("isRight",0);
            }
            if(choiceIds.contains(e.getId())){
                choiceNums.add(option_num+"");
            }
            jsonArray2.put(json2);
            num++;
        }
        jsonArray.put(jsonArray2);

        //描述文本
        JSONObject json3=new JSONObject();
        json3.put("text",choiceNums.toString());
        jsonArray.put(json3);

        return jsonArray.toString();
    }

}
