package com.example.onetry.component.file;

import com.example.onetry.common.WriteFileToFileSystemResult;
import com.example.onetry.exception.CustomException;
import com.example.onetry.exception.ExceptionCode;
import com.example.onetry.util.FileNameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
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

    public byte[] downloadPhotoFromFileSystem(String filePath) {

        //jar파일에서 resource 폴더 경로가 달라지는 경우를 위한 로직
        //jar파일이 실행되면, 로컬(ide)에서의 resource 경로와, jar파일에서의 resource 경로가 달라진다.
        //JAR 파일 내부에 있는 리소스는 파일 시스템 경로가 아니라 클래스패스 내의 경로로 표현
        if(filePath.contains("/image/default.jpg"))
        {
            //절대적인 경로로 리소스에 접근해야 합니다. 이 방법은 클래스로더를 통해 리소스에 접근하며,
            //클래스로더는 클래스패스 상에 있는 리소스에 접근할 수있다.
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
                log.info("photoPath : {}",filePath);
                Path path = new File(filePath).toPath();
                return Files.readAllBytes(path);
            }catch (IOException e){
                log.error(e.getMessage());
                throw new CustomException(ExceptionCode.FILE_READ_ERROR);
            }
        }
    }

    public Resource downloadFromResourceFileSystem(String filePath) {
        try {
            log.info("certificationPath : {}", filePath);
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new CustomException(ExceptionCode.FILE_READ_ERROR);
            }

            return resource;
        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage());
            throw new CustomException(ExceptionCode.FILE_READ_ERROR);
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

    public WriteFileToFileSystemResult uploadFromFileSystem(String fileName, MultipartFile fileData){
        String generateName = FileNameGenerator.generatorName(fileName);
        String filePath = FOLDER_PATH + generateName;
        log.info("filePath : {}", filePath);
        try{
            // FOLDER_PATH 디렉토리 생성 확인 및 생성
            File directory = new File(FOLDER_PATH);
            if (!directory.exists()) {
                directory.mkdirs();  // 폴더 경로가 없다면 생성
            }
            fileData.transferTo(new File(filePath));
        }catch (Exception e){
            log.error("파일 업로드 중 에러 발생 !! {}", e.getMessage());
            throw new CustomException(ExceptionCode.FILE_WRITE_ERROR);
        }
        return new WriteFileToFileSystemResult(filePath, generateName);
    }
}
