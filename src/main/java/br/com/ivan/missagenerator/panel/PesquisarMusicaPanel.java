package br.com.ivan.missagenerator.panel;

import javax.swing.JPanel;
import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;

import br.com.ivan.missagenerator.business.Processador;
import br.com.ivan.missagenerator.frame.MenuPrincipal;
import br.com.ivan.missagenerator.frame.Painel;

import java.awt.Insets;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class PesquisarMusicaPanel extends JPanel implements Painel {
	private JTextField textField;
	private JList list;
	private MenuPrincipal menuPrincipal;

	/**
	 * Create the panel.
	 * @param menuPrincipal 
	 */
	public PesquisarMusicaPanel(MenuPrincipal menuPrincipal) {
		this.menuPrincipal = menuPrincipal;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Pesquisar M\u00FAsica");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pesquisarMusica();
			}
		});
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(textField, gbc_textField);
		textField.setColumns(10);
		
		list = new JList(new DefaultListModel<String>());
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridwidth = 2;
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 1;
		add(list, gbc_list);

	}

	protected void pesquisarMusica() {
//		ArrayList<String> links;
//		try {
//			links = Processador.pesquisarLinks(textField.getText());
//			DefaultListModel<String> listModel = new DefaultListModel<>();
//			for (String link : links) {
//				listModel.addElement(link);
//			}
//			list.setModel(listModel);
//		} catch (IOException e) {
//			JOptionPane.showMessageDialog(this, "Erro ao tentar pesquisar links: " + e.getMessage());
//			e.printStackTrace();
//		}
	}

	@Override
	public void refreshValues() throws Exception {
	}

}
