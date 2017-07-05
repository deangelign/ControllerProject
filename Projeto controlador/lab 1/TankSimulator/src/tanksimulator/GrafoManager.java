/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksimulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author deangel
 */
public class GrafoManager extends Thread {

    int taxaDeUpdate_miliSegundos;
    JTextField valorTensao;
    JTextField valorAltura;
    JTextField valorAlturaTanque2;
    JTextField valorTempo;
    JTextField fatorDeAceleracao;
    JTextField valorErrorAltura;
    JPanel panelGraficoSinal;
    JPanel panelGraficoAltura;
    JPanel panelGraficoSinalAPenas;
    JPanel panelGraficoAlturaTankque2;
    JPanel panelGraficoErroAltura;
    JPanel panelGraficoTodos;
    //JLabel lbl_temeperatura;
    //JSpinner spn_setPoint;
    //SerialComLeitura serialComLeitura;
    XYSeries series;
    XYSeries seriesAltura;
    XYSeries seriesSetPoint;
    XYSeries seriesAlturaTanque2;
    XYSeries seriesErrorAltura;
    XYSeriesCollection datas;
    XYSeriesCollection datasAltura;
    XYSeriesCollection datasSetPoint;
    XYSeriesCollection datasAlturaTanque2;
    XYSeriesCollection datasErrorAltura;
    
    ChartPanel chartPanel;
    JFreeChart chart;
    ChartPanel chartPanelTensaoApenas;
    JFreeChart chartTensaoApenas;
    //double temperatura;
    ChartPanel chartPanelAltura;
    JFreeChart chartAltura;
    ChartPanel chartpanelAlturaTanque2;
    JFreeChart chartAlturaTanque2;
    ChartPanel chartpanelErrorAltura;
    JFreeChart chartErrorAltura;
    ChartPanel chartPanelTodos;
    JFreeChart chartTodos;
    double[] PVector;

    public GrafoManager(JPanel panelTensão, JPanel panelAltura, double[] PVector,
            JPanel panelTensãoApenas, JTextField valorTensao, JTextField valorAltura, JTextField valorTempo,
            JTextField fatorDeAceleracao, JTextField valorAlturaTanque2, JPanel panelGraficoAlturaTankque2,
            JPanel panelGraficoErroAltura, JTextField valorErrorAltura, JPanel panelTodos) {

        this.fatorDeAceleracao = fatorDeAceleracao;
        this.valorTensao = valorTensao;
        this.valorAltura = valorAltura;
        this.valorTempo = valorTempo;
        this.valorAlturaTanque2 = valorAlturaTanque2;
        this.valorErrorAltura = valorErrorAltura;
        


        this.taxaDeUpdate_miliSegundos = 1;


        this.PVector = PVector;
        //this.temperatura = 20;
        //this.lbl_temeperatura = lbl_temperatura;
        this.panelGraficoSinal = panelTensão;
        this.panelGraficoAltura = panelAltura;
        this.panelGraficoSinalAPenas = panelTensãoApenas;
        this.panelGraficoAlturaTankque2 = panelGraficoAlturaTankque2;
        this.panelGraficoErroAltura = panelGraficoErroAltura;
        this.panelGraficoTodos = panelTodos;
        //this.serialComLeitura = serial;

        //this.spn_setPoint = spinner;
        this.series = new XYSeries("Sinal");
        this.seriesAltura = new XYSeries("Nível do tanque1");
        this.seriesSetPoint = new XYSeries("setpoint");
        this.seriesAlturaTanque2 = new XYSeries("Nível do tanque2");
        this.seriesErrorAltura = new XYSeries("Error (cm)");
        

        this.datas = new XYSeriesCollection(series);
        this.datasAltura = new XYSeriesCollection(seriesAltura);
        this.datasSetPoint = new XYSeriesCollection(seriesSetPoint);
        this.datasAlturaTanque2 = new XYSeriesCollection(seriesAlturaTanque2);
        this.datasErrorAltura = new XYSeriesCollection(seriesErrorAltura);
        

        this.chart = ChartFactory.createXYLineChart("Sinal de Entrada", "t (s)", "Amplitude (V)",
                datas, PlotOrientation.VERTICAL, true, true, false);
        this.chartPanel = new ChartPanel(chart);
        this.chartPanel.setPreferredSize(new java.awt.Dimension(320, 190));
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);

        this.chartTensaoApenas = ChartFactory.createXYLineChart("Sinal de Entrada", "t (s)", "Amplitude (V)",
                datas, PlotOrientation.VERTICAL, true, true, false);
        this.chartPanelTensaoApenas = new ChartPanel(chartTensaoApenas);
        this.chartPanelTensaoApenas.setPreferredSize(new java.awt.Dimension(320, 190));
        XYPlot plotTensaoApenas = (XYPlot) chartTensaoApenas.getPlot();
        plot.setBackgroundPaint(Color.lightGray);

        this.chartAltura = ChartFactory.createXYLineChart("Nível do tanque1",
                "t (s)", "Altura (cm)", datasAltura, PlotOrientation.VERTICAL, true, true, false);
        this.chartPanelAltura = new ChartPanel(chartAltura);
        this.chartPanelAltura.setPreferredSize(new java.awt.Dimension(320, 190));
        XYPlot plotAltura = (XYPlot) chartAltura.getPlot();
        plotAltura.setBackgroundPaint(Color.lightGray);


        this.chartAlturaTanque2 = ChartFactory.createXYLineChart("Nível do tanque2", "t (s)", "Altura (cm)",
                datasAlturaTanque2, PlotOrientation.VERTICAL, true, true, false);
        this.chartpanelAlturaTanque2 = new ChartPanel(chartAlturaTanque2);
        this.chartpanelAlturaTanque2.setPreferredSize(new java.awt.Dimension(320, 190));
        XYPlot plotAlturaTanque2 = (XYPlot) chartAlturaTanque2.getPlot();
        plotAlturaTanque2.setBackgroundPaint(Color.lightGray);
        //plot.setDataset(1, datasSetPoint);
        //plot.setDataset(1, datasSetPoint);
        this.chartErrorAltura = ChartFactory.createXYLineChart("Error ", "t (s)", "error (cm)", datasErrorAltura, PlotOrientation.VERTICAL, true, true, false);
        this.chartpanelErrorAltura = new ChartPanel(chartErrorAltura);
        this.chartpanelErrorAltura.setPreferredSize(new java.awt.Dimension(320, 190));
        XYPlot plotErrorAltura = (XYPlot) chartErrorAltura.getPlot();
        plotErrorAltura.setBackgroundPaint(Color.lightGray);
        
        this.chartTodos = ChartFactory.createXYLineChart("Níveis", "t (s)", "Altura (cm)", datasAltura,
                PlotOrientation.VERTICAL, true, true, false);
        this.chartPanelTodos = new ChartPanel(chartTodos);
        this.chartPanelTodos.setPreferredSize(new java.awt.Dimension(640, 190));
        XYPlot plotTodos = (XYPlot) chartTodos.getPlot();
        

        XYLineAndShapeRenderer render2 = new XYLineAndShapeRenderer(true, false);
        //XYLineAndShapeRenderer render3 = new XYLineAndShapeRenderer(true,false);

        render2.setBaseSeriesVisibleInLegend(false);



        //plot.setRenderer(1, render2);


        //plot.getRenderer(1).setSeriesPaint(0, Color.BLUE);


        render2.setBaseSeriesVisibleInLegend(false);
        plot.getRenderer().setBaseSeriesVisibleInLegend(false);
        //plot.getRenderer().setBaseSeriesVisible(false);
        //plot.getRenderer(1).setSeriesPaint(0, Color.BLUE);


        XYLineAndShapeRenderer renderTensaoApenas = new XYLineAndShapeRenderer(true, false);
        renderTensaoApenas.setBaseSeriesVisibleInLegend(true);
        plotTensaoApenas.getRenderer().setBaseSeriesVisibleInLegend(true);

        plotErrorAltura.getRenderer().setBaseSeriesVisibleInLegend(true);



        plotAltura.setDataset(1, this.datasSetPoint);
        XYLineAndShapeRenderer rendererAltura = new XYLineAndShapeRenderer(true, false);
        rendererAltura.setBaseSeriesVisibleInLegend(false);
        //rendererAltura.setBaseSeriesVisibleInLegend(false);

        plotAltura.setRenderer(1, rendererAltura);
        plotAltura.getRenderer(1).setSeriesPaint(0, Color.BLUE);
        plotAltura.getRenderer().setBaseSeriesVisibleInLegend(true);
        plotAltura.getRenderer(1).setBaseSeriesVisibleInLegend(true);

        plotTodos.setDataset(1, this.datasSetPoint);
        plotTodos.setDataset(2, this.datasAlturaTanque2);
        
        XYLineAndShapeRenderer rendererTodos1 = new XYLineAndShapeRenderer(true, false);
        XYLineAndShapeRenderer rendererTodos2 = new XYLineAndShapeRenderer(true, false);
        
        plotTodos.setRenderer(1,rendererTodos1);
        plotTodos.setRenderer(2, rendererTodos2);
        plotTodos.getRenderer(1).setSeriesPaint(0, Color.BLUE);
        plotTodos.getRenderer(1).setBaseSeriesVisibleInLegend(true);
        plotTodos.getRenderer(2).setSeriesPaint(0, Color.GREEN);
        plotTodos.getRenderer(2).setBaseSeriesVisibleInLegend(true);


        plotAlturaTanque2.setDataset(1, this.datasSetPoint);
        XYLineAndShapeRenderer rendererAlturataque2 = new XYLineAndShapeRenderer(true, false);
        rendererAlturataque2.setBaseSeriesVisibleInLegend(false);
        plotAlturaTanque2.setRenderer(1, rendererAlturataque2);
        plotAlturaTanque2.getRenderer(0).setSeriesPaint(0, Color.GREEN);
        plotAlturaTanque2.getRenderer(1).setSeriesPaint(0, Color.BLUE);
        plotAlturaTanque2.getRenderer().setBaseSeriesVisibleInLegend(true);
        plotAlturaTanque2.getRenderer(1).setBaseSeriesVisibleInLegend(true);


        

    }

    public void addPonto(double X, double Y) {
        series.add(X, Y);
    }

    public void addPontoSeriesErrorAltura(double X, double Y) {
        seriesErrorAltura.add(X, Y);
    }

    public void addPontoSeriesAltura(double X, double Y) {
        this.seriesAltura.add(X, Y);
    }

    public void addPontoSeriesAlturatanque2(double X, double Y) {
        this.seriesAlturaTanque2.add(X, Y);
    }

    public void addPontoSetPoint(double X, double Y) {
        this.seriesSetPoint.add(X, Y);
    }

    public void refreshPanel() {

        //double aux = 100;
        this.refreshPanelTension();
        this.refreshPanelHeight();
        this.refreshPanelApenasTensao();
        this.refreshPanelErrorAltura();
        this.refreshPanelHeight2();
        this.refreshPanelGraficoTodos();

        try {
            this.taxaDeUpdate_miliSegundos = Integer.parseInt(this.fatorDeAceleracao.getText());
        } catch (Exception e) {
        }

        //if (this.taxaDeUpdate_miliSegundos < 1) {
        //    this.taxaDeUpdate_miliSegundos = 1;
        //}


        try {
            Thread.sleep(this.taxaDeUpdate_miliSegundos);
        } catch (InterruptedException ex) {
            Logger.getLogger(GrafoManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.PVector[3] = 2;


    }

    public void refreshPanelTension() {
        this.panelGraficoSinal.removeAll();
        this.panelGraficoSinal.setLayout(new java.awt.BorderLayout());
        this.panelGraficoSinal.add(chartPanel, BorderLayout.CENTER);
        this.panelGraficoSinal.validate();

    }

    public void refreshPanelGraficoTodos() {
        this.panelGraficoTodos.removeAll();
        this.panelGraficoTodos.setLayout(new java.awt.BorderLayout());
        this.panelGraficoTodos.add(chartPanelTodos, BorderLayout.CENTER);
        this.panelGraficoTodos.validate();

    }

    public void refreshPanelHeight() {
        this.panelGraficoAltura.removeAll();
        this.panelGraficoAltura.setLayout(new java.awt.BorderLayout());
        this.panelGraficoAltura.add(chartPanelAltura, BorderLayout.CENTER);
        this.panelGraficoAltura.validate();

    }

    public void refreshPanelHeight2() {
        this.panelGraficoAlturaTankque2.removeAll();
        this.panelGraficoAlturaTankque2.setLayout(new java.awt.BorderLayout());
        this.panelGraficoAlturaTankque2.add(chartpanelAlturaTanque2, BorderLayout.CENTER);
        this.panelGraficoAlturaTankque2.validate();
    }

    public void refreshPanelErrorAltura() {
        this.panelGraficoErroAltura.removeAll();
        this.panelGraficoErroAltura.setLayout(new java.awt.BorderLayout());
        this.panelGraficoErroAltura.add(chartpanelErrorAltura, BorderLayout.CENTER);
        this.panelGraficoErroAltura.validate();
    }

    public void refreshPanelApenasTensao() {
        this.panelGraficoSinalAPenas.removeAll();
        this.panelGraficoSinalAPenas.setLayout(new java.awt.BorderLayout());
        this.panelGraficoSinalAPenas.add(chartPanelTensaoApenas, BorderLayout.CENTER);
        this.panelGraficoSinalAPenas.validate();

    }

    public int valorSetPoint() {
        /*
         * try {
         *
         * int d = ( (Integer)this.spn_setPoint.getValue() ); if(d>100){return
         * 100;} if(d<0){return 0;} return d; } catch (Exception e) { return 0;
         *
         * }
         */
        return 0;
    }

    public void myClear() {
        this.series.clear();
        this.seriesAltura.clear();
        this.seriesSetPoint.clear();
        this.seriesAlturaTanque2.clear();
        this.seriesErrorAltura.clear();
        //this.addPonto(0, 0);
        //this.addPontoSeriesAltura(0, 0);
        //this.addPontoSetPoint(0, 0);
        //this.addPontoSeriesAlturatanque2(0, 0);
    }

    public void myClear2() {
        this.series.clear();
        this.seriesAltura.clear();
        this.seriesSetPoint.clear();
        this.seriesAlturaTanque2.clear();
        this.seriesErrorAltura.clear();
    }

    public void run() {


        //this.addPonto(0, 0);
        //this.addPontoSeriesAltura(0, 0);
        //this.addPontoSeriesAlturatanque2(0, 0);
        this.refreshPanel();

        double altura;
        double tempoS;
        while (true) {

            //valor = 26 + (Math.random() * 2);
            //this.addPonto(i, valor);
            //this.addPontoSeriesAltura(i, valor);

            if (this.PVector[3] < 1) {
                //tempoS = this.PVector[0] / Math.pow(10, 9);

                //this.addPonto(this.PVector[0], this.PVector[1]);
                //this.addPontoSeriesAltura(this.PVector[0], this.PVector[2]);
                this.addPonto(this.PVector[0], this.PVector[1]);
                this.addPontoSeriesAltura(this.PVector[0], this.PVector[2]);

                if (PVector[16] > 1) {
                    this.addPontoSeriesAlturatanque2(this.PVector[0], this.PVector[5]);
                }
                if (PVector[15] > 1) {
                    this.addPontoSetPoint(this.PVector[0], this.PVector[14]);
                    this.addPontoSeriesErrorAltura(this.PVector[0], PVector[13]);

                }

                this.valorTensao.setText(Double.toString(this.PVector[1]));
                this.valorAltura.setText(Double.toString(this.PVector[2]));
                this.valorTempo.setText(Double.toString(this.PVector[0]));
                this.valorAlturaTanque2.setText(Double.toString(this.PVector[5]));
                this.valorErrorAltura.setText(Double.toString(this.PVector[13]));
                
                this.refreshPanel();

            }
//            i++;
//            System.out.println(i++);

        }
    }

    public JFreeChart getChart() {
        return chart;
    }

    public void setChart(JFreeChart chart) {
        this.chart = chart;
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    public void setChartPanel(ChartPanel chartPanel) {
        this.chartPanel = chartPanel;
    }

    public XYSeriesCollection getDatas() {
        return datas;
    }

    public void setDatas(XYSeriesCollection datas) {
        this.datas = datas;
    }

    //public XYSeriesCollection getDatasSetPoint() {
    //    return datasSetPoint;
    //}
    //public void setDatasSetPoint(XYSeriesCollection datasSetPoint) {
    //    this.datasSetPoint = datasSetPoint;
    //}
    public JPanel getPanel() {
        return panelGraficoSinal;
    }

    public void setPanel(JPanel panel) {
        this.panelGraficoSinal = panel;
    }

    public XYSeries getSeries() {
        return series;
    }

    public void setSeries(XYSeries series) {
        this.series = series;
    }
    //public XYSeries getSeriesSetPoint() {
    //    return seriesSetPoint;
    //}
    //public void setSeriesSetPoint(XYSeries seriesSetPoint) {
    //    this.seriesSetPoint = seriesSetPoint;
    //}
    //public double getTemperatura() {
    //    return temperatura;
    //}
    //public void setTemperatura(double temperatura) {
    //    this.temperatura = temperatura;
    //}
}
