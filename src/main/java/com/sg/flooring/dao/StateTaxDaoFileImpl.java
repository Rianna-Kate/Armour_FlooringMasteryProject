package com.sg.flooring.dao;

import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.StateTax;

import java.io.*;
import java.util.*;

public class StateTaxDaoFileImpl implements StateTaxDao {
    private static final String FILE_NAME = "Taxes.txt";
    private static final String DELIMITER = ",";

    Map<String, StateTax> stateTaxes = new HashMap<>();

    public StateTaxDaoFileImpl() {
        read();
    }

    /**
     * Reads each line of text, separating into tokens that are then assigned to StateTax Object
     * variables to create and add an StateTax object to the hashmap.
     * @param stateTaxesAsText
     * @return
     */
    private StateTax unmarshallStateTax(String stateTaxesAsText) {
        String[] taxTokens = stateTaxesAsText.split(DELIMITER);
        String stateAbbr = taxTokens[0];
        String stateName = taxTokens[1];
        String taxRate = taxTokens[2];


        StateTax stateTaxFromFile = new StateTax(stateAbbr, stateName, taxRate);

        return stateTaxFromFile;
    }

    /**
     * Organize variables of StateTax object into text so that it can be written to a file.
     * @param aState
     * @return
     */
    private String marshallStateTax(StateTax aState) {
        String stateTaxesAsText = aState.getStateAbbreviation() + DELIMITER;
        stateTaxesAsText += aState.getStateName() + DELIMITER;
        stateTaxesAsText += aState.getTaxRate() + DELIMITER;

        return stateTaxesAsText;
    }

    /**
     * Reading all StateTax objects from the file "StateTax.txt" file
     * @throws FlooringDataPersistenceException
     */
    private void read()
            throws FlooringDataPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(FILE_NAME)));
        } catch (FileNotFoundException e) {
            throw new FlooringDataPersistenceException(
                    "-_- Could not load state tax file data into memory.", e);
        }
        String currentLine;

        StateTax currState;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            // unmarshall the line into a State
            currState = unmarshallStateTax(currentLine);

            stateTaxes.put(currState.getStateAbbreviation(), currState);
        }
        scanner.close();
    }

    // Not used
    private void write()
            throws FlooringDataPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(FILE_NAME));
        } catch (IOException e) {
            throw new FlooringDataPersistenceException(
                    "Could not save product data.", e);
        }

        String stateTaxesAsText;
        List<StateTax> stateList = this.getAllStateTaxes();
        for (StateTax currentState : stateList) {
            stateTaxesAsText = marshallStateTax(currentState);
            out.println(stateTaxesAsText);
            out.flush();
        }

        out.close();
    }

    /**
     * Get StateTax object based on specified text.
     * @param stateAbbreviation - Abbreviated version of state
     * @return StateTax object that has the specified abbreviated state name
     * @throws StateTaxNotFoundException
     */
    @Override
    public StateTax getStateTax(String stateAbbreviation) throws
            StateTaxNotFoundException {
        return stateTaxes.get(stateAbbreviation);
    }

    /**
     * Get the list of all Product objects in the products hashmap
     * @return List of all Product Objects within the products hashmap
     * @throws FlooringDataPersistenceException
     */
    @Override
    public List<StateTax> getAllStateTaxes() throws
            FlooringDataPersistenceException {
        return new ArrayList<StateTax>(stateTaxes.values());
    }
}
