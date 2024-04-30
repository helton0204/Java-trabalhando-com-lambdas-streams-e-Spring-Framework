package br.com.helton.screenmatch;

import br.com.helton.screenmatch.model.DadosSerie;
import br.com.helton.screenmatch.principal.Principal;
import br.com.helton.screenmatch.service.ConsumoApi;
import br.com.helton.screenmatch.service.ConverteDadosJsonParaClasse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibirMenu();
	}
}
