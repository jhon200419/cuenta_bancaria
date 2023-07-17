
import java.util.ArrayList;
import java.util.List;
abstract class CuentaBancaria {
    protected Cliente titular;
    protected String numeroCuenta;
    protected double saldo;
    protected List<Movimiento> movimientos;

    public abstract void retirar(double cantidad);

    public abstract void depositar(double cantidad);

    public CuentaBancaria(Cliente titular, String numeroCuenta, double saldoInicial) {
        this.titular = titular;
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
        this.movimientos = new ArrayList<>();
    }

    public Cliente getTitular() {
        return titular;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String obtenerEstadoCuenta() {
        StringBuilder sb = new StringBuilder();
        sb.append("Titular de la cuenta: ").append(titular.getNombre()).append("\n");
        sb.append("Tipo de cuenta: ").append(this instanceof CuentaAhorros ? "Ahorros" : "Corriente").append("\n");
        sb.append("Cédula del titular: ").append(titular.getCedula()).append("\n");
        sb.append("Número de cuenta: ").append(numeroCuenta).append("\n");
        sb.append("Saldo actual: ").append(saldo).append("\n");
        return sb.toString();
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }
}