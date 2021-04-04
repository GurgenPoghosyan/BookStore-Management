package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.common.exceptions.RoleNotFoundException;
import com.internship.bookstore.persistence.entity.*;
import com.internship.bookstore.persistence.repository.*;
import com.internship.bookstore.service.dto.GenreDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class CSVReaderService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;
    private final FileStorageRepository fileStorageRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final Path fileStorageLocation;
    private final RoleRepository roleRepository;

    public CSVReaderService(AuthorRepository authorRepository, PublisherRepository publisherRepository, GenreRepository genreRepository, UserRepository userRepository, UserDetailsRepository userDetailsRepository, FileStorageEntity fileStorageEntity, FileStorageRepository fileStorageRepository, BookRepository bookRepository, CommunityRepository communityRepository, RoleRepository roleRepository) {
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.fileStorageLocation = Paths.get(fileStorageEntity.getUploadDir()).toAbsolutePath().normalize();
        this.bookRepository = bookRepository;
        this.communityRepository = communityRepository;
        this.roleRepository = roleRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void csvBooksProcessor(MultipartFile multipartFile) {
        List<BookEntity> books = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withQuote(null).withDelimiter(';'))) {

            List<CSVRecord> csvRecords = csvParser.getRecords();
            int maxCount = 10;
            long i = 0;

            for (CSVRecord csvRecord : csvRecords) {
                String extension = ".jpg";
                String author = csvRecord.get("Book-Author");
                AuthorEntity authorEntity = new AuthorEntity();
                BookEntity bookEntity = new BookEntity();
                authorEntity.setName(author);
                AuthorEntity byName = authorRepository.findByName(author);
                if (byName != null) {
                    bookEntity.getAuthors().add(byName);
                } else {
                    AuthorEntity savedAuthor = authorRepository.save(authorEntity);
                    bookEntity.getAuthors().add(savedAuthor);
                }

                String publisher = csvRecord.get("Publisher");
                PublisherEntity publisherEntity = publisherRepository.findByName(publisher);
                if (publisherEntity != null) {
                    bookEntity.setPublisher(publisherEntity);
                } else {
                    PublisherEntity entity = new PublisherEntity();
                    entity.setName(publisher);
                    PublisherEntity savedPublisher = publisherRepository.save(entity);
                    bookEntity.setPublisher(savedPublisher);
                }
                bookEntity.setName(csvRecord.get("Book-Title"));
                bookEntity.setPages(ThreadLocalRandom.current().nextInt(100, 800));
                bookEntity.setDate(csvRecord.get("Year-Of-Publication"));
                bookEntity.setRating(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 10)).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bookEntity.setIsbn(csvRecord.get("ISBN"));

                    FileStorageEntity fileStoreEntity = new FileStorageEntity();
                    String fileUrl = csvRecord.get("Image-URL-S");
                    FileUtils.copyURLToFile(new URL(fileUrl), new File(fileStorageLocation.toString() + csvRecord.getRecordNumber()+".jpg"));
                    Path path = new File(fileStorageLocation.toString() + csvRecord.getRecordNumber()+".jpg").toPath();
                    fileStoreEntity.setDocumentFormat(Files.probeContentType(path));
                    fileStoreEntity.setExtension(extension);
                    fileStoreEntity.setFileName(csvRecord.getRecordNumber() + ".jpg");
                    fileStoreEntity.setUploadDir(fileStorageLocation.toString());
                    fileStoreEntity.setCreatedDate(LocalDateTime.now());
                    FileStorageEntity savedFile = fileStorageRepository.save(fileStoreEntity);
                    bookEntity.setBookCoverImage(savedFile);
                books.add(bookEntity);
                if (i == maxCount) {
                    bookRepository.saveAll(books);
                    maxCount = maxCount + 10;
                    books = new ArrayList<>();
                }
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFileFromUrl(String fileName,
                                String fileUrl) throws IOException {
        FileUtils.copyURLToFile(new URL(fileUrl), new File(fileName));
    }

    public List<GenreDto> csvGenreProcessor(MultipartFile multipartFile) {
        String line = "";
        String nextLine;
        List<GenreDto> genres = new ArrayList<>();

        try (Scanner s = new Scanner(multipartFile.getInputStream())) {
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
                                System.out.println(i + " " + Arrays.toString(values));
                                GenreEntity entity = genreRepository.findByGenreName(values[1]);
                                if (entity == null) {
                                    GenreEntity genreEntity = new GenreEntity();
                                    genreEntity.setGenreName(values[1]);
                                    GenreEntity savedGenre = genreRepository.save(genreEntity);
                                    genres.add(GenreDto.mapEntityToDto(savedGenre));
                                }
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

    public void csvAssignGenreToBook(MultipartFile multipartFile) {
        String line = "";
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                BookEntity bookEntity = bookRepository.findById(Long.parseLong(values[0])).orElseThrow(() -> new BookNotFoundException(Long.parseLong(values[0])));
                GenreEntity genreEntity = genreRepository.findById(Long.parseLong(values[1])).orElseThrow(() -> new GenreNotFoundException(Long.parseLong(values[1])));
                bookEntity.getGenres().add(genreEntity);
                bookRepository.save(bookEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void csvUsersProcessor(MultipartFile multipartFile) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        List<UserEntity> users = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withQuote(null).withDelimiter(','))) {

            List<CSVRecord> csvRecords = csvParser.getRecords();
            int maxCount = 10;
            long i = 0;

            for (CSVRecord csvRecord : csvRecords) {
                UserEntity userEntity = new UserEntity();
                UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
                CommunityEntity communityEntity = new CommunityEntity();

                communityEntity.setName(csvRecord.get("State"));
                communityEntity.setZip(csvRecord.get("Zip"));
                CommunityEntity savedCommunity = communityRepository.save(communityEntity);
                userEntity.getUserCommunities().add(savedCommunity);
                userEntity.setUsername(csvRecord.get("User Name"));
                userEntity.setPassword(bCryptPasswordEncoder.encode(csvRecord.get("Password;;;;")));
                userEntity.setStatus("ACTIVE");
                RoleEntity roleEntity = roleRepository.findById(1L).orElseThrow(()->new RoleNotFoundException(1L));
                userEntity.setRole(roleEntity);
                userDetailsEntity.setFirstName(csvRecord.get("First Name"));
                userDetailsEntity.setLastName(csvRecord.get("Last Name"));
                userDetailsEntity.setEmailAddress(csvRecord.get("E Mail"));
                userDetailsEntity.setPhoneNumber(csvRecord.get("Phone No."));
                userDetailsEntity.setAddress(csvRecord.get("Country") + "," +
                        csvRecord.get("City"));
                userDetailsEntity.setUser(userEntity);
                UserDetailsEntity savedUserDetails = userDetailsRepository.save(userDetailsEntity);
                userEntity.setDetails(savedUserDetails);
                users.add(userEntity);
                if (i == maxCount) {
                    userRepository.saveAll(users);
                    maxCount = maxCount + 10;
                    users = new ArrayList<>();
                }
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
