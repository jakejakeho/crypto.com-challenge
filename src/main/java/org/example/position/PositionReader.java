package org.example.position;

import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@Service
public class PositionReader {

    private static final String symbol = "symbol";

    private static final String positionSize = "positionSize";

    public List<Position> readCsvFile() {
        List<Position> positions = new ArrayList<>();
        try {
            String filePath = System.getProperty("user.dir") + "/src/main/java/org/example/position/position.csv";
            File csvFile = new File(filePath);
            Scanner csvReader = new Scanner(csvFile);
            if (csvReader.hasNextLine()) {
                String firstLine = csvReader.nextLine();
                String[] fields = firstLine.split(",");
                if (fields.length != 2 || !Objects.equals(fields[0], symbol) || !Objects.equals(fields[1], positionSize)) {
                    throw new UnsupportedOperationException();
                }
            }
            while (csvReader.hasNextLine()) {
                String dataLine = csvReader.nextLine();
                String[] data = dataLine.split(",");
                if (data.length != 2) {
                    throw new UnsupportedOperationException();
                }
                positions.add(new Position(data[0], data[1]));
            }
        } catch (Exception e) {
            System.out.println("Failed to read csv file" + e);
        }
        return positions;
    }
}
