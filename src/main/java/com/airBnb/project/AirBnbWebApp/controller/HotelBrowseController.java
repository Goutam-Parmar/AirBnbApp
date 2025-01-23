package com.airBnb.project.AirBnbWebApp.controller;
import com.airBnb.project.AirBnbWebApp.dto.HotelDto;
import com.airBnb.project.AirBnbWebApp.dto.HotelInfoDto;
import com.airBnb.project.AirBnbWebApp.dto.HotelPriceDto;
import com.airBnb.project.AirBnbWebApp.dto.HotelSearchRequest;
import com.airBnb.project.AirBnbWebApp.service.HotelService;
import com.airBnb.project.AirBnbWebApp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest){
      var page =  inventoryService.searchHotels(hotelSearchRequest);
      return  ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }


}
