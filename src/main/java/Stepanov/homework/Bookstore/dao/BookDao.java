package Stepanov.homework.Bookstore.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static Stepanov.homework.Bookstore.BookstoreApplication.context;

@Repository
public class BookDao {
    private static final String FIND_BY_ID = "select * from book_store.book where id = :id";
    private static final String FIND_BY_TITLE = "select * from book_store.book where title = :title";

    private static final String SAVE = "" +
            "insert into book_store.book (title, author_id, pages, publishing_house, year_of_publication, publication_number, price)" +
            "values(:title, :author_id, :pages, :publishing_house, :year_of_publication, :publication_number, :price)";

    private final NamedParameterJdbcTemplate template;

    public BookDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    //    Метод save сохраняет книгу в базу SQL
    public void save(Book book) {
        template.update(
                SAVE,
                new MapSqlParameterSource()
                        .addValue("title", book.getTitle())
                        .addValue("author_id", book.getAuthor_id())
                        .addValue("pages", book.getPages())
                        .addValue("publishing_house", book.getPublishing_house())
                        .addValue("year_of_publication", book.getYear_of_publication())
                        .addValue("publication_number", book.getPublication_number())
                        .addValue("price", book.getPrice())
        );
    }

    //     Метод findById получает книгу с базы SQL
    public Book findById(long id) {
        return template.query(
                        FIND_BY_ID,
                        new MapSqlParameterSource("id", id),
                        (rs, rn) -> Book.builder()
                                .id(rs.getLong("id"))
                                .title(rs.getString("title"))
                                .author_id(rs.getLong("author_id"))
                                .pages(rs.getInt("pages"))
                                .publishing_house("publishing_house")
                                .year_of_publication(rs.getInt("year_of_publication"))
                                .publication_number(rs.getInt("publication_number"))
                                .price(rs.getInt("price"))
                                .build()
                ).stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("book with id = " + id + "is not found"));
    }

    //     Метод priseBook получает цену книги по id с базы SQL
    public static int priseBook(long id) {
        BookDao bookDao = context.getBean(BookDao.class);
        Book book = bookDao.findById(id);
        return book.getPrice();
    }

    // Метод ищет книгу по параметрам (по названию и автору)
    public List<Book> findByParameters(String title, String surname, String name, String middle_name) {
        List<Book> newListBook = new ArrayList<>();
        List<Book> listBook = template.query(
                FIND_BY_TITLE,
                new MapSqlParameterSource("title", title),
                (rs, rn) -> Book.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .author_id(rs.getLong("author_id"))
                        .pages(rs.getInt("pages"))
                        .publishing_house("publishing_house")
                        .year_of_publication(rs.getInt("year_of_publication"))
                        .publication_number(rs.getInt("publication_number"))
                        .price(rs.getInt("price"))
                        .build()
        );

        if (listBook.size() < 1) {
            throw new RuntimeException("book with title = " + title + " is not found");
        } else if (surname != null) {
            AuthorDao authorDao = context.getBean(AuthorDao.class);
            List<Author> authorList = authorDao.findByParameters(surname, name, middle_name);
            for (int i = 0; i < authorList.size(); i++) {
                long author_id = authorList.get(i).getId();
                for (Book book : listBook) {
                    if (book.getAuthor_id() == author_id) {
                        newListBook.add(listBook.get(i));
                    }
                }
            }
        } else {
            newListBook = listBook;
        }
        return newListBook;
    }
}
