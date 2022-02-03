/*
* Nome: Daniel da Silva Pinto
* Número: 8200412
* Turma: LSIRC1T1
*
* Nome: Tomás Prior Pendão
* Número: 8170308
* Turma: LSIRC1T1
 */
package tp;

import edu.ma02.core.interfaces.IMeasurement;
import java.time.LocalDateTime;

public class Measurement implements IMeasurement {

    /**
     * Valor da medição
     */
    private final double value;

    /**
     * Data da medição
     */
    private final LocalDateTime time;

    /**
     * Método construtor para uma instância duma medição
     *
     * @param value Valor da medição
     * @param time Data da medição
     */
    public Measurement(double value, LocalDateTime time) {
        this.value = value;
        this.time = time;
    }

    /**
     * Override do getter para obter a data da medição
     *
     * @return Data da medição
     */
    @Override
    public LocalDateTime getTime() {
        return this.time;
    }

    /**
     * Override do getter do valor da medição
     *
     * @return Valor da medição
     */
    @Override
    public double getValue() {
        return this.value;
    }

    /**
     *
     * @param obj
     * @return true se os measurements forem iguais (foram capturados ao mesmo
     * tempo e têm o memso valor) ou false se não forem iguais
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Measurement other = (Measurement) obj;
        
        return this.value == other.value && this.time.equals(other.time);
    }
}
