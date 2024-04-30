package br.com.helton.screenmatch.service;

public interface IConverteDados {
    <T> T obterDados(String jason, Class<T> classe);
}
