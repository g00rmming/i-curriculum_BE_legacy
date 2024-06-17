package goorming.iCurriculum.take.service;

import goorming.iCurriculum.take.entity.Take;
import goorming.iCurriculum.take.entity.dto.TakeRequestDTO;
import goorming.iCurriculum.take.entity.dto.TakeResponseDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface TakeService {
    @Transactional
    TakeResponseDTO.TakenCourseListDTO findTakeList(Long memberId);


    @Transactional
    List<Take> takeCourse(Long memberId, TakeRequestDTO.CreateTakeListDTO createTakeListDTO);

    @Transactional
    TakeResponseDTO.TakenCourseDTO updateTake(Long takeId, TakeRequestDTO.UpdateTakenCourseDTO updateTakenCourseDTO);

    @Transactional
    TakeResponseDTO.UntakenCourseListDTO findUntakenList(Long memberId);

    @Transactional
    void deleteTake(Long takeId);

    @Transactional
    TakeResponseDTO.DashboardDTO findMemberStat(Long memberId);

    @Transactional
    TakeResponseDTO.UntakenCourseListDTO searchUntakenCourses(Long memberId, TakeRequestDTO.SearchOptionDTO searchUntakenCourseDTO);

}
