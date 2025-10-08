package com.yy.my_tutor.math.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.math.domain.Question;
import com.yy.my_tutor.math.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 问题控制器
 */
@RestController
@RequestMapping("/api/math/question")
@CrossOrigin(origins = "*")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    /**
     * 查询所有问题
     */
    @GetMapping("/list")
    public RespResult<List<Question>> findAllQuestions() {
        List<Question> questions = questionService.findAllQuestions();
        return RespResult.success(questions);
    }
    
    /**
     * 根据ID查询问题
     */
    @GetMapping("/{id}")
    public RespResult<Question> findQuestionById(@PathVariable Integer id) {
        Question question = questionService.findQuestionById(id);
        if (question != null) {
            return RespResult.success(question);
        } else {
            return RespResult.error("问题不存在");
        }
    }
    
    /**
     * 根据知识点ID查询问题
     */
    @GetMapping("/knowledge/{knowledgePointId}")
    public RespResult<List<Question>> findQuestionsByKnowledgePointId(@PathVariable Integer knowledgePointId) {
        List<Question> questions = questionService.findQuestionsByKnowledgePointId(knowledgePointId);
        return RespResult.success(questions);
    }
    
    /**
     * 根据题目类型查询问题
     */
    @GetMapping("/type/{questionType}")
    public RespResult<List<Question>> findQuestionsByType(@PathVariable Integer questionType) {
        List<Question> questions = questionService.findQuestionsByType(questionType);
        return RespResult.success(questions);
    }
    
    /**
     * 根据难度等级查询问题
     */
    @GetMapping("/difficulty/{difficultyLevel}")
    public RespResult<List<Question>> findQuestionsByDifficulty(@PathVariable Integer difficultyLevel) {
        List<Question> questions = questionService.findQuestionsByDifficulty(difficultyLevel);
        return RespResult.success(questions);
    }
    
    /**
     * 根据知识点和难度查询问题
     */
    @GetMapping("/knowledge/{knowledgePointId}/difficulty/{difficultyLevel}")
    public RespResult<List<Question>> findQuestionsByKnowledgeAndDifficulty(
            @PathVariable Integer knowledgePointId, @PathVariable Integer difficultyLevel) {
        List<Question> questions = questionService.findQuestionsByKnowledgeAndDifficulty(knowledgePointId, difficultyLevel);
        return RespResult.success(questions);
    }
    
    /**
     * 随机获取指定数量的题目
     */
    @GetMapping("/random")
    public RespResult<List<Question>> findRandomQuestions(@RequestParam Integer knowledgePointId, 
                                                        @RequestParam(defaultValue = "10") Integer limit) {
        List<Question> questions = questionService.findRandomQuestions(knowledgePointId, limit);
        return RespResult.success(questions);
    }
    
    /**
     * 新增问题
     */
    @PostMapping("/add")
    public RespResult<Question> addQuestion(@RequestBody Question question) {
        Question result = questionService.addQuestion(question);
        if (result != null) {
            return RespResult.success("问题添加成功", result);
        } else {
            return RespResult.error("问题添加失败");
        }
    }
    
    /**
     * 更新问题
     */
    @PutMapping("/update")
    public RespResult<Question> updateQuestion(@RequestBody Question question) {
        Question result = questionService.updateQuestion(question);
        if (result != null) {
            return RespResult.success("问题更新成功", result);
        } else {
            return RespResult.error("问题更新失败");
        }
    }
    
    /**
     * 删除问题
     */
    @DeleteMapping("/{id}")
    public RespResult<String> deleteQuestion(@PathVariable Integer id) {
        boolean result = questionService.deleteQuestion(id);
        if (result) {
            return RespResult.success("问题删除成功");
        } else {
            return RespResult.error("问题删除失败");
        }
    }
}
