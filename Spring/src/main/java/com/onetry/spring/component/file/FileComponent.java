package com.onetry.spring.component.file;

import com.onetry.spring.exception.CustomException;
import com.onetry.spring.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
@Log4j2
public class FileComponent {


    @Value("${fileSystemPath}")
    private String FOLDER_PATH;

    public byte[] downloadProfileFromFileSystem(String filePath) {

        if(filePath.contains("/image/default.jpg"))
        {
            try(InputStream inputStream = getClass().getResourceAsStream(filePath)){
                if(inputStream == null){
                    throw new IOException("Resource not found: " + filePath);
                }
                return inputStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try{
                log.info("profilePath : {}",filePath);
                Path path = new File(filePath).toPath();
                return Files.readAllBytes(path);
            }catch (IOException e){
                log.error(e.getMessage());
                throw new CustomException(ExceptionCode.FILE_READ_ERROR);
            }
        }
    }

    public void deleteFileFromFileSystem(String fileName) {
        String filePath = FOLDER_PATH + fileName;
        try {
            Path path = Paths.get(filePath);
            Files.delete(path);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new CustomException(ExceptionCode.FILE_DELETE_ERROR);
        }
    }

    public void updateFileSystemProfileImg(String oldFilePath, String updateFilePath, MultipartFile updateFile) {
        try {
            if(!oldFilePath.equals("/image/default.jpg")){
                log.info("해당 사진을 삭제하고 다시 업데이트 : {}",oldFilePath);
                Path path = Paths.get(oldFilePath);
                Files.deleteIfExists(path);
            }
            updateFile.transferTo(new File(updateFilePath));
            log.info("transper까지 완료입니다. :{}",updateFilePath);
        }catch (IOException e){
            log.error(e.getMessage());
            throw new CustomException(ExceptionCode.FILE_WRITE_ERROR);
        }
    }
}
