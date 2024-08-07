package com.example.EtiyaSportsClub.services;

import com.example.EtiyaSportsClub.dtos.ProgressGetDto;
import com.example.EtiyaSportsClub.dtos.requests.InitialProgressDto;
import com.example.EtiyaSportsClub.dtos.responses.ProgressForCalendar;
import com.example.EtiyaSportsClub.entities.ProgressEntity;
import com.example.EtiyaSportsClub.entities.UserEntity;
import com.example.EtiyaSportsClub.mappers.IProgressGetMapper;
import com.example.EtiyaSportsClub.repos.IProgressRepository;
import com.example.EtiyaSportsClub.repos.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {

    @Autowired
    IProgressRepository progressRepository;
    @Autowired
    IUserRepository userRepository;




    public ProgressService(IProgressRepository progressRepository, IUserRepository userRepository){
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
    }



    public List<ProgressGetDto> getAllProgressDto() {
        List<ProgressEntity> progresses = progressRepository.findAll();
        return IProgressGetMapper.INSTANCE.progressesToGetAllProgressesDto(progresses);
    }


    public ProgressGetDto getOneProgressDto(Long progressId) {
        Optional<ProgressEntity> optionalProgress = progressRepository.findById(progressId);
        if(optionalProgress.isPresent()){
            return IProgressGetMapper.INSTANCE.progressToGetProgressDto(optionalProgress.get());
        }else{
            throw new RuntimeException("Progress not found");
        }
    }

    public ProgressEntity createProgress(ProgressEntity newProgress) {
        return progressRepository.save(newProgress);
    }



    public ProgressEntity updateProgress(Long progressId, ProgressEntity newProgress) {
        Optional<ProgressEntity> progress = progressRepository.findById(progressId);
        if(progress.isPresent()){
            ProgressEntity foundedProgress = progress.get();
            foundedProgress.setRemainingCourseNumber(foundedProgress.getRemainingCourseNumber());
            progressRepository.save(foundedProgress);
            return foundedProgress;
        }
        return null;
    }


    public void deleteProgress(Long progressId) {
        progressRepository.deleteById(progressId);
    }

    public ProgressForCalendar getProgressByUsername(String username) {
        Optional<UserEntity> foundedUser = userRepository.findByUserName(username);
        if (foundedUser.isPresent()){
            ProgressEntity foundedProgress = progressRepository.findByUser_UserId(foundedUser.get().getUserId())
                    .orElseThrow(() -> new RuntimeException("Progress not found"));
            return IProgressGetMapper.INSTANCE.progressToProgressForCalendar(foundedProgress);
        }else{
            throw new RuntimeException("User Not Found");
        }

    }
}