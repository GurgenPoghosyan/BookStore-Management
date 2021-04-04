package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.FileStorageEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class FileStorageDto {
    private Long id;

    private String fileName;

    private String extension;

    private String pathDirectory;

    private String documentFormat;

    private LocalDateTime createdDate;

    private Long bookId;

    public static FileStorageDto mapEntityToDto(FileStorageEntity entity) {
        if (entity == null) {
            return null;
        }
        FileStorageDto dto = new FileStorageDto();
        dto.setId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setPathDirectory(entity.getUploadDir());
        dto.setExtension(entity.getExtension());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setDocumentFormat(entity.getDocumentFormat());
        return dto;
    }
}
