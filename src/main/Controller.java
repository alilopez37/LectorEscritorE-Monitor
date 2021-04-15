package main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.models.*;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller implements Observer {
    private Random random =  new Random(System.currentTimeMillis());
    private ArrayList<Circle> lectores = new ArrayList<>();
    private ArrayList<Circle> escritores = new ArrayList<>();
    private ArrayList<Circle> recurso = new ArrayList<>();
    private Object o1 = new Object();
    private Object o2 = new Object();


    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnSalir;

    @FXML
    private Button btnIniciar;

    @FXML
    void btnSalirOnMouseClicked(MouseEvent event) {
        Platform.exit();
        System.exit(1);
    }

    @FXML
    void btnIniciarOnMouseClicked(MouseEvent event) {
        Monitor monitor = new Monitor();
        int x=0;
        while (x<50){
            if (random.nextInt(2) == 0) {
                Lector lector = new Lector(monitor);
                lector.addObserver(this);
                new Thread(lector, String.valueOf(x)).start();
            }
            else {
                Escritor escritor = new Escritor(monitor);
                escritor.addObserver(this);
                new Thread(escritor, String.valueOf(x)).start();
            }
            x++;
        }

    }

    private void addLector(){
        synchronized (o1) {
            Circle circle = new Circle(10, Color.RED);
            AtomicInteger num = new AtomicInteger(lectores.size());

            Platform.runLater(() -> {
                int x = 25 + ((num.get() % 3) * 30);
                int y = 60 + ((num.get() / 3) * 30);
                circle.setLayoutX(x);
                circle.setLayoutY(y);
                anchorPane.getChildren().add(circle);
            });
            lectores.add(circle);
        }
    }
    private void removeLector(){}
    private void addEscritor(){
        synchronized (o2) {
            Circle circle = new Circle(10, Color.GREEN);
            AtomicInteger num = new AtomicInteger(escritores.size());

            Platform.runLater(() -> {
                int x = 125 + ((num.get() % 3) * 30);
                int y = 60 + ((num.get() / 3) * 30);
                circle.setLayoutX(x);
                circle.setLayoutY(y);
                anchorPane.getChildren().add(circle);
            });
            escritores.add(circle);
        }
    }
    private void removeEscritor(){}
    private void addRecurso(String tipo){
        Circle circle;
        if (tipo.equals("Lector"))
            synchronized (o1) {
                circle = lectores.remove(lectores.size() - 1);
            }
        else
            synchronized (o2) {
                circle = escritores.remove(escritores.size() - 1);
            }

        AtomicInteger num = new AtomicInteger(recurso.size());

        Platform.runLater(() -> {
            int x = 230 + ((num.get() % 10) * 30);
            int y = 60 + ((num.get() / 10) * 30);
            circle.setLayoutX(x);
            circle.setLayoutY(y);
        });
        recurso.add(circle);
    }

    @Override
    public void update(Observable o, Object arg) {
        String cadena = ((String)arg);
        if (o instanceof Lector ) {
            switch (cadena){
                case "1":
                    this.addLector();
                    break;
                case "2":
                    this.addRecurso("Lector");
                    break;
                case "3":
            }
        }
        else {
            switch (cadena){
                case "1":
                    this.addEscritor();
                    break;
                case "2":
                    this.addRecurso("Escritor");
                    break;
                case "3":
            }
        }
    }
}
