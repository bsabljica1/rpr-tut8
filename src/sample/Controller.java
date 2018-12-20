package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Controller {
    public static File file;
    public Label input;
    public Button searchBtn;
    public Button stopSearchBtn;
    public TextField searchText;
    public ListView<String> foundFiles;
    public Thread whileSearchThread;

    public class Pretraga implements Runnable{
        @Override
        public void run() {
            String fileName = searchText.getText();
            displayAll(new File(System.getProperty("user.home")), fileName);
        }
    }

    private void displayAll(File file, String fileName) {
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null){
                for(File f : files){
                    displayAll(f, fileName);
                }
            }
        }
        if(file.isFile()){
            if( file.getName().contains( fileName ) ) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    foundFiles.getItems().add(file.getAbsolutePath());
                } );
            }
        }
        if(file.getAbsolutePath().equals("user.home")){
            searchBtn.setDisable(false);
        }
    }

    public void Prekini(ActionEvent actionEvent){
        searchBtn.setDisable(false);
        stopSearchBtn.setDisable(true);
        whileSearchThread.stop();
    }

    public void initialize() {
        stopSearchBtn.setDisable(true);
        foundFiles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("slanje.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage secondaryStage = new Stage();
                secondaryStage.setTitle("Posalji datoteku");
                secondaryStage.setResizable(false);
                secondaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                if(foundFiles.getSelectionModel().getSelectedItem() != null)
                    file = new File(foundFiles.getSelectionModel().getSelectedItem());
                secondaryStage.show();
            }
        });
    }

    public void Pretrazi(ActionEvent actionEvent){
        searchBtn.setDisable(true);
        stopSearchBtn.setDisable(false);
        foundFiles.getSelectionModel().clearSelection();
        foundFiles.getItems().clear();
        Pretraga pretraga = new Pretraga();
        whileSearchThread = new Thread(pretraga);
        whileSearchThread.start();
    }
}