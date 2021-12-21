package tr.com.obss.googlecalendarservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.googlecalendarservice.exception.GoogleBadRequestException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

@Slf4j
public class FileUtil {

    public static File convertToFile(MultipartFile multipartFile){
        if(multipartFile.getOriginalFilename() == null){
            throw new BadCredentialsException("Invalid Account File");
        }
        File file = new File(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (FileNotFoundException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return file;
    }
}
