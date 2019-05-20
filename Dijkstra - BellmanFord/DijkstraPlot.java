/*******************************************************************************
* Salvatore Danilo Palumbo - 0642907
* Classe che realizza il plot dei tempi di DijkstraSP sulle due famiglie di
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

public class DijkstraPlot extends Application {




    @Override public void start(Stage stage){
      Stage stage2 = new Stage();
        stage.setTitle("Tempi di esecuzione");
        stage2.setTitle("Tempi di esecuzione");
        //Definisco gli assi
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("|V|");
        yAxis.setLabel("Nanoseconds");
        final NumberAxis xAxis2 = new NumberAxis();
        final NumberAxis yAxis2 = new NumberAxis();
        xAxis2.setLabel("|V|");
        yAxis2.setLabel("Nanoseconds");
        //Creo il plot
        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        final LineChart<Number,Number> lineChart2 = new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("DijkstraSP");
        lineChart2.setTitle("BellmanFordSP");

        //Definisco le serie di dati:
        // in questo caso sono: |E| = |V|^2-|V|
        // e |E| = |V|log|V|
        XYChart.Series series = new XYChart.Series();
				XYChart.Series series2 = new XYChart.Series();
        series.setName("Tempo con |E| = |V|^2-|V|");
				series2.setName("Tempo con |E| = |V|log|V|");

        ArrayList<Long> list = new ArrayList<Long>();
        try{
          Scanner s = new Scanner(new File("TEMPI_DIJKSTRAV2.txt"));
          while (s.hasNextLong()){
            list.add(s.nextLong());
          }
          s.close();
        }catch(Exception e){
          e.printStackTrace();
        }


        //Linea grafico per |E| = |V|^2-|V|
        for(int i=0;i<list.size();i++){
          series.getData().add(new XYChart.Data(10*1+50*i, list.get(i)));
        }

        ArrayList<Long> list2 = new ArrayList<Long>();
        try{
          Scanner s = new Scanner(new File("TEMPI_BF.txt"));
          while (s.hasNextLong()){
            list2.add(s.nextLong());
          }
          s.close();
        }catch(Exception e){
          e.printStackTrace();
        }


        //Linea grafico per |E| = |V|^2-|V|
        for(int i=0;i<list2.size();i++){
          series2.getData().add(new XYChart.Data(10*1+50*i, list2.get(i)));
        }




          Scene scene  = new Scene(lineChart,800,600);
          Scene scene2 = new Scene(lineChart2,600,400);
          lineChart.getData().add(series);
          lineChart.getData().add(series2);
          stage.setScene(scene);
          stage.show();
          stage2.setScene(scene2);
          stage2.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
