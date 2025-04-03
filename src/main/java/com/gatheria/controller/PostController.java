package com.gatheria.controller;

import com.gatheria.common.annotation.Auth;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.request.AssignmentCreateRequestDto;
import com.gatheria.dto.request.NoticeCreateRequestDto;
import com.gatheria.dto.response.AssignmentResponseDto;
import com.gatheria.dto.response.NoticeResponseDto;
import com.gatheria.dto.response.PostListResponseDto;
import com.gatheria.service.PostService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lectures/{lectureId}/posts")
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping()
  public ResponseEntity<List<PostListResponseDto>> getAllPost(
      @PathVariable Long lectureId,
      @Auth AuthInfo authInfo
  ) {
    List<PostListResponseDto> response = postService.showAllPosts(lectureId, authInfo);
    return ResponseEntity.ok(response);
  }


  @GetMapping("/notices")
  public ResponseEntity<List<NoticeResponseDto>> getAllNotices(
      @PathVariable Long lectureId,
      @Auth AuthInfo authInfo
  ) {
    List<NoticeResponseDto> response = postService.getNotices(lectureId, authInfo);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/assignments")
  public ResponseEntity<List<AssignmentResponseDto>> getAllAssignments(
      @PathVariable Long lectureId,
      @Auth AuthInfo authInfo
  ) {
    List<AssignmentResponseDto> response = postService.getAssignments(lectureId, authInfo);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/notice")
  public ResponseEntity<Void> createNotice(
      @PathVariable Long lectureId,
      @RequestBody NoticeCreateRequestDto request,
      @Auth AuthInfo authInfo) {
    postService.addNotice(lectureId, request, authInfo);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/notice/{postId}")
  public ResponseEntity<NoticeResponseDto> showNotice(
      @PathVariable Long lectureId,
      @PathVariable Long postId,
      @Auth AuthInfo authInfo
  ) {
    NoticeResponseDto response = postService.showNotice(lectureId, postId, authInfo);
    return ResponseEntity.ok(response);
  }


  @PutMapping("/notice/{postId}")
  public ResponseEntity<Void> updateNotice(
      @PathVariable Long lectureId,
      @PathVariable Long postId,
      @RequestBody NoticeCreateRequestDto request,
      @Auth AuthInfo authInfo
  ) {
    postService.updateNotice(lectureId, postId, request, authInfo);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/notice/{postId}")
  public ResponseEntity<Void> deleteNotice(
      @PathVariable Long lectureId,
      @PathVariable Long postId,
      @Auth AuthInfo authInfo
  ) {
    postService.deleteNotice(lectureId, postId, authInfo);
    return ResponseEntity.ok().build();
  }


  @PostMapping("/assignment")
  public ResponseEntity<Void> createAssignment(
      @PathVariable Long lectureId,
      @RequestBody AssignmentCreateRequestDto request,
      @Auth AuthInfo authInfo) {
    postService.addAssignment(lectureId, request, authInfo);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/assignment/{postId}")
  public ResponseEntity<AssignmentResponseDto> showAssignment(
      @PathVariable Long lectureId,
      @PathVariable Long postId,
      @Auth AuthInfo authInfo
  ) {
    AssignmentResponseDto response = postService.showAssignment(lectureId, postId, authInfo);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/assignment/{postId}")
  public ResponseEntity<Void> updateAssignment(
      @PathVariable Long lectureId,
      @PathVariable Long postId,
      @RequestBody AssignmentCreateRequestDto request,
      @Auth AuthInfo authInfo
  ) {
    postService.updateAssignment(lectureId, postId, request, authInfo);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/assignment/{postId}")
  public ResponseEntity<Void> deleteAssignment(
      @PathVariable Long lectureId,
      @PathVariable Long postId,
      @Auth AuthInfo authInfo
  ) {
    postService.deleteAssignment(lectureId, postId, authInfo);
    return ResponseEntity.ok().build();
  }

}
