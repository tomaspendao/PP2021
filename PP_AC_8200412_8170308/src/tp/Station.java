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

import edu.ma02.core.exceptions.MeasurementException;
import edu.ma02.core.exceptions.SensorException;
import edu.ma02.core.exceptions.StationException;
import edu.ma02.core.interfaces.ISensor;
import edu.ma02.core.interfaces.IStation;
import java.time.LocalDateTime;

public class Station implements IStation {

    /**
     * Nome da estação metereológica
     */
    private final String name;

    /**
     * Conjunto dos sensores na estação metereológica
     */
    private Sensor[] sensors;

    /**
     * Contador do número de sensores na estação metereológica
     */
    private int sensorCounter;

    /**
     * Método construror duma instância duma estação metereológica com um
     * determinado nome, contador a 0 (zero) e inicialização do conjunto dos
     * sensores
     *
     * @param name Nome da estação metereológica
     */
    public Station(String name) {
        this.name = name;
        this.sensorCounter = 0;
        this.sensors = new Sensor[sensorCounter + 1];
    }

    /**
     * Override do método getName cuja função é obter o nome da estação
     * metereológica
     *
     * @return Nome da estação metereológica
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Override do método addSensor cuja finalidade é adicionar um sensor a uma
     * estação metreológica
     *
     * @param sensor Sensor a adicionar à estação metereológica
     * @return true se o sensor for adicionado, false caso o sensor não seja
     * adicionado
     * @throws StationException retorna exceção se o parametro sensor for null
     * @throws SensorException retorna exceção se o id do sensor for inválido
     * i.e diferente de 10 caracteres
     */
    @Override
    public boolean addSensor(ISensor sensor) throws StationException, SensorException {
        if (findSensor(sensor.getId()) == true) {
            return false;
        }

        try {
            if (sensor == null) {
                throw new StationException("O sensor é nulo");
            }
            
            if (sensor.getId().length() != 10) {
                throw new SensorException("O sensorId é inválido (ex: tem um número de caracteres diferente de 10)");
            }
        } catch (StationException | SensorException ex) {
            return false;
        }

        if (this.sensorCounter == this.sensors.length) {
            Sensor[] temp = new Sensor[this.sensors.length + 10];
            
            for (int i = 0; i < this.sensorCounter; i++) {
                temp[i] = this.sensors[i];
            }
            
            this.sensors = temp;
        }

        this.sensors[this.sensorCounter] = (Sensor) sensor;
        this.sensorCounter++;
        
        return true;
    }

    /**
     * Override do método addMeasurement cujo objetivo é adicionar uma medição
     * com base no ID do sensor, no valor da medição, a data da medição e a
     * unidade da medição
     *
     * @param sensorId ID do sensor
     * @param value Valor da medição
     * @param date Data da medição
     * @param unit Unidade da medição
     * @return true caso a medição tenha sido adicionada, false caso não tenha
     * sido adicionada
     * @throws StationException retorna exceção se o sensor não existir ou se
     * qualquer parametro seja null
     * @throws SensorException exceção retornada do metodo addMeasurement da
     * classe sensor
     * @throws MeasurementException exceção retornada do metodo addMeasurement
     * da classe sensor
     */
    @Override
    public boolean addMeasurement(String sensorId, double value, LocalDateTime date, String unit) throws
            StationException, SensorException, MeasurementException {
        try {
            if (findSensor(sensorId) == false) {
                throw new StationException("O sensor não existe");
            }
            
            if ((Double) value == null || date == null || unit == null) {
                throw new StationException("Algum parâmetro é nulo");
            }
        } catch (StationException ex) {
            return false;
        }
        
        int index = findAndReturnSensor(sensorId);
        
        try {
            this.sensors[index].addMeasurement(value, date, unit);
        } catch (MeasurementException | SensorException e) {
            return false;
        }
        
        return true;
    }

    /**
     * Override do método getSensors cujo papel é obter sensores
     *
     * @return Sensores
     */
    @Override
    public ISensor[] getSensors() {
        Sensor[] res = new Sensor[sensorCounter];

        for (int i = 0; i < res.length; i++) {
            res[i] = this.sensors[i];
        }
        
        return res;
    }

    /**
     * Override do método getSensor cuja função é obter um sensor consoante o ID
     *
     * @param id ID do sensor a procurar
     * @return null caso não encontre o sensor, o sensor caso encontre o sensor
     * com o ID dado como parâmetro
     */
    @Override
    public ISensor getSensor(String id) {
        int index = findAndReturnSensor(id);

        if (index >= 0) {
            Sensor res = this.sensors[index];
            return res;
        }
        
        return null;
    }

    /**
     * Método findSensor cujo objetivo é procurar um sensor
     *
     * @param id ID do sensor a procurar
     * @return true caso o sensor exista, false caso não o encontre
     */
    private boolean findSensor(String id) {
        if (id == null) {
            return false;
        }
        
        for (int i = 0; i < this.sensorCounter; i++) {
            if (this.sensors[i].getId().equals(id)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Método findAndReturnSensor cujo papel é procurar um sensor com base no ID
     * e retornar a sua posição caso exista
     *
     * @param id ID do sensor a procurar
     * @return -1 caso seja um ID nulo ou o sensor não exista, retorna a posição
     * do sensor caso o mesmo exista
     */
    private int findAndReturnSensor(String id) {
        if (id == null) {
            return -1;
        }

        for (int i = 0; i < this.sensorCounter; i++) {
            if (this.sensors[i].getId().equals(id)) {
                return i;
            }
        }
        
        return -1;
    }

    /**
     * Método getSensorCounter cuja função é obter o número de sensores
     * associados a uma estação metereológica
     *
     * @return Número de sensores associados a uma estação metereológica
     */
    public int getSensorCounter() {
        return sensorCounter;
    }
}