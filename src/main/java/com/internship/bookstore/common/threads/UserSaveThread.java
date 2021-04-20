package com.internship.bookstore.common.threads;

import com.internship.bookstore.common.util.CsvParser;
import com.internship.bookstore.persistence.entity.UserEntity;
import com.internship.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@RequiredArgsConstructor
public class UserSaveThread implements Runnable {

    private final UserService userService;
    private final CsvParser csvParser;
    private final File csvFile;

    @Override
    public void run() {
        saveAll(csvFile);
    }

    private void saveAll(File csvFile) {
        List<UserEntity> entities = csvParser.parseUserCsv(csvFile);
        userService.saveAll(entities);
    }
}
