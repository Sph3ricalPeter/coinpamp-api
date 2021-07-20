package tech.peterj.coinpamp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class CoinPrice {

    @Id
    @GeneratedValue
    private long id;

    private double price;
    private Timestamp timestamp;

    public CoinPrice() {

    }

    public CoinPrice(double price, Timestamp timestamp) {
        this.price = price;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
