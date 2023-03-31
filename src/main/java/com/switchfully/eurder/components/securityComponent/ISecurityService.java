package com.switchfully.eurder.components.securityComponent;

import java.util.UUID;

public interface ISecurityService {

    UUID getCustomerUUIDFromAuth(String auth);
}
