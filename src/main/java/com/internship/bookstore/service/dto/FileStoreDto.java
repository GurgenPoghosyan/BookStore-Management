package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.FileStoreEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class FileStoreDto {
    private Long id;

    private String fileName;

    private String extension;

    private String pathDirectory;

    private LocalDateTime createdDate;
//todo entity to dto
    private BookDto book;

    public static FileStoreDto mapEntityToDto(FileStoreEntity entity) {
        if (entity == null) {
            return null;
        }
        FileStoreDto dto = new FileStoreDto();
        dto.setId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setPathDirectory(entity.getPathDirectory());
        dto.setExtension(entity.getExtension());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public static FileStoreEntity mapDtoToEntity(FileStoreDto fileStoreDto) {
        if (fileStoreDto == null) {
            return null;
        }
        FileStoreEntity fileStoreEntity = new FileStoreEntity();
        fileStoreEntity.setFileName(fileStoreDto.getFileName());
        fileStoreEntity.setPathDirectory(fileStoreDto.getPathDirectory());
        fileStoreEntity.setExtension(fileStoreDto.getExtension());
        fileStoreEntity.setCreatedDate(fileStoreDto.getCreatedDate());
        return fileStoreEntity;
    }
}
