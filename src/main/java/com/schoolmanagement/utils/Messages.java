package com.schoolmanagement.utils;

public class Messages {

    public static final String ALREADY_SEND_A_MESSAGE_TODAY = "Error : You already sent a message with this e-mail address today";
    public static final String ALREADY_REGISTER_MESSAGE_USERNAME = "Error : User with username %s already register";
    public static final String ALREADY_REGISTER_MESSAGE_SSN = "Error : User with ssn %s already register";
    public static final String ALREADY_REGISTER_MESSAGE_PHONE_NUMBER = "Error : User with phone number %s already register";

    public static final String NOT_PERMITTED_METHOD_MESSAGE = "You don't have any permission to change this value";

    public static final String NOT_FOUND_USER_MESSAGE = "Error : User not found";
    public static final String NOT_FOUND_USER2_MESSAGE = "Error : User not found with id %s";

    public static final String EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE = "Error : The start date cannot be less than the last registration date";
    public static final String EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE = "Error: The end date cannot be earlier than the start date" ;
    public static final String EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE = "Error: Education Term with Term and Year already exist" ;
    public static final String EDUCATION_TERM_NOT_FOUND_MESSAGE = "Error: Education Term with id %s not found" ;

    public static final String ALREADY_REGISTER_LESSON_MESSAGE = "Error: Lesson with lesson name %s already registered" ;
    public static final String NOT_FOUND_LESSON_MESSAGE = "Error: Lesson with this field %s not found" ;
    public static final String NOT_FOUND_LESSON_IN_LIST = "Error: Lesson not found in the list" ;

    public static final String TIME_NOT_VALID_MESSAGE = "Error: incorrect time" ;

}
