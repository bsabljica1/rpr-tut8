package sample;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.File;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.TouchEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ResourceBundle;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Controller
{

    public TextField podstring;
    public Button trazi;
    public ListView spisak;
    public File direktorij;
    public Button prekini;
    private Thread nit1;
    private ObservableList<String> lista;

    public Controller()
    {
        direktorij = new File(System.getProperty("user.home"));
        lista = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize()
    {
        spisak.setItems(lista);
        prekini.setDisable(true);
        trazi.setDisable(false);

        spisak.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                FXMLLoader loader = new FXMLLoader( getClass().getResource("slanje.fxml") );
                Parent root = loader.load();
                Stage prozor_za_slanje = new Stage();
                prozor_za_slanje.setTitle("Prozor za slanje");
                prozor_za_slanje.setScene(new Scene(root, 600, 400));
                prozor_za_slanje.show();
                }
                catch (IOException e) {}
            }
        });
    }

    private void IzvrsavanjePretrage(String put, String pretrazeni)
    {
    }

    public void Pretrazi(ActionEvent actionEvent)
    {
        trazi.setDisable(true);
        prekini.setDisable(false);
        lista.clear();
        Pretrazivanje pretraga = new Pretrazivanje();
        nit1= new Thread(pretraga);
        nit1.start();
    }

    public void Prekini(ActionEvent actionEvent)
    {
        trazi.setDisable(false);
        prekini.setDisable(true);
        nit1.stop();
    }

    public class Pretrazivanje implements Runnable
    {
        @Override
        public void run()
        {
            IzvrsavanjePretrage(direktorij.getAbsolutePath(), podstring.getText() );
        }
    }
}