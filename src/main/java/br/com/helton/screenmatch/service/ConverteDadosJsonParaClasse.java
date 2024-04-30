package br.com.helton.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDadosJsonParaClasse implements IConverteDados{

    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public <T> T converterJsonParaClasse(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
