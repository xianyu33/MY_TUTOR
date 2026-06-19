package com.yy.my_tutor.payment.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnualLicenseSeatUsageDTO {
    private Integer totalSeats;
    private Integer usedSeats;
    private Integer availableSeats;
    private List<OrderUsageDTO> orders = new ArrayList<>();

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OrderUsageDTO {
        private Integer orderId;
        private String orderNo;
        private String productName;
        private Integer quantity;
        private Integer used;
        private Integer available;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date paidAt;
        private Long unitAmount;
        private String currency;
        private List<ActivatedStudentDTO> activatedStudents = new ArrayList<>();
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ActivatedStudentDTO {
        private Integer studentId;
        private String studentName;
        private String studentAccount;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date activatedAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expireAt;
    }
}
