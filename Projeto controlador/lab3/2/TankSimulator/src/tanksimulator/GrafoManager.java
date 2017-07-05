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
import org.jfree.chart.*;
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
    JTextField valorAcumulado;
    JTextField valorDerivativo;
    //tempos tanque1
    JTextField tempoDesubida0100Tanque1;
    JTextField tempoDesubida1090Tanque1;
    JTextField tempoDesubida02080Tanque1;
    JTextField tempoDePicoTanque1;
    JTextField overShootTanque1;
    JTextField underShootTanque1;
    JTextField tempoDeAcomodacao10Tanque1;
    JTextField tempoDeAcomodacao5Tanque1;
    JTextField tempoDeAcomodacao2Tanque1;
    //tempos tanque2
    JTextField tempoDesubida0100Tanque2;
    JTextField tempoDesubida1090Tanque2;
    JTextField tempoDesubida02080Tanque2;
    JTextField tempoDePicoTanque2;
    JTextField overShootTanque2;
    JTextField underShootTanque2;
    JTextField tempoDeAcomodacao10Tanque2;
    JTextField tempoDeAcomodacao5Tanque2;
    JTextField tempoDeAcomodacao2Tanque2;
    JPanel _panelGraficoSinal;
    JPanel _panelGraficoErroAltura;
    JPanel _panelGraficoTodos;
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
    ChartPanel chartPanelSinal;
    JFreeChart chartSinal;
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
    //0 - sinal
    //1 -altura tanque1
    //2 - altura tanque2
    //3 - error altura
    //4 - grafico todos
    boolean[] taGrande;
    TimeMensureMaster timeMensureMaster;
    TimeMensureMaster timeMensureMaster2;

    public GrafoManager(JPanel panelTensão, final double[] PVector,
            JTextField valorTensao, JTextField valorAltura, JTextField valorTempo,
            JTextField fatorDeAceleracao, JTextField valorAlturaTanque2,
            JPanel panelGraficoErroAltura, JTextField valorErrorAltura, JPanel panelTodos,
            JTextField valorAcumulativo, JTextField valorDerivativo) {

        timeMensureMaster = new TimeMensureMaster();
        timeMensureMaster2 = new TimeMensureMaster();
        this.taGrande = new boolean[5];
        this.valorAcumulado = valorAcumulativo;
        this.valorDerivativo = valorDerivativo;

        this.fatorDeAceleracao = fatorDeAceleracao;
        this.valorTensao = valorTensao;
        this.valorAltura = valorAltura;
        this.valorTempo = valorTempo;
        this.valorAlturaTanque2 = valorAlturaTanque2;
        this.valorErrorAltura = valorErrorAltura;


        this.taxaDeUpdate_miliSegundos = 0;

        this.PVector = PVector;
        //this.temperatura = 20;
        //this.lbl_temeperatura = lbl_temperatura;
        this._panelGraficoSinal = panelTensão;

        this._panelGraficoErroAltura = panelGraficoErroAltura;
        this._panelGraficoTodos = panelTodos;
        //this.serialComLeitura = serial;

        this._panelGraficoErroAltura.setOpaque(false);
        this._panelGraficoSinal.setOpaque(false);
        this._panelGraficoTodos.setOpaque(false);

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


        this.chartSinal = ChartFactory.createXYLineChart("Sinal de Entrada", "t (s)", "Amplitude (V)",
                datas, PlotOrientation.VERTICAL, true, true, false);
        this.chartPanelSinal = new ChartPanel(chartSinal);
        this.chartPanelSinal.setPreferredSize(new java.awt.Dimension(650, 190));
        XYPlot plot = (XYPlot) chartSinal.getPlot();
        plot.setBackgroundPaint(Color.lightGray);




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
        this.chartPanelTodos.setPreferredSize(new java.awt.Dimension(650, 570));
        XYPlot plotTodos = (XYPlot) chartTodos.getPlot();


        chartPanelSinal.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {

                if (taGrande[0]) {
                    taGrande[0] = false;

                    visibleAllFirstpadded();
                    smallPanel(_panelGraficoSinal, chartPanelSinal);

                    if (PVector[16] < 1) {
                        _panelGraficoTodos.setVisible(false);
                    }
                    if (PVector[15] < 1) {
                        _panelGraficoErroAltura.setVisible(false);
                        _panelGraficoSinal.setSize(650, 190);
                        chartPanelSinal.setPreferredSize(new java.awt.Dimension(640, 190));

                    }


                } else {
                    taGrande[0] = true;
                    invisibleAllFirstpadded();
                    _panelGraficoSinal.setVisible(true);
                    bigPanel(_panelGraficoSinal, chartPanelSinal);
                }


            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });


        this.chartpanelErrorAltura.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                if (taGrande[3]) {
                    taGrande[3] = false;

                    //_panelGraficoAltura.setVisible(true);
                    visibleAllFirstpadded();
                    smallPanel(_panelGraficoErroAltura, chartpanelErrorAltura);

                    if (PVector[16] < 1) {
                        _panelGraficoTodos.setVisible(false);
                    }

                    if (PVector[15] < 1) {
                        _panelGraficoErroAltura.setVisible(false);
                        _panelGraficoSinal.setSize(650, 190);
                        chartPanelSinal.setPreferredSize(new java.awt.Dimension(640, 190));

                    }


                } else {
                    taGrande[3] = true;

                    invisibleAllFirstpadded();
                    _panelGraficoErroAltura.setVisible(true);
                    bigPanel(_panelGraficoErroAltura, chartpanelErrorAltura);

                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        this.chartPanelTodos.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                if (taGrande[4]) {
                    taGrande[4] = false;

                    //_panelGraficoAltura.setVisible(true);
                    //visibleAllFirstpadded();
                    //smallPanel(_panelGraficoTodos, chartPanelTodos);
                    chartPanelTodos.setPreferredSize(new java.awt.Dimension(640, 190));
                    _panelGraficoTodos.setSize(640, 190);



                    if (PVector[16] < 1) {
                        //_panelGraficoTodos.setVisible(false);
                    }

                    if (PVector[15] < 1) {
                        //_panelGraficoErroAltura.setVisible(false);
                        //_panelGraficoSinal.setSize(650, 190);
                        //chartPanelSinal.setPreferredSize(new java.awt.Dimension(640, 190));
                    }

                } else {
                    taGrande[4] = true;

                    invisibleAllFirstpadded();
                    _panelGraficoTodos.setVisible(true);
                    bigPanel(_panelGraficoTodos, chartPanelTodos);

                }

            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
        });



        XYLineAndShapeRenderer render2 = new XYLineAndShapeRenderer(true, false);
        //XYLineAndShapeRenderer render3 = new XYLineAndShapeRenderer(true,false);

        render2.setBaseSeriesVisibleInLegend(false);



        //plot.setRenderer(1, render2);


        //plot.getRenderer(1).setSeriesPaint(0, Color.BLUE);


        render2.setBaseSeriesVisibleInLegend(false);
        plot.getRenderer().setBaseSeriesVisibleInLegend(true);
        //plot.getRenderer().setBaseSeriesVisible(false);
        //plot.getRenderer(1).setSeriesPaint(0, Color.BLUE);



        plotErrorAltura.getRenderer().setBaseSeriesVisibleInLegend(true);



        XYLineAndShapeRenderer rendererAltura = new XYLineAndShapeRenderer(true, false);
        rendererAltura.setBaseSeriesVisibleInLegend(false);
        //rendererAltura.setBaseSeriesVisibleInLegend(false);


        plotTodos.setDataset(1, this.datasSetPoint);
        plotTodos.setDataset(2, this.datasAlturaTanque2);

        XYLineAndShapeRenderer rendererTodos1 = new XYLineAndShapeRenderer(true, false);
        XYLineAndShapeRenderer rendererTodos2 = new XYLineAndShapeRenderer(true, false);

        plotTodos.setRenderer(1, rendererTodos1);
        plotTodos.setRenderer(2, rendererTodos2);
        plotTodos.getRenderer(1).setSeriesPaint(0, Color.BLUE);
        plotTodos.getRenderer(1).setBaseSeriesVisibleInLegend(true);
        plotTodos.getRenderer(2).setSeriesPaint(0, Color.GREEN);
        plotTodos.getRenderer(2).setBaseSeriesVisibleInLegend(true);


        XYLineAndShapeRenderer rendererAlturataque2 = new XYLineAndShapeRenderer(true, false);
        rendererAlturataque2.setBaseSeriesVisibleInLegend(false);

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
        this.refreshPanelErrorAltura();
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
        this._panelGraficoSinal.removeAll();
        this._panelGraficoSinal.setLayout(new java.awt.BorderLayout());
        this._panelGraficoSinal.add(chartPanelSinal, BorderLayout.CENTER);
        this._panelGraficoSinal.validate();

    }

    public void refreshPanelGraficoTodos() {
        this._panelGraficoTodos.removeAll();
        this._panelGraficoTodos.setLayout(new java.awt.BorderLayout());
        this._panelGraficoTodos.add(chartPanelTodos, BorderLayout.CENTER);
        this._panelGraficoTodos.validate();

    }

    public void refreshPanelErrorAltura() {
        this._panelGraficoErroAltura.removeAll();
        this._panelGraficoErroAltura.setLayout(new java.awt.BorderLayout());
        this._panelGraficoErroAltura.add(chartpanelErrorAltura, BorderLayout.CENTER);
        this._panelGraficoErroAltura.validate();
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


        this.refreshPanel();

        double altura;
        double tempoS;
        while (true) {


            if (this.PVector[3] < 1) {

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
                this.valorAcumulado.setText(Double.toString(this.PVector[19]));
                this.valorDerivativo.setText(Double.toString(this.PVector[20]));


                this.refreshPanel();

                /*
                 * if (this.PVector[23] > 1) { if
                 * (this.timeMensureMaster.isJaFoiDeterminadoOTempoDeSubida0100pt2())
                 * { try {
                 * this.tempoDesubida0100Tanque1.setText(Double.toString(timeMensureMaster.getTempoDeSubida0100()));
                 * this.tempoDesubida1090Tanque1.setText(Double.toString(timeMensureMaster.getTempoDeSubida1090()));
                 * this.tempoDesubida02080Tanque1.setText(Double.toString(timeMensureMaster.getTempoDeSubida2080()));
                 * } catch (Exception e) { System.out.println("error 1"); }
                 *
                 * } else { this.tempoDesubida0100Tanque1.setText("");
                 * this.tempoDesubida1090Tanque1.setText("");
                 * this.tempoDesubida02080Tanque1.setText("");
                 *
                 * }
                 * }
                 */

//                if (this.PVector[16] > 1) {



                //imprimi o valor de subida 20-80%
                if (this.timeMensureMaster2.isJaFoiDeterminadoOTempoDeSubida2080pt2()) {
                    this.tempoDesubida02080Tanque2.setText(Double.toString(timeMensureMaster2.getTempoDeSubida2080()));
                } else {
                    this.tempoDesubida02080Tanque2.setText("");
                }

                //imprimi o valor de subida 10-90%
                if (this.timeMensureMaster2.isJaFoiDeterminadoOTempoDeSubida1090pt2()) {
                    this.tempoDesubida1090Tanque2.setText(Double.toString(timeMensureMaster2.getTempoDeSubida1090()));
                } else {
                    this.tempoDesubida1090Tanque2.setText("");
                }

                //imprimi o valor de subida de 0-100%
                if (this.timeMensureMaster2.isJaFoiDeterminadoOTempoDeSubida0100pt2()) {
                    this.tempoDesubida0100Tanque2.setText(Double.toString(timeMensureMaster2.getTempoDeSubida0100()));
                } else {
                    this.tempoDesubida0100Tanque2.setText("");
                }

                //overshoot e undershoot
                if (this.timeMensureMaster2.isJaFoiDeterminadoOTempoDeSubida0100pt2()) {
                    this.overShootTanque2.setText(Double.toString(timeMensureMaster2.getOvershootper()));
                    this.underShootTanque2.setText(Double.toString(timeMensureMaster2.getUndershootper()));
                }

                //tempo de acomodação de 10%
                if (this.timeMensureMaster2.isJaFoiDeterminadoTempoAcomodacao10()) {
                    this.tempoDeAcomodacao10Tanque2.setText(Double.toString(timeMensureMaster2.getTempoAcomodacao10()));
                } else {
                }


                //tempo de acomodação de 5
                if (this.timeMensureMaster2.isJaFoiDeterminadoTempoAcomodacao5()) {
                    this.tempoDeAcomodacao5Tanque2.setText(Double.toString(timeMensureMaster2.getTempoAcomodacao5()));
                } else {
                    this.tempoDeAcomodacao5Tanque2.setText("");
                }

                //tempo de acomodação de 2%
                if (this.timeMensureMaster2.isJaFoiDeterminadoTempoAcomodacao2()) {
                    this.tempoDeAcomodacao2Tanque2.setText(Double.toString(timeMensureMaster2.getTempoAcomodacao2()));
                } else {
                    this.tempoDeAcomodacao2Tanque1.setText("");
                }



//                }


                /*
                 * if (this.PVector[23] > 1) { if
                 * (this.timeMensureMaster.isJaFoiDeterminadoTempoAcomodacao10())
                 * { try {
                 * this.overShootTanque1.setText(Double.toString(timeMensureMaster.getOvershootper()));
                 * this.underShootTanque1.setText(Double.toString(timeMensureMaster.getUndershootper()));
                 * this.tempoDePicoTanque1.setText(Double.toString(timeMensureMaster.getTempoDePico()));
                 * this.tempoDeAcomodacao10Tanque1.setText(Double.toString(timeMensureMaster.getTempoAcomodacao10()));
                 * } catch (Exception e) { System.out.println("error 2"); } }
                 * else { this.overShootTanque1.setText("");
                 * this.underShootTanque1.setText("");
                 * this.tempoDePicoTanque1.setText("");
                 * this.tempoDeAcomodacao10Tanque1.setText("");
                 *
                 * }
                 * }
                 */

//                if (this.PVector[16] > 1) {
                if (this.timeMensureMaster2.isJaFoiDeterminadoTempoAcomodacao10()) {
                    try {
                        this.tempoDePicoTanque2.setText(Double.toString(timeMensureMaster2.getTempoDePico()));

                    } catch (Exception e) {
                        System.out.println("error 22");
                    }
                } else {
                    this.overShootTanque2.setText("");
                    this.underShootTanque2.setText("");
                    this.tempoDePicoTanque2.setText("");
                    this.tempoDeAcomodacao10Tanque2.setText("");

                }
//                }



                /*
                 * if (this.PVector[23] > 1) { if
                 * (this.timeMensureMaster.isJaFoiDeterminadoTempoAcomodacao5())
                 * { try {
                 * this.tempoDeAcomodacao5Tanque1.setText(Double.toString(timeMensureMaster.getTempoAcomodacao5()));
                 * } catch (Exception e) { System.out.println("error 3"); } }
                 * else { this.tempoDeAcomodacao5Tanque1.setText(""); } }
                 */



                /*
                 * if (this.PVector[23] > 1) { if
                 * (this.timeMensureMaster.isJaFoiDeterminadoTempoAcomodacao2())
                 * { try {
                 * this.tempoDeAcomodacao2Tanque1.setText(Double.toString(timeMensureMaster.getTempoAcomodacao2()));
                 * } catch (Exception e) { System.out.println("error 4"); } }
                 * else { this.tempoDeAcomodacao2Tanque1.setText(""); } }
                 */




            }

        }
    }

    public void getParametersTimeTextFildTanque1(JTextField tempoDesubida0100Tanque1, JTextField tempoDesubida1090Tanque1,
            JTextField tempoDesubida02080Tanque1, JTextField tempoDePicoTanque1, JTextField overShootTanque1, JTextField underShootTanque1,
            JTextField tempoDeAcomodacao10Tanque1, JTextField tempoDeAcomodacao5Tanque1, JTextField tempoDeAcomodacao2Tanque1) {

        this.tempoDesubida0100Tanque1 = tempoDesubida0100Tanque1;
        this.tempoDesubida1090Tanque1 = tempoDesubida1090Tanque1;
        this.tempoDesubida02080Tanque1 = tempoDesubida02080Tanque1;
        this.tempoDePicoTanque1 = tempoDePicoTanque1;
        this.overShootTanque1 = overShootTanque1;
        this.underShootTanque1 = underShootTanque1;
        this.tempoDeAcomodacao10Tanque1 = tempoDeAcomodacao10Tanque1;
        this.tempoDeAcomodacao5Tanque1 = tempoDeAcomodacao5Tanque1;
        this.tempoDeAcomodacao2Tanque1 = tempoDeAcomodacao2Tanque1;

    }

    public void getParametersTimeTextFildTanque2(JTextField tempoDesubida0100Tanque2, JTextField tempoDesubida1090Tanque2,
            JTextField tempoDesubida02080Tanque2, JTextField tempoDePicoTanque2, JTextField overShootTanque2, JTextField underShootTanque2,
            JTextField tempoDeAcomodacao10Tanque2, JTextField tempoDeAcomodacao5Tanque2, JTextField tempoDeAcomodacao2Tanque2) {

        this.tempoDesubida0100Tanque2 = tempoDesubida0100Tanque2;
        this.tempoDesubida1090Tanque2 = tempoDesubida1090Tanque2;
        this.tempoDesubida02080Tanque2 = tempoDesubida02080Tanque2;
        this.tempoDePicoTanque2 = tempoDePicoTanque2;
        this.overShootTanque2 = overShootTanque2;
        this.underShootTanque2 = underShootTanque2;
        this.tempoDeAcomodacao10Tanque2 = tempoDeAcomodacao10Tanque2;
        this.tempoDeAcomodacao5Tanque2 = tempoDeAcomodacao5Tanque2;
        this.tempoDeAcomodacao2Tanque2 = tempoDeAcomodacao2Tanque2;

    }

    public JFreeChart getChart() {
        return chartSinal;
    }

    public void setChart(JFreeChart chart) {
        this.chartSinal = chart;
    }

    public ChartPanel getChartPanel() {
        return chartPanelSinal;
    }

    public void setChartPanel(ChartPanel chartPanel) {
        this.chartPanelSinal = chartPanel;
    }

    public XYSeriesCollection getDatas() {
        return datas;
    }

    public void setDatas(XYSeriesCollection datas) {
        this.datas = datas;
    }

    public void invisibleAllFirstpadded() {
        _panelGraficoErroAltura.setVisible(false);
        _panelGraficoSinal.setVisible(false);
        _panelGraficoTodos.setVisible(false);
    }

    public void visibleAllFirstpadded() {
        _panelGraficoErroAltura.setVisible(true);
        _panelGraficoSinal.setVisible(true);
        _panelGraficoTodos.setVisible(true);
    }

    public void visibleAllFirstpadded2() {
        //_panelGraficoAlturaTankque2.setVisible(true);
        _panelGraficoErroAltura.setVisible(true);
        _panelGraficoSinal.setVisible(true);
        _panelGraficoTodos.setVisible(true);
    }

    private void bigPanel(JPanel panel, ChartPanel chartPanel) {
        chartPanel.setPreferredSize(new java.awt.Dimension(640, 550));
        panel.setSize(640, 550);
    }

    private void smallPanel(JPanel panel, ChartPanel chartPanel) {
        chartPanel.setPreferredSize(new java.awt.Dimension(320, 190));
        panel.setSize(320, 190);
    }

    private void smallPanel2(JPanel panel, ChartPanel chartPanel) {
        chartPanel.setPreferredSize(new java.awt.Dimension(650, 190));
        panel.setSize(650, 190);
    }

    public void graficoDoNivelDosDoisTanques(boolean istrue) {
        if (istrue) {
            this.chartpanelAlturaTanque2.setSize(320, 190);

            this.chartPanelAltura.setSize(320, 190);
        } else {
            this.chartPanelAltura.setSize(650, 190);
        }

    }

    //public XYSeriesCollection getDatasSetPoint() {
    //    return datasSetPoint;
    //}
    //public void setDatasSetPoint(XYSeriesCollection datasSetPoint) {
    //    this.datasSetPoint = datasSetPoint;
    //}
    public JPanel getPanel() {
        return _panelGraficoSinal;
    }

    public void setPanel(JPanel panel) {
        this._panelGraficoSinal = panel;
    }

    public XYSeries getSeries() {
        return series;
    }

    public void setSeries(XYSeries series) {
        this.series = series;
    }

    public TimeMensureMaster getTimeMensureMaster() {
        return timeMensureMaster;
    }

    public void setTimeMensureMaster(TimeMensureMaster timeMensureMaster) {
        this.timeMensureMaster = timeMensureMaster;
    }

    public TimeMensureMaster getTimeMensureMaster2() {
        return timeMensureMaster2;
    }

    public void setTimeMensureMaster2(TimeMensureMaster timeMensureMaster2) {
        this.timeMensureMaster2 = timeMensureMaster2;
    }
}
