package t5.bookhive.backendapi.enums;

/**https://www.baeldung.com/java-enum-values
*/
/*For retriveing order status*/

public enum OrderStatusEnum implements CodeEnum {
    NEW(0, "New OrderMain"),
    FINISHED(1, "Finished"),
    CANCELED(2, "Canceled")
    ;

    private  int code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

	/* Here this returns/sets the order status */
    @Override
    public Integer getCode() {
        return code;
    }
}
