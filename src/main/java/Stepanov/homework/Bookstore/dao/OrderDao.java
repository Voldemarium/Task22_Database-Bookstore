package Stepanov.homework.Bookstore.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;


@Repository
public class OrderDao {
    private static final String FIND_BY_ID = "select * from book_store.order where id = :id";

    private static final String SAVE = "" +
            "insert into book_store.order (order_date, book_id, quantity, price, purchase_amount)" +
            "values(:order_date, :book_id, :quantity, :price, :purchase_amount)";

    private final NamedParameterJdbcTemplate template;

    public OrderDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    //    Метод save сохраняет ордер в базу SQL
    public void save(Order order) {
        template.update(
                SAVE,
                new MapSqlParameterSource()
                        .addValue("order_date", new Date(System.currentTimeMillis())
                        )
                        .addValue("book_id", order.getBook_id())
                        .addValue("quantity", order.getQuantity())
                        .addValue("price", BookDao.priseBook(order.getBook_id()))
                        .addValue("purchase_amount", (
                                order.getQuantity() * BookDao.priseBook(order.getBook_id()))
                        )
        );
    }

    //     Метод findById получает ордер с базы SQL
    public Order findById(long id) {
        return template.query(
                        FIND_BY_ID,
                        new MapSqlParameterSource("id", id),
                        (rs, rn) -> Order.builder()
                                .id(rs.getLong("id"))
                                .order_date(rs.getDate("order_date"))
                                .book_id(rs.getLong("book_id"))
                                .quantity(rs.getInt("quantity"))
                                .price(rs.getInt("price"))
                                .purchase_amount(rs.getInt("purchase_amount"))
                                .build()
                ).stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("order with id = " + id + "is not found"));
    }
}
