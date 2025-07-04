package com.pinkcat.quickreservemvp.customer.dto;

import com.pinkcat.quickreservemvp.common.enums.GenderEnum;
import com.pinkcat.quickreservemvp.common.validation.user.EmailValid;
import com.pinkcat.quickreservemvp.common.validation.user.NameValid;
import com.pinkcat.quickreservemvp.common.validation.user.PhoneNumberValid;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerUpdateRequestDto {

    @NameValid
    private String name;

    @PhoneNumberValid
    private String phoneNumber;

    @EmailValid
    private String email;

    private GenderEnum gender;
}
