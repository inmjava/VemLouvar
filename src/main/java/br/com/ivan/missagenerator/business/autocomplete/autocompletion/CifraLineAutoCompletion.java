package br.com.ivan.missagenerator.business.autocomplete.autocompletion;

import java.io.IOException;

import org.fife.ui.autocomplete.CompletionProvider;

import br.com.ivan.missagenerator.business.Processador;
import br.com.ivan.missagenerator.business.autocomplete.provider.ProcessadorAutocomplete;
import model.Momento;

public class CifraLineAutoCompletion extends ReplaceLineAutoCompletion{

	public CifraLineAutoCompletion(CompletionProvider provider) {
		super(provider);
	}
	
	@Override
	public String tratarReplacement(String replacement) {
		try {
			String url = replacement;
			String[] cifra0eApresentacao1Nome2 = Processador.getCifra0EApresentacao1Nome2(replacement);
			String nome = cifra0eApresentacao1Nome2[2];
			Momento momentoSalmo = ProcessadorAutocomplete.getMomentoSalmo();
			return ProcessadorAutocomplete.madeMusicLine(momentoSalmo.getId()+"", "-1", momentoSalmo.getNome(), nome, url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
