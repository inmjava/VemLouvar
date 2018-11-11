package br.com.ivan.missagenerator.test;

import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TesteAutoComplete extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TesteAutoComplete frame = new TesteAutoComplete();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TesteAutoComplete() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
				new Object[][] {
					{"Esta � a linha n�mero 01"},
					{"Esta � a linha n�mero 02"},
					{"Esta � a linha n�mero 03"},
					{"Esta � a linha n�mero 04"},
					{"Esta � a linha n�mero 05"},
					{"Esta � a linha n�mero 06"},
					{"Esta � a linha n�mero 07"},
					{"Esta � a linha n�mero 08"},
					{"Esta � a linha n�mero 09"},
					{"Esta � a linha n�mero 10"},
					{"Esta � a linha n�mero 11"},
					{"Esta � a linha n�mero 12"},
					{"Esta � a linha n�mero 13"},
				},
			new String[] {
				"Coluna 1"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(445);
		table.setBounds(12, 27, 385, 192);
		contentPane.add(table);
		String[] strings = new String[] {"Esta \u00E9 a linha n\u00FAmero 01", "Esta \u00E9 a linha n\u00FAmero 02", "Esta \u00E9 a linha n\u00FAmero 03", "Esta \u00E9 a linha n\u00FAmero 04", "Esta \u00E9 a linha n\u00FAmero 05", "Esta \u00E9 a linha n\u00FAmero 06", "Esta \u00E9 a linha n\u00FAmero 07", "Esta \u00E9 a linha n\u00FAmero 08", "Esta \u00E9 a linha n\u00FAmero 09", "Esta \u00E9 a linha n\u00FAmero 10", "Esta \u00E9 a linha n\u00FAmero 11", "Esta \u00E9 a linha n\u00FAmero 12", "Esta \u00E9 a linha n\u00FAmero 13", "Esta \u00E9 a linha n\u00FAmero 14", "Esta \u00E9 a linha n\u00FAmero 15", "Esta \u00E9 a linha n\u00FAmero 16", "Esta \u00E9 a linha n\u00FAmero 17", "Esta \u00E9 a linha n\u00FAmero 18"};
		DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(strings);
	}
}
