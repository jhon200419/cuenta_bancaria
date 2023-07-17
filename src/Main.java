import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static JTextField nombreField;
    private static JTextField cedulaField;
    private static JTextField numeroCuentaField;
    private static JTextField saldoInicialField;
    private static JRadioButton cuentaAhorrosRadioButton;
    private static JRadioButton cuentaCorrienteRadioButton;
    private static JTextArea estadoCuentaArea;
    private static ArrayList<CuentaBancaria> cuentas;

    public static void main(String[] args) {
        // Inicializar lista de cuentas
        cuentas = new ArrayList<>();

        // Crear ventana principal
        JFrame frame = new JFrame("Sistema Bancario");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        // Panel de información del cliente
        JPanel clientePanel = new JPanel(new GridLayout(6, 2));
        clientePanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        clientePanel.add(nombreField);
        clientePanel.add(new JLabel("Cédula:"));
        cedulaField = new JTextField();
        clientePanel.add(cedulaField);
        clientePanel.add(new JLabel("Número de cuenta:"));
        numeroCuentaField = new JTextField();
        clientePanel.add(numeroCuentaField);
        clientePanel.add(new JLabel("Saldo inicial:"));
        saldoInicialField = new JTextField();
        clientePanel.add(saldoInicialField);

        // Panel de selección de tipo de cuenta
        JPanel tipoCuentaPanel = new JPanel(new GridLayout(1, 2));
        ButtonGroup tipoCuentaGroup = new ButtonGroup();
        cuentaAhorrosRadioButton = new JRadioButton("Cuenta de Ahorros");
        cuentaAhorrosRadioButton.setSelected(true);
        tipoCuentaGroup.add(cuentaAhorrosRadioButton);
        tipoCuentaPanel.add(cuentaAhorrosRadioButton);
        cuentaCorrienteRadioButton = new JRadioButton("Cuenta Corriente");
        tipoCuentaGroup.add(cuentaCorrienteRadioButton);
        tipoCuentaPanel.add(cuentaCorrienteRadioButton);
        clientePanel.add(new JLabel("Tipo de cuenta:"));
        clientePanel.add(tipoCuentaPanel);

        // Botón para crear cuenta
        JButton crearButton = new JButton("Crear cuenta");
        crearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crearCuenta();
            }
        });
        clientePanel.add(crearButton);

        frame.add(clientePanel, BorderLayout.NORTH);

        // Área de texto para el estado de cuenta
        estadoCuentaArea = new JTextArea();
        frame.add(new JScrollPane(estadoCuentaArea), BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel();

        // Botón para eliminar cuenta
        JButton eliminarButton = new JButton("Eliminar cuenta");
        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarCuenta();
            }
        });
        buttonPanel.add(eliminarButton);

        // Botón para editar cuenta
        JButton editarButton = new JButton("Editar cuenta");
        editarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarCuenta();
            }
        });
        buttonPanel.add(editarButton);

        // Botón para realizar depósito
        JButton depositarButton = new JButton("Depositar");
        depositarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarDeposito();
            }
        });
        buttonPanel.add(depositarButton);

        // Botón para realizar retiro
        JButton retirarButton = new JButton("Retirar");
        retirarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarRetiro();
            }
        });
        buttonPanel.add(retirarButton);

        // Botón para ver movimientos
        JButton movimientosButton = new JButton("Ver movimientos");
        movimientosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verMovimientos();
            }
        });
        buttonPanel.add(movimientosButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void crearCuenta() {
        String nombre = nombreField.getText();
        String cedula = cedulaField.getText();
        String numeroCuenta = numeroCuentaField.getText();
        String saldoInicialStr = saldoInicialField.getText();

        if (!nombre.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(null, "Ingrese un nombre válido (solo letras)");
            return;
        }

        if (!cedula.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Ingrese una cédula válida (solo números)");
            return;
        }

        if (!numeroCuenta.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Ingrese un número de cuenta válido (solo números)");
            return;
        }

        if (!saldoInicialStr.matches("\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(null, "Ingrese un saldo inicial válido (solo números)");
            return;
        }

        double saldoInicial = Double.parseDouble(saldoInicialStr);

        if (buscarCuenta(numeroCuenta) != null) {
            JOptionPane.showMessageDialog(null, "Ya existe una cuenta con ese número de cuenta");
            return;
        }

        if (buscarCuentaPorCedula(cedula) != null) {
            int opcion = JOptionPane.showConfirmDialog(null,
                    "Ya existe una cuenta con esa cédula. ¿Desea crear una nueva cuenta con la misma cédula?",
                    "Cédula duplicada", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.NO_OPTION) {
                return;
            }
        }

        Cliente titular = new Cliente(nombre, cedula);
        CuentaBancaria cuenta;
        if (cuentaAhorrosRadioButton.isSelected()) {
            cuenta = new CuentaAhorros(titular, numeroCuenta, saldoInicial);
        } else {
            cuenta = new CuentaCorriente(titular, numeroCuenta, saldoInicial);
        }

        cuentas.add(cuenta);
        actualizarEstadoCuenta();
        JOptionPane.showMessageDialog(null, "Cuenta creada exitosamente");
    }

    private static void eliminarCuenta() {
        String numeroCuenta = JOptionPane.showInputDialog(null, "Ingrese el número de cuenta a eliminar");
        CuentaBancaria cuentaEncontrada = buscarCuenta(numeroCuenta);
        if (cuentaEncontrada != null) {
            cuentas.remove(cuentaEncontrada);
            estadoCuentaArea.setText("");
            JOptionPane.showMessageDialog(null, "Cuenta eliminada correctamente");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró la cuenta especificada");
        }
    }

    private static void editarCuenta() {
        String numeroCuenta = JOptionPane.showInputDialog(null, "Ingrese el número de cuenta a editar");
        CuentaBancaria cuentaEncontrada = buscarCuenta(numeroCuenta);
        if (cuentaEncontrada != null) {
            String[] opciones = {"Nombre", "Cédula", "Saldo"};
            String seleccion = (String) JOptionPane.showInputDialog(null, "Seleccione la opción a editar",
                    "Editar cuenta", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

            if (seleccion != null) {
                switch (seleccion) {
                    case "Nombre":
                        String nuevoNombre = JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre");
                        if (!nuevoNombre.matches("[a-zA-Z ]+")) {
                            JOptionPane.showMessageDialog(null, "Ingrese un nombre válido (solo letras)");
                            return;
                        }
                        cuentaEncontrada.getTitular().setNombre(nuevoNombre);
                        break;
                    case "Cédula":
                        String nuevaCedula = JOptionPane.showInputDialog(null, "Ingrese la nueva cédula");
                        if (!nuevaCedula.matches("\\d+")) {
                            JOptionPane.showMessageDialog(null, "Ingrese una cédula válida (solo números)");
                            return;
                        }
                        if (buscarCuentaPorCedula(nuevaCedula) != null) {
                            JOptionPane.showMessageDialog(null, "Ya existe una cuenta con esa cédula");
                            return;
                        }
                        cuentaEncontrada.getTitular().setCedula(nuevaCedula);
                        break;
                    case "Saldo":
                        String nuevoSaldo = JOptionPane.showInputDialog(null, "Ingrese el nuevo saldo");
                        if (!nuevoSaldo.matches("\\d+(\\.\\d+)?")) {
                            JOptionPane.showMessageDialog(null, "Ingrese un saldo válido (solo números)");
                            return;
                        }
                        try {
                            double saldo = Double.parseDouble(nuevoSaldo);
                            cuentaEncontrada.setSaldo(saldo);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido para el saldo");
                        }
                        break;
                }

                actualizarEstadoCuenta();
                JOptionPane.showMessageDialog(null, "Cuenta actualizada correctamente");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró la cuenta especificada");
        }
    }

    private static void realizarDeposito() {
        String numeroCuenta = JOptionPane.showInputDialog(null, "Ingrese el número de cuenta para realizar el depósito");
        CuentaBancaria cuentaEncontrada = buscarCuenta(numeroCuenta);
        if (cuentaEncontrada != null) {
            String montoDeposito = JOptionPane.showInputDialog(null, "Ingrese el monto a depositar");
            if (!montoDeposito.matches("\\d+(\\.\\d+)?")) {
                JOptionPane.showMessageDialog(null, "Ingrese un monto válido (solo números)");
                return;
            }
            try {
                double monto = Double.parseDouble(montoDeposito);
                cuentaEncontrada.depositar(monto);
                actualizarEstadoCuenta();
                JOptionPane.showMessageDialog(null, "Depósito realizado correctamente");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido para el monto de depósito");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró la cuenta especificada");
        }
    }

    private static void realizarRetiro() {
        String numeroCuenta = JOptionPane.showInputDialog(null, "Ingrese el número de cuenta para realizar el retiro");
        CuentaBancaria cuentaEncontrada = buscarCuenta(numeroCuenta);
        if (cuentaEncontrada != null) {
            String montoRetiro = JOptionPane.showInputDialog(null, "Ingrese el monto a retirar");
            if (!montoRetiro.matches("\\d+(\\.\\d+)?")) {
                JOptionPane.showMessageDialog(null, "Ingrese un monto válido (solo números)");
                return;
            }
            try {
                double monto = Double.parseDouble(montoRetiro);
                cuentaEncontrada.retirar(monto);
                actualizarEstadoCuenta();
                JOptionPane.showMessageDialog(null, "Retiro realizado correctamente");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido para el monto de retiro");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró la cuenta especificada");
        }
    }

    private static void verMovimientos() {
        String numeroCuenta = JOptionPane.showInputDialog(null, "Ingrese el número de cuenta para ver los movimientos");
        CuentaBancaria cuentaEncontrada = buscarCuenta(numeroCuenta);
        if (cuentaEncontrada != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Movimientos de la cuenta ===\n");
            for (Movimiento movimiento : cuentaEncontrada.getMovimientos()) {
                sb.append(movimiento.getTipo()).append(": ").append(movimiento.getMonto()).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró la cuenta especificada");
        }
    }

    private static CuentaBancaria buscarCuenta(String numeroCuenta) {
        for (CuentaBancaria cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        return null;
    }

    private static CuentaBancaria buscarCuentaPorCedula(String cedula) {
        for (CuentaBancaria cuenta : cuentas) {
            if (cuenta.getTitular().getCedula().equals(cedula)) {
                return cuenta;
            }
        }
        return null;
    }

    private static void actualizarEstadoCuenta() {
        if (!cuentas.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Estado de cuenta ===\n");
            for (CuentaBancaria cuenta : cuentas) {
                sb.append(cuenta.obtenerEstadoCuenta()).append("\n");
            }
            estadoCuentaArea.setText(sb.toString());
        } else {
            estadoCuentaArea.setText("");
        }
    }
}