package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.persistence.entity.*;
import com.internship.bookstore.persistence.repository.*;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.service.dto.GenreDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private final FileStoreRepository fileStoreRepository;

    @Value("${file.upload-dir}")
    private String pathDirectory;

    @Autowired
    public CSVReaderService(AuthorRepository authorRepository,
                            BookRepository bookRepository,
                            PublisherRepository publisherRepository,
                            GenreRepository genreRepository,
                            FileStoreRepository fileStoreRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.genreRepository = genreRepository;
        this.fileStoreRepository = fileStoreRepository;
    }

//    public List<BookDto> csvBooksProcessor() {
//
//        String csvFile = "C:/Users/pogho/Desktop/books.csv";
//        String line = "";
//        String cvsSplitBy = ",";
//        List<BookDto> books = new ArrayList<>();
//
//        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
//            br.readLine();
//            while ((line = br.readLine()) != null) {
//                BookEntity bookEntity = new BookEntity();
//                String[] dataPoints = line.split(cvsSplitBy);
//                AuthorDto authorDto = new AuthorDto();
//                String[] splitAuthors = dataPoints[2].split("/");
//                for (String s : splitAuthors) {
//                    authorDto.setName(s);
//                    AuthorEntity byName = authorRepository.findByName(s);
//                    if (byName != null) {
//                        bookEntity.getAuthors().add(byName);
//                        continue;
//                    }
//                    AuthorEntity authorEntity = AuthorDto.mapDtoToEntity(authorDto);
//                    AuthorEntity savedAuthor = authorRepository.save(authorEntity);
//                    bookEntity.getAuthors().add(savedAuthor);
//                }
//                PublisherEntity publisherEntity = publisherRepository.findByName(dataPoints[11].split(";")[0]);
//                if (publisherEntity != null) {
//                    bookEntity.setPublisher(publisherEntity);
//                } else {
//                    PublisherEntity entity = new PublisherEntity();
//                    entity.setName(dataPoints[11].split(";")[0]);
//                    PublisherEntity savedPublisher = publisherRepository.save(entity);
//                    bookEntity.setPublisher(savedPublisher);
//                }
//                bookEntity.setName(dataPoints[1]);
//                bookEntity.setPages(Integer.parseInt(dataPoints[7]));
//                bookEntity.setDate(dataPoints[10]);
//                bookEntity.setRating(Double.parseDouble(dataPoints[3]));
//                bookEntity.setLanguage(dataPoints[6]);
//                BookEntity savedBook = bookRepository.save(bookEntity);
//                books.add(BookDto.mapEntityToDto(savedBook));
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return books;
//    }

//    public List<GenreDto> csvGenreProcessor() {
//
//        File csvFile = new File("C:/Users/pogho/Desktop/Book reviews/Preprocessed_data.csv");
//        String line = "";
//        String nextLine;
//        List<GenreDto> genres = new ArrayList<>();
//
//        try (Scanner s = new Scanner(csvFile)) {
//            s.nextLine();
//            long i = 0L;
//            line = s.nextLine();
//            String[] values = new String[0];
//            while (s.hasNextLine()) {
//                nextLine = s.nextLine();
//                int k = 0;
//                if (Character.isDigit(nextLine.charAt(0))) {
//                    k = 0;
//                    while (Character.isDigit(nextLine.charAt(k))) {
//                        k++;
//                    }
//                }
//                if (k == String.valueOf(i).length()) {
//                    line = nextLine;
//                } else {
//                    while (!line.startsWith(String.valueOf(i + 1))) {
//                        String[] values2 = nextLine.split("\"");
//                        if (values2.length > 1) {
//                            values = nextLine.split("'");
//                            if (values.length > 1) {
//                                if (values.length == 2) {
//                                    values = nextLine.split("\"\"");
//                                }
//                                System.out.println(i + " " + Arrays.toString(values));
//                                GenreEntity entity = genreRepository.findByGenreName(values[1]);
//                                if (entity == null) {
//                                    GenreEntity genreEntity = new GenreEntity();
//                                    genreEntity.setGenreName(values[1]);
//                                    GenreEntity savedGenre = genreRepository.save(genreEntity);
//                                    genres.add(GenreDto.mapEntityToDto(savedGenre));
//                                }
//                            }
//                            line = s.nextLine();
//                        } else {
//                            nextLine = s.nextLine();
//                        }
//                    }
//                }
//                i++;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return genres;
//    }
//
//    public void csvAssignGenreToBook() {
//        String csvFile = "C:/Users/pogho/Desktop/book-genres.csv";
//        String line = "";
//        String cvsSplitBy = ";";
//
//        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
//            while ((line = br.readLine()) != null) {
//                String[] values = line.split(cvsSplitBy);
//                BookEntity bookEntity = bookRepository.findById(Long.parseLong(values[0])).orElseThrow(() -> new BookNotFoundException(Long.parseLong(values[0])));
//                GenreEntity genreEntity = genreRepository.findById(Long.parseLong(values[1])).orElseThrow(() -> new GenreNotFoundException(Long.parseLong(values[1])));
//                bookEntity.getGenres().add(genreEntity);
//                bookRepository.save(bookEntity);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void csvAssignImagePathToBook() {
//        String csvFile = "C:/Users/pogho/Desktop/Book reviews/BX_Books.csv";
//        String line = "";
//        String cvsSplitBy = ";";
//        Long k = 1L;
//        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
//            br.readLine();
//            while (k != 11130) {
//                line = br.readLine();
//                String[] values = line.split(cvsSplitBy);
//                String imageUrl = values[7];
////                SaveImageFromUrl.saveImage(imageUrl, k + ".jpg");
//                Long finalK = k;
//                BookEntity bookEntity = bookRepository.findById(k).orElseThrow(() -> new BookNotFoundException(finalK));
//                bookEntity.setImagePath("C:/Users/pogho/IdeaProjects/BookStore-Management/src/main/resources/static/" + values[7].split("/")[5]);
//                bookRepository.save(bookEntity);
//                k++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void csvBooksProcessor(MultipartFile multipartFile) {
        List<BookEntity> books = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withQuote(null).withDelimiter(';'))) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            int maxCount = 1000;
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

//                    FileStoreEntity fileStoreEntity = new FileStoreEntity();
//                    String fileUrl = csvRecord.get("Image-URL-S");
//                    FileUtils.copyURLToFile(new URL(fileUrl), new File(pathDirectory + csvRecord.getRecordNumber()+".jpg"));
//
//                    fileStoreEntity.setBookCover(csvRecord.get("Book-Title"));
//                    fileStoreEntity.setExtension(extension);
//                    fileStoreEntity.setFilePath(pathDirectory + csvRecord.getRecordNumber() + ".jpg");
//                    fileStoreEntity.setPathDirectory(pathDirectory);
//                    fileStoreEntity.setFileName(String.valueOf(csvRecord.getRecordNumber()));
//                    fileStoreEntity.setCreatedDate(LocalDateTime.now());
//                    FileStoreEntity savedFile = fileStoreRepository.save(fileStoreEntity);
//                    bookEntity.setImageId(savedFile.getId());
                    books.add(bookEntity);
                    if (i==maxCount){
                        bookRepository.saveAll(books);
                        maxCount = maxCount+1000;
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
}
