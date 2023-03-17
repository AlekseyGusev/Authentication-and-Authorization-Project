package com.gusev.project.controller;

import com.gusev.project.domain.dto.GroupClassDto;
import com.gusev.project.service.GroupClassService;
import com.gusev.project.service.TrainerService;
import com.gusev.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("group-classes")
public class GroupClassController {

    private final GroupClassService groupClassService;
    private final TrainerService trainerService;
    private final UserService userService;

    @Autowired
    public GroupClassController(GroupClassService groupClassService, TrainerService trainerService, UserService userService) {
        this.groupClassService = groupClassService;
        this.trainerService = trainerService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getGroupClasses(Principal principal) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("groupClass/groupClasses");
        mav.addObject("groupClasses", groupClassService.findGroupClasses());
        mav.addObject("user", userService.findUserByUsername(principal.getName()));
        return mav;
    }

    @GetMapping("/{class_id}/users")
    public ModelAndView getGroupClassUsers(@PathVariable("class_id") Long groupClassId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("groupClass/groupClassesUsers");
        mav.addObject("class", groupClassService.findGroupClassById(groupClassId));
        mav.addObject("users", userService.findAllUsersByGroupClassId(groupClassId));
        return mav;
    }

    @GetMapping("{class_id}/users/all-available")
    public ModelAndView getAllAvailableUsers(@PathVariable("class_id") Long classId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("groupClass/groupClass-add-user");
        mav.addObject("classId", classId);
        mav.addObject("users", groupClassService.findAllUsersNotInGroupClass(classId));
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView createGroupClass() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("groupClass/groupClass-create");
        mav.addObject("groupClassDto", new GroupClassDto());
        mav.addObject("trainers", trainerService.findAllTrainers());
        return mav;
    }

    @PostMapping("/create")
    public String createGroupClass(@ModelAttribute @Valid GroupClassDto groupClassDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("trainers", trainerService.findAllTrainers());
            return "groupClass/groupClass-create";
        }
        redirectAttributes.addFlashAttribute("message", "Group class has been added");
        groupClassService.saveGroupClass(groupClassDto);
        return "redirect:/group-classes";
    }

    @GetMapping("/{class_id}/users/{user_id}/registration")
    public String addUserToGroupClass(@PathVariable("class_id") Long classId,
                                   @PathVariable("user_id") Long userId,
                                   RedirectAttributes redirectAttributes) {
        groupClassService.addUserToGroupClass(userId, classId);
        redirectAttributes.addFlashAttribute("message", "User has been added");
        return "redirect:/group-classes" + "/" + classId + "/users";
    }

    @GetMapping("/{class_id}/user-registration")
    public String registerUser(@PathVariable("class_id") Long groupClassId,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        groupClassService.registerUser(groupClassId, principal.getName());
        redirectAttributes.addFlashAttribute("message", "Registered");
        return "redirect:/group-classes";
    }

    @GetMapping("/{class_id}/delete")
    public String deleteGroupClass(@PathVariable("class_id") Long groupClassId, RedirectAttributes redirectAttributes) {
        groupClassService.deleteGroupClassById(groupClassId);
        redirectAttributes.addFlashAttribute("message", "Group class has been delete");
        return "redirect:/group-classes";
    }

    @GetMapping("/{class_id}/users/{user_id}/cancel-registration")
    public ModelAndView deleteGroupClassesUser(@PathVariable("class_id") Long groupClassId,
                                               @PathVariable("user_id") Long userId,
                                               RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/group-classes");
        groupClassService.cancelUserRegistration(groupClassId, userId);
        redirectAttributes.addFlashAttribute("message", "Registration has been canceled");
        return mav;
    }
}
