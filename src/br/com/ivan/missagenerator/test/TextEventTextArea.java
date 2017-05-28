package br.com.ivan.missagenerator.test;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

public class TextEventTextArea extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;
	private JButton btnRefro;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TextEventTextArea frame = new TextEventTextArea();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void juntarLinhas(JTextArea jtx) {
		try {
			int posicaoCursor = jtx.getCaretPosition();
			int numLinha = jtx.getLineOfOffset(posicaoCursor);
			int cursorInicioLinha = jtx.getLineStartOffset(numLinha);
			int cursorFimLinha = jtx.getLineEndOffset(numLinha);
			int lengthLinha = cursorFimLinha - cursorInicioLinha;
			String texto = jtx.getText();
			// ultima linha
			if(cursorFimLinha != texto.length()){
				jtx.setText(texto.substring(0, cursorFimLinha - 1) + " / " + texto.substring(cursorFimLinha));
			}
			jtx.setCaretPosition(cursorInicioLinha);
			jtx.requestFocus();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private void fazerRefrao(JTextArea jtx, boolean adicionarRefrao){
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
				if(adicionarRefrao){
					texto = adicionarRefrao(texto, jtx.getLineStartOffset(i));
				} else {
					texto = removerRefrao(texto, jtx.getLineStartOffset(i));
					
				}
				jtx.setText(texto);
			}
			texto = adicionarQuebra(texto,jtx.getLineStartOffset(linhaInicio), jtx.getLineEndOffset(linhaFim),adicionarRefrao);
			jtx.setText(texto);
			
			jtx.setCaretPosition(jtx.getLineStartOffset(linhaInicio));
			jtx.requestFocus();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private String adicionarQuebra(String texto, int offsetInicio, int offsetFim, boolean adicionarRefrao) {
		String quebra = adicionarRefrao ? "\n\n" : "\n";
		String texto1 = texto.substring(0, offsetInicio).trim();
		String texto2 = texto.substring(offsetInicio, offsetFim).trim();
		texto2 = texto2.replaceAll("\n\n", "\n");
		String texto3 = texto.substring(offsetFim).trim();
		if(offsetInicio != 0){
			texto1 += quebra;
		}
		return texto1 + texto2 + quebra + texto3;
	}

	private String adicionarRefrao(String texto, int posicaoStart) {
		if(texto.charAt(posicaoStart) == '$'){
			return texto;
		} else {
			return texto.substring(0, posicaoStart) + "$" + texto.substring(posicaoStart);
		}
	}

	private String removerRefrao(String texto, int posicaoStart) {
		if(texto.charAt(posicaoStart) == '$'){
			return texto.substring(0, posicaoStart) + texto.substring(posicaoStart + 1);
		} else {
			return texto;
		}
	}

	/**
	 * Create the frame.
	 */
	public TextEventTextArea() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnTest = new JButton("Refr\u00E3o+");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fazerRefrao(textArea, true);
			}
		});
		btnTest.setBounds(12, 215, 97, 25);
		contentPane.add(btnTest);
		
		textArea = new JTextArea();
		textArea.setText("Esta \u00E9 a linha n\u00FAmero 01\nEsta \u00E9 a linha n\u00FAmero 02\nEsta \u00E9 a linha n\u00FAmero 03\nEsta \u00E9 a linha n\u00FAmero 04\nEsta \u00E9 a linha n\u00FAmero 05\nEsta \u00E9 a linha n\u00FAmero 06\nEsta \u00E9 a linha n\u00FAmero 07\nEsta \u00E9 a linha n\u00FAmero 08\nEsta \u00E9 a linha n\u00FAmero 09\nEsta \u00E9 a linha n\u00FAmero 10\nEsta \u00E9 a linha n\u00FAmero 11\nEsta \u00E9 a linha n\u00FAmero 12\nEsta \u00E9 a linha n\u00FAmero 13\nEsta \u00E9 a linha n\u00FAmero 14\nEsta \u00E9 a linha n\u00FAmero 15\nEsta \u00E9 a linha n\u00FAmero 16\nEsta \u00E9 a linha n\u00FAmero 17\nEsta \u00E9 a linha n\u00FAmero 18\n");
//		contentPane.add(textArea);
		
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(12, 13, 408, 189);
		contentPane.add(scrollPane1);
		scrollPane1.setViewportView(textArea);
		
		btnRefro = new JButton("Refr\u00E3o-");
		btnRefro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fazerRefrao(textArea, false);
			}
		});
		btnRefro.setBounds(121, 215, 97, 25);
		contentPane.add(btnRefro);
		
	}
}
