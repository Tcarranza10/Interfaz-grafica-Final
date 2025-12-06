package batalla.vista;

/**
 * Formulario para mostrar los detalles de una partida cargada
 */
public class formHistorial extends javax.swing.JFrame {
    private javax.swing.JTextArea txtCombatLog;
    private javax.swing.JScrollPane scrollCombatLog;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblHeroeHeader;
    private javax.swing.JLabel lblVillanoHeader;
    private javax.swing.JLabel lblGanadorHeader;
    private javax.swing.JLabel lblTurnosHeader;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // Labels para estadísticas
    private javax.swing.JLabel lblMayorDanio;
    private javax.swing.JLabel lblBatallaMasLarga;
    private javax.swing.JLabel lblArmasHeroe;
    private javax.swing.JLabel lblArmasVillano;
    private javax.swing.JLabel lblSupremosHeroe;
    private javax.swing.JLabel lblSupremosVillano;
    private javax.swing.JLabel lblWinrateHeroe;
    private javax.swing.JLabel lblWinrateVillano;

    public formHistorial() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        scrollCombatLog = new javax.swing.JScrollPane();
        txtCombatLog = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblMayorDanio = new javax.swing.JLabel();
        lblBatallaMasLarga = new javax.swing.JLabel();
        lblArmasHeroe = new javax.swing.JLabel();
        lblArmasVillano = new javax.swing.JLabel();
        lblSupremosHeroe = new javax.swing.JLabel();
        lblSupremosVillano = new javax.swing.JLabel();
        lblWinrateHeroe = new javax.swing.JLabel();
        lblWinrateVillano = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DETALLES DE PARTIDA");

    lblHeroeHeader = new javax.swing.JLabel();
    lblVillanoHeader = new javax.swing.JLabel();
    lblGanadorHeader = new javax.swing.JLabel();
    lblTurnosHeader = new javax.swing.JLabel();
    lblHeroeHeader.setForeground(new java.awt.Color(255,255,255));
    lblVillanoHeader.setForeground(new java.awt.Color(255,255,255));
    lblGanadorHeader.setForeground(new java.awt.Color(255,255,255));
    lblTurnosHeader.setForeground(new java.awt.Color(255,255,255));
    lblHeroeHeader.setText("Héroe: -");
    lblVillanoHeader.setText("Villano: -");
    lblGanadorHeader.setText("Ganador: -");
    lblTurnosHeader.setText("Turnos: -");

        txtCombatLog.setEditable(false);
        txtCombatLog.setColumns(20);
        txtCombatLog.setRows(5);
        scrollCombatLog.setViewportView(txtCombatLog);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Estadísticas"));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel2.setText("Estadísticas de la Partida:");

        lblMayorDanio.setText("Mayor daño en un solo ataque: -");

        lblBatallaMasLarga.setText("Batalla más larga: -");

        lblArmasHeroe.setText("Total armas invocadas héroe: -");

        lblArmasVillano.setText("Total armas invocadas villano: -");

        lblSupremosHeroe.setText("Ataques supremos ejecutados héroe: -");

        lblSupremosVillano.setText("Ataques supremos ejecutados villano: -");

        lblWinrateHeroe.setText("Winrate héroe: -");

        lblWinrateVillano.setText("Winrate villano: -");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(lblMayorDanio)
                    .addComponent(lblBatallaMasLarga)
                    .addComponent(lblArmasHeroe)
                    .addComponent(lblArmasVillano)
                    .addComponent(lblSupremosHeroe)
                    .addComponent(lblSupremosVillano)
                    .addComponent(lblWinrateHeroe)
                    .addComponent(lblWinrateVillano))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMayorDanio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBatallaMasLarga)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblArmasHeroe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblArmasVillano)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSupremosHeroe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSupremosVillano)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblWinrateHeroe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblWinrateVillano)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCerrar.setText("Cerrar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(lblHeroeHeader)
                    .addComponent(lblVillanoHeader)
                    .addComponent(lblGanadorHeader)
                    .addComponent(lblTurnosHeader))
                .addGap(200, 200, 200))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHeroeHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblVillanoHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGanadorHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTurnosHeader)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollCombatLog, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCerrar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollCombatLog, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCerrar)
                .addContainerGap())
        );

        pack();
    }

    public void setCombatLog(String log) {
        txtCombatLog.setText(log);
    }

    public void setMayorDanio(String texto) {
        lblMayorDanio.setText("Mayor daño en un solo ataque: " + texto);
    }

    public void setBatallaMasLarga(String texto) {
        lblBatallaMasLarga.setText("Batalla más larga: " + texto);
    }

    public void setArmasHeroe(String texto) {
        lblArmasHeroe.setText("Total armas invocadas héroe: " + texto);
    }

    public void setArmasVillano(String texto) {
        lblArmasVillano.setText("Total armas invocadas villano: " + texto);
    }

    public void setSupremosHeroe(String texto) {
        lblSupremosHeroe.setText("Ataques supremos ejecutados héroe: " + texto);
    }

    public void setSupremosVillano(String texto) {
        lblSupremosVillano.setText("Ataques supremos ejecutados villano: " + texto);
    }

    public void setWinrateHeroe(String texto) {
        lblWinrateHeroe.setText("Winrate héroe: " + texto);
    }

    public void setWinrateVillano(String texto) {
        lblWinrateVillano.setText("Winrate villano: " + texto);
    }

    public javax.swing.JButton getBtnCerrar() {
        return btnCerrar;
    }

    // Header setters for hero/villain/winner/turns
    public void setHeroeNombre(String texto) { lblHeroeHeader.setText("Héroe: " + texto); }
    public void setVillanoNombre(String texto) { lblVillanoHeader.setText("Villano: " + texto); }
    public void setGanador(String texto) { lblGanadorHeader.setText("Ganador: " + texto); }
    public void setTurnos(String texto) { lblTurnosHeader.setText("Turnos: " + texto); }
}
