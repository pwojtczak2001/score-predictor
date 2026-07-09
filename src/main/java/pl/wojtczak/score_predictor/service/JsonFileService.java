package pl.wojtczak.score_predictor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.imports.MatchImportDto;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class JsonFileService {

    @Value("${json.file.path}")
    private String filePath;

    private final ObjectMapper objectMapper;

    public JsonFileService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<MatchImportDto> loadMatches() throws IOException {
            File jsonFile = new File(filePath);
            return objectMapper.readValue(jsonFile, new TypeReference<List<MatchImportDto>>() {});
        }
}
