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

    private StateTax unmarshallStateTax(String stateTaxesAsText) {
        String[] taxTokens = stateTaxesAsText.split(DELIMITER);
        String stateAbbr = taxTokens[0];
        String stateName = taxTokens[1];
        String taxRate = taxTokens[2];


        StateTax stateTaxFromFile = new StateTax(stateAbbr, stateName, taxRate);

        return stateTaxFromFile;
    }

    private String marshallStateTax(StateTax aState) {
        String stateTaxesAsText = aState.getStateAbbreviation() + DELIMITER;
        stateTaxesAsText += aState.getStateName() + DELIMITER;
        stateTaxesAsText += aState.getTaxRate() + DELIMITER;

        return stateTaxesAsText;
    }

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

    @Override
    public StateTax getStateTax(String stateAbbreviation) throws
            StateTaxNotFoundException {
        return stateTaxes.get(stateAbbreviation);
    }

    @Override
    public List<StateTax> getAllStateTaxes() throws
            FlooringDataPersistenceException {
        return new ArrayList<StateTax>(stateTaxes.values());
    }
}
