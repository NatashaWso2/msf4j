/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.msf4j.example;

import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.wso2.carbon.messaging.Headers;
import org.wso2.msf4j.Request;
import org.wso2.msf4j.Response;
import org.wso2.msf4j.interceptor.RequestInterceptor;

/**
 * Interceptor for handling custom JWT claims.
 */
public class CustomJWTClaimsInterceptor implements RequestInterceptor {

    private static final String JWT_HEADER = "X-JWT-Assertion";
    private static final String AUTH_TYPE_JWT = "JWT";

    @Override
    public boolean interceptRequest(Request request, Response response) throws Exception {
        Headers headers = request.getHeaders();
        if (headers != null) {
            String jwtHeader = headers.get(JWT_HEADER);
            if (jwtHeader != null) {
                SignedJWT signedJWT = SignedJWT.parse(jwtHeader);
                ReadOnlyJWTClaimsSet readOnlyJWTClaimsSet = signedJWT.getJWTClaimsSet();
                if (readOnlyJWTClaimsSet != null) {
                    // Do something with claims
                    return true;
                }
            }
        }
        response.setHeader(javax.ws.rs.core.HttpHeaders.WWW_AUTHENTICATE, AUTH_TYPE_JWT);
        response.setStatus(javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode());
        return false;
    }
}
