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
    JTextField valorAcumulativoEscravo;
    JTextField valorDerivativoEscravo;
    JTextField valorEstimadoTanque1;
    JTextField valorEstimadoTanque2;
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
    JPanel _panelGraficoTodos;
    
    XYSeries seriesTensaoNaBomba;
    XYSeries seriesAltura;
    XYSeries seriesSetPoint;
    XYSeries seriesAlturaTanque2;
    XYSeries seriesErrorAltura;
    XYSeries seriesSaidaControladorMestre;
    XYSeries seriesTensaoSemSaturacao;
    XYSeries seriesAcaoProporcional;
    XYSeries seriesAcaoIntregrativa;
    XYSeries seriesAcaoDerivativa;
    XYSeries SeriesEstimadorTanque1;
    XYSeries SeriesEstimadorTanque2;
    XYSeriesCollection datasTensaoNaBomba;
    XYSeriesCollection datasAltura;
    XYSeriesCollection datasSetPoint;
    XYSeriesCollection datasAlturaTanque2;
    XYSeriesCollection datasErrorAltura;
    XYSeriesCollection datasSaidaControladorMestre;
    XYSeriesCollection datasTensaoSemSaturacao;
    XYSeriesCollection datasAcaoProporcional;
    XYSeriesCollection datasAcaoIntregrativa;
    XYSeriesCollection datasAcaoDerivativa;
    XYSeriesCollection datasEstimadorTanque1;
    XYSeriesCollection datasEstimadorTanque2;
    XYPlot plotTodos;
    XYPlot plotSinal;
    ChartPanel chartPanelSinal;
    JFreeChart chartSinal;
    //double temperatura;
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
    int i = 0;

    public GrafoManager(JPanel panelTensão, final double[] PVector,
            JTextField valorTensao, JTextField valorAltura, JTextField valorTempo,
            JTextField fatorDeAceleracao, JTextField valorAlturaTanque2,
            JTextField valorErrorAltura, JPanel panelTodos,
            JTextField valorAcumulativo, JTextField valorDerivativo,
            JTextField valorAcumulativoEscravo, JTextField valorDerivativoEscravo,
            JTextField valorEstimadoTanque1, JTextField valorEstimadoTanque2) {


        this.valorEstimadoTanque1 = valorEstimadoTanque1;
        this.valorEstimadoTanque2 = valorEstimadoTanque2;

        this.valorAcumulativoEscravo = valorAcumulativoEscravo;
        this.valorDerivativoEscravo = valorDerivativoEscravo;

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

        this._panelGraficoTodos = panelTodos;
        //this.serialComLeitura = serial;

        this._panelGraficoSinal.setOpaque(false);
        this._panelGraficoTodos.setOpaque(false);

        //this.spn_setPoint = spinner;
        this.seriesTensaoNaBomba = new XYSeries("Sinal");
        this.seriesAltura = new XYSeries("Nível do tanque1");
        this.seriesSetPoint = new XYSeries("setpoint");
        this.seriesAlturaTanque2 = new XYSeries("Nível do tanque2");
        this.seriesErrorAltura = new XYSeries("Error");
        this.seriesSaidaControladorMestre = new XYSeries("Referência Escravo");
        this.seriesTensaoSemSaturacao = new XYSeries("Tensão sem saturação");
        this.seriesAcaoProporcional = new XYSeries("Ação Proporcional");
        this.seriesAcaoIntregrativa = new XYSeries("Ação Integrativa");
        this.seriesAcaoDerivativa = new XYSeries("Ação Derivativa");
        this.SeriesEstimadorTanque1 = new XYSeries("Estimador tanque1");
        this.SeriesEstimadorTanque2 = new XYSeries("Estimador tanque2");


        /*
         * this.seriesTensaoNaBomba.setMaximumItemCount(100);
         * this.seriesAltura.setMaximumItemCount(100);
         * this.seriesSetPoint.setMaximumItemCount(100);
         * this.seriesAlturaTanque2.setMaximumItemCount(100);
         * this.seriesErrorAltura.setMaximumItemCount(100);
         * this.seriesSaidaControladorMestre.setMaximumItemCount(100);
         * this.seriesTensaoSemSaturacao.setMaximumItemCount(100);
         * this.seriesAcaoProporcional.setMaximumItemCount(100);
         * this.seriesAcaoIntregrativa.setMaximumItemCount(100);
         * this.seriesAcaoDerivativa.setMaximumItemCount(100);
         */

        this.datasTensaoNaBomba = new XYSeriesCollection(seriesTensaoNaBomba);
        this.datasAltura = new XYSeriesCollection(seriesAltura);
        this.datasSetPoint = new XYSeriesCollection(seriesSetPoint);
        this.datasAlturaTanque2 = new XYSeriesCollection(seriesAlturaTanque2);
        this.datasErrorAltura = new XYSeriesCollection(seriesErrorAltura);
        this.datasSaidaControladorMestre = new XYSeriesCollection(seriesSaidaControladorMestre);
        this.datasTensaoSemSaturacao = new XYSeriesCollection(seriesTensaoSemSaturacao);
        this.datasAcaoProporcional = new XYSeriesCollection(seriesAcaoProporcional);
        this.datasAcaoIntregrativa = new XYSeriesCollection(seriesAcaoIntregrativa);
        this.datasAcaoDerivativa = new XYSeriesCollection(seriesAcaoDerivativa);

        this.datasEstimadorTanque1 = new XYSeriesCollection(SeriesEstimadorTanque1);
        this.datasEstimadorTanque2 = new XYSeriesCollection(SeriesEstimadorTanque2);



        this.chartSinal = ChartFactory.createXYLineChart("Sinal na bomba", "t (s)", "Amplitude (V)",
                datasTensaoNaBomba, PlotOrientation.VERTICAL, true, true, false);
        this.chartPanelSinal = new ChartPanel(chartSinal);
        this.chartPanelSinal.setPreferredSize(new java.awt.Dimension(650, 285));
        plotSinal = (XYPlot) chartSinal.getPlot();
        plotSinal.setBackgroundPaint(Color.lightGray);

        this.chartTodos = ChartFactory.createXYLineChart("Níveis", "t (s)", "Altura (cm)", datasAltura,
                PlotOrientation.VERTICAL, true, true, false);
        this.chartPanelTodos = new ChartPanel(chartTodos);
        this.chartPanelTodos.setPreferredSize(new java.awt.Dimension(650, 285));
        this.plotTodos = (XYPlot) chartTodos.getPlot();


        chartPanelSinal.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                if (PVector[27] > 1) {
                    if (taGrande[0]) {
                        taGrande[0] = false;

                        _panelGraficoTodos.setVisible(true);
                        _panelGraficoTodos.setSize(650, 285);
                        chartPanelTodos.setPreferredSize(new java.awt.Dimension(650, 285));


                        _panelGraficoSinal.setSize(650, 285);
                        chartPanelSinal.setPreferredSize(new java.awt.Dimension(650, 285));


                    } else {
                        taGrande[0] = true;
                        invisibleAllFirstpadded();
                        _panelGraficoSinal.setVisible(true);
                        bigPanel(_panelGraficoSinal, chartPanelSinal);
                    }
                } else {
                    taGrande[4] = true;
                }

            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });




        this.chartPanelTodos.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                if (PVector[27] > 1) {
                    if (taGrande[4]) {
                        taGrande[4] = false;

                        //_panelGraficoAltura.setVisible(true);
                        //visibleAllFirstpadded();
                        //smallPanel(_panelGraficoTodos, chartPanelTodos);

                        _panelGraficoTodos.setSize(650, 285);
                        chartPanelTodos.setPreferredSize(new java.awt.Dimension(650, 285));


                        _panelGraficoSinal.setSize(650, 285);
                        chartPanelSinal.setPreferredSize(new java.awt.Dimension(650, 285));
                        _panelGraficoSinal.setVisible(true);



                    } else {
                        taGrande[4] = true;

                        invisibleAllFirstpadded();
                        _panelGraficoTodos.setVisible(true);
                        bigPanel(_panelGraficoTodos, chartPanelTodos);

                    }

                } else {
                    taGrande[4] = true;
                }


            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
        });




        plotTodos.setDataset(1, this.datasSetPoint);
        plotTodos.setDataset(2, this.datasAlturaTanque2);
        plotTodos.setDataset(3, this.datasSaidaControladorMestre);
        plotTodos.setDataset(4, this.datasErrorAltura);
        plotTodos.setDataset(5, this.datasEstimadorTanque1);
        plotTodos.setDataset(6, this.datasEstimadorTanque2);

        plotSinal.setDataset(1, this.datasTensaoSemSaturacao);
        plotSinal.setDataset(2, this.datasAcaoProporcional);
        plotSinal.setDataset(3, this.datasAcaoIntregrativa);
        plotSinal.setDataset(4, this.datasAcaoDerivativa);

        XYLineAndShapeRenderer rendererTodos1 = new XYLineAndShapeRenderer(true, false);//altura tanque 1
        XYLineAndShapeRenderer rendererTodos2 = new XYLineAndShapeRenderer(true, false);//altura tanque 2
        XYLineAndShapeRenderer rendererTodos3 = new XYLineAndShapeRenderer(true, false);//referencia do mestre (caso seja usado o cascata)
        XYLineAndShapeRenderer rendererTodos4 = new XYLineAndShapeRenderer(true, false);//erro 
        XYLineAndShapeRenderer rendererTodos5 = new XYLineAndShapeRenderer(true, false);//erro
        XYLineAndShapeRenderer rendererTodos6 = new XYLineAndShapeRenderer(true, false);//erro

        XYLineAndShapeRenderer rendererSinal1 = new XYLineAndShapeRenderer(true, false);//valor sem saturação
        XYLineAndShapeRenderer rendererSinal2 = new XYLineAndShapeRenderer(true, false);//ação P
        XYLineAndShapeRenderer rendererSinal3 = new XYLineAndShapeRenderer(true, false);//ação I
        XYLineAndShapeRenderer rendererSinal4 = new XYLineAndShapeRenderer(true, false);//ação D 

        plotTodos.setRenderer(1, rendererTodos1);
        plotTodos.setRenderer(2, rendererTodos2);
        plotTodos.setRenderer(3, rendererTodos3);
        plotTodos.setRenderer(4, rendererTodos4);
        plotTodos.setRenderer(5, rendererTodos5);
        plotTodos.setRenderer(6, rendererTodos6);


        plotSinal.setRenderer(1, rendererSinal1);
        plotSinal.setRenderer(2, rendererSinal2);
        plotSinal.setRenderer(3, rendererSinal3);
        plotSinal.setRenderer(4, rendererSinal4);

        plotTodos.getRenderer(1).setSeriesPaint(0, Color.BLUE);
        plotTodos.getRenderer(1).setBaseSeriesVisibleInLegend(true);

        plotTodos.getRenderer(2).setSeriesPaint(0, Color.GREEN);
        plotTodos.getRenderer(2).setBaseSeriesVisibleInLegend(true);

        plotTodos.getRenderer(3).setSeriesPaint(0, Color.YELLOW);
        plotTodos.getRenderer(3).setBaseSeriesVisibleInLegend(false);
        plotTodos.getRenderer(3).setBaseSeriesVisible(false);

        plotTodos.getRenderer(4).setSeriesPaint(0, Color.MAGENTA);
        plotTodos.getRenderer(4).setBaseSeriesVisible(false);
        plotTodos.getRenderer(4).setBaseSeriesVisibleInLegend(false);

        plotTodos.getRenderer(5).setSeriesPaint(0, Color.BLACK);
        plotTodos.getRenderer(5).setBaseSeriesVisible(false);
        plotTodos.getRenderer(5).setBaseSeriesVisibleInLegend(false);

        plotTodos.getRenderer(6).setSeriesPaint(0, Color.ORANGE);
        plotTodos.getRenderer(6).setBaseSeriesVisible(false);
        plotTodos.getRenderer(6).setBaseSeriesVisibleInLegend(false);



        plotSinal.getRenderer(1).setSeriesPaint(0, Color.BLUE);
        plotSinal.getRenderer(1).setBaseSeriesVisible(false);
        plotSinal.getRenderer(1).setBaseSeriesVisibleInLegend(false);

        plotSinal.getRenderer(2).setSeriesPaint(0, Color.GREEN);
        plotSinal.getRenderer(2).setBaseSeriesVisible(false);
        plotSinal.getRenderer(2).setBaseSeriesVisibleInLegend(false);

        plotSinal.getRenderer(3).setSeriesPaint(0, Color.YELLOW);
        plotSinal.getRenderer(3).setBaseSeriesVisible(false);
        plotSinal.getRenderer(3).setBaseSeriesVisibleInLegend(false);

        plotSinal.getRenderer(4).setSeriesPaint(0, Color.MAGENTA);
        plotSinal.getRenderer(4).setBaseSeriesVisible(false);
        plotSinal.getRenderer(4).setBaseSeriesVisibleInLegend(false);



    }

    public void addPonto(double X, double Y) {
        seriesTensaoNaBomba.add(X, Y);
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

    public void addPontoSeriesSaidaControladorMestre(double X, double Y) {
        this.seriesSaidaControladorMestre.add(X, Y);
    }

    public void addPontoSeriesTensaoSemSaturacao(double X, double Y) {
        this.seriesTensaoSemSaturacao.add(X, Y);
    }

    public void addPontoSeriesAcaoProporcional(double X, double Y) {
        this.seriesAcaoProporcional.add(X, Y);
    }

    public void addPontoSeriesAcaoIntegrativa(double X, double Y) {
        this.seriesAcaoIntregrativa.add(X, Y);
    }

    public void addPontoSeriesAcaoDerivativa(double X, double Y) {
        this.seriesAcaoDerivativa.add(X, Y);
    }

    public void addPontoSeriesEstimadorTanque1(double X, double Y) {
        this.SeriesEstimadorTanque1.add(X, Y);
    }

    public void addPontoSeriesEstimadorTanque2(double X, double Y) {
        this.SeriesEstimadorTanque2.add(X, Y);
    }

    public void refreshPanel() {

        //double aux = 100;
        this.refreshPanelTension();
        this.refreshPanelGraficoTodos();

//        try {
//            this.taxaDeUpdate_miliSegundos = Integer.parseInt(this.fatorDeAceleracao.getText());
//        } catch (Exception e) {
//        }



//        try {
//            Thread.sleep(this.taxaDeUpdate_miliSegundos);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(GrafoManager.class.getName()).log(Level.SEVERE, null, ex);
//        }

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
        this.seriesTensaoNaBomba.clear();
        this.seriesAltura.clear();
        this.seriesSetPoint.clear();
        this.seriesAlturaTanque2.clear();
        this.seriesErrorAltura.clear();
        this.seriesSaidaControladorMestre.clear();
        this.seriesTensaoSemSaturacao.clear();
        this.seriesAcaoProporcional.clear();
        this.seriesAcaoIntregrativa.clear();
        this.seriesAcaoDerivativa.clear();
        this.SeriesEstimadorTanque1.clear();
        this.SeriesEstimadorTanque2.clear();
        //this.addPonto(0, 0);
        //this.addPontoSeriesAltura(0, 0);
        //this.addPontoSetPoint(0, 0);
        //this.addPontoSeriesAlturatanque2(0, 0);
    }

    public void myClear2() {
        this.seriesTensaoNaBomba.clear();
        this.seriesAltura.clear();
        this.seriesSetPoint.clear();
        this.seriesAlturaTanque2.clear();
        this.seriesErrorAltura.clear();
        this.seriesSaidaControladorMestre.clear();
        this.seriesTensaoSemSaturacao.clear();
        this.seriesAcaoProporcional.clear();
        this.seriesAcaoIntregrativa.clear();
        this.seriesAcaoDerivativa.clear();
        this.SeriesEstimadorTanque1.clear();
        this.SeriesEstimadorTanque2.clear();


    }

    public void run() {


        this.refreshPanel();

        double altura;
        double tempoS;
        while (true) {


            if (this.PVector[3] < 1) {



                this.addPonto(this.PVector[0], this.PVector[1]);
                this.addPontoSeriesAltura(this.PVector[0], this.PVector[2]);

                if (this.PVector[27] > 1) {
                    this.addPontoSeriesTensaoSemSaturacao(this.PVector[0], this.PVector[28]);
                    this.addPontoSeriesAcaoProporcional(this.PVector[0], this.PVector[29]);
                    this.addPontoSeriesAcaoIntegrativa(this.PVector[0], this.PVector[30]);
                    this.addPontoSeriesAcaoDerivativa(this.PVector[0], this.PVector[31]);
                }

                if (PVector[16] > 1) {
                    this.addPontoSeriesAlturatanque2(this.PVector[0], this.PVector[5]);
                }
                if (PVector[15] > 1) {
                    this.addPontoSetPoint(this.PVector[0], this.PVector[14]);
                    this.addPontoSeriesErrorAltura(this.PVector[0], PVector[13]);
                }

                if (PVector[25] > 1) {
                    this.addPontoSeriesSaidaControladorMestre(this.PVector[0], this.PVector[26]);
                }
                
                if(PVector[39] > 1){
                    this.addPontoSeriesEstimadorTanque1(this.PVector[0],this.PVector[35] );
                    this.addPontoSeriesEstimadorTanque2(this.PVector[0], this.PVector[36]);
                }

                this.valorTensao.setText(Double.toString(this.PVector[1]));
                this.valorAltura.setText(Double.toString(this.PVector[2]));
                this.valorTempo.setText(Double.toString(this.PVector[0]));
                this.valorAlturaTanque2.setText(Double.toString(this.PVector[5]));
                this.valorErrorAltura.setText(Double.toString(this.PVector[13]));
                this.valorAcumulado.setText(Double.toString(this.PVector[19]));
                this.valorDerivativo.setText(Double.toString(this.PVector[20]));
                this.valorAcumulativoEscravo.setText(Double.toString(this.PVector[33]));
                this.valorDerivativoEscravo.setText(Double.toString(this.PVector[34]));
                this.valorEstimadoTanque1.setText(Double.toString(this.PVector[35]));
                this.valorEstimadoTanque2.setText(Double.toString(this.PVector[36]));

                this.refreshPanel();





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
        return datasTensaoNaBomba;
    }

    public void setDatas(XYSeriesCollection datas) {
        this.datasTensaoNaBomba = datas;
    }

    public void invisibleAllFirstpadded() {
        _panelGraficoSinal.setVisible(false);
        _panelGraficoTodos.setVisible(false);
    }

    public void visibleAllFirstpadded() {
        _panelGraficoSinal.setVisible(true);
        _panelGraficoTodos.setVisible(true);
    }

    public void visibleAllFirstpadded2() {
        //_panelGraficoAlturaTankque2.setVisible(true);
        _panelGraficoSinal.setVisible(true);
        _panelGraficoTodos.setVisible(true);
    }

    private void bigPanel(JPanel panel, ChartPanel chartPanel) {
        chartPanel.setPreferredSize(new java.awt.Dimension(650, 570));
        panel.setSize(650, 570);
    }

    private void smallPanel(JPanel panel, ChartPanel chartPanel) {
        chartPanel.setPreferredSize(new java.awt.Dimension(320, 190));
        panel.setSize(320, 190);
    }

    private void smallPanel2(JPanel panel, ChartPanel chartPanel) {
        chartPanel.setPreferredSize(new java.awt.Dimension(650, 190));
        panel.setSize(650, 190);
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
        return seriesTensaoNaBomba;
    }

    public void setSeries(XYSeries series) {
        this.seriesTensaoNaBomba = series;
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

    public void setPlotTodos(XYPlot plotTodos) {
        this.plotTodos = plotTodos;
    }

    public XYPlot getPlotTodos() {
        return plotTodos;
    }

    public XYPlot getPlotSinal() {
        return plotSinal;
    }

    public void setPlotSinal(XYPlot plotSinal) {
        this.plotSinal = plotSinal;
    }
}
