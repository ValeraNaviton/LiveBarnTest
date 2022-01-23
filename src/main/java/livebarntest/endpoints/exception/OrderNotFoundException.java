package livebarntest.endpoints.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class OrderNotFoundException extends Exception {

  private String orderId;

}
