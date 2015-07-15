/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcustomerudea;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author felipe
 */
public class CustomerSearchController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private TextField textFieldSearch;

    @FXML
    private Button buttonSearch;

    @FXML
    private TableView<Customer> tableView;

    @FXML
    private WebTarget clientTarget;

    @FXML
    private void handleSearchAction() {
        // Handle TextField text changes.
        textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Customer> data = tableView.getItems();
            data.clear();
            Client client = ClientBuilder.newClient();
            client.register(CustomerMessageBodyReader.class);

            System.out.println("TextField Text Changed (newValue: " + newValue + ")");

            if (textFieldSearch.getText().length() > 0) {
                clientTarget = client.target("http://localhost:8080/JavaFxServer/webresources/customer/search/{beginBy}");
                clientTarget = clientTarget.resolveTemplate("beginBy", newValue);
                System.out.println(clientTarget.getUri());
            } else {
                clientTarget = client.target("http://localhost:8080/JavaFxServer/webresources/customer/");
            }

            GenericType<List<Customer>> listc = new GenericType<List<Customer>>() {
            };
            List<Customer> customers = clientTarget.request("application/json").get(listc);

            for (Customer c : customers) {
                data.add(c);
                System.out.println(c.toString());
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handleSearchAction();
    }

}
