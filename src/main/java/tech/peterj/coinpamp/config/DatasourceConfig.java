package tech.peterj.coinpamp.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {

    /**
     * Creates datasource from local env variables
     *
     * @return Datasource
     */
    @Bean
    public DataSource getDataSource() {
        String url = System.getenv("COINPAMP_DB_URL");
        String user = System.getenv("COINPAMP_DB_USERNAME");
        String password = System.getenv("COINPAMP_DB_PASSWORD");

        return DataSourceBuilder.create()
                .url(url)
                .username(user)
                .password(password)
                .build();
    }

}
