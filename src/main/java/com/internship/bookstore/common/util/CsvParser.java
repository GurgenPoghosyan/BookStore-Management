package com.internship.bookstore.common.util;

import com.internship.bookstore.persistence.entity.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class CsvParser {

    public List<UserEntity> parseUserCsv(File csvFile) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        List<UserEntity> users = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(csvFile));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withQuote(null).withDelimiter(','))) {

            List<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                UserEntity userEntity = new UserEntity();
                UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
                CommunityEntity communityEntity = new CommunityEntity();

                communityEntity.setName(csvRecord.get("State"));
                communityEntity.setZip(csvRecord.get("Zip"));

                userEntity.getUserCommunities().add(communityEntity);
                userEntity.setUsername(csvRecord.get("User Name"));
                userEntity.setPassword(bCryptPasswordEncoder.encode(csvRecord.get("Password;;;;")));
                userEntity.setStatus("ACTIVE");

                userDetailsEntity.setFirstName(csvRecord.get("First Name"));
                userDetailsEntity.setLastName(csvRecord.get("Last Name"));
                userDetailsEntity.setEmailAddress(csvRecord.get("E Mail"));
                userDetailsEntity.setPhoneNumber(csvRecord.get("Phone No."));
                userDetailsEntity.setAddress(csvRecord.get("County") + "," +
                        csvRecord.get("City"));
                userDetailsEntity.setUser(userEntity);
                userEntity.setDetails(userDetailsEntity);
                users.add(userEntity);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<BookEntity> parseBookCsv(File csvFile) {
        List<BookEntity> books = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(csvFile));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withQuote(null).withDelimiter(';'))) {

            List<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                BookEntity bookEntity = new BookEntity();

                String authorName = csvRecord.get("Book-Author");
                AuthorEntity authorEntity = new AuthorEntity();
                authorEntity.setName(authorName);
                bookEntity.getAuthors().add(authorEntity);

                String publisherName = csvRecord.get("Publisher");
                PublisherEntity publisherEntity = new PublisherEntity();
                publisherEntity.setName(publisherName);
                bookEntity.setPublisher(publisherEntity);

                bookEntity.setName(csvRecord.get("Book-Title"));
                bookEntity.setPages(ThreadLocalRandom.current().nextInt(100, 800));
                bookEntity.setDate(csvRecord.get("Year-Of-Publication"));
                bookEntity.setRating(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 10)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bookEntity.setIsbn(csvRecord.get("ISBN"));
                books.add(bookEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<GenreEntity> parseGenreCsv(File file) {
        String line = "";
        String nextLine;
        List<GenreEntity> genres = new ArrayList<>();

        try (Scanner s = new Scanner(file)) {
            s.nextLine();
            long i = 0L;
            line = s.nextLine();
            String[] values;
            while (s.hasNextLine()) {
                nextLine = s.nextLine();
                int k = 0;
                if (Character.isDigit(nextLine.charAt(0))) {
                    k = 0;
                    while (Character.isDigit(nextLine.charAt(k))) {
                        k++;
                    }
                }
                if (k == String.valueOf(i).length()) {
                    line = nextLine;
                } else {
                    while (!line.startsWith(String.valueOf(i + 1))) {
                        String[] values2 = nextLine.split("\"");
                        if (values2.length > 1) {
                            values = nextLine.split("'");
                            if (values.length > 1) {
                                if (values.length == 2) {
                                    values = nextLine.split("\"\"");
                                }
                                GenreEntity genreEntity = new GenreEntity();
                                genreEntity.setGenreName(values[1]);
                                genres.add(genreEntity);
                            }
                            line = s.nextLine();
                        } else {
                            nextLine = s.nextLine();
                        }
                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return genres;
    }
}
