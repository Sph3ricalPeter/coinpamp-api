package tech.peterj.coinpamp.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Coin {

    @Id
    private String id;
    private String name;
    private String symbol;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    private List<CoinPrice> prices;

    public Coin() {

    }

    public Coin(String id, String name, String symbol, List<CoinPrice> prices) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.prices = prices;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<CoinPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<CoinPrice> prices) {
        this.prices = prices;
    }
}
