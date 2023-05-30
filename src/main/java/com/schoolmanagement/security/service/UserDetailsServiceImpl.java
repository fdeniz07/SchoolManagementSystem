package com.schoolmanagement.security.service;

import com.schoolmanagement.entity.concretes.*;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService { //Bu sinif ile UserDetailImpl sinifimizi security'e tanitiyoruz

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final ViceDeanRepository viceDeanRepository;
    private final DeanRepository deanRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Student student = studentRepository.findByUsernameEquals(username);
        Teacher teacher = teacherRepository.findByUsernameEquals(username);
        Dean dean = deanRepository.findByUsernameEquals(username);
        ViceDean viceDean = viceDeanRepository.findByUsernameEquals(username);
        Admin admin = adminRepository.findByUsernameEquals(username);

        if (student != null) {
            return new UserDetailsImpl(student.getId(),
                    student.getUsername(),
                    student.getName(),
                    false,
                    student.getPassword(),
                    student.getUserRole().getRoleType().name() //Enum tipinin adini almamiz gerekiyor(Admin,Dean,...)
            );
        } else if (teacher != null) {
            return new UserDetailsImpl(teacher.getId(),
                    teacher.getUsername(),
                    teacher.getName(),
                    teacher.getIsAdvisor(),
                    teacher.getPassword(),
                    teacher.getUserRole().getRoleType().name()
            );
        } else if (dean != null) {
            return new UserDetailsImpl(dean.getId(),
                    dean.getUsername(),
                    dean.getName(),
                    false,
                    dean.getPassword(),
                    dean.getUserRole().getRoleType().name()
            );
        } else if (viceDean != null) {
            return new UserDetailsImpl(viceDean.getId(),
                    viceDean.getUsername(),
                    viceDean.getName(),
                    false,
                    viceDean.getPassword(),
                    viceDean.getUserRole().getRoleType().name()
            );
        } else if (admin != null) {
            return new UserDetailsImpl(admin.getId(),
                    admin.getUsername(),
                    admin.getName(),
                    false,
                    admin.getPassword(),
                    admin.getUserRole().getRoleType().name()
            );
        }
        //TODO --> Security katmani icin exception handle class'i olusturulacak
        throw new ConflictException("User not found");
    }
}
