package com.gusev.project.controller;

import com.gusev.project.domain.dto.TrainerDto;
import com.gusev.project.exception.UsernameExistsException;
import com.gusev.project.service.GroupClassService;
import com.gusev.project.service.TrainerService;
import com.gusev.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final UserService userService;
    private final GroupClassService groupClassService;

    @Autowired
    public TrainerController(TrainerService trainerService, UserService userService, GroupClassService groupClassService) {
        this.trainerService = trainerService;
        this.userService = userService;
        this.groupClassService = groupClassService;
    }

    @GetMapping
    public ModelAndView getTrainers() {
        return new ModelAndView("trainer/trainers", "trainers", trainerService.findAllTrainers());
    }

    @GetMapping("/{trainer_id}/users")
    public ModelAndView getTrainersUsers(@PathVariable("trainer_id") Long trainerId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("trainer/trainersUsers");
        mav.addObject("trainer", trainerService.findTrainerDtoById(trainerId));
        mav.addObject("users", userService.findUsersByTrainerId(trainerId));
        return mav;
    }

    @GetMapping("/{trainer_id}/group-classes")
    public ModelAndView getTrainersGroupClasses(@PathVariable("trainer_id") Long trainerId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("trainer/trainersGroupClasses");
        mav.addObject("trainer", trainerService.findTrainerDtoById(trainerId));
        mav.addObject("groupClasses", groupClassService.findAllTrainersGroupClasses(trainerId));
        return mav;
    }

    @GetMapping("/{trainer_id}/users/all-available")
    public ModelAndView getAllUsers(@PathVariable("trainer_id") Long trainerId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("trainer/trainer-add-user");
        mav.addObject("trainerId", trainerId);
        mav.addObject("users", userService.findAllUsersWithoutTrainer());
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView createTrainer() {
        return new ModelAndView("trainer/trainer-create", "trainerDto", new TrainerDto());
    }

    @PostMapping("/create")
    public String createTrainer(@ModelAttribute @Valid TrainerDto dto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) throws UsernameExistsException {
        if (bindingResult.hasErrors()) {
            return "trainer/trainer-create";
        }
        if (trainerService.saveTrainer(dto)) {
            redirectAttributes.addFlashAttribute("message", "Trainer has been added");
        } else {
            redirectAttributes.addFlashAttribute("error", "There is an account with username: " + dto.getUsername());
        }
        return "redirect:/trainers";
    }

    @GetMapping("/{trainer_id}/users/{user_id}/registration")
    public String addUserToTrainer(@PathVariable("trainer_id") Long trainerId,
                                   @PathVariable("user_id") Long userId,
                                   RedirectAttributes redirectAttributes) {
        userService.addTrainerToUser(userId, trainerId);
        redirectAttributes.addFlashAttribute("message", "User has been added");
        return "redirect:/trainers/" + trainerId + "/users";
    }

    @GetMapping("/{trainer_id}/delete")
    public String deleteTrainer(@PathVariable("trainer_id") Long trainerId, RedirectAttributes redirectAttributes) {
        trainerService.deleteTrainerById(trainerId);
        redirectAttributes.addFlashAttribute("message", "Trainer has been deleted");
        return "redirect:/trainers";
    }

    @GetMapping("/{trainer_id}/users/{user_id}/delete")
    public String deleteTrainersUser(@PathVariable("trainer_id") Long trainerId,
                                     @PathVariable("user_id") Long userId,
                                     RedirectAttributes redirectAttributes) {
        userService.deleteUsersTrainer(userId, trainerId);
        redirectAttributes.addFlashAttribute("message", "User has been deleted");
        return "redirect:/trainers/" + trainerId + "/users";
    }
}
