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

import edu.ma02.core.interfaces.ICartesianCoordinates;

public class CartesianCoordinates implements ICartesianCoordinates {

    /**
     * Coordenada x da posição cartesiana do sensor
     */
    private final double x;

    /**
     * Coordenada y da posição cartesiana do sensor
     */
    private final double y;

    /**
     * Coordenada z da posição cartesiana do sensor
     */
    private final double z;

    /**
     * Método construtor duma instância de coordenadas dum determinado sensor
     *
     * @param x Coordenada x da posição cartesiana do sensor
     * @param y Coordenada y da posição cartesiana do sensor
     * @param z Coordenada z da posição cartesiana do sensor
     */
    public CartesianCoordinates(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Getter da coordenada x da posição cartesiana do sensor
     *
     * @return Coordenada x da posição cartesiana do sensor
     */
    @Override
    public double getX() {
        return this.x;
    }

    /**
     * Getter da coordenada y da posição cartesiana do sensor
     *
     * @return Coordenada y da posição cartesiana do sensor
     */
    @Override
    public double getY() {
        return this.y;
    }

    /**
     * Getter da coordenada z da posição cartesiana do sensor
     *
     * @return Coordenada z da posição cartesiana do sensor
     */
    @Override
    public double getZ() {
        return this.z;
    }
}
