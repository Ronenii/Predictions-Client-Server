package properties.property.random.value.impl;

import properties.property.random.value.api.AbstractRndValueGen;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringRndValueGen extends AbstractRndValueGen <String> {

    final String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789().-_,?! ";

    /**
     * Using 'AbstractRndValueGen' random member. random.nextInt(50) + 1 generates an integer from 1 to 50. The return value is
     * created using streams and composed of the allowed characters specified above.
     */
    @Override
    public String generateRandomValue() {
        int valueLength = random.nextInt(50) + 1;
        return IntStream.range(0, valueLength)
                .mapToObj(i -> allowedCharacters.charAt(random.nextInt(allowedCharacters.length())))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
