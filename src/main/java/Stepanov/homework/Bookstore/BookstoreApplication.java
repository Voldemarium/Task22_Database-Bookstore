package Stepanov.homework.Bookstore;

import Stepanov.homework.Bookstore.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class BookstoreApplication {

    private static final Logger log = LoggerFactory.getLogger(BookstoreApplication.class);

    public static ConfigurableApplicationContext context;

    public static void main(String[] args) {

        context = SpringApplication.run(BookstoreApplication.class, args);

       //  Добавление автора в таблицу
        AuthorDao authorDao = context.getBean(AuthorDao.class);
        Author forUpdateAuthor = Author.builder()
                .name("Vladimir")
                .middle_name("Victorovich")
                .surname("Ivanov")
                .year_of_birth(1975)
                .build();
 //       authorDao.save(forUpdateAuthor);

        //  Добавление книги в таблицу
        BookDao bookDao = context.getBean(BookDao.class);
        Book forUpdateBook = Book.builder()
                .title("Summer")
                .author_id(5L)
                .pages(380)
                .publishing_house("Phoenix")
                .year_of_publication(2022)
                .publication_number(1)
                .price(600)
                .build();
 //        bookDao.save(forUpdateBook);

        //Выбор книги для покупки по названию и автору
        List<Book> bookList= bookDao.findByParameters("Summer", "Ivanov", "Vladimir", null);
        log.info("{}", bookList.toString());

        //  Добавление заказа (покупка) по id книги
        OrderDao orderDao = context.getBean(OrderDao.class);
        Order forUpdateOrder = Order.builder()
                .book_id(2)
                .quantity(3)
                .build();
 //       orderDao.save(forUpdateOrder);
    }
}
