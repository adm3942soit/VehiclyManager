package com.adonis.data.payments;

import com.adonis.data.Audit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

/**
 * Created by oksdud on 03.05.2017.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "credit_card", schema = "")
@Getter
@Setter
@NoArgsConstructor
public class CreditCard extends Audit {
//    @Column(name = "PERSON_ID", nullable = false)
//    private Long personId;
    @Column(name = "NUMBER", nullable = false)
    private String number;
    @Digits(message = "This field must be digit", integer = 3, fraction = 0)
    @Column(name = "CVV2", nullable = false)
    private String cvv2;
    @Column(name = "TYPE", nullable = false)
    private String type;
    @Digits(message = "This field must be digit", integer = 4, fraction = 0)
    @Column(name = "EXPIRE_YEAR", nullable = false)
    private String expireYear;
    @Digits(message = "This field must be digit", integer = 2, fraction = 0)
    @Column(name = "EXPIRE_MONTH", nullable = false)
    private String expireMonth;

}
