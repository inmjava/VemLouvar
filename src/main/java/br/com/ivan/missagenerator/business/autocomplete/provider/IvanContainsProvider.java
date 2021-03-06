package br.com.ivan.missagenerator.business.autocomplete.provider;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
				
				String retorno = marcarPosicoes(str2, str1);
				if (retorno != null) {
					String finalInputText = "<html>"+retorno+"</html>";
					retVal.add(new ShorthandCompletion(c.getProvider(), finalInputText, c.getReplacementText()));
				}
				index++;
			}

		}
		return retVal;
	}
	
	
	@Override
	public String getAlreadyEnteredText(JTextComponent comp) {
		return ProcessadorAutocomplete.obterConteudoLinhaProvider(comp);
	}
	

	public static String marcarPosicoes(String str1, String str2) {
		String str2Upp = StringUtils.stripAccents(str2.toUpperCase());
		String str1Upp = StringUtils.stripAccents(str1.toUpperCase());
		
		if (Stream.of(str1Upp.split(" ")).allMatch(str2Upp::contains)) {
			String[] words = str1Upp.split(" ");
			String str2Stripado = StringUtils.stripAccents(str2);
			for (String word : words) {
				str2Stripado = str2Stripado.replaceAll("(?i)"+word, "<b>$0</b>");
			}
//			int i = str2Stripado.indexOf("<b>");
//			int j = str2Stripado.indexOf("</b>");
//			while (i > 0) {
//				str2 = str2.substring(0, i) + "<b>" + str2.substring(i);
//				str2 = str2.substring(0, j) + "</b>" + str2.substring(j);
//				i = str2Stripado.indexOf("<b>", i + 1);
//				j = str2Stripado.indexOf("</b>", j + 1);
//			}
			return str2Stripado;
		} else {
			return null;
		}
	}
	
}
