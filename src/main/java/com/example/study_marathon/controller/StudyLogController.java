package com.example.study_marathon.controller;

import com.example.study_marathon.dto.StudyLogForm;
import com.example.study_marathon.service.StudyLogService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/study-logs")
public class StudyLogController {

    private final StudyLogService studyLogService;

    public StudyLogController(StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    @GetMapping
    public String showLogs(Model model, @ModelAttribute("studyLogForm") StudyLogForm form) {
        model.addAttribute("logs", studyLogService.getLatestLogs());
        if (form.getStudyDate() == null) {
            form.setStudyDate(java.time.LocalDate.now());
        }
        return "study_logs/index";
    }

    @PostMapping
    public String createLog(@Valid @ModelAttribute("studyLogForm") StudyLogForm form,
                            BindingResult bindingResult,
                            Model model,
                            @RequestParam(value = "redirectTo", required = false) String redirectTo) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("logs", studyLogService.getLatestLogs());
            return "study_logs/index";
        }

        studyLogService.createLog(form);

        if (redirectTo != null && !redirectTo.isBlank()) {
            return "redirect:" + redirectTo;
        }

        return "redirect:/study-logs";
    }

    @PostMapping("/{id}/delete")
    public String deleteLog(@PathVariable("id") Long id) {
        studyLogService.deleteLog(id);
        return "redirect:/study-logs";
    }
}
