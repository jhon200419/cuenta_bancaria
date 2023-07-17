import javax.swing.*;

class CuentaAhorros extends CuentaBancaria {
    public CuentaAhorros(Cliente titular, String numeroCuenta, double saldoInicial) {
        super(titular, numeroCuenta, saldoInicial);
    }

    @Override
    public void retirar(double cantidad) {
        if (cantidad <= saldo) {
            saldo -= cantidad;
            System.out.println("Se retiraron " + cantidad + " unidades. Saldo actual: " + saldo);
            Movimiento movimiento = new Movimiento("Retiro", cantidad);
            movimientos.add(movimiento);
        } else {
            JOptionPane.showMessageDialog(null, "Saldo insuficiente");
        }
    }

    @Override
    public void depositar(double cantidad) {
        saldo += cantidad;
        System.out.println("Se depositaron " + cantidad + " unidades. Saldo actual: " + saldo);
        Movimiento movimiento = new Movimiento("Depósito", cantidad);
        movimientos.add(movimiento);
    }
}