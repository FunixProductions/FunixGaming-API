package fr.funixgaming.api.core.utils.network;

import fr.funixgaming.api.core.config.ApiConfig;
import fr.funixgaming.api.core.exceptions.ApiException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class IPUtils {

    public static final String HEADER_X_FORWARDED = "X-FORWARDED-FOR";

    private final InetAddress[] whitelist;
    private final ApiConfig apiConfig;

    /**
     * Init the IPUtils with the whitelist
     * @param apiConfig config to set up ip tools
     * @throws ApiException if the whitelist is not valid
     */
    public IPUtils(ApiConfig apiConfig) throws ApiException {
        final String[] listIp = apiConfig.getIpWhitelist();
        this.whitelist = new InetAddress[listIp.length];
        this.apiConfig = apiConfig;

        for (int i = 0; i < listIp.length; ++i) {
            try {
                this.whitelist[i] = InetAddress.getByName(listIp[i]);
            } catch (UnknownHostException e) {
                throw new ApiException(String.format("L'adresse ip entrée %s est invalide.", listIp[i]), e);
            }
        }
    }

    /**
     * Can access to the API. If is present in whitelist
     * @param ip ip string fetrched from request
     * @return bool access or not
     * @throws ApiException if ip is not valid
     */
    public boolean canAccess(final String ip) throws ApiException {
        if (apiConfig.isDisableWhitelist()) {
            return false;
        }

        try {
            final InetAddress inetAddress = InetAddress.getByName(ip);

            if (isIpLocalAddress(inetAddress)) {
                return true;
            } else {
                return isWhitelisted(inetAddress);
            }
        } catch (UnknownHostException e) {
            throw new ApiException(String.format("L'adresse ip entrée %s est invalide.", ip), e);
        }
    }

    /**
     * Get the client IP address from the request.
     * @param request http request to fetch ip
     * @return IP in string
     */
    public String getClientIp(final HttpServletRequest request) {
        final String addressHeader = request.getHeader(HEADER_X_FORWARDED);
        final String remoteAddress;

        if (!this.apiConfig.isProxied()) {
            remoteAddress = request.getRemoteAddr();
        } else {
            if (Strings.isEmpty(addressHeader)) {
                remoteAddress = request.getRemoteAddr();
            } else {
                final String[] addresses = addressHeader.split(",");
                remoteAddress = addresses[0].replaceAll(" ", "");
            }
        }

        return remoteAddress;
    }

    private boolean isIpLocalAddress(final InetAddress ip) throws ApiException {
        return ip.isLoopbackAddress() || ip.isSiteLocalAddress();
    }

    private boolean isWhitelisted(final InetAddress ip) throws ApiException {
        for (final InetAddress inetAddress : whitelist) {
            if (inetAddress.equals(ip)) {
                return true;
            }
        }
        return false;
    }

}
