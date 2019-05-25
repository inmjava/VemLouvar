package br.com.ivan.missagenerator.business.autocomplete.autocompletion;

import java.io.IOException;

import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionProvider;

import br.com.ivan.missagenerator.business.Processador;
import br.com.ivan.missagenerator.business.autocomplete.provider.ProcessadorAutocomplete;
import model.Momento;

public class LetraLineAutoCompletion extends AutoCompletion{

	private JTextComponent txtApresentacao;

	public LetraLineAutoCompletion(CompletionProvider provider, JTextComponent txtApresentacao) {
		super(provider);
		this.txtApresentacao = txtApresentacao;
	}
	
	@Override
	protected void insertCompletion(Completion c, boolean typedParamListStartChar) {
		// replace nothing
		
		JTextComponent textComp = getTextComponent();
		String alreadyEntered = c.getAlreadyEntered(textComp);
		hidePopupWindow();
		Caret caret = textComp.getCaret();

		int dot = caret.getDot();
		int len = alreadyEntered.length();
		int start = dot - len;
		Document doc = textComp.getDocument();
		String replacement = getReplacementText(c, doc, start, len);

		String[] cifra0eApresentacao1Nome2 = Processador.getCifra0EApresentacao1Nome2(replacement);
		
		txtApresentacao.setText(cifra0eApresentacao1Nome2[1]);
		txtApresentacao.setCaretPosition(0);
		
	}
	
}
