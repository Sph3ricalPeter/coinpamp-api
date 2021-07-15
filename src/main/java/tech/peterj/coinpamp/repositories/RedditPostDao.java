package tech.peterj.coinpamp.repositories;

import org.springframework.stereotype.Repository;
import tech.peterj.coinpamp.model.RedditPost;

@Repository
public class RedditPostDao extends GenericDao<RedditPost> {

    protected RedditPostDao() {
        super(RedditPost.class);
    }
}
