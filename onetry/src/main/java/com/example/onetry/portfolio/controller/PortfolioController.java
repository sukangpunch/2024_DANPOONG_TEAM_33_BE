package com.example.onetry.portfolio.controller;

import com.example.onetry.common.CommonResponseDto;
import com.example.onetry.portfolio.dto.req.PortfolioCreateDto;
import com.example.onetry.common.FileDownload;
import com.example.onetry.portfolio.dto.res.PortfolioInfo;
import com.example.onetry.portfolio.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "[Portfolio] 포트 폴리오를 저장, 삭제, 조회 합니다.")
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/portfolio")
public class PortfolioController {
    private final PortfolioService portfolioService;

    @Operation(summary = "포트폴리오 상세 정보 조회", description = "portfolioId에 해당하는 포트폴리오를 조회")
    @GetMapping("/{portfolioId}")
    public ResponseEntity<CommonResponseDto<PortfolioInfo>> getPortfolioDetail(
            @PathVariable("portfolioId") Long portfolioId,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        PortfolioInfo portfolioInfo= portfolioService.getPortfolioInfo(portfolioId, email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "자격증 상세 정보를 성공적으로 조회하였습니다.",
                portfolioInfo
        ));
    }

    @Operation(summary = "유저의 포트폴리오 전부 조회", description = "유저의 모든 포트폴리오를 가져옵니다.")
    @GetMapping("/all")
    public ResponseEntity<CommonResponseDto<List<PortfolioInfo>>> getAllPortfolio(
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        List<PortfolioInfo> portfolioInfos = portfolioService.getAllPortfolio(email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "유저의 모든 포트폴리오를 성공적으로 조회하였습니다.",
                portfolioInfos
        ));
    }

    @Operation(summary = "포트폴리오를 추가합니다.", description = "인증된 유저의 포트폴리오를 추가합니다.")
    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto<PortfolioInfo>> addCertification(
            @ModelAttribute PortfolioCreateDto portfolioCreateDto,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        PortfolioInfo portfolioInfo = portfolioService.addCreatePortfolio(email, portfolioCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                "유저의 포트폴리오를 성공적으로 추가 하였습니다.",
                portfolioInfo
        ));
    }

    @Operation(summary = "포트폴리오 삭제", description = "포트폴리오를 삭제합니다.")
    @DeleteMapping("/delete/{portfolioId}")
    public ResponseEntity<CommonResponseDto<Void>> deletePortfolio(
            @PathVariable("portfolioId") Long portfolioId,
            @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        portfolioService.deletePortfolio(portfolioId, email);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto<>(
                        portfolioId + "번 포트폴리오가 삭제되었습니다",
                        null
        ));
    }


    @Operation(summary = "포트폴리오 다운로드",description = "유저의 포트폴리오를 다운로드합니다.")
    @GetMapping("/download")
    public ResponseEntity<Resource> findPortfolioFile(final @RequestParam("portfolioName") String portfolioName){
        FileDownload portFolioDownload = portfolioService.downloadPortfolioFromFileSystem(portfolioName);
        String encodedFileName = UriUtils.encodePathSegment(portFolioDownload.fileName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + ".pdf\"";
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(portFolioDownload.resource());
    }
}
