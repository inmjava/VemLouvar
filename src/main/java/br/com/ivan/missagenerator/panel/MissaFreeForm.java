package br.com.ivan.missagenerator.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
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
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import br.com.ivan.missagenerator.business.Processador;
import br.com.ivan.missagenerator.business.autocomplete.provider.IvanContainsProvider;
import br.com.ivan.missagenerator.frame.MenuPrincipal;
import br.com.ivan.missagenerator.frame.Painel;
import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.MusicaDao;
import model.dao.factory.MomentoDaoFactory;
import model.dao.factory.MusicaDaoFactory;

public class MissaFreeForm extends JPanel implements Painel {
	private static final String NEXT_MUSIC = "nextMusic";
	private static final String PREVIOUS_MUSIC = "previousMusic";
	private static final String REMOVE_LINE = "removeLine";
	private static final String ADD_MUSICA = "addMusica";
	private static final String MAKE_MISSA_FOCUS = "makeMissaFocus";
	private static final String MAKE_FILTER_FOCUS = "makeFilterFocus";
	private static final Object MAKE_NEW_MUSIC = "makeNewMusic";
	private JTextField txtFiltro;
	private JComboBox cboMomentos;
	private JComboBox cboMusicas;
	private String strFiltro;
	private JTextArea txtApresentacao;
	private RSyntaxTextArea txtMissa;
	private int linhaSelecionada;
	private AutoCompletion ac;
	private MenuPrincipal menuPrincipal;

	/**
	 * Create the panel.
	 * 
	 * @param menuPrincipal
	 */
	public MissaFreeForm(MenuPrincipal menuPrincipal) {
		this.menuPrincipal = menuPrincipal;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblFiltro = new JLabel("Filtro");
		GridBagConstraints gbc_lblFiltro = new GridBagConstraints();
		gbc_lblFiltro.anchor = GridBagConstraints.EAST;
		gbc_lblFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_lblFiltro.gridx = 0;
		gbc_lblFiltro.gridy = 0;
		add(lblFiltro, gbc_lblFiltro);

		txtFiltro = new JTextField();
		txtFiltro.getInputMap().put(KeyStroke.getKeyStroke("control M"), MAKE_MISSA_FOCUS);
		txtFiltro.getActionMap().put(MissaFreeForm.MAKE_MISSA_FOCUS, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeMissaFocus();
			}
		});

		txtFiltro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					filtrarMomentos();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MissaFreeForm.this,
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

		JButton btnNewButton = new JButton("Filtrar M\u00FAsica");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				filtrar();
			}
		});

		JButton btnFiltrar = new JButton("Filtrar Momento");
		btnFiltrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					filtrarMomentos();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MissaFreeForm.this,
							"Houve um erro durante a operação \"Filtrar momentos\", detalhes tecnicos: "
									+ e1.getLocalizedMessage(),
							"ERRO", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_btnFiltrar = new GridBagConstraints();
		gbc_btnFiltrar.insets = new Insets(0, 0, 5, 5);
		gbc_btnFiltrar.gridx = 2;
		gbc_btnFiltrar.gridy = 0;
		add(btnFiltrar, gbc_btnFiltrar);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 0;
		add(btnNewButton, gbc_btnNewButton);

		JLabel lblMomento = new JLabel("Momento");
		GridBagConstraints gbc_lblMomento = new GridBagConstraints();
		gbc_lblMomento.anchor = GridBagConstraints.EAST;
		gbc_lblMomento.insets = new Insets(0, 0, 5, 5);
		gbc_lblMomento.gridx = 0;
		gbc_lblMomento.gridy = 1;
		add(lblMomento, gbc_lblMomento);

		cboMomentos = new JComboBox();
		cboMomentos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selecionarMomento();
			}
		});
		GridBagConstraints gbc_cboMomentos = new GridBagConstraints();
		gbc_cboMomentos.gridwidth = 2;
		gbc_cboMomentos.insets = new Insets(0, 0, 5, 5);
		gbc_cboMomentos.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboMomentos.gridx = 1;
		gbc_cboMomentos.gridy = 1;
		add(cboMomentos, gbc_cboMomentos);

		JButton btnSalvarMissa = new JButton("Salvar Missa");
		btnSalvarMissa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				salvarMissa();
			}
		});
		GridBagConstraints gbc_btnSalvarMissa = new GridBagConstraints();
		gbc_btnSalvarMissa.insets = new Insets(0, 0, 5, 0);
		gbc_btnSalvarMissa.gridx = 3;
		gbc_btnSalvarMissa.gridy = 1;
		add(btnSalvarMissa, gbc_btnSalvarMissa);

		JLabel lblMsica = new JLabel("M\u00FAsica");
		GridBagConstraints gbc_lblMsica = new GridBagConstraints();
		gbc_lblMsica.anchor = GridBagConstraints.EAST;
		gbc_lblMsica.insets = new Insets(0, 0, 5, 5);
		gbc_lblMsica.gridx = 0;
		gbc_lblMsica.gridy = 2;
		add(lblMsica, gbc_lblMsica);

		cboMusicas = new JComboBox();
		cboMusicas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selecionarMusicaCombo();
			}
		});
		GridBagConstraints gbc_cboMusicas = new GridBagConstraints();
		gbc_cboMusicas.gridwidth = 2;
		gbc_cboMusicas.insets = new Insets(0, 0, 5, 5);
		gbc_cboMusicas.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboMusicas.gridx = 1;
		gbc_cboMusicas.gridy = 2;
		add(cboMusicas, gbc_cboMusicas);

		JButton btnNewButton_1 = new JButton("Adicionar M\u00FAsica");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionarMusica();
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 3;
		gbc_btnNewButton_1.gridy = 2;
		add(btnNewButton_1, gbc_btnNewButton_1);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.9);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GridBagConstraints gbc_splitPane_1 = new GridBagConstraints();
		gbc_splitPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane_1.fill = GridBagConstraints.BOTH;
		gbc_splitPane_1.gridwidth = 4;
		gbc_splitPane_1.gridx = 0;
		gbc_splitPane_1.gridy = 3;
		add(splitPane_1, gbc_splitPane_1);

		txtMissa = new RSyntaxTextArea(20, 60);

		JScrollPane scrollPane = new RTextScrollPane(txtMissa);
		splitPane_1.setLeftComponent(scrollPane);

		// txtMissa.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		txtMissa.setCodeFoldingEnabled(false);
		txtMissa.getInputMap().put(KeyStroke.getKeyStroke("control N"), MissaFreeForm.MAKE_NEW_MUSIC);
		txtMissa.getActionMap().put(MissaFreeForm.MAKE_NEW_MUSIC, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				makeNewMusic();
			}
		});
		txtMissa.getInputMap().put(KeyStroke.getKeyStroke("control F"), MissaFreeForm.MAKE_FILTER_FOCUS);
		txtMissa.getActionMap().put(MissaFreeForm.MAKE_FILTER_FOCUS, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				makeFilterFocus();
			}
		});
		txtMissa.getInputMap().put(KeyStroke.getKeyStroke("control shift A"), ADD_MUSICA);
		txtMissa.getActionMap().put(MissaFreeForm.ADD_MUSICA, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarMusica();
			}
		});
		txtMissa.getInputMap().put(KeyStroke.getKeyStroke("control D"), REMOVE_LINE);
		txtMissa.getActionMap().put(MissaFreeForm.REMOVE_LINE, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeLineCaret();
			}
		});
		txtMissa.getInputMap().put(KeyStroke.getKeyStroke("control COMMA"), PREVIOUS_MUSIC);
		txtMissa.getActionMap().put(MissaFreeForm.PREVIOUS_MUSIC, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previousMusic();
			}
		});
		txtMissa.getInputMap().put(KeyStroke.getKeyStroke("control PERIOD"), NEXT_MUSIC);
		txtMissa.getActionMap().put(MissaFreeForm.NEXT_MUSIC, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextMusic();
			}
		});

		txtMissa.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				selecionarMusicaCaretUpdate();
			}
		});
		txtMissa.setFont(new Font("Consolas", Font.PLAIN, 15));
		scrollPane.setViewportView(txtMissa);

		txtApresentacao = new RSyntaxTextArea(20, 60);

		JScrollPane scrollPane2 = new RTextScrollPane(txtApresentacao);
		scrollPane2.setViewportView(txtApresentacao);

		splitPane_1.setRightComponent(scrollPane2);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 4;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		add(panel, gbc_panel);

		JButton btnNewButton_2 = new JButton("Gerar PPT");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerarPPT();
			}
		});
		panel.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("Gerar DOCX");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerarDOCX();
			}
		});
		panel.add(btnNewButton_3);
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

		DefaultCompletionProvider provider = new IvanContainsProvider();
		for (Momento m : momentos) {
			cboMomentos.addItem(m);
			List<Musica> musicas = momentoDao.listarMusicas(m);
			for (Musica musica : musicas) {
				String textoMusica = StringUtils.rightPad(m.getId() + ":", 5)
						+ StringUtils.rightPad(musica.getId() + ":", 5) + " "
						+ StringUtils.rightPad(m.getNome() + ":", 20) + " "
						+ StringUtils.rightPad(musica.getNome() + "", 0);
				provider.addCompletion(new ShorthandCompletion(provider,
						m.getNome() + " - " + musica.getNome() + " - " + musica.getApresentacao(), textoMusica));
			}
		}
		ac = new AutoCompletion(provider);
		ac.install(txtMissa);
	}

	@Override
	public void refreshValues() throws Exception {
		carregarMomentos();
		carregarMissaSalva();
	}

	private void carregarMissaSalva() {
		try {
			txtMissa.setText(Processador.loadMissa().trim());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a operação \"carregarMissaSalva\", detalhes tecnicos: "
							+ e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void carregarMomentos() throws Exception {
		carregarMomentos(null);
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

	private void filtrar() {
		strFiltro = txtFiltro.getText();
		selecionarMomento();
	}

	private void filtrarMomentos() throws Exception {
		strFiltro = txtFiltro.getText();
		carregarMomentos(strFiltro);
		if (!strFiltro.equals("")) {
			adicionarMusica();
		}
		txtMissa.requestFocus();
	}

	private void salvarMissa() {
		salvarMissa(false);
	}

	private void salvarMissa(boolean isCaretUpdate) {
		try {
			Processador.salvarMissa(txtMissa.getText());
			if(!isCaretUpdate){
				JOptionPane.showMessageDialog(this, "A Missa foi salva com sucesso!", "Informação",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a operação \"salvarMissa\", detalhes tecnicos: " + e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);

		}
	}

	private void selecionarMusicaCombo() {
		if (cboMusicas.getSelectedIndex() < 0)
			return;
		Musica musica = (Musica) cboMusicas.getSelectedItem();
		selecionarMusica(musica);
	}

	private void selecionarMusica(Musica musica) {
		String apresentacao = musica.getApresentacao();
		txtApresentacao.setText(apresentacao);
		txtApresentacao.setCaretPosition(0);
		try {
			if (strFiltro != null && !strFiltro.equals("")) {
				int length = strFiltro.length();
				int index = apresentacao.toUpperCase().indexOf(strFiltro.toUpperCase());
				while (index >= 0) {
					txtApresentacao.getHighlighter().addHighlight(index, index + length,
							new DefaultHighlighter.DefaultHighlightPainter(Color.pink));
					index = apresentacao.toUpperCase().indexOf(strFiltro.toUpperCase(), index + 1);
				}
			}
			txtApresentacao.setCaretPosition(0);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void makeNewMusic() {
		try {
			int posicaoCursor = txtMissa.getCaretPosition();
			int numLinha = txtMissa.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtMissa.getLineStartOffset(numLinha);
			int cursorFimLinha = txtMissa.getLineEndOffset(numLinha);
			String linha = txtMissa.getText().substring(cursorInicioLinha, cursorFimLinha);
			String[] elementos = linha.split(":");
			if (StringUtils.isNumeric(elementos[0]) && StringUtils.isNumeric(elementos[1].trim())) {
				CadastroMusicasPanel cmp = (CadastroMusicasPanel) this.menuPrincipal.abrirPanel(NomePanel.CADASTRO_MUSICA);
				MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
				Momento m = momentoDao.listar(Long.parseLong(elementos[0]));
				cmp.criarNovaMusicaExterno(m);
				
			}
		} catch (BadLocationException e) {
			System.out.println("Problema com a posicao da linha");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException (Normal do split)");
		} catch (Exception e) {
			System.out.println("Verificar: " + e.getMessage());
		}

	}

	private void makeFilterFocus() {
		txtFiltro.requestFocus();
		txtFiltro.selectAll();
	}

	public void makeMissaFocus() {
		txtMissa.requestFocus();
	}

	private void previousMusic() {
		int selectedIndex = cboMusicas.getSelectedIndex();
		if (selectedIndex > 0) {
			cboMusicas.setSelectedIndex(selectedIndex - 1);
		} else {
			selectedIndex = cboMomentos.getSelectedIndex();
			if (selectedIndex > 0) {
				cboMomentos.setSelectedIndex(selectedIndex - 1);
				int itemCount = cboMusicas.getItemCount();
				if (itemCount > 0) {
					cboMusicas.setSelectedIndex(itemCount - 1);
				}
			}
		}
		adicionarMusica();
	}

	private void nextMusic() {
		int selectedIndex = cboMusicas.getSelectedIndex();
		if (selectedIndex < cboMusicas.getItemCount() - 1) {
			cboMusicas.setSelectedIndex(selectedIndex + 1);
		} else {
			selectedIndex = cboMomentos.getSelectedIndex();
			if (selectedIndex < cboMomentos.getItemCount() - 1) {
				cboMomentos.setSelectedIndex(selectedIndex + 1);
			}
		}
		adicionarMusica();
	}

	private void removeLineCaret() {
		try {
			int posicaoCursor = txtMissa.getCaretPosition();
			int numLinha = txtMissa.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtMissa.getLineStartOffset(numLinha);
			int cursorFimLinha = txtMissa.getLineEndOffset(numLinha);
			txtMissa.setText(
					txtMissa.getText().substring(0, cursorInicioLinha) + txtMissa.getText().substring(cursorFimLinha));
			txtMissa.setCaretPosition(cursorInicioLinha);
		} catch (BadLocationException e) {
			System.out.println("Problema com a posicao da linha");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException (Normal do split)");
		} catch (Exception e) {
			System.out.println("Verificar: " + e.getMessage());
		}
	}

	private void selecionarMusicaCaretUpdate() {
		try {
			salvarMissa(true);
			int posicaoCursor = txtMissa.getCaretPosition();
			int numLinha = txtMissa.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtMissa.getLineStartOffset(numLinha);
			int cursorFimLinha = txtMissa.getLineEndOffset(numLinha);
			if (linhaSelecionada != numLinha) {
				linhaSelecionada = numLinha;
				String linha = txtMissa.getText().substring(cursorInicioLinha, cursorFimLinha);
				String[] elementos = linha.split(":");
				if (StringUtils.isNumeric(elementos[0]) && StringUtils.isNumeric(elementos[1].trim())) {
					MusicaDao musicaDao = MusicaDaoFactory.createMusicaDao();
					MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
					Momento momento = momentoDao.listar(Long.parseLong(elementos[0]));
					Musica musica = musicaDao.listar(Long.parseLong(elementos[1].trim()));
					selecionarMusica(musica);
					selecionarMomemtoEMusicaCaretUpdate(momento, musica);
				}
			}
		} catch (BadLocationException e) {
			System.out.println("Problema com a posicao da linha");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException (Normal do split)");
		} catch (Exception e) {
			System.out.println("Verificar: " + e.getMessage());
		}
	}

	private void selecionarMomemtoEMusicaCaretUpdate(Momento momento, Musica musica) {
		cboMomentos.setSelectedItem(momento);
		cboMusicas.setSelectedItem(musica);
	}

	private void adicionarMusica() {
		try {
			Musica musica = (Musica) cboMusicas.getSelectedItem();
			Momento momento = (Momento) cboMomentos.getSelectedItem();

			int posicaoCursor = txtMissa.getCaretPosition();
			int numLinha = txtMissa.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtMissa.getLineStartOffset(numLinha);
			int cursorFimLinha = txtMissa.getLineEndOffset(numLinha);
			int lengthLinha = cursorFimLinha - cursorInicioLinha;

			String texto = txtMissa.getText();
			txtMissa.setText(texto.substring(0, cursorInicioLinha) + StringUtils.rightPad(momento.getId() + ":", 5)
					+ StringUtils.rightPad(musica.getId() + ":", 5) + " "
					+ StringUtils.rightPad(momento.getNome() + ":", 20) + " "
					+ StringUtils.rightPad(musica.getNome() + "", 0) + "\n" + texto.substring(cursorFimLinha));
			txtMissa.setCaretPosition(cursorInicioLinha);
			txtMissa.requestFocus();
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a operação \"adicionarMusica\", detalhes tecnicos: "
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
					"Houve um erro durante a operação \"gerarPPT\", detalhes tecnicos: " + e.getLocalizedMessage(),
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
					"Houve um erro durante a operação \"gerarDOCX\", detalhes tecnicos: " + e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
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
			missa.add(elementos[2].trim());
			// música
			missa.add(musicaDao.listar(Long.parseLong(elementos[1].trim())));
		}
		return missa;
	}
}