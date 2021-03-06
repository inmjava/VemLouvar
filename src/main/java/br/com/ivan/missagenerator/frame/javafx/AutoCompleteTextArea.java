package br.com.ivan.missagenerator.frame.javafx;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.ivan.missagenerator.business.autocomplete.provider.IvanContainsProvider;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Path;
import javafx.stage.Window;

/**
 * This class is a TextField which implements an "autocomplete" functionality,
 * based on a supplied list of entries.
 * 
 * @author Caleb Brinkman
 */
public class AutoCompleteTextArea extends TextArea {
	/** The existing autocomplete entries. */
	private final SortedSet<String> entries;
	/** The popup used to select an entry. */
	private ContextMenu entriesPopup;
	private List<String> completions;

	/** Construct a new AutoCompleteTextField. */
	public AutoCompleteTextArea() {
		super();
		entries = new TreeSet<>();
		entriesPopup = new ContextMenu();
		this.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.SPACE && event.isControlDown()) {
					if (getText().length() == 0) {
						entriesPopup.hide();
					} else {
						List<String> searchResult = getListaDeSugestoes();
						searchResult.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));
						populatePopup(searchResult);
						if (!entriesPopup.isShowing()) {
							Path caret = findCaret(AutoCompleteTextArea.this);
							Point2D screenLoc = findScreenLocation(caret);
							entriesPopup.show(AutoCompleteTextArea.this, screenLoc.getX(), screenLoc.getY() + 20);
							Skin<?> skin = entriesPopup.getSkin();
							if (skin != null) {
								Node fstItem = skin.getNode().lookup(".menu-item");
								if (fstItem != null) {
									fstItem.requestFocus();
									entriesPopup.requestFocus();
								}
							}
						}
					}
				}
				if (event.getCode() == KeyCode.ENTER) {
					if (entriesPopup.isShowing()) {
						entriesPopup.fireEvent(null);
					}

				}
			}
		});

//		focusedProperty().addListener(new ChangeListener<Boolean>() {
//			@Override
//			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean,
//					Boolean aBoolean2) {
//				entriesPopup.hide();
//			}
//		});

	}

	private Path findCaret(Parent parent) {
		// Warning: this is an ENORMOUS HACK
		for (Node n : parent.getChildrenUnmodifiable()) {
			if (n instanceof Path) {
				return (Path) n;
			} else if (n instanceof Parent) {
				Path p = findCaret((Parent) n);
				if (p != null) {
					return p;
				}
			}
		}
		return null;
	}

	private Point2D findScreenLocation(Node node) {
		double x = 0;
		double y = 0;
		for (Node n = node; n != null; n = n.getParent()) {
			Bounds parentBounds = n.getBoundsInParent();
			x += parentBounds.getMinX();
			y += parentBounds.getMinY();
		}
		Scene scene = node.getScene();
		x += scene.getX();
		y += scene.getY();
		Window window = scene.getWindow();
		x += window.getX();
		y += window.getY();
		Point2D screenLoc = new Point2D(x, y);
		return screenLoc;
	}

	/**
	 * Get the existing set of autocomplete entries.
	 * 
	 * @return The existing autocomplete entries.
	 */
	public SortedSet<String> getEntries() {
		return entries;
	}

	/**
	 * Populate the entry set with the given search results. Display is limited to
	 * 10 entries, for performance.
	 * 
	 * @param searchResult The set of matching strings.
	 */
	private void populatePopup(List<String> searchResult) {
		List<CustomMenuItem> menuItems = new LinkedList<>();
		// If you'd like more entries, modify this line.
		int maxEntries = 10;
		int count = Math.min(searchResult.size(), maxEntries);
		for (int i = 0; i < count; i++) {
			final String result = searchResult.get(i);
			Label entryLabel = new Label(result);
			CustomMenuItem item = new CustomMenuItem(entryLabel, true);
			item.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					replaceLineContent(result);
					entriesPopup.hide();
				}
			});
			menuItems.add(item);
		}
		entriesPopup.getItems().clear();
		entriesPopup.getItems().addAll(menuItems);

	}

	private List<String> getListaDeSugestoes() {
//		try {
//			return Processador.pesquisarLinks(getLineContent());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return logicaPesquisa(getLineContent());
	}

	private List<String> logicaPesquisa(String entradaLinha) {
		
		List<String> resultado = new ArrayList<>();
		int index = 0;
		
		while (index<getCompletions().size()) {
			String c = getCompletions().get(index);
			
			String str1 = Normalizer.normalize(c, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			String str2 = Normalizer.normalize(entradaLinha, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			
			String retorno = IvanContainsProvider.marcarPosicoes(str2, str1);
			if (retorno != null) {
				String finalInputText = "<html>"+retorno+"</html>";
				resultado.add(finalInputText);
			}
			index++;
		}
		
		return resultado;
	}
	
	private String getLineContent() {
		String str = getText();
		int posicaoCursor = this.getCaretPosition();
		int lineStartOffset = getLineStartOffset(posicaoCursor);
		int lineEndOffset = getLineEndOffset(posicaoCursor);
		return str.substring(lineStartOffset, lineEndOffset);
	}

	private void replaceLineContent(final String result) {
		String str = getText();
		int posicaoCursor = this.getCaretPosition();
		int lineStartOffset = getLineStartOffset(posicaoCursor);
		int lineEndOffset = getLineEndOffset(posicaoCursor);
		setText(str.substring(0, lineStartOffset) + result + str.substring(lineEndOffset));
		positionCaret(lineStartOffset + result.length());
	}

	private int getLineStartOffset(int caretPosition) {
		int lineStartOffset = this.getText().substring(0, caretPosition).lastIndexOf("\n");
		return lineStartOffset == -1 ? 0 : lineStartOffset + 1;
	}

	private int getLineEndOffset(int caretPosition) {
		int lineEndOffSet = this.getText().substring(caretPosition).indexOf("\n");
		return (lineEndOffSet == -1 ? getText().length() : lineEndOffSet + caretPosition);
	}

	public List<String> getCompletions() {
		if(completions == null) {
			completions = FreeFormControllerFXML.listarETratarErrosCompletions();
		}
		return completions;
	}

	public void setCompletions(List<String> completions) {
		this.completions = completions;
	}
}