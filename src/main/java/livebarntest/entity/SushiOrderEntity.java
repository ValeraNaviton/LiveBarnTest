package livebarntest.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sushi_order")
public class SushiOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private OrderStatusEntity status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sushi_id", referencedColumnName = "id")
    private SushiType sushi;

    @Column(name = "createdAt")
    private Date createdAt = new Date();
}
