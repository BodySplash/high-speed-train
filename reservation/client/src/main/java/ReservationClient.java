import common.aeron.client.ClusterClient;

import java.util.Scanner;

public class ReservationClient {

    public static void main(String[] args) {
        try (var ignored = ClusterClient.launch(ClusterClient.configuration()
                .rootDirectory("./data"))) {
            // TODO -> create proxy
            var scanner = new Scanner(System.in);
            while (true) {
                System.out.print('>');
                var command = scanner.nextLine();
                if ("exit".equals(command)) {
                    break;
                }
            }
        }
    }

}
