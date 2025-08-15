import java.util.Random;

public class TelemetryRaceSim {

    static class CarTelemetry {
        String driver;
        int speed;         // km/h
        int gear;          // 1–8
        double tireTemp;   // °C
        double fuelLevel;  // %
        int lap;

        @Override
        public String toString() {
            return String.format("%s | Lap %d | Speed: %dkm/h | Gear: %d | Tire: %.1f°C | Fuel: %.1f%%",
                    driver, lap, speed, gear, tireTemp, fuelLevel);
        }
    }

    static Random rand = new Random();
    static final int TOTAL_LAPS = 10;

    public static void main(String[] args) throws InterruptedException {
        CarTelemetry[] cars = {
            initCar("Musau GP"),
            initCar("Rival Motors"),
            initCar("Apex Racing")
        };

        System.out.println("🏎️ Race Start!");
        boolean raceOngoing = true;

        while (raceOngoing) {
            raceOngoing = false;

            for (CarTelemetry car : cars) {
                try {
                    updateTelemetry(car);
                    checkTelemetry(car);
                    System.out.println(car);
                } catch (RuntimeException e) {
                    System.err.println("⚠️ " + car.driver + ": " + e.getMessage());
                    performPitStop(car);
                }

                if (car.lap <= TOTAL_LAPS) {
                    raceOngoing = true;
                }
            }

            printLeaderboard(cars);
            Thread.sleep(1000);
        }

        System.out.println("🏁 Race Finished!");
    }

    static CarTelemetry initCar(String name) {
        CarTelemetry car = new CarTelemetry();
        car.driver = name;
        car.lap = 1;
        car.fuelLevel = 100;
        return car;
    }

    static void updateTelemetry(CarTelemetry car) {
        // Only update if race not finished
        if (car.lap > TOTAL_LAPS) return;

        car.speed = rand.nextInt(351);
        car.gear = rand.nextInt(8) + 1;
        car.tireTemp = 70 + rand.nextDouble() * 60;
        car.fuelLevel -= 1 + rand.nextDouble() * 3; // fuel burn
        if (car.speed > 200 && rand.nextDouble() < 0.1) {
            car.lap++; // complete a lap
        }
    }

    static void checkTelemetry(CarTelemetry car) {
        if (car.tireTemp > 120) {
            throw new RuntimeException("🔥 Tire overheating!");
        }
        if (car.fuelLevel < 5) {
            throw new RuntimeException("⛽ Critical fuel level!");
        }
    }

    static void performPitStop(CarTelemetry car) {
        System.out.println("🔧 " + car.driver + " enters the pits...");
        car.tireTemp = 85;
        car.fuelLevel = 100;
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        System.out.println("✅ " + car.driver + " exits the pits refreshed!");
    }

    static void printLeaderboard(CarTelemetry[] cars) {
        System.out.println("\n=== 🏆 Leaderboard ===");
        java.util.Arrays.stream(cars)
            .sorted((c1, c2) -> Integer.compare(c2.lap, c1.lap))
            .forEach(c -> System.out.println(c.driver + " - Lap " + c.lap + "/" + TOTAL_LAPS));
        System.out.println();
    }
}