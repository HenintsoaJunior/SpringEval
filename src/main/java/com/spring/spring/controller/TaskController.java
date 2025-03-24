package com.spring.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.spring.dto.tasks.TaskResponseDTO;
import com.spring.spring.dto.tasks.TaskDetailsDTO;
import com.spring.spring.model.TaskModel;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/spring")
public class TaskController {

    @Autowired
    private TaskModel taskModel;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping("/tasks")
    public String getTasks(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        try {
            TaskResponseDTO response = taskModel.getAll(page, session);
            model.addAttribute("tasks", response.getData().getTasks());
            model.addAttribute("pagination", response.getData().getPagination());
            model.addAttribute("status", response.getStatus());
            return "pages/task/tasks";
        } catch (Exception e) {
            logger.error("Error in getTasks: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/task/tasks";
        }
    }

    @GetMapping("/task/{externalId}")
    public String getTaskDetails(@PathVariable("externalId") String externalId, Model model, HttpSession session) {
        try {
            TaskDetailsDTO task = taskModel.getDetails(externalId, session);
            model.addAttribute("task", task);
            model.addAttribute("status", "success");
            return "pages/task/show";
        } catch (Exception e) {
            logger.error("Error in getTaskDetails: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/task/show";
        }
    }
}