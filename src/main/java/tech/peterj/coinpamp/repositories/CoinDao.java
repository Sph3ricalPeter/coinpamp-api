package tech.peterj.coinpamp.repositories;

import org.springframework.stereotype.Repository;
import tech.peterj.coinpamp.model.Coin;

@Repository
public class CoinDao extends GenericDao<Coin>{

    protected CoinDao() {
        super(Coin.class);
    }

}
