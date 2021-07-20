package tech.peterj.coinpamp.services;

import org.springframework.stereotype.Service;
import tech.peterj.coinpamp.model.Coin;
import tech.peterj.coinpamp.repositories.CoinDao;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class CoinService {

    private CoinDao dao;

    public CoinService(CoinDao dao) {
        this.dao = dao;
    }

    @Transactional
    public Coin saveCoin(Coin coin) {
        Objects.requireNonNull(coin);
        if (dao.exists(coin.getId())) {
            return dao.update(coin);
        }
        dao.persist(coin);
        return coin;
    }

    @Transactional
    public Coin findCoin(String id) {
        return dao.find(id);
    }

}
