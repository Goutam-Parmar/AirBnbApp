package com.airBnb.project.AirBnbWebApp.service;

import com.airBnb.project.AirBnbWebApp.entity.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl , String failureUrl);

}
