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

import edu.ma02.core.interfaces.IStatistics;

public class Statistics implements IStatistics{

    /**
     * Descrição da estatística
     */
    private final String description;
 
    /**
     * Valor da estatística
     */
    private final double value;
 
    /**
     * Método construtor duma instância duma estatística
     *
     * @param description Descrição da estatística
     * @param value Valor da estatíscia
     */
    public Statistics(String description, double value) {
        this.description = description;
        this.value = value;
    }
 
    /**
     * Override do método getDescription cuja função é obter a descrição da
     * estatística
     *
     * @return A descriçã da estatística
     */
    @Override
    public String getDescription() {
        return this.description;
    }
 
    /**
     * Override do método getValue cuja finalidade é obter o valor da
     * estatística
     *
     * @return O valor da estatística
     */
    @Override
    public double getValue() {
        return this.value;
    }
}