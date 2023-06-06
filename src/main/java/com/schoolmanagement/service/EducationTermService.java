package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.EducationTerm;
import com.schoolmanagement.entity.concretes.ViceDean;
import com.schoolmanagement.exception.InvalidTimeException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.request.EducationTermRequest;
import com.schoolmanagement.payload.response.EducationTermResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.EducationTermRepository;
import com.schoolmanagement.utils.CheckParameterUpdateMethod;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationTermService {

    private final EducationTermRepository educationTermRepository;

    //Not: save() **************************************************************************************************************************************
    public ResponseMessage<EducationTermResponse> save(EducationTermRequest request) {

        //!!! Son Kayit tarihi, ders döneminin baslangic tarihinden önce olmalidir
        if (request.getLastRegistrationDate().isAfter(request.getStartDate())) {
            throw new InvalidTimeException(Messages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
        }

        //!!! Bitis tarihi baslangic tarihinden büyük olmamali
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new InvalidTimeException(Messages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
        }

        //!!! Ayni term ve baslangic tarihine sahip birden fazla kayit var mi kontrolü
        if (educationTermRepository.existsByTermAndYear(request.getTerm(), request.getStartDate().getYear())) {
            throw new InvalidTimeException(Messages.EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE);
        }

        //!!! save metoduna dto --> POJO dönüsümü yapip gönderiyoruz
        EducationTerm savedEducationterm = educationTermRepository.save(createEducationTerm(request));

        //!!! Response objesini olusturuluyor
        return ResponseMessage.<EducationTermResponse>builder()
                .message("Education Term created")
                .object(createEducationTermResponse(savedEducationterm))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    // Dto to Pojo
    private EducationTerm createEducationTerm(EducationTermRequest request) {

        return EducationTerm.builder()
                .term(request.getTerm())
                .startDate(request.getStartDate())
                .endDate(request.getLastRegistrationDate())
                .lastRegistrationDate(request.getLastRegistrationDate())
                .build();
    }

    // Pojo to Dto
    private EducationTermResponse createEducationTermResponse(EducationTerm response) {
        return EducationTermResponse.builder()
                .id(response.getId())
                .term(response.getTerm())
                .startDate(response.getStartDate())
                .endDate(response.getEndDate())
                .lastRegistrationDate(response.getLastRegistrationDate())
                .build();
    }

    //Not: getById() *************************************************************************************************************************************
    public EducationTermResponse get(Long id) {

        //!!! ya yoksa kontrolü
        //if (educationTermRepository.existsById(id)); //bu metot isimizi görür ama biz kendimiz yazmak istersek
        if (educationTermRepository.existsByIdEquals(id)) {
            throw new ResourceNotFoundException(String.format(Messages.EDUCATION_TERM_NOT_FOUND_MESSAGE));
        }

        //!!! POJO --> DTO dönüsümü ile response hazirlaniyor
        return createEducationTermResponse(educationTermRepository.findByIdEquals(id));  // findById de calisir ama biz elle yaziyoruz PQL yazmak icin
    }

    //Not: getAll() *************************************************************************************************************************************
    public List<EducationTermResponse> getAll() {

        return educationTermRepository.findAll()
                .stream()
                .map(this::createEducationTermResponse)
                .collect(Collectors.toList());

    }

    // Not :  getAllWithPage() ****************************************************************************************************************************
    public Page<EducationTermResponse> getAllWithPage(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return educationTermRepository.findAll(pageable).map(this::createEducationTermResponse);
    }

    // Not :  delete() ************************************************************************************************************************************
    public ResponseMessage<?> delete(Long id) {

        //!!! Acaba var mi kontrolü
        if (!educationTermRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(Messages.EDUCATION_TERM_NOT_FOUND_MESSAGE, id)); //Bu id yoksa hata firlat
        }

        educationTermRepository.deleteById(id);

        return ResponseMessage.builder()
                .message("Education Term deleted successfuly")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not :  updateById() ********************************************************************************************************************************
    public ResponseMessage<EducationTermResponse> update(EducationTermRequest request, Long id) {

        //!!! id kontrolü
        if (!educationTermRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(Messages.EDUCATION_TERM_NOT_FOUND_MESSAGE, id));
        }

        // !!! getStartDate ve lastRegistrationDate kontrolu
        if (request.getStartDate() != null && request.getLastRegistrationDate() != null) {
            if (request.getLastRegistrationDate().isAfter(request.getStartDate())) {
                throw new ResourceNotFoundException(Messages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
            }
        }

        // !!! startDate-endDate kontrolu
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getEndDate().isBefore(request.getStartDate())) {
                throw new ResourceNotFoundException(Messages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
            }
        }
        EducationTerm updatedEducationTerm = createUpdatedEducationTerm(request, id);
        educationTermRepository.save(updatedEducationTerm);

        //!!! Response objesini olusturuluyor
        ResponseMessage.ResponseMessageBuilder<EducationTermResponse> responseMessageBuilder = ResponseMessage.builder();
        return responseMessageBuilder
                .object(createEducationTermResponse(updatedEducationTerm))
                .httpStatus(HttpStatus.CREATED)
                .message("Education Term Updated Successfully")
                .build();
    }

    //DTO --> POJO
    private EducationTerm createUpdatedEducationTerm(EducationTermRequest request, Long id) {
        return EducationTerm.builder()
                .id(id)
                .term(request.getTerm())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .lastRegistrationDate(request.getLastRegistrationDate())
                .build();
    }

   // EDUCATION-TERM-SERVICE
// ODEV-1 : ya yoksa kontrolleri method uzerinden cagrilmali
// ODEV-2 : save ve update methodalrindaki tarih kontrolleri ayri bir method uzerinden cagrilmali
}






























