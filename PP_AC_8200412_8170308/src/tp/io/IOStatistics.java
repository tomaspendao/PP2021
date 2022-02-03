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

public class IOStatistics implements edu.ma02.io.interfaces.IOStatistics {

    /**
     * Número de medições lidas
     */
    private int numMeasurements;

    /**
     * Número de medições únicas lidas
     */
    private int newMeasurements;

    /**
     * Número de estações metereológicas únicas lidas
     */
    private int newStations;

    /**
     * Número de estações metereológicas lidas
     */
    private int numStations;

    /**
     * Número de sensores lidos
     */
    private int numSensor;

    /**
     * Número de sensores únicos lidos
     */
    private int newSensor;

    /**
     * Conjunto das excessões lançadas
     */
    private String[] exceptions;

    /**
     * Contador para o número de exeções lançadas
     */
    private int countExceptions;

    /**
     * Método construtor para uma instância de IOStatistics onde todas os
     * contadores são inicializados a 0 (zero)
     */
    public IOStatistics() {
        this.exceptions = new String[0];
        this.countExceptions = 0;
        this.newMeasurements = 0;
        this.newSensor = 0;
        this.newStations = 0;
        this.numMeasurements = 0;
        this.numSensor = 0;
        this.numStations = 0;
    }

    /**
     * Override do método getNumberOfReadMeasurements cuja finalidade é obter o
     * número de medições lidas
     *
     * @return Número de medições lidas
     */
    @Override
    public int getNumberOfReadMeasurements() {
        return this.numMeasurements;
    }

    /**
     * Override do método getNumberOfNewStationsRead cujo objetivo é obter o
     * número de estações metereológicas únicas lidas
     *
     * @return Número de estações metereológicas únicas lidas
     */
    @Override
    public int getNumberOfNewStationsRead() {
        return this.newStations;
    }

    /**
     * Override do método getNumberOfStationsRead com o papel de obter o número
     * de estações metereológicas lidas
     *
     * @return Número de estações metereológicas lidas
     */
    @Override
    public int getNumberOfStationsRead() {
        return this.numStations;
    }

    /**
     * Override do método getNumberOfSensorsRead cuja função é obter o número de
     * sensores lidos
     *
     * @return Número de sensores lidos
     */
    @Override
    public int getNumberOfSensorsRead() {
        return this.numSensor;
    }

    /**
     * Override do método getNumberOfNewSensorsRead cuja finalidade é obter o
     * número de sensores únicos lidos
     *
     * @return Número de sensores únicos lidos
     */
    @Override
    public int getNumberOfNewSensorsRead() {
        return this.newSensor;
    }

    /**
     * Override do método getNumberOfNewMeasurementsRead cujo objetivo é obter o
     * número de medições únicas lidas
     *
     * @return Número de medições únicas lidas
     */
    @Override
    public int getNumberOfNewMeasurementsRead() {
        return this.newMeasurements;
    }

    /**
     * Override do método getExceptions que tem o papel de obter o conjunto das
     * exeções lançadas na importação da informação
     *
     * @return Conjunto das exeções lançadas na importação da informação
     */
    @Override
    public String[] getExceptions() {
        return this.exceptions;
    }

    /**
     * Método addException cuja função é adicionar uma exceção lançada
     *
     * @param exception Exeção a ser registada
     * @return true caso adicione a exceção, false caso esta não seja adicionada
     */
    public boolean addException(String exception) {
        if (exception == null) {
            return false;
        }

        if (this.exceptions.length == this.countExceptions) {
            String[] temp = new String[this.exceptions.length + 1];

            for (int i = 0; i < this.countExceptions; i++) {
                temp[i] = this.exceptions[i];
            }

            this.exceptions = temp;
        }

        this.exceptions[this.countExceptions] = exception;
        this.countExceptions++;

        return true;
    }

    /**
     * Método incMeasurementsRead cuja finalidade é incrementar o número de
     * medições lidas
     *
     * @param is
     */
    public void incMeasurementsRead(boolean is) {
        if (is == true) {
            this.newMeasurements++;
        }

        this.numMeasurements++;
    }

    /**
     * Método incSensorsRead cujo objetivo é incrementar o número de sensores
     * lidos
     *
     * @param is
     */
    public void incSensorsRead(boolean is) {
        if (is == true) {
            this.newSensor++;
        }

        this.numSensor++;
    }

    /**
     * Método incStationRead cujo papel é incrementar o número de estações
     * metereológicas lidas
     *
     * @param is
     */
    public void incStationRead(boolean is) {
        if (is == true) {
            this.newStations++;
        }
        
        this.numStations++;
    }
}