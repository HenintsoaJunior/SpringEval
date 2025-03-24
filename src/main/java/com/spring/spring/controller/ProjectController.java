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

import com.spring.spring.dto.project.ProjectResponseDTO;
import com.spring.spring.dto.project.ProjectDetailsDTO;
import com.spring.spring.model.ProjectModel;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/spring")
public class ProjectController {

    @Autowired
    private ProjectModel projectModel;

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @GetMapping("/projects")
    public String getProjects(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        try {
            ProjectResponseDTO response = projectModel.getAll(page, session);
            model.addAttribute("projects", response.getData().getProjects());
            model.addAttribute("pagination", response.getData().getPagination());
            model.addAttribute("status", response.getStatus());
            return "pages/project/projects";
        } catch (Exception e) {
            logger.error("Error in getProjects: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/project/projects";
        }
    }

    @GetMapping("/project/{externalId}")
    public String getProjectDetails(@PathVariable("externalId") String externalId, Model model, HttpSession session) {
        try {
            ProjectDetailsDTO project = projectModel.getDetails(externalId, session);
            model.addAttribute("project", project);
            model.addAttribute("status", "success");
            return "pages/project/show";
        } catch (Exception e) {
            logger.error("Error in getProjectDetails: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            if (e.getMessage().contains("non autorisé")) {
                return "redirect:/";
            }
            return "pages/project/show";
        }
    }
}