package com.airBnb.project.AirBnbWebApp.service;


import com.airBnb.project.AirBnbWebApp.entity.Booking;
import com.airBnb.project.AirBnbWebApp.entity.User;
import com.airBnb.project.AirBnbWebApp.repository.BookingRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
@Slf4j
public class CheckoutServiceImp implements CheckoutService{

   private final BookingRepository bookingRepository;




    @Override
    public String getCheckoutSession(Booking booking, String successUrl, String failureUrl) {
     log.info("creating session for booking with id{}",booking.getCreatedAt());
        User user = (User)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            CustomerCreateParams customerPrams = CustomerCreateParams.builder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();

            Customer customer = Customer.create(customerPrams);

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                   .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L) // Always 1 for a single booking
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) // Convert to paisa
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(booking.getHotel().getName())
                                                                    .setDescription("Booking ID: "+booking.getId())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();



            Session session = Session.create(params);

            booking.setPaymentSessionId(session.getId());
            bookingRepository.save(booking);
            log.info("session created successfully for booking with id{}",booking.getCreatedAt());
            return session.getUrl();
        }catch (StripeException e){
            throw new RuntimeException(e);
        }
    }
}
