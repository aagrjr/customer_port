
package br.com.portfolio.configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.logstash.logback.decorate.JsonFactoryDecorator;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig implements JsonFactoryDecorator {

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        var module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ToStringSerializer());
        objectMapper.registerModules(new JavaTimeModule(), module, objectIdModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDefaultPropertyInclusion(Include.NON_EMPTY);
        return objectMapper;
    }

    private SimpleModule objectIdModule() {
        final var module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ToStringSerializer());
        return module;
    }

    @Override
    public JsonFactory decorate(JsonFactory factory) {
        var objectMapper = (ObjectMapper) factory.getCodec();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(objectIdModule());
        return factory;
    }



}
