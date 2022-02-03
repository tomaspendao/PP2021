/*
* Nome: Daniel da Silva Pinto
* Número: 8200412
* Turma: LSIRC1T1
*
* Nome: Tomás Prior Pendão
* Número: 8170308
* Turma: LSIRC1T1
 */
package tp.io;

import edu.ma02.core.enumerations.Parameter;
import edu.ma02.core.enumerations.Unit;
import edu.ma02.core.exceptions.CityException;
import edu.ma02.core.exceptions.MeasurementException;
import edu.ma02.core.exceptions.SensorException;
import edu.ma02.core.exceptions.StationException;
import edu.ma02.core.interfaces.ICity;
import edu.ma02.io.interfaces.IImporter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tp.CartesianCoordinates;
import tp.GeographicCoordinates;
import tp.Sensor;
import tp.Station;

public class Importer implements IImporter {

    /**
     * Override do método importData cuja função é importar data a partir dum
     * dado ficheiro
     *
     * @param city Cidade para onde a informação vai ser importada
     * @param file Ficheiro com a informação a importar
     * @return null
     * @throws FileNotFoundException Execeção lançada caso o ficheiro não seja
     * encontrado
     * @throws IOException
     * @throws CityException Exeção lançada caso a cidade seja nula
     */
    @Override
    public IOStatistics importData(ICity city, String file) throws FileNotFoundException, IOException, CityException {

        IOStatistics statistics = new tp.io.IOStatistics();

        try {
            if (city == null) {
                throw new CityException("A cidade é nula");
            }

            FileReader reader = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            JSONArray sensorDataArray = (JSONArray) obj;

            for (int i = 0; i < sensorDataArray.size(); i++) {
                //um objecto para uma medição
                JSONObject measurement = (JSONObject) sensorDataArray.get(i);

                //obter o objeto das coordenadas
                JSONObject coordinates = (JSONObject) measurement.get("coordinates");

                //copiar as coordendas
                if (coordinates == null) {
                    continue;
                }

                
                
                String avg = (String) measurement.get("avg");
                
                if(avg==null){
                    continue;
                }
                
                
                
                
                CartesianCoordinates cartesianCoordinates = new CartesianCoordinates(
                        Double.parseDouble(String.valueOf(coordinates.get("x"))),
                        Double.parseDouble(String.valueOf(coordinates.get("y"))),
                        Double.parseDouble(String.valueOf(coordinates.get("z"))));

                GeographicCoordinates geographicCoordinates = new GeographicCoordinates(
                        (double) coordinates.get("lat"), (double) coordinates.get("lng"));

                //adiconar uma station, se existir não adiciona//adicionar station a city
                boolean stationBoolean = city.addStation((String) measurement.get("address"));
                statistics.incStationRead(stationBoolean);

                //obter o valor
                double value = Double.parseDouble(String.valueOf(measurement.get("value")));

                //obter a unidade
                Station station = (Station) city.getStation((String) measurement.get("address"));//obter estação
                
                Sensor sensor = new Sensor((String) measurement.get("id"), cartesianCoordinates,
                        geographicCoordinates);//obter o sensor
                
                
                Parameter parameter = sensor.getParameter();//obter o parametro

                if (parameter == null) {
                    continue;
                }

                Unit unit = parameter.getUnit();//obter a unidade
                String unitString = Unit.getUnitString(unit);//passar a unidade para string              

                //obter a date e formatar a date
                String dateString = (String) measurement.get("date");
                LocalDateTime date = this.getDateFormated(dateString);

                //adicionar sensor a city
                boolean sensorBoolean = city.addSensor(station.getName(), sensor.getId(), cartesianCoordinates,
                        geographicCoordinates);
                statistics.incSensorsRead(sensorBoolean);

                //adicionar a medição a city
                boolean measurementBoolean = city.addMeasurement(station.getName(), sensor.getId(), value, unitString, date);
                statistics.incMeasurementsRead(measurementBoolean);
            }

        } catch (FileNotFoundException ex) {
            statistics.addException(ex.toString());

            return null;
        } catch (CityException | ParseException | StationException | SensorException | MeasurementException
                | IOException ex) {
            statistics.addException(ex.toString());

            return null;
        }

        return statistics;
    }

    /**
     * Método getDateFormated cuja finalidade é formatar uma dada data para um
     * formato mais percetível
     *
     * @param dateString Data a formatar
     * @return Data formatada
     */
    private LocalDateTime getDateFormated(String dateString) {
        int year = Integer.parseInt(dateString.substring(0, 4));
        int month = Integer.parseInt(dateString.substring(4, 6));
        int day = Integer.parseInt(dateString.substring(6, 8));
        int hour = Integer.parseInt(dateString.substring(8, 10));
        int minute = Integer.parseInt(dateString.substring(10, 12));
        
        return LocalDateTime.of(year, Month.of(month), day, hour, minute);
    }
}