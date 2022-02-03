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

import edu.ma02.core.interfaces.IGeographicCoordinates;

public class GeographicCoordinates implements IGeographicCoordinates{
    
    /**
     * Latitude do sensor 
     */
    private final double latitude;
    
    /**
     * Longitude do sensor
     */
    private final double longitude;

    /**
     * Método construtor para as coordenadas geográficas dum sensor
     * @param latitude Latitude dum sensor
     * @param longitude Longitude dum sensor
     */
    public GeographicCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    /**
     * Override do getter da latitude dum sensor
     * @return Latitude dum sensor
     */
    @Override
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Override do getter da longitude dum sensor
     * @return Longitude dum sensor
     */
    @Override
    public double getLongitude() {
        return this.longitude;
    }
}