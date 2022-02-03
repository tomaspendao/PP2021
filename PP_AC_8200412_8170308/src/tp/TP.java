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

import edu.ma02.core.exceptions.CityException;
import edu.ma02.core.exceptions.MeasurementException;
import edu.ma02.core.exceptions.SensorException;
import edu.ma02.core.exceptions.StationException;
import java.io.IOException;
import java.time.LocalDateTime;
import tp.io.Importer;

public class TP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws StationException, SensorException, CityException, MeasurementException, IOException {

        /*CartesianCoordinates cartesian1 = new CartesianCoordinates(1, 1, 1);
        GeographicCoordinates geoggraphic1 = new GeographicCoordinates(1, 1);

        Sensor sensor1 = new Sensor("QA0NO20001", cartesian1, geoggraphic1);
        Sensor sensor2 = new Sensor("QA0NO20002", cartesian1, geoggraphic1);
        Sensor sensor3 = new Sensor("QA0NO20003", cartesian1, geoggraphic1);

        Station station1 = new Station("station1");

        station1.addSensor(sensor1);
        station1.addSensor(sensor2);
        System.out.println(station1.getSensorCounter());

        // valores de sensor
        /*System.out.println(sensor1.getId());
        System.out.println(sensor1.getType());
        System.out.println(sensor1.getParameter());
        System.out.println(sensor1.getParameter().getUnit());*/
        //sensor
        /*Sensor[] sensores1 = (Sensor[]) station1.getSensors();
        System.out.println(sensores1.length);
        for (int i = 0; i < sensores1.length; i++) {
            System.out.println("id: " + sensores1[i].getId());
            System.out.println("type: " + sensores1[i].getType());
            System.out.println("param: " + sensores1[i].getParameter());
            System.out.println("unit: " + sensores1[i].getParameter().getUnit());
        }*/
        City city1 = new City("0001", "Lixa");

        /*
        city1.addStation("station1");
        city1.addStation("station2");

        city1.addSensor("station1", "QA0NO20001", cartesian1, geoggraphic1);
        city1.addSensor("station1", "QA0NO20002", cartesian1, geoggraphic1);
        city1.addSensor("station1", "QA0NO20003", cartesian1, geoggraphic1);

        city1.addMeasurement("station1", "QA0NO20001", 42, Unit.UG_M3.toString(), LocalDateTime.MAX);
        city1.addMeasurement("station1", "QA0NO20001", 44, Unit.UG_M3.toString(), LocalDateTime.now());
        city1.addMeasurement("station1", "QA0NO20002", 42, Unit.UG_M3.toString(), LocalDateTime.MAX);
        //Measurement measurement1 = new Measurement(44, LocalDateTime.now());

        Sensor[] sensores2 = (Sensor[]) city1.getSensorsByStation("station1");
        System.out.println(sensores2.length);
        Measurement[] measurements1 = (Measurement[]) city1.getMeasurementsBySensor("QA0NO20001");
        System.out.println("date: " + measurements1[0].getTime());
        for (int i = 0; i < sensores2.length; i++) {
            System.out.println("id: " + sensores2[i].getId());
            System.out.println("date: " + measurements1[0].getTime());
            System.out.println("dateStandard: UTC");
            System.out.println("value: " + measurements1[0].getValue());
            System.out.println("unit: " + sensores2[i].getParameter().getUnit());
            System.out.println("address: " + station1.getName());
            System.out.println("coordinates");
            System.out.println("\tx: " + sensores2[i].getCartesianCoordinates().getX());
            System.out.println("\ty: " + sensores2[i].getCartesianCoordinates().getY());
            System.out.println("\tz: " + sensores2[i].getCartesianCoordinates().getZ());
            System.out.println("\tlat: " + sensores2[i].getGeographicCoordinates().getLatitude());
            System.out.println("\tlng: " + sensores2[i].getGeographicCoordinates().getLongitude());
        }
        ///////CCity Staticts      

        Statistics[] statistics = (Statistics[]) city1.getMeasurementsByStation(AggregationOperator.COUNT, Parameter.NO2, LocalDateTime.MAX, LocalDateTime.MAX);

        Statistics[] statistics2 = (Statistics[]) city1.getMeasurementsBySensor("station1", AggregationOperator.COUNT, Parameter.NO2, LocalDateTime.MAX, LocalDateTime.MAX);

        System.out.println("lenght: " + statistics2.length);

        for (int i = 0; i < statistics2.length; i++) {
            System.out.println(statistics2[i].getDescription());
            System.out.println(statistics2[i].getValue());
        }
        
        for (int i = 0; i < statistics.length; i++) {
            System.out.println(statistics[i].getDescription());
            System.out.println(statistics[i].getValue());
        }*/
        //IMPORTER
        /*Importer importer = new Importer();
                
        importer.importData(city1, "./files/result.json");
        
        System.out.println("numero de stations: " + city1.getStationCounter());
        
        int count=0;
        int countSensor = 0;
            Station[] stations = (Station[])city1.getStations();
            for (int j = 0; j < stations.length; j++) {
                Sensor[] sensores = (Sensor[])stations[j].getSensors();
                countSensor = countSensor + sensores.length;
                for (int i = 0; i < sensores.length; i++) {
                    count = count + sensores[i].getNumMeasurements();
                }
            }
        System.out.println("numero de sensores:" + countSensor);
        System.out.println("numero de measurements:" + count);
        //System.out.println(city1.getSensorsByStation("Calçada da Ajuda").length);
        
        
        
        Statistics[] statistics = (Statistics[])city1.getMeasurementsByStation( AggregationOperator.AVG, Parameter.NO2);
        //System.out.println(statistics.length);
        
        for (int i = 0; i < statistics.length; i++) {
            System.out.println(statistics[i].getDescription());
            System.out.println(statistics[i].getValue());
        }
        //EXPORTER
        city1.export();*/
        //city1.exporTableTest();
        // Importer importer = new Importer();
        //importer.importData(city1, "./files/test.json");
        //  city1.graphNumOfMeasurmentsByMonth();   
        Measurement measurement1 = new Measurement(0.00, LocalDateTime.MIN);
        Measurement measurement2 = new Measurement(-0.00, LocalDateTime.MIN);
        
        System.out.println(measurement1.equals(measurement2));
        
    }
}
