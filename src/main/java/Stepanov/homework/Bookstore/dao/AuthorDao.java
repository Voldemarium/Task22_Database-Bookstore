package Stepanov.homework.Bookstore.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthorDao {
    private static final String FIND_BY_ID = "select * from book_store.author where id = :id";
    private static final String FIND_BY_SURNAME = "select * from book_store.author where surname = :surname";
    private static final String SAVE = "" +
            "insert into book_store.author (name, middle_name, surname,  year_of_birth)" +
            "values(:name, :middle_name, :surname, :year_of_birth)";

    private final NamedParameterJdbcTemplate template;

    public AuthorDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    //    Метод save сохраняет автора в базу SQL
    public void save(Author author) {
        template.update(
                SAVE,
                new MapSqlParameterSource()
                        .addValue("name", author.getName())
                        .addValue("middle_name", author.getMiddle_name())
                        .addValue("surname", author.getSurname())
                        .addValue("year_of_birth", author.getYear_of_birth())
        );
    }

    //     Метод findById получает автора с базы SQL по id
    public Author findById(long id) {
        return template.query(
                        FIND_BY_ID,
                        new MapSqlParameterSource("id", id),
                        (rs, rn) -> Author.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .middle_name(rs.getString("middle_name"))
                                .surname(rs.getString("surname"))
                                .year_of_birth(rs.getInt("year_of_birth"))
                                .build()
                ).stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("author with id = " + id + "is not found"));
    }

    //     Метод findByParameters получает авторов с базы SQL по параметрам
    public List<Author> findByParameters(String surname, String name, String middle_name) {
        List<Author> newAuthorList = new ArrayList<>();
        List<Author> authorList = template.query(
                FIND_BY_SURNAME,
                new MapSqlParameterSource("surname", surname),
                (rs, rn) -> Author.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .middle_name(rs.getString("middle_name"))
                        .surname(rs.getString("surname"))
                        .year_of_birth(rs.getInt("year_of_birth"))
                        .build()
        );

        if (authorList.size() < 1) {
            throw new RuntimeException("book with surname = " + surname + " is not found");
        } else if (name != null || middle_name != null) {

            for (Author author : authorList) {
                if (middle_name == null) {
                    if (author.getName().equals(name)) {
                        newAuthorList.add(author);
                    }
                }
                if (name == null) {
                    if (author.getMiddle_name().equals(middle_name)) {
                        newAuthorList.add(author);
                    }
                }
                if (name != null && middle_name != null) {
                    if (author.getName().equals(name) && author.getMiddle_name().equals(middle_name)) {
                        newAuthorList.add(author);
                    }
                }
            }
        }
        return newAuthorList;
    }
}
