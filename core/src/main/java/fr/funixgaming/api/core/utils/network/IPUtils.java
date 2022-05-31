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

    private final InetAddress[] whitelist;
    private final ApiConfig apiConfig;

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

    public boolean canAccess(final String ip) throws ApiException {
        if (apiConfig.isDisableWhitelist()) {
            return true;
        }

        try {
            final InetAddress inetAddress = InetAddress.getByName(ip);

            return isIpLocalAddress(inetAddress) || isWhitelisted(inetAddress);
        } catch (UnknownHostException e) {
            throw new ApiException(String.format("L'adresse ip entrée %s est invalide.", ip), e);
        }
    }

    private boolean isIpLocalAddress(final InetAddress ip) throws ApiException {
        return ip.isAnyLocalAddress() || ip.isLoopbackAddress();
    }

    private boolean isWhitelisted(final InetAddress ip) throws ApiException {
        for (final InetAddress inetAddress : whitelist) {
            if (inetAddress.equals(ip)) {
                return true;
            }
        }
        return false;
    }

    public String getClientIp(final HttpServletRequest request) {
        final String remoteAddress;
        String addressHeader = request.getHeader("X-FORWARDED-FOR");

        if (!this.apiConfig.isProxied() && Strings.isEmpty(addressHeader)) {
            remoteAddress = request.getRemoteAddr();
        } else {
            final String[] addresses = addressHeader.split(",");
            remoteAddress = addresses[0].replaceAll(" ", "");
        }

        return remoteAddress;
    }

}
