package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.LessonProgramRequest;
import com.schoolmanagement.payload.response.LessonProgramResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.service.EducationTermService;
import com.schoolmanagement.service.LessonProgramService;
import com.schoolmanagement.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lessonProgram")
@RequiredArgsConstructor
public class LessonProgramController {

    private final LessonProgramService lessonProgramService;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;

    //Not: save() *************************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PostMapping("/save") //http://localhost:8080/lessonProgram/save
    public ResponseMessage<LessonProgramResponse> save(@RequestBody @Valid LessonProgramRequest request) {

        return lessonProgramService.save(request);
    }

    // NOT : getAll() *********************************************************************************************************************************
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN' , 'MANAGER' , 'ASSISTANT_MANAGER' , 'TEACHER' , 'STUDENT')")
    public List<LessonProgramResponse> getAll() {

        return lessonProgramService.getAllLessonProgram();
    }

    // Not :  getById() ********************************************************************************************************************************
    @GetMapping("/getById/{id}") //http://localhost:8080/lessonPrograms/getById/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public LessonProgramResponse getById(@PathVariable Long id) {

        return lessonProgramService.getByLessonProgramId(id);
    }

    // Not :  getAllLessonProgramUnassigned() ************************************************************************************************************
    @GetMapping("/getAllUnassigned") //http://localhost:8080/lessonPrograms/getAllUnassigned
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public List<LessonProgramResponse> getAllUnassigned() {

        return lessonProgramService.getAllLessonProgramUnassigned();
    }

    // Not :  getAllLessonProgramAssigned() ***************************************************************************************************************
    @GetMapping("/getAllAssigned") //http://localhost:8080/lessonPrograms/getAllAssigned
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public List<LessonProgramResponse> getAllAssigned() {

        return lessonProgramService.getAllLessonProgramAssigned();
    }

    // Not :  Delete() *************************************************************************************************************************************
    @DeleteMapping("/delete/{id}") //http://localhost:8080/lessonPrograms/delete/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage delete(@PathVariable Long id) {

        return lessonProgramService.deleteLessonProgram(id);
    }

    // Not :  getLessonProgramByTeacher() *******************************************************************************************************************
    @GetMapping("/getAllLessonProgramByTeacher") //http://localhost:8080/getAllLessonProgramByTeacher
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public Set<LessonProgramResponse> getLessonProgramByTeacher(HttpServletRequest httpServletRequest) {

        String username = (String) httpServletRequest.getAttribute("username");
        return lessonProgramService.getLessonProgramByTeacher(username);
    }

    // Not :  getLessonProgramByStudent() *******************************************************************************************************************
    @GetMapping("/getAllLessonProgramByStudent") //http://localhost:8080/getAllLessonProgramByStudent
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public Set<LessonProgramResponse> getLessonProgramByStudent(HttpServletRequest httpServletRequest) {

        String username = (String) httpServletRequest.getAttribute("username");
        return lessonProgramService.getLessonProgramByStudent(username);
    }

    // Not :  getAllWithPage() *******************************************************************************************************************************
    @GetMapping("/search") //http://localhost:8080/search
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public Page<LessonProgramResponse> search(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "startDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return lessonProgramService.search(page,size,sort,type);
    }
}

























