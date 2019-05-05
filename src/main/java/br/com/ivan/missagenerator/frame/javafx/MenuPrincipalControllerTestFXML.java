/**
 * Sample Skeleton for 'vemlouvarfreeform.fxml' Controller Class
 */

package br.com.ivan.missagenerator.frame.javafx;

import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MenuPrincipalControllerTestFXML {

    @FXML
    private AutoCompleteTextField textFiled1;

    @FXML
    private AutoCompleteTextArea txtArea1;
	
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	txtArea1.getEntries().addAll(Arrays.asList("AA", "AB", "AC","BCA"));
    	
    	
    }

}