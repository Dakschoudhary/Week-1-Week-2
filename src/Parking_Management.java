import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
    String status;

    ParkingSpot() {
        status = "EMPTY";
    }
}

class ParkingLot {

    private ParkingSpot[] table;
    private int capacity;
    private int occupied = 0;
    private int totalProbes = 0;
    private int totalParks = 0;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    public String parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].status.equals("OCCUPIED")) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = "OCCUPIED";

        occupied++;
        totalProbes += probes;
        totalParks++;

        return "Assigned spot #" + index + " (" + probes + " probes)";
    }

    public String exitVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (!table[index].status.equals("EMPTY")) {

            if (table[index].status.equals("OCCUPIED") &&
                    table[index].licensePlate.equals(licensePlate)) {

                long duration = System.currentTimeMillis() - table[index].entryTime;
                double hours = duration / 3600000.0;
                double fee = Math.ceil(hours) * 5;

                table[index].status = "DELETED";
                occupied--;

                return "Spot #" + index + " freed, Duration: " +
                        String.format("%.2f", hours) + "h, Fee: $" + fee;
            }

            index = (index + 1) % capacity;
            probes++;

            if (probes > capacity) break;
        }

        return "Vehicle not found";
    }

    public void getStatistics() {

        double occupancy = (occupied * 100.0) / capacity;
        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
    }
}

public class Parking_Management {

    public static void main(String[] args) throws Exception {

        ParkingLot lot = new ParkingLot(500);

        System.out.println(lot.parkVehicle("ABC-1234"));
        System.out.println(lot.parkVehicle("ABC-1235"));
        System.out.println(lot.parkVehicle("XYZ-9999"));

        Thread.sleep(2000);

        System.out.println(lot.exitVehicle("ABC-1234"));

        lot.getStatistics();
    }
}