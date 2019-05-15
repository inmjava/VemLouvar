package br.com.ivan.missagenerator.business.provider;

import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

public class SalmoContainsProvider extends DefaultCompletionProvider {

	@Override
	protected List<Completion> getCompletionsImpl(JTextComponent comp) {
		return completions;
	}

	@Override
	public String getAlreadyEnteredText(JTextComponent comp) {
		Document doc = comp.getDocument();

		int dot = comp.getCaretPosition();
		Element root = doc.getDefaultRootElement();
		int index = root.getElementIndex(dot);
		Element elem = root.getElement(index);
		int start = elem.getStartOffset();

		int end = elem.getEndOffset() - 1;
		if (dot < end) {
			dot = end;
			comp.setCaretPosition(dot);
		}

		int len = dot - start;

		try {
			doc.getText(start, len, seg);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			return EMPTY_STRING;
		}
		return seg.toString();
	}
}
