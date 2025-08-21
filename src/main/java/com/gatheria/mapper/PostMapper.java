package com.gatheria.mapper;

import com.gatheria.domain.Assignment;
import com.gatheria.domain.Notice;
import com.gatheria.domain.Post;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostMapper {

  @Insert("INSERT INTO posts (lecture_id, instructor_id, type, title, content) VALUES (#{lectureId}, #{instructorId}, 'NOTICE', #{title}, #{contents})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertNotice(Notice notice);

  @Insert("INSERT INTO posts (lecture_id, instructor_id, type, title, content) " +
      "VALUES (#{lectureId}, #{instructorId}, 'ASSIGNMENT', #{title}, #{contents})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertAssignment(Assignment assignment);

  @Insert("INSERT INTO assignment_details (post_id, due_date) " +
      "VALUES (#{id}, #{dueDate})")
  void insertAssignmentDetails(Assignment assignment);

  @Select("SELECT * FROM posts WHERE id = #{postId} AND type = 'NOTICE'")
  Notice findNoticeById(Long postId);

  Assignment findAssignmentById(Long postId);

  @Update(
      "UPDATE posts SET title = #{title}, content = #{contents}, updated_at = CURRENT_TIMESTAMP " +
          "WHERE id = #{id} AND type = 'NOTICE'")
  void updateNotice(Notice updateNotice);

  @Update(
      "UPDATE posts SET title = #{title}, content = #{contents}, updated_at = CURRENT_TIMESTAMP " +
          "WHERE id = #{id} AND type = 'ASSIGNMENT'")
  void updateAssignment(Assignment updateAssignment);

  @Update("UPDATE assignment_details SET due_date = #{dueDate} " +
      "WHERE post_id = #{id}")
  void updateAssignmentDetails(Assignment updateAssignment);

  @Delete("DELETE FROM posts WHERE id = #{postId}")
  void deletePost(Long postId);

  @Select("SELECT * FROM posts WHERE lecture_id = #{lectureId} AND type = 'NOTICE' ORDER BY created_at DESC")
  List<Notice> findAllNoticesByLectureId(Long lectureId);

  List<Assignment> findAllAssignmentsByLectureId(Long lectureId);

  List<Post> findAllPostsByLectureId(Long lectureId);
}

