package org.activityinfo.server.endpoint.rest;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GravatarService {

    private static final Logger LOGGER = Logger.getLogger(GravatarService.class.getName());

    private static final MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

    public static String getAvatar(String emailAddress) {
        String avatarKey = "avatar:" + emailAddress;
        String dataUrl = (String) memcacheService.get(avatarKey);
        if(dataUrl != null) {
            return dataUrl;
        }

        // First try gravatar to see if the user has a profile pic
        String gravatarUri = fetchGravatar(emailAddress).orElse("");

        memcacheService.put(avatarKey, gravatarUri);

        return gravatarUri;
    }

    /**
     * Fetches an avatar image from Gravatar as a data uri
     */
    private static Optional<String> fetchGravatar(String emailAddress) {
        URLFetchService fetchService = URLFetchServiceFactory.getURLFetchService();
        try {
            HTTPResponse response = fetchService.fetch(new URL(gravatarUrl(emailAddress) + "?d=404"));
            if(response.getResponseCode() == 200) {
                Optional<String> contentType = response
                        .getHeaders()
                        .stream()
                        .filter(h -> h.getName().equalsIgnoreCase("Content-Type"))
                        .findAny()
                        .map(h -> h.getValue());

                return contentType.map(type -> dataUri(type, response.getContent()));
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to fetch gravatar", e);
        }
        return Optional.empty();
    }

    public static String gravatarUrl(String emailAddress) {
        return "https://www.gravatar.com/avatar/" + emailHash(emailAddress);
    }

    private static String dataUri(String contentType, byte[] content) {
        return "data:" + contentType + ";base64," + BaseEncoding.base64().encode(content);
    }


    /**
     *
     * @see <a href="https://en.gravatar.com/site/implement/hash/">Gravatar reference</a>
     * @param emailAddress the email address to hash
     * @return a
     */
    @SuppressWarnings("deprecation")
    static String emailHash(String emailAddress) {
        return Hashing.md5().hashString(emailAddress.trim().toLowerCase(), Charsets.UTF_8).toString();
    }

}
