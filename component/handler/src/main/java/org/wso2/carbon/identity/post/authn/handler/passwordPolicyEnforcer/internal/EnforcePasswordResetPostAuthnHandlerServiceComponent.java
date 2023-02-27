/*
 * Copyright (c) 2023, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.post.authn.handler.passwordPolicyEnforcer.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.application.authentication.framework.handler.request.PostAuthenticationHandler;
import org.wso2.carbon.identity.post.authn.handler.passwordPolicyEnforcer.EnforcePasswordResetAuthenticationHandler;
import org.wso2.carbon.user.core.service.RealmService;

@Component(
        name = "identity.post.authn.handler.resetPasswordEnforcer",
        immediate = true
)
public class EnforcePasswordResetPostAuthnHandlerServiceComponent {

    private static final Log log = LogFactory.getLog(EnforcePasswordResetPostAuthnHandlerServiceComponent.class);

    @Activate
    protected void activate(ComponentContext context) {

        try {
            EnforcePasswordResetAuthenticationHandler enforcePasswordResetAuthenticationHandler =
                    new EnforcePasswordResetAuthenticationHandler();
            context.getBundleContext().registerService(PostAuthenticationHandler.class.getName(),
                    enforcePasswordResetAuthenticationHandler, null);

        } catch (Throwable e) {
            log.error("Error while activating EnforcePasswordResetAuthenticationHandler.", e);
        }
    }

    public static RealmService getRealmService() {
        return EnforcePasswordResetPostAuthenticationHandlerDataHolder.getInstance().getRealmService();
    }

    @Reference(
            name = "user.realmservice.default",
            service = RealmService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRealmService"
    )
    protected void setRealmService(RealmService realmService) {

        EnforcePasswordResetPostAuthenticationHandlerDataHolder.getInstance().setRealmService(realmService);
        if (log.isDebugEnabled()) {
            log.debug("RealmService is set in the User Store Count bundle");
        }
    }

    protected void unsetRealmService(RealmService realmService) {

        EnforcePasswordResetPostAuthenticationHandlerDataHolder.getInstance().setRealmService(null);
        if (log.isDebugEnabled()) {
            log.debug("RealmService is unset in the Application Authentication Framework bundle");
        }
    }
}

