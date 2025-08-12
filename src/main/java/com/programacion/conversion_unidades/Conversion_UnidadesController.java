package com.programacion.conversion_unidades;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

public class Conversion_UnidadesController {

    private static final Pattern NUMERO_PATTERN = Pattern.compile("-?\\d*\\.?\\d*");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    private TextField valorField;

    @FXML
    private ComboBox<String> origenCombo;

    @FXML
    private ComboBox<String> destinoCombo;

    @FXML
    private Label resultadoLabel;

    @FXML
    private ListView<String> historialListView;

    // Cola para mantener solo las últimas 10 conversiones
    private final Queue<String> historialQueue = new LinkedList<>();

    @FXML
    public void initialize() {
        // Llenar los ComboBox
        origenCombo.getItems().addAll("Metros", "Pulgadas", "Kilogramos", "Libras", "Celsius", "Fahrenheit");
        destinoCombo.getItems().addAll("Metros", "Pulgadas", "Kilogramos", "Libras", "Celsius", "Fahrenheit");
    }

    @FXML
    private void convertir() {
        try {
            double valor = Double.parseDouble(valorField.getText());
            String origen = origenCombo.getValue();
            String destino = destinoCombo.getValue();

            if (origen == null || destino == null) {
                resultadoLabel.setText("Seleccione ambas unidades");
                return;
            }
            // Validar que no sean iguales
            if (origen.equals(destino)) {
                mostrarAlerta("Seleccione unidades diferentes.");
                return;
            }
            if (!mismaCategoria(origen, destino)) {
                mostrarAlerta("No se puede convertir entre categorías distintas.");
                return;
            }

            double resultado = realizarConversion(valor, origen, destino);

            resultadoLabel.setText(String.format("%.2f", resultado));

            // Agregar al historial
            String registro = valor + " " + origen + " = " + String.format("%.2f", resultado) + " " + destino;
            agregarAlHistorial(registro);

        } catch (NumberFormatException e) {
            resultadoLabel.setText("Ingrese un valor válido");
        }
    }
    private boolean mismaCategoria(String origen, String destino) {
        String categoriaOrigen = obtenerCategoria(origen);
        String categoriaDestino = obtenerCategoria(destino);
        return categoriaOrigen.equals(categoriaDestino);
    }
    private String obtenerCategoria(String unidad) {
        switch (unidad) {
            case "Metros":
            case "Kilómetros":
            case "Centímetros":
            case "Pulgadas":
                return "longitud";
            case "Celsius":
            case "Fahrenheit":
                return "temperatura";
            case "Libras":
            case "Kilogramos":
                return "peso";
            default:
                return "desconocido";
        }
    }

    private double realizarConversion(double valor, String origen, String destino) {
        if (origen.equals(destino)) return valor;

        switch (origen + "->" + destino) {
            case "Metros->Pulgadas":
                return valor * 39.3701;
            case "Pulgadas->Metros":
                return valor / 39.3701;
            case "Kilogramos->Libras":
                return valor * 2.20462;
            case "Libras->Kilogramos":
                return valor / 2.20462;
            case "Celsius->Fahrenheit":
                return (valor * 9 / 5) + 32;
            case "Fahrenheit->Celsius":
                return (valor - 32) * 5 / 9;
            default:
                return valor;
        }
    }
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Advertencia");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void agregarAlHistorial(String registro) {
        historialQueue.add(registro);
        if (historialQueue.size() > 10) {
            historialQueue.poll(); // Elimina el más antiguo
        }
        historialListView.getItems().setAll(historialQueue);
    }

    @FXML
    private void limpiar() {
        valorField.clear();
        origenCombo.getSelectionModel().clearSelection();
        destinoCombo.getSelectionModel().clearSelection();
        resultadoLabel.setText("");
    }
}