package Stepanov.homework.Bookstore.dao;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class Order {
    private long id;
    private Date order_date;
    private long book_id;
    private int quantity;
    private int price;
    private int purchase_amount;

}
