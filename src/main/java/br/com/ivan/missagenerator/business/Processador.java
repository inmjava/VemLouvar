package br.com.ivan.missagenerator.business;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Segment;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.factory.MomentoDaoFactory;

public class Processador {

	private static JSONArray jsonArray;
	
	public static void main(String[] args) throws IOException {
		Processador.getSalmoLinks();
	}
	
	public static List<String> getSalmoLinks() throws IOException{
		String url = "https://liturgia.cancaonova.com/pb/";
		Document jsoupDoc = Jsoup.connect(url).userAgent("Mozilla").get();

		Elements elements = jsoupDoc.select("#wp-calendar tbody tr td a");
		ArrayList<String> links = new ArrayList<>();
		for (Element e : elements) {
			String link = e.attr("href");
//			System.out.println(link);
			links.add(link);
		}
		return links;
	}
	
	public static void main5(String[] args) throws IOException {
		String url = "https://www.letras.mus.br/cancao-nova/169358/";
		Document jsoupDoc = Jsoup.connect(url).userAgent("Mozilla").get();
		String musicaTexto = "";
		for (Element paragrafo : jsoupDoc.select(".cnt-letra p")) {
			for (String linha : paragrafo.html().split("<br />")) {
				musicaTexto += paragrafo.html(linha).text() + "\n";
			}
			musicaTexto += "\n";
		}
		System.out.println(musicaTexto);
	}
	
	public static void main4(String[] args) throws FileNotFoundException, IOException {
		String searchMusic = "$eu navegarei";
		
		System.out.println(searchMusic.startsWith("$"));
		
		ArrayList<String> returnArr = new ArrayList<String>();
		Document jsoupDoc = Jsoup.connect("http://www.bing.com/search?q=site%3Awww.letras.mus.br+" + searchMusic).get();
		Elements links = jsoupDoc.select("a[href]");
		for (Element link : links) {
			String strLink = link.attr("abs:href");
			if(strLink.startsWith("https://www.letras.mus.br")){
				System.out.println(strLink);
				returnArr.add(strLink);
			}
		}
	}
	
	public static void main3(String[] args) throws FileNotFoundException, IOException {
		Document jsoupDoc = Jsoup.connect("http://liturgia.cancaonova.com/").get();
		Elements links = jsoupDoc.select("a[href]");
		for (Element link : links) {
			String strLink = link.attr("abs:href");
			if(strLink.startsWith("http://liturgia.cancaonova.com/liturgia/")){
				System.out.println(strLink);
			}
		}
	}

	public static void main2(String[] args) throws IOException {
		String link = "http://cifraclub.com/comunidade-shalom/estar-em-tuas-maos/bla.html";
		link = ((link.contains(".html") ? link.substring(0,link.lastIndexOf("/")) : link) + "/imprimir.html").replaceAll("//", "/");
		System.out.println(link);
	}

	public static String getUrlMusica(String nomePesquisa, int index) throws IOException {
		if (jsonArray == null || index == 0) {
			nomePesquisa = URLEncoder.encode(nomePesquisa, "UTF-8");
			Document jsoupDoc = Jsoup.connect("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=site:cifraclub.com+" + nomePesquisa).get();
			String text = jsoupDoc.select("body").text();
			jsonArray = new JSONObject(text).getJSONObject("responseData").getJSONArray("results");
		}
		JSONObject jsonObject = jsonArray.getJSONObject(index % jsonArray.length());
		return jsonObject.getString("url");
	}

	public static String primeiraMaiuscula(String texto) {
		texto = texto.trim();
		if (texto.equals("")) {
			return texto;
		}
		return texto.substring(0, 1).toUpperCase() + texto.substring(1);
	}
	
	
	public static ArrayList<String> pesquisarLinks(String searchMusic) throws IOException{
		ArrayList<String> returnArr = new ArrayList<String>();
		Document jsoupDoc = Jsoup.connect("http://www.bing.com/search?q=site%3Awww.cifraclub.com.br+imprimir+" + searchMusic).get();
		Elements links = jsoupDoc.select("a[href]");
		for (Element link : links) {
			String strLink = link.attr("abs:href");
			if(strLink.startsWith("https://www.cifraclub.com.br")){
				returnArr.add(strLink);
			}
		}
		return returnArr;
	}
	
	
	public static ArrayList<String> pesquisarLinksLetras(String searchMusic) throws IOException{
		ArrayList<String> returnArr = new ArrayList<String>();
		Document jsoupDoc = Jsoup.connect("http://www.bing.com/search?q=site%3Awww.letras.mus.br+" + searchMusic).get();
		Elements links = jsoupDoc.select("a[href]");
		for (Element link : links) {
			String strLink = link.attr("abs:href");
			if(strLink.startsWith("https://www.letras.mus.br")){
				returnArr.add(strLink);
			}
		}
		return returnArr;
	}

	public static ArrayList<String> pesquisarSalmo() throws IOException {
		ArrayList<String> returnArr = new ArrayList<String>();
		Document jsoupDoc = Jsoup.connect("http://liturgia.cancaonova.com/").get();
		Elements links = jsoupDoc.select("a[href]");
		for (Element link : links) {
			String strLink = link.attr("abs:href");
			if(strLink.startsWith("http://liturgia.cancaonova.com/liturgia/")){
				returnArr.add(strLink);
			}
		}
		return returnArr;
	}
	

	public static String[] getCifra0EApresentacao1Nome2(String url) throws IOException {
		Document jsoupDoc = Jsoup.connect(url).userAgent("Mozilla").get();
		String musicaApresentacao = "";
		String musicaCifra = "";
		String nome = "";
		if (url.contains("cancaonova")) {
			for (Iterator<Element> iterator2 = jsoupDoc.select(".tab-pane#liturgia-2 p").iterator(); iterator2
					.hasNext();) {
				Element type = (Element) iterator2.next();
				musicaCifra += type.text() + "\n";
			}
			musicaApresentacao = jsoupDoc.select(".tab-pane#liturgia-2 p").get(1).text();
		} else if (url.contains("https://www.letras.mus.br")){
			String musicaTexto = "";
			for (Element paragrafo : jsoupDoc.select(".cnt-letra p")) {
				for (String linha : paragrafo.html().split("<br />")) {
					musicaTexto += paragrafo.html(linha).text() + "\n";
				}
				musicaTexto += "\n";
				musicaApresentacao = musicaTexto;
			}
			return new String[] { "", musicaApresentacao, "" };
		} else {
			musicaApresentacao = jsoupDoc.select("pre").text();
			musicaCifra = jsoupDoc.select("pre").text();
			nome = jsoupDoc.select("h1").text();
		}

		musicaCifra = musicaCifra.replaceAll("\n(.*)\n(.*)\\|-(.*)(\n|\\z)", "");
		musicaCifra = musicaCifra.replaceAll("\n(.*)\n(.*)\\|-(.*)(\n|\\z)", "");
		
		String[] splitData = musicaApresentacao.split("\n");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < splitData.length; i++) {
			String eachSplit = splitData[i];
			if ((eachSplit.matches("(.*)(\\w{3,}+)(.*)(\\w{3,}+)(.*)") || eachSplit.equals(""))
					&& !eachSplit.matches("(.*)(\\#)(.*)") && !eachSplit.matches("(.*)(7)(.*)")
					&& !eachSplit.matches("(.*)(9)(.*)")) {
				sb.append(eachSplit + "\n");
			}
		}
		String arquivoCantosPpt = "\n" + sb.toString() + "\n";
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("(\\s)(\\s)+", "$1");
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("(\\.)(\\.)+", "");
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("^(\\s)", "");
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("aa+", "a");
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("ee+", "e");
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("ii+", "i");
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("oo+", "o");
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("uu+", "u");
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("jesus", "Jesus");
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("senhor", "Senhor");
		arquivoCantosPpt = arquivoCantosPpt.replaceAll("deus", "Deus");

		return new String[] { musicaCifra, arquivoCantosPpt, nome };
	}

	public static void gerarPreviewApresentacao(Collection missa) throws FileNotFoundException, IOException {
		// create a new empty slide show
		SlideShow ppt = new SlideShow(new FileInputStream("template/template.ppt"));

		for (Iterator iterator = missa.iterator(); iterator.hasNext();) {
			Object itemMissa = (Object) iterator.next();
			if (itemMissa instanceof String) {
				createTitleMomento(ppt, (String) itemMissa);
			} else if (itemMissa instanceof Musica) {
				adicionarMusica(ppt, itemMissa);
				// adicionando slide em branco...
				if (iterator.hasNext()) {
					createTitleMomento(ppt, "");
				}
			}

		}

		File tempFile = saveppt(ppt);
		System.out.println(tempFile.getAbsolutePath());
		Desktop.getDesktop().open(tempFile);
	}

	public static void gerarPreviewCifra(Collection missa) throws FileNotFoundException, IOException {
		// create a new empty slide show
		XWPFDocument doc = new XWPFDocument(new FileInputStream("template/template.docx"));
		doc.removeBodyElement(0);

		for (Iterator iterator = missa.iterator(); iterator.hasNext();) {
			Object itemMissa = (Object) iterator.next();
			if (itemMissa instanceof String) {
				createMomentoDocx(doc, (String) itemMissa);
			} else if (itemMissa instanceof Musica) {
				createCifraDocx(doc, ((Musica) itemMissa).getCifra());
			}

		}
		File tempFile = savedocx(doc);
		System.out.println(tempFile.getAbsolutePath());
		Desktop.getDesktop().open(tempFile);
	}

	public static void gerarPreviewApresentacao(Musica m) throws FileNotFoundException, IOException {
		gerarPreviewApresentacao(Arrays.asList(new Musica[] { m }));
	}

	public static void gerarPreviewApresentacaoTxt(String text) throws FileNotFoundException, IOException {
		Musica m = new Musica();
		m.setApresentacao(text);
		gerarPreviewApresentacao(m);
	}

	private static void adicionarMusica(SlideShow ppt, Object itemMissa) {
		Musica musica = (Musica) itemMissa;
		String text = musica.getApresentacao();
		String[] linhas = text.split("\n");
		for (int i = 0; i < linhas.length; i++) {
			String linha = linhas[i];
			if (!linha.trim().equals("")) {
				createTitle(ppt, linha);
			}
		}
	}

	private static File saveppt(SlideShow ppt) throws FileNotFoundException, IOException {
		String fileName = "Missa-" + getTodayTemplateFileNameTemplate();
		String fileType = ".ppt";
		File outputFile = selectFile(null, fileName, fileType);
		boolean fileSelected = (outputFile != null);

		if (!fileSelected) {
			outputFile = File.createTempFile(fileName, fileType);
		}

		FileOutputStream out;
		try {
			out = new FileOutputStream(outputFile);
			ppt.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Por favor feche o arquivo antes de salva-lo", "ERRO",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (!fileSelected) {
			outputFile.deleteOnExit();
		}
		return outputFile;
	}

	private static File savedocx(XWPFDocument doc) throws FileNotFoundException, IOException {
		String fileName = "Missa-" + getTodayTemplateFileNameTemplate();
		String fileType = ".docx";
		File outputFile = selectFile(null, fileName, fileType);
		boolean fileSelected = (outputFile != null);

		if (!fileSelected) {
			outputFile = File.createTempFile(fileName, fileType);
		}

		FileOutputStream out;
		try {
			out = new FileOutputStream(outputFile);
			doc.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Por favor feche o arquivo antes de salva-lo", "ERRO",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (!fileSelected) {
			outputFile.deleteOnExit();
		}
		return outputFile;
	}

	public static void exportar(Collection missa) throws IOException {
		String saida = "";
		for (Iterator iterator = missa.iterator(); iterator.hasNext();) {
			Object itemMissa = (Object) iterator.next();
			if (itemMissa instanceof String) {
				saida += itemMissa + ";";
			} else if (itemMissa instanceof Musica) {
				Musica musica = (Musica) itemMissa;
				saida += musica.getNome() + ";" + musica.getId() + "\r\n";
			}
		}

		String fileName = "Missa-" + getTodayTemplateFileNameTemplate();
		String fileType = ".txt";
		File outputFile = selectFile(null, fileName, fileType);
		boolean fileSelected = (outputFile != null);

		if (!fileSelected) {
			outputFile = File.createTempFile(fileName, fileType);
		}

		FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(saida);
		bw.close();
		if (!fileSelected) {
			outputFile.deleteOnExit();
		}
		Desktop.getDesktop().open(outputFile.getAbsoluteFile());

	}

	private static void createTitleMomento(SlideShow ppt, String momento) {
		Slide s1 = ppt.createSlide();
		TextBox title = s1.addTitle();
		String strSlide = momento;
		title.setText("\n\n\n" + strSlide);
		title.setHorizontalAlignment(TextBox.AlignCenter);
		title.setVerticalAlignment(TextBox.AnchorMiddleCentered);
		RichTextRun rt = title.getTextRun().getRichTextRuns()[0];
		rt.setFontSize(80);
		rt.setFontColor(new Color(63, 138, 112));
		rt.setBold(true);
	}

	private static void createMomentoDocx(XWPFDocument doc, String momento) {
		XWPFParagraph p1 = doc.createParagraph();
		p1.setStyle("mgmomento");
		XWPFRun r1 = p1.createRun();
		r1.setText(momento);
	}

	private static void createCifraDocx(XWPFDocument doc, String musica) {
		musica = musica.replaceAll("\r\n", "\n");
		String arquivoCantosDocx = "\n" + musica + "\n";

		XWPFParagraph p = doc.createParagraph();
		p.setStyle("mgcifra");
		XWPFRun r2 = p.createRun();
		
		String[] lines = arquivoCantosDocx.split("\n");
		r2.setText(lines[0], 0); // set first line into XWPFRun
        for(int i=1;i<lines.length;i++){
            // add break and insert new text
        	r2.addBreak();
        	r2.setText(lines[i]);
        }
    	r2.addBreak();
	}

	private static void createTitle(SlideShow ppt, String strSlide) {
		Slide s1 = ppt.createSlide();
		TextBox title = s1.addTitle();
		boolean refrao = false;
		if (strSlide.startsWith("$")) {
			strSlide = strSlide.substring(1);
			refrao = true;
		}
		title.setText(strSlide);
		title.setHorizontalAlignment(TextBox.AlignLeft);
		RichTextRun rt = title.getTextRun().getRichTextRuns()[0];
		if (strSlide.length() < 71) {
			rt.setFontSize(72);
		} else if (strSlide.length() < 80) {
			rt.setFontSize(66);
		} else if (strSlide.length() < 103) {
			rt.setFontSize(60);
		} else if (strSlide.length() < 115) {
			rt.setFontSize(54);
		} else if (strSlide.length() < 155) {
			rt.setFontSize(48);
		} else if (strSlide.length() < 193) {
			rt.setFontSize(44);
		} else {
			rt.setFontSize(40);
		}
		rt.setFontName("Arial");
		if (refrao) {
			// rt.setFontColor(Color.red);
			rt.setFontColor(new Color(219, 75, 94));
		}
		// rt.setBold(true);
		// rt.setItalic(true);
		// rt.setUnderlined(true);
		// rt.setAlignment(TextBox.AlignRight);
	}

	private static String getTodayTemplateFileNameTemplate() {
		return new SimpleDateFormat("dd-MMM-YY").format(new Date()).toUpperCase();
	}

	public static File selectFile(Component parent, String fileName, String fileType) {
		// parent component of the dialog
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File(System.getProperty("user.home"), "Desktop/" + fileName + fileType));

		int userSelection = fileChooser.showSaveDialog(parent);
		File fileToSave = null;
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			fileToSave = fileChooser.getSelectedFile();
		}
		return fileToSave;
	}

	public static HashMap<String, Long> importarSelecoes() throws FileNotFoundException {

		Component parent;

		String fileName = "Missa-" + getTodayTemplateFileNameTemplate();
		String fileType = ".txt";

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File(System.getProperty("user.home"), "Desktop/" + fileName + fileType));

		int userSelection = fileChooser.showOpenDialog(null);
		File fileToOpen = null;
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			fileToOpen = fileChooser.getSelectedFile();
		}

		HashMap<String, Long> selecoes = new HashMap<String, Long>();

		Scanner scanner = new Scanner(fileToOpen).useDelimiter(";|\\r\\n");
		while (scanner.hasNext()) {
			String momento = scanner.next();
			scanner.next();
			Long id = Long.parseLong(scanner.next());
			selecoes.put(momento, id);
		}
		return selecoes;
	}

	public static void salvarMissa(String missaFreeForm) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("db/missa-freeform.txt");
		writer.println(missaFreeForm);
		writer.close();
	}

	public static String loadMissa() throws IOException {
		File file = new File("db/missa-freeform.txt");
		Path path = file.toPath();
		if(file.exists()) {
			return new String(Files.readAllBytes(path));
		}
		return "";
	}

	public static String obterConteudoLinhaProvider(JTextComponent comp, String EMPTY_STRING, Segment seg) {
		javax.swing.text.Document doc = comp.getDocument();

		int dot = comp.getCaretPosition();
		javax.swing.text.Element root = doc.getDefaultRootElement();
		int index = root.getElementIndex(dot);
		javax.swing.text.Element elem = root.getElement(index);
		int start = elem.getStartOffset();

		int end = elem.getEndOffset()-1;
		if(dot < end) {
			dot = end;
			comp.setCaretPosition(dot);
		}
		
		int len = dot-start;
		
		try {
			doc.getText(start, len, seg);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			return EMPTY_STRING;
		}
		return seg.toString();
	}
	
	
	public static int ordinalIndexOf(String str, String substr, int n) {
	    int pos = str.indexOf(substr);
	    while (--n > 0 && pos != -1)
	        pos = str.indexOf(substr, pos + 1);
	    return pos;
	}
}
