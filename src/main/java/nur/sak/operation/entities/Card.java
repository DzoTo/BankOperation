package nur.sak.operation.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "card")
@Getter
@Setter
@RequiredArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "your_sequence_generator")
    @SequenceGenerator(name = "your_sequence_generator", sequenceName ="your_sequence_name", allocationSize = 1)
    private Long id;
    private Long cardNum;
    private String bankName;
    private Double mainBalance;
    private Boolean isDepositOpen;


    @ManyToMany(mappedBy = "cardSet", fetch = FetchType.EAGER)
    private Set<User> userSet;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "deposit_id", referencedColumnName = "id")
    private Deposit deposit;
}
