package br.com.portfolio.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("br.com.portfolio.repository")
@EnableMongoAuditing(dateTimeProviderRef = "offsetDateTimeProvider")
@Slf4j
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Autowired
    private Environment env;

    @Override
    protected String getDatabaseName() {
        return env.getProperty("spring.data.mongodb.database");
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(new ConnectionString(env.getProperty("spring.data.mongodb.uri")));
    }

    @Override
    public MongoCustomConversions customConversions() {
        final List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new DateToOffsetDateTimeConverter());
        converters.add(new OffsetDateTimeToDateConverter());
        return new MongoCustomConversions(converters);
    }

    class DateToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {

        @Override
        public OffsetDateTime convert(Date source) {
            return source == null ? null : OffsetDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC);
        }
    }

    class OffsetDateTimeToDateConverter implements Converter<OffsetDateTime, Date> {

        @Override
        public Date convert(OffsetDateTime source) {
            return source == null ? null : Date.from(source.toInstant());
        }
    }
}
