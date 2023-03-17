package com.gusev.project.util;

import static com.gusev.project.util.Utils.USERNAME;

public class UrlPath {

    public static final String ID = "/1";
    public static final String CREATE = "/create";
    public static final String USERS = "/users";
    public static final String DELETE = "/delete";
    public static final String EDIT = "/edit";
    public static final String UPDATE = "/update";
    public static final String INFO = "/info";
    public static final String ACCOUNT = "/account";
    public static final String GROUP_CLASSES = "/group-classes";
    public static final String TRAINERS = "/trainers";
    public static final String ALL_AVAILABLE = "/all-available";
    public static final String REGISTRATION = "/registration";
    public static final String USER_REGISTRATION = "/user-registration";
    public static final String CANCEL_REGISTRATION = "/cancel-registration";

    // GROUP CLASS
    public static final String GROUP_CLASSES_ID = GROUP_CLASSES + ID;
    public static final String GROUP_CLASS_USERS = GROUP_CLASSES_ID + USERS;
    public static final String GROUP_CLASS_USERS_ALL_AVAILABLE = GROUP_CLASS_USERS + ALL_AVAILABLE;
    public static final String GROUP_CLASS_CREATE = GROUP_CLASSES + CREATE;
    public static final String GROUP_CLASS_USER_REGISTRATION = GROUP_CLASS_USERS + ID + REGISTRATION;
    public static final String GROUP_CLASS_USERS_USER_REGISTRATION = GROUP_CLASSES_ID + USER_REGISTRATION;
    public static final String GROUP_CLASS_USERS_CANCEL_REGISTRATION = GROUP_CLASS_USERS + ID + CANCEL_REGISTRATION;
    public static final String GROUP_CLASS_DELETE = GROUP_CLASSES_ID + DELETE;

    // TRAINER
    public static final String TRAINERS_ID = TRAINERS + ID;
    public static final String TRAINER_USERS = TRAINERS_ID + USERS;
    public static final String TRAINER_GROUP_CLASSES = TRAINERS_ID + GROUP_CLASSES;
    public static final String TRAINER_ADD_USER_ALL_AVAILABLE = TRAINER_USERS + ALL_AVAILABLE;
    public static final String TRAINER_CREATE = TRAINERS + CREATE;
    public static final String TRAINER_ADD_USER = TRAINER_USERS + ID + REGISTRATION;
    public static final String TRAINER_DELETE = TRAINERS_ID + DELETE;
    public static final String TRAINER_DELETE_USER = TRAINER_USERS + ID + DELETE;

    // USER
    public static final String USERS_ID = USERS + ID;
    public static final String USER_INFO = USERS_ID + INFO;
    public static final String USER_ACCOUNT_INFO = USERS + ACCOUNT + "/" + USERNAME;
    public static final String USER_CREATE = USERS + CREATE;
    public static final String USER_EDIT = USERS_ID + EDIT;
    public static final String USER_UPDATE = USERS + "/2" + UPDATE;
    public static final String USER_DELETE = USERS_ID + DELETE;
    public static final String USER_CANCEL_REGISTRATION = USERS_ID + GROUP_CLASSES_ID + CANCEL_REGISTRATION;
}
