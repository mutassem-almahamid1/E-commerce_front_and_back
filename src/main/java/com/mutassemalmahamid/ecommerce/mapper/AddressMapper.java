package com.mutassemalmahamid.ecommerce.mapper;

import com.mutassemalmahamid.ecommerce.mapper.helper.AssistantHelper;
import com.mutassemalmahamid.ecommerce.model.document.Address;
import com.mutassemalmahamid.ecommerce.model.dto.request.AddressRequest;
import com.mutassemalmahamid.ecommerce.model.dto.response.AddressResponse;

import java.time.LocalDateTime;

public class AddressMapper {

    public static Address toEntity(AddressRequest addressRequest) {
        return Address.builder()
                .street(AssistantHelper.trimString(addressRequest.getStreet()))
                .city(AssistantHelper.trimString(addressRequest.getCity()))
                .country(AssistantHelper.trimString(addressRequest.getCountry()))
                .zipCode(AssistantHelper.trimString(addressRequest.getZipCode()))
                .build();
    }

    public static AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .country(address.getCountry())
                .zipCode(address.getZipCode())
                .build();
    }

}
