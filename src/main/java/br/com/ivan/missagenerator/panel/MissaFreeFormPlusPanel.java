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
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.AutoCompletionEvent;
import org.fife.ui.autocomplete.AutoCompletionEvent.Type;
import org.fife.ui.autocomplete.AutoCompletionListener;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import br.com.ivan.missagenerator.business.Processador;
import br.com.ivan.missagenerator.business.provider.IvanContainsProvider;
import br.com.ivan.missagenerator.business.provider.MomentoContainsProvider;
import br.com.ivan.missagenerator.business.provider.ReplaceLineAutoCompletion;
import br.com.ivan.missagenerator.business.provider.SalmoContainsProvider;
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
		

	    //Group the radio buttons.
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
		splitPane.setRightComponent(new JScrollPane(txtCifra));

		txtApresentacao = new RSyntaxTextArea(20, 60);
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
				String textoMusica = madeMusicLine(m.getId().toString(), musica.getId().toString(), m.getNome(), musica.getNome(), musica.getLink());
				provider.addCompletion(new ShorthandCompletion(provider,
						m.getNome() + " - " + musica.getNome() + " - " + musica.getApresentacao(), textoMusica));
			}
		}
		ac = new ReplaceLineAutoCompletion(provider);
		ac.install(txtMissa);

	}

	private String madeMusicLine(String idMomento, String idMusica, String nomeMomento, String nomeMusica, String linkMusica) {
		return Processador.madeMusicLine(idMomento, idMusica, nomeMomento, nomeMusica, linkMusica);
	}

	private void carregarMissaSalva() {
		try {
			txtMissa.setText(Processador.loadMissa().trim());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a operaï¿½ï¿½o \"carregarMissaSalva\", detalhes tecnicos: "
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
					"Houve um erro durante a operaï¿½ï¿½o \"gerarPPT\", detalhes tecnicos: " + e.getLocalizedMessage(),
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
					"Houve um erro durante a operaï¿½ï¿½o \"gerarDOCX\", detalhes tecnicos: " + e.getLocalizedMessage(),
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
			// mï¿½sica
			missa.add(musicaDao.listar(Long.parseLong(elementos[1].trim())));
		}
		return missa;
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
					Musica musica = musicaDao.listar(Long.parseLong(elementos[1].trim()));
					selecionarMusica(musica);
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

	private void salvarMissa(boolean isCaretUpdate) {
		try {
			Processador.salvarMissa(txtMissa.getText());
			if (!isCaretUpdate) {
				JOptionPane.showMessageDialog(this, "A Missa foi salva com sucesso!", "Informaï¿½ï¿½o",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a operaï¿½ï¿½o \"salvarMissa\", detalhes tecnicos: "
							+ e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
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

	private void interno() {
		txtMissa.requestFocus();
	}

	private void cifra() {
	}

	private void letra() {
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

		ac = new ReplaceLineAutoCompletion(provider);
		ac.install(txtMissa);
		ac.addAutoCompletionListener(new AutoCompletionListener() {

			@Override
			public void autoCompleteUpdate(AutoCompletionEvent e) {
				if (e.getEventType().equals(Type.POPUP_SHOWN)) {
					// setCursorInicioLinha();
				}
					
				if (e.getEventType().equals(Type.POPUP_HIDDEN)) {
					buscarSalmo();
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
				provider.addCompletion(new ShorthandCompletion(provider, momento.getId() + ":" + momento.getNome(), momento.getId() + ":" + momento.getNome()));
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
		try {
			String url = getConteudoLinhaSelecionada();
			String[] cifra0eApresentacao1Nome2 = Processador.getCifra0EApresentacao1Nome2(url);
			txtApresentacao.setText(url);
			String cifra = cifra0eApresentacao1Nome2[0];
			String apresentacao = cifra0eApresentacao1Nome2[1];
			String nome = cifra0eApresentacao1Nome2[2];
			txtCifra.setText(cifra);
			txtApresentacao.setText(apresentacao);
			adicionarLinha(-1L, -1L, "naoselecionado", nome, url);
		} catch (IOException e) {
			// ignore error
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void adicionarLinha(Long idMomento, Long idMusica, String nomeMomento, String nomeMusica, String linkMusica) {
		try {
			int posicaoCursor = txtMissa.getCaretPosition();
			int numLinha = txtMissa.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtMissa.getLineStartOffset(numLinha);
			int cursorFimLinha = txtMissa.getLineEndOffset(numLinha);

			String texto = txtMissa.getText();
			txtMissa.setText(texto.substring(0, cursorInicioLinha) + // linhas anteriores
							 madeMusicLine(idMomento.toString(), idMusica.toString(), nomeMomento, nomeMusica, linkMusica) + //"\n" + // nova linha
							 texto.substring(cursorFimLinha));
			txtMissa.setCaretPosition(cursorInicioLinha);
			txtMissa.requestFocus();
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(this,
					"Houve um erro durante a operação \"adicionarMusica\", detalhes tecnicos: "
							+ e.getLocalizedMessage(),
					"ERRO", JOptionPane.ERROR_MESSAGE);
		}		
	}

	private String getConteudoLinhaSelecionada() {
		try {
			int posicaoCursor = txtMissa.getCaretPosition();
			int numLinha = txtMissa.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = txtMissa.getLineStartOffset(numLinha);
			int cursorFimLinha = txtMissa.getLineEndOffset(numLinha);

			return txtMissa.getText().substring(cursorInicioLinha, cursorFimLinha);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
