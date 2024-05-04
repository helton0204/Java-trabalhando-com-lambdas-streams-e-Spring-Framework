package br.com.helton.screenmatch.principal;

import br.com.helton.screenmatch.model.DadosEpisodio;
import br.com.helton.screenmatch.model.DadosSerie;
import br.com.helton.screenmatch.model.DadosTemporada;
import br.com.helton.screenmatch.model.Episodio;
import br.com.helton.screenmatch.service.ConsumoApi;
import br.com.helton.screenmatch.service.ConverteDadosJsonParaClasse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDadosJsonParaClasse conversor = new ConverteDadosJsonParaClasse();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibirMenu(){
        System.out.println("Digite o nome da série para a busca");
        String nomeSerie = leitura.nextLine();
        String json = consumo.obterDadosJson(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dadosSerie = conversor.converterJsonParaClasse(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for(int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumo.obterDadosJson(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.converterJsonParaClasse(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);

        }

        temporadas.forEach(temporada -> {
            System.out.println("Temporada: " + temporada.numero());
            temporada.episodios().forEach(episodio -> System.out.println(episodio.titulo()));
        });

        List<DadosEpisodio> todosOsEpisodios = temporadas.stream()
                .flatMap(temporada -> temporada.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("Top 5 episódios de " + nomeSerie + "segundo melhor avaliacao:");
        todosOsEpisodios.stream()
                .filter(dadosEpisodio -> !dadosEpisodio.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        System.out.println("\nTop 10 episódios");
        todosOsEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .peek(e -> System.out.println("Ordenação " + e))
                .limit(10)
                .peek(e -> System.out.println("Limite " + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("Mapeamento " + e))
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(temporada -> temporada.episodios().stream()
                        .map(episodio -> new Episodio(temporada.numero(), episodio))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Digite um trecho do título do episódio");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episódio não encontrado!");
        }

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());


        System.out.println("A partir de que ano você deseja ver os episódios? ");
        int ano = leitura.nextInt();
        leitura.nextLine();
        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódio: " + e.getTitulo() +
                                " Data lançamento: " + e.getDataLancamento().format(formatador)
                ));


    }
}
