package ru.s21school.retailanalytics_web.controllers.tableControllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.exceptions.CsvExportException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class ImportExportHandler {
    private final String apiUrl;
    private final String tableName;
    private final RestTemplate restTemplate;

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        try {
            ResponseEntity<Resource> response =
                    restTemplate.exchange(apiUrl + "/export",
                            HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                            });
            Resource resource = response.getBody();
            if (resource == null) {
                throw new CsvExportException(String.format("Exception after request to {%s}. Status code: {%s}, but response body is null.", apiUrl, response.getStatusCode()));
            }
            servletResponse.setContentType("text/csv");
            servletResponse.addHeader("Content-Disposition", String.format("attachment; filename=\"%s.tsv\"", tableName));
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.getWriter().print(resource.getContentAsString(StandardCharsets.UTF_8));
        } catch (HttpClientErrorException exception) {
            throw new CsvExportException(String.format("Exception during request to {%s}. Status code: {%s}. Message is {%s}", apiUrl, exception.getStatusCode(), exception.getMessage()));
        }
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        Resource invoicesResource = file.getResource();
        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", invoicesResource);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, httpHeaders);
        String url = apiUrl + "/import";
        restTemplate.postForEntity(url, httpEntity, Object.class);
        return "redirect:/data/checks";
    }
}
