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
		baseData = Arrays.asList(new String[] { "Abatiá", "Adrianópolis", "Agudos do Sul", "Almirante Tamandaré",
				"Altamira do Paraná", "Alto Paraíso", "Alto Paraná", "Alto Piquiri", "Altônia", "Alvorada do Sul",
				"Amaporã", "Ampére", "Anahy", "Andirá", "Ângulo", "Antonina", "Antônio Olinto", "Apucarana",
				"Arapongas", "Arapoti", "Arapuã", "Araruna", "Araucária", "Ariranha do Ivaí", "Assaí",
				"Assis Chateaubriand", "Astorga", "Atalaia", "Balsa Nova", "Bandeirantes", "Barbosa Ferraz",
				"Barra do Jacaré", "Barracão", "Bela Vista da Caroba", "Bela Vista do Paraíso", "Bituruna",
				"Boa Esperança", "Boa Esperança do Iguaçu", "Boa Ventura de São Roque", "Boa Vista da Aparecida",
				"Bocaiúva do Sul", "Bom Jesus do Sul", "Bom Sucesso", "Bom Sucesso do Sul", "Borrazópolis", "Braganey",
				"Brasilândia do Sul", "Cafeara", "Cafelândia", "Cafezal do Sul", "Califórnia", "Cambará", "Cambé",
				"Cambira", "Campina da Lagoa", "Campina do Simão", "Campina Grande do Sul", "Campo Bonito",
				"Campo do Tenente", "Campo Largo", "Campo Magro", "Campo Mourão", "Cândido de Abreu", "Candói",
				"Cantagalo", "Capanema", "Capitão Leônidas Marques", "Carambeí", "Carlópolis", "Cascavel", "Castro",
				"Catanduvas", "Centenário do Sul", "Cerro Azul", "Céu Azul", "Chopinzinho", "Cianorte", "Cidade Gaúcha",
				"Clevelândia", "Colombo", "Colorado", "Congonhinhas", "Conselheiro Mairinck", "Contenda", "Corbélia",
				"Cornélio Procópio", "Coronel Domingos Soares", "Coronel Vivida", "Corumbataí do Sul", "Cruz Machado",
				"Cruzeiro do Iguaçu", "Cruzeiro do Oeste", "Cruzeiro do Sul", "Cruzmaltina", "Curitiba", "Curiúva",
				"Diamante do Norte", "Diamante do Sul", "Diamante d`Oeste", "Dois Vizinhos", "Douradina",
				"Doutor Camargo", "Doutor Ulysses", "Enéas Marques", "Engenheiro Beltrão", "Entre Rios do Oeste",
				"Esperança Nova", "Espigão Alto do Iguaçu", "Farol", "Faxinal", "Fazenda Rio Grande", "Fênix",
				"Fernandes Pinheiro", "Figueira", "Flor da Serra do Sul", "Floraí", "Floresta", "Florestópolis",
				"Flórida", "Formosa do Oeste", "Foz do Iguaçu", "Foz do Jordão", "Francisco Alves", "Francisco Beltrão",
				"General Carneiro", "Godoy Moreira", "Goioerê", "Goioxim", "Grandes Rios", "Guaíra", "Guairaçá",
				"Guamiranga", "Guapirama", "Guaporema", "Guaraci", "Guaraniaçu", "Guarapuava", "Guaraqueçaba",
				"Guaratuba", "Honório Serpa", "Ibaiti", "Ibema", "Ibiporã", "Icaraíma", "Iguaraçu", "Iguatu", "Imbaú",
				"Imbituva", "Inácio Martins", "Inajá", "Indianópolis", "Ipiranga", "Iporã", "Iracema do Oeste", "Irati",
				"Iretama", "Itaguajé", "Itaipulândia", "Itambaracá", "Itambé", "Itapejara d`Oeste", "Itaperuçu",
				"Itaúna do Sul", "Ivaí", "Ivaiporã", "Ivaté", "Ivatuba", "Jaboti", "Jacarezinho", "Jaguapitã",
				"Jaguariaíva", "Jandaia do Sul", "Janiópolis", "Japira", "Japurá", "Jardim Alegre", "Jardim Olinda",
				"Jataizinho", "Jesuítas", "Joaquim Távora", "Jundiaí do Sul", "Juranda", "Jussara", "Kaloré", "Lapa",
				"Laranjal", "Laranjeiras do Sul", "Leópolis", "Lidianópolis", "Lindoeste", "Loanda", "Lobato",
				"Londrina", "Luiziana", "Lunardelli", "Lupionópolis", "Mallet", "Mamborê", "Mandaguaçu", "Mandaguari",
				"Mandirituba", "Manfrinópolis", "Mangueirinha", "Manoel Ribas", "Marechal Cândido Rondon",
				"Maria Helena", "Marialva", "Marilândia do Sul", "Marilena", "Mariluz", "Maringá", "Mariópolis",
				"Maripá", "Marmeleiro", "Marquinho", "Marumbi", "Matelândia", "Matinhos", "Mato Rico", "Mauá da Serra",
				"Medianeira", "Mercedes", "Mirador", "Miraselva", "Missal", "Moreira Sales", "Morretes",
				"Munhoz de Melo", "Nossa Senhora das Graças", "Nova Aliança do Ivaí", "Nova América da Colina",
				"Nova Aurora", "Nova Cantu", "Nova Esperança", "Nova Esperança do Sudoeste", "Nova Fátima",
				"Nova Laranjeiras", "Nova Londrina", "Nova Olímpia", "Nova Prata do Iguaçu", "Nova Santa Bárbara",
				"Nova Santa Rosa", "Nova Tebas", "Novo Itacolomi", "Ortigueira", "Ourizona", "Ouro Verde do Oeste",
				"Paiçandu", "Palmas", "Palmeira", "Palmital", "Palotina", "Paraíso do Norte", "Paranacity", "Paranaguá",
				"Paranapoema", "Paranavaí", "Pato Bragado", "Pato Branco", "Paula Freitas", "Paulo Frontin", "Peabiru",
				"Perobal", "Pérola", "Pérola d`Oeste", "Piên", "Pinhais", "Pinhal de São Bento", "Pinhalão", "Pinhão",
				"Piraí do Sul", "Piraquara", "Pitanga", "Pitangueiras", "Planaltina do Paraná", "Planalto",
				"Ponta Grossa", "Pontal do Paraná", "Porecatu", "Porto Amazonas", "Porto Barreiro", "Porto Rico",
				"Porto Vitória", "Prado Ferreira", "Pranchita", "Presidente Castelo Branco", "Primeiro de Maio",
				"Prudentópolis", "Quarto Centenário", "Quatiguá", "Quatro Barras", "Quatro Pontes", "Quedas do Iguaçu",
				"Querência do Norte", "Quinta do Sol", "Quitandinha", "Ramilândia", "Rancho Alegre",
				"Rancho Alegre d`Oeste", "Realeza", "Rebouças", "Renascença", "Reserva", "Reserva do Iguaçu",
				"Ribeirão Claro", "Ribeirão do Pinhal", "Rio Azul", "Rio Bom", "Rio Bonito do Iguaçu",
				"Rio Branco do Ivaí", "Rio Branco do Sul", "Rio Negro", "Rolândia", "Roncador", "Rondon",
				"Rosário do Ivaí", "Sabáudia", "Salgado Filho", "Salto do Itararé", "Salto do Lontra", "Santa Amélia",
				"Santa Cecília do Pavão", "Santa Cruz de Monte Castelo", "Santa Fé", "Santa Helena", "Santa Inês",
				"Santa Isabel do Ivaí", "Santa Izabel do Oeste", "Santa Lúcia", "Santa Maria do Oeste", "Santa Mariana",
				"Santa Mônica", "Santa Tereza do Oeste", "Santa Terezinha de Itaipu", "Santana do Itararé",
				"Santo Antônio da Platina", "Santo Antônio do Caiuá", "Santo Antônio do Paraíso",
				"Santo Antônio do Sudoeste", "Santo Inácio", "São Carlos do Ivaí", "São Jerônimo da Serra", "São João",
				"São João do Caiuá", "São João do Ivaí", "São João do Triunfo", "São Jorge do Ivaí",
				"São Jorge do Patrocínio", "São Jorge d`Oeste", "São José da Boa Vista", "São José das Palmeiras",
				"São José dos Pinhais", "São Manoel do Paraná", "São Mateus do Sul", "São Miguel do Iguaçu",
				"São Pedro do Iguaçu", "São Pedro do Ivaí", "São Pedro do Paraná", "São Sebastião da Amoreira",
				"São Tomé", "Sapopema", "Sarandi", "Saudade do Iguaçu", "Sengés", "Serranópolis do Iguaçu", "Sertaneja",
				"Sertanópolis", "Siqueira Campos", "Sulina", "Tamarana", "Tamboara", "Tapejara", "Tapira",
				"Teixeira Soares", "Telêmaco Borba", "Terra Boa", "Terra Rica", "Terra Roxa", "Tibagi",
				"Tijucas do Sul", "Toledo", "Tomazina", "Três Barras do Paraná", "Tunas do Paraná", "Tuneiras do Oeste",
				"Tupãssi", "Turvo", "Ubiratã", "Umuarama", "União da Vitória", "Uniflor", "Uraí", "Ventania",
				"Vera Cruz do Oeste", "Verê", "Virmond", "Vitorino", "Wenceslau Braz", "Xambrê" });
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