package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.ChooseLessonTeacherRequest;
import com.schoolmanagement.payload.request.TeacherRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.TeacherResponse;
import com.schoolmanagement.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    //Not: save() *************************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @PostMapping("/save") //http://localhost:8080/teachers/save
    public ResponseMessage<TeacherResponse> save(@RequestBody @Valid TeacherRequest request) {

        return teacherService.save(request);
    }

    //Not: getAllTeacher() ****************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @PostMapping("/getAllTeacher") //http://localhost:8080/teachers/getAllTeacher
    public List<TeacherResponse> getAllTeacher() {

        return teacherService.getAllTeacher();
    }

    // Not: updateTeacherById() ************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @PutMapping("/update/{userId}")  // http://localhost:8080/teachers/update/1
    public ResponseMessage<TeacherResponse> updateTeacher(@RequestBody @Valid TeacherRequest teacher, @PathVariable Long userId) {

        return teacherService.updateTeacher(teacher, userId);
    }

    // Not: getTeacherByName() **************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @GetMapping("/getTeacherByName")
    public List<TeacherResponse> getTeacherByName(@RequestParam(name = "name") String teacherName){
        return teacherService.getTeacherByName(teacherName);

    }

    // Not: deleteTeacher() ******************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseMessage<?> deleteTeacher(@PathVariable Long id) {
        return  teacherService.deleteTeacher(id);
    }

    // Not: getTeacherById() ******************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @GetMapping("/getSavedTeacherById/{id}")
    public ResponseMessage<TeacherResponse> getSavedTeacherById(@PathVariable Long id){

        return teacherService.getSavedTeacherById(id);
    }

    // Not: getAllWithPage() ******************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @GetMapping("/getSavedTeacherById/{id}")
    public Page<TeacherResponse> search(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "startDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return teacherService.search(page,size,sort,type);
    }

    // Not: addLessonToTeachersLessonsProgram() **********************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @PostMapping("/getSavedTeacherById/{id}")
    public ResponseMessage<TeacherResponse> chooseLesson(@RequestBody @Valid ChooseLessonTeacherRequest request){

        return teacherService.chooseLesson(request);
    }

}






















