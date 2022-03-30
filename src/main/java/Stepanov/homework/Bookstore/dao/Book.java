package Stepanov.homework.Bookstore.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
public class Book {
    private long id;
    private String title;
    private long author_id;
    private int pages;
    private String publishing_house;
    private int year_of_publication;
    private int publication_number;
    private int price;
}
