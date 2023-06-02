package com.schoolmanagement.utils;

import com.schoolmanagement.entity.abstracts.User;
import com.schoolmanagement.payload.request.abstracts.BaseUserRequest;

public class CheckParameterUpdateMethod {

    public static boolean checkParameter(User user, BaseUserRequest baseUserRequest){

        //Unique field sayimiz kadar karsilastirma yapilir.
        /** AMAC

            Burayi bu sekilde tasarlamamizin amaci, unique olan alanlarimizi duplicate metodumuz ile kontrol ediyor.
            eger unique alanlar degismiyorsa bos yere diger record'larda gereksiz eslestirme ve aramanin önüne geciyoruz

         */
        return user.getSsn().equalsIgnoreCase(baseUserRequest.getSsn())
                || user.getPhoneNumber().equalsIgnoreCase(baseUserRequest.getPhoneNumber())
                || user.getUsername().equalsIgnoreCase(baseUserRequest.getUsername());

    }
}
