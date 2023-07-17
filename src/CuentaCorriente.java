import javax.swing.*;

class CuentaCorriente extends CuentaBancaria {
    public CuentaCorriente(Cliente titular, String numeroCuenta, double saldoInicial) {
        super(titular, numeroCuenta, saldoInicial);
    }
    private static final double LIMITE_SOBREGIRO = 1000.0;

    @Override
    public void retirar(double cantidad) {
        if (saldo >= cantidad) {
            saldo -= cantidad;
            System.out.println("Se retiraron " + cantidad + " unidades. Saldo actual: " + saldo);
            Movimiento movimiento = new Movimiento("Retiro", cantidad);
            movimientos.add(movimiento);
        } else {
            double sobregiroNecesario = cantidad - saldo;
            if (sobregiroNecesario <= LIMITE_SOBREGIRO) {
                int opcion = JOptionPane.showConfirmDialog(null,
                        "Saldo insuficiente. ¿Desea realizar un retiro con sobregiro?",
                        "Saldo insuficiente",
                        JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    sobregiro(sobregiroNecesario);
                    saldo -= cantidad;
                    System.out.println("Se retiraron " + cantidad + " unidades. Saldo actual: " + saldo);
                    Movimiento movimiento = new Movimiento("Retiro", cantidad);
                    movimientos.add(movimiento);
                } else {
                    JOptionPane.showMessageDialog(null, "Saldo insuficiente");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se permite un sobregiro mayor a $1000");
            }
        }
    }



    @Override
    public void depositar(double cantidad) {
        saldo += cantidad;
        System.out.println("Se depositaron " + cantidad + " unidades. Saldo actual: " + saldo);
        Movimiento movimiento = new Movimiento("Depósito", cantidad);
        movimientos.add(movimiento);
    }

    private void sobregiro(double cantidad) {
        saldo -= cantidad;
        System.out.println("Se realizó un sobregiro de " + cantidad + " unidades. Saldo actual: " + saldo);
        Movimiento movimiento = new Movimiento("Sobregiro", cantidad);
        movimientos.add(movimiento);
    }

}