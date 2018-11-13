package br.com.ivan.missagenerator.business;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;

public class IvanContainsProvider extends DefaultCompletionProvider{

	@Override
	protected List<Completion> getCompletionsImpl(JTextComponent comp) {
		List<Completion> retVal = new ArrayList<Completion>();
		String text = getAlreadyEnteredText(comp);

		if (text!=null) {

			int index = 0;

			while (index<completions.size()) {
				Completion c = completions.get(index);
				
				String str1 = Normalizer.normalize(c.getInputText(), Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				str1 = StringUtils.stripAccents(str1).toUpperCase();
				String str2 = Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				str2 = StringUtils.stripAccents(str2).toUpperCase();
			    
				if (Stream.of(str2.split(" ")).allMatch(str1::contains)) {
					String finalInputText = "<html>"+c.getInputText().replaceAll("(?i)"+text, "<b>$0</b>")+"</html>";
					retVal.add(new ShorthandCompletion(c.getProvider(), finalInputText, c.getReplacementText()));
				}
				index++;
			}

		}
		return retVal;
	}
	
	
	@Override
	public String getAlreadyEnteredText(JTextComponent comp) {
		Document doc = comp.getDocument();

		int dot = comp.getCaretPosition();
		Element root = doc.getDefaultRootElement();
		int index = root.getElementIndex(dot);
		Element elem = root.getElement(index);
		int start = elem.getStartOffset();
		int len = dot-start;
		try {
			doc.getText(start, len, seg);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			return EMPTY_STRING;
		}
		return seg.toString();
	}
	
	public static void main(String[] args) {
		
		String str1 = "proclamam céu TERRA".toUpperCase();
		String str2 = "Santo - Santo, santo, santo - Santo, santo, santo (bis) / Senhor Deus do Universo (bis) / O céu e a terra proclamam a vossa gloria (bis)".toUpperCase();
		
		if (Stream.of(str1.split(" ")).allMatch(str2::contains)) {
			System.out.println("ok");
		} else {
			System.out.println("nok");
		}
	}
	
}
