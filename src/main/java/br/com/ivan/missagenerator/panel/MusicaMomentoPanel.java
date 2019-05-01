package br.com.ivan.missagenerator.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.com.ivan.missagenerator.frame.MenuPrincipal;
import br.com.ivan.missagenerator.frame.Painel;
import model.Momento;
import model.Musica;
import model.dao.MomentoDao;
import model.dao.MusicaDao;
import model.dao.factory.MomentoDaoFactory;
import model.dao.factory.MusicaDaoFactory;

public class MusicaMomentoPanel extends JPanel implements Painel {

	private JComboBox<Musica> cboMusicas;
	private JComboBox<Momento> cboMomentos;
	private Collection<Musica> musicas;
	private Collection<Momento> momentos;
	private JList jlNAssociados;
	private JList jlAssociados;
	private DefaultListModel dlmAssociados;
	private DefaultListModel dlmNAssociados;
	private JButton btnAdd;
	private JButton btnRemove;
	private boolean isMusicaSelecionada;
	private MenuPrincipal menuPrincipal;

	public void refreshValues() throws Exception{
		inicializaModels();
		carregarMomentos();
		carregarMusicas();
		adicionarEventos();
	}
	
	private void adicionarEventos() {
		cboMomentos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selecionaMomento();
			}
		});
		cboMusicas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selecionaMusica();
			}
		});

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				adicionar();
			}
		});
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remover();
			}
		});
		
	}

	private void inicializaModels() {
		dlmAssociados = new DefaultListModel();
		jlAssociados.setModel(dlmAssociados);
		dlmNAssociados = new DefaultListModel();
		jlNAssociados.setModel(dlmNAssociados);
	}

	private void carregarMusicas() throws Exception {
		MusicaDao musicadao = MusicaDaoFactory.createMusicaDao();
		musicas = musicadao.listar();
		cboMusicas.removeAllItems();
		for (Musica musica : musicas) {
			cboMusicas.addItem(musica);
		}
	}

	private void carregarMomentos() throws Exception {
		MomentoDao momentodao = MomentoDaoFactory.createMomentoDao();
		momentos = momentodao.listar();
		cboMomentos.removeAllItems();
		for (Momento momento : momentos) {
			cboMomentos.addItem(momento);
		}
	}
	
	private void selecionaMomento(){
		isMusicaSelecionada = false;
		if(musicas == null || momentos == null){
			return;
		}
		dlmNAssociados.removeAllElements();
		dlmAssociados.removeAllElements();
		for (Musica musica : musicas) {
			dlmNAssociados.addElement(musica);
		}
		Momento momento = (Momento) cboMomentos.getSelectedItem();
		if(momento == null)
			return;
		MomentoDao momentoDao = MomentoDaoFactory.createMomentoDao();
		try {
			List musicas2 = momentoDao.listarMusicas(momento);
			for (Iterator iterator = musicas2.iterator(); iterator.hasNext();) {
				Musica musica = (Musica) iterator.next();
				dlmAssociados.addElement(musica);
				dlmNAssociados.removeElement(musica);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Houve um erro durante a operação \"Carga Músicas por Momento\", detalhes tecnicos: " + e.getLocalizedMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private void selecionaMusica(){
		isMusicaSelecionada = true;
		if(musicas == null || momentos == null){
			return;
		}
		dlmNAssociados.removeAllElements();
		dlmAssociados.removeAllElements();
		for (Momento momento : momentos) {
			dlmNAssociados.addElement(momento);
		}
		Musica musica = (Musica) cboMusicas.getSelectedItem();
		if(musica == null){
			return;
		}
		MusicaDao musicaDao = MusicaDaoFactory.createMusicaDao();
		try {
			List momentos = musicaDao.listarMomentos(musica);
			for (Iterator iterator = momentos.iterator(); iterator.hasNext();) {
				Momento momento = (Momento) iterator.next();
				dlmAssociados.addElement(momento);
				dlmNAssociados.removeElement(momento);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void adicionar() {
		
		Collection clNAssociados = new ArrayList();
		
		int[] nAssociadosSelecionados = jlNAssociados.getSelectedIndices();
		for (int i = 0; i < nAssociadosSelecionados.length; i++) {
			clNAssociados.add(dlmNAssociados.get(nAssociadosSelecionados[i]));
		}

		try {
			if (isMusicaSelecionada) {
				Musica m = (Musica) cboMusicas.getSelectedItem();
				for (Object momento : clNAssociados) {
					m.getMomentos().add(momento);
					dlmAssociados.addElement(momento);
					dlmNAssociados.removeElement(momento);
				}
				MusicaDao md = MusicaDaoFactory.createMusicaDao();
				md.salvar(m);

			} else {
				Momento m = (Momento) cboMomentos.getSelectedItem();
				for (Object momento : clNAssociados) {
					m.getMusicas().add(momento);
					dlmAssociados.addElement(momento);
					dlmNAssociados.removeElement(momento);
				}
				MomentoDao md = MomentoDaoFactory.createMomentoDao();
				md.salvar(m);
			}
			JOptionPane.showMessageDialog(this, "A associação foi realizada com sucesso", "Informação", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Ocorreu um erro durante a tentativa de acesso ao banco de " + (isMusicaSelecionada ? "Musicas" : "Momentos") + ", detalhes: " + e.getLocalizedMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	private void remover(){
		Collection clAssociados = new ArrayList();
		
		int[] associadosSelecionados = jlAssociados.getSelectedIndices();
		for (int i = 0; i < associadosSelecionados.length; i++) {
			clAssociados.add(dlmAssociados.get(associadosSelecionados[i]));
		}

		try {
			if (isMusicaSelecionada) {
				Musica m = (Musica) cboMusicas.getSelectedItem();
				for (Object momento : clAssociados) {
					m.getMomentos().remove(momento);
					dlmAssociados.removeElement(momento);
					dlmNAssociados.addElement(momento);
				}
				MusicaDao md = MusicaDaoFactory.createMusicaDao();
				md.salvar(m);

			} else {
				Momento m = (Momento) cboMomentos.getSelectedItem();
				for (Object momento : clAssociados) {
					m.getMusicas().remove(momento);
					dlmAssociados.removeElement(momento);
					dlmNAssociados.addElement(momento);
				}
				MomentoDao md = MomentoDaoFactory.createMomentoDao();
				md.salvar(m);
			}
			JOptionPane.showMessageDialog(this, "A desassociação foi realizada com sucesso", "Informação", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Ocorreu um erro durante a tentativa de acesso ao banco de " + (isMusicaSelecionada ? "Musicas" : "Momentos") + ", detalhes: " + e.getLocalizedMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Create the panel.
	 * @param menuPrincipal 
	 */
	public MusicaMomentoPanel(MenuPrincipal menuPrincipal) {
		this.menuPrincipal = menuPrincipal;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblMomento = new JLabel("Momento");
		GridBagConstraints gbc_lblMomento = new GridBagConstraints();
		gbc_lblMomento.insets = new Insets(0, 0, 5, 5);
		gbc_lblMomento.anchor = GridBagConstraints.EAST;
		gbc_lblMomento.gridx = 0;
		gbc_lblMomento.gridy = 0;
		add(lblMomento, gbc_lblMomento);
		
		cboMomentos = new JComboBox();
		cboMomentos.setModel(new DefaultComboBoxModel(new String[] {"teste", "teste2", "teste3"}));
		GridBagConstraints gbc_cboMomento = new GridBagConstraints();
		gbc_cboMomento.insets = new Insets(0, 0, 5, 5);
		gbc_cboMomento.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboMomento.gridx = 1;
		gbc_cboMomento.gridy = 0;
		add(cboMomentos, gbc_cboMomento);
		
		JLabel lblMsica = new JLabel("M\u00FAsica");
		GridBagConstraints gbc_lblMsica = new GridBagConstraints();
		gbc_lblMsica.insets = new Insets(0, 0, 5, 5);
		gbc_lblMsica.anchor = GridBagConstraints.EAST;
		gbc_lblMsica.gridx = 3;
		gbc_lblMsica.gridy = 0;
		add(lblMsica, gbc_lblMsica);
		
		cboMusicas = new JComboBox();
		GridBagConstraints gbc_cboMusica = new GridBagConstraints();
		gbc_cboMusica.insets = new Insets(0, 0, 5, 5);
		gbc_cboMusica.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboMusica.gridx = 5;
		gbc_cboMusica.gridy = 0;
		add(cboMusicas, gbc_cboMusica);
		
		JLabel label = new JLabel("     ");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 7;
		gbc_label.gridy = 0;
		add(label, gbc_label);
		
		JLabel lblNoAssociadas = new JLabel("N\u00E3o Associado");
		GridBagConstraints gbc_lblNoAssociadas = new GridBagConstraints();
		gbc_lblNoAssociadas.insets = new Insets(0, 0, 5, 5);
		gbc_lblNoAssociadas.gridx = 1;
		gbc_lblNoAssociadas.gridy = 1;
		add(lblNoAssociadas, gbc_lblNoAssociadas);
		
		JLabel lblMsicasAssociadas = new JLabel("Associado");
		GridBagConstraints gbc_lblMsicasAssociadas = new GridBagConstraints();
		gbc_lblMsicasAssociadas.insets = new Insets(0, 0, 5, 5);
		gbc_lblMsicasAssociadas.gridx = 5;
		gbc_lblMsicasAssociadas.gridy = 1;
		add(lblMsicasAssociadas, gbc_lblMsicasAssociadas);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);
		
		jlNAssociados = new JList();
		scrollPane.setViewportView(jlNAssociados);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 3;
		gbc_panel.gridy = 2;
		add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		btnAdd = new JButton("");
		btnAdd.setIcon(new ImageIcon(MusicaMomentoPanel.class.getResource("/org/tango-project/tango-icon-theme/16x16/actions/go-last.png")));
		panel.add(btnAdd);
		
		btnRemove = new JButton("");
		btnRemove.setIcon(new ImageIcon(MusicaMomentoPanel.class.getResource("/org/tango-project/tango-icon-theme/16x16/actions/go-first.png")));
		panel.add(btnRemove);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 5;
		gbc_scrollPane_1.gridy = 2;
		add(scrollPane_1, gbc_scrollPane_1);
		
		jlAssociados = new JList();
		scrollPane_1.setViewportView(jlAssociados);
	}
}
