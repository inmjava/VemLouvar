package br.com.ivan.missagenerator.business.provider;


import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class ReplaceLineAutoCompletion extends AutoCompletion{

	public ReplaceLineAutoCompletion(CompletionProvider provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}

	protected void insertCompletion(Completion c,
			boolean typedParamListStartChar) {

		JTextComponent textComp = getTextComponent();
		String alreadyEntered = c.getAlreadyEntered(textComp);
		hidePopupWindow();
		Caret caret = textComp.getCaret();

		int dot = caret.getDot();
		int len = alreadyEntered.length();
		int start = dot - len;
		Document doc = textComp.getDocument();
		String replacement = getReplacementText(c, doc,
				start, len);
		replacement = "abrobra!";
		
//		Element root = doc.getDefaultRootElement();
//		int index = root.getElementIndex(dot);
//		Element elem = root.getElement(index);
//		
//		start = elem.getStartOffset();
//		int end = elem.getEndOffset();
		
		
		try {
			RSyntaxTextArea txtArea = (RSyntaxTextArea) textComp;
			int posicaoCursor = txtArea.getCaretPosition();
			int numLinha = txtArea.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtArea.getLineStartOffset(numLinha);
			int cursorFimLinha = txtArea.getLineEndOffset(numLinha);
			
			
			caret.setDot(cursorInicioLinha);
			caret.moveDot(cursorFimLinha);
			textComp.replaceSelection(replacement);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
