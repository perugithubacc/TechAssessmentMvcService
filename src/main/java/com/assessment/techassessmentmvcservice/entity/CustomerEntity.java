package com.assessment.techassessmentmvcservice.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techassessment.techassessmentmvcservice.model.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;


@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_id_seq", allocationSize = 1)
    @Column(name = "customer_id", nullable = false, unique = true, length = 20)
    private Long customerId;

    @Column(name = "document_id", nullable = false, unique = true)
    private String documentId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "office_number")
    private String officeNumber;

    @Column(name = "personal_email", unique = true, nullable = false)
    private String personalEmail;

    @Column(name = "office_email", unique = true)
    private String officeEmail;

    @Column(name = "family_members")
    private String familyMembers;

    @Column(name = "address")
    private String address;

    @Column(name = "customer_type", nullable = false)
    private String customerType;

    @Column(name = "age")
    private Integer age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CustomerEntity that = (CustomerEntity) o;
        return getCustomerId() != null && Objects.equals(getCustomerId(), that.getCustomerId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Customer toDto() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            List<String> family_members = objectMapper.readValue(this.familyMembers, List.class);
            @SuppressWarnings("unchecked")
            List<String> addressList = objectMapper.readValue(this.address, List.class);

            return Customer.builder()
                    .customerId(this.customerId.toString())
                    .documentId(this.documentId)
                    .firstName(this.firstName)
                    .middleName(this.middleName)
                    .lastName(this.lastName)
                    .birthday(this.birthday)
                    .gender(Customer.GenderEnum.valueOf(this.gender))
                    .customerType(this.customerType)
                    .age(Period.between(birthday, LocalDate.now()).getYears())
                    .mobileNumber(this.mobileNumber)
                    .officeNumber(this.officeNumber)
                    .officeEmail(this.officeEmail)
                    .personalEmail(this.personalEmail)
                    .documentId(this.documentId)
                    .familyMembers(family_members)
                    .address(addressList)
                    .build();
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Json processing exception occurred: " + this.getCustomerId());
        }
    }

    public CustomerEntity toEntity(Customer customer)  {
        try {
            return CustomerEntity.builder()
                    .firstName(customer.getFirstName())
                    .middleName(customer.getMiddleName())
                    .lastName(customer.getLastName())
                    .documentId(customer.getDocumentId())
                    .birthday(customer.getBirthday())
                    .gender(customer.getGender().getValue())
                    .customerType(customer.getCustomerType())
                    .officeEmail(customer.getOfficeEmail())
                    .personalEmail(customer.getPersonalEmail())
                    .mobileNumber(customer.getMobileNumber())
                    .officeNumber(customer.getOfficeNumber())
                    .familyMembers(new ObjectMapper().writeValueAsString(customer.getFamilyMembers()))
                    .address(new ObjectMapper().writeValueAsString(customer.getAddress()))
                    .build();
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Json processing exception occurred: " + customer.getCustomerId());
        }
    }
}
