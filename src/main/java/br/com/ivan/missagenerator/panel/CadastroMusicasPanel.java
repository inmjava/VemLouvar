package br.com.ivan.missagenerator.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import br.com.ivan.missagenerator.business.AutoCompleteWindow;
import br.com.ivan.missagenerator.business.Processador;
import br.com.ivan.missagenerator.frame.MenuPrincipal;
import br.com.ivan.missagenerator.frame.Painel;
import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.MusicaDao;
import model.dao.factory.MomentoDaoFactory;
import model.dao.factory.MusicaDaoFactory;

public class CadastroMusicasPanel extends JPanel implements Painel {
	private static final String CONTINUAR_MISSA = "continuarMissa";
	private JTextField txtLinkMusica;
	private JTextField txtNomeMusica;
	private JComboBox<Musica> cboMusicas;
	private JTextArea txtCifraMusica;
	private JTextArea txtApresentacao;
	private JComboBox cboMomentos;
	private JButton btnLigarLinhas;
	private String textoAnterior;
	private JTextField txtFiltro;
	private String strFiltro;
	private int index;
	private String textoPesquisa;
	private MenuPrincipal menuPrincipal;

	public static void main(String[] args) throws IOException {
		String teste = "";
		teste += "Tem muita gente\n";
		teste += "Que não percebe\n";
		teste += "O quanto Deus tem anos dar\n";
		teste += "Não acreditam\n";
		teste += "Em suas preces\n";
		teste += "E estão sempre prontos a falhar\n";
		teste += "\n";
		teste += "Porque você\n";
		teste += "Não tenta ser\n";
		teste += "De maneira que Deus quer\n";
		teste += "Tenha coragem de dar-lhe a vida\n";
		teste += "E tudo, tudo que você tiver\n";
		teste += "\n";
		teste += "Faça um teste\n";
		teste += "E largue o mundo agora\n";
		teste += "Ele vai dar tudo novo pra você\n";
		teste += "Faça um teste\n";
		teste += "E largue o mundo agora\n";
		teste += "Ele vai dar tudo novo pra você\n";
		teste += "Esqueça do mal\n";
		teste += "E quando menos perceber\n";
		teste += "Cristo vai dar tudo novo pra você\n";
		teste += "\n";
		teste += "Saiba que o reino de Jesus Cristo\n";
		teste += "Não é somente pra você\n";
		teste += "Compartilhando de sua vida\n";
		teste += "Você logo vai saber\n";
		teste += "\n";
		teste += "Cristo vai dar tudo\n";
		teste += "Ele vai dar tudo\n";
		teste += "Tudo novo pra você\n";

		teste = Processador.getCifra0EApresentacao1Nome2("https://www.letras.mus.br/maria-do-rosario/1229878/")[1];

		System.out.println(teste);
		System.out.println("============================");

		teste = new CadastroMusicasPanel(null).algoritimoAutoOrganizar(teste);

		System.out.println(teste);

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

	public void refreshValues() throws Exception {
		carregarMomentos();
	}

	public void softRefresh() {
		selecionarMomento();
	}

	private void carregarMomentos() throws Exception {
		cboMomentos.removeAllItems();
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		for (Momento m : momentoDao.listar()) {
			cboMomentos.addItem(m);
		}
	}

	private void autoOrganizar() {
		txtApresentacao.setText(algoritimoAutoOrganizar(txtApresentacao.getText()));
	}

	private void ligarLinhas(JTextArea jtx) {
		try {
			int posicaoCursor = jtx.getCaretPosition();
			int numLinha = jtx.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = jtx.getLineStartOffset(numLinha);
			int cursorFimLinha = jtx.getLineEndOffset(numLinha);
			int lengthLinha = cursorFimLinha - cursorInicioLinha;
			textoAnterior = jtx.getText();
			// ultima linha
			if (cursorFimLinha != textoAnterior.length()) {
				String textoAntesLinha = textoAnterior.substring(0, cursorFimLinha - 1).trim();
				textoAntesLinha = removerPontuacaoAntes(textoAntesLinha);
				String textoDepoisLinha = textoAnterior.substring(cursorFimLinha).trim();
				textoDepoisLinha = removerPontuacaoDepois(textoDepoisLinha);
				jtx.setText(Processador.primeiraMaiuscula(textoAntesLinha) + " / "
						+ Processador.primeiraMaiuscula(textoDepoisLinha));
				jtx.setCaretPosition(jtx.getLineStartOffset(numLinha + 1));
			} else {
				jtx.setCaretPosition(cursorInicioLinha);
			}
			jtx.requestFocus();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void desfazer(JTextArea jtx) {
		if (textoAnterior == null) {
			return;
		}
		try {
			int posicaoCursor = jtx.getCaretPosition();
			int numLinha = jtx.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = jtx.getLineStartOffset(numLinha);
			int cursorFimLinha = jtx.getLineEndOffset(numLinha);
			int lengthLinha = cursorFimLinha - cursorInicioLinha;
			jtx.setText(textoAnterior);
			jtx.setCaretPosition(cursorInicioLinha);
			jtx.requestFocus();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
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

	private void removerBarras(JTextArea jtx) {
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
			int posicaoInicio = jtx.getLineStartOffset(jtx.getLineOfOffset(posicaoInicioSelecao));
			int posicaoFim = jtx.getLineEndOffset(jtx.getLineOfOffset(posicaoFimSelecao));

			texto = removerBarras(texto, posicaoInicio, posicaoFim);
			jtx.setText(texto);

			jtx.setCaretPosition(posicaoInicio);
			jtx.requestFocus();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private String removerBarras(String texto, int posicaoInicio, int posicaoFim) {
		return texto.substring(0, posicaoInicio) + texto.substring(posicaoInicio, posicaoFim).replaceAll(" / ", "\n")
				+ texto.substring(posicaoFim);
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

	private void filtrar() throws Exception {
		strFiltro = txtFiltro.getText();
		carregarMomentos(strFiltro);
	}

	private void carregarMomentos(String filtro) throws Exception {
		cboMomentos.removeAllItems();
		Collection<Momento> momentos = null;
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		if (filtro == null || filtro.equals("")) {
			momentos = momentoDao.listar();
		} else {
			momentos = momentoDao.listarMomentosPorFiltroMusica(filtro);
		}
		for (Momento m : momentos) {
			cboMomentos.addItem(m);
		}
	}

	public void criarNovaMusicaExterno(Momento m) {
		cboMomentos.setSelectedItem(m);
		novaMusica();
		txtLinkMusica.requestFocus();
	}

	private void selecionarMomento() {
		selecionarMomento(null);
	}

	private void selecionarMomento(Musica m) {
		cboMusicas.removeAllItems();
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		try {
			Momento selectedItem = (Momento) cboMomentos.getSelectedItem();
			if (selectedItem == null) {
				return;
			}
			List<Musica> musicas = momentoDao.listarMusicas(selectedItem, strFiltro);
			for (Musica musica : musicas) {
				cboMusicas.addItem(musica);
			}
			if (m != null) {
				cboMusicas.setSelectedItem(m);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a operação \"Carga Músicas por Momento\", detalhes tecnicos: "
							+ e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void selecionarMusica() {
		if (cboMusicas.getSelectedIndex() < 0)
			return;
		Musica musica = (Musica) cboMusicas.getSelectedItem();
		txtLinkMusica.setText(musica.getLink());
		txtNomeMusica.setText(musica.getNome());
		txtCifraMusica.setText(musica.getCifra());
		txtApresentacao.setText(musica.getApresentacao());
		txtCifraMusica.setCaretPosition(0);
		txtApresentacao.setCaretPosition(0);
	}

	private void novaMusica() {
		blank();
		cboMusicas.removeAllItems();
	}

	private void blank() {
		txtLinkMusica.setText("");
		txtNomeMusica.setText("");
		txtCifraMusica.setText("");
		txtApresentacao.setText("");
	}

	private void removerMusica() {
		if (cboMusicas.getSelectedIndex() < 0) {
			return;
		}
		blank();
		MusicaDao musicaDao = MusicaDaoFactory.createMusicaDao();
		Musica musica = (Musica) cboMusicas.getSelectedItem();
		try {
			musicaDao.excluir(musica);
			cboMusicas.removeItem(musica);
			JOptionPane.showMessageDialog(this, "A música foi excluída com sucesso", "Informação",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a operação \"Excluir\", detalhes tecnicos: " + e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void salvarMusica() {
		boolean novaMusica = cboMusicas.getSelectedIndex() < 0;
		Musica m;
		if (novaMusica) {
			m = new Musica();
		} else {
			m = (Musica) cboMusicas.getSelectedItem();
		}
		m.setNome(txtNomeMusica.getText());
		m.setCifra(txtCifraMusica.getText());
		m.setApresentacao(txtApresentacao.getText());
		m.setLink(txtLinkMusica.getText());
		MusicaDao musicaDao = MusicaDaoFactory.createMusicaDao();
		try {
			Momento momentoSelecionado = (Momento) cboMomentos.getSelectedItem();
			if (novaMusica) {
				m.getMomentos().add(momentoSelecionado);
				musicaDao.salvar(m);
				selecionarMomento(m);
			} else {
				musicaDao.alterar(m);
			}
			JOptionPane.showMessageDialog(this, "A música foi salva com sucesso", "Informação",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a operação \"Salvar\", detalhes tecnicos: " + e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void buscar() {
		try {
			txtLinkMusica.setText(Processador.getUrlMusica(textoPesquisa, index));
			imprimirCifraClub();
			String url = txtLinkMusica.getText();
			String[] cifra0eApresentacao1Nome2 = Processador.getCifra0EApresentacao1Nome2(url);
			txtCifraMusica.setText(cifra0eApresentacao1Nome2[0]);
			txtNomeMusica.setText(cifra0eApresentacao1Nome2[2]);
			txtCifraMusica.setCaretPosition(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Houve um na conexão ao tentar processar o texto: " + e.getLocalizedMessage(), "ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void imprimirCifraClub() {
		String link = txtLinkMusica.getText();
		link = ((link.contains(".html") ? link.substring(0, link.lastIndexOf("/")) : link) + "/imprimir.html")
				.replaceAll("//imprimir.html", "/imprimir.html");
		txtLinkMusica.setText(link);
	}

	private void gerar() {
		gerar(true);
	}

	private void gerar(boolean exibirMensagem) {
		if (txtLinkMusica.getText().contains("cifraclub")) {
			imprimirCifraClub();
		}
		String url = txtLinkMusica.getText();
		String[] cifra0eApresentacao1Nome2 = Processador.getCifra0EApresentacao1Nome2(url);
		if (url.contains("https://www.letras.mus.br")) {
			txtApresentacao.setText(cifra0eApresentacao1Nome2[1]);
		} else {
			txtCifraMusica.setText(cifra0eApresentacao1Nome2[0]);
			txtApresentacao.setText(cifra0eApresentacao1Nome2[1]);
			txtNomeMusica.setText(cifra0eApresentacao1Nome2[2]);
		}
		txtCifraMusica.setCaretPosition(0);
		txtApresentacao.setCaretPosition(0);
		if (exibirMensagem) {
			JOptionPane.showMessageDialog(this, "Música processada com sucesso, não se esqueça de salvar...",
					"Informação", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void previewApresentacao() {
		try {
			Processador.gerarPreviewApresentacaoTxt(txtApresentacao.getText());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante o preview\r\n detalhes tecnicos: " + e.getLocalizedMessage(), "ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void continuarMissa() {
		MissaFreeForm missaFreeForm = (MissaFreeForm) this.menuPrincipal.abrirPanel(NomePanel.FAZER_MISSA2);
	}

	/**
	 * Create the panel.
	 * 
	 * @param menuPrincipal
	 * 
	 * @throws Exception
	 */
	public CadastroMusicasPanel(MenuPrincipal menuPrincipal) {
		this.menuPrincipal = menuPrincipal;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblFiltro = new JLabel("Filtro");
		GridBagConstraints gbc_lblFiltro = new GridBagConstraints();
		gbc_lblFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_lblFiltro.anchor = GridBagConstraints.EAST;
		gbc_lblFiltro.gridx = 0;
		gbc_lblFiltro.gridy = 0;
		add(lblFiltro, gbc_lblFiltro);

		txtFiltro = new JTextField();
		txtFiltro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					filtrar();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(CadastroMusicasPanel.this,
							"Houve um erro durante a operação \"Filtrar momentos\", detalhes tecnicos: "
									+ e1.getLocalizedMessage(),
							"ERRO", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_txtFiltro = new GridBagConstraints();
		gbc_txtFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_txtFiltro.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFiltro.gridx = 1;
		gbc_txtFiltro.gridy = 0;
		add(txtFiltro, gbc_txtFiltro);
		txtFiltro.setColumns(10);

		JLabel lblMomento = new JLabel("Momento");
		GridBagConstraints gbc_lblMomento = new GridBagConstraints();
		gbc_lblMomento.anchor = GridBagConstraints.EAST;
		gbc_lblMomento.insets = new Insets(0, 0, 5, 5);
		gbc_lblMomento.gridx = 0;
		gbc_lblMomento.gridy = 1;
		add(lblMomento, gbc_lblMomento);

		cboMomentos = new JComboBox();
		cboMomentos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selecionarMomento();
			}
		});
		GridBagConstraints gbc_cboMomentos = new GridBagConstraints();
		gbc_cboMomentos.insets = new Insets(0, 0, 5, 5);
		gbc_cboMomentos.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboMomentos.gridx = 1;
		gbc_cboMomentos.gridy = 1;
		add(cboMomentos, gbc_cboMomentos);

		JLabel lblMsicas = new JLabel("M\u00FAsicas");
		GridBagConstraints gbc_lblMsicas = new GridBagConstraints();
		gbc_lblMsicas.insets = new Insets(0, 0, 5, 5);
		gbc_lblMsicas.anchor = GridBagConstraints.EAST;
		gbc_lblMsicas.gridx = 0;
		gbc_lblMsicas.gridy = 2;
		add(lblMsicas, gbc_lblMsicas);

		cboMusicas = new JComboBox<Musica>();
		cboMusicas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selecionarMusica();
			}
		});
		GridBagConstraints gbc_cboMusica = new GridBagConstraints();
		gbc_cboMusica.insets = new Insets(0, 0, 5, 5);
		gbc_cboMusica.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboMusica.gridx = 1;
		gbc_cboMusica.gridy = 2;
		add(cboMusicas, gbc_cboMusica);

		JButton btnNovaMsica = new JButton("Nova M\u00FAsica");
		btnNovaMsica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				novaMusica();
			}
		});
		btnNovaMsica.setIcon(new ImageIcon(CadastroMusicasPanel.class
				.getResource("/org/tango-project/tango-icon-theme/16x16/actions/document-new.png")));
		GridBagConstraints gbc_btnNovaMsica = new GridBagConstraints();
		gbc_btnNovaMsica.insets = new Insets(0, 0, 5, 0);
		gbc_btnNovaMsica.gridx = 2;
		gbc_btnNovaMsica.gridy = 2;
		add(btnNovaMsica, gbc_btnNovaMsica);

		JLabel lblLink = new JLabel("Link");
		GridBagConstraints gbc_lblLink = new GridBagConstraints();
		gbc_lblLink.anchor = GridBagConstraints.EAST;
		gbc_lblLink.insets = new Insets(0, 0, 5, 5);
		gbc_lblLink.gridx = 0;
		gbc_lblLink.gridy = 3;
		add(lblLink, gbc_lblLink);

		txtLinkMusica = new JTextField();
		txtLinkMusica.getInputMap().put(KeyStroke.getKeyStroke("control ENTER"), CadastroMusicasPanel.CONTINUAR_MISSA);
		txtLinkMusica.getActionMap().put(CadastroMusicasPanel.CONTINUAR_MISSA, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				continuarMissa();
			}
		});

		GridBagConstraints gbc_txtLinkMusica = new GridBagConstraints();
		gbc_txtLinkMusica.insets = new Insets(0, 0, 5, 5);
		gbc_txtLinkMusica.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLinkMusica.gridx = 1;
		gbc_txtLinkMusica.gridy = 3;
		add(txtLinkMusica, gbc_txtLinkMusica);
		txtLinkMusica.setColumns(10);

		JButton btnGerar = new JButton("Gerar");
		btnGerar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerar();
			}
		});
		btnGerar.setIcon(new ImageIcon(CadastroMusicasPanel.class
				.getResource("/org/tango-project/tango-icon-theme/16x16/actions/view-refresh.png")));
		GridBagConstraints gbc_btnGerar = new GridBagConstraints();
		gbc_btnGerar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGerar.insets = new Insets(0, 0, 5, 0);
		gbc_btnGerar.gridx = 2;
		gbc_btnGerar.gridy = 3;
		add(btnGerar, gbc_btnGerar);

		JLabel lblNomeMsica = new JLabel("Nome M\u00FAsica");
		GridBagConstraints gbc_lblNomeMsica = new GridBagConstraints();
		gbc_lblNomeMsica.anchor = GridBagConstraints.EAST;
		gbc_lblNomeMsica.insets = new Insets(0, 0, 5, 5);
		gbc_lblNomeMsica.gridx = 0;
		gbc_lblNomeMsica.gridy = 4;
		add(lblNomeMsica, gbc_lblNomeMsica);

		txtNomeMusica = new JTextField();
		txtNomeMusica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				index = 0;
				textoPesquisa = txtNomeMusica.getText();
				buscar();
			}
		});
		GridBagConstraints gbc_txtNomeMusica = new GridBagConstraints();
		gbc_txtNomeMusica.insets = new Insets(0, 0, 5, 5);
		gbc_txtNomeMusica.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNomeMusica.gridx = 1;
		gbc_txtNomeMusica.gridy = 4;
		add(txtNomeMusica, gbc_txtNomeMusica);
		txtNomeMusica.setColumns(10);

		carregarAutoComplete();

		JButton btnBuscar = new JButton("Próxima Cifra");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				index++;
				buscar();
			}
		});
		GridBagConstraints gbc_btnBuscar = new GridBagConstraints();
		gbc_btnBuscar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnBuscar.insets = new Insets(0, 0, 5, 0);
		gbc_btnBuscar.gridx = 2;
		gbc_btnBuscar.gridy = 4;
		add(btnBuscar, gbc_btnBuscar);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.7);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridwidth = 3;
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 5;
		add(splitPane, gbc_splitPane);

		txtCifraMusica = new RSyntaxTextArea(20, 60);

		JScrollPane scrollPane1 = new RTextScrollPane(txtCifraMusica);
		splitPane.setRightComponent(scrollPane1);
		scrollPane1.setViewportView(txtCifraMusica);

		txtApresentacao = new RSyntaxTextArea(20, 60);
		JScrollPane scrollPane2 = new RTextScrollPane(txtApresentacao);
		splitPane.setLeftComponent(scrollPane2);
		scrollPane2.setViewportView(txtApresentacao);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 6;
		add(panel, gbc_panel);

		JButton btnNewButton = new JButton("Salvar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvarMusica();
			}
		});

		btnLigarLinhas = new JButton("Ligar Linhas");
		btnLigarLinhas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ligarLinhas(txtApresentacao);
			}
		});

		JButton btnRefro = new JButton("Refr\u00E3o+");
		btnRefro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fazerRefrao(txtApresentacao, true);
			}
		});

		JButton btnAutoOrganizar = new JButton("Auto Organizar");
		btnAutoOrganizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				autoOrganizar();
			}
		});
		panel.add(btnAutoOrganizar);
		panel.add(btnRefro);

		JButton btnRefro_1 = new JButton("Refr\u00E3o-");
		btnRefro_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fazerRefrao(txtApresentacao, false);
			}
		});
		panel.add(btnRefro_1);

		JButton btnQuebrar = new JButton("Quebrar");
		btnQuebrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removerBarras(txtApresentacao);
			}
		});
		panel.add(btnQuebrar);
		panel.add(btnLigarLinhas);

		JButton btnDesfazer = new JButton("Desfazer");
		btnDesfazer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desfazer(txtApresentacao);
			}
		});
		panel.add(btnDesfazer);
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Excluir");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removerMusica();
			}
		});
		panel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Preview PPT");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewApresentacao();
			}
		});
		panel.add(btnNewButton_2);
	}

	private void carregarAutoComplete() {
		new AutoCompleteWindow(txtLinkMusica, 300, false, new AutoCompleteWindow.CustomSearch() {

			@Override
			public ArrayList<String> customSearcher() {
				try {
					if (((Momento) cboMomentos.getSelectedItem()).getNome().equalsIgnoreCase("salmo")) {
						return Processador.pesquisarSalmo();
					}
					if (txtLinkMusica.getText().startsWith("$")) {
						return Processador.pesquisarLinksLetras(txtLinkMusica.getText());
					}
					return Processador.pesquisarLinks(txtLinkMusica.getText());
				} catch (IOException e) {
					JOptionPane.showConfirmDialog(null, "Erro ao tentar encontrar o link da musica: " + e.getMessage());
				}
				return new ArrayList<String>();
			}

			@Override
			public void autocompleteListener(String selecion) {
				gerar(false);
			}
		});
	}
}