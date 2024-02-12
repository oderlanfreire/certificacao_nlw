package com.oderlan.certification_nlw.modules.students.controllers;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oderlan.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.oderlan.certification_nlw.modules.students.dto.VerifyCertificationDTO;
import com.oderlan.certification_nlw.modules.students.useCases.StudentCertificationAnswerUseCase;
import com.oderlan.certification_nlw.modules.students.useCases.VerifyIfHasCertificationUseCase;


@RestController
@RequestMapping("/students")
public class StudentController {
    
    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    @Autowired
    private StudentCertificationAnswerUseCase studentCertificationAnswerUseCase;

    @PostMapping("/verifyIfHasCertification")
    public String verfyIfHasCertification(@RequestBody VerifyCertificationDTO verifyCertificationDTO){


        var result = this.verifyIfHasCertificationUseCase.execute(verifyCertificationDTO);
        if (result){
            return "Usuário já fez a prova!";
        }
        return "Usuario pode fazer a prova";
    }

    @PostMapping("/certification/answer")
    public ResponseEntity<Object> certificationAnswer(
            @RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO) {
        try {
            var result = this.studentCertificationAnswerUseCase.execute(studentCertificationAnswerDTO);
            return ResponseEntity.ok().body(result);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
