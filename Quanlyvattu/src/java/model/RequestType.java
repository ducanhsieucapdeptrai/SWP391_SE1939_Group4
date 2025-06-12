package model;

public class RequestType {

    private int requestTypeId;
    private String requestTypeName;

    public RequestType() {
    }

    public RequestType(int requestTypeId, String requestTypeName) {
        this.requestTypeId = requestTypeId;
        this.requestTypeName = requestTypeName;
    }

    public int getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    @Override
    public String toString() {
        return "RequestType{"
                + "requestTypeId=" + requestTypeId
                + ", requestTypeName='" + requestTypeName + '\''
                + '}';
    }
}
