package com.oderlan.certification_nlw.modules.students.useCases;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oderlan.certification_nlw.modules.questions.entities.QuestionEntity;
import com.oderlan.certification_nlw.modules.questions.repositories.QuestionRepository;
import com.oderlan.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.oderlan.certification_nlw.modules.students.dto.VerifyCertificationDTO;
import com.oderlan.certification_nlw.modules.students.entities.AnswersCertificationEntity;
import com.oderlan.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.oderlan.certification_nlw.modules.students.entities.StudentEntity;
import com.oderlan.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import com.oderlan.certification_nlw.modules.students.repositories.StudentRepository;


@Service
public class StudentCertificationAnswerUseCase {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception{

        var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyCertificationDTO(dto.getEmail(), dto.getTechnology()));

        if (hasCertification){
            throw new Exception("Você ja possui essa certificação!");
        }


        List<QuestionEntity>questionEntities = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertificationEntity> answersCertifications = new ArrayList<>();

        AtomicInteger correctAnswersCount = new AtomicInteger(0);

        dto.getQuestionAnswer().stream().forEach(questionAnswer -> {
            var quest = questionEntities.stream().filter(question -> question.getId().equals(questionAnswer.getQuestionID())).findFirst().get();
        
            var findCorrectAlternative = quest.getAlternatives().stream().filter(alternative -> alternative.isCorrect()).findFirst().get();
        
        
            if(findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())){
                questionAnswer.setCorrect(true);
                correctAnswersCount.incrementAndGet();
            }else{
                questionAnswer.setCorrect(false);
            }

            var answersCertificationEntity = AnswersCertificationEntity.builder()
            .answerID(questionAnswer.getAlternativeID())
            .questionID(questionAnswer.getQuestionID())
            .isCorrect(questionAnswer.isCorrect()).build();

            answersCertifications.add(answersCertificationEntity);
        });

        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID;
        if (student.isEmpty()){
            var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        }else{
            studentID = student.get().getId();
        }

        CertificationStudentEntity certificationStudentEntity = 
            CertificationStudentEntity.builder()
            .technology(dto.getTechnology())
            .studentID(studentID)
            .grade(correctAnswersCount.get())
            .build();

        var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);
        
        answersCertifications.stream().forEach(answersCertification ->{
            answersCertification.setCertificationID(certificationStudentEntity.getId());
            answersCertification.setCertificationStudentEntity(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswersCertificationEntity(answersCertifications);

        certificationStudentRepository.save(certificationStudentEntity);

        return certificationStudentCreated;
    }
}
