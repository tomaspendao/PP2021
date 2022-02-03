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

import edu.ma02.core.enumerations.Parameter;
import edu.ma02.core.enumerations.SensorType;
import edu.ma02.core.enumerations.Unit;
import edu.ma02.core.exceptions.MeasurementException;
import edu.ma02.core.exceptions.SensorException;
import edu.ma02.core.interfaces.ICartesianCoordinates;
import edu.ma02.core.interfaces.IGeographicCoordinates;
import edu.ma02.core.interfaces.IMeasurement;
import edu.ma02.core.interfaces.ISensor;
import java.time.LocalDateTime;

public class Sensor implements ISensor {

    /**
     * ID do sensor
     */
    private String id;

    /**
     * Tipo do sensor
     */
    private SensorType sensorType;

    /**
     * Parametros lidos pelo sensor
     */
    private Parameter parameter;

    /**
     * Coordenadas cartesianas do sensor
     */
    private CartesianCoordinates cartesianCoordinates;

    /**
     * Coordenadas geográficas do sensor
     */
    private GeographicCoordinates geographicCoordinates;

    /**
     * Conjunto de medições efetuadas pelo sensor
     */
    private Measurement[] measurements;

    /**
     * Contador do número de medições efetuadas pelo sensor
     */
    private int measurementsCount;

    /**
     * Método construtor para uma instância de Sensor
     *
     * @param id ID do sensor caso seja válido
     * @param cartesianCoordinates Coordenadas cartesianas do sensor
     * @param geographicCoordinates Coordenadas geográficas do sensor
     */
    public Sensor(String id, CartesianCoordinates cartesianCoordinates, GeographicCoordinates geographicCoordinates) {
        if (id.length() == 10) {
            this.id = id;
            this.cartesianCoordinates = cartesianCoordinates;
            this.geographicCoordinates = geographicCoordinates;
            this.measurementsCount = 0;
            this.measurements = new Measurement[measurementsCount + 1];

            //setSensorType();
            //setParameter();
        }
    }

    /**
     * Método setSensorType com o objetivo de definir o tipo do sensor a partir
     * dos dois primeiros caracteres do ID
     *
     * @return true se for um tipo de sensor válido, falso em caso contrátrio
     */
    private boolean setSensorType() {
        String str = this.id;

        str = str.substring(0, 2);

        switch (str) {
            case "QA":
                this.sensorType = SensorType.AIR;
                return true;
            case "RU":
                this.sensorType = SensorType.NOISE;
                return true;
            case "ME":
                this.sensorType = SensorType.WEATHER;
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * Método setParameter cuja função é definir os parâmetros que o sensor mede
     *
     * @return true caso seja possível definir os parâmetros, false caso
     * contrário
     */
    private boolean setParameter() {
        Parameter[] parameters = this.sensorType.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            if (this.id.contains(parameters[i].toString())) {
                this.parameter = parameters[i];
                
                return true;
            }
        }
        
        return false;
    }

    /**
     * Override do getter do ID cuja função é obter o ID do sensor
     *
     * @return ID do sensor
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Override do getter do tipo de sensor cuja função é obter o tipo de sensor
     *
     * @return Tipo do sensor
     */
    @Override
    public SensorType getType() {
        try {
            setSensorType();
            setParameter();
            
            if (this.sensorType == null) {
                throw new NullPointerException("O tipo de sensor é nulo");
            }
        } catch (NullPointerException e) {

        }
        
        return this.sensorType;
    }

    /**
     * Override do método getCartesianCoordinates cuja função é obter as
     * corrdenadas cartesianas do sensor
     *
     * @return Coordenadas cartesianas do sensor
     */
    @Override
    public ICartesianCoordinates getCartesianCoordinates() {
        return this.cartesianCoordinates;
    }

    /**
     * Override do método getGeographicCoordinates cuja função é obter as
     * coordenadas geográficas do sensor
     *
     * @return Coordenadas geográficas do sensor
     */
    @Override
    public IGeographicCoordinates getGeographicCoordinates() {
        return this.geographicCoordinates;
    }

    /**
     * Override do método addMeasurement cuja função é adicionar uma medição
     * caso seja válida. Para uma medição ser válida a unidade tem que ser
     * compatível com o tipo do sensor e ter um valor válido
     *
     * @param value Valor da medição
     * @param date Data da medição
     * @param unit Unidade de medição
     * @return true caso a medição foi adicionada, false caso contrário
     * @throws SensorException
     * @throws MeasurementException Se o value é igual a -99 ou qualuqer
     * parametro é invalido
     */
    @Override
    public boolean addMeasurement(double value, LocalDateTime date, String unit) throws SensorException,
            MeasurementException {
        try {
            if (!(unit.equals(Unit.getUnitString(this.getParameter().getUnit())))) {
                throw new SensorException("A unidade não é compatível com a unidade pré-definida para o sensor");
            }
            
            if (value == -99 || date == null) {
                throw new MeasurementException("O valor é -99 ou algum parâmetro é inválido");
            }
        } catch (MeasurementException | SensorException ex) {
            return false;
        }
        
        if (this.measurementsCount == this.measurements.length) {
            Measurement[] temp = new Measurement[this.measurements.length + 20];
            
            for (int i = 0; i < this.measurementsCount; i++) {
                temp[i] = this.measurements[i];
            }
            this.measurements = temp;
        }
        
        Measurement newMeasurement = new Measurement(value, date);
        
        if (findMeasurement(newMeasurement) == true) {            
            return false;
        }
        
        this.measurements[this.measurementsCount] = newMeasurement;
        this.measurementsCount++;
        
        return true;
    }

    /**
     * Método findMeasurement cuja função é encontrar uma medição
     *
     * @param measurement Medição a procurar
     * @return true caso encontre a medição, false caso não encontre a medição
     */
    private boolean findMeasurement(Measurement measurement) {
        for (int i = 0; i < this.measurementsCount; i++) {
            if (this.measurements[i].equals(measurement)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Override do método getNumMeasurements cuja função é obter o número de
     * medições efetuadas pelo sensor
     *
     * @return Número de medições do sensor
     */
    @Override
    public int getNumMeasurements() {
        return this.measurementsCount;
    }

    /**
     * Override do método getMeasurements cuja função é obter o conjunto de
     * medições
     *
     * @return conjunto de medições
     */
    @Override
    public IMeasurement[] getMeasurements() {
        Measurement[] res = new Measurement[measurementsCount];
        
        for (int i = 0; i < res.length; i++) {
            res[i] = this.measurements[i];
        }
        
        return res;
    }

    /**
     * Override do método getParameter cuja função é obter os parametros lidos
     * pelo sensor
     *
     * @return Parâmetros lidos pelo sensor
     */
    @Override
    public Parameter getParameter() {
        try {
            setSensorType();
            setParameter();
            
            if (this.parameter == null) {
                throw new NullPointerException("O parâmetro é nulo");
            }
            
            return this.parameter;
        } catch (NullPointerException e) {
            return null;
        }
    }
}