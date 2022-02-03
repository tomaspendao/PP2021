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
import edu.ma02.core.exceptions.CityException;
import edu.ma02.core.exceptions.MeasurementException;
import edu.ma02.core.exceptions.SensorException;
import edu.ma02.core.exceptions.StationException;
import edu.ma02.dashboards.Dashboard;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Scanner;
import tp.io.IOStatistics;
import tp.io.Importer;

public class Menu {

    /**
     * Método menu cuja função é apresentar ao utilizador um menu com as opções
     * disponíveis
     *
     * @param in Escolha da opção
     * @param name Nome da cidade
     * @return Caso seja uma opção inválida apresenta novamente o menu, caso
     * contrário retorna o número da opção escolhida
     */
    private static int menu(Scanner in, String name) {
        System.out.println("\t" + name.toUpperCase() + "\n");

        System.out.println("1 - Adicionar Uma Estação\n2 - Adicionar Um Sensor\n3 - Adicionar Uma Medição\n4 - "
                + "Importar JSON\n5 - Exportar JSON\n6 - Listar Estações\n7 - Listar Sensores\n8 - Listar Medições\n9 "
                + "- Criar Gráficos\n10 - Estatísticas\n0 - Sair");
        System.out.print("Opção: ");

        int choice = in.nextInt();

        System.out.println();

        if (choice >= 0 && choice <= 10) {
            return choice;
        } else {
            System.out.println("Opção inválida\n");

            return choice;
        }
    }

    public static void main(String[] args) throws CityException, StationException, SensorException,
            MeasurementException, IOException {
        Scanner in = new Scanner(System.in);
        Importer importer = new Importer();
        City city = starter();
        boolean exit = false;

        while (!exit) {
            switch (menu(in, city.getName())) {
                case 1:
                    System.out.println("--->Adiconar Estação<---\n");

                    addStation(city);

                    System.out.println();

                    break;
                case 2:
                    System.out.println("--->Adicionar Sensor<---\n");

                    addSensor(city);

                    System.out.println();

                    break;
                case 3:
                    System.out.println("--->Adicionar Medição<---\n");

                    addMeasurement(city);

                    System.out.println();

                    break;
                case 4:
                    System.out.println("-->Importar JSON<---\n");

                    Scanner file_path = new Scanner(System.in);

                    System.out.print("Insira o nome do ficheiro: ");

                    String path = file_path.next();

                    if (path.contains(".json")) {
                        path = "./files/" + path;
                    } else {
                        path = "./files/" + path + ".json";
                    }

                    IOStatistics stat = importer.importData(city, path);
                    showIOStatistics(stat);

                    System.out.println();

                    break;
                case 5:
                    System.out.println("--->Exportar<---\n");

                    city.export();

                    System.out.println();

                    break;
                case 6:
                    System.out.println("--->Listar Estações<---\n");

                    showStations(city);

                    System.out.println();

                    break;
                case 7:
                    System.out.println("--->Listar Sensores<---\n");

                    showSensors(city);

                    System.out.println();

                    break;
                case 8:
                    System.out.println("--->Listar Medições<---\n");

                    showMeasurements(city);

                    System.out.println();

                    break;
                case 9:
                    System.out.println("--->Criar Gráficos<---\n");

                    String[] jsons = getGraphs(city);
                    Dashboard.render(jsons);

                    System.out.println();

                    break;
                case 10:
                    System.out.println("--->Estatisticas<---\n");

                    optionsStatistics(in, city);

                    break;
                case 0:
                    exit = true;

                    break;
            }
        }
    }

    /**
     * Método addStation cuja finalidade é adicionar uma estação metereológica
     * caso esta opção seja escolhida
     *
     * @param city Cidade onde a estação metereológica será criada
     * @throws CityException Exceção lançada quando a cidade é inválida
     */
    private static void addStation(City city) throws CityException {
        Scanner name = new Scanner(System.in,"utf-8");

        System.out.print("Insira o nome da Estação: ");
        String sName = name.nextLine();

        city.addStation(sName);
    }

    /**
     * Método addSensor cujo objetivo é adicionar um sensor a uma dada cidade
     *
     * @param city Cidade que vai ter um sensor adicionado
     * @throws CityException Exeção lançada caso a cidade seja nula
     * @throws StationException Exeção lançada caso a estação metereológica seja
     * nula
     * @throws SensorException Exeção lançada caso o sensor seja nula
     */
    private static void addSensor(City city) throws CityException, StationException, SensorException {
        Scanner stationName = new Scanner(System.in);
        Scanner sensorName = new Scanner(System.in);
        Scanner x = new Scanner(System.in);
        Scanner y = new Scanner(System.in);
        Scanner z = new Scanner(System.in);
        Scanner lat = new Scanner(System.in);
        Scanner lng = new Scanner(System.in);

        System.out.print("Insira o nome da Estação: ");
        String station = stationName.next();
        System.out.print("Insira o ID do Sensor: ");
        String sensor = sensorName.next();
        System.out.print("Insira a coordenada X: ");
        double coo_x = x.nextDouble();
        System.out.print("Insira a coordenada Y: ");
        double coo_y = y.nextDouble();
        System.out.print("Insira a coordenada Z: ");
        double coo_z = z.nextDouble();

        System.out.print("Insira a Latitude: ");
        double lati = lat.nextDouble();
        System.out.print("Insira a Longitude: ");
        double logi = lng.nextDouble();

        CartesianCoordinates cartesianCoordinates = new CartesianCoordinates(coo_x, coo_y, coo_z);
        GeographicCoordinates geographicCoordenates = new GeographicCoordinates(lati, logi);

        city.addSensor(station, sensor, cartesianCoordinates, geographicCoordenates);
    }

    /**
     * Método addMeasurement cujo papel é adicionar uma medição a uma dada
     * cidade
     *
     * @param city Cidade onde vai ser criada uma medição
     * @throws CityException Exeção lançada caso a cidade seja nula
     * @throws StationException Exeção lançada caso a estação metereológica seja
     * nula
     * @throws SensorException Exeção lançada caso o sensor seja nulo
     * @throws MeasurementException Exeção lançada caso a medição seja nula
     */
    private static void addMeasurement(City city) throws CityException, StationException,
            SensorException, MeasurementException {
        Scanner name_station_measurement = new Scanner(System.in);
        Scanner name_sensor_measurement = new Scanner(System.in);
        Scanner valueStr = new Scanner(System.in);
        Scanner unitStr = new Scanner(System.in);
        Scanner dateStr = new Scanner(System.in);

        System.out.print("Insira o nome da Estação: ");
        String station = name_station_measurement.next();

        System.out.print("Insira o ID do Sensor: ");
        String sensor = name_sensor_measurement.next();

        System.out.print("Insira o valor da Medição: ");
        double value = valueStr.nextDouble();

        System.out.print("Insira o unidade de Medição: ");
        String unit = unitStr.next();

        System.out.print("Insira o data da Medição: ");
        String date = dateStr.next();

        LocalDateTime new_date = dateFormat(date);

        if (new_date != null && station != null && sensor != null && unit != null) {
            city.addMeasurement(station, sensor, value, unit, new_date);
        }
    }

    /**
     * Método showStations cuja função é apresentar todas as estações
     * metereológicas duma dada cidade
     *
     * @param city Cidade onde estão as estações metereológicas a apresentar
     * @return false caso não consiga apresentar todas as estações
     * metereológicas e true caso consiga apresentá-las
     */
    private static boolean showStations(City city) {
        if (city == null) {
            return false;
        }

        Station[] stations = (Station[]) city.getStations();

        for (int i = 0; i < city.getStationCounter(); i++) {
            System.out.println("Nome da estação: " + stations[i].getName());
        }

        System.out.println();

        return true;
    }

    /**
     * Método showSensors cuja finalidade é apresentar todos os sensores duma
     * dada ciadade
     *
     * @param city Cidade onde estão guardados os sensores
     * @return false caso não consiga apresentar todos os sensores, true caso
     * consiga apresentá-los
     */
    private static boolean showSensors(City city) {
        if (city == null) {
            return false;
        }

        Station[] stations = (Station[]) city.getStations();

        for (int i = 0; i < stations.length; i++) {
            Sensor[] sensores = (Sensor[]) stations[i].getSensors();

            for (int j = 0; j < sensores.length; j++) {
                System.out.println("ID do sensor: " + sensores[j].getId());

                System.out.println();
            }
        }

        return true;
    }

    /**
     * Método showMeasurements cujo objetivo é apresentar todas as medições duma
     * dada cidade
     *
     * @param city Cidade onde estão guardadas as medições
     * @return false caso não consiga apresentar todas as medições, true caso
     * consiga apresentá-los
     */
    private static boolean showMeasurements(City city) {
        if (city == null) {
            return false;
        }

        Station[] stations = (Station[]) city.getStations();

        for (int i = 0; i < stations.length; i++) {
            Sensor[] sensores = (Sensor[]) stations[i].getSensors();

            for (int j = 0; j < sensores.length; j++) {
                Measurement[] measurements = (Measurement[]) sensores[j].getMeasurements();

                for (int k = 0; k < measurements.length; k++) {
                    System.out.println("Sensor: " + sensores[j].getId());
                    System.out.println("\tValor da medição: " + measurements[k].getValue());
                    System.out.println("\tData da medição: " + measurements[k].getTime() + "\n");
                }
            }
        }

        return true;
    }

    /**
     * Método getGraphs cujo papel é criar uma string JSON com os diferentes
     * gráficos criados
     *
     * @param city Cidade sobre a qual vão ser criados gráficos
     * @return string de JSONs
     */
    private static String[] getGraphs(City city) {
        String[] jsons = new String[4];

        jsons[0] = city.graphNumOfMeasurmentsByMonth();
        jsons[1] = city.graphNumOfMeasurementsByUnit();
        jsons[2] = city.graphNumOfMeasurementsByParameter();
        jsons[3] = city.graphNumOfTypeSensors();

        return jsons;
    }

    /**
     * Método starter cuja função é dar início ao programa criando uma cidade
     *
     * @return Cidade criada
     */
    private static City starter() {
        Scanner cityNameScanner = new Scanner(System.in,"utf-8");

        System.out.print("Insira o nome da cidade: ");
        String cityName = cityNameScanner.nextLine();

        Scanner cityIdScanner = new Scanner(System.in);
        System.out.print("Insira o ID da cidade: ");
        String cityId = cityIdScanner.next();

        System.out.println();

        City res = new City(cityId, cityName);

        return res;
    }

    /**
     * Método showIOStatistics cuja finalidade é apresentar um resumo de todos
     * os dados lidos dum dado ficheiro JSON
     *
     * @param iOStatistics Estatísticas a receber
     */
    private static void showIOStatistics(IOStatistics iOStatistics) {
        if (iOStatistics != null) {
            System.out.println("\n--->Dados Lidos do JSON\n");

            System.out.println("Medições lidas: " + iOStatistics.getNumberOfReadMeasurements());
            System.out.println("Medições únicas lidas: " + iOStatistics.getNumberOfNewMeasurementsRead());
            System.out.println("Sensores lidos: " + iOStatistics.getNumberOfSensorsRead());
            System.out.println("Sensores únicos lidos: " + iOStatistics.getNumberOfNewSensorsRead());
            System.out.println("Estações metereológicas lidas: " + iOStatistics.getNumberOfStationsRead());
            System.out.println("Estações metereológicas únicas lidas: " + iOStatistics.getNumberOfNewStationsRead());
            System.out.println("Número de exeções ocorridas: " + iOStatistics.getExceptions().length);

            String[] exception = iOStatistics.getExceptions();

            if (exception != null) {
                for (int i = 0; i < exception.length; i++) {
                    System.out.println(exception[i]);
                }
            }

            System.out.println();
        }
    }

    /**
     * Método statisticsMenu cujo objetivo é apresentar um menu com as opções de
     * consultar as medições por sensor ou por estação metereológica
     *
     * @param in Scanner para a opção escolhida
     * @return Opção escolhida caso seja válida, caso não seja apresenta
     * novamente o menu
     */
    private static int statisticsMenu(Scanner in) {
        System.out.println("\n1 - Mediçoes por Sensor\n2 - Mediçoes por Estação\n0 - Back");
        System.out.print("Opção: ");

        int choice = in.nextInt();

        System.out.println();

        if (choice >= 0 && choice <= 2) {
            return choice;
        } else {
            System.out.println("Opção errada!");

            return choice;
        }
    }

    /**
     * Método statisticsDateOrNoDateMenu cujo papel é apresentar um menu com a
     * escolha de consultar medições com um intervalo de tempo ou todas as
     * medições guardadas
     *
     * @param in Scanner para a opção escolhida
     * @param type Pode ser sensor ou station
     * @return Caso a opção não seja válida apresenta novamente o menu, caso
     * contrário retorna a opção escolhida
     */
    private static int statisticsDateOrNoDateMenu(Scanner in, String type) {
        System.out.println("\n1 - Medições sem intervalo de tempo\n2 - Medições por com datas\n3 - Lista de " + type
                + "\n0 - Back");
        System.out.print("Opção: ");

        int choice = in.nextInt();

        System.out.println();

        if (choice >= 0 && choice <= 3) {
            return choice;
        } else {
            System.out.println("Opção errada!");

            return choice;
        }
    }

    /**
     * Método optionsStatistics cuja função é, com base no retorno do método
     * statisticsMenu apresentar os dados da opção escolhida
     *
     * @param in Scanner com a opção escolhida
     * @param city Cidade na qual vai ser realizada a estatística
     */
    private static void optionsStatistics(Scanner in, City city) {
        boolean exit = false;

        while (!exit) {
            switch (statisticsMenu(in)) {
                case 1:
                    System.out.println("Mediçoes por Sensor");
                    optionsStatisticsNext(in, "Sensor", city);

                    break;
                case 2:
                    System.out.println("Mediçoes por Estação");
                    optionsStatisticsNext(in, "Estação", city);

                    break;
                case 0:
                    exit = true;

                    break;
            }
        }
    }

    /**
     * Método optionsStatisticsNext cuja finalidade é, com base no retorno
     * método statisticsDateOrNoDateMenu, apresentar estatísticas para a opção
     * escolhida
     *
     * @param in Scanner da opção escolhida
     * @param type Pode ser sensor ou station
     * @param city Cidade sobre a qual a estatística vai ser calculada
     */
    private static void optionsStatisticsNext(Scanner in, String type, City city) {
        boolean exit = false;

        while (!exit) {
            switch (statisticsDateOrNoDateMenu(in, type)) {
                case 1:
                    System.out.println("Mediçoes por " + type);
                    getMeasurementWithoutDate(city, type);

                    break;
                case 2:
                    System.out.println("Mediçoes " + type + " com datas");
                    getMeasurementWithDate(city, type);

                    break;
                case 3:
                    switch (type) {
                        case "Sensor":
                            showSensors(city);

                            break;
                        default:
                            showStations(city);

                            break;
                    }
                case 0:
                    exit = true;

                    break;
            }
        }
    }

    /**
     * Método getMeasurementWithoutDate cujo objetivo é obter medições sem ser
     * compreendido num intervalo de tempo
     *
     * @param city Cidade sobre a qual a estatística vai ser calculada
     * @param type Pode ser sensor ou station
     */
    private static void getMeasurementWithoutDate(City city, String type) {
        Scanner stationName = new Scanner(System.in, "utf-8");
        Scanner operator = new Scanner(System.in);
        Scanner parameter = new Scanner(System.in);
        Statistics[] statistics;

        if (type.equals("Sensor")) {
            System.out.print("Insira o nome da Estação: ");
            String station = stationName.nextLine();

            System.out.print("Insira o Operador: ");
            AggregationOperator operatorRes = getOperatorMenu(operator);

            System.out.print("Insira o Parametro: ");
            Parameter parameterRes = getParameterMenu(parameter);

            statistics = (Statistics[]) city.getMeasurementsBySensor(station, operatorRes, parameterRes);
        } else {
            System.out.print("Insira o Operador: ");
            AggregationOperator operatorRes = getOperatorMenu(operator);

            System.out.print("Insira o Parâmetro: ");
            Parameter parameterRes = getParameterMenu(parameter);

            statistics = (Statistics[]) city.getMeasurementsByStation(operatorRes, parameterRes);
        }

        if (statistics != null) {
            for (int i = 0; i < statistics.length; i++) {
                System.out.print(statistics[i].getDescription());
                System.out.println(statistics[i].getValue());
            }
        }
    }

    /**
     * Método getMeasurementWithDate cujo papel é obter um conjunto de medições
     * compreendidas num intervalo de tempo
     *
     * @param city Cidade sobre a qual vão ser calculadas as estatísticas
     * @param type Pode ser sensor ou station
     */
    private static void getMeasurementWithDate(City city, String type) {
        Scanner stationName = new Scanner(System.in, "utf-8");
        Scanner operator = new Scanner(System.in);
        Scanner parameter = new Scanner(System.in);
        Scanner startDate = new Scanner(System.in);
        Scanner endDate = new Scanner(System.in);
        Statistics[] statistics;

        if (type.equals("Sensor")) {
            System.out.print("Insira o nome da Estação: ");
            String station = stationName.nextLine();

            System.out.print("Insira o Operador: ");
            AggregationOperator operatorRes = getOperatorMenu(operator);

            System.out.print("Insira o Parâmetro: ");
            Parameter parameterRes = getParameterMenu(parameter);

            System.out.print("Insira a data minima (yyyymmddhhmm): ");
            String startDateString = startDate.next();

            LocalDateTime startDateRes = dateFormat(startDateString);

            System.out.print("Insira a data maxima (yyyymmddhhmm): ");
            String endDateString = endDate.nextLine();

            LocalDateTime endDateRes = dateFormat(endDateString);

            statistics = (Statistics[]) city.getMeasurementsBySensor(station, operatorRes, parameterRes, startDateRes, endDateRes);
        } else {
            System.out.print("Insira o Operador: ");
            AggregationOperator operatorRes = getOperatorMenu(operator);

            System.out.print("Insira o Parâmetro: ");
            Parameter parameterRes = getParameterMenu(parameter);

            System.out.print("Insira a data minima (yyyymmddhhmm): ");
            String startDateString = startDate.next();

            LocalDateTime startDateRes = dateFormat(startDateString);

            System.out.print("Insira a data máxima (yyyymmddhhmm): ");
            String endDateString = endDate.nextLine();

            LocalDateTime endDateRes = dateFormat(endDateString);

            statistics = (Statistics[]) city.getMeasurementsByStation(operatorRes, parameterRes, startDateRes, endDateRes);
        }

        if (statistics != null) {
            for (int i = 0; i < statistics.length; i++) {
                System.out.print(statistics[i].getDescription());
                System.out.println(statistics[i].getValue());
            }
        }

    }

    /**
     * Método getOperatorMenu cuja função é apresentar um menu com as operações
     * possíveis de serem escolhidas para o cálculo de estatísticas
     *
     * @param in Scanner da opção escolhida
     * @return A operação escolhida caso seja válida, caso não seja apresenta
     * novamente o menu
     */
    private static AggregationOperator getOperatorMenu(Scanner in) {
        System.out.println("\n\t1 - Valor Médio\n\t2 - Valor Minimo\n\t3 - Valor Maximo\n\t4 - Número de Valores\n");
        System.out.print("\tOpção: ");

        int choice = in.nextInt();

        System.out.println();

        while (choice < 0 || choice > 4) {
            if (choice >= 0 && choice <= 4) {
                switch (choice) {
                    case 1:
                        return AggregationOperator.AVG;
                    case 2:
                        return AggregationOperator.MIN;
                    case 3:
                        return AggregationOperator.MAX;
                    default:
                        return AggregationOperator.COUNT;
                }
            } else {
                System.out.println("Opção errada!");
            }
        }

        return null;
    }

    /**
     * Método getParameterMenu cuja finalidade é aprsentar um menu com todos os
     * parâmetros existentes
     *
     * @param in Scanner da opção escolhida
     * @return Parâmetro escolhido ou null caso tenha sido escolhido um
     * parâmetro inválido
     */
    private static Parameter getParameterMenu(Scanner in) {
        System.out.println("\n\t1 - NO2\n\t2 - O3\n\t3 - PM2_5\n\t4 - PM10\n\t5 - SO2\n\t6 - C6H6\n\t7 - CO\n\t"
                + "8 - LAEQ\n\t9 - PA\n\t10 - TEMP\n\t11 - RU\n\t12 - VD\n\t13 - VI\n\t14 - HM\n\t15 - PC\n\t"
                + "16 - RG\n");
        System.out.print("\tOpção: ");

        int choice = in.nextInt();

        System.out.println();

        while (choice < 0 || choice > 16) {
            if (choice >= 0 && choice <= 16) {
                switch (choice) {
                    case 1:
                        return Parameter.NO2;
                    case 2:
                        return Parameter.O3;
                    case 3:
                        return Parameter.PM2_5;
                    case 4:
                        return Parameter.PM10;
                    case 5:
                        return Parameter.SO2;
                    case 6:
                        return Parameter.C6H6;
                    case 7:
                        return Parameter.CO;
                    case 8:
                        return Parameter.LAEQ;
                    case 9:
                        return Parameter.PA;
                    case 10:
                        return Parameter.TEMP;
                    case 11:
                        return Parameter.RU;
                    case 12:
                        return Parameter.VD;
                    case 13:
                        return Parameter.VI;
                    case 14:
                        return Parameter.HM;
                    case 15:
                        return Parameter.PC;
                    default:
                        return Parameter.RG;
                }
            } else {
                System.out.println("Opção errada!");
            }
        }
        return null;
    }

    /**
     * Método dateFormat cujo objetivo é formatar uma data em formato String
     * para uma data formatada em LocalDateTime
     *
     * @param date Data a formatar
     * @return Data formatada caso esta não seja nula, nesse caso retorna null
     */
    private static LocalDateTime dateFormat(String date) {
        if (date == null) {
            return null;
        }

        if (date.length() == 12) {
            LocalDateTime formattedDate = LocalDateTime.of(Integer.parseInt(date.substring(0, 4)),
                    Month.of(Integer.parseInt(date.substring(4, 6))), Integer.parseInt(date.substring(6, 8)),
                    Integer.parseInt(date.substring(8, 10)), Integer.parseInt(date.substring(10, 12)));

            return formattedDate;
        } else {
            System.out.println("Data inválida");

            return null;
        }
    }
}
