package com.example.quicksells.domain.user.model.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-\\[\\]{};':\"\\\\|,.<>/?]).{10,}$",
            message = "비밀번호는 대소문자, 숫자, 특수문자를 포함한 10자 이상이어야 합니다.")
    private String password;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$",
            message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
    private String phone;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s,()-]{5,100}$",
            message = "주소는 5~100자의 한글, 영문, 숫자 및 특수문자(- , ())만 입력 가능합니다.")
    private String address;

    public boolean isAllFieldEmpty() {return password == null && phone == null && address == null;}
}
