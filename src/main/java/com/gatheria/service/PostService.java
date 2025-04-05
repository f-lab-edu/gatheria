package com.gatheria.service;

import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.request.AssignmentCreateRequestDto;
import com.gatheria.dto.request.NoticeCreateRequestDto;
import com.gatheria.dto.response.AssignmentResponseDto;
import com.gatheria.dto.response.NoticeResponseDto;
import com.gatheria.dto.response.PostListResponseDto;
import java.util.List;

public interface PostService {

  void addNotice(Long lectureId, NoticeCreateRequestDto request, AuthInfo authInfo);

  void addAssignment(Long lectureId, AssignmentCreateRequestDto request, AuthInfo authInfo);

  NoticeResponseDto showNotice(Long lectureId, Long postId, AuthInfo authInfo);

  AssignmentResponseDto showAssignment(Long lectureId, Long postId, AuthInfo authInfo);

  void updateNotice(Long lectureId, Long postId, NoticeCreateRequestDto request, AuthInfo authInfo);

  void updateAssignment(Long lectureId, Long postId, AssignmentCreateRequestDto request,
      AuthInfo authInfo);

  void deleteNotice(Long lectureId, Long postId, AuthInfo authInfo);

  void deleteAssignment(Long lectureId, Long postId, AuthInfo authInfo);

  List<NoticeResponseDto> showNotices(Long lectureId, AuthInfo authInfo);

  List<AssignmentResponseDto> showAssignments(Long lectureId, AuthInfo authInfo);

  List<PostListResponseDto> showAllPosts(Long lectureId, AuthInfo authInfo);


}
