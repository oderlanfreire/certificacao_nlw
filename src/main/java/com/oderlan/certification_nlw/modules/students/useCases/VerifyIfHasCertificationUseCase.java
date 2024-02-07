package com.oderlan.certification_nlw.modules.students.useCases;

import org.springframework.stereotype.Service;

import com.oderlan.certification_nlw.modules.students.dto.VerifyCertificationDTO;

@Service
public class VerifyIfHasCertificationUseCase {
    
    public boolean execute( VerifyCertificationDTO dto ){
        if(dto.getEmail().equals("oderlanfreire@gmail.com") && dto.getTechnology().equals("AWS")){
            return true;
        }
        else{
            return false;
        }
    }
}
