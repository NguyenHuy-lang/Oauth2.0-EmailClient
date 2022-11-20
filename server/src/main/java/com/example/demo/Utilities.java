// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.demo;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;


import com.microsoft.graph.authentication.BaseAuthenticationProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class Utilities {
    private Utilities() {
        throw new IllegalStateException("Utility class. Don't instantiate");
    }
    public static Map<String,String> filterClaims(OidcUser principal) {
        final String[] claimKeys = {"sub", "aud", "ver", "iss", "name", "oid", "preferred_username"};
        final List<String> includeClaims = Arrays.asList(claimKeys);

        Map<String,String> filteredClaims = new HashMap<>();
        includeClaims.forEach(claim -> {
            if (principal.getIdToken().getClaims().containsKey(claim)) {
                filteredClaims.put(claim, principal.getIdToken().getClaims().get(claim).toString());
            }
        });
        return filteredClaims;
    }

    public static ArrayList<String> graphUserProperties(OAuth2AuthorizedClient graphAuthorizedClient) {
        final GraphServiceClient graphServiceClient = Utilities.getGraphServiceClient(graphAuthorizedClient);
        final User user = graphServiceClient.me().buildRequest().get();
        ArrayList<String>userProperties = new ArrayList<>();
        GraphAuthenticationProvider graphAuthenticationProvider = new GraphAuthenticationProvider(graphAuthorizedClient);


        if (user == null) {
            userProperties.add("GraphSDK returned null User object.");
        } else {
            userProperties.add(user.displayName);
            userProperties.add(user.mobilePhone);
            userProperties.add(user.city);
            userProperties.add(user.givenName);
            userProperties.add(graphAuthenticationProvider.getAccessToken());

        }
        return userProperties;
    }


    public static GraphServiceClient getGraphServiceClient(OAuth2AuthorizedClient graphAuthorizedClient) {
        GraphAuthenticationProvider graphAuthenticationProvider = new GraphAuthenticationProvider(graphAuthorizedClient);
        return GraphServiceClient.builder().authenticationProvider(graphAuthenticationProvider)
                .buildClient();
    }
    private static class GraphAuthenticationProvider
            extends BaseAuthenticationProvider {

        private OAuth2AuthorizedClient graphAuthorizedClient;
        public GraphAuthenticationProvider(OAuth2AuthorizedClient graphAuthorizedClient) {
            this.graphAuthorizedClient = graphAuthorizedClient;
        }

        @Override
        public CompletableFuture<String> getAuthorizationTokenAsync(final URL requestUrl){
            return CompletableFuture.completedFuture(graphAuthorizedClient.getAccessToken().getTokenValue());
        }

        public String getAccessToken() {
            return graphAuthorizedClient.getAccessToken().getTokenValue().toString();
        }
    }
}