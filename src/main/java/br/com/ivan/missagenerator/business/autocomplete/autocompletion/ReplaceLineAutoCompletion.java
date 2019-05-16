package br.com.ivan.missagenerator.business.autocomplete.autocompletion;


import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class ReplaceLineAutoCompletion extends AutoCompletion{

	public ReplaceLineAutoCompletion(CompletionProvider provider) {
		super(provider);
	}

	protected void insertCompletion(Completion c, boolean typedParamListStartChar) {

		JTextComponent textComp = getTextComponent();
		String alreadyEntered = c.getAlreadyEntered(textComp);
		hidePopupWindow();
		Caret caret = textComp.getCaret();

		int dot = caret.getDot();
		int len = alreadyEntered.length();
		int start = dot - len;
		Document doc = textComp.getDocument();
		String replacement = getReplacementText(c, doc, start, len);
		
		replacement = tratarReplacement(replacement);
		
		try {
			RSyntaxTextArea txtArea = (RSyntaxTextArea) textComp;
			int posicaoCursor = txtArea.getCaretPosition();
			int numLinha = txtArea.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtArea.getLineStartOffset(numLinha);
			int cursorFimLinha = txtArea.getLineEndOffset(numLinha);
			

			caret.setDot(cursorInicioLinha);
			// resolvendo caso nao exista nada no txtArea
			if(cursorFimLinha > 0) {
				// na ultima linha ele pega a posicao correta, nas demais pega a posicao +1 = \n
				if(cursorFimLinha != txtArea.getText().length()) {
					cursorFimLinha--;
				}
				caret.moveDot(cursorFimLinha);
			}
			
			textComp.replaceSelection(replacement);
			caret.setDot(posicaoCursor);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String tratarReplacement(String replacement) {
		return replacement;
	}
}
