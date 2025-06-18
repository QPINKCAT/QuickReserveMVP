package com.pinkcat.quickreservemvp.user.entity;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk")
    private Long userPk;

    @Comment("유저 아이디")
    @Column(name = "user_id", length = 320, unique = true, nullable = false)
    private String id;

    @Comment("유저명")
    @Column(name = "user_name", length = 20, unique = true, nullable = false)
    private String name;

    @Comment("유저 비밀번호")
    @Column(name = "user_pw", length = 20, unique = true, nullable = false)
    private String password;

    @Comment("유저 핸드폰번호")
    @Column(name = "user_phone_number", nullable = false)
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 핸드폰 번호 형식이 아닙니다.")
    private String phoneNumber;

    @Comment("유저 이메일")
    @Column(name = "user_email", nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "올바른 이메일 주소 형식이 아닙니다.")
    private String email;

    @Comment("유저 성별")
    @Column(name = "user_gender")
    private GenderEnum gender;
}