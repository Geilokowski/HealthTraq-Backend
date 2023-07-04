package iu.study.healthtraq.utils;

import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomStringUtils {
    public static Optional<String> getResponseBodyString(@NotNull Response response) throws IOException {
        if(response.body() == null)
            return Optional.empty();
        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.getBuffer();
        return Optional.of(buffer.clone().readString(StandardCharsets.UTF_8));
    }

    public static Optional<Integer> tryParseInteger(String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getFirstGroup(String str, String regex){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);

        if(m.find() && m.groupCount() > 0){
            return Optional.ofNullable(m.group(1));
        }else{
            return Optional.empty();
        }
    }
}
