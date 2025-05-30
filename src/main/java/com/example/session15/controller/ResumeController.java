package com.example.session15.controller;

import com.example.session15.model.Resume;
import com.example.session15.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public String list(Model model) throws Exception {
        model.addAttribute("resumes", resumeService.findAll());
        return "resume/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("resume", new Resume());
        return "resume/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute Resume resume, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return "resume/form";
        }
        resumeService.save(resume);
        return "redirect:/resumes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) throws Exception {
        Resume resume = resumeService.findById(id);
        if (resume == null) {
            return "redirect:/resumes";
        }
        model.addAttribute("resume", resume);
        return "resume/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, @Valid @ModelAttribute Resume resume, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return "resume/form";
        }
        resume.setId(id);
        resumeService.update(resume);
        return "redirect:/resumes";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) throws Exception {
        resumeService.delete(id);
        return "redirect:/resumes";
    }
}