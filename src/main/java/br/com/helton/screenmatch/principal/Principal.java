package br.com.helton.screenmatch.principal;

import br.com.helton.screenmatch.model.DadosSerie;
import br.com.helton.screenmatch.model.DadosTemporadas;
import br.com.helton.screenmatch.service.ConsumoApi;
import br.com.helton.screenmatch.service.ConverteDadosJsonParaClasse;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDadosJsonParaClasse conversor = new ConverteDadosJsonParaClasse();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibirMenu(){
        System.out.println("Digite o nome da série para a busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDadosJson(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dadosSerie = conversor.converterJsonParaClasse(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporadas> temporadas = new ArrayList<>();

        for(int i = 1; i<=dadosSerie.totalTemporadas(); i++) {
            json = consumo.obterDadosJson(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
            DadosTemporadas dadosTemporada = conversor.converterJsonParaClasse(json, DadosTemporadas.class);
            temporadas.add(dadosTemporada);

        }
        temporadas.forEach(temporada -> {
            System.out.println("Temporada: " + temporada.numero());
            temporada.episodios().forEach(episodio -> System.out.println(episodio.titulo()));
        });

        //"https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c"
    }
}
