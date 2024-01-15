package unit;

import com.example.bookstore.app.exception.BadRequestException;
import com.example.bookstore.app.model.author.Author_entity;
import com.example.bookstore.app.model.author.Author_model;
import com.example.bookstore.app.repository.AuthorDao;
import com.example.bookstore.app.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorDao authorDao;

    @InjectMocks
    private AuthorService authorService;


    //===========//===========//===========//===========//===========//
    //CREATE AUTHOR

    @Test
    public void createAuthor_PayloadValid_ReturningId() {
        Long id = 1L;

        Author_model author = Author_model.builder()
                .fio("Andrey AP")
                .photo("Photo")
                .biography("Biography")
                .build();

        Mockito.when(authorDao.createAuthor(author))
                .thenReturn(id);

        ResponseEntity<Long> response = authorService.createAuthor(author);

        assertNotNull(response);
        assertEquals(response.getBody(), id);
    }

    @Test
    public void createAuthor_InvalidPayload_ThrowsBadRequest() {

        Author_model author = Author_model.builder()
                .photo("Photo")
                .biography("Biography")
                .build();

        assertThrows(BadRequestException.class, () -> {
            authorService.createAuthor(author);
        });
    }


    //===========//===========//===========//===========//===========//
    //EDIT AUTHOR

    @Test
    public void editAuthor_ValidPayload_ReturningMessage() {

        Author_entity author = Author_entity.builder()
                .id(1L)
                .fio("Andrey AP")
                .photo("Photo")
                .biography("Biography")
                .build();

        Mockito.when(authorDao.IsAuthorExists(author.getId()))
                .thenReturn(true);

        ResponseEntity<String> response = authorService.editAuthor(author);

        assertNotNull(response);
    }

    @Test
    public void editAuthor_InvalidPayload_ThrowsBadRequest() {

        Author_entity author = Author_entity.builder()
                .id(1L)
                .photo("Photo")
                .biography("Biography")
                .build();

        Mockito.when(authorDao.IsAuthorExists(author.getId()))
                .thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            authorService.editAuthor(author);
        });
    }

    @Test
    public void editAuthor_AuthorDoesNotExists_ThrowsBadRequest() {

        Author_entity author = Author_entity.builder()
                .id(1L)
                .fio("Andrey AP")
                .photo("Photo")
                .biography("Biography")
                .build();

        Mockito.when(authorDao.IsAuthorExists(author.getId()))
                .thenReturn(false);

        assertThrows(BadRequestException.class, () -> {
            authorService.editAuthor(author);
        });
    }


    //===========//===========//===========//===========//===========//
    //DELETE AUTHOR

    @Test
    public void deleteAuthor_ValidPayload_ReturningMessage() {

        Long author_id = 1L;

        Mockito.when(authorDao.IsAuthorExists(author_id))
                .thenReturn(true);

        ResponseEntity<String> response = authorService.deleteAuthor(author_id);

        assertNotNull(response);
    }

    @Test
    public void deleteAuthor_AuthorDoesNotExists_ThrowsBadRequest() {

        Long author_id = 1L;

        Mockito.when(authorDao.IsAuthorExists(author_id))
                .thenReturn(false);

        assertThrows(BadRequestException.class, () -> {
            authorService.deleteAuthor(author_id);
        });
    }


    //===========//===========//===========//===========//===========//
    //GET AUTHOR BY ID

    @Test
    public void getAuthorById_ValidPayload_ReturningAuthorEntity() {

        Long author_id = 1L;
        Author_entity author = Author_entity.builder()
                .id(author_id)
                .fio("Andrey AP")
                .photo("Photo")
                .biography("Biography")
                .build();

        Mockito.when(authorDao.getAuthorById(author_id))
                .thenReturn(Optional.ofNullable(author));

        ResponseEntity<Author_entity> response = authorService.getAuthorById(author_id);

        assertNotNull(response);
        assertEquals(response.getBody(), author);
    }

    @Test
    public void getAuthorById_AuthorDoesntExists_ReturningSuccessResponse() {

        Long author_id = 1L;

        Mockito.when(authorDao.getAuthorById(author_id))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> {
            ResponseEntity<Author_entity> response = authorService.getAuthorById(author_id);
        });
    }


    //===========//===========//===========//===========//===========//
    //GET AUTHORS

    @Test
    public void getAuthors_ValidPayload_ReturningCollectionOfAuthorEntities() {

        Collection<Author_entity> authors = new ArrayList<>(List.of(
                Author_entity.builder()
                        .id(1L)
                        .fio("A_Andrey 1")
                        .photo("B_Photo")
                        .biography("Biography1")
                        .build(),
                Author_entity.builder()
                        .id(2L)
                        .fio("B_Andrey 2")
                        .photo("C_Photo")
                        .biography("Biography2")
                        .build(),
                Author_entity.builder()
                        .id(3L)
                        .fio("C_Andrey 3")
                        .photo("A_Photo")
                        .biography("Biography3")
                        .build()
        ));

        Mockito.when(authorDao.getAuthors(0L, 0L, "")).thenReturn(authors);

        ResponseEntity<Collection<Author_entity>> response = authorService.getAuthors(0L,0L,"");

        assertNotNull(response);
        assertEquals(response.getBody(), authors);
    }

    @Test
    public void getAuthors_InvalidPayload_ThrowsBadRequest() {

        assertThrows(BadRequestException.class, () -> {
            authorService.getAuthors(-1L,0L,"");
        });
    }

}
