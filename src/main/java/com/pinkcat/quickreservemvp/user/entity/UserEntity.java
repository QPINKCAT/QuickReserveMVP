package com.pinkcat.quickreservemvp.user.entity;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(name = "pk", column = @Column(name = "user_pk"))
public class UserEntity extends BaseEntity {
  @Comment("유저 아이디")
  @Column(name = "user_id", length = 320, unique = true, nullable = false)
  private String id;

  @Comment("유저명")
  @Column(name = "user_name", length = 20, nullable = false)
  private String name;

  @Comment("유저 비밀번호")
  @Column(name = "user_pw", length = 255, nullable = false)
  private String password;

  @Comment("유저 핸드폰번호")
  @Column(name = "user_phone_number", nullable = false)
  @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 핸드폰 번호 형식이 아닙니다.")
  private String phoneNumber;

  @Comment("유저 이메일")
  @Column(name = "user_email", nullable = false)
  @Pattern(
      regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
      message = "올바른 이메일 주소 형식이 아닙니다.")
  private String email;

  @Comment("유저 성별")
  @Column(name = "user_gender")
  private GenderEnum gender;
}
