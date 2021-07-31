package tech.peterj.coinpamp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import tech.peterj.coinpamp.model.RedditPost;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public class RedditPostDao extends GenericDao<RedditPost> {

    protected RedditPostDao() {
        super(RedditPost.class);
    }

    public List<RedditPost> findTopNPostsOfToday(int n) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp yesterday = Timestamp.from(c.getTime().toInstant());

        return em.createNamedQuery("RedditPost.findTopNPostsOfToday", RedditPost.class)
                .setParameter("yesterday", yesterday)
                .setMaxResults(n)
                .getResultList();
    }

}
