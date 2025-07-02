package com.pinkcat.quickreservemvp.customer.entity;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import com.pinkcat.quickreservemvp.common.model.BaseEntity;
import com.pinkcat.quickreservemvp.common.validation.ValidationPatterns;
import com.pinkcat.quickreservemvp.customer.dto.CustomerUpdateRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "customer")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(name = "pk", column = @Column(name = "customer_pk"))
public class CustomerEntity extends BaseEntity {
  //    @Id
  //    @GeneratedValue(strategy = GenerationType.IDENTITY)
  //    @Column(name = "customer_pk")
  //    private Long customerPk;

  @Comment("고객 아이디")
  @Column(name = "customer_id", length = 320, unique = true, nullable = false)
  private String id;

  @Comment("고객명")
  @Column(name = "customer_name", length = 20, nullable = false)
  private String name;

  @Comment("고객 비밀번호")
  @Column(name = "customer_password", length = 255, nullable = false)
  private String password;

  @Comment("고객 핸드폰번호")
  @Column(name = "customer_phone_number", nullable = false)
  @Pattern(regexp = ValidationPatterns.PHONE_NUMBER, message = "올바른 핸드폰 번호 형식이 아닙니다.")
  private String phoneNumber;

  @Comment("고객 이메일")
  @Column(name = "customer_email", nullable = false)
  @Pattern(regexp = ValidationPatterns.EMAIL, message = "올바른 이메일 주소 형식이 아닙니다.")
  private String email;

  @Enumerated(EnumType.STRING)
  @Comment("고객 성별")
  @Column(name = "customer_gender")
  private GenderEnum gender;

  public void updateInfo(CustomerUpdateRequestDto dto) {
    if (dto.getName() != null && !dto.getName().isBlank()) {
      this.name = dto.getName();
    }
    if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank()) {
      this.phoneNumber = dto.getPhoneNumber();
    }
    if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
      this.email = dto.getEmail();
    }
    if (dto.getGender() != null) {
      this.gender = dto.getGender();
    }
  }

  public void updateEncordedPassword(String newEncordedPassword) {
    this.password = newEncordedPassword;
  }
}
