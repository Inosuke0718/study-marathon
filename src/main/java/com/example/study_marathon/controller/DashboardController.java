package com.example.study_marathon.controller;

import com.example.study_marathon.dto.StudyLogForm;
import com.example.study_marathon.service.StudyLogService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DashboardController {

    private final StudyLogService studyLogService;

    public DashboardController(StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, @ModelAttribute("studyLogForm") StudyLogForm form) {
        int totalMinutes = studyLogService.getWeeklyTotalMinutesForCurrentUser();
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        model.addAttribute("weeklyTotalMinutes", totalMinutes);
        model.addAttribute("weeklyTotalHours", hours);
        model.addAttribute("weeklyTotalRemainMinutes", minutes);
        model.addAttribute("logs", studyLogService.getLatestLogs());

        if (form.getStudyDate() == null) {
            form.setStudyDate(java.time.LocalDate.now());
        }

        return "dashboard/index";
    }

    @PostMapping("/dashboard/logs")
    public String createLogFromDashboard(@Valid @ModelAttribute("studyLogForm") StudyLogForm form,
                                         BindingResult bindingResult,
                                         Model model) {
        if (bindingResult.hasErrors()) {
            // バリデーションエラー時もダッシュボードを再表示
            int totalMinutes = studyLogService.getWeeklyTotalMinutesForCurrentUser();
            int hours = totalMinutes / 60;
            int minutes = totalMinutes % 60;

            model.addAttribute("weeklyTotalMinutes", totalMinutes);
            model.addAttribute("weeklyTotalHours", hours);
            model.addAttribute("weeklyTotalRemainMinutes", minutes);
            model.addAttribute("logs", studyLogService.getLatestLogs());

            return "dashboard/index";
        }

        studyLogService.createLog(form);
        return "redirect:/dashboard";
    }
}
