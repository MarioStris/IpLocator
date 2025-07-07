package org.locator.iplocator.validator;

import org.locator.iplocator.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class IpValidatorImpl implements IpValidator {
    @Override
    public void validateIp(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            throw new ValidationException("IP address cannot be null or empty");
        }

        String trimmedIp = ip.trim();

        if (!isValidIPv4(trimmedIp) && !isValidIPv6(trimmedIp)) {
            throw new ValidationException("Invalid IP address format: " + trimmedIp);
        }
    }

    private boolean isValidIPv4(String ip) {
        String ipv4Regex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipv4Regex);
    }

    private boolean isValidIPv6(String ip) {
        String ipv6Regex = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|" +
                "^::1$|" +
                "^::$";
        return ip.matches(ipv6Regex);
    }

    @Override
    public void validateProvider(String provider) {
        if (Objects.isNull(provider)) {
            throw new ValidationException("Provider cannot be null");
        }
    }
}
