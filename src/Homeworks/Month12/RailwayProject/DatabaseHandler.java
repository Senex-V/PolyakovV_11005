package Homeworks.Month12.RailwayProject;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

class DatabaseHandler {
    private static final String TRAINS_TXT_PATH = "src/Homeworks/Month12/RailwayProject/data/trains.txt";
    private static final String STATIONS_TXT_PATH = "src/Homeworks/Month12/RailwayProject/data/stations.txt";
    private static final String WAYMATRIX_TXT_PATH = "src/Homeworks/Month12/RailwayProject/data/waymatrix.txt";
    private static final String TABLE_NAME = "timetable";
    private static final String DB_PATH = "D:\\Projects\\Java\\" +
            "PolyakovV_11005\\src\\Homeworks\\Month12" +
            "\\RailwayProject\\data\\" + TABLE_NAME + ".db";
    private static final String[] COLUMNS = {
            "id",
            "train_name",
            "train_speed",
            "train_capacity",
            "train_ticket_cost",
            "train_type",
            "train_route"
    };
    private Connection connection;
    private static final ArrayList<String> waymatrixRows = getWayMatrix();
    private static final ArrayList<String> stationNames = getStationNames();

    DatabaseHandler() {
        openDatabase();
    }

    private void openDatabase() {
        try {
            Class.forName("org.sqlite.JDBC"); // class loading
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    void add(Train train) {
        String query = "INSERT INTO " + TABLE_NAME + " " +
                "( train_name, train_speed, train_capacity, train_ticket_cost, train_type, train_route )\n" +
                "VALUES ( '" +
                train.getName() + "', " +
                train.getSpeed() + ", " +
                train.getCapacity() + ", " +
                train.getTicketCost() + ", " +
                train.getTrainType().getId() + ", '" +
                train.getRouteCode() + "' );";

        System.out.println(query);
        try {
            Statement statement = connection.createStatement();
            //statement.executeUpdate(query);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void update(Train train) {
        String query = "UPDATE " + TABLE_NAME + " SET\n" +
                COLUMNS[1] + " = '" + train.getName() + "',\n" +
                COLUMNS[2] + " = " + train.getSpeed() + ",\n" +
                COLUMNS[3] + " = " + train.getCapacity() + ",\n" +
                COLUMNS[4] + " = " + train.getTicketCost() + ",\n" +
                COLUMNS[5] + " = " + train.getTrainType().getId() + ",\n" +
                COLUMNS[6] + " = '" + train.getRouteCode() + "'\n" +
                "WHERE\n" +
                COLUMNS[0] + " = " + train.getId() + ";";

        System.out.println(query);
        try {
            Statement statement = connection.createStatement();
            //statement.executeUpdate(query);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void displayDatabase() {
        String query = "SELECT * FROM " + TABLE_NAME + ";";

        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                System.out.println(new Train(
                        results.getInt(COLUMNS[0]),
                        results.getString(COLUMNS[1]),
                        results.getInt(COLUMNS[2]),
                        results.getInt(COLUMNS[3]),
                        results.getInt(COLUMNS[4]),
                        TrainType.values()[results.getInt(COLUMNS[5])],
                        results.getString(COLUMNS[6]))
                );
            }
            results.close();
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    // Database parser
    static ArrayList<Train> getTrains() {
        int idCounter = 0;
        ArrayList<Train> trains = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new FileReader(TRAINS_TXT_PATH));
            while (scanner.hasNext()) {
                trains.add(new Train(
                        idCounter++,
                        scanner.next(),
                        scanner.nextInt(),
                        scanner.nextInt(),
                        scanner.nextInt(),
                        TrainType.values()[scanner.nextInt()],
                        scanner.next())
                );
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trains;
    }

    // get all stations and set their ways
    static ArrayList<Station> getStations() {
        ArrayList<Station> stations = new ArrayList<>();
        for (int i = 0; i < stationNames.size(); i++) {
            stations.add(new Station(
                    i,
                    stationNames.get(i),
                    getWaysFor(i)
            ));
        }
        return stations;
    }

    static ArrayList<String> getStationNames() {
        ArrayList<String> stationNames = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new FileReader(STATIONS_TXT_PATH));
            while (scanner.hasNext()) {
                stationNames.add(scanner.next());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stationNames;
    }

    // waymatrix.txt parser returns ArrayList of Strings each of which contains one row
    static ArrayList<String> getWayMatrix() {
        ArrayList<String> wayRows = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new FileReader(WAYMATRIX_TXT_PATH));
            while (scanner.hasNext()) {
                wayRows.add(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wayRows;
    }

    // waymatrixRows parser for finding exact ways referring to station with give id
    static ArrayList<Way> getWaysFor(int id) {
        ArrayList<Way> ways = new ArrayList<>();
        String wayRow = waymatrixRows.get(id);
        String[] splittedRow = wayRow.split(" ");
        for (int i = 0; i < splittedRow.length; i++) {
            ways.add(new Way(

            ));
        }

        return ways;
    }
}
