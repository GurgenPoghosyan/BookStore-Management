package com.internship.bookstore.service.collection;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.CollectionNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.entity.author.Author;
import com.internship.bookstore.entity.book.Book;
import com.internship.bookstore.entity.collection.Collection;
import com.internship.bookstore.entity.genre.Genre;
import com.internship.bookstore.persistence.book.BookRepository;
import com.internship.bookstore.persistence.collection.CollectionRepository;
import com.internship.bookstore.persistence.genre.GenreRepository;
import com.internship.bookstore.transform.request.collection.CollectionCreateRequest;
import com.internship.bookstore.transform.request.collection.CollectionUpdateRequset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class CollectionService implements CRUDService<CollectionCreateRequest,
        CollectionUpdateRequset, ResponseEntity<Collection>, Long> {

    private final CollectionRepository collectionRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository,
                             BookRepository bookRepository,
                             GenreRepository genreRepository) {
        this.collectionRepository = collectionRepository;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public ResponseEntity<Collection> create(CollectionCreateRequest request) {
        Collection collection = new Collection();
        collection.setName(request.getName());
        Genre genre = genreRepository.findByGenreName(request.getGenreName()).orElseThrow(() -> new GenreNotFoundException(request.getGenreName()));
        collection.setGenre(genre);
        Collection savedCollection = collectionRepository.save(collection);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCollection);
    }

    @Override
    public ResponseEntity<Collection> get(Long id) {
        Collection collection = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        return ResponseEntity.ok(collection);
    }

    public List<Collection> getCollectionData(String name) {
        if (name != null) {
            return collectionRepository.findAll().stream().filter(book -> book.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return collectionRepository.findAll();
    }

    @Override
    public ResponseEntity<Collection> update(CollectionUpdateRequset request, Long id) {
        Collection collection = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        collection.setName(request.getName());
        Collection updatedCollection = collectionRepository.save(collection);
        return ResponseEntity.ok(updatedCollection);
    }

    @Override
    public void delete(Long id) {
        Collection collection = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        collectionRepository.delete(collection);
    }

    public ResponseEntity<Collection> addBookToCollection(Long bookId, Long collectionId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException(bookId));
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(() -> new CollectionNotFoundException(collectionId));
        collection.getBooks().add(book);
        Collection savedCollection = collectionRepository.save(collection);
        return ResponseEntity.ok(savedCollection);
    }
}
