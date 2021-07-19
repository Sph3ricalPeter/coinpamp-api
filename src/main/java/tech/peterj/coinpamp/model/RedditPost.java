package tech.peterj.coinpamp.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Timestamp;

@Entity
public class RedditPost {

    @Id
    private String id;

    @Lob
    private String title;

    private String author;

    @Lob
    private String text;

    private String ups;
    private Timestamp timestamp;
    private String permalink;

    public RedditPost() {}

    public RedditPost(String id, String title, String author, String text, String ups, Timestamp timestamp, String permalink) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.text = text;
        this.ups = ups;
        this.timestamp = timestamp;
        this.permalink = permalink;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }
}
