package com.schoolmanagement.utils;

import com.schoolmanagement.exception.InvalidTimeException;
import com.schoolmanagement.payload.request.EducationTermRequest;

public class CheckValidDates {

    public static void checkLastRegistrationDate(EducationTermRequest request){

        //!!! Son Kayit tarihi, ders döneminin baslangic tarihinden önce olmalidir
        if (request.getLastRegistrationDate().isAfter(request.getStartDate())) {
            throw new InvalidTimeException(Messages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
        }

        //!!! Bitis tarihi baslangic tarihinden büyük olmamali
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new InvalidTimeException(Messages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
        }

    }
}
