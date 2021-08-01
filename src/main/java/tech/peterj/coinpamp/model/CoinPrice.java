package tech.peterj.coinpamp.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CoinPrice {

    @Id
    private long id;

    private double price;

    public CoinPrice() {

    }

    public CoinPrice(long id, double price) {
        this.id = id;
        this.price = price;
    }

    public void setId(long id) {
        this.id = id;
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

}
