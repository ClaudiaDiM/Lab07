package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.Poweroutages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Nerc> cmbNerc;

    @FXML
    private TextField txtYears;

    @FXML
    private TextField txtHours;

    @FXML
    private TextArea txtResult;
    
    private Model model;

    @FXML
    void doRun(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	try {
    		
    		Nerc nerc = cmbNerc.getSelectionModel().getSelectedItem();
    		if(nerc == null) {
    			txtResult.setText("Select a NERC (area identifier)");
				return;
    		}
    		
    		int Ymax = Integer.parseInt(txtYears.getText());
    		int Hmax = Integer.parseInt(txtHours.getText());
    		
    		int yearListSize = model.getYearList().size();
    		if(Ymax <= 0 || Ymax > yearListSize) {
    			txtResult.setText("Select a number of years in range [1, " + yearListSize + "]");
				return;
    		}
    		
    		if (Hmax <= 0) {
				txtResult.setText("Select a number of hours greater than 0");
				return;
			}
    		
    		txtResult.setText(
					String.format("Computing the worst case analysis... for %d hours and %d years", Hmax, Ymax));
			List<Poweroutages> worstCase = model.getWorstCase(Ymax, Hmax, nerc);

			txtResult.clear();
			txtResult.appendText("Tot people affected: " + model.sumAffectedPeople(worstCase) + "\n");
			txtResult.appendText("Tot hours of outage: " + model.sumOutageHours(worstCase) + "\n");

			for (Poweroutages ee : worstCase) {
				txtResult.appendText(String.format("%d %s %s %d %d", ee.getYear(), ee.getOutageStart(),
						ee.getOutageEnd(), ee.getOutageDuration(), ee.getAffectedPeople()));
				txtResult.appendText("\n");
			}

    		
    		
    		
    	}catch (NumberFormatException e) {
			txtResult.setText("Insert a valid number of years and of hours");
		}
    	
    	

    }

    @FXML
    void initialize() {
        assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Nerc> nercList = model.getNercList();
    	
    	cmbNerc.getItems().addAll(nercList);
    }
}
