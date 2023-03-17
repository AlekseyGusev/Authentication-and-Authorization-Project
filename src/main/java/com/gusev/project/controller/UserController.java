package com.gusev.project.controller;

import com.gusev.project.domain.dto.UserDto;
import com.gusev.project.exception.UsernameExistsException;
import com.gusev.project.service.GroupClassService;
import com.gusev.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final GroupClassService groupClassService;

    @Autowired
    public UserController(UserService userService, GroupClassService groupClassService) {
        this.userService = userService;
        this.groupClassService = groupClassService;
    }

    @GetMapping
    public ModelAndView getAllUsers() {
        return new ModelAndView("user/users", "users", userService.findAllUsers());
    }

    @GetMapping("/{user_id}/info")
    public ModelAndView getUserInfo(@PathVariable("user_id") Long userId) {
        return new ModelAndView("user/user", "user", userService.findUserById(userId));
    }

    @GetMapping("/account/{username}")
    public ModelAndView showUserAccount(@PathVariable("username") String username) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/user");
        mav.addObject("user", userService.findUserByUsername(username));
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView createUser() {
        return new ModelAndView("user/user-create", "userDto", new UserDto());
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute @Valid UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws UsernameExistsException {
        if (bindingResult.hasErrors()) {
            return "user/user-create";
        }
        userService.saveUser(userDto);
        redirectAttributes.addFlashAttribute("message", "User has been added");
        return "redirect:/users";
    }

    @GetMapping("/{user_id}/edit")
    public ModelAndView editUser(@PathVariable("user_id") Long userId) {
        return new ModelAndView("user/user-edit", "user", userService.findUserById(userId));
    }

    @PostMapping("/{user_id}/update")
    public String updateUser(@PathVariable("user_id") Long userId,
                             @ModelAttribute @Valid UserDto user,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            user = new UserDto();
            user.setId(userId);
            model.addAttribute("user", user);
            return "user/user-edit";
        }
        try {
            userService.updateUser(userId, user);
        } catch (UsernameExistsException e) {
            user = new UserDto();
            user.setId(userId);
            model.addAttribute("user", user);
            model.addAttribute("error", e.getMessage());
            return "user/user-edit";
        }
        redirectAttributes.addFlashAttribute("message", "User has been updated");
        return "redirect:/users";
    }

    @GetMapping("/{user_id}/delete")
    public String deleteUser(@PathVariable("user_id") Long userId, RedirectAttributes redirectAttributes) {
        userService.deleteUserById(userId);
        redirectAttributes.addFlashAttribute("message", "User has been deleted");
        return "redirect:/users";
    }

    @GetMapping("/{user_id}/group-classes/{group-class_id}/cancel-registration")
    public String cancelGroupClassRegistration(@PathVariable("user_id") Long userId,
                                               @PathVariable("group-class_id") Long groupClassId,
                                               RedirectAttributes redirectAttributes) {
        groupClassService.cancelUserRegistration(groupClassId, userId);
        redirectAttributes.addFlashAttribute("message", "Registration has been canceled");
        return "redirect:/users/" + userId + "/info";
    }
}