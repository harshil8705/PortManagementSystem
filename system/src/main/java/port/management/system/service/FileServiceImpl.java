package port.management.system.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import port.management.system.exception.ApiException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        if (file == null || file.getOriginalFilename() == null) {

            throw new ApiException("File or Filename cannot be null.");

        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equalsIgnoreCase("image/jpeg")
                        && !contentType.equalsIgnoreCase("image/png"))) {

            throw new ApiException("Only JPG and PNG images are allowed.");

        }

        String extension = file.getOriginalFilename().substring(
                        file.getOriginalFilename().lastIndexOf('.'))
                .toLowerCase();

        if (!extension.equals(".jpg") && !extension.equals(".png") && !extension.equals(".jpeg")) {

            throw new ApiException("Invalid image extension. Only .jpg and .png allowed.");

        }

        long maxSizeInBytes = 6 * 1024 * 1024;

        if (file.getSize() > maxSizeInBytes) {

            throw new ApiException("Image size must be less than 6MB.");

        }

        String fileName;
        File destinationFile;

        do {
            String randomId = UUID.randomUUID().toString();
            fileName = randomId + extension;
            destinationFile = new File(path + File.separator + fileName);
        } while (destinationFile.exists());

        File folder = new File(path);
        if (!folder.exists()) folder.mkdirs();

        Files.copy(file.getInputStream(), destinationFile.toPath());

        return destinationFile.getAbsolutePath();

    }

}
