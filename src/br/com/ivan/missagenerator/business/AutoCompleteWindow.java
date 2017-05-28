package br.com.ivan.missagenerator.business;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.apache.commons.lang3.StringUtils;

public class AutoCompleteWindow extends JWindow {

	public interface CustomSearch {
		public ArrayList<String> customSearcher();
		public void autocompleteListener(String selecion);
	}

	private JList<String> jlist;
	private List<String> baseData;
	private int selected;
	private JTextField jTextArea;
	private int hg;
	private CustomSearch cs;
	private boolean searchByKeyReleased;

	public AutoCompleteWindow(JTextField component, int hg, boolean searchByKeyReleased) {
		this(component, hg, true, null);
	}

	public AutoCompleteWindow(JTextField component, int hg, boolean searchByKeyReleased, CustomSearch cs) {
		jTextArea = component;
		this.hg = hg;
		this.cs = cs;
		this.searchByKeyReleased = searchByKeyReleased;
		setVisible(false);
		jlist = new JList<>();
		jlist.setFont(jlist.getFont().deriveFont(Font.PLAIN));
		jlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				doAutoComplete(jlist.getSelectedValue().toString());
			}
		});
		jTextArea.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_UP:
					AutoCompleteWindow.this.selectPrevious();
					break;
				case KeyEvent.VK_DOWN:
					if(!AutoCompleteWindow.this.isVisible()){
						AutoCompleteWindow.this.searchAutoComplete(jTextArea.getText());
					} else {
						AutoCompleteWindow.this.selectNext();
					}
					// handle down
					break;
				case KeyEvent.VK_LEFT:
					// handle left
					break;
				case KeyEvent.VK_RIGHT:
					// handle right
					break;
				case KeyEvent.VK_ENTER:
					// handle enter
					break;
				case KeyEvent.VK_ESCAPE:
					AutoCompleteWindow.this.setVisible(false);
					break;
				default:
					if(AutoCompleteWindow.this.searchByKeyReleased){
						AutoCompleteWindow.this.searchAutoComplete(jTextArea.getText());
					}
					break;
				}
			};
		});
		jTextArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAutoComplete(jlist.getSelectedValue().toString());
			}
		});
		jTextArea.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent arg0) {
			}

			public void ancestorMoved(AncestorEvent arg0) {
				AutoCompleteWindow.this.setVisible(false);
			}

			public void ancestorRemoved(AncestorEvent arg0) {
			}
		});
		add(new JScrollPane(jlist));
		baseData = Arrays.asList(new String[] { "Abati�", "Adrian�polis", "Agudos do Sul", "Almirante Tamandar�",
				"Altamira do Paran�", "Alto Para�so", "Alto Paran�", "Alto Piquiri", "Alt�nia", "Alvorada do Sul",
				"Amapor�", "Amp�re", "Anahy", "Andir�", "�ngulo", "Antonina", "Ant�nio Olinto", "Apucarana",
				"Arapongas", "Arapoti", "Arapu�", "Araruna", "Arauc�ria", "Ariranha do Iva�", "Assa�",
				"Assis Chateaubriand", "Astorga", "Atalaia", "Balsa Nova", "Bandeirantes", "Barbosa Ferraz",
				"Barra do Jacar�", "Barrac�o", "Bela Vista da Caroba", "Bela Vista do Para�so", "Bituruna",
				"Boa Esperan�a", "Boa Esperan�a do Igua�u", "Boa Ventura de S�o Roque", "Boa Vista da Aparecida",
				"Bocai�va do Sul", "Bom Jesus do Sul", "Bom Sucesso", "Bom Sucesso do Sul", "Borraz�polis", "Braganey",
				"Brasil�ndia do Sul", "Cafeara", "Cafel�ndia", "Cafezal do Sul", "Calif�rnia", "Cambar�", "Camb�",
				"Cambira", "Campina da Lagoa", "Campina do Sim�o", "Campina Grande do Sul", "Campo Bonito",
				"Campo do Tenente", "Campo Largo", "Campo Magro", "Campo Mour�o", "C�ndido de Abreu", "Cand�i",
				"Cantagalo", "Capanema", "Capit�o Le�nidas Marques", "Carambe�", "Carl�polis", "Cascavel", "Castro",
				"Catanduvas", "Centen�rio do Sul", "Cerro Azul", "C�u Azul", "Chopinzinho", "Cianorte", "Cidade Ga�cha",
				"Clevel�ndia", "Colombo", "Colorado", "Congonhinhas", "Conselheiro Mairinck", "Contenda", "Corb�lia",
				"Corn�lio Proc�pio", "Coronel Domingos Soares", "Coronel Vivida", "Corumbata� do Sul", "Cruz Machado",
				"Cruzeiro do Igua�u", "Cruzeiro do Oeste", "Cruzeiro do Sul", "Cruzmaltina", "Curitiba", "Curi�va",
				"Diamante do Norte", "Diamante do Sul", "Diamante d`Oeste", "Dois Vizinhos", "Douradina",
				"Doutor Camargo", "Doutor Ulysses", "En�as Marques", "Engenheiro Beltr�o", "Entre Rios do Oeste",
				"Esperan�a Nova", "Espig�o Alto do Igua�u", "Farol", "Faxinal", "Fazenda Rio Grande", "F�nix",
				"Fernandes Pinheiro", "Figueira", "Flor da Serra do Sul", "Flora�", "Floresta", "Florest�polis",
				"Fl�rida", "Formosa do Oeste", "Foz do Igua�u", "Foz do Jord�o", "Francisco Alves", "Francisco Beltr�o",
				"General Carneiro", "Godoy Moreira", "Goioer�", "Goioxim", "Grandes Rios", "Gua�ra", "Guaira��",
				"Guamiranga", "Guapirama", "Guaporema", "Guaraci", "Guarania�u", "Guarapuava", "Guaraque�aba",
				"Guaratuba", "Hon�rio Serpa", "Ibaiti", "Ibema", "Ibipor�", "Icara�ma", "Iguara�u", "Iguatu", "Imba�",
				"Imbituva", "In�cio Martins", "Inaj�", "Indian�polis", "Ipiranga", "Ipor�", "Iracema do Oeste", "Irati",
				"Iretama", "Itaguaj�", "Itaipul�ndia", "Itambarac�", "Itamb�", "Itapejara d`Oeste", "Itaperu�u",
				"Ita�na do Sul", "Iva�", "Ivaipor�", "Ivat�", "Ivatuba", "Jaboti", "Jacarezinho", "Jaguapit�",
				"Jaguaria�va", "Jandaia do Sul", "Jani�polis", "Japira", "Japur�", "Jardim Alegre", "Jardim Olinda",
				"Jataizinho", "Jesu�tas", "Joaquim T�vora", "Jundia� do Sul", "Juranda", "Jussara", "Kalor�", "Lapa",
				"Laranjal", "Laranjeiras do Sul", "Le�polis", "Lidian�polis", "Lindoeste", "Loanda", "Lobato",
				"Londrina", "Luiziana", "Lunardelli", "Lupion�polis", "Mallet", "Mambor�", "Mandagua�u", "Mandaguari",
				"Mandirituba", "Manfrin�polis", "Mangueirinha", "Manoel Ribas", "Marechal C�ndido Rondon",
				"Maria Helena", "Marialva", "Maril�ndia do Sul", "Marilena", "Mariluz", "Maring�", "Mari�polis",
				"Marip�", "Marmeleiro", "Marquinho", "Marumbi", "Matel�ndia", "Matinhos", "Mato Rico", "Mau� da Serra",
				"Medianeira", "Mercedes", "Mirador", "Miraselva", "Missal", "Moreira Sales", "Morretes",
				"Munhoz de Melo", "Nossa Senhora das Gra�as", "Nova Alian�a do Iva�", "Nova Am�rica da Colina",
				"Nova Aurora", "Nova Cantu", "Nova Esperan�a", "Nova Esperan�a do Sudoeste", "Nova F�tima",
				"Nova Laranjeiras", "Nova Londrina", "Nova Ol�mpia", "Nova Prata do Igua�u", "Nova Santa B�rbara",
				"Nova Santa Rosa", "Nova Tebas", "Novo Itacolomi", "Ortigueira", "Ourizona", "Ouro Verde do Oeste",
				"Pai�andu", "Palmas", "Palmeira", "Palmital", "Palotina", "Para�so do Norte", "Paranacity", "Paranagu�",
				"Paranapoema", "Paranava�", "Pato Bragado", "Pato Branco", "Paula Freitas", "Paulo Frontin", "Peabiru",
				"Perobal", "P�rola", "P�rola d`Oeste", "Pi�n", "Pinhais", "Pinhal de S�o Bento", "Pinhal�o", "Pinh�o",
				"Pira� do Sul", "Piraquara", "Pitanga", "Pitangueiras", "Planaltina do Paran�", "Planalto",
				"Ponta Grossa", "Pontal do Paran�", "Porecatu", "Porto Amazonas", "Porto Barreiro", "Porto Rico",
				"Porto Vit�ria", "Prado Ferreira", "Pranchita", "Presidente Castelo Branco", "Primeiro de Maio",
				"Prudent�polis", "Quarto Centen�rio", "Quatigu�", "Quatro Barras", "Quatro Pontes", "Quedas do Igua�u",
				"Quer�ncia do Norte", "Quinta do Sol", "Quitandinha", "Ramil�ndia", "Rancho Alegre",
				"Rancho Alegre d`Oeste", "Realeza", "Rebou�as", "Renascen�a", "Reserva", "Reserva do Igua�u",
				"Ribeir�o Claro", "Ribeir�o do Pinhal", "Rio Azul", "Rio Bom", "Rio Bonito do Igua�u",
				"Rio Branco do Iva�", "Rio Branco do Sul", "Rio Negro", "Rol�ndia", "Roncador", "Rondon",
				"Ros�rio do Iva�", "Sab�udia", "Salgado Filho", "Salto do Itarar�", "Salto do Lontra", "Santa Am�lia",
				"Santa Cec�lia do Pav�o", "Santa Cruz de Monte Castelo", "Santa F�", "Santa Helena", "Santa In�s",
				"Santa Isabel do Iva�", "Santa Izabel do Oeste", "Santa L�cia", "Santa Maria do Oeste", "Santa Mariana",
				"Santa M�nica", "Santa Tereza do Oeste", "Santa Terezinha de Itaipu", "Santana do Itarar�",
				"Santo Ant�nio da Platina", "Santo Ant�nio do Caiu�", "Santo Ant�nio do Para�so",
				"Santo Ant�nio do Sudoeste", "Santo In�cio", "S�o Carlos do Iva�", "S�o Jer�nimo da Serra", "S�o Jo�o",
				"S�o Jo�o do Caiu�", "S�o Jo�o do Iva�", "S�o Jo�o do Triunfo", "S�o Jorge do Iva�",
				"S�o Jorge do Patroc�nio", "S�o Jorge d`Oeste", "S�o Jos� da Boa Vista", "S�o Jos� das Palmeiras",
				"S�o Jos� dos Pinhais", "S�o Manoel do Paran�", "S�o Mateus do Sul", "S�o Miguel do Igua�u",
				"S�o Pedro do Igua�u", "S�o Pedro do Iva�", "S�o Pedro do Paran�", "S�o Sebasti�o da Amoreira",
				"S�o Tom�", "Sapopema", "Sarandi", "Saudade do Igua�u", "Seng�s", "Serran�polis do Igua�u", "Sertaneja",
				"Sertan�polis", "Siqueira Campos", "Sulina", "Tamarana", "Tamboara", "Tapejara", "Tapira",
				"Teixeira Soares", "Tel�maco Borba", "Terra Boa", "Terra Rica", "Terra Roxa", "Tibagi",
				"Tijucas do Sul", "Toledo", "Tomazina", "Tr�s Barras do Paran�", "Tunas do Paran�", "Tuneiras do Oeste",
				"Tup�ssi", "Turvo", "Ubirat�", "Umuarama", "Uni�o da Vit�ria", "Uniflor", "Ura�", "Ventania",
				"Vera Cruz do Oeste", "Ver�", "Virmond", "Vitorino", "Wenceslau Braz", "Xambr�" });
		setValues(baseData);
	}

	public void doAutoComplete(String returnStr) {
		jTextArea.setText(returnStr);
		setVisible(false);
		if(this.cs != null){
			this.cs.autocompleteListener(returnStr);
		}
	}

	public void setValues(List<String> listValues) {
		this.baseData = listValues;
	}

	public void setValues(String[] arrValues) {
		setValues(Arrays.asList(arrValues));
	}

	public void searchAutoComplete(String text) {
		setBounds(jTextArea.getLocationOnScreen().x, jTextArea.getLocationOnScreen().y + jTextArea.getHeight(),
				jTextArea.getWidth(), this.hg);
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		if (this.cs != null) {
			ArrayList<String> results = this.cs.customSearcher();
			for (String result : results) {
				listModel.addElement(result);
			}
		} else {
			for (String data : baseData) {
				String str1 = Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				String str2 = Normalizer.normalize(data, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				if (StringUtils.containsIgnoreCase(str2, str1)) {
					listModel.addElement("<html>" + data.replaceAll("(?i)" + text, "<b>$0</b>") + "</html>");
				}
			}
		}
		jlist.setModel(listModel);
		selected = 0;
		doSelect();
		setVisible(true);
	}

	private void doSelect() {
		if (jlist.getModel().getSize() == 0) {
			return;
		}
		if (selected == 0) {
			jlist.setSelectedIndex(0);
			return;
		}
		selected = (selected + jlist.getModel().getSize()) % jlist.getModel().getSize();
		jlist.setSelectedIndex(selected);
	}

	public void selectNext() {
		selected++;
		doSelect();
	}

	public void selectPrevious() {
		selected--;
		doSelect();
	}

	public String closeWindow() {
		String selectedValue = jlist.getSelectedValue().toString();
		setVisible(false);
		return selectedValue;
	}
}