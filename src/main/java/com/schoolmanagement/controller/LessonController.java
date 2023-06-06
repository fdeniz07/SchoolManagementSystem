package com.schoolmanagement.controller;

import com.schoolmanagement.entity.concretes.Lesson;
import com.schoolmanagement.payload.request.LessonRequest;
import com.schoolmanagement.payload.response.LessonResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    //Not: save() **************************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @PostMapping("/save") //http://localhost:8080/lessons/save
    public ResponseMessage<LessonResponse> save(@RequestBody @Valid LessonRequest lesson) {

        return lessonService.save(lesson);
    }

    // Not :  delete() ************************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @DeleteMapping("/delete/{id}") //http://localhost:8080/lessons/delete/1
    public ResponseMessage deleteLesson(@PathVariable Long id) {
        return lessonService.deleteLesson(id);
    }

    //Not: getLessonByName() ******************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @RequestMapping("/getLessonByName") //http://localhost:8080/lessons/getLessonByName?lessonName=Math
    public  ResponseMessage<LessonResponse> getLessonByLessonName(@RequestParam String lessonName){
        return lessonService.getLessonByLessonName(lessonName);
    }

    //Not: getAllLesson() **********************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')") //TODO --> TEACHER ya da STUDENT eklenebilir mi
    @GetMapping("/getAll")  // http://localhost:8080/lessons/getAll
    public List<LessonResponse> getAllLesson() {
        return lessonService.getAllLesson();
    }

    //Not: getAllWithPage() ********************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @GetMapping("/search") // http://localhost:8080/lessons/search
    public Page<LessonResponse> search(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "startDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return lessonService.search(page,size,sort,type);
    }

    //Not: getAllLessonByLessonIds() *************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @GetMapping("/getAllLessonByLessonId")  // http://localhost:8080/lessons/getAllLessonByLessonId
    public Set<Lesson> getAllLessonByLessonId(@RequestParam(name = "lessonId") Set<Long> idList){
        return lessonService.getLessonByLessonIdList(idList);
    }

    //TODO : Update metoduda yazilacak
}





























