package br.com.helton.screenmatch.service;

public interface IConverteDados {
    <T> T converterJsonParaClasse(String jason, Class<T> classe);
}
