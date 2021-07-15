package tech.peterj.coinpamp.services;

import org.springframework.stereotype.Service;
import tech.peterj.coinpamp.model.RedditPost;
import tech.peterj.coinpamp.repositories.RedditPostDao;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RedditPostService {

    private RedditPostDao dao;

    public RedditPostService(RedditPostDao dao) {
        this.dao = dao;
    }

    @Transactional
    public RedditPost savePost(RedditPost post) {
        Objects.requireNonNull(post);
        if (dao.exists(post.getId())) {
            return dao.update(post);
        }
        return dao.persist(post);
    }

    @Transactional
    public List<RedditPost> saveAllPosts(List<RedditPost> posts) {
        Objects.requireNonNull(posts);
        List<RedditPost> ret = new ArrayList<>();
        for (RedditPost post : posts) {
            ret.add(savePost(post));
        }
        return ret;
    }

}
