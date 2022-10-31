package com.trendyol.kubernetesoperatorapi.adapter.kubernetes.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.gson.Gson;
import com.trendyol.kubernetesoperatorapi.domain.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KubernetesYamlConverterComponent {

    public Object convertKubernetesYml(Map<String, String> variables, String path, Class clazz) {
        var mapper = new ObjectMapper(new YAMLFactory());
        try {
            var inputStream = getClass().getResourceAsStream(path);
            var reader = new BufferedReader(new InputStreamReader(inputStream));
            var contents = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            var deployment = mapper.readValue(contents, clazz);
            var json = new Gson().toJson(deployment);
            for (var entry : variables.entrySet()) {
                String variable = "__" + entry.getKey() + "__";
                json = json.replaceAll(variable, entry.getValue());
            }
            return new Gson().fromJson(json, clazz);
        } catch (Exception e) {
            log.error("Cannot convert kubernetes yml exceptionMessage: {}", e.getMessage());
            throw new BusinessException("Cannot convert kubernetes yml.");
        }
    }
}
