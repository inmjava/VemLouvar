package br.com.ivan.missagenerator.frame;

import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.com.ivan.missagenerator.panel.CadastroMomentos;
import br.com.ivan.missagenerator.panel.CadastroMusicasPanel;
import br.com.ivan.missagenerator.panel.CriarMissaPanel;
import br.com.ivan.missagenerator.panel.MissaFreeForm;
import br.com.ivan.missagenerator.panel.MusicaMomentoPanel;
import br.com.ivan.missagenerator.panel.NomePanel;
import br.com.ivan.missagenerator.panel.PesquisarMusicaPanel;
import javax.swing.JProgressBar;

public class MenuPrincipal extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuPrincipal frame = new MenuPrincipal();
					frame.abrirPanel(NomePanel.FAZER_MISSA2);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public JPanel abrirPanel(int qual){
		Painel panelQual = null;
		try {
			switch (qual) {
			case NomePanel.CADASTRO_MOMENTO:
				panelQual = new CadastroMomentos(this);
				break;
			case NomePanel.CADASTRO_MUSICA:	
				panelQual = new CadastroMusicasPanel(this);
				break;
			case NomePanel.FAZER_MISSA:
				panelQual = new CriarMissaPanel(this);
				break;
			case NomePanel.FAZER_MISSA2:
				panelQual = new MissaFreeForm(this);
				break;
			case NomePanel.MUSICA_MOMENTO:
				panelQual = new MusicaMomentoPanel(this);
				break;
			case NomePanel.PESQUISAR_MUSICA:
				panelQual = new PesquisarMusicaPanel(this);
				break;
			
				
			}
			panelQual.refreshValues();
			this.setContentPane((JPanel)panelQual);
			this.getContentPane().revalidate();
			if(panelQual instanceof MissaFreeForm){
				((MissaFreeForm)panelQual).makeMissaFocus();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "ERRO: " + e.getLocalizedMessage());
		}
		return (JPanel) panelQual;
	}

	/**
	 * Create the frame.
	 */
	public MenuPrincipal() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		setTitle("Vem Louvar");
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 720);
		
		setIconImage(new ImageIcon("images/icon.png").getImage());
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnArquivo_1 = new JMenu("Arquivo");
		menuBar.add(mnArquivo_1);
		
		JMenuItem mntmSair = new JMenuItem("Sair");
		mnArquivo_1.add(mntmSair);
		mntmSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		JMenu mnNewMenu = new JMenu("Louvor");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmFazerMissa = new JMenuItem("Criar Louvor");
		mntmFazerMissa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirPanel(NomePanel.FAZER_MISSA);
			}
		});
		mnNewMenu.add(mntmFazerMissa);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Louvor Free Form");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirPanel(NomePanel.FAZER_MISSA2);
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenu mnArquivo = new JMenu("Cadastro");
		menuBar.add(mnArquivo);
		
		JMenuItem mntmCadastroDeMomentos = new JMenuItem("Momentos");
		mntmCadastroDeMomentos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirPanel(NomePanel.CADASTRO_MOMENTO);
			}
		});
		
		JMenuItem mntmCadastroDeMsicas = new JMenuItem("M\u00FAsicas");
		mntmCadastroDeMsicas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirPanel(NomePanel.CADASTRO_MUSICA);
			}
		});
		mnArquivo.add(mntmCadastroDeMsicas);
		mnArquivo.add(mntmCadastroDeMomentos);
		
		JMenuItem mntmMsicaMomento = new JMenuItem("Momentos M\u00FAsicas");
		mntmMsicaMomento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirPanel(NomePanel.MUSICA_MOMENTO);
			}
		});
		mnArquivo.add(mntmMsicaMomento);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout());
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setIcon(new ImageIcon("images/splash.jpg"));
		contentPane.add(lblNewLabel);
		
		
	}

}
