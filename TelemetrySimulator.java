public class TelemetrySimulator {

    static class CarTelemetry {
        int speed;         // km/h
        int gear;          // 1–8
        double tireTemp;   // °C
        double fuelLevel;  // %

        @Override
        public String toString() {
            return String.format("Speed: %dkm/h | Gear: %d | Tire Temp: %.1f°C | Fuel: %.1f%%",
                    speed, gear, tireTemp, fuelLevel);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            try {
                CarTelemetry data = generateRandomData();
                checkTelemetry(data); // might throw exceptions
                System.out.println(data);
            } catch (RuntimeException e) {
                System.err.println("⚠️ ALERT: " + e.getMessage());
            }
            Thread.sleep(1000); // 1 second update interval
        }
    }

    static CarTelemetry generateRandomData() {
        CarTelemetry t = new CarTelemetry();
        t.speed = (int)(Math.random() * 351);      // 0–350 km/h
        t.gear = (int)(Math.random() * 8) + 1;     // 1–8
        t.tireTemp = 70 + (Math.random() * 60);    // 70–130°C
        t.fuelLevel = Math.random() * 100;         // 0–100%
        return t;
    }

    static void checkTelemetry(CarTelemetry t) {
        if (t.tireTemp > 120) {
            throw new RuntimeException("🔥 Tire overheating! (" + t.tireTemp + "°C)");
        }
        if (t.fuelLevel < 5) {
            throw new RuntimeException("⛽ Critical fuel level! (" + t.fuelLevel + "%)");
        }
    }
}