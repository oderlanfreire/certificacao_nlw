package com.oderlan.certification_nlw.modules.students.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oderlan.certification_nlw.modules.students.dto.VerifyCertificationDTO;
import com.oderlan.certification_nlw.modules.students.useCases.VerifyIfHasCertificationUseCase;
import com.oderlan.certification_nlw.modules.students.useCases.verifyIfHasCertificationUseCase;

@RestController
@RequestMapping("/students")
public class StudentController {
    
    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    @PostMapping("/verifyIfHasCertification")
    public String verfyIfHasCertification(@RequestBody VerifyCertificationDTO verifyCertificationDTO){


        var result = this.verifyIfHasCertificationUseCase.execute(verifyCertificationDTO);
        if (result){
            return "Usuário já fez a prova!";
        }
        return "Usuario pode fazer a prova";
    }
}
