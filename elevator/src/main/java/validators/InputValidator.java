package validators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static constants.Constants.*;
import static constants.Messages.*;

@SuppressWarnings("all")
public final class InputValidator {

    private static final Logger LOGGER = LogManager.getLogger();

    private InputValidator() {
    }

    public static Map<String, Integer> getIntProperties(String configFileName) {
        Optional<Properties> properties = getProperties(configFileName);

        return properties.map(InputValidator::validateProperties).get();
    }

    private static Optional<Properties> getProperties(String propertiesFile) {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(propertiesFile);

        try {
            properties.load(stream);

            return Optional.of(properties);
        } catch (IOException e) {
            LOGGER.error(String.format(ERROR_LOADING_RESOURCES_MESSAGE, propertiesFile));

            return Optional.empty();
        } catch (NullPointerException e) {
            LOGGER.error(String.format(FILE_NOT_FOUND_MESSAGE, propertiesFile));

            return Optional.empty();
        }
    }

    private static Map<String, Integer> validateProperties(Properties properties) {
        return REQUIRED_PARAMETERS
                .stream()
                .collect(
                        Collectors.toMap(
                                propertyName -> propertyName,
                                propertyName -> {
                                    String propertyValue = properties
                                            .computeIfAbsent(propertyName,
                                                    DEFAULT_PARAMETERS_MAP::get)
                                            .toString();
                                    return validatePropertyRange(propertyName, propertyValue);
                                }
                        )
                );
    }

    private static int validatePropertyRange(String propertyName, String propertyValue) {
        boolean isInputInteger = Pattern
                .compile(REG_EXP_FOR_NON_NEGATIVE_INTEGER_NUMBERS)
                .matcher(propertyValue)
                .matches();

        if (isInputInteger && Integer.valueOf(propertyValue)
                >= DEFAULT_PARAMETERS_MAP.get(propertyName)) {

            return Integer.valueOf(propertyValue);
        } else {
            LOGGER.error(String.format(INCORRECT_PROPERTY_VALUE, propertyName, propertyValue));

            return DEFAULT_PARAMETERS_MAP.get(propertyName);
        }
    }
}
