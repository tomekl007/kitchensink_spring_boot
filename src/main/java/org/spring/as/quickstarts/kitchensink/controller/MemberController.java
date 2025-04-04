/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spring.as.quickstarts.kitchensink.controller;

import org.spring.as.quickstarts.kitchensink.model.Member;
import org.spring.as.quickstarts.kitchensink.service.MemberRegistration;
import org.spring.as.quickstarts.kitchensink.data.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
public class MemberController {

    private final MemberRegistration memberRegistration;
    private final MemberRepository memberRepository;

    @Autowired
    public MemberController(MemberRegistration memberRegistration, MemberRepository memberRepository) {
        this.memberRegistration = memberRegistration;
        this.memberRepository = memberRepository;
    }

    @ModelAttribute("newMember")
    public Member newMember() {
        return new Member();
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Member> members = memberRepository.findAllOrderedByName();
        model.addAttribute("members", members);
        return "index";
    }

    @GetMapping("/index.jsf")
    public String indexJsf(Model model) {
        List<Member> members = memberRepository.findAllOrderedByName();
        model.addAttribute("members", members);
        return "index";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newMember") Member member, 
                          BindingResult bindingResult, 
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "index";
        }
        
        try {
            memberRegistration.register(member);
            redirectAttributes.addFlashAttribute("message", "Registration successful!");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/";
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            redirectAttributes.addFlashAttribute("message", errorMessage);
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/";
        }
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }
}
