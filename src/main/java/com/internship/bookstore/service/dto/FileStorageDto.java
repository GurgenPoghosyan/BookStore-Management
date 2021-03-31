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

    private BookDto book;

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

    public static FileStorageEntity mapDtoToEntity(FileStorageDto fileStorageDto) {
        if (fileStorageDto == null) {
            return null;
        }
        FileStorageEntity fileStorageEntity = new FileStorageEntity();
        fileStorageEntity.setFileName(fileStorageDto.getFileName());
        fileStorageEntity.setUploadDir(fileStorageDto.getPathDirectory());
        fileStorageEntity.setExtension(fileStorageDto.getExtension());
        fileStorageEntity.setCreatedDate(fileStorageDto.getCreatedDate());
        fileStorageEntity.setDocumentFormat(fileStorageDto.documentFormat);
        return fileStorageEntity;
    }
}
