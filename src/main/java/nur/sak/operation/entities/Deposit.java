package nur.sak.operation.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Deposit")
@Getter
@Setter
@RequiredArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "your_sequence_generator2")
    @SequenceGenerator(name = "your_sequence_generator2", sequenceName ="your_sequence_name2", allocationSize = 1)
    private Long id;
    private String currency;
    private Double amountOfMoney;
    private Double interestRate = 0.1500;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date openAccDate;


}
