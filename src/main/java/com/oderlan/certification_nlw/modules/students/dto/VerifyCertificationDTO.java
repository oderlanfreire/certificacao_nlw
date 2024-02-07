package com.oderlan.certification_nlw.modules.students.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCertificationDTO {
    
    private String email;
    private String technology;
}
