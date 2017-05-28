package br.com.ivan.missagenerator.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.ivan.missagenerator.frame.MenuPrincipal;
import br.com.ivan.missagenerator.frame.Painel;
import model.Momento;
import model.dao.MomentoDao;
import model.dao.factory.MomentoDaoFactory;

public class CadastroMomentos extends JPanel implements Painel{
	private JTextField txtNomeMomento;
	private JList jlstMomentos;
	private DefaultListModel<Momento> dlmMomentos;
	private MenuPrincipal menuPrincipal;

	private void selecionaMomento(){
		if(jlstMomentos.getSelectedIndex() < 0){
			return;
		}
		txtNomeMomento.setText(dlmMomentos.getElementAt(jlstMomentos.getSelectedIndex()).getNome());
	}
	
	public void carregarMomentos() throws Exception {
		dlmMomentos = new DefaultListModel<Momento>();
		jlstMomentos.setModel(dlmMomentos);
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		for (Momento m : momentoDao.listar()) {
			dlmMomentos.addElement(m);
		}
	}
	
	private void salvarMomento(){
		if(jlstMomentos.getSelectedIndex() < 0){
			Momento m = new Momento();
			m.setNome(txtNomeMomento.getText());
			MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
			try {
				momentoDao.salvar(m);
				carregarMomentos();
				JOptionPane.showMessageDialog(this, "Momento salvo com sucesso");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Não foi possível salvar o momento: " + e.getLocalizedMessage());
			}
		} else {
			Momento m = dlmMomentos.getElementAt(jlstMomentos.getSelectedIndex());
			m.setNome(txtNomeMomento.getText());
			MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
			try {
				momentoDao.salvar(m);
				carregarMomentos();
				JOptionPane.showMessageDialog(this, "Momento atualizado com sucesso");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Não foi possível atualizar o momento: " + e.getLocalizedMessage());
			}
		}
	}
	
	private void removerMomento(){
		if(jlstMomentos.getSelectedIndex() < 0){
			JOptionPane.showMessageDialog(this, "Selecione o momento que deve ser excluído");
		} else {
			Momento m = dlmMomentos.getElementAt(jlstMomentos.getSelectedIndex());
			MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
			try {
				momentoDao.excluir(m);
				carregarMomentos();
				JOptionPane.showMessageDialog(this, "Momento excluído com sucesso");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Não foi possível excluir o momento: " + e.getLocalizedMessage());
			}
			
		}
	}
	
	/**
	 * Create the panel.
	 * @param menuPrincipal 
	 * @throws Exception 
	 */
	public CadastroMomentos(MenuPrincipal menuPrincipal) {
		this.menuPrincipal = menuPrincipal;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Nome Momento");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		txtNomeMomento = new JTextField();
		GridBagConstraints gbc_txtNomeMomento = new GridBagConstraints();
		gbc_txtNomeMomento.insets = new Insets(0, 0, 5, 5);
		gbc_txtNomeMomento.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNomeMomento.gridx = 1;
		gbc_txtNomeMomento.gridy = 0;
		add(txtNomeMomento, gbc_txtNomeMomento);
		txtNomeMomento.setColumns(10);
		
		jlstMomentos = new JList();
		jlstMomentos.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				selecionaMomento();
			}
		});
		
		JButton button = new JButton("Salvar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvarMomento();
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 0);
		gbc_button.gridx = 2;
		gbc_button.gridy = 0;
		add(button, gbc_button);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridwidth = 2;
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 1;
		add(jlstMomentos, gbc_list);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JButton btnNovo = new JButton("   Novo   ");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				novo();
			}
		});
		panel.add(btnNovo);
		
		JButton btnNewButton_1 = new JButton(" Remover");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removerMomento();
			}
		});
		btnNewButton_1.setIcon(null);
		panel.add(btnNewButton_1);
	}
	
	private void novo(){
		jlstMomentos.clearSelection();
	}

	@Override
	public void refreshValues() throws Exception {
		carregarMomentos();
	}
}
