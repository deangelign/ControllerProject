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
    
    
    JPanel _panelGraficoSinal;
    JPanel _panelGraficoAltura;
    JPanel _panelGraficoSinalAPenas;
    JPanel _panelGraficoAlturaTankque2;
    JPanel _panelGraficoErroAltura;
    JPanel _panelGraficoTodos;
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

    public GrafoManager(JPanel panelTensão, JPanel panelAltura, final double[] PVector,
            JTextField valorTensao, JTextField valorAltura, JTextField valorTempo,
            JTextField fatorDeAceleracao, JTextField valorAlturaTanque2, JPanel panelGraficoAlturaTankque2,
            JPanel panelGraficoErroAltura, JTextField valorErrorAltura, JPanel panelTodos,
            JTextField valorAcumulativo, JTextField valorDerivativo) {

        this.taGrande = new boolean[5];
        this.valorAcumulado = valorAcumulativo;
        this.valorDerivativo = valorDerivativo;
        
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
        this._panelGraficoSinal = panelTensão;
        this._panelGraficoAltura = panelAltura;
        
        this._panelGraficoAlturaTankque2 = panelGraficoAlturaTankque2;
        this._panelGraficoErroAltura = panelGraficoErroAltura;
        this._panelGraficoTodos = panelTodos;
        //this.serialComLeitura = serial;

        this._panelGraficoAltura.setOpaque(false);
        this._panelGraficoAlturaTankque2.setOpaque(false);
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


        this.chartAltura = ChartFactory.createXYLineChart("Nível do tanque1",
                "t (s)", "Altura (cm)", datasAltura, PlotOrientation.VERTICAL, true, true, false);
        this.chartPanelAltura = new ChartPanel(chartAltura);
        this.chartPanelAltura.setPreferredSize(new java.awt.Dimension(650, 190));
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
        this.chartPanelTodos.setPreferredSize(new java.awt.Dimension(650, 190));
        XYPlot plotTodos = (XYPlot) chartTodos.getPlot();


        chartPanelSinal.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {

                if (taGrande[0]) {
                    taGrande[0] = false;

                    visibleAllFirstpadded();
                    smallPanel(_panelGraficoSinal, chartPanelSinal);

                    if (PVector[16] < 1) {
                        _panelGraficoAlturaTankque2.setVisible(false);
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


        this.chartPanelAltura.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                if (taGrande[1]) {
                    taGrande[1] = false;

                    //_panelGraficoAltura.setVisible(true);
                    visibleAllFirstpadded();
                    smallPanel(_panelGraficoAltura, chartPanelAltura);

                    if (PVector[16] < 1) {
                        _panelGraficoAlturaTankque2.setVisible(false);
                        smallPanel2(_panelGraficoAltura, chartPanelAltura);
                        _panelGraficoTodos.setVisible(false);
                    }
                    if (PVector[15] < 1) {
                        _panelGraficoErroAltura.setVisible(false);
                        _panelGraficoSinal.setSize(650, 190);
                        chartPanelSinal.setPreferredSize(new java.awt.Dimension(640, 190));

                    }

                }else {
                    taGrande[1] = true;
                    invisibleAllFirstpadded();
                    _panelGraficoAltura.setVisible(true);
                    bigPanel(_panelGraficoAltura, chartPanelAltura);
                }

            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });


        chartpanelAlturaTanque2.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                if (taGrande[2]) {
                    taGrande[2] = false;


                    //_panelGraficoAltura.setVisible(true);
                    visibleAllFirstpadded();
                    smallPanel(_panelGraficoAlturaTankque2, chartpanelAlturaTanque2);


                    if (PVector[16] < 1) {
                        _panelGraficoAlturaTankque2.setVisible(false);
                        _panelGraficoTodos.setVisible(false);
                    }
                    if (PVector[15] < 1) {
                        _panelGraficoErroAltura.setVisible(false);
                        _panelGraficoSinal.setSize(650, 190);
                        chartPanelSinal.setPreferredSize(new java.awt.Dimension(640, 190));

                    }



                } else {
                    taGrande[2] = true;

                    invisibleAllFirstpadded();
                    _panelGraficoAlturaTankque2.setVisible(true);
                    bigPanel(_panelGraficoAlturaTankque2, chartpanelAlturaTanque2);

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
                        _panelGraficoAlturaTankque2.setVisible(false);
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
                    visibleAllFirstpadded();
                    //smallPanel(_panelGraficoTodos, chartPanelTodos);
                    chartPanelTodos.setPreferredSize(new java.awt.Dimension(640, 190));
                    _panelGraficoTodos.setSize(640, 190);



                    if (PVector[16] < 1) {
                        _panelGraficoAlturaTankque2.setVisible(false);
                        _panelGraficoTodos.setVisible(false);
                    }

                    if (PVector[15] < 1) {
                        _panelGraficoErroAltura.setVisible(false);
                        _panelGraficoSinal.setSize(650, 190);
                        chartPanelSinal.setPreferredSize(new java.awt.Dimension(640, 190));
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

        plotTodos.setRenderer(1, rendererTodos1);
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

    public void refreshPanelHeight() {
        this._panelGraficoAltura.removeAll();
        this._panelGraficoAltura.setLayout(new java.awt.BorderLayout());
        this._panelGraficoAltura.add(chartPanelAltura, BorderLayout.CENTER);
        this._panelGraficoAltura.validate();

    }

    public void refreshPanelHeight2() {
        this._panelGraficoAlturaTankque2.removeAll();
        this._panelGraficoAlturaTankque2.setLayout(new java.awt.BorderLayout());
        this._panelGraficoAlturaTankque2.add(chartpanelAlturaTanque2, BorderLayout.CENTER);
        this._panelGraficoAlturaTankque2.validate();
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
                this.valorAcumulado.setText(Double.toString(this.PVector[19]));
                this.valorDerivativo.setText(Double.toString(this.PVector[20]));
                
                this.refreshPanel();

            }
//            i++;
//            System.out.println(i++);

        }
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
        _panelGraficoAlturaTankque2.setVisible(false);
        _panelGraficoErroAltura.setVisible(false);
        _panelGraficoSinal.setVisible(false);
        _panelGraficoTodos.setVisible(false);
        _panelGraficoAltura.setVisible(false);
    }

    public void visibleAllFirstpadded() {
        _panelGraficoAlturaTankque2.setVisible(true);
        _panelGraficoErroAltura.setVisible(true);
        _panelGraficoSinal.setVisible(true);
        _panelGraficoTodos.setVisible(true);
        _panelGraficoAltura.setVisible(true);
    }

    public void visibleAllFirstpadded2() {
        //_panelGraficoAlturaTankque2.setVisible(true);
        _panelGraficoErroAltura.setVisible(true);
        _panelGraficoSinal.setVisible(true);
        _panelGraficoTodos.setVisible(true);
        _panelGraficoAltura.setVisible(true);
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
            this._panelGraficoAlturaTankque2.setVisible(true);
            this._panelGraficoAlturaTankque2.setPreferredSize(new java.awt.Dimension(320, 190));
            this.chartpanelAlturaTanque2.setSize(320, 190);

            this._panelGraficoAltura.setVisible(true);
            this._panelGraficoAltura.setPreferredSize(new java.awt.Dimension(320, 190));
            this.chartPanelAltura.setSize(320, 190);
        } else {
            this._panelGraficoAlturaTankque2.setVisible(false);

            //this._panelGraficoAltura.setVisible(true);
            this._panelGraficoAltura.setPreferredSize(new java.awt.Dimension(650, 190));
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
