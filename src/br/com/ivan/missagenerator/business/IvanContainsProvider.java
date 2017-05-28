package br.com.ivan.missagenerator.business;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

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
				String str2 = Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			    
				if (StringUtils.containsIgnoreCase(str1, str2)) {
					String finalInputText = "<html>"+c.getInputText().replaceAll("(?i)"+text, "<b>$0</b>")+"</html>";
					retVal.add(new ShorthandCompletion(c.getProvider(), finalInputText, c.getReplacementText()));
				}
				index++;
			}

		}
		return retVal;
	}
}
