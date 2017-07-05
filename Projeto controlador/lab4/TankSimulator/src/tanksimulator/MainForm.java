/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksimulator;

import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.plaf.IconUIResource;

/**
 *
 * @author Deangelo
 */
public class MainForm extends javax.swing.JFrame {

    FormManager formManager;
    SimulatorConection simulatorConection;
    GeradorDeSinais geradorDeSinais;
    GrafoManager grafoManager;
    Thread t;
    Thread TGraphics;
    //transit
    //0 - tempo atual
    //1 - valor da tensão enviada
    //2 - nivel do tanque 1
    //3 - semaforo utilizado entre simulatorConection e GrafoManager.
    //4 - variavel de controle para a trava supeior
    //5 - nivel do tanque 2
    //6 - nivel do tanque 3
    //7 - nivel do tanque 4
    //8 - nivel do tanque 5
    //9 - nivel do tanque 6
    //10 - nivel do tanque 7
    //11 - nivel do tanque 8
    //12 - erro Volts
    //13 - erro Altura
    //14 - valor setpoint
    //15 - malhaAberta < 1...malha fechada > 1
    //16 - flag que indica se o canal 2 esta sendo usado
    //17 - variavel de controle para a trava inferior
    //18 - variavel de controle para a trava de entumpimento
    //19 - valor acumulado
    //20 - valor derivativo
    //21 - flag que indica se esta sendo usada as constantes de tempo 0 nao esta sendo usado 1 esta sendo usado
    //22 - flag que indica o metodo de integração 1 > x trapezio 1 < x retangulo
    //23 - flag que indica se o canal 1 esta sendo usado
    //24 - controle do nivel do tanque 1 quando x < 1. controle do nivel do tanque 2 quando x > 1
    //25 - flag que indica se esta sendo usado o controle cascata (utilizada para plotar o grafico)
    //      x < 1 não esta sendo uado; x > 1 esta sendo usado
    //26 - valor de saida do controlador mestre para um sistema em cascata
    //27 - flag que indica se esta sendo usado graficos de tensão.  x < 1 não esta sendo uado; x > 1 esta sendo usado
    //28 - valor da tensão sem saturação
    //29 - ação proporcional
    //30 - ação integrativa
    //31 - ação derivativa
    double transit[];
    boolean aux, aux2;
    TimeMensureMaster timeMensureMaster;
    TimeMensureMaster timeMensureMaster2;

    public MainForm() {
        initComponents();

        aux2 = false;
        timeMensureMaster = new TimeMensureMaster();
        timeMensureMaster2 = new TimeMensureMaster();

        this.Radio_OpenSytem.setSelected(true);
        this.channel1_Read.setSelected(true);
        this.channel1_Write.setSelected(true);


        this.txtF_setPoint.setEnabled(false);
        this.txtF_kpMestre.setEnabled(false);
        this.txtF_kiMestre.setEnabled(false);
        this.txtF_kdMestre.setEnabled(false);
        this.cmb_tipoDeControle.setEnabled(false);
        this.chbx_constantesDeTempo.setEnabled(false);

        //this.lbl_setPoint.setVisible(false);
        //this.txtF_setPoint.setVisible(false);
        //this.lbl_amplitude.setVisible(false);
        this.lbl_offset.setVisible(false);
        this.lbl_periodo.setVisible(false);
        //this.txtF_amplitude.setVisible(false);
        this.txtF_offset.setVisible(false);
        this.txtF_Periodo.setVisible(false);

        this.pnl_tanqueDeControle.setVisible(false);



        this.txtF_Periodo.setText("20.0");
        this.txtF_offset.setText("0.0");
        this.txtF_amplitude.setText("0.0");
        this.txtF_fatorAceleracao.setText("0");
        this.txtF_ip.setText("localhost");
        this.txtF_porta.setText("20081");

        this.transit = new double[32];
        this.transit[3] = 2;
        this.transit[15] = 0;
        this.transit[21] = 0;
        this.transit[23] = 2;
        this.transit[25] = 0;
        this.transit[27] = 2;


        this.grafoManager = new GrafoManager(this.pnl_GraficoSinal, this.transit,
                this.txtF_valorTensao, this.txtF_valorNivel, this.txtF_tempo, this.txtF_fatorAceleracao, this.txtF_valorNivelTanque2,
                this.txtF_errorAltura, this.pnl_grafico_todos,
                this.txtF_fatorIntegrativo, this.txtF_fatorDerivativo);
        this.grafoManager.setTimeMensureMaster(this.timeMensureMaster);
        this.grafoManager.setTimeMensureMaster2(this.timeMensureMaster2);
        this.grafoManager.getParametersTimeTextFildTanque1(txtF_tempoDeSubida0100Tanque1, txtF_tempoDeSubida1090Tanque1, txtF_tempoDeSubida2080Tanque1, txtF_tempoDePicoTanque1, txtF_overshootTanque1, txtF_underShootTanque1, txtF_tempoDeAcomodacao10Tanque1, txtF_tempoDeAcomodacao5Tanque1, txtF_tempoDeAcomodacao2Tanque1);
        this.grafoManager.getParametersTimeTextFildTanque2(txtF_tempoDeSubida0100Tanque2, txtF_tempoDeSubida1090Tanque2, txtF_tempoDeSubida2080Tanque2, txtF_tempoDePicoTanque2, txtF_overshootTanque2, txtF_underShootTanque2, txtF_tempoDeAcomodacao10Tanque2, txtF_tempoDeAcomodacao5Tanque2, txtF_tempoDeAcomodacao2Tanque2);


        this.TGraphics = new Thread(this.grafoManager);
        this.TGraphics.start();

        this.formManager = new FormManager();

        //this.simulatorConection = new SimulatorConection("localhost", 20081, 0, 0, this.txtF_amplitude, 
        //        this.txtF_offset, this.txtF_Periodo, 0, transit);
        this.simulatorConection = new SimulatorConection();
        this.simulatorConection.setTimeMensureMaster(this.timeMensureMaster);
        this.simulatorConection.setTimeMensureMaster2(this.timeMensureMaster2);
        //this.simulatorConection.setTensionValue(1);
        this.t = new Thread(simulatorConection);
        //this.t.setPriority(Thread.MAX_PRIORITY);
        this.t.start();



        aux = false;
        this.pnl_grafico_todos.setVisible(true);
//      this.pnl_GraficoSinal.setVisible(false);

        this.chebx_exibirSetpoint.setEnabled(false);
        this.chebx_Erro.setEnabled(false);
        this.chebx_exibirNivelDoTanque2.setEnabled(false);

        //desabilita o textfields do escravo
        this.txtF_kpEscravo.setEnabled(false);
        this.txtF_kiEscravo.setEnabled(false);
        this.txtF_kdEscravo.setEnabled(false);
        this.cmb_tipoDeControleEscravo.setEnabled(false);

        //deixa invisivel os textfield do escravo
        this.txtF_kpEscravo.setVisible(false);
        this.txtF_kiEscravo.setVisible(false);
        this.txtF_kdEscravo.setVisible(false);

        this.lbl_kpEscravo.setVisible(false);
        this.lbl_kiEscravo.setVisible(false);
        this.lbl_kdEscravo.setVisible(false);

        this.lbl_mestre.setVisible(false);
        this.lbl_escravo.setVisible(false);

        this.lbl_tipoDeControEscravo.setVisible(false);
        this.cmb_tipoDeControleEscravo.setVisible(false);

        //alguns chexkbox serao ocultados
        //this.chebx_tensaoAplicada.setVisible(false);
        //this.chebx_acaoProporcional.setVisible(false);
        //this.chebx_acaoIntagral.setVisible(false);
        //this.chebx_acaoDerivatica.setVisible(false);

        this.lbl_advertencia.setVisible(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        pnl_grafico_aba1 = new javax.swing.JPanel();
        pnl_grafico_todos = new javax.swing.JPanel();
        pnl_GraficoSinal = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        lbl_tensao = new javax.swing.JLabel();
        txtF_valorTensao = new javax.swing.JTextField();
        lbl_nivel = new javax.swing.JLabel();
        txtF_valorNivel = new javax.swing.JTextField();
        lbl_tempo = new javax.swing.JLabel();
        txtF_tempo = new javax.swing.JTextField();
        lbl_saida_tensao_volts = new javax.swing.JLabel();
        lbl_saida_nivel_centimetro = new javax.swing.JLabel();
        lbl_saida_tempo_segundo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtF_errorAltura = new javax.swing.JTextField();
        lbl_saida_erro_centimetro = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtF_valorNivelTanque2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtF_fatorIntegrativo = new javax.swing.JTextField();
        txtF_fatorDerivativo = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        lbl_tempoDeSubida0100Tanque1 = new javax.swing.JLabel();
        txtF_tempoDeSubida0100Tanque1 = new javax.swing.JTextField();
        lbl_tempoDePicoTanque1 = new javax.swing.JLabel();
        txtF_tempoDePicoTanque1 = new javax.swing.JTextField();
        lbl_overShootTanque1 = new javax.swing.JLabel();
        txtF_overshootTanque1 = new javax.swing.JTextField();
        lbl_tempoDeSubida1090Tanque1 = new javax.swing.JLabel();
        txtF_tempoDeSubida1090Tanque1 = new javax.swing.JTextField();
        lbl_tempoDeSubida2080Tanque1 = new javax.swing.JLabel();
        txtF_tempoDeSubida2080Tanque1 = new javax.swing.JTextField();
        lbl_underShootTanque1 = new javax.swing.JLabel();
        lbl_tempoDeAcomodacao10Tanque1 = new javax.swing.JLabel();
        lbl_tempoDeAcomodacao5Tanque1 = new javax.swing.JLabel();
        lbl_tempoDeAcomodacao2Tanque1 = new javax.swing.JLabel();
        txtF_underShootTanque1 = new javax.swing.JTextField();
        txtF_tempoDeAcomodacao10Tanque1 = new javax.swing.JTextField();
        txtF_tempoDeAcomodacao5Tanque1 = new javax.swing.JTextField();
        txtF_tempoDeAcomodacao2Tanque1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        lbl_tempoDeSubida0100Tanque2 = new javax.swing.JLabel();
        txtF_tempoDeSubida0100Tanque2 = new javax.swing.JTextField();
        lbl_tempoDePicoTanque2 = new javax.swing.JLabel();
        txtF_tempoDePicoTanque2 = new javax.swing.JTextField();
        lbl_overShootTanque2 = new javax.swing.JLabel();
        txtF_overshootTanque2 = new javax.swing.JTextField();
        lbl_tempoDeSubida1090Tanque2 = new javax.swing.JLabel();
        txtF_tempoDeSubida1090Tanque2 = new javax.swing.JTextField();
        lbl_tempoDeSubida2080Tanque2 = new javax.swing.JLabel();
        txtF_tempoDeSubida2080Tanque2 = new javax.swing.JTextField();
        lbl_underShootTanque2 = new javax.swing.JLabel();
        lbl_tempoDeAcomodacao10Tanque2 = new javax.swing.JLabel();
        lbl_tempoDeAcomodacao5Tanque2 = new javax.swing.JLabel();
        lbl_tempoDeAcomodacao2Tanque2 = new javax.swing.JLabel();
        txtF_underShootTanque2 = new javax.swing.JTextField();
        txtF_tempoDeAcomodacao10Tanque2 = new javax.swing.JTextField();
        txtF_tempoDeAcomodacao5Tanque2 = new javax.swing.JTextField();
        txtF_tempoDeAcomodacao2Tanque2 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jTabbedPane7 = new javax.swing.JTabbedPane();
        jPanel14 = new javax.swing.JPanel();
        panel_canalDeLeitura = new javax.swing.JPanel();
        lbl_canalDeLeitura = new javax.swing.JLabel();
        channel1_Read = new javax.swing.JRadioButton();
        channel2_Read = new javax.swing.JRadioButton();
        channel3_Read = new javax.swing.JRadioButton();
        channel4_Read = new javax.swing.JRadioButton();
        channel5_Read = new javax.swing.JRadioButton();
        channel6_Read = new javax.swing.JRadioButton();
        channel7_Read = new javax.swing.JRadioButton();
        channel8_Read = new javax.swing.JRadioButton();
        panel_Escrita = new javax.swing.JPanel();
        lbl_canalDeEscrita = new javax.swing.JLabel();
        channel1_Write = new javax.swing.JRadioButton();
        channel2_Write = new javax.swing.JRadioButton();
        channel3_Write = new javax.swing.JRadioButton();
        channel4_Write = new javax.swing.JRadioButton();
        channel5_Write = new javax.swing.JRadioButton();
        channel6_Write = new javax.swing.JRadioButton();
        channel7_Write = new javax.swing.JRadioButton();
        channel8_Write = new javax.swing.JRadioButton();
        pnl_malha = new javax.swing.JPanel();
        lbl_Malha = new javax.swing.JLabel();
        Radio_OpenSytem = new javax.swing.JRadioButton();
        Radio_ClosedSystem = new javax.swing.JRadioButton();
        pnl_tanqueDeControle = new javax.swing.JPanel();
        lbl_Malha1 = new javax.swing.JLabel();
        Radio_selectedTank1 = new javax.swing.JRadioButton();
        Radio_selectedTank2 = new javax.swing.JRadioButton();
        jPanel15 = new javax.swing.JPanel();
        lbl_configuracoes_ip = new javax.swing.JLabel();
        txtF_ip = new javax.swing.JTextField();
        lbl_configuracoes_porta = new javax.swing.JLabel();
        txtF_porta = new javax.swing.JTextField();
        lbl_configuracoes_conexao = new javax.swing.JLabel();
        btn_refresh = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lbl_tipoDeControle = new javax.swing.JLabel();
        cmb_tipoDeControle = new javax.swing.JComboBox();
        lbl_setPoint = new javax.swing.JLabel();
        lbl_kpMestre = new javax.swing.JLabel();
        txtF_kpMestre = new javax.swing.JTextField();
        lbl_kiMestre = new javax.swing.JLabel();
        txtF_kiMestre = new javax.swing.JTextField();
        lbl_kdMestre = new javax.swing.JLabel();
        txtF_kdMestre = new javax.swing.JTextField();
        txtF_setPoint = new javax.swing.JTextField();
        lbl_mestre = new javax.swing.JLabel();
        lbl_kpEscravo = new javax.swing.JLabel();
        txtF_kpEscravo = new javax.swing.JTextField();
        lbl_kiEscravo = new javax.swing.JLabel();
        txtF_kiEscravo = new javax.swing.JTextField();
        lbl_kdEscravo = new javax.swing.JLabel();
        txtF_kdEscravo = new javax.swing.JTextField();
        lbl_escravo = new javax.swing.JLabel();
        lbl_tipoDeControEscravo = new javax.swing.JLabel();
        cmb_tipoDeControleEscravo = new javax.swing.JComboBox();
        pnl_entradaMalhaAberta = new javax.swing.JPanel();
        lbl_sinal = new javax.swing.JLabel();
        cmbx_tipoDeSinal = new javax.swing.JComboBox();
        lbl_amplitude = new javax.swing.JLabel();
        txtF_amplitude = new javax.swing.JTextField();
        lbl_offset = new javax.swing.JLabel();
        txtF_offset = new javax.swing.JTextField();
        lbl_periodo = new javax.swing.JLabel();
        txtF_Periodo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        cmb_serieOuCascata = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        lbl_fatorDeAceleracao = new javax.swing.JLabel();
        txtF_fatorAceleracao = new javax.swing.JTextField();
        lbl_grafico = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        chbx_constantesDeTempo = new javax.swing.JCheckBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        chebx_exibirNivelDoTanque1 = new javax.swing.JCheckBox();
        chebx_exibirNivelDoTanque2 = new javax.swing.JCheckBox();
        chebx_exibirSetpoint = new javax.swing.JCheckBox();
        chebx_Erro = new javax.swing.JCheckBox();
        chebx_graficoSinal = new javax.swing.JCheckBox();
        chebx_graficoReferenciaEscravo = new javax.swing.JCheckBox();
        chebx_tensaoAplicadaSemSaturacao = new javax.swing.JCheckBox();
        chebx_acaoProporcional = new javax.swing.JCheckBox();
        chebx_acaoIntagral = new javax.swing.JCheckBox();
        chebx_acaoDerivatica = new javax.swing.JCheckBox();
        chebx_Tensao = new javax.swing.JCheckBox();
        btn_swithOnOff = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbl_advertencia = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lbl_conexao_leitura0 = new javax.swing.JLabel();
        lbl_conexao_leitura1 = new javax.swing.JLabel();
        lbl_conexao_leitura2 = new javax.swing.JLabel();
        lbl_conexao_leitura3 = new javax.swing.JLabel();
        lbl_conexao_leitura4 = new javax.swing.JLabel();
        lbl_conexao_leitura5 = new javax.swing.JLabel();
        lbl_conexao_leitura6 = new javax.swing.JLabel();
        lbl_conexao_leitura7 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lbl_conexao_Escrita0 = new javax.swing.JLabel();
        lbl_conexao_Escrita1 = new javax.swing.JLabel();
        lbl_conexao_Escrita2 = new javax.swing.JLabel();
        lbl_conexao_Escrita3 = new javax.swing.JLabel();
        lbl_conexao_Escrita4 = new javax.swing.JLabel();
        lbl_conexao_Escrita5 = new javax.swing.JLabel();
        lbl_conexao_Escrita6 = new javax.swing.JLabel();
        lbl_conexao_Escrita7 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        pnl_grafico_aba1.setBackground(new java.awt.Color(204, 255, 255));
        pnl_grafico_aba1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnl_grafico_aba1.setForeground(new java.awt.Color(153, 255, 255));

        pnl_grafico_todos.setBackground(new java.awt.Color(255, 255, 51));
        pnl_grafico_todos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnl_grafico_todosLayout = new javax.swing.GroupLayout(pnl_grafico_todos);
        pnl_grafico_todos.setLayout(pnl_grafico_todosLayout);
        pnl_grafico_todosLayout.setHorizontalGroup(
            pnl_grafico_todosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        pnl_grafico_todosLayout.setVerticalGroup(
            pnl_grafico_todosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        pnl_GraficoSinal.setBackground(new java.awt.Color(255, 153, 153));
        pnl_GraficoSinal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnl_GraficoSinalLayout = new javax.swing.GroupLayout(pnl_GraficoSinal);
        pnl_GraficoSinal.setLayout(pnl_GraficoSinalLayout);
        pnl_GraficoSinalLayout.setHorizontalGroup(
            pnl_GraficoSinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnl_GraficoSinalLayout.setVerticalGroup(
            pnl_GraficoSinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnl_grafico_aba1Layout = new javax.swing.GroupLayout(pnl_grafico_aba1);
        pnl_grafico_aba1.setLayout(pnl_grafico_aba1Layout);
        pnl_grafico_aba1Layout.setHorizontalGroup(
            pnl_grafico_aba1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_grafico_aba1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_grafico_aba1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_grafico_todos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_GraficoSinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(256, Short.MAX_VALUE))
        );
        pnl_grafico_aba1Layout.setVerticalGroup(
            pnl_grafico_aba1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_grafico_aba1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_grafico_todos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnl_GraficoSinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(503, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Gráficos", pnl_grafico_aba1);

        jTabbedPane5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Saida", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Aharoni", 3, 14))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(204, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbl_tensao.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tensao.setText("Tensão");

        txtF_valorTensao.setEnabled(false);

        lbl_nivel.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_nivel.setText("Nível 1");

        txtF_valorNivel.setEditable(false);
        txtF_valorNivel.setEnabled(false);

        lbl_tempo.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        lbl_tempo.setText("Tempo");

        txtF_tempo.setEditable(false);
        txtF_tempo.setEnabled(false);

        lbl_saida_tensao_volts.setText("V");

        lbl_saida_nivel_centimetro.setText("cm");

        lbl_saida_tempo_segundo.setText("s");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel6.setText("Erro");

        txtF_errorAltura.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        txtF_errorAltura.setEnabled(false);

        lbl_saida_erro_centimetro.setText("cm");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setText("Nível 2");

        txtF_valorNivelTanque2.setEnabled(false);

        jLabel3.setText("cm");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel7.setText("Acumulativo");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel8.setText("Derivativo");

        txtF_fatorIntegrativo.setEnabled(false);

        txtF_fatorDerivativo.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lbl_tempo)
                        .addGap(7, 7, 7)
                        .addComponent(txtF_tempo))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_tensao)
                            .addComponent(lbl_nivel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtF_valorTensao, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                            .addComponent(txtF_valorNivel)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtF_valorNivelTanque2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lbl_saida_tensao_volts, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(57, 57, 57)
                        .addComponent(txtF_errorAltura, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_saida_erro_centimetro)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(lbl_saida_nivel_centimetro)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(lbl_saida_tempo_segundo)
                                    .addGap(75, 75, 75))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtF_fatorDerivativo, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(txtF_fatorIntegrativo))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tensao)
                    .addComponent(lbl_saida_tensao_volts)
                    .addComponent(txtF_valorTensao)
                    .addComponent(jLabel6)
                    .addComponent(txtF_errorAltura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_saida_erro_centimetro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_nivel, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtF_valorNivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtF_valorNivelTanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_saida_nivel_centimetro))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_tempo)
                            .addComponent(txtF_tempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_saida_tempo_segundo)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtF_fatorIntegrativo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtF_fatorDerivativo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(112, 112, 112))
        );

        jTabbedPane5.addTab("Geral", jPanel3);

        jPanel5.setBackground(new java.awt.Color(204, 255, 255));

        lbl_tempoDeSubida0100Tanque1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeSubida0100Tanque1.setText("Tempo de subida 0-100");

        txtF_tempoDeSubida0100Tanque1.setEnabled(false);

        lbl_tempoDePicoTanque1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDePicoTanque1.setText("Tempo de pico");

        txtF_tempoDePicoTanque1.setEnabled(false);

        lbl_overShootTanque1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_overShootTanque1.setText("Overshoot");

        txtF_overshootTanque1.setEnabled(false);

        lbl_tempoDeSubida1090Tanque1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeSubida1090Tanque1.setText("Tempo de subida 10-90");

        txtF_tempoDeSubida1090Tanque1.setEnabled(false);

        lbl_tempoDeSubida2080Tanque1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeSubida2080Tanque1.setText("Tempo de subida 20-80");

        txtF_tempoDeSubida2080Tanque1.setEnabled(false);

        lbl_underShootTanque1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_underShootTanque1.setText("Undershoot");

        lbl_tempoDeAcomodacao10Tanque1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeAcomodacao10Tanque1.setText("Tempo de acomodação 10%");

        lbl_tempoDeAcomodacao5Tanque1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeAcomodacao5Tanque1.setText("Tempo de acomodação 5%");

        lbl_tempoDeAcomodacao2Tanque1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeAcomodacao2Tanque1.setText("Tempo de acomodação 2%");

        txtF_underShootTanque1.setEnabled(false);

        txtF_tempoDeAcomodacao10Tanque1.setEnabled(false);

        txtF_tempoDeAcomodacao5Tanque1.setEnabled(false);

        txtF_tempoDeAcomodacao2Tanque1.setEnabled(false);

        jLabel12.setText("s");

        jLabel13.setText("s");

        jLabel14.setText("s");

        jLabel15.setText("s");

        jLabel16.setText("%");

        jLabel17.setText("%");

        jLabel18.setText("s");

        jLabel19.setText("s");

        jLabel20.setText("s");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_tempoDeSubida2080Tanque1)
                            .addComponent(lbl_tempoDePicoTanque1)
                            .addComponent(lbl_overShootTanque1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtF_tempoDePicoTanque1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtF_overshootTanque1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtF_tempoDeSubida2080Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lbl_tempoDeSubida1090Tanque1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtF_tempoDeSubida1090Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lbl_tempoDeSubida0100Tanque1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtF_tempoDeSubida0100Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)))
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lbl_tempoDeAcomodacao5Tanque1)
                        .addGap(18, 18, 18)
                        .addComponent(txtF_tempoDeAcomodacao5Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lbl_tempoDeAcomodacao2Tanque1)
                        .addGap(18, 18, 18)
                        .addComponent(txtF_tempoDeAcomodacao2Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_tempoDeAcomodacao10Tanque1)
                            .addComponent(lbl_underShootTanque1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtF_underShootTanque1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtF_tempoDeAcomodacao10Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tempoDeSubida0100Tanque1)
                    .addComponent(txtF_tempoDeSubida0100Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_underShootTanque1)
                    .addComponent(txtF_underShootTanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tempoDeSubida1090Tanque1)
                    .addComponent(txtF_tempoDeSubida1090Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_tempoDeAcomodacao10Tanque1)
                    .addComponent(txtF_tempoDeAcomodacao10Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tempoDeSubida2080Tanque1)
                    .addComponent(txtF_tempoDeSubida2080Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_tempoDeAcomodacao5Tanque1)
                    .addComponent(txtF_tempoDeAcomodacao5Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tempoDePicoTanque1)
                    .addComponent(txtF_tempoDePicoTanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_tempoDeAcomodacao2Tanque1)
                    .addComponent(txtF_tempoDeAcomodacao2Tanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel20))
                .addGap(4, 4, 4)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtF_overshootTanque1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_overShootTanque1)
                    .addComponent(jLabel16))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("Tanque 1", jPanel5);

        jPanel11.setBackground(new java.awt.Color(204, 255, 255));

        lbl_tempoDeSubida0100Tanque2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeSubida0100Tanque2.setText("Tempo de subida 0-100");

        txtF_tempoDeSubida0100Tanque2.setEnabled(false);

        lbl_tempoDePicoTanque2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDePicoTanque2.setText("Tempo de pico");

        txtF_tempoDePicoTanque2.setEnabled(false);

        lbl_overShootTanque2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_overShootTanque2.setText("Overshoot");

        txtF_overshootTanque2.setEnabled(false);

        lbl_tempoDeSubida1090Tanque2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeSubida1090Tanque2.setText("Tempo de subida 10-90");

        txtF_tempoDeSubida1090Tanque2.setEnabled(false);

        lbl_tempoDeSubida2080Tanque2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeSubida2080Tanque2.setText("Tempo de subida 20-80");

        txtF_tempoDeSubida2080Tanque2.setEnabled(false);

        lbl_underShootTanque2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_underShootTanque2.setText("Undershoot");

        lbl_tempoDeAcomodacao10Tanque2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeAcomodacao10Tanque2.setText("Tempo de acomodação 10%");

        lbl_tempoDeAcomodacao5Tanque2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeAcomodacao5Tanque2.setText("Tempo de acomodação 5%");

        lbl_tempoDeAcomodacao2Tanque2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_tempoDeAcomodacao2Tanque2.setText("Tempo de acomodação 2%");

        txtF_underShootTanque2.setEnabled(false);

        txtF_tempoDeAcomodacao10Tanque2.setEnabled(false);

        txtF_tempoDeAcomodacao5Tanque2.setEnabled(false);

        txtF_tempoDeAcomodacao2Tanque2.setEnabled(false);

        jLabel21.setText("s");

        jLabel22.setText("s");

        jLabel23.setText("s");

        jLabel24.setText("s");

        jLabel25.setText("%");

        jLabel26.setText("%");

        jLabel27.setText("s");

        jLabel28.setText("s");

        jLabel29.setText("s");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_tempoDeSubida2080Tanque2)
                            .addComponent(lbl_tempoDePicoTanque2)
                            .addComponent(lbl_overShootTanque2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(txtF_tempoDePicoTanque2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(txtF_overshootTanque2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel25))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(txtF_tempoDeSubida2080Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23))))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(lbl_tempoDeSubida1090Tanque2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtF_tempoDeSubida1090Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(lbl_tempoDeSubida0100Tanque2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtF_tempoDeSubida0100Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)))
                .addGap(31, 31, 31)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(lbl_tempoDeAcomodacao5Tanque2)
                        .addGap(18, 18, 18)
                        .addComponent(txtF_tempoDeAcomodacao5Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(lbl_tempoDeAcomodacao2Tanque2)
                        .addGap(18, 18, 18)
                        .addComponent(txtF_tempoDeAcomodacao2Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_tempoDeAcomodacao10Tanque2)
                            .addComponent(lbl_underShootTanque2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtF_underShootTanque2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtF_tempoDeAcomodacao10Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tempoDeSubida0100Tanque2)
                    .addComponent(txtF_tempoDeSubida0100Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_underShootTanque2)
                    .addComponent(txtF_underShootTanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tempoDeSubida1090Tanque2)
                    .addComponent(txtF_tempoDeSubida1090Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_tempoDeAcomodacao10Tanque2)
                    .addComponent(txtF_tempoDeAcomodacao10Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tempoDeSubida2080Tanque2)
                    .addComponent(txtF_tempoDeSubida2080Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_tempoDeAcomodacao5Tanque2)
                    .addComponent(txtF_tempoDeAcomodacao5Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tempoDePicoTanque2)
                    .addComponent(txtF_tempoDePicoTanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_tempoDeAcomodacao2Tanque2)
                    .addComponent(txtF_tempoDeAcomodacao2Tanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel29))
                .addGap(4, 4, 4)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtF_overshootTanque2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_overShootTanque2)
                    .addComponent(jLabel25))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("Tanque 2", jPanel11);

        jPanel13.setBackground(new java.awt.Color(204, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do sistema", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Aharoni", 3, 14))); // NOI18N

        jPanel14.setBackground(new java.awt.Color(204, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        panel_canalDeLeitura.setBackground(new java.awt.Color(204, 255, 204));
        panel_canalDeLeitura.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        lbl_canalDeLeitura.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_canalDeLeitura.setText("Canal de Leitura");

        channel1_Read.setBackground(new java.awt.Color(204, 255, 204));
        channel1_Read.setText("0");
        channel1_Read.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel1_ReadActionPerformed(evt);
            }
        });

        channel2_Read.setBackground(new java.awt.Color(204, 255, 204));
        channel2_Read.setText("1");
        channel2_Read.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel2_ReadActionPerformed(evt);
            }
        });

        channel3_Read.setBackground(new java.awt.Color(204, 255, 204));
        channel3_Read.setText("2");
        channel3_Read.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel3_ReadActionPerformed(evt);
            }
        });

        channel4_Read.setBackground(new java.awt.Color(204, 255, 204));
        channel4_Read.setText("3");
        channel4_Read.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel4_ReadActionPerformed(evt);
            }
        });

        channel5_Read.setBackground(new java.awt.Color(204, 255, 204));
        channel5_Read.setText("4");
        channel5_Read.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel5_ReadActionPerformed(evt);
            }
        });

        channel6_Read.setBackground(new java.awt.Color(204, 255, 204));
        channel6_Read.setText("6");
        channel6_Read.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel6_ReadActionPerformed(evt);
            }
        });

        channel7_Read.setBackground(new java.awt.Color(204, 255, 204));
        channel7_Read.setText("6");
        channel7_Read.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel7_ReadActionPerformed(evt);
            }
        });

        channel8_Read.setBackground(new java.awt.Color(204, 255, 204));
        channel8_Read.setText("7");
        channel8_Read.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel8_ReadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_canalDeLeituraLayout = new javax.swing.GroupLayout(panel_canalDeLeitura);
        panel_canalDeLeitura.setLayout(panel_canalDeLeituraLayout);
        panel_canalDeLeituraLayout.setHorizontalGroup(
            panel_canalDeLeituraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_canalDeLeituraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_canalDeLeituraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_canalDeLeituraLayout.createSequentialGroup()
                        .addComponent(channel4_Read)
                        .addGap(18, 18, 18)
                        .addComponent(channel8_Read))
                    .addGroup(panel_canalDeLeituraLayout.createSequentialGroup()
                        .addComponent(channel3_Read)
                        .addGap(18, 18, 18)
                        .addComponent(channel7_Read))
                    .addGroup(panel_canalDeLeituraLayout.createSequentialGroup()
                        .addComponent(channel1_Read)
                        .addGap(18, 18, 18)
                        .addComponent(channel5_Read))
                    .addGroup(panel_canalDeLeituraLayout.createSequentialGroup()
                        .addComponent(channel2_Read)
                        .addGap(18, 18, 18)
                        .addComponent(channel6_Read))
                    .addComponent(lbl_canalDeLeitura))
                .addGap(8, 67, Short.MAX_VALUE))
        );
        panel_canalDeLeituraLayout.setVerticalGroup(
            panel_canalDeLeituraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_canalDeLeituraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_canalDeLeitura)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_canalDeLeituraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(channel1_Read)
                    .addComponent(channel5_Read))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_canalDeLeituraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(channel2_Read)
                    .addComponent(channel6_Read))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_canalDeLeituraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(channel3_Read)
                    .addComponent(channel7_Read))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_canalDeLeituraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(channel4_Read)
                    .addComponent(channel8_Read))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        panel_Escrita.setBackground(new java.awt.Color(204, 255, 204));
        panel_Escrita.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        lbl_canalDeEscrita.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_canalDeEscrita.setText("Canal de Escrita");

        channel1_Write.setBackground(new java.awt.Color(204, 255, 204));
        channel1_Write.setText("0");
        channel1_Write.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel1_WriteActionPerformed(evt);
            }
        });

        channel2_Write.setBackground(new java.awt.Color(204, 255, 204));
        channel2_Write.setText("1");
        channel2_Write.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel2_WriteActionPerformed(evt);
            }
        });

        channel3_Write.setBackground(new java.awt.Color(204, 255, 204));
        channel3_Write.setText("2");
        channel3_Write.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel3_WriteActionPerformed(evt);
            }
        });

        channel4_Write.setBackground(new java.awt.Color(204, 255, 204));
        channel4_Write.setText("3");
        channel4_Write.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel4_WriteActionPerformed(evt);
            }
        });

        channel5_Write.setBackground(new java.awt.Color(204, 255, 204));
        channel5_Write.setText("4");
        channel5_Write.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel5_WriteActionPerformed(evt);
            }
        });

        channel6_Write.setBackground(new java.awt.Color(204, 255, 204));
        channel6_Write.setText("5");
        channel6_Write.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel6_WriteActionPerformed(evt);
            }
        });

        channel7_Write.setBackground(new java.awt.Color(204, 255, 204));
        channel7_Write.setText("6");
        channel7_Write.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel7_WriteActionPerformed(evt);
            }
        });

        channel8_Write.setBackground(new java.awt.Color(204, 255, 204));
        channel8_Write.setText("7");
        channel8_Write.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channel8_WriteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_EscritaLayout = new javax.swing.GroupLayout(panel_Escrita);
        panel_Escrita.setLayout(panel_EscritaLayout);
        panel_EscritaLayout.setHorizontalGroup(
            panel_EscritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_EscritaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_EscritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_EscritaLayout.createSequentialGroup()
                        .addComponent(channel4_Write)
                        .addGap(18, 18, 18)
                        .addComponent(channel8_Write))
                    .addGroup(panel_EscritaLayout.createSequentialGroup()
                        .addComponent(channel3_Write)
                        .addGap(18, 18, 18)
                        .addComponent(channel7_Write))
                    .addGroup(panel_EscritaLayout.createSequentialGroup()
                        .addComponent(channel1_Write)
                        .addGap(18, 18, 18)
                        .addComponent(channel5_Write))
                    .addGroup(panel_EscritaLayout.createSequentialGroup()
                        .addComponent(channel2_Write)
                        .addGap(18, 18, 18)
                        .addComponent(channel6_Write))
                    .addComponent(lbl_canalDeEscrita))
                .addGap(8, 71, Short.MAX_VALUE))
        );
        panel_EscritaLayout.setVerticalGroup(
            panel_EscritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_EscritaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_canalDeEscrita)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_EscritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(channel1_Write)
                    .addComponent(channel5_Write))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_EscritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(channel2_Write)
                    .addComponent(channel6_Write))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_EscritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(channel3_Write)
                    .addComponent(channel7_Write))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_EscritaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(channel4_Write)
                    .addComponent(channel8_Write))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        pnl_malha.setBackground(new java.awt.Color(204, 255, 204));
        pnl_malha.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        lbl_Malha.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_Malha.setText("Malha");

        Radio_OpenSytem.setBackground(new java.awt.Color(204, 255, 204));
        Radio_OpenSytem.setText("Aberta");
        Radio_OpenSytem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Radio_OpenSytemActionPerformed(evt);
            }
        });

        Radio_ClosedSystem.setBackground(new java.awt.Color(204, 255, 204));
        Radio_ClosedSystem.setText("Fechada");
        Radio_ClosedSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Radio_ClosedSystemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_malhaLayout = new javax.swing.GroupLayout(pnl_malha);
        pnl_malha.setLayout(pnl_malhaLayout);
        pnl_malhaLayout.setHorizontalGroup(
            pnl_malhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_malhaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_malhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Radio_OpenSytem)
                    .addComponent(Radio_ClosedSystem)
                    .addComponent(lbl_Malha))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_malhaLayout.setVerticalGroup(
            pnl_malhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_malhaLayout.createSequentialGroup()
                .addComponent(lbl_Malha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Radio_OpenSytem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Radio_ClosedSystem))
        );

        pnl_tanqueDeControle.setBackground(new java.awt.Color(204, 255, 204));
        pnl_tanqueDeControle.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        lbl_Malha1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_Malha1.setText("Controle");

        Radio_selectedTank1.setBackground(new java.awt.Color(204, 255, 204));
        Radio_selectedTank1.setText("Tanque1");
        Radio_selectedTank1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Radio_selectedTank1ActionPerformed(evt);
            }
        });

        Radio_selectedTank2.setBackground(new java.awt.Color(204, 255, 204));
        Radio_selectedTank2.setText("Tanque2");
        Radio_selectedTank2.setEnabled(false);
        Radio_selectedTank2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Radio_selectedTank2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_tanqueDeControleLayout = new javax.swing.GroupLayout(pnl_tanqueDeControle);
        pnl_tanqueDeControle.setLayout(pnl_tanqueDeControleLayout);
        pnl_tanqueDeControleLayout.setHorizontalGroup(
            pnl_tanqueDeControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_tanqueDeControleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_tanqueDeControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Radio_selectedTank1)
                    .addComponent(Radio_selectedTank2)
                    .addComponent(lbl_Malha1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_tanqueDeControleLayout.setVerticalGroup(
            pnl_tanqueDeControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_tanqueDeControleLayout.createSequentialGroup()
                .addComponent(lbl_Malha1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Radio_selectedTank1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Radio_selectedTank2))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(panel_canalDeLeitura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panel_Escrita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(pnl_malha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnl_tanqueDeControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_Escrita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel_canalDeLeitura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl_malha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnl_tanqueDeControle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88))
        );

        jTabbedPane7.addTab("Canais e sistema", jPanel14);

        jPanel15.setBackground(new java.awt.Color(204, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbl_configuracoes_ip.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_configuracoes_ip.setText("IP");

        lbl_configuracoes_porta.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_configuracoes_porta.setText("Porta");

        lbl_configuracoes_conexao.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        lbl_configuracoes_conexao.setText("Sistema Desconectado");

        btn_refresh.setIcon(new javax.swing.ImageIcon("C:\\Users\\deangel\\Documents\\NetBeansProjects\\TankSimulator\\Imports\\img\\conectedIcon1.png")); // NOI18N
        btn_refresh.setToolTipText("Conectar a um servidor");
        btn_refresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_refreshMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_refreshMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_refreshMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lbl_configuracoes_porta)
                                .addComponent(lbl_configuracoes_ip))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtF_ip, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtF_porta, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(lbl_configuracoes_conexao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(298, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_configuracoes_ip)
                    .addComponent(txtF_ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_configuracoes_porta)
                    .addComponent(txtF_porta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lbl_configuracoes_conexao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(131, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab("Conexão", jPanel15);

        jPanel16.setBackground(new java.awt.Color(204, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel2.setBackground(new java.awt.Color(204, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Malha Fechada", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 10))); // NOI18N

        lbl_tipoDeControle.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_tipoDeControle.setText("Controle");

        cmb_tipoDeControle.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "P", "PI", "PD", "PID", "PI-D" }));
        cmb_tipoDeControle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_tipoDeControleItemStateChanged(evt);
            }
        });

        lbl_setPoint.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_setPoint.setText("setPoint");

        lbl_kpMestre.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_kpMestre.setText("Kp");

        txtF_kpMestre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtF_kpMestreActionPerformed(evt);
            }
        });

        lbl_kiMestre.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_kiMestre.setText("Ki");

        txtF_kiMestre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtF_kiMestreActionPerformed(evt);
            }
        });

        lbl_kdMestre.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_kdMestre.setText("Kd");

        txtF_kdMestre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtF_kdMestreActionPerformed(evt);
            }
        });

        txtF_setPoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtF_setPointActionPerformed(evt);
            }
        });

        lbl_mestre.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_mestre.setText("Mestre");

        lbl_kpEscravo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_kpEscravo.setText("Kp");

        txtF_kpEscravo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtF_kpEscravoActionPerformed(evt);
            }
        });

        lbl_kiEscravo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_kiEscravo.setText("Ki");

        txtF_kiEscravo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtF_kiEscravoActionPerformed(evt);
            }
        });

        lbl_kdEscravo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_kdEscravo.setText("Kd");

        txtF_kdEscravo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtF_kdEscravoActionPerformed(evt);
            }
        });

        lbl_escravo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_escravo.setText("Escravo");

        lbl_tipoDeControEscravo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_tipoDeControEscravo.setText("Controle");

        cmb_tipoDeControleEscravo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "P", "PI", "PD", "PID", "PI-D" }));
        cmb_tipoDeControleEscravo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_tipoDeControleEscravoItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lbl_setPoint)
                                .addGap(8, 8, 8)
                                .addComponent(txtF_setPoint, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbl_mestre)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_kpMestre)
                                        .addComponent(lbl_kiMestre))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(txtF_kiMestre))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(txtF_kpMestre))))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(lbl_kdMestre)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtF_kdMestre, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lbl_escravo)
                                .addGap(0, 40, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_kiEscravo)
                                    .addComponent(lbl_kpEscravo)
                                    .addComponent(lbl_kdEscravo))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtF_kpEscravo, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                    .addComponent(txtF_kiEscravo)
                                    .addComponent(txtF_kdEscravo))
                                .addContainerGap())))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lbl_tipoDeControle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmb_tipoDeControle, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lbl_tipoDeControEscravo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmb_tipoDeControleEscravo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tipoDeControle)
                    .addComponent(cmb_tipoDeControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tipoDeControEscravo)
                    .addComponent(cmb_tipoDeControleEscravo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtF_setPoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_setPoint))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_mestre)
                    .addComponent(lbl_escravo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_kpMestre)
                    .addComponent(txtF_kpMestre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_kpEscravo)
                    .addComponent(txtF_kpEscravo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_kiMestre)
                    .addComponent(txtF_kiMestre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_kiEscravo)
                    .addComponent(txtF_kiEscravo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_kdMestre)
                    .addComponent(txtF_kdMestre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_kdEscravo)
                    .addComponent(txtF_kdEscravo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnl_entradaMalhaAberta.setBackground(new java.awt.Color(204, 255, 255));
        pnl_entradaMalhaAberta.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Malha Aberta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N

        lbl_sinal.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_sinal.setText("Sinal");

        cmbx_tipoDeSinal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Degrau", "Senóide", "Quadrada", "Dente de Serra", "Aleatório" }));
        cmbx_tipoDeSinal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbx_tipoDeSinalItemStateChanged(evt);
            }
        });

        lbl_amplitude.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_amplitude.setText("Amplitude");

        txtF_amplitude.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtF_amplitudeKeyReleased(evt);
            }
        });

        lbl_offset.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_offset.setText("Offset");

        txtF_offset.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtF_offsetKeyReleased(evt);
            }
        });

        lbl_periodo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbl_periodo.setText("Período");

        txtF_Periodo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtF_PeriodoKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Malha aberta");

        javax.swing.GroupLayout pnl_entradaMalhaAbertaLayout = new javax.swing.GroupLayout(pnl_entradaMalhaAberta);
        pnl_entradaMalhaAberta.setLayout(pnl_entradaMalhaAbertaLayout);
        pnl_entradaMalhaAbertaLayout.setHorizontalGroup(
            pnl_entradaMalhaAbertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_entradaMalhaAbertaLayout.createSequentialGroup()
                .addGroup(pnl_entradaMalhaAbertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_entradaMalhaAbertaLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_entradaMalhaAbertaLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(pnl_entradaMalhaAbertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_entradaMalhaAbertaLayout.createSequentialGroup()
                                .addComponent(lbl_sinal, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbx_tipoDeSinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_entradaMalhaAbertaLayout.createSequentialGroup()
                                .addComponent(lbl_amplitude, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtF_amplitude, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_entradaMalhaAbertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(pnl_entradaMalhaAbertaLayout.createSequentialGroup()
                                    .addComponent(lbl_periodo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtF_Periodo))
                                .addGroup(pnl_entradaMalhaAbertaLayout.createSequentialGroup()
                                    .addComponent(lbl_offset, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(26, 26, 26)
                                    .addComponent(txtF_offset, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        pnl_entradaMalhaAbertaLayout.setVerticalGroup(
            pnl_entradaMalhaAbertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_entradaMalhaAbertaLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(3, 3, 3)
                .addGroup(pnl_entradaMalhaAbertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_sinal)
                    .addComponent(cmbx_tipoDeSinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_entradaMalhaAbertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_amplitude)
                    .addComponent(txtF_amplitude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_entradaMalhaAbertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_offset)
                    .addComponent(txtF_offset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_entradaMalhaAbertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_periodo)
                    .addComponent(txtF_Periodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(204, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Opções de Controle", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 12))); // NOI18N

        cmb_serieOuCascata.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        cmb_serieOuCascata.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Controle Serie tanque 1", "Controle serie tanque 2", "Controle Cascata" }));
        cmb_serieOuCascata.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_serieOuCascataItemStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel1.setText("opções");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmb_serieOuCascata, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmb_serieOuCascata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(pnl_entradaMalhaAberta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(pnl_entradaMalhaAberta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab("Entradas", jPanel16);

        jPanel17.setBackground(new java.awt.Color(204, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbl_fatorDeAceleracao.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_fatorDeAceleracao.setText("Fator de Delay");

        lbl_grafico.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lbl_grafico.setText("Limpar Gráfico");

        jButton2.setText("Ok");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        chbx_constantesDeTempo.setBackground(new java.awt.Color(204, 255, 255));
        chbx_constantesDeTempo.setText("Utilizar constantes de tempo");
        chbx_constantesDeTempo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbx_constantesDeTempoActionPerformed(evt);
            }
        });

        jCheckBox1.setBackground(new java.awt.Color(204, 255, 255));
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Integrar utilizando o método do trapézio");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(chbx_constantesDeTempo)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(lbl_fatorDeAceleracao, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtF_fatorAceleracao, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(lbl_grafico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)))
                .addContainerGap(243, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_fatorDeAceleracao)
                    .addComponent(txtF_fatorAceleracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_grafico)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(chbx_constantesDeTempo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addContainerGap(133, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab("Ferramentas e opções", jPanel17);

        jPanel10.setBackground(new java.awt.Color(204, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        chebx_exibirNivelDoTanque1.setBackground(new java.awt.Color(204, 255, 255));
        chebx_exibirNivelDoTanque1.setSelected(true);
        chebx_exibirNivelDoTanque1.setText("Exibir nível do tanque 1");
        chebx_exibirNivelDoTanque1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_exibirNivelDoTanque1ActionPerformed(evt);
            }
        });

        chebx_exibirNivelDoTanque2.setBackground(new java.awt.Color(204, 255, 255));
        chebx_exibirNivelDoTanque2.setSelected(true);
        chebx_exibirNivelDoTanque2.setText("Exibir nível do tanque 2");
        chebx_exibirNivelDoTanque2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_exibirNivelDoTanque2ActionPerformed(evt);
            }
        });

        chebx_exibirSetpoint.setBackground(new java.awt.Color(204, 255, 255));
        chebx_exibirSetpoint.setSelected(true);
        chebx_exibirSetpoint.setText("Exibir setpoint");
        chebx_exibirSetpoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_exibirSetpointActionPerformed(evt);
            }
        });

        chebx_Erro.setBackground(new java.awt.Color(204, 255, 255));
        chebx_Erro.setText("Exibir erro (cm)");
        chebx_Erro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_ErroActionPerformed(evt);
            }
        });

        chebx_graficoSinal.setBackground(new java.awt.Color(204, 255, 255));
        chebx_graficoSinal.setSelected(true);
        chebx_graficoSinal.setText("Exibir Grafico da tensão na bomba");
        chebx_graficoSinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_graficoSinalActionPerformed(evt);
            }
        });

        chebx_graficoReferenciaEscravo.setBackground(new java.awt.Color(204, 255, 255));
        chebx_graficoReferenciaEscravo.setText("Referência do Escravo (controle cascata)");
        chebx_graficoReferenciaEscravo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_graficoReferenciaEscravoActionPerformed(evt);
            }
        });

        chebx_tensaoAplicadaSemSaturacao.setBackground(new java.awt.Color(204, 255, 255));
        chebx_tensaoAplicadaSemSaturacao.setText("Tensão aplicada sem saturação");
        chebx_tensaoAplicadaSemSaturacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_tensaoAplicadaSemSaturacaoActionPerformed(evt);
            }
        });

        chebx_acaoProporcional.setBackground(new java.awt.Color(204, 255, 255));
        chebx_acaoProporcional.setText("Ação porporcional");
        chebx_acaoProporcional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_acaoProporcionalActionPerformed(evt);
            }
        });

        chebx_acaoIntagral.setBackground(new java.awt.Color(204, 255, 255));
        chebx_acaoIntagral.setText("Ação integrativa");
        chebx_acaoIntagral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_acaoIntagralActionPerformed(evt);
            }
        });

        chebx_acaoDerivatica.setBackground(new java.awt.Color(204, 255, 255));
        chebx_acaoDerivatica.setText("Ação derivativa");
        chebx_acaoDerivatica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_acaoDerivaticaActionPerformed(evt);
            }
        });

        chebx_Tensao.setBackground(new java.awt.Color(204, 255, 255));
        chebx_Tensao.setSelected(true);
        chebx_Tensao.setText("Tensão");
        chebx_Tensao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chebx_TensaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chebx_graficoSinal)
                            .addComponent(chebx_Erro)
                            .addComponent(chebx_exibirSetpoint)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chebx_tensaoAplicadaSemSaturacao)
                                    .addComponent(chebx_acaoIntagral)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(chebx_acaoDerivatica)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chebx_Tensao))
                                    .addComponent(chebx_acaoProporcional)))
                            .addComponent(chebx_exibirNivelDoTanque2))
                        .addContainerGap())
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(chebx_exibirNivelDoTanque1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                        .addComponent(chebx_graficoReferenciaEscravo)
                        .addGap(17, 17, 17))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chebx_exibirNivelDoTanque1)
                    .addComponent(chebx_graficoReferenciaEscravo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chebx_exibirNivelDoTanque2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chebx_exibirSetpoint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chebx_Erro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chebx_graficoSinal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chebx_tensaoAplicadaSemSaturacao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chebx_acaoProporcional, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chebx_acaoIntagral, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chebx_acaoDerivatica)
                    .addComponent(chebx_Tensao))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab("Gráficos", jPanel10);

        btn_swithOnOff.setBackground(new java.awt.Color(204, 255, 255));
        btn_swithOnOff.setIcon(new javax.swing.ImageIcon("C:\\Users\\deangel\\Documents\\NetBeansProjects\\TankSimulator\\Imports\\img\\teste2.png")); // NOI18N
        btn_swithOnOff.setToolTipText("Ligar sistema");
        btn_swithOnOff.setBorder(null);
        btn_swithOnOff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_swithOnOffMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_swithOnOffMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_swithOnOffMouseExited(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(204, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setIcon(new javax.swing.ImageIcon("C:\\Users\\deangel\\Documents\\NetBeansProjects\\TankSimulator\\Imports\\img\\discon.png")); // NOI18N
        jLabel9.setToolTipText("Sistema disconectado");

        jLabel11.setIcon(new javax.swing.ImageIcon("C:\\Users\\deangel\\Documents\\NetBeansProjects\\TankSimulator\\Imports\\img\\off1.png")); // NOI18N
        jLabel11.setToolTipText("Sistema desligado");

        lbl_advertencia.setIcon(new javax.swing.ImageIcon("C:\\Users\\deangel\\Desktop\\2\\TankSimulator\\Imports\\img\\adv2.png")); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_advertencia)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9)
            .addComponent(jLabel11)
            .addComponent(lbl_advertencia)
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(btn_swithOnOff)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_swithOnOff)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(132, 132, 132))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Sistema", jPanel1);

        jPanel6.setBackground(new java.awt.Color(204, 255, 255));

        jPanel7.setBackground(new java.awt.Color(204, 255, 204));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Status da Conexão", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Leitura", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        lbl_conexao_leitura0.setText("Canal 0: Desabilitado");

        lbl_conexao_leitura1.setText("Canal 1: Desabilitado");

        lbl_conexao_leitura2.setText("Canal 2: Desabilitado");

        lbl_conexao_leitura3.setText("Canal 3: Desabilitado");

        lbl_conexao_leitura4.setText("Canal 4: Desabilitado");

        lbl_conexao_leitura5.setText("Canal 5: Desabilitado");

        lbl_conexao_leitura6.setText("Canal 6: Desabilitado");

        lbl_conexao_leitura7.setText("Canal 7: Desabilitado");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_conexao_leitura0)
                    .addComponent(lbl_conexao_leitura1)
                    .addComponent(lbl_conexao_leitura2)
                    .addComponent(lbl_conexao_leitura3)
                    .addComponent(lbl_conexao_leitura4)
                    .addComponent(lbl_conexao_leitura5)
                    .addComponent(lbl_conexao_leitura6)
                    .addComponent(lbl_conexao_leitura7))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_conexao_leitura0)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_leitura1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_leitura2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_leitura3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_leitura4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_leitura5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_leitura6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_leitura7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(204, 255, 204));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Escrita", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        lbl_conexao_Escrita0.setText("Canal 0: Desabilitado");

        lbl_conexao_Escrita1.setText("Canal 1: Desabilitado");

        lbl_conexao_Escrita2.setText("Canal 2: Desabilitado");

        lbl_conexao_Escrita3.setText("Canal 3: Desabilitado");

        lbl_conexao_Escrita4.setText("Canal 4: Desabilitado");

        lbl_conexao_Escrita5.setText("Canal 5: Desabilitado");

        lbl_conexao_Escrita6.setText("Canal 6: Desabilitado");

        lbl_conexao_Escrita7.setText("Canal 7: Desabilitado");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_conexao_Escrita0)
                    .addComponent(lbl_conexao_Escrita1)
                    .addComponent(lbl_conexao_Escrita2)
                    .addComponent(lbl_conexao_Escrita3)
                    .addComponent(lbl_conexao_Escrita4)
                    .addComponent(lbl_conexao_Escrita5)
                    .addComponent(lbl_conexao_Escrita6)
                    .addComponent(lbl_conexao_Escrita7))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_conexao_Escrita0)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_Escrita1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_Escrita2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_Escrita3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_Escrita4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_Escrita5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_Escrita6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_conexao_Escrita7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(692, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(394, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Conexão", jPanel6);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 723, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Radio_ClosedSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Radio_ClosedSystemActionPerformed
        this.formManager.validateTypeSystem(Radio_OpenSytem, Radio_ClosedSystem, Radio_ClosedSystem);

        this.simulatorConection.setMalhaAberta(false);


        this.txtF_amplitude.setEnabled(false);
        this.txtF_Periodo.setEnabled(false);
        this.txtF_offset.setEnabled(false);

        this.cmbx_tipoDeSinal.setEnabled(false);


        this.txtF_setPoint.setEnabled(true);
        this.txtF_kpMestre.setEnabled(true);
        this.txtF_kiMestre.setEnabled(true);
        this.txtF_kdMestre.setEnabled(true);
        this.cmb_tipoDeControle.setEnabled(true);


        this.txtF_kpEscravo.setEnabled(true);
        this.txtF_kiEscravo.setEnabled(true);
        this.txtF_kdEscravo.setEnabled(true);
        this.cmb_tipoDeControleEscravo.setEnabled(true);


        //this.pnl_tanqueDeControle.setVisible(true);

        this.transit[15] = 2;
        this.chebx_Erro.setEnabled(true);
        //this.grafoManager._panelGraficoSinal.setSize(320, 190);
        //this.grafoManager.chartPanelSinal.setPreferredSize(new java.awt.Dimension(320, 190));
        //this.grafoManager._panelGraficoErroAltura.setVisible(true);

        this.chbx_constantesDeTempo.setEnabled(true);
        this.chebx_Erro.setEnabled(true);
        this.chebx_exibirSetpoint.setEnabled(true);
        this.chebx_exibirSetpoint.setSelected(true);

    }//GEN-LAST:event_Radio_ClosedSystemActionPerformed

    private void channel1_WriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel1_WriteActionPerformed
        this.formManager.validateWriteChannel(channel1_Write, channel2_Write,
                channel3_Write, channel4_Write, channel5_Write, channel6_Write,
                channel7_Write, channel8_Write, channel1_Write);
    }//GEN-LAST:event_channel1_WriteActionPerformed

    private void channel2_WriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel2_WriteActionPerformed
        this.formManager.validateWriteChannel(channel1_Write, channel2_Write,
                channel3_Write, channel4_Write, channel5_Write, channel6_Write,
                channel7_Write, channel8_Write, channel2_Write);
    }//GEN-LAST:event_channel2_WriteActionPerformed

    private void channel3_WriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel3_WriteActionPerformed
        this.formManager.validateWriteChannel(channel1_Write, channel2_Write,
                channel3_Write, channel4_Write, channel5_Write, channel6_Write,
                channel7_Write, channel8_Write, channel3_Write);

    }//GEN-LAST:event_channel3_WriteActionPerformed

    private void channel4_WriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel4_WriteActionPerformed
        this.formManager.validateWriteChannel(channel1_Write, channel2_Write,
                channel3_Write, channel4_Write, channel5_Write, channel6_Write,
                channel7_Write, channel8_Write, channel4_Write);

    }//GEN-LAST:event_channel4_WriteActionPerformed

    private void channel5_WriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel5_WriteActionPerformed
        this.formManager.validateWriteChannel(channel1_Write, channel2_Write,
                channel3_Write, channel4_Write, channel5_Write, channel6_Write,
                channel7_Write, channel8_Write, channel5_Write);
    }//GEN-LAST:event_channel5_WriteActionPerformed

    private void channel6_WriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel6_WriteActionPerformed
        this.formManager.validateWriteChannel(channel1_Write, channel2_Write,
                channel3_Write, channel4_Write, channel5_Write, channel6_Write,
                channel7_Write, channel8_Write, channel6_Write);

    }//GEN-LAST:event_channel6_WriteActionPerformed

    private void channel7_WriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel7_WriteActionPerformed
        this.formManager.validateWriteChannel(channel1_Write, channel2_Write,
                channel3_Write, channel4_Write, channel5_Write, channel6_Write,
                channel7_Write, channel8_Write, channel7_Write);

    }//GEN-LAST:event_channel7_WriteActionPerformed

    private void channel8_WriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel8_WriteActionPerformed
        this.formManager.validateWriteChannel(channel1_Write, channel2_Write,
                channel3_Write, channel4_Write, channel5_Write, channel6_Write,
                channel7_Write, channel8_Write, channel8_Write);

    }//GEN-LAST:event_channel8_WriteActionPerformed

    private void Radio_OpenSytemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Radio_OpenSytemActionPerformed
        this.formManager.validateTypeSystem(Radio_OpenSytem, Radio_ClosedSystem, Radio_OpenSytem);

        this.simulatorConection.setMalhaAberta(true);
        this.transit[15] = 0;

        this.txtF_setPoint.setEnabled(false);
        this.txtF_kpMestre.setEnabled(false);
        this.txtF_kiMestre.setEnabled(false);
        this.txtF_kdMestre.setEnabled(false);
        this.cmb_tipoDeControle.setEnabled(false);
        this.cmb_tipoDeControleEscravo.setEnabled(false);
        this.chbx_constantesDeTempo.setEnabled(false);
        this.pnl_tanqueDeControle.setVisible(false);

        this.txtF_kpEscravo.setEnabled(false);
        this.txtF_kiEscravo.setEnabled(false);
        this.txtF_kdEscravo.setEnabled(false);

        this.cmbx_tipoDeSinal.setEnabled(true);
        this.txtF_amplitude.setEnabled(true);

        this.txtF_Periodo.setEnabled(true);
        this.txtF_offset.setEnabled(true);

        this.chebx_Erro.setEnabled(false);
        //this.grafoManager._panelGraficoSinal.setSize(650, 190);
        //this.grafoManager.chartPanelSinal.setPreferredSize(new java.awt.Dimension(650, 190));
        //this.grafoManager._panelGraficoErroAltura.setVisible(false);

        this.chebx_exibirSetpoint.setSelected(false);
        this.chebx_exibirSetpoint.setEnabled(false);
        this.chebx_Erro.setEnabled(false);

    }//GEN-LAST:event_Radio_OpenSytemActionPerformed

    private void cmbx_tipoDeSinalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbx_tipoDeSinalItemStateChanged

        if (this.cmbx_tipoDeSinal.getSelectedIndex() == 0) {
            //this.lbl_amplitude.setVisible(false);
            this.lbl_offset.setVisible(false);
            this.lbl_periodo.setVisible(false);
            //this.txtF_amplitude.setVisible(false);
            this.txtF_offset.setVisible(false);
            this.txtF_Periodo.setVisible(false);
            this.txtF_Periodo.setText("20");

            this.simulatorConection.setSignalType(0);

        }

        if (this.cmbx_tipoDeSinal.getSelectedIndex() == 1) {
            //this.lbl_amplitude.setVisible(true);
            this.lbl_offset.setVisible(true);
            this.lbl_periodo.setVisible(true);
            //this.txtF_amplitude.setVisible(true);
            this.txtF_offset.setVisible(true);
            this.txtF_Periodo.setVisible(true);

            this.simulatorConection.setSignalType(1);

        }

        if (this.cmbx_tipoDeSinal.getSelectedIndex() == 2) {
            //this.lbl_amplitude.setVisible(true);
            this.lbl_offset.setVisible(true);
            this.lbl_periodo.setVisible(true);
            //this.txtF_amplitude.setVisible(true);
            this.txtF_offset.setVisible(true);
            this.txtF_Periodo.setVisible(true);

            this.simulatorConection.setSignalType(2);

        }

        if (this.cmbx_tipoDeSinal.getSelectedIndex() == 3) {
            //this.lbl_amplitude.setVisible(true);
            this.lbl_offset.setVisible(true);
            this.lbl_periodo.setVisible(true);
            //this.txtF_amplitude.setVisible(true);
            this.txtF_offset.setVisible(true);
            this.txtF_Periodo.setVisible(true);

            this.simulatorConection.setSignalType(3);

        }

        if (this.cmbx_tipoDeSinal.getSelectedIndex() == 4) {
            //this.lbl_amplitude.setVisible(true);
            this.lbl_offset.setVisible(true);
            this.lbl_periodo.setVisible(true);
            //this.txtF_amplitude.setVisible(true);
            this.txtF_offset.setVisible(true);
            this.txtF_Periodo.setVisible(true);

            this.simulatorConection.setSignalType(4);

        }


    }//GEN-LAST:event_cmbx_tipoDeSinalItemStateChanged

    private void btn_refreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_refreshMouseClicked

        if (!this.simulatorConection.isConnectionStatus()) {

            String ip;
            int porta, canalW, canalR;

            ip = this.txtF_ip.getText();
            porta = Integer.parseInt(this.txtF_porta.getText());
            canalW = this.formManager.whichChannelIsUsedtoWrite(channel1_Write, channel2_Write, channel3_Write, channel4_Write, channel5_Write, channel6_Write, channel7_Write, channel8_Write);
            String status;
            boolean sucess = false;
            boolean canais[] = new boolean[8];

            canais[0] = this.channel1_Read.isSelected();
            canais[1] = this.channel2_Read.isSelected();
            canais[2] = this.channel3_Read.isSelected();
            canais[3] = this.channel4_Read.isSelected();
            canais[4] = this.channel5_Read.isSelected();
            canais[5] = this.channel6_Read.isSelected();
            canais[6] = this.channel7_Read.isSelected();
            canais[7] = this.channel8_Read.isSelected();
            //canalR = this.formManager.whichChannelIsUsedtoRead(channel1_Read, channel2_Read, channel3_Read, channel4_Read, channel5_Read, channel6_Read, channel7_Read, channel8_Read);

            this.simulatorConection.refreshConnection(ip, porta, canalW, 0, this.txtF_amplitude,
                    this.txtF_offset, this.txtF_Periodo, 0, transit, canais);


            if (this.simulatorConection.isConnectionStatus()) {
                this.lbl_configuracoes_conexao.setText("Sistema Conectado");
                this.jLabel9.setIcon(new ImageIcon(".\\Imports\\img\\conec.png"));
                this.jLabel9.setToolTipText("Sistema conectado");
                this.txtF_ip.setEnabled(false);
                this.txtF_porta.setEnabled(false);

                this.channel1_Read.setEnabled(false);
                this.channel2_Read.setEnabled(false);
                this.channel3_Read.setEnabled(false);
                this.channel4_Read.setEnabled(false);
                this.channel5_Read.setEnabled(false);
                this.channel6_Read.setEnabled(false);
                this.channel7_Read.setEnabled(false);
                this.channel8_Read.setEnabled(false);

                this.channel1_Write.setEnabled(false);
                this.channel2_Write.setEnabled(false);
                this.channel3_Write.setEnabled(false);
                this.channel4_Write.setEnabled(false);
                this.channel5_Write.setEnabled(false);
                this.channel6_Write.setEnabled(false);
                this.channel7_Write.setEnabled(false);
                this.channel8_Write.setEnabled(false);

                if (canalW == 0) {
                    this.lbl_conexao_Escrita0.setText("Canal 0: Habilitado");
                }

                if (canalW == 1) {
                    this.lbl_conexao_Escrita1.setText("Canal 1: Habilitado");

                }

                if (canalW == 2) {
                    this.lbl_conexao_Escrita2.setText("Canal 2: Habilitado");
                }

                if (canalW == 3) {
                    this.lbl_conexao_Escrita3.setText("Canal 3: Habilitado");
                }

                if (canalW == 4) {
                    this.lbl_conexao_Escrita4.setText("Canal 4: Habilitado");
                }

                if (canalW == 5) {
                    this.lbl_conexao_Escrita5.setText("Canal 5: Habilitado");
                }

                if (canalW == 6) {
                    this.lbl_conexao_Escrita6.setText("Canal 6: Habilitado");
                }

                if (canalW == 7) {
                    this.lbl_conexao_Escrita7.setText("Canal 7: Habilitado");
                }

                if (canais[0]) {
                    this.lbl_conexao_leitura0.setText("Canal 0: Habilitado");
                }

                if (canais[1]) {
                    this.transit[16] = 2;
                    this.lbl_conexao_leitura1.setText("Canal 1: Habilitado");
                    //this.grafoManager.graficoDoNivelDosDoisTanques(true);


                }

                if (canais[2]) {

                    this.lbl_conexao_leitura2.setText("Canal 2: Habilitado");
                }

                if (canais[3]) {
                    this.lbl_conexao_leitura3.setText("Canal 3: Habilitado");
                }

                if (canais[4]) {
                    this.lbl_conexao_leitura4.setText("Canal 4: Habilitado");
                }

                if (canais[5]) {
                    this.lbl_conexao_leitura5.setText("Canal 5: Habilitado");
                }

                if (canais[6]) {
                    this.lbl_conexao_leitura6.setText("Canal 6: Habilitado");
                }

                if (canais[7]) {
                    this.lbl_conexao_leitura7.setText("Canal 7: Habilitado");
                }

            }


        }





    }//GEN-LAST:event_btn_refreshMouseClicked

    private void txtF_amplitudeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtF_amplitudeKeyReleased

        this.simulatorConection.conversionAmplitude();
    }//GEN-LAST:event_txtF_amplitudeKeyReleased

    private void txtF_offsetKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtF_offsetKeyReleased
        this.simulatorConection.conversionOffset();
    }//GEN-LAST:event_txtF_offsetKeyReleased

    private void txtF_PeriodoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtF_PeriodoKeyReleased
        this.simulatorConection.conversionPeriodo();
    }//GEN-LAST:event_txtF_PeriodoKeyReleased

    private void btn_swithOnOffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_swithOnOffMouseClicked
        if (this.simulatorConection.isConnectionStatus()) {
            if (aux) {
                this.simulatorConection.turnOffSystem();
                //this.simulatorConection.getTimeMensureMaster().reset();
                //this.simulatorConection.getTimeMensureMaster().setSetpointAntigo(0);
                aux = false;
                //this.grafoManager.myClear();
                this.btn_swithOnOff.setIcon(new ImageIcon(".\\Imports\\img\\teste2.png"));
                this.btn_swithOnOff.setToolTipText("Ligar sistema");
                this.jLabel11.setIcon(new ImageIcon(".\\Imports\\img\\off1.png"));
                this.jLabel11.setToolTipText("Sistema desligado");
            } else {
                this.grafoManager.myClear();
                this.simulatorConection.conversionAmplitude();
                this.simulatorConection.conversionOffset();
                this.simulatorConection.conversionPeriodo();
                this.simulatorConection.turnOnSystem();
                this.simulatorConection.conversionSetpoint(this.txtF_setPoint);

                this.simulatorConection.conversionKp(txtF_kpMestre);
                this.simulatorConection.conversionKi(txtF_kiMestre);
                this.simulatorConection.conversionKd(txtF_kdMestre);

                this.simulatorConection.conversionKpEscravo(txtF_kpEscravo);
                this.simulatorConection.conversionKiEscravo(txtF_kiEscravo);
                this.simulatorConection.conversionKdEscravo(txtF_kdEscravo);


                this.simulatorConection.getTimeMensureMaster().reset();
                this.simulatorConection.getTimeMensureMaster().setTempoDePico(0);
                this.simulatorConection.getTimeMensureMaster().setValorDePico(0);
                this.simulatorConection.getTimeMensureMaster().setUndershootper(0);

                this.simulatorConection.getTimeMensureMaster2().reset();
                this.simulatorConection.getTimeMensureMaster2().setTempoDePico(0);
                this.simulatorConection.getTimeMensureMaster2().setValorDePico(0);
                this.simulatorConection.getTimeMensureMaster2().setUndershootper(0);

                aux = true;

                this.btn_swithOnOff.setIcon(new ImageIcon(".\\Imports\\img\\stopp2.png"));
                //this.simulatorConection.start();
                this.btn_swithOnOff.setToolTipText("Desligar sistema");
                this.jLabel11.setIcon(new ImageIcon(".\\Imports\\img\\on1.png"));
                this.jLabel11.setToolTipText("Sistema ligado");

            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sistema sem conexão", "Tank Simulator", JOptionPane.OK_CANCEL_OPTION);
        }
    }//GEN-LAST:event_btn_swithOnOffMouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        this.grafoManager.myClear2();
    }//GEN-LAST:event_jButton2MouseClicked

    private void channel1_ReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel1_ReadActionPerformed
        this.formManager.validateReadChannel(channel1_Read, channel2_Read, channel3_Read, channel4_Read, channel5_Read, channel6_Read, channel7_Read, channel8_Read, channel1_Read);
        if (channel1_Read.isSelected()) {
            this.chebx_exibirNivelDoTanque1.setSelected(true);
            this.chebx_exibirNivelDoTanque1.setEnabled(true);
            this.transit[23] = 2;
            this.Radio_selectedTank1.setEnabled(true);
        } else {
            this.chebx_exibirNivelDoTanque1.setSelected(false);
            this.chebx_exibirNivelDoTanque1.setEnabled(false);
            this.transit[23] = 0;
            this.Radio_selectedTank1.setSelected(false);
        }
    }//GEN-LAST:event_channel1_ReadActionPerformed

    private void channel2_ReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel2_ReadActionPerformed
        this.formManager.validateReadChannel(channel1_Read, channel2_Read, channel3_Read, channel4_Read, channel5_Read, channel6_Read, channel7_Read, channel8_Read, channel2_Read);

        if (channel2_Read.isSelected()) {
            this.chebx_exibirNivelDoTanque2.setEnabled(true);
            this.chebx_exibirNivelDoTanque2.setSelected(true);
            this.transit[16] = 2;
            this.Radio_selectedTank2.setSelected(true);
        } else {
            this.chebx_exibirNivelDoTanque2.setEnabled(false);
            this.chebx_exibirNivelDoTanque2.setSelected(false);
            this.transit[16] = 0;
        }
    }//GEN-LAST:event_channel2_ReadActionPerformed

    private void channel3_ReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel3_ReadActionPerformed
        this.formManager.validateReadChannel(channel1_Read, channel2_Read, channel3_Read, channel4_Read, channel5_Read, channel6_Read, channel7_Read, channel8_Read, channel3_Read);
    }//GEN-LAST:event_channel3_ReadActionPerformed

    private void channel4_ReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel4_ReadActionPerformed
        this.formManager.validateReadChannel(channel1_Read, channel2_Read, channel3_Read, channel4_Read, channel5_Read, channel6_Read, channel7_Read, channel8_Read, channel4_Read);
    }//GEN-LAST:event_channel4_ReadActionPerformed

    private void channel5_ReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel5_ReadActionPerformed
        this.formManager.validateReadChannel(channel1_Read, channel2_Read, channel3_Read, channel4_Read, channel5_Read, channel6_Read, channel7_Read, channel8_Read, channel5_Read);
    }//GEN-LAST:event_channel5_ReadActionPerformed

    private void channel6_ReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel6_ReadActionPerformed
        this.formManager.validateReadChannel(channel1_Read, channel2_Read, channel3_Read, channel4_Read, channel5_Read, channel6_Read, channel7_Read, channel8_Read, channel6_Read);
    }//GEN-LAST:event_channel6_ReadActionPerformed

    private void channel7_ReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel7_ReadActionPerformed
        this.formManager.validateReadChannel(channel1_Read, channel2_Read, channel3_Read, channel4_Read, channel5_Read, channel6_Read, channel7_Read, channel8_Read, channel7_Read);
    }//GEN-LAST:event_channel7_ReadActionPerformed

    private void channel8_ReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channel8_ReadActionPerformed
        this.formManager.validateReadChannel(channel1_Read, channel2_Read, channel3_Read, channel4_Read, channel5_Read, channel6_Read, channel7_Read, channel8_Read, channel8_Read);
    }//GEN-LAST:event_channel8_ReadActionPerformed

    private void cmb_tipoDeControleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_tipoDeControleItemStateChanged
        this.simulatorConection.setTypeControl(this.cmb_tipoDeControle.getSelectedIndex());
    }//GEN-LAST:event_cmb_tipoDeControleItemStateChanged

    private void chbx_constantesDeTempoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbx_constantesDeTempoActionPerformed
        if (this.chbx_constantesDeTempo.isSelected()) {
            this.lbl_kiMestre.setText("Ti");
            this.lbl_kdMestre.setText("Td");
            this.transit[21] = 1;
        } else {
            this.lbl_kiMestre.setText("Ki");
            this.lbl_kdMestre.setText("Kd");
            this.transit[21] = 0;
        }
    }//GEN-LAST:event_chbx_constantesDeTempoActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (this.jCheckBox1.isSelected()) {
            this.transit[22] = 0;
        } else {
            this.transit[22] = 2;
        }

    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void btn_swithOnOffMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_swithOnOffMouseEntered
        this.btn_swithOnOff.setBorder(BorderFactory.createEtchedBorder());
    }//GEN-LAST:event_btn_swithOnOffMouseEntered

    private void btn_swithOnOffMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_swithOnOffMouseExited
        this.btn_swithOnOff.setBorder(null);
    }//GEN-LAST:event_btn_swithOnOffMouseExited

    private void btn_refreshMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_refreshMouseEntered
        this.btn_refresh.setBorder(BorderFactory.createEtchedBorder());
    }//GEN-LAST:event_btn_refreshMouseEntered

    private void btn_refreshMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_refreshMouseExited
        this.btn_refresh.setBorder(null);
    }//GEN-LAST:event_btn_refreshMouseExited

    private void txtF_setPointActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtF_setPointActionPerformed
        this.simulatorConection.conversionSetpoint(this.txtF_setPoint);
    }//GEN-LAST:event_txtF_setPointActionPerformed

    private void Radio_selectedTank1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Radio_selectedTank1ActionPerformed
        if (!this.channel1_Read.isSelected()) {
            this.Radio_selectedTank1.setSelected(false);
        }
    }//GEN-LAST:event_Radio_selectedTank1ActionPerformed

    private void Radio_selectedTank2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Radio_selectedTank2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Radio_selectedTank2ActionPerformed

    private void cmb_tipoDeControleEscravoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_tipoDeControleEscravoItemStateChanged
        this.simulatorConection.setTypeControlEscravo(this.cmb_tipoDeControleEscravo.getSelectedIndex());
    }//GEN-LAST:event_cmb_tipoDeControleEscravoItemStateChanged

    private void cmb_serieOuCascataItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_serieOuCascataItemStateChanged

        this.simulatorConection.setTipoDeControleSerieOuCascata(this.cmb_serieOuCascata.getSelectedIndex());

        if (this.cmb_serieOuCascata.getSelectedIndex() == 2) {
            this.lbl_tipoDeControEscravo.setVisible(true);
            this.cmb_tipoDeControleEscravo.setVisible(true);

            this.lbl_kpEscravo.setVisible(true);
            this.lbl_kiEscravo.setVisible(true);
            this.lbl_kdEscravo.setVisible(true);
            this.txtF_kpEscravo.setVisible(true);
            this.txtF_kiEscravo.setVisible(true);
            this.txtF_kdEscravo.setVisible(true);
            this.transit[25] = 2;

        } else {
            this.lbl_tipoDeControEscravo.setVisible(false);
            this.cmb_tipoDeControleEscravo.setVisible(false);

            this.lbl_kpEscravo.setVisible(false);
            this.lbl_kiEscravo.setVisible(false);
            this.lbl_kdEscravo.setVisible(false);
            this.txtF_kpEscravo.setVisible(false);
            this.txtF_kiEscravo.setVisible(false);
            this.txtF_kdEscravo.setVisible(false);
            this.transit[25] = 0;
        }

    }//GEN-LAST:event_cmb_serieOuCascataItemStateChanged

    private void chebx_exibirNivelDoTanque1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_exibirNivelDoTanque1ActionPerformed
        if (this.chebx_exibirNivelDoTanque1.isSelected()) {
            this.grafoManager.getPlotTodos().getRenderer().setBaseSeriesVisible(true);
            this.grafoManager.getPlotTodos().getRenderer().setBaseSeriesVisibleInLegend(true);
        } else {
            this.grafoManager.getPlotTodos().getRenderer().setBaseSeriesVisible(false);
            this.grafoManager.getPlotTodos().getRenderer().setBaseSeriesVisibleInLegend(false);
        }
    }//GEN-LAST:event_chebx_exibirNivelDoTanque1ActionPerformed

    private void chebx_exibirNivelDoTanque2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_exibirNivelDoTanque2ActionPerformed
        if (this.chebx_exibirNivelDoTanque2.isSelected()) {
            this.grafoManager.getPlotTodos().getRenderer(2).setBaseSeriesVisible(true);
            this.grafoManager.getPlotTodos().getRenderer(2).setBaseSeriesVisibleInLegend(true);
        } else {
            this.grafoManager.getPlotTodos().getRenderer(2).setBaseSeriesVisible(false);
            this.grafoManager.getPlotTodos().getRenderer(2).setBaseSeriesVisibleInLegend(false);

        }
    }//GEN-LAST:event_chebx_exibirNivelDoTanque2ActionPerformed

    private void chebx_exibirSetpointActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_exibirSetpointActionPerformed
        if (this.chebx_exibirSetpoint.isSelected()) {
            this.grafoManager.getPlotTodos().getRenderer(1).setBaseSeriesVisible(true);
            this.grafoManager.getPlotTodos().getRenderer(1).setBaseSeriesVisibleInLegend(true);
        } else {
            this.grafoManager.getPlotTodos().getRenderer(1).setBaseSeriesVisible(false);
            this.grafoManager.getPlotTodos().getRenderer(1).setBaseSeriesVisibleInLegend(false);

        }
    }//GEN-LAST:event_chebx_exibirSetpointActionPerformed

    private void chebx_graficoReferenciaEscravoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_graficoReferenciaEscravoActionPerformed
        if (this.chebx_graficoReferenciaEscravo.isSelected()) {
            this.grafoManager.getPlotTodos().getRenderer(3).setBaseSeriesVisible(true);
            this.grafoManager.getPlotTodos().getRenderer(3).setBaseSeriesVisibleInLegend(true);
        } else {
            this.grafoManager.getPlotTodos().getRenderer(3).setBaseSeriesVisible(false);
            this.grafoManager.getPlotTodos().getRenderer(3).setBaseSeriesVisibleInLegend(false);
        }
    }//GEN-LAST:event_chebx_graficoReferenciaEscravoActionPerformed

    private void txtF_kpMestreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtF_kpMestreActionPerformed
        this.simulatorConection.conversionKp(this.txtF_kpMestre);
    }//GEN-LAST:event_txtF_kpMestreActionPerformed

    private void txtF_kiMestreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtF_kiMestreActionPerformed
        this.simulatorConection.conversionKi(this.txtF_kiMestre);
    }//GEN-LAST:event_txtF_kiMestreActionPerformed

    private void txtF_kdMestreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtF_kdMestreActionPerformed
        this.simulatorConection.conversionKd(this.txtF_kdMestre);
    }//GEN-LAST:event_txtF_kdMestreActionPerformed

    private void txtF_kpEscravoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtF_kpEscravoActionPerformed
        this.simulatorConection.conversionKpEscravo(txtF_kpEscravo);
    }//GEN-LAST:event_txtF_kpEscravoActionPerformed

    private void txtF_kiEscravoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtF_kiEscravoActionPerformed
        this.simulatorConection.conversionKiEscravo(txtF_kiEscravo);
    }//GEN-LAST:event_txtF_kiEscravoActionPerformed

    private void txtF_kdEscravoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtF_kdEscravoActionPerformed
        this.simulatorConection.conversionKdEscravo(txtF_kdEscravo);
    }//GEN-LAST:event_txtF_kdEscravoActionPerformed

    private void chebx_ErroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_ErroActionPerformed
        if (this.chebx_Erro.isSelected()) {
            this.grafoManager.getPlotTodos().getRenderer(4).setBaseSeriesVisible(true);
            this.grafoManager.getPlotTodos().getRenderer(4).setBaseSeriesVisibleInLegend(true);
        } else {
            this.grafoManager.getPlotTodos().getRenderer(4).setBaseSeriesVisible(false);
            this.grafoManager.getPlotTodos().getRenderer(4).setBaseSeriesVisibleInLegend(false);
        }


    }//GEN-LAST:event_chebx_ErroActionPerformed

    private void chebx_graficoSinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_graficoSinalActionPerformed
        if (this.chebx_graficoSinal.isSelected()) {
            this.chebx_tensaoAplicadaSemSaturacao.setVisible(true);
            this.chebx_acaoProporcional.setVisible(true);
            this.chebx_acaoIntagral.setVisible(true);
            this.chebx_acaoDerivatica.setVisible(true);
            this.chebx_Tensao.setVisible(true);
            this.transit[27] = 2;

            this.pnl_grafico_todos.setVisible(true);
            this.pnl_grafico_todos.setSize(650, 285);
            this.grafoManager.chartPanelTodos.setPreferredSize(new java.awt.Dimension(650, 285));

            this.pnl_GraficoSinal.setVisible(true);
            this.pnl_GraficoSinal.setSize(650, 285);
            this.grafoManager.chartPanelSinal.setPreferredSize(new java.awt.Dimension(650, 285));

            this.grafoManager.taGrande[4] = false;
            this.grafoManager.taGrande[0] = false;

        } else {
            this.chebx_tensaoAplicadaSemSaturacao.setVisible(false);
            this.chebx_acaoProporcional.setVisible(false);
            this.chebx_acaoIntagral.setVisible(false);
            this.chebx_acaoDerivatica.setVisible(false);
            this.chebx_Tensao.setVisible(false);
            this.transit[27] = 1;

            this.pnl_grafico_todos.setVisible(true);
            this.grafoManager.taGrande[4] = true;
            this.pnl_GraficoSinal.setVisible(false);
            this.pnl_grafico_todos.setSize(650, 570);
            this.grafoManager.chartPanelTodos.setPreferredSize(new java.awt.Dimension(650, 570));

        }
    }//GEN-LAST:event_chebx_graficoSinalActionPerformed

    private void chebx_tensaoAplicadaSemSaturacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_tensaoAplicadaSemSaturacaoActionPerformed
        if (this.chebx_tensaoAplicadaSemSaturacao.isSelected()) {
            this.grafoManager.getPlotSinal().getRenderer(1).setBaseSeriesVisible(true);
            this.grafoManager.getPlotSinal().getRenderer(1).setBaseSeriesVisibleInLegend(true);
        } else {
            this.grafoManager.getPlotSinal().getRenderer(1).setBaseSeriesVisible(false);
            this.grafoManager.getPlotSinal().getRenderer(1).setBaseSeriesVisibleInLegend(false);
        }

    }//GEN-LAST:event_chebx_tensaoAplicadaSemSaturacaoActionPerformed

    private void chebx_acaoProporcionalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_acaoProporcionalActionPerformed
        if (this.chebx_acaoProporcional.isSelected()) {
            this.grafoManager.getPlotSinal().getRenderer(2).setBaseSeriesVisible(true);
            this.grafoManager.getPlotSinal().getRenderer(2).setBaseSeriesVisibleInLegend(true);
        } else {
            this.grafoManager.getPlotSinal().getRenderer(2).setBaseSeriesVisible(false);
            this.grafoManager.getPlotSinal().getRenderer(2).setBaseSeriesVisibleInLegend(false);
        }

    }//GEN-LAST:event_chebx_acaoProporcionalActionPerformed

    private void chebx_acaoIntagralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_acaoIntagralActionPerformed
        if (this.chebx_acaoIntagral.isSelected()) {
            this.grafoManager.getPlotSinal().getRenderer(3).setBaseSeriesVisible(true);
            this.grafoManager.getPlotSinal().getRenderer(3).setBaseSeriesVisibleInLegend(true);
        } else {
            this.grafoManager.getPlotSinal().getRenderer(3).setBaseSeriesVisible(false);
            this.grafoManager.getPlotSinal().getRenderer(3).setBaseSeriesVisibleInLegend(false);
        }

    }//GEN-LAST:event_chebx_acaoIntagralActionPerformed

    private void chebx_acaoDerivaticaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_acaoDerivaticaActionPerformed
        if (this.chebx_acaoDerivatica.isSelected()) {
            this.grafoManager.getPlotSinal().getRenderer(4).setBaseSeriesVisible(true);
            this.grafoManager.getPlotSinal().getRenderer(4).setBaseSeriesVisibleInLegend(true);
        } else {
            this.grafoManager.getPlotSinal().getRenderer(4).setBaseSeriesVisible(false);
            this.grafoManager.getPlotSinal().getRenderer(4).setBaseSeriesVisibleInLegend(false);
        }
    }//GEN-LAST:event_chebx_acaoDerivaticaActionPerformed

    private void chebx_TensaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chebx_TensaoActionPerformed
        if (this.chebx_Tensao.isSelected()) {
            this.grafoManager.getPlotSinal().getRenderer(0).setBaseSeriesVisible(true);
            this.grafoManager.getPlotSinal().getRenderer(0).setBaseSeriesVisibleInLegend(true);
        } else {
            this.grafoManager.getPlotSinal().getRenderer(0).setBaseSeriesVisible(false);
            this.grafoManager.getPlotSinal().getRenderer(0).setBaseSeriesVisibleInLegend(false);
        }

    }//GEN-LAST:event_chebx_TensaoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Radio_ClosedSystem;
    private javax.swing.JRadioButton Radio_OpenSytem;
    private javax.swing.JRadioButton Radio_selectedTank1;
    private javax.swing.JRadioButton Radio_selectedTank2;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JButton btn_swithOnOff;
    private javax.swing.JRadioButton channel1_Read;
    private javax.swing.JRadioButton channel1_Write;
    private javax.swing.JRadioButton channel2_Read;
    private javax.swing.JRadioButton channel2_Write;
    private javax.swing.JRadioButton channel3_Read;
    private javax.swing.JRadioButton channel3_Write;
    private javax.swing.JRadioButton channel4_Read;
    private javax.swing.JRadioButton channel4_Write;
    private javax.swing.JRadioButton channel5_Read;
    private javax.swing.JRadioButton channel5_Write;
    private javax.swing.JRadioButton channel6_Read;
    private javax.swing.JRadioButton channel6_Write;
    private javax.swing.JRadioButton channel7_Read;
    private javax.swing.JRadioButton channel7_Write;
    private javax.swing.JRadioButton channel8_Read;
    private javax.swing.JRadioButton channel8_Write;
    private javax.swing.JCheckBox chbx_constantesDeTempo;
    private javax.swing.JCheckBox chebx_Erro;
    private javax.swing.JCheckBox chebx_Tensao;
    private javax.swing.JCheckBox chebx_acaoDerivatica;
    private javax.swing.JCheckBox chebx_acaoIntagral;
    private javax.swing.JCheckBox chebx_acaoProporcional;
    private javax.swing.JCheckBox chebx_exibirNivelDoTanque1;
    private javax.swing.JCheckBox chebx_exibirNivelDoTanque2;
    private javax.swing.JCheckBox chebx_exibirSetpoint;
    private javax.swing.JCheckBox chebx_graficoReferenciaEscravo;
    private javax.swing.JCheckBox chebx_graficoSinal;
    private javax.swing.JCheckBox chebx_tensaoAplicadaSemSaturacao;
    private javax.swing.JComboBox cmb_serieOuCascata;
    private javax.swing.JComboBox cmb_tipoDeControle;
    private javax.swing.JComboBox cmb_tipoDeControleEscravo;
    private javax.swing.JComboBox cmbx_tipoDeSinal;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane7;
    private javax.swing.JLabel lbl_Malha;
    private javax.swing.JLabel lbl_Malha1;
    private javax.swing.JLabel lbl_advertencia;
    private javax.swing.JLabel lbl_amplitude;
    private javax.swing.JLabel lbl_canalDeEscrita;
    private javax.swing.JLabel lbl_canalDeLeitura;
    private javax.swing.JLabel lbl_conexao_Escrita0;
    private javax.swing.JLabel lbl_conexao_Escrita1;
    private javax.swing.JLabel lbl_conexao_Escrita2;
    private javax.swing.JLabel lbl_conexao_Escrita3;
    private javax.swing.JLabel lbl_conexao_Escrita4;
    private javax.swing.JLabel lbl_conexao_Escrita5;
    private javax.swing.JLabel lbl_conexao_Escrita6;
    private javax.swing.JLabel lbl_conexao_Escrita7;
    private javax.swing.JLabel lbl_conexao_leitura0;
    private javax.swing.JLabel lbl_conexao_leitura1;
    private javax.swing.JLabel lbl_conexao_leitura2;
    private javax.swing.JLabel lbl_conexao_leitura3;
    private javax.swing.JLabel lbl_conexao_leitura4;
    private javax.swing.JLabel lbl_conexao_leitura5;
    private javax.swing.JLabel lbl_conexao_leitura6;
    private javax.swing.JLabel lbl_conexao_leitura7;
    private javax.swing.JLabel lbl_configuracoes_conexao;
    private javax.swing.JLabel lbl_configuracoes_ip;
    private javax.swing.JLabel lbl_configuracoes_porta;
    private javax.swing.JLabel lbl_escravo;
    private javax.swing.JLabel lbl_fatorDeAceleracao;
    private javax.swing.JLabel lbl_grafico;
    private javax.swing.JLabel lbl_kdEscravo;
    private javax.swing.JLabel lbl_kdMestre;
    private javax.swing.JLabel lbl_kiEscravo;
    private javax.swing.JLabel lbl_kiMestre;
    private javax.swing.JLabel lbl_kpEscravo;
    private javax.swing.JLabel lbl_kpMestre;
    private javax.swing.JLabel lbl_mestre;
    private javax.swing.JLabel lbl_nivel;
    private javax.swing.JLabel lbl_offset;
    private javax.swing.JLabel lbl_overShootTanque1;
    private javax.swing.JLabel lbl_overShootTanque2;
    private javax.swing.JLabel lbl_periodo;
    private javax.swing.JLabel lbl_saida_erro_centimetro;
    private javax.swing.JLabel lbl_saida_nivel_centimetro;
    private javax.swing.JLabel lbl_saida_tempo_segundo;
    private javax.swing.JLabel lbl_saida_tensao_volts;
    private javax.swing.JLabel lbl_setPoint;
    private javax.swing.JLabel lbl_sinal;
    private javax.swing.JLabel lbl_tempo;
    private javax.swing.JLabel lbl_tempoDeAcomodacao10Tanque1;
    private javax.swing.JLabel lbl_tempoDeAcomodacao10Tanque2;
    private javax.swing.JLabel lbl_tempoDeAcomodacao2Tanque1;
    private javax.swing.JLabel lbl_tempoDeAcomodacao2Tanque2;
    private javax.swing.JLabel lbl_tempoDeAcomodacao5Tanque1;
    private javax.swing.JLabel lbl_tempoDeAcomodacao5Tanque2;
    private javax.swing.JLabel lbl_tempoDePicoTanque1;
    private javax.swing.JLabel lbl_tempoDePicoTanque2;
    private javax.swing.JLabel lbl_tempoDeSubida0100Tanque1;
    private javax.swing.JLabel lbl_tempoDeSubida0100Tanque2;
    private javax.swing.JLabel lbl_tempoDeSubida1090Tanque1;
    private javax.swing.JLabel lbl_tempoDeSubida1090Tanque2;
    private javax.swing.JLabel lbl_tempoDeSubida2080Tanque1;
    private javax.swing.JLabel lbl_tempoDeSubida2080Tanque2;
    private javax.swing.JLabel lbl_tensao;
    private javax.swing.JLabel lbl_tipoDeControEscravo;
    private javax.swing.JLabel lbl_tipoDeControle;
    private javax.swing.JLabel lbl_underShootTanque1;
    private javax.swing.JLabel lbl_underShootTanque2;
    private javax.swing.JPanel panel_Escrita;
    private javax.swing.JPanel panel_canalDeLeitura;
    private javax.swing.JPanel pnl_GraficoSinal;
    private javax.swing.JPanel pnl_entradaMalhaAberta;
    private javax.swing.JPanel pnl_grafico_aba1;
    private javax.swing.JPanel pnl_grafico_todos;
    private javax.swing.JPanel pnl_malha;
    private javax.swing.JPanel pnl_tanqueDeControle;
    private javax.swing.JTextField txtF_Periodo;
    private javax.swing.JTextField txtF_amplitude;
    private javax.swing.JTextField txtF_errorAltura;
    private javax.swing.JTextField txtF_fatorAceleracao;
    private javax.swing.JTextField txtF_fatorDerivativo;
    private javax.swing.JTextField txtF_fatorIntegrativo;
    private javax.swing.JTextField txtF_ip;
    private javax.swing.JTextField txtF_kdEscravo;
    private javax.swing.JTextField txtF_kdMestre;
    private javax.swing.JTextField txtF_kiEscravo;
    private javax.swing.JTextField txtF_kiMestre;
    private javax.swing.JTextField txtF_kpEscravo;
    private javax.swing.JTextField txtF_kpMestre;
    private javax.swing.JTextField txtF_offset;
    private javax.swing.JTextField txtF_overshootTanque1;
    private javax.swing.JTextField txtF_overshootTanque2;
    private javax.swing.JTextField txtF_porta;
    private javax.swing.JTextField txtF_setPoint;
    private javax.swing.JTextField txtF_tempo;
    private javax.swing.JTextField txtF_tempoDeAcomodacao10Tanque1;
    private javax.swing.JTextField txtF_tempoDeAcomodacao10Tanque2;
    private javax.swing.JTextField txtF_tempoDeAcomodacao2Tanque1;
    private javax.swing.JTextField txtF_tempoDeAcomodacao2Tanque2;
    private javax.swing.JTextField txtF_tempoDeAcomodacao5Tanque1;
    private javax.swing.JTextField txtF_tempoDeAcomodacao5Tanque2;
    private javax.swing.JTextField txtF_tempoDePicoTanque1;
    private javax.swing.JTextField txtF_tempoDePicoTanque2;
    private javax.swing.JTextField txtF_tempoDeSubida0100Tanque1;
    private javax.swing.JTextField txtF_tempoDeSubida0100Tanque2;
    private javax.swing.JTextField txtF_tempoDeSubida1090Tanque1;
    private javax.swing.JTextField txtF_tempoDeSubida1090Tanque2;
    private javax.swing.JTextField txtF_tempoDeSubida2080Tanque1;
    private javax.swing.JTextField txtF_tempoDeSubida2080Tanque2;
    private javax.swing.JTextField txtF_underShootTanque1;
    private javax.swing.JTextField txtF_underShootTanque2;
    private javax.swing.JTextField txtF_valorNivel;
    private javax.swing.JTextField txtF_valorNivelTanque2;
    private javax.swing.JTextField txtF_valorTensao;
    // End of variables declaration//GEN-END:variables
}