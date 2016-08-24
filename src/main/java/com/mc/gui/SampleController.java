/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mc.gui;

import com.google.common.collect.Lists;
import com.mc.MusicService;
import com.mc.Song;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author darek
 */
public class SampleController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(SampleController.class);

    @FXML
    private TextField inputUsername;

    @FXML
    private PasswordField inputPassword;

    @FXML
    private Button buttonSignInOut;

    @FXML
    private ListView listPlaylist;

    private boolean signedIn = false;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private static final List<FileChooser.ExtensionFilter> MUSIC_FILTER = Lists.newArrayList(
            new FileChooser.ExtensionFilter("Music files (*.mp3, *.wav)",
                    Lists.newArrayList("*.mp3", "*.wav")),
            new FileChooser.ExtensionFilter("All files", Lists.newArrayList("*", ".*"))
    );

    @FXML
    protected void clickOpen(ActionEvent event) {
        Window window = ((Control) event.getSource()).getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(MUSIC_FILTER);
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.setTitle("Open music files");
        List<File> files = fc.showOpenMultipleDialog(window);
        if (files != null) {
            ObservableList<Song> observableList =
                    FXCollections.observableList(files.stream().map(Song::new).collect(Collectors.
                            toList())
                    );
            listPlaylist.getItems().addAll(observableList);
            MusicService.getInstance().setSongs(observableList);
        }
    }

    @FXML
    protected void pickedSong(MouseEvent event) {
        if (event.getClickCount() == 2) {
            clickPlay(null);
        }
    }

    @FXML
    protected void clickStop(ActionEvent event) {
        MusicService.getInstance().stop();
    }

    @FXML
    protected void clickNext(ActionEvent event) {
        MusicService.getInstance().next();
    }

    @FXML
    protected void clickPlay(ActionEvent event) {
        int index = listPlaylist.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            MusicService.getInstance().play(index);
        }
    }

    @FXML
    protected void clickSignInOut(ActionEvent event
    ) {
        if (!signedIn) {
            String username = inputUsername.getText().trim();
            String password = inputPassword.getText();
            inputPassword.clear();
            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Missing information");
                alert.setHeaderText("Please provide username and a password");
                alert.setContentText("These information are required to sign in");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
                return;
            }
        } else {
            inputUsername.clear();
        }
        signedIn = !signedIn;
        buttonSignInOut.setText(signedIn ? "Sign out" : "Sign in");
        inputUsername.setDisable(signedIn);
        inputPassword.setDisable(signedIn);
    }

}
