package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Register {

	private static Stage stage;

	public Register() {
		try {
			stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("register.fxml"));
			loader.setController(new Controller());
			Parent root = loader.load();
			Scene scene = new Scene(root, 525, 350);
			scene.getStylesheets().add(getClass().getResource("application.css").toString());
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class Controller implements Initializable {

		@FXML
		private TextField usernameTextField;
		@FXML
		private TextField emailTextField;
		@FXML
		private PasswordField passwordField;
		@FXML
		private PasswordField confirmPasswordField;
		@FXML
		private CheckBox rememberMeCheckBox;

		@Override
		public void initialize(URL location, ResourceBundle resources) {
		}

		@FXML
		public void registerButtonClicked(ActionEvent event) {
			//TODO: Check if other fields are valid
			if (passwordField.getText().equals(confirmPasswordField.getText())) {
				Grid.initiateBoincRpc();
				new InitialProjectRegistration(usernameTextField.getText(), emailTextField.getText(), passwordField.getText());
				Register.stage.close();
			} else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setContentText("'Password' and 'Confirm Password' do no match!");
				alert.showAndWait();
			}
		}
	}
}