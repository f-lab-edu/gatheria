package com.gatheria.service.impl;

import com.gatheria.domain.Assignment;
import com.gatheria.domain.Lecture;
import com.gatheria.domain.Notice;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.request.AssignmentCreateRequestDto;
import com.gatheria.dto.request.NoticeCreateRequestDto;
import com.gatheria.dto.response.AssignmentResponseDto;
import com.gatheria.dto.response.NoticeResponseDto;
import com.gatheria.dto.response.PostListResponseDto;
import com.gatheria.mapper.LectureMapper;
import com.gatheria.mapper.PostMapper;
import com.gatheria.service.PostService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostServiceImpl implements PostService {

  private final LectureMapper lectureMapper;
  private final PostMapper postMapper;

  public PostServiceImpl(LectureMapper lectureMapper, PostMapper postMapper) {
    this.lectureMapper = lectureMapper;
    this.postMapper = postMapper;
  }

  @Override
  @Transactional
  public void addNotice(Long lectureId, NoticeCreateRequestDto request, AuthInfo authInfo) {
    authInfo.validateInstructor();

    Lecture lecture = lectureMapper.findLectureById(lectureId);

    if (!lecture.isOwnedBy(authInfo.getInstructorId())) {
      throw new RuntimeException("수업 권한 x");
    }

    Notice notice = Notice.of(
        lectureId,
        authInfo.getInstructorId(),
        request.getTitle(),
        request.getContents()
    );

    postMapper.insertNotice(notice);
  }

  @Override
  @Transactional
  public void addAssignment(Long lectureId, AssignmentCreateRequestDto request, AuthInfo authInfo) {
    //테스트 완료
    authInfo.validateInstructor();

    Lecture lecture = lectureMapper.findLectureById(lectureId);

    if (!lecture.isOwnedBy(authInfo.getInstructorId())) {
      throw new RuntimeException("수업 권한 x");
    }

    Assignment assignment = Assignment.of(
        lectureId,
        authInfo.getInstructorId(),
        request.getTitle(),
        request.getContents(),
        request.getDueDate()
    );

    postMapper.insertAssignment(assignment);
    postMapper.insertAssignmentDetails(assignment);

  }

  @Override
  public NoticeResponseDto showNotice(Long lectureId, Long postId, AuthInfo authInfo) {
    //테스트 완료
    Notice notice = postMapper.findNoticeById(postId);

    if (!notice.isLecture(lectureId)) {
      throw new RuntimeException("해당 수업 포스터 x");
    }

    return NoticeResponseDto.from(notice);
  }

  @Override
  public AssignmentResponseDto showAssignment(Long lectureId, Long postId, AuthInfo authInfo) {
    Assignment assignment = postMapper.findAssignmentById(postId);

    if (!assignment.isLecture(lectureId)) {
      throw new RuntimeException("해당 수업 포스터 x");
    }

    return AssignmentResponseDto.from(assignment);
  }

  @Override
  public void updateNotice(Long lectureId, Long postId, NoticeCreateRequestDto request,
      AuthInfo authInfo) {

    authInfo.validateInstructor();

    Notice notice = postMapper.findNoticeById(postId);

    if (notice == null) {
      throw new RuntimeException("없는 포스터");
    }

    if (!notice.isLecture(lectureId)) {
      throw new RuntimeException("해당 수업 포스터 x");
    }

    if (!notice.isInstructor(authInfo.getInstructorId())) {
      throw new RuntimeException("본인이 쓴 글만 수정 가능");
    }

    Notice updateNotice = Notice.of(postId, lectureId, notice.getInstructorId(), request.getTitle(),
        request.getContents());

    postMapper.updateNotice(updateNotice);
  }

  @Override
  public void updateAssignment(Long lectureId, Long postId, AssignmentCreateRequestDto request,
      AuthInfo authInfo) {

    authInfo.validateInstructor();

    Assignment assignment = postMapper.findAssignmentById(postId);

    if (assignment == null) {
      throw new RuntimeException("없는 포스터");
    }

    if (!assignment.isLecture(lectureId)) {
      throw new RuntimeException("해당 수업 포스터 x");
    }

    if (!assignment.isInstructor(authInfo.getInstructorId())) {
      throw new RuntimeException("본인이 쓴 글만 수정 가능");
    }

    Assignment updateAssignment = Assignment.of(postId, lectureId, assignment.getInstructorId(),
        request.getTitle(), request.getContents(), request.getDueDate());

    postMapper.updateAssignment(updateAssignment);
    postMapper.updateAssignmentDetails(updateAssignment);

  }

  @Override
  public void deleteNotice(Long lectureId, Long postId, AuthInfo authInfo) {
    authInfo.validateInstructor();

    Notice notice = postMapper.findNoticeById(postId);

    if (notice == null) {
      throw new RuntimeException("없는 포스터");
    }

    if (!notice.isLecture(lectureId)) {
      throw new RuntimeException("해당 수업 포스터 x");
    }

    // 수정 신청한 교수가 쓴거야?
    if (!notice.isInstructor(authInfo.getInstructorId())) {
      throw new RuntimeException("본인이 쓴 글만 수정 가능");
    }

    postMapper.deletePost(postId);
  }

  @Override
  public void deleteAssignment(Long lectureId, Long postId, AuthInfo authInfo) {
    authInfo.validateInstructor();

    Assignment assignment = postMapper.findAssignmentById(postId);

    if (assignment == null) {
      throw new RuntimeException("없는 포스터");
    }

    if (!assignment.isLecture(lectureId)) {
      throw new RuntimeException("해당 수업 포스터 x");
    }

    if (!assignment.isInstructor(authInfo.getInstructorId())) {
      throw new RuntimeException("본인이 쓴 글만 수정 가능");
    }

    postMapper.deletePost(postId);

  }


  @Override
  public List<NoticeResponseDto> getNotices(Long lectureId, AuthInfo authInfo) {
    //테스트 완료
    return postMapper.findAllNoticesByLectureId(lectureId).stream()
        .map(NoticeResponseDto::from)
        .collect(Collectors.toList());
  }

  @Override
  public List<AssignmentResponseDto> getAssignments(Long lectureId, AuthInfo authInfo) {
    //테스트 완료
    return postMapper.findAllAssignmentsByLectureId(lectureId).stream()
        .map(AssignmentResponseDto::from)
        .collect(Collectors.toList());
  }

  @Override
  public List<PostListResponseDto> showAllPosts(Long lectureId, AuthInfo authInfo) {
    //TODO : 질문하기(동작 x)
    return postMapper.findAllPostsByLectureId(lectureId).stream()
        .map(post -> post.createListDto())
        .collect(Collectors.toList());
  }
}
