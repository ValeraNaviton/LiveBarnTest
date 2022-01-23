package livebarntest.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "status")
// not sure why regular enum is not suitable here ...
public class OrderStatusEntity {
  @Id
  @GeneratedValue
  private int id;

  @Column(name = "name")
  private String name;

  public OrderStatus getStatus() {
    for (OrderStatus status : OrderStatus.values()) {
      if (status.name.equals(this.name)) {
        return status;
      }
    }
    throw new IllegalArgumentException("unknown status [" + this.name + "]");
  }

  public int getId() {
    return id;
  }

  public enum OrderStatus {
    CREATED("created"),
    IN_PROGRESS("in-progress"),
    PAUSED("paused"),
    FINISHED("finished"),
    CANCELLED("cancelled");

    private String name;

    public static final Set<OrderStatus> PAUSABLE_STATUSES = new HashSet<>(Arrays.asList(CREATED, IN_PROGRESS));
    public static final Set<OrderStatus> RESUMABLE_STATUSES = new HashSet<>(Arrays.asList(CREATED, PAUSED));
    public static final Set<OrderStatus> CANCELLABLE_STATUSES = new HashSet<>(Arrays.asList(CREATED, IN_PROGRESS, PAUSED));

    OrderStatus(String name) {
      this.name = name;
    }

      public String getName() {
          return name;
      }
  }
}
