package br.com.ivan.missagenerator.panel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.AutoCompletionEvent;
import org.fife.ui.autocomplete.AutoCompletionEvent.Type;
import org.fife.ui.autocomplete.AutoCompletionListener;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import br.com.ivan.missagenerator.business.IvanContainsProvider;
import br.com.ivan.missagenerator.business.Processador;
import br.com.ivan.missagenerator.frame.MenuPrincipal;
import br.com.ivan.missagenerator.frame.Painel;
import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.MusicaDao;
import model.dao.factory.MomentoDaoFactory;
import model.dao.factory.MusicaDaoFactory;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;

public class MissaFreeFormPlusPanel extends JPanel implements Painel {

	private JTextArea txtMissa;
	private JTextArea txtCifra;
	private JTextArea txtApresentacao;
	private AutoCompletion ac;
	private int linhaSelecionada;
	private JRadioButton rdbtnLocal;
	private JRadioButton rdbtnCifra;
	private JRadioButton rdbtnLetra;
	private JRadioButton rdbtnSalmo;

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
		
		rdbtnLocal = new JRadioButton("local");
		rdbtnLocal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				local();
			}
		});
		panel_1.add(rdbtnLocal);
		
		rdbtnCifra = new JRadioButton("cifra");
		rdbtnCifra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cifra();
			}
		});
		panel_1.add(rdbtnCifra);
		
		rdbtnLetra = new JRadioButton("letra");
		rdbtnLetra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				letra();
			}
		});
		panel_1.add(rdbtnLetra);
		
		rdbtnSalmo = new JRadioButton("salmo");
		rdbtnSalmo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salmo();
			}
		});
		panel_1.add(rdbtnSalmo);

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
		txtMissa.requestFocus();
	}

	private void carregarMusicas() throws Exception {
			Collection<Momento> momentos = null;
			MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
			momentos = momentoDao.listar();

			DefaultCompletionProvider provider = new IvanContainsProvider();
			for (Momento m : momentos) {
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

	private void selecionarMusica(Musica musica) {
		String apresentacao = musica.getApresentacao();
		txtApresentacao.setText(apresentacao);
		txtApresentacao.setCaretPosition(0);
		txtCifra.setText(musica.getCifra());
		txtCifra.setCaretPosition(0);
		txtMissa.requestFocus();
	}
	
	private void local() {
		rdbtnCifra.setSelected(false);
		rdbtnLetra.setSelected(false);
		rdbtnSalmo.setSelected(false);
	}
	
	private void cifra() {
		rdbtnLocal.setSelected(false);
		rdbtnLetra.setSelected(false);
		rdbtnSalmo.setSelected(false);
	}
	
	private void letra() {
		rdbtnLocal.setSelected(false);
		rdbtnCifra.setSelected(false);
		rdbtnSalmo.setSelected(false);
	}
	
	private void salmo() {
		rdbtnLocal.setSelected(false);
		rdbtnCifra.setSelected(false);
		rdbtnLetra.setSelected(false);
		
		gerarAutoCompleteSalmo();
	}

	private void gerarAutoCompleteSalmo() {

		List<String> salmoLinks;
		try {
			salmoLinks = Processador.getSalmoLinks();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		DefaultCompletionProvider provider = new DefaultCompletionProvider();
		
		for (String linkSalmo : salmoLinks) {
			provider.addCompletion(new ShorthandCompletion(provider, linkSalmo, linkSalmo));
		}

		ac = new AutoCompletion(provider);
		ac.install(txtMissa);
		ac.addAutoCompletionListener(new AutoCompletionListener() {
			
			@Override
			public void autoCompleteUpdate(AutoCompletionEvent e) {
				if(e.getEventType().equals(Type.POPUP_HIDDEN)) {
					buscarSalmo();
				}
			}
		});
		
		
//		Collection<Momento> momentos = null;
//		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
//		momentos = momentoDao.listar();
//
//		DefaultCompletionProvider provider = new IvanContainsProvider();
//		for (Momento m : momentos) {
//			List<Musica> musicas = momentoDao.listarMusicas(m);
//			for (Musica musica : musicas) {
//				String textoMusica = StringUtils.rightPad(m.getId() + ":", 5)
//						+ StringUtils.rightPad(musica.getId() + ":", 5) + " "
//						+ StringUtils.rightPad(m.getNome() + ":", 20) + " "
//						+ StringUtils.rightPad(musica.getNome() + "", 0);
//				provider.addCompletion(new ShorthandCompletion(provider,
//						m.getNome() + " - " + musica.getNome() + " - " + musica.getApresentacao(), textoMusica));
//			}
//		}
//		ac = new AutoCompletion(provider);
//		ac.install(txtMissa);
		
	}
	
	
	private void buscarSalmo() {
		try {
			String url = getConteudoLinhaSelecionada();
			String[] cifra0eApresentacao1Nome2 = Processador.getCifra0EApresentacao1Nome2(url);
			txtApresentacao.setText(url);
			txtCifra.setText(cifra0eApresentacao1Nome2[0]);
			txtApresentacao.setText(cifra0eApresentacao1Nome2[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	private String getConteudoLinhaSelecionada(){
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
