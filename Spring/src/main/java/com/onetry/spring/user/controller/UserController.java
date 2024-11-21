package com.onetry.spring.user.controller;


import com.onetry.spring.common.CommonResponseDto;
import com.onetry.spring.user.dto.req.UpdateUserInfo;
import com.onetry.spring.user.dto.res.UserInfoResDto;
import com.onetry.spring.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;

@Tag(name = "[User] User API")
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 정보 조회", description = "토큰 정보 기반 유저 정보를 조회합니다")
    @GetMapping("/info")
    public ResponseEntity<CommonResponseDto<UserInfoResDto>> getUserInfo(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        UserInfoResDto userInfoResDto = userService.getUserInfo(email);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        "유저 정보 조회를 성공적으로 완료하였습니다.",
                        userInfoResDto
                )
        );
    }

    @Operation(summary = "프로필 사진 조회",description = "유저의 프로필 사진을 조회합니다.")
    @GetMapping("/profile")
    public ResponseEntity<byte[]> findProfileImg(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_PNG).body(
                userService.downloadProfileFromFileSystem(userDetails.getUsername())
        );
    }

    @Operation(summary = "프로필 사진 수정",description = "유저의 프로필 사진을 수정 합니다." +
            "<br> 유저의 Token 으로 유저를 구분짓습니다.")
    @PutMapping(consumes = MULTIPART_FORM_DATA)
    public ResponseEntity<CommonResponseDto<UserInfoResDto>> updateProfileToFileSystem(@AuthenticationPrincipal UserDetails userDetails,
                                                                                       @RequestParam("file") MultipartFile updateFile) throws Exception {
        String email = userDetails.getUsername();
        log.info("[updateProfile] 유저의 프로필 사진을 수정합니다. userEmail : {}", email);
        UserInfoResDto updateProfileImgUser = userService.updateProfileImg(email, updateFile);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        "프로필 사진 수정을 성공적으로 완료하였습니다.",
                        updateProfileImgUser)
        );
    }

    @Operation(summary = "유저 정보 수정",description = "해당 유저의 정보를 수정합니다. " + "<br> 아직까진 이름만 변경")
    @PatchMapping("/update")
    public ResponseEntity<CommonResponseDto<UserInfoResDto>> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                                            @RequestBody UpdateUserInfo updateUserInfo){
        UserInfoResDto userInfoResDto = userService.updateUserInfo(userDetails.getUsername(), updateUserInfo);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        "유저 정보 수정이 성공적으로 완료되었습니다.",
                        userInfoResDto));
    }

    @Operation(summary = "유저 삭제", description = "토큰 정보 기반으로 유저를 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<CommonResponseDto<Void>> deleteUser(@AuthenticationPrincipal UserDetails userDetails){
        userService.deleteUserInfo(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        "유저 삭제가 성공적으로 완료되었습니다.",
                        null));
    }
}
