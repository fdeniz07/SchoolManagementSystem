package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.Dean;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.dto.DeanDto;
import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.response.DeanResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.DeanRepository;
import com.schoolmanagement.utils.CheckParameterUpdateMethod;
import com.schoolmanagement.utils.CheckUniqueFields;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service //service yazmazsak Bean olmaz, bean olmazsa DI yapamayiz
@RequiredArgsConstructor
public class DeanService {

    private final DeanRepository deanRepository;
    private final DeanDto deanDto;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final CheckUniqueFields checkUniqueFields;

    //Not: save() *********************************************************************************************************************************
    public ResponseMessage<DeanResponse> save(DeanRequest deanRequest) {

        //!!! Dublicate kontrolü (unique alanlar daha önceden kullanilmamis olmali) --checkDublucate -->utils altina tasinabilir
        checkUniqueFields.checkDuplicate(deanRequest.getUsername(),
                deanRequest.getSsn(),
                deanRequest.getPhoneNumber());

        //!!! DTO - POJO Dönüsümü --> dto dan gelen bilgileri db ye kaydetmek icin dönüüsm
        Dean dean = createDtoForDean(deanRequest);

        //Role ve password bilgileri uygun sekilde setleniyor
        dean.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        dean.setPassword(passwordEncoder.encode(dean.getPassword()));

        //!!! DB'ye kayit
        Dean saveDean = deanRepository.save(dean); //aslinda geri döndürmek mecburi degil ama frontend de gözüksün diye gönderiyoruz

        return ResponseMessage.<DeanResponse>builder() //kaydedilen datayi entity den dto ya cevirip frontend'e gönderiyoruz
                .message("Dean saved")
                .httpStatus(HttpStatus.CREATED)
                .object(createDeanResponse(saveDean)) //yardimci metot
                .build();
    }

    private Dean createDtoForDean(DeanRequest deanRequest) { //createDtoToPOJO

        return deanDto.dtoDean(deanRequest); //Injection yaparak (1.Yol - kisa) --> Tüm bean ler bir klasörde toplanmali(config-->CreateObjectBean)

    }

    private DeanResponse createDeanResponse(Dean dean) {

        return DeanResponse.builder()  //injection yapmadan / builder ile (2.Yol - uzun)
                .userId(dean.getId())
                .username(dean.getUsername())
                .name(dean.getName())
                .surname(dean.getSurname())
                .birthDay(dean.getBirthDay())
                .birthPlace(dean.getBirthPlace())
                .phoneNumber(dean.getPhoneNumber())
                .gender(dean.getGender())
                .ssn(dean.getSsn())
                .build();
    }

    //Not: updateById() *********************************************************************************************************************************
    public ResponseMessage<DeanResponse> update(DeanRequest newDean, Long deanId) {

        //  checkDeanExists(deanId);  // tekrarlanan kisim icin
        Optional<Dean> dean = deanRepository.findById(deanId);

        //dean object bos olma kontrolü
        if (!dean.isPresent()) { //isPresent yerine isEmpty de kullanilabilir (o zaman ! gerek yok)

            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, deanId));
        } else if (!CheckParameterUpdateMethod.checkParameter(dean.get(), newDean)) { //dean.get() optional yapi oldugu icin dean'a get metodu ile ulasabiliriz

            checkUniqueFields.checkDuplicate(newDean.getUsername(), newDean.getSsn(), newDean.getPhoneNumber()); //tek parametre degistirildiginde senaryo postmande test edilmeli

        }

        //!!! Güncellenen yeni bilgiler ile Dean objesinin kaydediyoruz
        Dean updatedDean = createUpdatedDean(newDean, deanId);
        updatedDean.setPassword(passwordEncoder.encode(newDean.getPassword())); //request'den gelen plain text formatindaki password'u encode eder
        deanRepository.save(updatedDean);

        return ResponseMessage.<DeanResponse>builder()
                .message("Dean Updated Successfuly")
                .httpStatus(HttpStatus.OK)
                .object(createDeanResponse(updatedDean))
                .build();
    }

    //Yardimci method
    private Dean createUpdatedDean(DeanRequest deanRequest, Long managerId) {

        return Dean.builder()
                .id(managerId)
                .username(deanRequest.getUsername())
                .ssn(deanRequest.getSsn())
                .name(deanRequest.getName())
                .surname(deanRequest.getSurname())
                .birthDay(deanRequest.getBirthDay())
                .birthPlace(deanRequest.getBirthPlace())
                .phoneNumber(deanRequest.getPhoneNumber())
                .gender(deanRequest.getGender())
                .userRole(userRoleService.getUserRole(RoleType.MANAGER))
                .build();
    }

    //Not: delete() *********************************************************************************************************************************
    public ResponseMessage<?> deleteDean(Long deanId) {

        checkDeanExists(deanId);  // tekrarlanan kisim icin

       /* Optional<Dean> dean = deanRepository.findById(deanId);

        //dean object bos olma kontrolü
        if (!dean.isPresent()) { //isPresent yerine isEmpty de kullanilabilir (o zaman ! gerek yok)

            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, deanId));
        }*/

        deanRepository.deleteById(deanId);

        return ResponseMessage.builder()
                .message("Dean deleted")
                .httpStatus(HttpStatus.OK) //object geri dönmüyoruz, cünkü ortada object yok :))
                .build();
    }

    // Not: getById() *******************************************************************************************************************************
    public ResponseMessage<DeanResponse> getDeanById(Long deanId) {

        // ODEV : asagida goz kanatan kod grubu methoid haline cevrilip cagirilacak

        checkDeanExists(deanId);  // tekrarlanan kisim icin

        /*Optional<Dean> dean = deanRepository.findById(deanId);

        if (!dean.isPresent()) { // isEmpty() de kullanilabilir

            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, deanId));
        }*/

        return ResponseMessage.<DeanResponse>builder()
                .message("Dean Successfully found")
                .httpStatus(HttpStatus.OK)
                .object(createDeanResponse(checkDeanExists(deanId).get()))// .object(createDeanResponse(dean.get()))
                .build();

    }

    //Not: getAll() *********************************************************************************************************************************
    public List<DeanResponse> getAllDean() {

        //return List<Dean> deans = deanRepository.findAll();

        return deanRepository.findAll()
                .stream()
                .map(this::createDeanResponse)
                .collect(Collectors.toList());
    }

    //Not: search() *********************************************************************************************************************************
    public Page<DeanResponse> search(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        //Eger default degerler geliyorsa yani hicbir deger girilmediyse asagidaki kod calisir
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return deanRepository.findAll(pageable).map(this::createDeanResponse);
    }


    // Not: tekrarlanan kod blogu icin yazilan method
    private Optional<Dean> checkDeanExists(Long deanId) {
        Optional<Dean> dean = deanRepository.findById(deanId);
        if (!dean.isPresent()) {
            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, deanId));
        }
        return dean;
    }

}






























