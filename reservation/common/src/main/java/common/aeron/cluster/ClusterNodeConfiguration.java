package common.aeron.cluster;

import io.aeron.cluster.service.ClusteredService;

public class ClusterNodeConfiguration {

    private Mode mode = Mode.LOCAL;
    private String rootDirectory = "./data";
    private ClusteredService service;
    private int memberId = 0;

    public enum Mode {
        LOCAL, PERF
    }

    public ClusterNodeConfiguration mode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public Mode mode() {
        return mode;
    }

    public ClusterNodeConfiguration rootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
        return this;
    }

    public String rootDirectory() {
        return rootDirectory;
    }

    public ClusterNodeConfiguration service(ClusteredService service) {
        this.service = service;
        return this;
    }

    public ClusteredService service() {
        return service;
    }

    public ClusterNodeConfiguration memberId(int memberId) {
        this.memberId = memberId;
        return this;
    }

    public int memberId() {
        return memberId;
    }
}
