package org.librarymanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutController {

    @FXML private
    Label headingLbl;

    private void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
        }
    }

    @FXML
    private void visitGithub(){
        openWebpage("https://github.com/Hasnatrasool163");
    }

    @FXML
    private void visitLinkedin(){
        openWebpage("https://www.linkedin.com/in/hasnatrasool163/");
    }
}
