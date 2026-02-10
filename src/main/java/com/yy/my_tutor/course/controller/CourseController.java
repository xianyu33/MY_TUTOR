package com.yy.my_tutor.course.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.course.domain.Course;
import com.yy.my_tutor.course.domain.GenerateCourseRequest;
import com.yy.my_tutor.course.service.CourseGenerateService;
import com.yy.my_tutor.course.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @Resource
    private CourseGenerateService courseGenerateService;

    /**
     * 根据知识点生成课程
     * POST /api/course/generate
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
        } catch (IllegalStateException e) {
            log.warn("Stage validation failed: {}", e.getMessage());
            return RespResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to generate course", e);
            return RespResult.error("课程生成失败: " + e.getMessage());
        }
    }

    /**
     * 流式生成课程（SSE）
     * POST /api/course/generate/stream
     */
    @PostMapping(value = "/generate/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generateCourseStream(@RequestBody GenerateCourseRequest request) {
        log.info("Streaming course generation for request: {}", request);
        return courseGenerateService.generateCourseStream(request);
    }

    /**
     * 根据学生ID和知识点ID查询课程（含指定语言内容）
     * GET /api/course/detail
     */
    @GetMapping("/detail")
    public RespResult<Course> getCourseByStudentAndKnowledgePoint(@RequestParam Integer studentId,
                                                                   @RequestParam Integer knowledgePointId,
                                                                   @RequestParam(defaultValue = "en") String language) {
        Course course = courseService.findByStudentAndKnowledgePoint(studentId, knowledgePointId);
        if (course == null) {
            return RespResult.error("课程不存在");
        }
        // 填充指定语言的内容
        Course courseWithContent = courseService.findByIdWithContent(course.getId(), language);
        return RespResult.success(courseWithContent);
    }

    /**
     * 根据ID查询课程（含指定语言内容）
     * GET /api/course/{id}
     */
    @GetMapping("/{id}")
    public RespResult<Course> getCourseById(@PathVariable Integer id,
                                             @RequestParam(defaultValue = "en") String language) {
        Course course = courseService.findByIdWithContent(id, language);
        if (course == null) {
            return RespResult.error("课程不存在");
        }
        return RespResult.success(course);
    }

    /**
     * 根据学生ID查询课程列表
     * GET /api/course/student/{studentId}
     */
    @GetMapping("/student/{studentId}")
    public RespResult<List<Course>> getCoursesByStudentId(@PathVariable Integer studentId) {
        List<Course> courses = courseService.findByStudentId(studentId);
        return RespResult.success(courses);
    }

    /**
     * 根据知识点ID查询课程列表
     * GET /api/course/knowledge-point/{knowledgePointId}
     */
    @GetMapping("/knowledge-point/{knowledgePointId}")
    public RespResult<List<Course>> getCoursesByKnowledgePointId(@PathVariable Integer knowledgePointId) {
        List<Course> courses = courseService.findByKnowledgePointId(knowledgePointId);
        return RespResult.success(courses);
    }

    /**
     * 判断学生对某知识点的建议难度级别
     * GET /api/course/difficulty-level
     */
    @GetMapping("/difficulty-level")
    public RespResult<Integer> getDifficultyLevel(@RequestParam Integer studentId,
                                                   @RequestParam Integer knowledgePointId) {
        Integer level = courseGenerateService.determineDifficultyLevel(studentId, knowledgePointId);
        return RespResult.success(level);
    }

    /**
     * 删除课程
     * DELETE /api/course/{id}
     */
    @DeleteMapping("/{id}")
    public RespResult<Boolean> deleteCourse(@PathVariable Integer id) {
        boolean result = courseService.deleteById(id);
        if (result) {
            return RespResult.success("删除成功", true);
        }
        return RespResult.error("删除失败");
    }

    /**
     * 标记当前阶段完成
     * POST /api/course/{id}/complete-stage
     */
    @PostMapping("/{id}/complete-stage")
    public RespResult<Course> completeStage(@PathVariable Integer id) {
        try {
            Course course = courseService.completeStage(id);
            return RespResult.success("阶段完成标记成功", course);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return RespResult.error(e.getMessage());
        }
    }
}
