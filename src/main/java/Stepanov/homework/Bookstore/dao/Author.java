package Stepanov.homework.Bookstore.dao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author {
    private long id;
    private String name;
    private String middle_name;
    private String surname;
    private int year_of_birth;
}
