package com.gusev.project.util;

import com.gusev.project.domain.GroupClass;
import com.gusev.project.domain.Trainer;
import com.gusev.project.domain.User;
import com.gusev.project.domain.dto.GroupClassDto;
import com.gusev.project.domain.dto.TrainerDto;
import com.gusev.project.domain.dto.UserDto;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

public class Utils {

    public static final long ID = 1L;
    public static final String USERNAME = "USERNAME";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String DIFFERENT_USERNAME = "DIFFERENT_USERNAME";
    public static final String DIFFERENT_FIRST_NAME = "DIFFERENT_FIRST_NAME";
    public static final String DIFFERENT_LASTNAME = "DIFFERENT_LASTNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String ENCODED_PASSWORD = "ENCODED_PASSWORD";

    public static GroupClass groupClassMock() {
        return mock(GroupClass.class);
    }

    public static GroupClassDto groupClassDtoMock() {
        return mock(GroupClassDto.class);
    }

    public static Trainer trainerMock() {
        return mock(Trainer.class);
    }

    public static User userMock() {
        return mock(User.class);
    }

    public static User userTest() {
        User user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setPassword(ENCODED_PASSWORD);
        return user;
    }

    public static Trainer trainer() {
        Trainer trainer = new Trainer();
        trainer.setId(ID);
        trainer.setUsername(USERNAME);
        trainer.setFirstName(FIRST_NAME);
        trainer.setLastName(LAST_NAME);
        trainer.setPassword(ENCODED_PASSWORD);
        return trainer;
    }

    public static UserDto userDtoMock() {
        return mock(UserDto.class);
    }

    public static UserDto userDto() {
        UserDto dto = new UserDto();
        dto.setUsername(USERNAME);
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME);
        dto.setPassword(PASSWORD);
        return dto;
    }

    public static TrainerDto trainerDto() {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUsername(USERNAME);
        trainerDto.setFirstName(FIRST_NAME);
        trainerDto.setLastName(LAST_NAME);
        trainerDto.setPassword(PASSWORD);
        return trainerDto;
    }

    public static GroupClass groupClass() {
        return new GroupClass();
    }

    public static GroupClassDto groupClassDto() {
        return new GroupClassDto();
    }

    public static GroupClassDto groupClassDtoWithTrainer() {
        GroupClassDto dto = groupClassDto();
        dto.setId(ID);
        TrainerDto trainerDto = trainerDto();
        trainerDto.setId(ID);
        dto.setTrainer(trainerDto);
        return dto;
    }

    public static Set<GroupClass> groupClassSet() {
        return Set.of(groupClassMock(), groupClassMock(), groupClassMock());
    }

    public static Set<GroupClassDto> groupClassDtoSet() {
        GroupClassDto gc1 = groupClassDto();
        GroupClassDto gc2 = groupClassDto();
        GroupClassDto gc3 = groupClassDto();
        gc1.setId(ID);
        gc2.setId(ID + ID);
        gc3.setId(ID + ID + ID);
        LocalDateTime now = LocalDateTime.now();
        gc1.setDate(String.valueOf(now));
        gc2.setDate(String.valueOf(now.plusMinutes(ID)));
        gc3.setDate(String.valueOf(now.plusMinutes(ID + ID)));
        Set<GroupClassDto> dtoSet = new LinkedHashSet<>();
        dtoSet.add(gc1);
        dtoSet.add(gc2);
        dtoSet.add(gc3);
        return dtoSet;
    }

    public static Set<TrainerDto> trainerDtoSet() {
        TrainerDto td1 = trainerDto();
        TrainerDto td2 = trainerDto();
        td1.setId(ID);
        td2.setId(ID + ID);
        Set<TrainerDto> dtoSet = new LinkedHashSet<>();
        dtoSet.add(td1);
        dtoSet.add(td2);
        return dtoSet;
    }

    public static Set<GroupClassDto> groupClassDtoSetWithSameTrainer(){
        Set<GroupClassDto> set = groupClassDtoSet();
        TrainerDto trainer = trainerDto();
        trainer.setId(ID);
        set.forEach(g -> g.setTrainer(trainer));
        return set;
    }

    public static Set<User> userSet() {
        User u1 = userTest();
        User u2 = userTest();
        u1.setId(ID);
        u2.setId(ID + ID);
        return Set.of(u1, u2);
    }

    public static Set<UserDto> userDtoSet() {
        UserDto u1 = userDto();
        UserDto u2 = userDto();
        u1.setId(ID);
        u2.setId(ID + ID);
        return Set.of(u1, u2);
    }

    public static Set<User> userSetWithSameTrainer() {
        Set<User> userSet = userSet();
        Trainer trainer = trainer();
        userSet.forEach(u -> u.setTrainer(trainer));
        return userSet;
    }

    public static Set<UserDto> userDtoSetWithSameTrainer() {
        Set<UserDto> userDtoSet = userDtoSet();
        TrainerDto trainerDto = trainerDto();
        trainerDto.setId(ID);
        userDtoSet.forEach(u -> u.setTrainer(trainerDto));
        return userDtoSet;
    }

    public static Set<User> userSetWithSameGroupClass() {
        Set<User> userSet = userSet();
        GroupClass groupClass = groupClass();
        userSet.forEach(u -> u.setGroupClasses(Set.of(groupClass)));
        return userSet;
    }

    public static Set<UserDto> userDtoSetWithSameGroupClass() {
        Set<UserDto> userDtoSet = userDtoSet();
        GroupClassDto groupClassDto = groupClassDto();
        groupClassDto.setId(ID);
        userDtoSet.forEach(u -> u.setGroupClasses(Set.of(groupClassDto)));
        return userDtoSet;
    }

    public static UserDto userDtoWithUsernameAndPassword(){
        UserDto dto = userDto();
        dto.setUsername(USERNAME);
        dto.setPassword(PASSWORD);
        return dto;
    }

    public static User changedUser(){
        User user = userTest();
        user.setUsername(DIFFERENT_USERNAME);
        user.setFirstName(DIFFERENT_FIRST_NAME);
        user.setLastName(DIFFERENT_LASTNAME);
        return user;
    }

    public static UserDto changedUserDto(){
        UserDto dto = userDto();
        dto.setUsername(DIFFERENT_USERNAME);
        dto.setFirstName(DIFFERENT_FIRST_NAME);
        dto.setLastName(DIFFERENT_LASTNAME);
        return dto;
    }

    public static User userWithUsernameAndPassword(){
        User user = userTest();
        user.setUsername(USERNAME);
        user.setPassword(ENCODED_PASSWORD);
        return user;
    }

    public static User userWithTrainer() {
        User user = userTest();
        user.setTrainer(trainer());
        return user;
    }

    public static User userWithTrainerAndGroupClass(){
        User user = userTest();
        user.setTrainer(trainer());
        user.setGroupClasses(Set.of(groupClass()));
        return user;
    }

    public static Set<Trainer> trainerSet() {
        Trainer t1 = new Trainer();
        Trainer t2 = new Trainer();
        t1.setId(ID);
        t2.setId(ID + ID);
        return Set.of(t1, t2);
    }

    public static LocalDateTime toLocalDateTime(String date) {
        return LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse(date));
    }

    public static RequestPostProcessor ADMIN() {
        return user("admin").roles("ADMIN");
    }
    public static RequestPostProcessor TRAINER() {
        return user("trainer").roles("TRAINER");
    }
    public static RequestPostProcessor USER() {
        return user("user").roles("USER");
    }
}
