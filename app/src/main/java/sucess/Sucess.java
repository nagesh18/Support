
package sucess;


public class Sucess {

    private Boolean status;
    private Integer code;
    private String user_name;
    private String caller_id;
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String userName) {
        this.user_name = userName;
    }

    public String getCallerId() {
        return caller_id;
    }

    public void setCallerId(String callerId) {
        this.caller_id = callerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
