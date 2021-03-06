package br.com.ivan.missagenerator.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.AutoCompletionEvent;
import org.fife.ui.autocomplete.AutoCompletionEvent.Type;
import org.fife.ui.autocomplete.AutoCompletionListener;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import br.com.ivan.missagenerator.business.Processador;
import br.com.ivan.missagenerator.business.autocomplete.autocompletion.CifraLineAutoCompletion;
import br.com.ivan.missagenerator.business.autocomplete.autocompletion.LetraLineAutoCompletion;
import br.com.ivan.missagenerator.business.autocomplete.autocompletion.ReplaceLineAutoCompletion;
import br.com.ivan.missagenerator.business.autocomplete.autocompletion.SalmoLineAutoCompletion;
import br.com.ivan.missagenerator.business.autocomplete.provider.CifraContainsProvider;
import br.com.ivan.missagenerator.business.autocomplete.provider.IvanContainsProvider;
import br.com.ivan.missagenerator.business.autocomplete.provider.LetraContainsProvider;
import br.com.ivan.missagenerator.business.autocomplete.provider.MomentoContainsProvider;
import br.com.ivan.missagenerator.business.autocomplete.provider.ProcessadorAutocomplete;
import br.com.ivan.missagenerator.business.autocomplete.provider.SalmoContainsProvider;
import br.com.ivan.missagenerator.frame.MenuPrincipal;
import br.com.ivan.missagenerator.frame.Painel;
import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.MusicaDao;
import model.dao.factory.MomentoDaoFactory;
import model.dao.factory.MusicaDaoFactory;

public class MissaFreeFormPlusPanel extends JPanel implements Painel {

	private RSyntaxTextArea txtMissa;
	private JTextArea txtCifra;
	private JTextArea txtApresentacao;
	private AutoCompletion ac;
	private int linhaSelecionada;
	private JRadioButton rdbtnInterno;
	private JRadioButton rdbtnCifra;
	private JRadioButton rdbtnLetra;
	private JRadioButton rdbtnSalmo;
	private boolean adicionouInput = false;
	private String strInterno = "Interno";
	private String strCifra = "Cifra";
	private String strSalmo = "Salmo";
	private String strLetra = "Letra";
	private String strMomento = "Momento";
	private JRadioButton rdbtnMomento;
	private HashMap<String, String[]> cacheUrl;
	private String urlSelecionada;
	private JButton btnAutoorganizar;
	private JButton btnRefrao;
	private JButton btnRefrao_1;

	/**
	 * Create the panel.
	 * 
	 * @param menuPrincipal
	 */
	public MissaFreeFormPlusPanel(MenuPrincipal menuPrincipal) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		add(panel_1, gbc_panel_1);

		rdbtnInterno = new JRadioButton(strInterno);
		rdbtnInterno.setMnemonic(KeyEvent.VK_I);
		rdbtnInterno.setActionCommand(strInterno);
		rdbtnInterno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				interno();
			}
		});
		rdbtnInterno.setSelected(true);
		panel_1.add(rdbtnInterno);

		rdbtnCifra = new JRadioButton(strCifra);
		rdbtnCifra.setMnemonic(KeyEvent.VK_C);
		rdbtnCifra.setActionCommand(strCifra);
		rdbtnCifra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cifra();
			}
		});
		panel_1.add(rdbtnCifra);

		rdbtnLetra = new JRadioButton(strLetra);
		rdbtnLetra.setMnemonic(KeyEvent.VK_L);
		rdbtnLetra.setActionCommand(strLetra);
		rdbtnLetra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letra();
			}
		});
		panel_1.add(rdbtnLetra);

		rdbtnSalmo = new JRadioButton(strSalmo);
		rdbtnSalmo.setMnemonic(KeyEvent.VK_S);
		rdbtnSalmo.setActionCommand(strSalmo);
		rdbtnSalmo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salmo();
			}
		});
		panel_1.add(rdbtnSalmo);

		rdbtnMomento = new JRadioButton(strMomento);
		rdbtnMomento.setMnemonic(KeyEvent.VK_M);
		rdbtnMomento.setActionCommand(strSalmo);
		rdbtnMomento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				momento();
			}
		});
		panel_1.add(rdbtnMomento);

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnInterno);
		group.add(rdbtnCifra);
		group.add(rdbtnLetra);
		group.add(rdbtnSalmo);
		group.add(rdbtnMomento);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.7);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		add(splitPane, gbc_splitPane);

		txtCifra = new RSyntaxTextArea(20, 60);
		txtCifra.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				atualizarCacheCaretUpdate();
			}
		});
		splitPane.setRightComponent(new JScrollPane(txtCifra));

		txtApresentacao = new RSyntaxTextArea(20, 60);
		txtApresentacao.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				atualizarCacheCaretUpdate();
			}
		});
		splitPane.setLeftComponent(new JScrollPane(txtApresentacao));

		txtMissa = new RSyntaxTextArea(20, 60);
		txtMissa.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				selecionarMusicaCaretUpdate();
			}
		});
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 2;
		add(new JScrollPane(txtMissa), gbc_textArea);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.SOUTH;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		add(panel, gbc_panel);

		JButton btnNewButton = new JButton("Gerar PPT");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gerarPPT();
			}
		});
		
		btnAutoorganizar = new JButton("Auto Organizar");
		btnAutoorganizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				autoOrganizar();
			}
		});
		panel.add(btnAutoorganizar);
		
		btnRefrao = new JButton("Refrao +");
		btnRefrao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fazerRefrao(txtApresentacao, true);
			}
		});
		panel.add(btnRefrao);
		
		btnRefrao_1 = new JButton("Refrao -");
		btnRefrao_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fazerRefrao(txtApresentacao, false);
			}
		});
		panel.add(btnRefrao_1);
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Gerar DOCX");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerarDOCX();
			}
		});
		panel.add(btnNewButton_1);

	}

	public static void main2(String[] args) {
		JFrame jf = new JFrame();
		jf.setContentPane(new MissaFreeFormPlusPanel(null));
		jf.setBounds(100, 100, 1200, 700);
		jf.setVisible(true);
	}

	@Override
	public void refreshValues() throws Exception {
		cacheUrl = new HashMap<>();
		carregarMissaSalva();
		carregarMusicas();
		makeInput();
		txtMissa.requestFocus();
		setCursorInicioLinha();
	}

	private void makeInput() {

		if (!adicionouInput) {

			txtMissa.setCodeFoldingEnabled(false);
			txtMissa.getInputMap().put(KeyStroke.getKeyStroke("control shift S"), MissaFreeFormPlus.PESQUISA_SALMO);
			txtMissa.getActionMap().put(MissaFreeFormPlus.PESQUISA_SALMO, new AbstractAction() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					pesquisaSalmo();
				}
			});

			adicionouInput = true;

		}
	}

	private void pesquisaSalmo() {
		salmo();
	}

	private void carregarMusicas() throws Exception {
		Collection<Momento> momentos = null;
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		momentos = momentoDao.listar();

		DefaultCompletionProvider provider = new IvanContainsProvider();
		for (Momento m : momentos) {
			List<Musica> musicas = momentoDao.listarMusicas(m);
			for (Musica musica : musicas) {
				String textoMusica = madeMusicLine(m.getId().toString(), musica.getId().toString(), m.getNome(),
						musica.getNome(), musica.getLink());
				provider.addCompletion(new ShorthandCompletion(provider,
						m.getNome() + " - " + musica.getNome() + " - " + musica.getApresentacao(), textoMusica));
			}
		}
		ac = new ReplaceLineAutoCompletion(provider);
		ac.install(txtMissa);

	}

	private String madeMusicLine(String idMomento, String idMusica, String nomeMomento, String nomeMusica,
			String linkMusica) {
		return ProcessadorAutocomplete.madeMusicLine(idMomento, idMusica, nomeMomento, nomeMusica, linkMusica);
	}

	private void carregarMissaSalva() {
		try {
			txtMissa.setText(Processador.loadMissa().trim());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a opera��o \"carregarMissaSalva\", detalhes tecnicos: "
							+ e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void gerarPPT() {
		try {
			Collection gerarMissaCollection = gerarMissaCollection();
			if (!gerarMissaCollection.isEmpty()) {
				Processador.gerarPreviewApresentacao(gerarMissaCollection);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a opera��o \"gerarPPT\", detalhes tecnicos: " + e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void gerarDOCX() {
		try {
			Collection gerarMissaCollection = gerarMissaCollection();
			if (!gerarMissaCollection.isEmpty()) {
				Processador.gerarPreviewCifra(gerarMissaCollection);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a opera��o \"gerarDOCX\", detalhes tecnicos: " + e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection gerarMissaCollection() throws NumberFormatException, Exception {
		MusicaDao musicaDao = MusicaDaoFactory.createMusicaDao();
		LinkedList missa = new LinkedList();
		String[] linhas = txtMissa.getText().split("\n");
		for (String linha : linhas) {
			String[] elementos = linha.split(":");
			if (elementos.length <= 1)
				continue;
			// momento
			missa.add(ProcessadorAutocomplete.getLinhaNomeMomento(linha));
			// m�sica
			long idMusica = ProcessadorAutocomplete.getLinhaIdMusicaLong(linha);
			if (idMusica > 0) {
				missa.add(musicaDao.listar(Long.parseLong(elementos[1].trim())));
			} else {
				String url = ProcessadorAutocomplete.getLinhaLinkMusica(linha);
				String[] cifra0eApresentacao1Nome2 = cacheUrl.get(url);
				missa.add(new Musica(cifra0eApresentacao1Nome2, url));
			}
		}
		return missa;
	}

	private void adicionaBarraNAdicional() {
		if (!txtMissa.getText().endsWith("\n\n")) {
			Runnable doHighlight = new Runnable() {
				@Override
				public void run() {
					int caretPosition = txtMissa.getCaretPosition();
					txtMissa.setCaretPosition(txtMissa.getText().length());
					txtMissa.replaceSelection("\n\n");
					txtMissa.setCaretPosition(caretPosition);
				}
			};
			SwingUtilities.invokeLater(doHighlight);
		}
	}

	private void selecionarMusicaCaretUpdate() {
//		adicionaBarraNAdicional();
		try {
			urlSelecionada = null;
			salvarMissa(true);
			MusicaDao musicaDao = MusicaDaoFactory.createMusicaDao();
			String linha = ProcessadorAutocomplete.getConteudoLinhaSelecionada(txtMissa);
			long idMusica = Long.parseLong(ProcessadorAutocomplete.getLinhaIdMusica(linha));
			if (idMusica > 0) {
				Musica musica = musicaDao.listar(idMusica);
				selecionarMusica(musica);
			} else {
				urlSelecionada = ProcessadorAutocomplete.getLinhaLinkMusica(linha);
				selecionarLink();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("OK m�sica n�o mapeada nessa linha");
		} catch (Exception e) {
//			throw new RuntimeException(e);
			e.printStackTrace();
			System.out.println("Erro update caret, msg: " + e.getLocalizedMessage());
			System.out.println("Erro update caret, msg: " + e.getMessage());
			System.out.println(e);
		}
	}

	private void selecionarMusica(Musica musica) {
		String apresentacao = musica.getApresentacao();
		txtApresentacao.setText(apresentacao);
		txtApresentacao.setCaretPosition(0);
		txtCifra.setText(musica.getCifra());
		txtCifra.setCaretPosition(0);
		txtMissa.requestFocus();
	}

	private void selecionarLink() {
		String[] cifra0eApresentacao1Nome2 = cacheUrl.get(urlSelecionada);
		if (cifra0eApresentacao1Nome2 == null) {
			cifra0eApresentacao1Nome2 = Processador.getCifra0EApresentacao1Nome2(urlSelecionada);
			cacheUrl.put(urlSelecionada, cifra0eApresentacao1Nome2);
		}
		txtApresentacao.setText(cifra0eApresentacao1Nome2[1]);
		txtApresentacao.setCaretPosition(0);
		txtCifra.setText(cifra0eApresentacao1Nome2[0]);
		txtCifra.setCaretPosition(0);
		txtMissa.requestFocus();
	}

	private void atualizarCacheCaretUpdate() {
		if (urlSelecionada != null) {
			cacheUrl.put(urlSelecionada, new String[] { txtCifra.getText(), txtApresentacao.getText(), "" });
		}
	}

	private void salvarMissa(boolean isCaretUpdate) {
		try {
			Processador.salvarMissa(txtMissa.getText());
			if (!isCaretUpdate) {
				JOptionPane.showMessageDialog(this, "A Missa foi salva com sucesso!", "Informa��o",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a opera��o \"salvarMissa\", detalhes tecnicos: "
							+ e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void interno() {
		try {
			carregarMusicas();
			txtMissa.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
			new RuntimeException(e);
		}
	}

	private void cifra() {
		DefaultCompletionProvider provider = new CifraContainsProvider();

		ac = new CifraLineAutoCompletion(provider);
//		ac.addAutoCompletionListener(new AutoCompletionListener() {
//
//			@Override
//			public void autoCompleteUpdate(AutoCompletionEvent e) {
//				if (e.getEventType().equals(Type.POPUP_SHOWN)) {
//				}
//
//				if (e.getEventType().equals(Type.POPUP_HIDDEN)) {
//
//				}
//			}
//		});

		ac.install(txtMissa);
		txtMissa.requestFocus();

	}

	private void letra() {
		DefaultCompletionProvider provider = new LetraContainsProvider();

		ac = new LetraLineAutoCompletion(provider, txtApresentacao);
		ac.addAutoCompletionListener(new AutoCompletionListener() {

			@Override
			public void autoCompleteUpdate(AutoCompletionEvent e) {
				if (e.getEventType().equals(Type.POPUP_SHOWN)) {

					String linha = ProcessadorAutocomplete.getConteudoLinhaSelecionada(txtMissa);
					int start = ProcessadorAutocomplete.getPosicaoInicioDaLinha(txtMissa);
					int fim = ProcessadorAutocomplete.ordinalIndexOf(linha, ":", 4) + start;
					int inicio = ProcessadorAutocomplete.ordinalIndexOf(linha, ":", 3);
					inicio = ProcessadorAutocomplete.getIndexOfNonWhitespaceAfterWhitespace(linha.substring(inicio))
							+ inicio + start;

					txtMissa.setCaretPosition(inicio);
					txtMissa.moveCaretPosition(fim);

				}

				if (e.getEventType().equals(Type.POPUP_HIDDEN)) {

				}
			}
		});

		ac.install(txtMissa);
		txtMissa.requestFocus();
	}

	private void salmo() {

		List<String> salmoLinks;
		try {
			salmoLinks = Processador.getSalmoLinks();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		DefaultCompletionProvider provider = new SalmoContainsProvider();

		for (String linkSalmo : salmoLinks) {
			provider.addCompletion(new ShorthandCompletion(provider, linkSalmo, linkSalmo));
		}

		ac = new SalmoLineAutoCompletion(provider);
		ac.install(txtMissa);
		ac.addAutoCompletionListener(new AutoCompletionListener() {

			@Override
			public void autoCompleteUpdate(AutoCompletionEvent e) {
				if (e.getEventType().equals(Type.POPUP_SHOWN)) {
					// setCursorInicioLinha();
				}

				if (e.getEventType().equals(Type.POPUP_HIDDEN)) {
					// buscarSalmo();
				}
			}
		});
		txtMissa.requestFocus();

	}

	private void momento() {

		try {
			MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
			List<String> retorno = new ArrayList<>();
			Collection<Momento> momentos = momentoDao.listar();

			DefaultCompletionProvider provider = new MomentoContainsProvider();

			for (Momento momento : momentos) {
				provider.addCompletion(new ShorthandCompletion(provider, momento.getId() + ":" + momento.getNome(),
						momento.getId() + ":" + momento.getNome()));
			}

			ac = new ReplaceLineAutoCompletion(provider);
			ac.install(txtMissa);
			ac.addAutoCompletionListener(new AutoCompletionListener() {

				@Override
				public void autoCompleteUpdate(AutoCompletionEvent e) {
					if (e.getEventType().equals(Type.POPUP_SHOWN)) {
						// setCursorInicioLinha();
					}

					if (e.getEventType().equals(Type.POPUP_HIDDEN)) {
						// do nothing
					}
				}
			});
			txtMissa.requestFocus();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void setCursorInicioLinha() {
		try {
			int posicaoCursor = txtMissa.getCaretPosition();
			int numLinha = txtMissa.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtMissa.getLineStartOffset(numLinha);
			txtMissa.setCaretPosition(cursorInicioLinha);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
	}

	private void buscarSalmo() {
	}

	private void adicionarLinha(Long idMomento, Long idMusica, String nomeMomento, String nomeMusica,
			String linkMusica) {
		try {
			int posicaoCursor = txtMissa.getCaretPosition();
			int numLinha = txtMissa.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtMissa.getLineStartOffset(numLinha);
			int cursorFimLinha = txtMissa.getLineEndOffset(numLinha);

			String texto = txtMissa.getText();
			txtMissa.setText(texto.substring(0, cursorInicioLinha) + // linhas anteriores
					madeMusicLine(idMomento.toString(), idMusica.toString(), nomeMomento, nomeMusica, linkMusica) + // "\n"
																													// +
																													// //
																													// nova
																													// linha
					texto.substring(cursorFimLinha));
			txtMissa.setCaretPosition(cursorInicioLinha);
			txtMissa.requestFocus();
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a opera��o \"adicionarMusica\", detalhes tecnicos: "
							+ e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
		}
	}
	


	private String algoritimoAutoOrganizar(String musica) {
		int maxlinesize = 100;
		String linhaFinal = "";
		String linhaTemporaria = "";

		musica = musica.replaceAll("\r", "");

		String[] blocos = musica.split("\n\n");
		for (String bloco : blocos) {
			boolean finalizouUmaLinha = false;
			String[] linhas = bloco.split("\n");
			for (String linha : linhas) {

				if ((linhaTemporaria + linha).length() < maxlinesize) {
					linhaTemporaria = removerPontuacaoDepois(linhaTemporaria)
							+ (linhaTemporaria.equals("") ? "" : " / ")
							+ Processador.primeiraMaiuscula(removerPontuacaoAntes(linha));
				} else {
					finalizouUmaLinha = true;
					linhaFinal += "\n" + linhaTemporaria;
					linhaTemporaria = linha;
				}
			}

			if (finalizouUmaLinha && linhaTemporaria.length() < (maxlinesize / 2)) {
				linhaFinal = removerPontuacaoDepois(linhaFinal) + (linhaFinal.equals("") ? "" : " / ")
						+ Processador.primeiraMaiuscula(removerPontuacaoAntes(linhaTemporaria));
			} else {
				linhaFinal += "\n" + linhaTemporaria;

			}
			linhaTemporaria = "";
			linhaFinal += "\n";
		}

		return linhaFinal.trim();

	}

	private String removerPontuacaoDepois(String textoDepoisLinha) {
		if (textoDepoisLinha.equals("")) {
			return "";
		}
		char firstChar = textoDepoisLinha.charAt(0);
		while (!Character.isLetter(firstChar)) {
			textoDepoisLinha = textoDepoisLinha.substring(1);
			firstChar = textoDepoisLinha.charAt(0);
		}
		return textoDepoisLinha;

	}

	private String removerPontuacaoAntes(String textoAntesLinha) {
		if (textoAntesLinha.equals("")) {
			return textoAntesLinha;
		}
		char lastChar = textoAntesLinha.charAt(textoAntesLinha.length() - 1);
		while (!Character.isLetter(lastChar)) {
			textoAntesLinha = textoAntesLinha.substring(0, textoAntesLinha.length() - 1);
			lastChar = textoAntesLinha.charAt(textoAntesLinha.length() - 1);
		}
		return textoAntesLinha;
	}

	private void autoOrganizar() {
		txtApresentacao.setText(algoritimoAutoOrganizar(txtApresentacao.getText()));
	}
	
	private void fazerRefrao(JTextArea jtx, boolean adicionarRefrao) {
		try {
			int posicaoCursor = jtx.getCaretPosition();
			int numLinha = jtx.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = jtx.getLineStartOffset(numLinha);
			int cursorFimLinha = jtx.getLineEndOffset(numLinha);
			int lengthLinha = cursorFimLinha - cursorInicioLinha;

			String texto = jtx.getText();
			// ultima linha

			int posicaoInicioSelecao = jtx.getSelectionStart();
			int posicaoFimSelecao = jtx.getSelectionEnd();
			int linhaInicio = jtx.getLineOfOffset(posicaoInicioSelecao);
			int linhaFim = jtx.getLineOfOffset(posicaoFimSelecao);

			for (int i = linhaInicio; i <= linhaFim; i++) {
				if (adicionarRefrao) {
					texto = adicionarRefrao(texto, jtx.getLineStartOffset(i));
				} else {
					texto = removerRefrao(texto, jtx.getLineStartOffset(i));
				}
				jtx.setText(texto);
			}
			if (adicionarRefrao) {
				texto = adicionarQuebra(texto, jtx.getLineStartOffset(linhaInicio), jtx.getLineEndOffset(linhaFim),
						adicionarRefrao);
				jtx.setText(texto);
			}

			jtx.setCaretPosition(jtx.getLineStartOffset(linhaInicio));
			jtx.requestFocus();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private String adicionarRefrao(String texto, int posicaoStart) {
		if (texto.charAt(posicaoStart) == '$') {
			return texto;
		} else {
			return texto.substring(0, posicaoStart) + "$"
					+ Processador.primeiraMaiuscula(texto.substring(posicaoStart));
		}
	}

	private String removerRefrao(String texto, int posicaoStart) {
		if (texto.charAt(posicaoStart) == '$') {
			return texto.substring(0, posicaoStart) + texto.substring(posicaoStart + 1);
		} else {
			return texto;
		}
	}

	private String adicionarQuebra(String texto, int offsetInicio, int offsetFim, boolean adicionarRefrao) {
		String quebra = adicionarRefrao ? "\n\n" : "\n";
		String texto1 = texto.substring(0, offsetInicio).trim();
		String texto2 = texto.substring(offsetInicio, offsetFim).trim();
		texto2 = texto2.replaceAll("\n\n", "\n");
		String texto3 = texto.substring(offsetFim).trim();
		if (offsetInicio != 0) {
			texto1 += quebra;
		}
		return texto1 + texto2 + quebra + texto3;
	}

}
