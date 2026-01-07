package com.yy.my_tutor.course.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.course.domain.Course;
import com.yy.my_tutor.course.domain.GenerateCourseRequest;
import com.yy.my_tutor.course.service.CourseGenerateService;
import com.yy.my_tutor.course.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程控制器
 */
@Slf4j
@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @Resource
    private CourseGenerateService courseGenerateService;

    /**
     * 根据知识点生成课程
     * POST /course/generate
     *
     * @param request 生成课程请求参数
     * @return 生成的课程
     */
    @PostMapping("/generate")
    public RespResult<Course> generateCourse(@RequestBody GenerateCourseRequest request) {
        log.info("Generating course for request: {}", request);
        try {
            Course course = courseGenerateService.generateCourse(request);
            return RespResult.success("课程生成成功", course);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return RespResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to generate course", e);
            return RespResult.error("课程生成失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询课程
     * GET /course/{id}
     *
     * @param id 课程ID
     * @return 课程信息
     */
    @GetMapping("/{id}")
    public RespResult<Course> getCourseById(@PathVariable Integer id) {
        Course course = courseService.findById(id);
        if (course == null) {
            return RespResult.error("课程不存在");
        }
        return RespResult.success(course);
    }

    /**
     * 根据学生ID查询课程列表
     * GET /course/student/{studentId}
     *
     * @param studentId 学生ID
     * @return 课程列表
     */
    @GetMapping("/student/{studentId}")
    public RespResult<List<Course>> getCoursesByStudentId(@PathVariable Integer studentId) {
        List<Course> courses = courseService.findByStudentId(studentId);
        return RespResult.success(courses);
    }

    /**
     * 根据知识点ID查询课程列表
     * GET /course/knowledge-point/{knowledgePointId}
     *
     * @param knowledgePointId 知识点ID
     * @return 课程列表
     */
    @GetMapping("/knowledge-point/{knowledgePointId}")
    public RespResult<List<Course>> getCoursesByKnowledgePointId(@PathVariable Integer knowledgePointId) {
        List<Course> courses = courseService.findByKnowledgePointId(knowledgePointId);
        return RespResult.success(courses);
    }

    /**
     * 判断学生对某知识点的建议难度级别
     * GET /course/difficulty-level
     *
     * @param studentId        学生ID
     * @param knowledgePointId 知识点ID
     * @return 建议的难度级别
     */
    @GetMapping("/difficulty-level")
    public RespResult<Integer> getDifficultyLevel(@RequestParam Integer studentId,
                                                   @RequestParam Integer knowledgePointId) {
        Integer level = courseGenerateService.determineDifficultyLevel(studentId, knowledgePointId);
        return RespResult.success(level);
    }

    /**
     * 删除课程
     * DELETE /course/{id}
     *
     * @param id 课程ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public RespResult<Boolean> deleteCourse(@PathVariable Integer id) {
        boolean result = courseService.deleteById(id);
        if (result) {
            return RespResult.success("删除成功", true);
        }
        return RespResult.error("删除失败");
    }
}
