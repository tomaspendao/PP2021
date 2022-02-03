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

import edu.ma02.core.enumerations.AggregationOperator;
import edu.ma02.core.enumerations.Parameter;
import edu.ma02.core.enumerations.Unit;
import edu.ma02.core.exceptions.CityException;
import edu.ma02.core.exceptions.MeasurementException;
import edu.ma02.core.exceptions.SensorException;
import edu.ma02.core.exceptions.StationException;
import edu.ma02.core.interfaces.ICartesianCoordinates;
import edu.ma02.core.interfaces.ICity;
import edu.ma02.core.interfaces.ICityStatistics;
import edu.ma02.core.interfaces.IGeographicCoordinates;
import edu.ma02.core.interfaces.IMeasurement;
import edu.ma02.core.interfaces.ISensor;
import edu.ma02.core.interfaces.IStation;
import edu.ma02.core.interfaces.IStatistics;
import edu.ma02.io.interfaces.IExporter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class City implements ICity, ICityStatistics, IExporter {

    /**
     * ID duma cidade
     */
    private final String id;

    /**
     * Nome duma cidade
     */
    private final String name;

    /**
     * Conjunto das estações meteorológicas duma cidade
     */
    private Station[] stations;

    /**
     * Número de estações meteorológicas duma cidade
     */
    private int stationCounter;

    /**
     * FileWriter para fazer o export dos dados para um ficheiro JSON
     */
    private static FileWriter file;

    /**
     * Método construtor duma instância de cidade inicializando o ID, nome,
     * número de estações meteorológicas a 0 (zero) e inicializa o array Station
     * com mais uma posição do que o número de estações meteorológicas
     * existentes
     *
     * @param id ID da cidade
     * @param name Nome da cidade
     */
    public City(String id, String name) {
        this.id = id;
        this.name = name;

        this.stationCounter = 0;
        this.stations = new Station[stationCounter + 1];
    }

    /**
     * Override do método addStation cuja função é adicionar uma nova estação
     * meteorológica caso esta não seja nula e seja única. Aloca espaço caso o
     * array Station esteja cheio
     *
     * @param name Nome da estação meteorológicas a adicionar
     * @return false caso não consiga adicionar
     * @throws CityException Exceção causadora da não adição da estação
     * meteorológicas
     */
    @Override
    public boolean addStation(String name) throws CityException {
        try {
            if (name == null) {
                throw new CityException("O parâmetro nome é nulo");
            }
        } catch (CityException ex) {
            return false;
        }
        if (findStation(name) == true) {
            return false;
        }
        if (this.stationCounter == this.stations.length) {
            Station[] temp = new Station[stationCounter + 10];
            for (int i = 0; i < this.stationCounter; i++) {
                temp[i] = this.stations[i];
            }
            this.stations = temp;
        }
        this.stations[this.stationCounter] = new Station(name);
        this.stationCounter++;
        return true;
    }

    /**
     * Override do método addSensor cuja função é adicionar um sensor a uma
     * estação meteorológica existente e se o sensor tem um ID válido
     *
     * @param stationName Nome da estação meteorológica que vai receber o sensor
     * @param sensorId ID do sensor a adicionar à estação meteorológica
     * @param cartesianCoordinates Coordenadas cartesianas do sensor
     * @param geographicCoordenates Coordenadas geográficas do sensor
     * @return false caso não consiga adicionar o sensor a uma estação
     * meteorológica, true caso seja adicionado com sucesso
     * @throws CityException exceção retornada caso o parametro stationName seja
     * null ou a estação não exista na cidade
     * @throws StationException exceção retornada caso o parametro sensorId seja
     * null ou o sensor já exista na estação
     * @throws SensorException id do sensor é inválido i.e. diferente de 10
     * caracters
     */
    @Override
    public boolean addSensor(String stationName, String sensorId, ICartesianCoordinates cartesianCoordinates,
            IGeographicCoordinates geographicCoordenates) throws CityException, StationException, SensorException {
        try {
            if (stationName == null || this.findStation(stationName) == false) {
                throw new CityException("A estação metereológica não existe ou o parâmetro stationName é nulo");
            }

        } catch (CityException ex) {
            return false;
        }

        int index = this.findAndReturnStation(stationName);

        try {
            if (sensorId.length() != 10) {
                throw new SensorException("sensorId é inválido");
            }
            if (this.stations[index].getSensor(sensorId) != null || sensorId.length() != 10) {
                throw new StationException("O sensor já existe ou o sensorId é inválido (ex: ter um número de "
                        + "caracteres diferente de 10");
            }
        } catch (SensorException | StationException ex) {
            return false;
        }

        Sensor sensor = new Sensor(sensorId, (CartesianCoordinates) cartesianCoordinates,
                (GeographicCoordinates) geographicCoordenates);

        this.stations[index].addSensor(sensor);
        return true;
    }

    /**
     * Override do método addMeasurement cuja funcionalidade é adicionar uma
     * medição feita por um sensor caso o sensor seja encontrado, não nulo, se a
     * medida for compatível com o tipo do sensor e se for um valor válido
     *
     * @param stationName Nome da estação onde está o sensor
     * @param sensorId ID do sensor que realizou a medição
     * @param value Valor da medição
     * @param unit Unidade da medição
     * @param date Data da medição
     * @return false se a medição não foi adicionada, true caso tenha sido
     * adicionada
     * @throws CityException exceção retornada caso o parametro stationName seja
     * null ou a estação não exista na cidade
     * @throws StationException exceção retornada caso o sensor não exista na
     * cidade
     * @throws SensorException exceção retornada caso a unidade fornecida não
     * seja compativel com a unidade pre definida para o sensor
     * @throws MeasurementException exceção retornada caso o parametro value
     * seja -99 ou a date seja null
     */
    @Override
    public boolean addMeasurement(String stationName, String sensorId, double value, String unit, LocalDateTime date)
            throws CityException, StationException, SensorException, MeasurementException {
        try {
            if (stationName == null || this.findStation(stationName) == false) {
                throw new CityException("A estação metereológica não existe ou o parâmetro stationName é nulo");
            }
        } catch (CityException ex) {
            return false;
        }

        int index = this.findAndReturnStation(stationName);

        try {
            if (this.stations[index].getSensor(sensorId) == null) {
                throw new StationException("O sensor não existe");
            }

            Sensor sensor = (Sensor) this.stations[index].getSensor(sensorId);

            if (!(Unit.getUnitString(sensor.getParameter().getUnit()).equals(unit))) {
                throw new SensorException("A unidade não é compatível com a unidade pré-definida para o sensor");
            }

            if (value == -99 || date == null) {
                throw new MeasurementException("O valor é -99 ou algum parâmetro é inválido");
            }
        } catch (StationException | SensorException | MeasurementException ex) {
            return false;
        }

        return this.stations[index].getSensor(sensorId).addMeasurement(value, date, unit);
    }

    /**
     * Método findStation cuja função é procurar uma estação metereológica
     *
     * @param name Nome da estação metereológica a procurar
     * @return true caso encontre a estação metereológica, false caso não
     * encontre
     */
    private boolean findStation(String name) {
        for (int i = 0; i < this.stationCounter; i++) {
            if (this.stations[i].getName().equals(name) == true) {
                return true;
            }
        }

        return false;
    }

    /**
     * Método findAndReturnStation cuja funcionalidade é procurar e retornar a
     * estação metereológica
     *
     * @param name Nome da estação metereológica a procurar
     * @return A posição da estação metereolófica caso a encontre ou -1 (menos
     * um) caso não encontre a estação
     */
    private int findAndReturnStation(String name) {
        for (int i = 0; i < this.stationCounter; i++) {
            if (this.stations[i].getName().equals(name) == true) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Override do método getSensorsByStation cujo papel é encontrar os sensores
     * associados a uma estação metereológica
     *
     * @param stationName Nome da estação metereológica
     * @return conjunto dos sensores associados a uma estação metereológica
     */
    @Override
    public ISensor[] getSensorsByStation(String stationName) {
        Station temp = (Station) this.getStation(stationName);
        Sensor[] res = (Sensor[]) temp.getSensors();

        return res;
    }

    /**
     * Override do método getMeasurementsBySensor cuja finalidade é obter o
     * conjunto das medições dum determinado sensor caso o ID passado no
     * parâmetro não seja nulo
     *
     * @param sensorId ID do sensor
     * @return null caso não encontre o sensor, o conjunto das medições caso
     * encontre o sensor
     */
    @Override
    public IMeasurement[] getMeasurementsBySensor(String sensorId) {
        for (int i = 0; i < this.stationCounter; i++) {
            if (this.stations[i].getSensor(sensorId) != null) {
                Sensor resSensor = (Sensor) this.stations[i].getSensor(sensorId);
                Measurement[] res = (Measurement[]) resSensor.getMeasurements();

                return res;
            }
        }

        return null;
    }

    /**
     * Getter do número de estações metereológicas duma cidade
     *
     * @return Número de estações metereológicas numa cidade
     */
    public int getStationCounter() {
        return stationCounter;
    }

    /**
     * Override do getter do ID duma cidade
     *
     * @return ID da cidade
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Override do getter do Name duma cidade
     *
     * @return Name da cidade
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Override do getter do conjunto de estações da cidade
     *
     * @return conjunto das estações da cidade
     */
    @Override
    public IStation[] getStations() {
        Station[] res = new Station[stationCounter];

        for (int i = 0; i < res.length; i++) {
            res[i] = this.stations[i];
        }

        return res;
    }

    /**
     * Override do getter duma estação metereológica
     *
     * @param name Nome da estação metereológica a procurar
     * @return null caso não exista a estação metereológica, caso exista retorna
     * a estação
     */
    @Override
    public IStation getStation(String name) {
        int index = findAndReturnStation(name);

        if (index >= 0) {
            Station res = this.stations[index];

            return res;
        }

        return null;
    }

    /**
     * Override do método getMeasurementsByStation que recebe um operador, um
     * parâmetro e duas datas que retorna um conjunto de estatísticas com base
     * numa medição e num intervalo de tempo
     *
     * @param operator Operador
     * @param parameter Parâmetro da medição
     * @param start Data inicial do intervalo de tempo
     * @param end Data final do intervalo de tempo
     * @return Conjunto de medições com base no operador, parâmetro e intervalo
     * de tempo
     */
    @Override
    public IStatistics[] getMeasurementsByStation(AggregationOperator operator, Parameter parameter,
            LocalDateTime start, LocalDateTime end) {
        Station[] stations = (Station[]) this.getStations();
        Statistics[] stats = new Statistics[this.stationCounter];
        int statsCount = 0;

        if (stations != null) {
            for (int i = 0; i < this.getStationCounter(); i++) {
                double[] values = getValues(stations[i].getName(), parameter, start, end);

                if (parameter != null && operator != null && start != null && end != null) {
                    String description = "a estação metereológica: " + stations[i].getName() + " para o parâmetro:"
                            + parameter.name() + ", " + Parameter.getParameterName(parameter) + ", entre " + start + " e "
                            + end + ": ";

                    if (values.length == 0) {
                        stats[statsCount++] = new Statistics("Sem medições n" + description, -99);
                    } else {
                        stats[statsCount++] = calcWithOperator(values, operator, description);
                    }
                }
            }
        }

        return stats;
    }

    /**
     * Override do método getMeasurementsByStation cuja função é retornar um
     * conjunto de estatísticas com base no operador e num parâmetro
     *
     * @param operator Operador
     * @param parameter Parâmetro da medição
     * @return Conjunto de estatísticas com base num determinado operador e
     * parâmetro
     */
    @Override
    public IStatistics[] getMeasurementsByStation(AggregationOperator operator, Parameter parameter) {
        Station[] stations = (Station[]) this.getStations();
        Statistics[] stats = new Statistics[this.getStationCounter()];
        int statsCount = 0;

        for (int i = 0; i < this.getStationCounter(); i++) {
            double[] values = getValues(stations[i].getName(), parameter);
            String description = "a estação: " + stations[i].getName() + " para o parâmetro:" + parameter.name()
                    + ", " + Parameter.getParameterName(parameter) + ": ";

            if (values.length == 0) {
                stats[statsCount++] = new Statistics("Sem medições n" + description, -99);
            } else {
                stats[statsCount++] = calcWithOperator(values, operator, description);
            }
        }

        return stats;
    }

    /**
     * Override do método getMeasurementsBySensor cuja função é retornar um
     * conjunto de estatísticas com base nas medições duma estação e num
     * intervalo de tempo
     *
     * @param stationName Nome da estação
     * @param operator Operador
     * @param parameter Parâmetro da medição
     * @param start Data inicial do intervalo de tempo
     * @param end Data final do intervalo de tempo
     * @return Conjunto de estatísticas com base nas medições duma estação e num
     * intervalo de tempo
     */
    @Override
    public IStatistics[] getMeasurementsBySensor(String stationName, AggregationOperator operator, Parameter parameter,
            LocalDateTime start, LocalDateTime end) {
        Station station = (Station) this.getStation(stationName);

        if (station != null) {
            Sensor[] sensors = (Sensor[]) station.getSensors();
            Statistics[] stats = new Statistics[sensors.length];
            int statsCount = 0;

            for (int i = 0; i < station.getSensorCounter(); i++) {
                double[] values = getValuesBySensor(stationName, sensors[i].getId(), parameter, start, end);
                String description = "o sensor: " + sensors[i].getId() + " para o pârametro:" + parameter.name() + ", "
                        + Parameter.getParameterName(parameter) + ", entre " + start + " e " + end + ": ";

                if (values.length == 0) {
                    stats[statsCount++] = new Statistics("Sem medições n" + description, -99);
                } else {
                    stats[statsCount++] = calcWithOperator(values, operator, description);
                }
            }

            return stats;
        }

        return null;
    }

    /**
     * Override do método getMeasurementsBySensor cuja função é devolver um
     * conjunto de estatísticas com base no operador e parâmetro
     *
     * @param stationName Nome da estação metereológica
     * @param operator Operador
     * @param parameter Parâmetro da medição
     * @return Conjunto de estatísticas com base num operador e num parâmetro
     */
    @Override
    public IStatistics[] getMeasurementsBySensor(String stationName, AggregationOperator operator,
            Parameter parameter) {
        Station station = (Station) this.getStation(stationName);
        //System.out.println(stationName);

        if (station != null) {
            Sensor[] sensors = (Sensor[]) station.getSensors();
            Statistics[] stats = new Statistics[sensors.length];
            int statsCount = 0;

            for (int i = 0; i < station.getSensorCounter(); i++) {
                double[] values = getValuesBySensor(stationName, sensors[i].getId(), parameter);
                String description = "o sensor: " + sensors[i].getId() + " para o parâmetro:" + parameter.name() + ", "
                        + Parameter.getParameterName(parameter) + ": ";

                if (values.length == 0) {
                    stats[statsCount++] = new Statistics("Sem medições n" + description, -99);
                } else {
                    stats[statsCount++] = calcWithOperator(values, operator, description);
                }
            }

            return stats;
        }

        return null;
    }

    /**
     * Método calcWithOperator cuja finalidade é fazer o cálculo consoante o
     * operador pretendio e um conjunto de valores
     *
     * @param values Conjunto de valores a ser usados na operação
     * @param operator Operação a executar
     * @param description Descrição da estatística
     * @return Resultados das contas consoante o conjunto de valores e a
     * operação realizada
     */
    private Statistics calcWithOperator(double[] values, AggregationOperator operator, String description) {
        switch (operator) {
            case AVG:
                if (values.length > 0) {
                    double total = 0;

                    for (int j = 0; j < values.length; j++) {
                        total = total + values[j];
                    }

                    total = total / values.length;

                    return new Statistics("Valor da Medição média n" + description, total);
                }

                break;
            case COUNT:
                return new Statistics("Número de medições n" + description, values.length);
            case MAX:
                if (values.length > 0) {
                    double max = -99;

                    for (int j = 0; j < values.length; j++) {
                        if (values[j] > max) {
                            max = values[j];
                        }
                    }

                    return new Statistics("Valor da Medição máxima n" + description, max);
                }

                break;
            case MIN:
                if (values.length > 0) {
                    double min = values[0];

                    for (int j = 0; j < values.length; j++) {
                        if (values[j] < min) {
                            min = values[j];
                        }
                    }

                    return new Statistics("Valor da Medição mínima n" + description, min);
                }

                break;
            default:
                return new Statistics("Operador inválido para " + description, -99);
        }

        return new Statistics("Sem medições n" + description, -99);
    }

    /**
     * Método getValues cuja função é obter o conjunto dos valores de medições
     * com base no ID duma estação metereológica e dum parâmetro
     *
     * @param stationId ID da estação metereológica
     * @param parameter Parâmetro da medição
     * @return Conjunto dos valores das medições com um determinado parâmetro
     */
    private double[] getValues(String stationId, Parameter parameter) {
        double[] res = new double[countMeasurements(stationId, parameter)];
        int resCounter = 0;
        Station station = (Station) this.getStation(stationId);
        Sensor[] sensors = (Sensor[]) station.getSensors();

        for (int j = 0; j < station.getSensorCounter(); j++) {
            Parameter[] param = sensors[j].getType().getParameters();

            for (int k = 0; k < param.length; k++) {
                if (parameter.equals(param[k])) {
                    Measurement[] measurements = (Measurement[]) sensors[j].getMeasurements();

                    for (int h = 0; h < sensors[j].getNumMeasurements(); h++) {
                        res[resCounter] = measurements[h].getValue();

                        resCounter++;
                    }
                }
            }
        }

        return res;
    }

    /**
     * Método getValues cuja funcionalidade é obter o conjunto dos valores de
     * medições com base num ID duma estação metereológica, num parâmetro e num
     * intervalo de tempo
     *
     * @param stationId ID da estação metereológica
     * @param parameter Parâmetro da medição
     * @param start Data inicial do intervalo de tempo
     * @param end Data final do intervalo de tempo
     * @return Conjunto dos valores de medições com numa determinada estação
     * metereológica, com um determinado parâmetro e num determinado intervalo
     * de tempo
     */
    private double[] getValues(String stationId, Parameter parameter, LocalDateTime start, LocalDateTime end) {
        double[] res = new double[countMeasurements(stationId, parameter, start, end)];
        int resCounter = 0;
        Station station = (Station) this.getStation(stationId);
        Sensor[] sensors = (Sensor[]) station.getSensors();

        for (int j = 0; j < station.getSensorCounter(); j++) {
            Parameter[] param = sensors[j].getType().getParameters();

            for (int k = 0; k < param.length; k++) {
                if (parameter.equals(param[k])) {
                    Measurement[] measurements = (Measurement[]) sensors[j].getMeasurements();

                    for (int h = 0; h < sensors[j].getNumMeasurements(); h++) {
                        if (measurements[h].getTime().compareTo(start) >= 0
                                && measurements[h].getTime().compareTo(end) <= 0) {
                            res[resCounter] = measurements[h].getValue();

                            resCounter++;
                        }
                    }
                }
            }
        }

        return res;
    }

    /**
     * Método countMeasurements cuja função é contar as medições com um
     * determinado parâmetro numa estação metereológica
     *
     * @param stationId ID da estação metereológica
     * @param parameter Parâmetro da medição
     * @return Número de medições com um determinado parâmetro numa dada estação
     * metereológica
     */
    private int countMeasurements(String stationId, Parameter parameter) {
        int res = 0;
        Station station = (Station) this.getStation(stationId);
        Sensor[] sensors = (Sensor[]) station.getSensors();

        for (int j = 0; j < station.getSensorCounter(); j++) {
            Parameter[] param = sensors[j].getType().getParameters();

            for (int k = 0; k < param.length; k++) {
                if (parameter.equals(param[k])) {
                    for (int h = 0; h < sensors[j].getNumMeasurements(); h++) {
                        res++;
                    }
                }
            }
        }

        return res;
    }

    /**
     * Método countMeasurements cuja finalidade é obter o número de medições uma
     * determinada estação metereológica com um determinado parâmetro e num
     * intervalo de tempo
     *
     * @param stationId ID da estação metereológica
     * @param parameter Parâmetro da medição
     * @param start Data inicial do intervalo de tempo
     * @param end Data final do intervalo de tempo
     * @return Número de medições numa determinada estação metereológica com um
     * determinado parâmetro e num determinado intervalo de tempo
     */
    private int countMeasurements(String stationId, Parameter parameter, LocalDateTime start, LocalDateTime end) {
        int res = 0;
        Station station = (Station) this.getStation(stationId);
        Sensor[] sensors = (Sensor[]) station.getSensors();

        for (int j = 0; j < station.getSensorCounter(); j++) {
            Parameter[] param = sensors[j].getType().getParameters();

            for (int k = 0; k < param.length; k++) {
                if (parameter.equals(param[k])) {
                    Measurement[] measurements = (Measurement[]) sensors[j].getMeasurements();

                    for (int h = 0; h < sensors[j].getNumMeasurements(); h++) {
                        if (measurements[h].getTime().compareTo(start) >= 0
                                && measurements[h].getTime().compareTo(end) <= 0) {
                            res++;
                        }
                    }
                }
            }
        }

        return res;
    }

    /**
     * Método getValuesBySensor cujo objetivo é obter o conjunto dos valores dum
     * sensor recebendo um ID duma estação metereológica, o ID do sensor e o
     * parâmetro da medição
     *
     * @param stationId ID da estação metereológica
     * @param sensorId ID do sensor
     * @param parameter Parâmetro da medição
     * @return Conjunto dos valores das medições com um determinado parâmetro
     */
    private double[] getValuesBySensor(String stationId, String sensorId, Parameter parameter) {
        double[] res = new double[countMeasurementsBySensor(stationId, sensorId, parameter)];
        int resCounter = 0;
        Sensor sensor = this.getSensorByStation(stationId, sensorId);
        Parameter[] param = sensor.getType().getParameters();

        for (int k = 0; k < param.length; k++) {
            if (parameter.equals(param[k])) {
                Measurement[] measurements = (Measurement[]) sensor.getMeasurements();

                for (int h = 0; h < sensor.getNumMeasurements(); h++) {
                    res[resCounter] = measurements[h].getValue();

                    resCounter++;
                }
            }
        }

        return res;
    }

    /**
     * Método getValuesBySensor cujo papel é obter os valores das medições com
     * base num ID duma estação metereológica, num ID do sensor, num parâmetro e
     * num intervalo de tempo
     *
     * @param stationId ID da estação metereológica
     * @param sensorId ID do sensor
     * @param parameter Parâmetro das medições
     * @param start Data inicial do intervalo de tempo
     * @param end Data final do intervalo de tempo
     * @return Conjunto das medições efetuadas num sensor num determinado tempo
     * e com base num parâmetro
     */
    private double[] getValuesBySensor(String stationId, String sensorId, Parameter parameter, LocalDateTime start,
            LocalDateTime end) {
        double[] res = new double[countMeasurementsBySensor(stationId, sensorId, parameter, start, end)];
        int resCounter = 0;
        Sensor sensor = this.getSensorByStation(stationId, sensorId);
        Parameter[] param = sensor.getType().getParameters();

        for (int k = 0; k < param.length; k++) {
            if (parameter.equals(param[k])) {
                Measurement[] measurements = (Measurement[]) sensor.getMeasurements();

                for (int h = 0; h < sensor.getNumMeasurements(); h++) {
                    if (measurements[h].getTime().compareTo(start) >= 0
                            && measurements[h].getTime().compareTo(end) <= 0) {
                        res[resCounter] = measurements[h].getValue();

                        resCounter++;
                    }
                }
            }
        }

        return res;
    }

    /**
     * Método countMeasurementsBySensor cuja função é obter o número de medições
     * que um dado sensor efetuou com um determinado parâmetro
     *
     * @param stationId ID da estação metereológica
     * @param sensorId ID do sensor
     * @param parameter Parâmetro da medição
     * @return Número de medições efetuadas por um sensor com um determinado
     * parâmetro
     */
    private int countMeasurementsBySensor(String stationId, String sensorId, Parameter parameter) {
        int res = 0;
        Sensor sensor = this.getSensorByStation(stationId, sensorId);

        if (sensor == null) {
            return 0;
        }

        Parameter[] param = sensor.getType().getParameters();

        for (int k = 0; k < param.length; k++) {
            if (parameter.equals(param[k])) {
                Measurement[] measurements = (Measurement[]) sensor.getMeasurements();

                for (int h = 0; h < sensor.getNumMeasurements(); h++) {
                    res++;
                }
            }
        }

        return res;
    }

    /**
     * Método countMeasurementsBySensor cuja finalidade é obter o número de
     * medições efetuadas num determinado sensor com um dado parâmetro num certo
     * intervalo de tempo
     *
     * @param stationId ID da estação metereológica
     * @param sensorId ID do sensor
     * @param parameter Parâmetro da medição
     * @param start Data inicial do intervalo de tempo
     * @param end Data final do intervalo de tempo
     * @return Número de medições num determinado sensor com um dado parâmetro
     * num certo intervalo de tempo
     */
    private int countMeasurementsBySensor(String stationId, String sensorId, Parameter parameter, LocalDateTime start,
            LocalDateTime end) {
        int res = 0;
        Sensor sensor = this.getSensorByStation(stationId, sensorId);

        if (sensor == null) {
            return 0;
        }

        Parameter[] param = sensor.getType().getParameters();

        for (int k = 0; k < param.length; k++) {
            if (parameter.equals(param[k])) {
                Measurement[] measurements = (Measurement[]) sensor.getMeasurements();

                for (int h = 0; h < sensor.getNumMeasurements(); h++) {
                    if (measurements[h].getTime().compareTo(start) >= 0
                            && measurements[h].getTime().compareTo(end) <= 0) {
                        res++;
                    }
                }
            }
        }

        return res;
    }

    /**
     * Método getSensorByStation cujo objetivo é obter um sensor associado com
     * uma dada estação metereológica
     *
     * @param stationId ID da estação metereológica
     * @param sensorId ID do sensor
     * @return null caso não haja um sensor associadoa uma dada estação
     * metereológica, o sensor caso este seja encontrado
     */
    private Sensor getSensorByStation(String stationId, String sensorId) {
        Sensor[] sensors = (Sensor[]) this.getSensorsByStation(stationId);

        for (int i = 0; i < sensors.length; i++) {
            if (sensors[i].getId().equals(sensorId)) {
                return sensors[i];
            }
        }

        return null;
    }

    /**
     * Override do método export cujo papel é exportar para um ficheiro os dados
     * duma cidade
     *
     * @return Erro caso os dados não tenham sido exportados, se bem sucesido
     * retorna o caminho até ao ficheiro exportado
     */
    @Override
    public String export() {
        JSONArray objArray = new JSONArray();

        for (int i = 0; i < this.getStationCounter(); i++) {
            Sensor[] sensores = (Sensor[]) this.stations[i].getSensors();

            for (int j = 0; j < sensores.length; j++) {
                Measurement[] measurements = (Measurement[]) sensores[j].getMeasurements();

                for (int k = 0; k < measurements.length; k++) {
                    JSONObject measurementObj = new JSONObject();

                    measurementObj.put("id", sensores[j].getId());
                    measurementObj.put("date", this.getDateToString(measurements[k].getTime()));
                    measurementObj.put("dateStandard", "UTC");
                    measurementObj.put("value", measurements[k].getValue());
                    measurementObj.put("unit", Unit.getUnitString(sensores[j].getParameter().getUnit()));
                    measurementObj.put("address", stations[i].getName());

                    JSONObject coordinatesObj = new JSONObject();

                    coordinatesObj.put("x", sensores[j].getCartesianCoordinates().getX());
                    coordinatesObj.put("y", sensores[j].getCartesianCoordinates().getY());
                    coordinatesObj.put("z", sensores[j].getCartesianCoordinates().getZ());
                    coordinatesObj.put("lat", sensores[j].getGeographicCoordinates().getLatitude());
                    coordinatesObj.put("lng", sensores[j].getGeographicCoordinates().getLongitude());

                    measurementObj.put("coordinates", coordinatesObj);

                    objArray.add(measurementObj);
                }
            }
        }

        String filePath = "./files/result" + LocalDateTime.now().hashCode() + ".json";

        writeToFile(filePath, objArray);

        return filePath;
    }

    /**
     * Método writeToFile cuja função é escrever para um ficheiro
     *
     * @param filePath Caminho do ficheiro a escrever
     * @param objArray
     */
    private void writeToFile(String filePath, JSONArray objArray) {
        try {
            file = new FileWriter(filePath);
            file.write(objArray.toJSONString().replace("\\/", "/"));

            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + objArray);
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
    }

    /**
     * Método graphNumOfMeasurmentsByMonth cuja função é criar um gráfico de
     * barras com o número de medições para cada mês
     *
     * @return retorna uma string do objecto JSON
     */
    protected String graphNumOfMeasurmentsByMonth() {
        JSONObject obj = new JSONObject();
        JSONObject dataObj = new JSONObject();
        JSONArray labelsArray = new JSONArray();
        String[] months = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
            "Outubro", "Novembro", "Dezembro"};

        for (int i = 0; i < 12; i++) {
            labelsArray.add(months[i]);
        }

        JSONArray datasetArray = new JSONArray();
        JSONObject measurementsObj = new JSONObject();

        measurementsObj.put("label", "Numero de Medições");

        JSONArray dataArray = new JSONArray();

        for (int i = 0; i < 12; i++) {
            dataArray.add(this.totalMeasurementsInAMonth(i + 1));
        }

        measurementsObj.put("data", dataArray);

        datasetArray.add(measurementsObj);

        dataObj.put("datasets", datasetArray);
        dataObj.put("labels", labelsArray);

        obj.put("data", dataObj);
        obj.put("type", "bar");

        return obj.toJSONString();
    }

    /**
     * Método graphNumOfMeasurementsByUnit cuja finalidade é criar um gráfico
     * com o número de medições para cada unidade
     *
     * @return retorna uma string do objecto JSON
     */
    protected String graphNumOfMeasurementsByUnit() {
        JSONObject obj = new JSONObject();
        JSONArray labelsArray = new JSONArray();
        String[] units = {"μg/m3", "mg/m3", "dB(A)", "mbar", "%", "ºC", "km/h", "º", "mm",
            "W/m2", "UV indice", "Tráfego Médio Horário por via:veículos"};
        String[] labels = {"UG_M3", "MG_M3", "DB", "MBAR", "PERCENTAGE", "DEGREE_CELSIUS", "KM_H", "DEGREE", "MM",
            "W_M2", "UV", "TMH"};

        for (int i = 0; i < labels.length; i++) {
            labelsArray.add(labels[i]);
        }

        JSONObject dataObj = new JSONObject();

        dataObj.put("labels", labelsArray);

        JSONArray dataArray = new JSONArray();

        JSONArray datasetsArray = new JSONArray();

        for (int i = 0; i < units.length; i++) {
            dataArray.add(this.totalMeasurementsWithAUnit(units[i]));
        }

        JSONObject measurementsObj = new JSONObject();

        measurementsObj.put("data", dataArray);
        measurementsObj.put("label", "Número de Medições por Unidade");
        measurementsObj.put("hoverOffset", 10);

        datasetsArray.add(measurementsObj);

        dataObj.put("datasets", datasetsArray);

        obj.put("type", "pie");
        obj.put("data", dataObj);

        System.out.println(obj.toJSONString());

        return obj.toJSONString();
    }

    /**
     * Método graphNumOfMeasurementsByParameter cujo objetivo é criar um gráfico
     * representativo do número de medições para cada parâmetro
     *
     * @return retorna uma string do objecto JSON
     */
    protected String graphNumOfMeasurementsByParameter() {
        JSONObject obj = new JSONObject();
        JSONArray labelsArray = new JSONArray();
        String[] parameters = {"NO2", "O3", "PM2_5", "PM10", "SO2", "C6H6", "CO", "LAEQ", "PA", "TEMP", "RU", "VD",
            "VI", "HM", "PC", "RG"};

        for (int i = 0; i < parameters.length; i++) {
            labelsArray.add(parameters[i]);
        }

        JSONObject dataObj = new JSONObject();

        dataObj.put("labels", labelsArray);

        JSONArray dataArray = new JSONArray();
        JSONArray datasetsArray = new JSONArray();

        for (int i = 0; i < parameters.length; i++) {
            dataArray.add(this.totalMeasurementsWithAParameter(parameters[i]));
        }

        JSONObject parametersObj = new JSONObject();

        parametersObj.put("data", dataArray);
        parametersObj.put("label", "Número de Medições por Parâmetro");
        parametersObj.put("hoverOffset", 10);

        datasetsArray.add(parametersObj);

        dataObj.put("datasets", datasetsArray);

        obj.put("type", "doughnut");
        obj.put("data", dataObj);

        System.out.println(obj.toJSONString());

        return obj.toJSONString();
    }

    /**
     * Método graphNumOfTypeSensors cujo papel é criar um gráfico com o número
     * de cada tipo de sensor
     *
     * @return retorna uma string do objecto JSON
     */
    protected String graphNumOfTypeSensors() {
        JSONObject obj = new JSONObject();
        JSONArray labelsArray = new JSONArray();
        String[] sensorType = {"Qualidade do Ar", "Ruído", "Estado do Tempo"};
        String[] sensorCode = {"AIR", "NOISE", "WEATHER"};

        for (int i = 0; i < sensorType.length; i++) {
            labelsArray.add(sensorType[i]);
        }

        JSONObject dataObj = new JSONObject();

        dataObj.put("labels", labelsArray);

        JSONArray dataArray = new JSONArray();
        JSONArray datasetsArray = new JSONArray();

        for (int i = 0; i < sensorType.length; i++) {
            dataArray.add(this.totalSensorsWithAType(sensorCode[i]));
        }

        JSONObject parametersObj = new JSONObject();

        parametersObj.put("data", dataArray);
        parametersObj.put("label", "Número de sensores para cada tipo");
        parametersObj.put("hoverOffset", 10);

        datasetsArray.add(parametersObj);

        dataObj.put("datasets", datasetsArray);

        obj.put("type", "radar");
        obj.put("data", dataObj);

        System.out.println(obj.toJSONString());

        return obj.toJSONString();
    }

    /**
     * Método getDateToString cuja função é através duma certa data formatar a
     * data para um formato adequado
     *
     * @param date Data a formatar
     * @return Data formatada
     */
    private String getDateToString(LocalDateTime date) {
        String res = "";
        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getMonthValue());

        if (month.length() == 1) {
            String zero = "0";
            zero = zero + month;
            month = zero;
        }

        String day = String.valueOf(date.getDayOfMonth());

        if (day.length() == 1) {
            String zero = "0";
            zero = zero + day;
            day = zero;
        }

        String hour = String.valueOf(date.getHour());

        if (hour.length() == 1) {
            String zero = "0";
            zero = zero + hour;
            hour = zero;
        }

        String minut = String.valueOf(date.getMinute());

        if (minut.length() == 1) {
            String zero = "0";
            zero = zero + minut;
            minut = zero;
        }

        res = res + year + month + day + hour + minut;

        return res;
    }

    /**
     * Método numTotalMeasurements com a finalidade de obter o número total de
     * medições
     *
     * @return Número total de medições
     */
    private int numTotalMeasurements() {
        int count = 0;
        Station[] station = (Station[]) this.getStations();

        for (int i = 0; i < station.length; i++) {
            Sensor[] sensores = (Sensor[]) station[i].getSensors();

            for (int j = 0; j < sensores.length; j++) {
                Measurement[] measurements = (Measurement[]) sensores[j].getMeasurements();

                for (int k = 0; k < measurements.length; k++) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Método totalMeasurements cujo objetivo é obter um conjunto de todas as
     * medições
     *
     * @return Conjunto de todas as medições
     */
    private Measurement[] totalMeasurements() {
        Measurement[] allMeasurements = new Measurement[this.numTotalMeasurements()];
        int pos = 0;
        Station[] station = (Station[]) this.getStations();

        for (int i = 0; i < station.length; i++) {
            Sensor[] sensores = (Sensor[]) station[i].getSensors();

            for (int j = 0; j < sensores.length; j++) {
                Measurement[] measurements = (Measurement[]) sensores[j].getMeasurements();

                for (int k = 0; k < measurements.length; k++) {
                    allMeasurements[pos] = measurements[k];

                    pos++;
                }
            }
        }

        return allMeasurements;
    }

    /**
     * Método totalMeasurementsInAMonth cujo papel é obter o número total de
     * medições efetuadas num determinado mês
     *
     * @param month Mês para o qual se quer obter o número de medições
     * @return O número de medições para um determinado mês
     */
    private int totalMeasurementsInAMonth(int month) {
        int res = 0;
        Measurement[] measurements = totalMeasurements();

        for (int i = 0; i < measurements.length; i++) {
            if (measurements[i].getTime().getMonthValue() == month) {
                res++;
            }
        }

        return res;
    }

    /**
     * Método totalMeasurementsWithAUnit cuja função é obter o número de
     * medições que usam uma dada unidade
     *
     * @param unit Unidade para o qual vão ser contadas o número de medições
     * @return Número de medições que usam uma determinada unidade
     */
    private int totalMeasurementsWithAUnit(String unit) {
        int res = 0;
        Sensor[] sensors = totalSensors();

        for (int i = 0; i < sensors.length; i++) {
            if (Unit.getUnitString(sensors[i].getParameter().getUnit()).equals(unit)) {
                res = res + sensors[i].getMeasurements().length;
            }
        }

        return res;
    }

    /**
     * Método totalMeasurementsWithAParameter cuja finalidade é obter o número
     * de medições que um dado parâmetro tem
     *
     * @param parameter Parâmetro para o qual as medições vão ser contadas
     * @return Número de medições que um dado parâmetro tem
     */
    private int totalMeasurementsWithAParameter(String parameter) {
        int res = 0;
        Sensor[] sensors = totalSensors();

        for (int i = 0; i < sensors.length; i++) {
            if (sensors[i].getParameter().toString().equals(parameter)) {
                res = res + sensors[i].getMeasurements().length;
            }
        }

        return res;
    }

    /**
     * Método totalSensorsWithAType cujo objetivo é obter o número de sensores
     * com um dado tipo
     *
     * @param sensorType Tipo do sensor para o qual vai ser contado o número de
     * sensores
     * @return Número de sensores com um dado tipo
     */
    private int totalSensorsWithAType(String sensorType) {
        int res = 0;
        Sensor[] sensors = totalSensors();

        for (int i = 0; i < sensors.length; i++) {
            if (sensors[i].getType().toString().equals(sensorType)) {
                res = res + sensors[i].getMeasurements().length;
            }
        }

        return res;
    }

    /**
     * Método numTotalSensors cujo papel é obter o número total de sensores
     *
     * @return Número total de sensores
     */
    private int numTotalSensors() {
        int res = 0;

        for (int i = 0; i < this.stationCounter; i++) {
            res = res + this.stations[i].getSensorCounter();
        }

        return res;
    }

    /**
     * Método totalSensors cuja função é obter um conjunto de todos os sensores
     *
     * @return Conjunto de todos os sensores
     */
    private Sensor[] totalSensors() {
        Sensor[] allSensors = new Sensor[this.numTotalSensors()];
        int pos = 0;

        for (int i = 0; i < this.stationCounter; i++) {
            Sensor[] tempSensores = (Sensor[]) this.stations[i].getSensors();

            for (int j = 0; j < tempSensores.length; j++) {
                allSensors[pos] = tempSensores[j];

                pos++;
            }
        }

        return allSensors;
    }
}
