/*******************************************************************************
* Salvatore Danilo Palumbo - 0642907
* Classe che realizza il plot dei tempi di BellmanFordSP sulle due famiglie di
* algoritmi.
* Ho calcolato i tempi creati su grafi random con la classe GeneratorAnalyzerDB
* e li ho inseriti come asse y del plot.
* Inoltre, questa classe è basata su un'altra classe di JavaFX per la realizzazione
* di plot.
*******************************************************************************/


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.*;
import java.lang.*;
import java.io.*;

public class BellmanFordPlot extends Application {
    @Override public void start(Stage stage) {
        stage.setTitle("Tempi di esecuzione");
        //Definisco gli assi
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("|V|");
        yAxis.setLabel("Nanoseconds");
        //Creo il plot
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("BellmanFord");

        //Definisco le serie di dati:
        // in questo caso sono: |E| = |V|^2-|V|
        // e |E| = |V|log|V|
        XYChart.Series series = new XYChart.Series();
				XYChart.Series series1 = new XYChart.Series();
        series.setName("Tempo con |E| = |V|^2-|V|");
				series1.setName("Tempo con |E| = |V|log|V|");


        //Linea grafico per |E| = |V|^2-|V|
        series.getData().add(new XYChart.Data(10, 1573103));
        series.getData().add(new XYChart.Data(50, 1204400));
        series.getData().add(new XYChart.Data(100, 1405727));
        series.getData().add(new XYChart.Data(150, 1503626));
        series.getData().add(new XYChart.Data(200, 1404542));
        series.getData().add(new XYChart.Data(250, 1406121));
        series.getData().add(new XYChart.Data(350, 1405726));
        series.getData().add(new XYChart.Data(400, 1483098));
        series.getData().add(new XYChart.Data(450, 1548628));
        series.getData().add(new XYChart.Data(500, 1484678));
        series.getData().add(new XYChart.Data(500, 1550207));



        //Linea grafico per |E| = |V|log|V|;
        series1.getData().add(new XYChart.Data(10, 1595210));
        series1.getData().add(new XYChart.Data(50, 1080052));
        series1.getData().add(new XYChart.Data(100, 1525732));
        series1.getData().add(new XYChart.Data(150, 1457834));
        series1.getData().add(new XYChart.Data(200, 1109659));
        series1.getData().add(new XYChart.Data(250, 1576656));
        series1.getData().add(new XYChart.Data(350, 1745611));
        series1.getData().add(new XYChart.Data(400, 1734953));
        series1.getData().add(new XYChart.Data(450, 1012550));
        series1.getData().add(new XYChart.Data(500, 1616921));
        series1.getData().add(new XYChart.Data(500, 1849432));


        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);
				lineChart.getData().add(series1);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
