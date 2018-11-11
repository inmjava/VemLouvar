package br.com.ivan.missagenerator.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import br.com.ivan.missagenerator.business.Processador;
import br.com.ivan.missagenerator.frame.MenuPrincipal;
import br.com.ivan.missagenerator.frame.Painel;
import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.MusicaDao;
import model.dao.factory.MomentoDaoFactory;
import model.dao.factory.MusicaDaoFactory;

public class CriarMissaPanel extends JPanel implements Painel {

	private static final int INICIO_COMPONENTES = 3;
	private static final String NAO_HAVERA = "<Não Haverá>";
	private JPanel panelMomentosMusicas;
	private Collection<Musica> musicas;
	private Collection<Momento> momentos;
	private MenuPrincipal menuPrincipal;

	private void carregarMomentos() throws Exception {
		MomentoDao momentodao = MomentoDaoFactory.createMomentoDao();
		momentos = momentodao.listar();
	}

	private void carregarMusicas(long idMomento, JComboBox cboMusicas) throws Exception {
		cboMusicas.removeAllItems();
		MusicaDao musicadao = MusicaDaoFactory.createMusicaDao();
		cboMusicas.addItem(NAO_HAVERA);
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		List musicas = momentoDao.listarMusicas(idMomento);
		for (Iterator iterator = musicas.iterator(); iterator.hasNext();) {
			Musica musica = (Musica) iterator.next();
			cboMusicas.addItem(musica);
		}
		if(musicas.size() > 0){
			cboMusicas.setSelectedIndex(1);
		}
	}

	public void refreshValues() throws Exception {
		carregarMomentos();
		criarCombos();
	}

	private void criarCombos() throws Exception {
		int i = 1;
		for (Iterator iterator = momentos.iterator(); iterator.hasNext();) {
			Momento momento = (Momento) iterator.next();
			
			JLabel labelMomentos = new JLabel(momento.getNome());
			labelMomentos.setToolTipText(momento.getId()+"");
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.insets = new Insets(0, 0, 0, 5);
			gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox.gridx = 0;
			gbc_comboBox.gridy = i;
			panelMomentosMusicas.add(labelMomentos, gbc_comboBox);
			
			JComboBox comboMusicas = new JComboBox();
			carregarMusicas(momento.getId(), comboMusicas);
			GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
			gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox_1.gridx = 1;
			gbc_comboBox_1.gridy = i;
			panelMomentosMusicas.add(comboMusicas, gbc_comboBox_1);
			
			JButton btnNewButton_3 = new JButton("Nova M\u00FAsica");
			btnNewButton_3.setEnabled(false);
			btnNewButton_3.setIcon(new ImageIcon(CriarMissaPanel.class.getResource("/actions/document-new.png")));
			GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
			gbc_btnNewButton_3.gridx = 2;
			gbc_btnNewButton_3.gridy = i;
			panelMomentosMusicas.add(btnNewButton_3, gbc_btnNewButton_3);
			
			i++;
		}
	}
	
	private void updateCombos() throws Exception{
		GridBagLayout layout = (GridBagLayout) panelMomentosMusicas.getLayout();
		Component[] components = panelMomentosMusicas.getComponents();
		int idAtual = 0;
		for (int i = INICIO_COMPONENTES; i < components.length; i++) {
			Component component = components[i];
			if (component instanceof JLabel){
				JLabel label = (JLabel) component;
				idAtual = new Integer(label.getToolTipText());
			} else if (component instanceof JComboBox) {
				JComboBox combo = (JComboBox) component;
				carregarMusicas(idAtual, combo);
			}
		}
	}

	private ArrayList gerarItensMissa() {
		ArrayList itensMissa = new ArrayList();
		GridBagLayout layout = (GridBagLayout) panelMomentosMusicas.getLayout();
		Component[] components = panelMomentosMusicas.getComponents();
		for (int i = INICIO_COMPONENTES; i < components.length; i++) {
			Component component = components[i];
			if (component instanceof JLabel){
				JLabel label = (JLabel) component;
				itensMissa.add(label.getText());
			} else if (component instanceof JComboBox) {
				JComboBox combo = (JComboBox) component;
				Object selectedItem = combo.getSelectedItem();
				if(selectedItem.equals(NAO_HAVERA)){
					itensMissa.remove(itensMissa.size() - 1);
				} else{
					itensMissa.add(selectedItem);	
				}
			}
		}
		return itensMissa;
	}
	
	private void gerarCifra(){
		ArrayList itensMissa = gerarItensMissa();
		try {
			Processador.gerarPreviewCifra(itensMissa);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void exportar(){
		ArrayList itensMissa = gerarItensMissa();
		try {
			Processador.exportar(itensMissa);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void gerarPPT() {
		ArrayList itensMissa = gerarItensMissa();
		try {
			Processador.gerarPreviewApresentacao(itensMissa);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void importar() {
		try {
			HashMap<String, Long> selecoes = Processador.importarSelecoes();
			
			Long selecionado = -1l;
			
			GridBagLayout layout = (GridBagLayout) panelMomentosMusicas.getLayout();
			Component[] components = panelMomentosMusicas.getComponents();
			for (int i = INICIO_COMPONENTES; i < components.length; i++) {
				Component component = components[i];
				if (component instanceof JLabel){
					JLabel label = (JLabel) component;
					if(selecoes.containsKey(label.getText())){
						selecionado = selecoes.get(label.getText());
					} else {
						// não achou
						selecionado = -1l;
					}
				} else if (component instanceof JComboBox) {
					JComboBox combo = (JComboBox) component;
					combo.setSelectedItem(NAO_HAVERA);
					if(selecionado != -1) {
		                for(int j = 0; j < combo.getItemCount(); j++) {
		                    Object element = combo.getItemAt(j);
		                    if(element instanceof Musica){
		                    	if(((Musica)element).getId() == selecionado){
		                    		combo.setSelectedIndex(j);
		                    		break;
		                    	}
		                    }
		                }
					}
				}
			}

			JOptionPane.showMessageDialog(this, "A importacao foi realizada com sucesso", "Informação", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * Create the panel.
	 * @param menuPrincipal 
	 * @throws Exception 
	 */
	public CriarMissaPanel(MenuPrincipal menuPrincipal) {
		this.menuPrincipal = menuPrincipal;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);
		
		panelMomentosMusicas = new JPanel();
		scrollPane.setViewportView(panelMomentosMusicas);
		GridBagLayout gbl_panelMomentosMusicas = new GridBagLayout();
		gbl_panelMomentosMusicas.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelMomentosMusicas.rowHeights = new int[]{0, 0, 0};
		gbl_panelMomentosMusicas.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelMomentosMusicas.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelMomentosMusicas.setLayout(gbl_panelMomentosMusicas);
		
		JLabel lblNewLabel = new JLabel("Momento");
		lblNewLabel.setBackground(UIManager.getColor("Button.darkShadow"));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panelMomentosMusicas.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("M\u00FAsica");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 0;
		panelMomentosMusicas.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JLabel lblAdicionar = new JLabel("Adicionar");
		GridBagConstraints gbc_lblAdicionar = new GridBagConstraints();
		gbc_lblAdicionar.insets = new Insets(0, 0, 5, 0);
		gbc_lblAdicionar.gridx = 2;
		gbc_lblAdicionar.gridy = 0;
		panelMomentosMusicas.add(lblAdicionar, gbc_lblAdicionar);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		
		JButton btnNewButton = new JButton("Gerar PPT");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gerarPPT();
			}
		});
		panel.add(btnNewButton);
		
		JButton btnGerarCifra = new JButton("Gerar Cifra");
		btnGerarCifra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerarCifra();
			}
		});
		panel.add(btnGerarCifra);
		
		JButton btnExportar = new JButton("Exportar");
		btnExportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exportar();
			}
		});
		panel.add(btnExportar);
		
		JButton btnImportar = new JButton("Importar");
		btnImportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				importar();
			}
		});
		panel.add(btnImportar);
	}

}
