package com.example.survey.controller;

import com.example.survey.model.Survey;
import com.example.survey.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class SurveyController {

    @Autowired
    private SurveyRepository surveyRepository;

    /**
     * Display the survey form for new entry.
     */
    @GetMapping("/")
    public String showSurveyForm(Model model) {
        model.addAttribute("survey", new Survey());
        return "survey"; // should load survey.html from templates
    }

    /**
     * Handle form submission for a new survey.
     */
    @PostMapping("/submit")
    public String submitSurvey(@ModelAttribute Survey survey) {
        surveyRepository.save(survey);
        return "redirect:/list";
    }

    /**
     * View all submitted surveys.
     */
    @GetMapping("/list")
    public String viewSurveys(Model model) {
        List<Survey> surveys = surveyRepository.findAll();
        model.addAttribute("surveys", surveys);
        return "survey-list"; // survey-list.html
    }

    /**
     * Display the survey form for editing an existing survey.
     */
    @GetMapping("/edit/{id}")
    public String editSurvey(@PathVariable("id") Long id, Model model) {
        Optional<Survey> survey = surveyRepository.findById(id);
        if (survey.isPresent()) {
            model.addAttribute("survey", survey.get());
            return "survey"; // Reuse the form
        } else {
            return "redirect:/list";
        }
    }

    /**
     * Handle update of an existing survey.
     */
    @PostMapping("/update/{id}")
    public String updateSurvey(@PathVariable("id") Long id, @ModelAttribute Survey updatedSurvey) {
        Optional<Survey> existingSurvey = surveyRepository.findById(id);
        if (existingSurvey.isPresent()) {
            updatedSurvey.setId(id);
            surveyRepository.save(updatedSurvey);
        }
        return "redirect:/list";
    }

    /**
     * Delete a survey by ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteSurvey(@PathVariable("id") Long id) {
        surveyRepository.deleteById(id);
        return "redirect:/list";
    }
}