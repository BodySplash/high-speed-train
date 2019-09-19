import common.aeron.client.ClusterClient;
import reservation.client.ReservationClusterProxy;

import java.util.Scanner;

public class ReservationClient {

    public static void main(String[] args) {
        try (var client = ClusterClient.launch(ClusterClient.configuration()
                .rootDirectory("./data"))) {
            var proxy = new ReservationClusterProxy(client);
            client.subscribe(proxy);
            var scanner = new Scanner(System.in);
            while (true) {
                System.out.print('>');
                var command = scanner.nextLine();
                if ("exit".equals(command)) {
                    break;
                }
                if (command.startsWith("ct")) {
                    var params = command.split(" ");
                    proxy.createTrain(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                }
            }

        }
    }

}
