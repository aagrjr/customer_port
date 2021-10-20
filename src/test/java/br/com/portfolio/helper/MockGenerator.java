package br.com.portfolio.helper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class MockGenerator implements BeforeAllCallback {

    private static MockGenerator instance;
    private final ObjectMapper objectMapper;
    private final Map<String, String> mockData = new HashMap<>();

    private MockGenerator() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDefaultPropertyInclusion(Include.NON_EMPTY);

        loadData();
        System.out.println("Total of loaded files : " + mockData.size());
    }

    public static MockGenerator instance() {
        if (instance == null) {
            instance = new MockGenerator();
        }
        return instance;
    }

    public String asString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public MappingJackson2HttpMessageConverter getHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    public MockTranslator generateFromJson(String fileName) {
        return new MockTranslator(objectMapper, data(fileName + ".json"));
    }

    public MockTranslator generateFrom(String value) {
        return new MockTranslator(objectMapper, value);
    }

    public MockTranslator generateFrom(Map<String, Object> map) {
        return new MockTranslator(objectMapper, map);
    }

    public String data(String fileName) {
        return mockData.get(fileName);
    }

    // Load mock files in Classpath
    private void loadData() {
        try {
            loadDataIn("/mock");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataIn(String location) {
        File inner = new File(getClass().getResource(location).getFile());

        final var files = inner.listFiles();

        Stream.of(files).filter(File::isFile).filter(file -> file.getName().contains(".json")).map(file -> file.getName())
                .forEach(name -> mockData.put(name, readJsonFile(location.concat("/").concat(name))));
    }

    private String readJsonFile(String fileName) {
        String content = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
                content += "\n";
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

    }

    public class MockTranslator {

        private final ObjectMapper jsonMapper;
        private final Map<String, Object> dataMap = new LinkedHashMap<>();

        private MockTranslator(ObjectMapper jsonMapper, final String data) {
            this.jsonMapper = jsonMapper;
            fillMap(data);
        }

        private MockTranslator(ObjectMapper jsonMapper, final Map<String, Object> map) {
            this.jsonMapper = jsonMapper;
            this.dataMap.putAll(map);
        }

        private void fillMap(String data) {
            try {
                final var customMap = jsonMapper.readValue(data, Map.class);
                for (Object key : customMap.keySet()) {
                    dataMap.put(String.valueOf(key), customMap.get(key));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public MockTranslator andSet(String paramName, Object value) {
            this.dataMap.put(paramName, value);
            return this;
        }

        public MockTranslator andReplicate(Object value, String... paramNames) {
            for (String param : paramNames) {
                this.dataMap.put(param, value);
            }
            return this;
        }

        public <T> T as(Class<T> clazz) {
            try {
                return jsonMapper.readValue(rawString(), clazz);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public String rawString() {
            try {
                return jsonMapper.writeValueAsString(dataMap);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

}
