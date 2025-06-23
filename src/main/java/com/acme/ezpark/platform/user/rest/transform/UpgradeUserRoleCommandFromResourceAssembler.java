package com.acme.ezpark.platform.user.interfaces.rest.transform;

import com.acme.ezpark.platform.user.domain.model.commands.UpgradeUserRoleCommand;
import com.acme.ezpark.platform.user.interfaces.rest.resources.UpgradeUserRoleResource;


public class UpgradeUserRoleCommandFromResourceAssembler {
    
    public static UpgradeUserRoleCommand toCommandFromResource(UpgradeUserRoleResource resource) {
        return new UpgradeUserRoleCommand(
            resource.email(),
            resource.requestedRole()
        );
    }
}
